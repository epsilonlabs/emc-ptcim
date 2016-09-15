/*******************************************************************************
 * Copyright (c) 2016 University of York
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Hoacio Hoyos Rodriguez - Initial API and implementation
 *******************************************************************************/
package org.eclipse.epsilon.emc.ptcim;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.emc.ptcim.ole.COMBridge;
import org.eclipse.epsilon.emc.ptcim.ole.COMModel;
import org.eclipse.epsilon.emc.ptcim.ole.COMObject;
import org.eclipse.epsilon.emc.ptcim.ole.COMPropertyManager;
import org.eclipse.epsilon.emc.ptcim.ole.EpsilonCOMException;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.exceptions.models.EolEnumerationValueNotFoundException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelElementTypeNotFoundException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.exceptions.models.EolNotInstantiableModelElementTypeException;
import org.eclipse.epsilon.eol.execute.introspection.IPropertyGetter;
import org.eclipse.epsilon.eol.execute.introspection.IPropertySetter;
import org.eclipse.epsilon.eol.models.CachedModel;
import org.eclipse.epsilon.eol.models.IRelativePathResolver;

/**
 * The Class PtcimModel provides the EMC access to PTC Intregity Modeler models
 */
public class PtcimModel extends CachedModel<COMObject> {
	
	public static final String ATT_CLASS = "class";

	/** The Constant COM_ID. */
	public static final String COM_ID = "org.eclipse.epsilon.emc.artisan.COM";
	
	/** The Constant BRIDGE_INDEX. */
	private static final int BRIDGE_INDEX = 0;
	
	/** The Constant GETTER_INDEX. */
	private static final int GETTER_INDEX = 1;
	
	/** The Constant MANAGER_INDEX. */
	private static final int MANAGER_INDEX = 2;
	
	/** The Constant SETTER_INDEX. */
	private static final int SETTER_INDEX = 3;

	public static final String PROPERTY_MODEL_REFERENCE = "model_ref";

	public static final String PROPERTY_SERVER_NAME = "server";

	public static final String PROPERTY_REPOSITORY_NAME = "repository";

	public static final String PROPERTY_VERSION_NUMBER = "version";
	
	
	/** The bridge. */
	protected COMBridge<COMObject, COMObject> bridge;
	
	/**  The Project, needed for type testing and instantiation. */ 
	private COMObject theProject;
	
	/** The is initialized. */
	private boolean isInitialized = false;
	
	/**  The Artisan Model. */
	private COMModel model = null;
	
	
	/** The property getter. */
	private IPropertyGetter propertyGetter;

	/** The property setter. */
	private IPropertySetter propertySetter;
	
	/** The property manager. */
	private COMPropertyManager propertyManager;
	
	/**  The Artisan Studio. */
	private COMObject studio = null;
	private boolean connectedToStudio = false;

	private String modelId;

	private String server;

	private String repository;

	private String version;

	/**
	 * Instantiates a new artisan model. Gets the COM helpers from the extension
	 */
	public PtcimModel() {
		// FIXME USe the Extension point to get the jawin classes
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IExtensionPoint ep = reg.getExtensionPoint(COM_ID);
		IExtension[] extensions = ep.getExtensions();
		// There should only be one contributor
		IExtension ext = extensions[0];
		IConfigurationElement[] ce = ext.getConfigurationElements();
    	try {
			bridge = (COMBridge<COMObject, COMObject>) ce[BRIDGE_INDEX].createExecutableExtension(ATT_CLASS);
			setPropertyGetter((IPropertyGetter) ce[GETTER_INDEX].createExecutableExtension(ATT_CLASS));
			setPropertyManager(((COMPropertyManager) ce[MANAGER_INDEX].createExecutableExtension(ATT_CLASS)).getInstance());
			setPropertySetter((IPropertySetter) ce[SETTER_INDEX].createExecutableExtension(ATT_CLASS));
		} catch (CoreException e) {
			throw new IllegalStateException(e);
		}
    	cachingEnabled = false;
    	/*
		 * Set objManager = CreateObject("Studio.ModelManager")
		 * objManager.AddModel("\\Enabler\MyServer\MyRepository","MyModel")
		 */
		if (!isInitialized()) {
			try {
				bridge.initialiseCOM();
			} catch (EpsilonCOMException e) {
				throw new IllegalStateException(e);
			}
		}
		isInitialized = true;
	}


