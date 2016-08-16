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
package org.eclipse.epsilon.emc.artisan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.emc.COM.COMBridge;
import org.eclipse.epsilon.emc.COM.COMModel;
import org.eclipse.epsilon.emc.COM.COMObject;
import org.eclipse.epsilon.emc.COM.COMProperty;
import org.eclipse.epsilon.emc.COM.COMPropertyManager;
import org.eclipse.epsilon.emc.COM.EpsilonCOMException;
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
 * The Class ArtisanModel.
 */
public class ArtisanModel extends CachedModel<COMObject> {
	
	/** The Constant COM_ID. */
	private static final String COM_ID = "org.eclipse.epsilon.emc.artisan.COM";
	
	/** The Constant BRIDGE_INDEX. */
	private static final int BRIDGE_INDEX = 0;
	
	/** The Constant GETTER_INDEX. */
	private static final int GETTER_INDEX = 1;
	
	/** The Constant MANAGER_INDEX. */
	private static final int MANAGER_INDEX = 2;
	
	/** The Constant SETTER_INDEX. */
	private static final int SETTER_INDEX = 3;
	
	
	/** The bridge. */
	// FIXME How to inject the correct implementation?
	COMBridge<COMObject, COMObject> bridge;
	
	/**  The Project, needed for type testing and instantiation. */ 
	private COMObject theProject;
	
	/** The is initialized. */
	private boolean isInitialized = false;
	
	/**  The Artisan Model. */
	private COMModel model = null;
	
	/**  The Artisan Studio. */
	private COMObject studio = null;
	
	
	/** The property getter. */
	// FIXME How to inject the corect implementation?
	private IPropertyGetter propertyGetter;

	/** The property setter. */
	private IPropertySetter propertySetter;
	
	/** The property manager. */
	private COMPropertyManager propertyManager;

	private boolean connectedToStudio = false;