	private void abortTransaction() throws EpsilonCOMException {
		List<Object> args = new ArrayList<Object>();
		args.add("Transaction");
		args.add(0);
		args.add("Abort");
		theProject.invoke("PropertySet", args);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.CachedModel#allContentsFromModel()
	 */
	@Override
	protected Collection<COMObject> allContentsFromModel() {
		assert model != null;
		Collection<? extends COMObject> elements;
		List<Object> args = new ArrayList<Object>();
		args.add("");
		try {
			COMObject res = (COMObject) model.invoke("Items", args);
			elements = res.wrapInColleciton(model, "");
		} catch (EpsilonCOMException e) {
			throw new IllegalStateException(e);
		}
		return (Collection<COMObject>) elements;
	}

	private void beginTransaction() throws EpsilonCOMException {
		List<Object> args = new ArrayList<Object>();
		args.add("Transaction");
		args.add(0);
		args.add("Begin");
		theProject.invoke("PropertySet", args);
	}
	
	private void commitTransaction() throws EpsilonCOMException {
		List<Object> args = new ArrayList<Object>();
		args.add("Transaction");
		args.add(0);
		args.add("Commit");
		theProject.invoke("PropertySet", args);
	}

	/**
	 * Connect to artisan studio.
	 *
	 * @throws EpsilonCOMException the epsilon COM exception
	 */
	public void connectToStudio() throws EpsilonCOMException {
		if (!connectedToStudio ) {
			studio = bridge.connectToCOM("Studio.Editor");
			connectedToStudio = true;
		}
	}

	/**
	 * Creates a new instances in a specific parent and association.
	 * In Artisan, elements created through the API can not be moved from the parent 
	 * in which they where created. This method allows to create elements in a 
	 * specific hierarchy. For Add operations the parameters must be the parent object.
	 * The type must match the name of the association. For AddByType operations,
	 * the parameters must be the parent object, and the association. 
	 */
	@Override
	public Object createInstance(String type, Collection<Object> parameters)
			throws EolModelElementTypeNotFoundException, EolNotInstantiableModelElementTypeException {
		
		if ((parameters == null) || parameters.isEmpty()) {
			return super.createInstance(type);
		}
		Object newInstance = null;
		Iterator<Object> it = parameters.iterator();
		Object parent = it.next();
		COMObject comParent = (COMObject) parent;
		List<Object> args = new ArrayList<Object>();
		args.add(type);
		if (parameters.size() == 1) {		// Add
			try {
				newInstance = comParent.invoke("Add", args);
				setNewInstanceId(newInstance);
			} catch (EpsilonCOMException e) {
				throw new EolModelElementTypeNotFoundException(this.name, type);
			}
		}
		else {								// AddByType
			Object association = it.next();
			args.add(association);
			try {
				newInstance = comParent.invoke("AddByType", args);
				setNewInstanceId(newInstance);
			} catch (EpsilonCOMException e) {
				throw new EolModelElementTypeNotFoundException(this.name, type);
			}
		}
		return newInstance;
	}

	/**
	 * In Artisan the type is the same name as the association name in the model (dictionary)
	 * Specialised classes are obtained by using attribute settings (see AddByType).
	 *
	 * @param type the type
	 * @return the COM object
	 * @throws EolModelElementTypeNotFoundException the eol model element type not found exception
	 * @throws EolNotInstantiableModelElementTypeException the eol not instantiable model element type exception
	 */
	@Override
	protected COMObject createInstanceInModel(String type)
			throws EolModelElementTypeNotFoundException, EolNotInstantiableModelElementTypeException {
		if (!isInstantiable(type)) {
			throw new EolNotInstantiableModelElementTypeException(getName(), type);
		}
		List<Object> args = new ArrayList<Object>();
		args.add(type);
		Object newInstance = null;
		try {
			newInstance = model.invoke("Add", args);
			setNewInstanceId(newInstance);
		} catch (EpsilonCOMException e) {
			throw new EolModelElementTypeNotFoundException(getName(), type);
		}
		return (COMObject) newInstance;
	}
	
	
	/**
	 * Deletes an object from the model.
	 * Important:  If you delete an object and that object is linked to other objects through
	 * automation interface associations that have their Propagate Delete flag set to TRUE,
	 * objects that are linked through those associations will also be deleted. For example,
	 * a Class is related to its child Attributes through the Attribute association, which has
	 * its Propagate Delete flag set to TRUE. If you delete a Class, its Attributes will also
	 * be deleted.
	 */
	@Override
	protected boolean deleteElementInModel(Object instance) throws EolRuntimeException {
		assert instance instanceof COMObject;
		boolean success = false;
		try {
			((COMObject) instance).invoke("Delete");
			success = true;
		} catch (EpsilonCOMException e) {
			throw new EolRuntimeException(e.getMessage());
		}
		return success;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.CachedModel#disposeModel()
	 */
	@Override
	protected void disposeModel() {
		propertyManager.dispose();
		if (isInitialized()) {
			if (!storeOnDisposal) {
				try {
					abortTransaction();
				} catch (EpsilonCOMException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				bridge.uninitialiseCOM();
			} catch (EpsilonCOMException e) {
				// FIXME Does Epsilon has a logger for this?
				e.printStackTrace();
			}
		}
	}

	/**
	 * Artisan does not provide support for all of kind, so for this models this method
	 * delegates to {@link #getAllOfTypeFromModel(String)}.
	 */
	@Override
	public Collection<COMObject> getAllOfKind(String kind) throws EolModelElementTypeNotFoundException {
		return getAllOfType(kind);
	}
	
	@Override
	protected Collection<? extends COMObject> getAllOfKindFromModel(String kind)
			throws EolModelElementTypeNotFoundException {
		throw new UnsupportedOperationException("Artisan models don't use cache.");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.CachedModel#getAllOfType(java.lang.String)
	 */
	@Override
	public Collection<COMObject> getAllOfType(String type) throws EolModelElementTypeNotFoundException {
		assert model != null;
		List<? extends COMObject> elements;
		List<Object> args = new ArrayList<Object>();
		args.add(type);
		List<Object> byRefArgs = new ArrayList<Object>();
		byRefArgs.add("*");
		COMObject res;
		try {
			res = (COMObject) model.invoke("Items", args);	//, byRefArgs);
		} catch (EpsilonCOMException e) {
			throw new EolModelElementTypeNotFoundException(name, type);
		}
		elements = res.wrapInColleciton(model, type);	//new JawinCollection(res, model, type);
		return (List<COMObject>) elements;
	}


	@Override
	protected Collection<? extends COMObject> getAllOfTypeFromModel(String type)
			throws EolModelElementTypeNotFoundException {
		throw new UnsupportedOperationException("Artisan models don't use cache.");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.CachedModel#getAllTypeNamesOf(java.lang.Object)
	 */
	@Override
	protected Collection<String> getAllTypeNamesOf(Object instance) {
		String type = getTypeNameOf(instance);
		ArrayList<String> res = new ArrayList<String>();
		res.add(type);
		return res;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.CachedModel#getCacheKeyForType(java.lang.String)
	 */
	@Override
	protected Object getCacheKeyForType(String type) throws EolModelElementTypeNotFoundException {
		return type;
	}

	public COMModel getCOMModel() {
		return model;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.IModel#getElementById(java.lang.String)
	 */
	@Override
	public Object getElementById(String id) {
		List<Object> args = new ArrayList<Object>();
		args.add(id);
		COMObject res = null;
		try {
			res = (COMObject) theProject.invoke("ItemById", args);
			if (res != null)
				res.setId(id);
		} catch (EpsilonCOMException e) {
			// FIXME Log me!
			e.printStackTrace();
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.IModel#getElementId(java.lang.Object)
	 */
	@Override
	public String getElementId(Object instance) {
		assert instance instanceof COMObject;
		String id = ((COMObject) instance).getId();
		if (id == null)
		{
			List<Object> args = new ArrayList<Object>();
			args.add("Id");
			try {
				id = (String) ((COMObject) instance).get("Property", args);
				((COMObject) instance).setId(id);
			} catch (EpsilonCOMException e) {
				// FIXME Log me!
				throw new IllegalStateException(e);
			}
		}
		return id;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.IModel#getEnumerationValue(java.lang.String, java.lang.String)
	 */
	@Override
	public Object getEnumerationValue(String enumeration, String label) throws EolEnumerationValueNotFoundException {
		throw new UnsupportedOperationException("Artisan Model does not support enumerations");
	}
	
	/**
	 * Get the Id of the model used for the PTC IM repository
	 */
	public String getModelId() {
		return modelId;
	}
	
	

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.Model#getPropertyGetter()
	 */
	@Override
	public IPropertyGetter getPropertyGetter() {
		return propertyGetter;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.Model#getPropertySetter()
	 */
	@Override
	public IPropertySetter getPropertySetter() {
		return propertySetter;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.IModel#getTypeNameOf(java.lang.Object)
	 */
	@Override
	public String getTypeNameOf(Object instance) {
		assert instance instanceof COMObject;
		String typeName;
		try {
			typeName = (String) ((COMObject) instance).get("Property", "Type");
		} catch (EpsilonCOMException e) {
			// TODO Auto-generated catch block
			throw new IllegalArgumentException(e);
		}
		return typeName;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.IModel#hasType(java.lang.String)
	 */
	@Override
	public boolean hasType(String type) {
		List<Object> args = new ArrayList<Object>();
		args.add(type);
		String errDispPtr = "";
		try {
			errDispPtr = (String) theProject.invoke("GetClassProperties", args);
		} catch (EpsilonCOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return errDispPtr.length() > 0;
	}

	/**
	 * @return the isInitialized
	 */
	public boolean isInitialized() {
		return isInitialized;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.IModel#isInstantiable(java.lang.String)
	 */
	@Override
	public boolean isInstantiable(String type) {
		// TODO I think all types in the Artisan metamodel are instatiatable, we would need to make
		// sure that the name is actually valid. 
		return hasType(type);
	}

	@Override
	public boolean isOfKind(Object instance, String metaClass) throws EolModelElementTypeNotFoundException {
		return isOfType(instance, metaClass);
	}
	
	@Override
	public boolean isOfType(Object instance, String metaClass) throws EolModelElementTypeNotFoundException {
		assert instance instanceof COMObject;
		String type = getTypeNameOf(instance);
		return metaClass.equals(type);
		
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.Model#knowsAboutProperty(java.lang.Object, java.lang.String)
	 */
	@Override
	public boolean knowsAboutProperty(Object instance, String property) {
		Object p = null;
		if (instance instanceof COMObject) {
			p = propertyManager.getProperty((COMObject) instance, property);
		}
		return p != null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.CachedModel#load(org.eclipse.epsilon.common.util.StringProperties, org.eclipse.epsilon.eol.models.IRelativePathResolver)
	 */
	@Override
	public void load(StringProperties properties, IRelativePathResolver resolver) throws EolModelLoadingException {
		super.load(properties, resolver);
		modelId = properties.getProperty(PROPERTY_MODEL_REFERENCE);
		server = properties.getProperty(PROPERTY_SERVER_NAME);
		repository = properties.getProperty(PROPERTY_REPOSITORY_NAME);
		version = properties.getProperty(PROPERTY_VERSION_NUMBER);
		load();
	}
	
	
	
	public void loadDictionary() throws EolModelLoadingException {
		//List<Object> args = new ArrayList<Object>();
		//args.add("Dictionary");
		List<Object> byRefArgs = new ArrayList<Object>();
		byRefArgs.add("Dictionary");
		byRefArgs.add("Dictionary");
		try {
			COMObject res = (COMObject) theProject.invoke("Item", byRefArgs);
			model = bridge.wrapModel(res);
		} catch (EpsilonCOMException e) {
			try {
				bridge.uninitialiseCOM();
			} catch (EpsilonCOMException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			throw new EolModelLoadingException(e, this);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.CachedModel#loadModel()
	 */
	@Override
	protected void loadModel() throws EolModelLoadingException {
		if (isInitialized()) {
			if  (readOnLoad) {
				COMObject artisanApp;
				try {
					artisanApp = bridge.connectToCOM("OMTE.Projects");
				} catch (EpsilonCOMException e) {
					throw new EolModelLoadingException(e, this);
				}
				try {
					if (server.length() == 0) {
						theProject = bridge.openModel(artisanApp, modelId);
					}
					else {
						theProject = bridge.openModel(artisanApp, modelId, server, repository, version);
					}
				} catch (EpsilonCOMException e) {
					throw new EolModelLoadingException(e, this);
				}
			}
			else if (storeOnDisposal) {
				try {
					PtcimModelFactory.createModel(this, server, repository, getName());
				} catch (EolRuntimeException e) {
					throw new EolModelLoadingException(e, this);
				}
			}
			try {
				beginTransaction();
			} catch (EpsilonCOMException e) {
				throw new EolModelLoadingException(e, this);
			}
			loadDictionary();
		}
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.IModel#owns(java.lang.Object)
	 */
	@Override
	public boolean owns(Object instance) {
		if (instance instanceof COMObject) {
			if (((COMObject) instance).getId() == null) {
				throw new IllegalStateException("COMObjects can not be found without an Id");
			}
			Object other = getElementById(((COMObject) instance).getId());
			return other != null;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.IModel#setElementId(java.lang.Object, java.lang.String)
	 */
	@Override
	public void setElementId(Object instance, String newId) {
		throw new UnsupportedOperationException("Artisan objects Ids are read only.");
		
	}

	/**
	 * Sets the model.
	 *
	 * @param model the new model
	 */
	protected void setModel(COMModel model) {
		this.model = model;
	}

	/**
	 * @param newInstance
	 * @throws EpsilonCOMException
	 */
	private void setNewInstanceId(Object newInstance) throws EpsilonCOMException {
		String id = (String) ((COMObject) newInstance).get("Property", "Id");
		((COMObject) newInstance).setId(id);
	}

	/**
	 * Sets the property getter.
	 *
	 * @param propertyGetter the new property getter
	 */
	public void setPropertyGetter(IPropertyGetter propertyGetter) {
		this.propertyGetter = propertyGetter;
	}
	
	private void setPropertyManager(COMPropertyManager manager) {
		this.propertyManager = manager;
	}

	/**
	 * Sets the property setter.
	 *
	 * @param propertySetter the new property setter
	 */
	public void setPropertySetter(IPropertySetter propertySetter) {
		this.propertySetter = propertySetter;
	}

	/**
	 * Sets the the project.
	 *
	 * @param theProject the new the project
	 */
	protected void setTheProject(COMObject theProject) {
		this.theProject = theProject;
	}
	/**
	 * Opens the Artisan Sutio Editor and shows the given object. By default the first
	 * diagram in which the object apperas is selected.
	 * @throws EpsilonCOMException
	 */
	public void showInStudio(Object instance) throws EpsilonCOMException {
		assert instance instanceof COMObject;
		COMObject cobject = (COMObject) instance;
		connectToStudio();
		studio.invoke("ShowMainWindow");
		List<Object> args = new ArrayList<Object>();
		args.add(getModelId());
		studio.invoke("OpenModel",args);
		String objectId = getElementId(instance);
		args.clear();
		args.add("Using Diagram");
		COMObject diag = (COMObject) cobject.invoke("Item", args);
		if (diag != null) {
			Object diagramId = getElementId(diag);
			args.clear();
			args.add("Representing Symbol");
			Object objSymbol = cobject.invoke("Item", args);
			Object symboldId = getElementId(objSymbol);
			args.clear();
			args.add(diagramId);
			studio.invoke("OpenDiagram", args);
			args.clear();
			args.add(diagramId);
			args.add(symboldId);
			studio.invoke("SelectSymbol2", args);
		}
		else {		// There is no diagram, use the project tree
			args.clear();
			args.add(objectId);
			args.add("Packages");
			studio.invoke("SelectBrowserItem", args);
		}
		studio.invoke("SetForegroundWindow");
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.IModel#store()
	 */
	@Override
	public boolean store() {
		assert storeOnDisposal;
		try {
			commitTransaction();
		} catch (EpsilonCOMException e) {
			// FIXME Log! or exception
			System.err.println("There was an error finishing the transaction on model store. Changes to the model might have been partially commited.");
			return false;
		}
		return true;

	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.IModel#store(java.lang.String)
	 */
	@Override
	public boolean store(String location) {
		// FIXME We could do a CloneModel?
		throw new UnsupportedOperationException("Artisan models are updated per invocation.");
	}

}