	/**
	 * Instantiates a new artisan model. Gets the COM helpers from the extension
	 */
	public ArtisanModel() {
		// FIXME USe the Extension point to get the jawin classes
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IExtensionPoint ep = reg.getExtensionPoint(COM_ID);
		IExtension[] extensions = ep.getExtensions();
		// There should only be one contributor
		IExtension ext = extensions[0];
		IConfigurationElement[] ce = ext.getConfigurationElements();
    	try {
			bridge = (COMBridge<COMObject, COMObject>) ce[BRIDGE_INDEX].createExecutableExtension("class");
			setPropertyGetter((IPropertyGetter) ce[GETTER_INDEX].createExecutableExtension("class"));
			setPropertyManager(((COMPropertyManager) ce[MANAGER_INDEX].createExecutableExtension("class")).getInstance());
			setPropertySetter((IPropertySetter) ce[SETTER_INDEX].createExecutableExtension("class"));
		} catch (CoreException e) {
			throw new IllegalStateException(e);
		}
    	cachingEnabled = false;
    	/*
		 * Set objManager = CreateObject("Studio.ModelManager")
		 * objManager.AddModel("\\Enabler\MyServer\MyRepository","MyModel")
		 */
		if (!isInitialized) {
			try {
				bridge.initialiseCOM();
			} catch (EpsilonCOMException e) {
				throw new IllegalStateException(e);
			}
		}
		isInitialized = true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.CachedModel#allContentsFromModel()
	 */
	@Override
	protected Collection<COMObject> allContentsFromModel() {
		assert model != null;
		Collection<? extends COMObject> elements;
		List<Object> args = new ArrayList<Object>();
		args.add("Owned Contents");
		args.add("*");
		try {
			COMObject res = (COMObject) model.invoke("Items", args);
			elements = model.wrapInColleciton(res, model, "Owned Contents");
		} catch (EpsilonCOMException e) {
			throw new IllegalStateException(e);
		}
		return (Collection<COMObject>) elements;
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
	 * Opens the Artisan Sutio Editor and shows the given object. By default the first
	 * diagram in which the object apperas is selected.
	 * @throws EpsilonCOMException
	 */
	public void showInStudio(Object instance) throws EpsilonCOMException {
		assert instance instanceof COMObject;
		COMObject cobject = (COMObject) instance;
		connectToStudio();
		studio.invoke("ShowMainWindow");
		studio.invoke("SetForegroundWindow");
		List<Object> args = new ArrayList<Object>();
		args.add(getName());
		studio.invoke("OpenModel",args);
		String id = getElementId(instance);
		// Find a diagram related to the object
		// First Diagram
		args.clear();
		args.add("Using Diagram");
		COMObject diag = (COMObject) cobject.invoke("Item", args);
		Object dId = getElementId(diag);
		args.clear();
		args.add("Representing Symbol");
		Object objSymbol = cobject.invoke("Item", args);
		Object symboldId = getElementId(objSymbol);
		args.clear();
		args.add(dId);
		studio.invoke("OpenDiagram", args);
		args.clear();
		args.add(dId);
		args.add(symboldId);
		studio.invoke("SelectSymbol2", args);
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
		args.add(type.toUpperCase());
		Object newInstance = null;
		String id;
		try {
			newInstance = model.invoke("AddByType", args);
			id = (String) ((COMObject) newInstance).get("Property", "Id");
			((COMObject) newInstance).setId(id);
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
		if (isInitialized) {
			try {
				if (storeOnDisposal) {
					commitTransaction();
				}
				else {
					abortTransaction();
				}
			} catch (EpsilonCOMException e) {
				// FIXME Log! or exception
				System.err.println("There was an error finishing the transaction on model disposal. Changes to the model might have been partially commited.");
			}
			finally {
				try {
					bridge.uninitializeCOM();
				} catch (EpsilonCOMException e) {
					// FIXME Does Epsilon has a logger for this?
					//e.printStackTrace();
				}
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
		Collection<? extends COMObject> elements;
		List<Object> args = new ArrayList<Object>();
		args.add(type);
		List<Object> byRefArgs = new ArrayList<Object>();
		byRefArgs.add("*");
		try {
			COMObject res = (COMObject) model.invoke("Items", args, byRefArgs);
			elements = model.wrapInColleciton(res, model, type);	//new JawinCollection(res, model, type);
		} catch (EpsilonCOMException e) {
			throw new EolModelElementTypeNotFoundException(name, type);
		}
		return (Collection<COMObject>) elements;
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

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.IModel#getElementById(java.lang.String)
	 */
	@Override
	public Object getElementById(String id) {
		List<Object> args = new ArrayList<Object>();
		args.add(id);
		Object res = null;
		try {
			res = model.invoke("ItemById", args);
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

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.IModel#isInstantiable(java.lang.String)
	 */
	@Override
	public boolean isInstantiable(String type) {
		// TODO I think all types in the Artisan metamodel are instatiatable, we would need to make
		// sure that the name is actually valid. 
		return hasType(type);
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
		load();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.CachedModel#loadModel()
	 */
	@Override
	protected void loadModel() throws EolModelLoadingException {
		if (isInitialized)
		{
			COMObject artisanApp;
			try {
				artisanApp = bridge.connectToCOM("OMTE.Projects");
			} catch (EpsilonCOMException e) {
				try {
					bridge.uninitializeCOM();
				} catch (EpsilonCOMException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				throw new EolModelLoadingException(e, this);
			}
			try {
				theProject = bridge.openModel(artisanApp, name);
				beginTransaction();
			} catch (EpsilonCOMException e) {
				try {
					bridge.uninitializeCOM();
				} catch (EpsilonCOMException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				throw new EolModelLoadingException(e, this);
			}
			List<Object> args = new ArrayList<Object>();
			args.add("Dictionary");
			try {
				COMObject res = (COMObject) theProject.invoke("Item", "Dictionary", args);
				model = bridge.wrapModel(res);
			} catch (EpsilonCOMException e) {
				try {
					bridge.uninitializeCOM();
				} catch (EpsilonCOMException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				throw new EolModelLoadingException(e, this);
			}
		}
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
	
	private void abortTransaction() throws EpsilonCOMException {
		List<Object> args = new ArrayList<Object>();
		args.add("Transaction");
		args.add(0);
		args.add("Abort");
		theProject.invoke("PropertySet", args);
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.IModel#owns(java.lang.Object)
	 */
	@Override
	public boolean owns(Object instance) {
		Collection<COMObject> all = allContentsFromModel();
		return all.contains(instance);
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

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.IModel#store()
	 */
	@Override
	public boolean store() {
		throw new UnsupportedOperationException("Artisan models are updated per invocation.");

	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.IModel#store(java.lang.String)
	 */
	@Override
	public boolean store(String location) {
		throw new UnsupportedOperationException("Artisan models are updated per invocation.");
	}

}
