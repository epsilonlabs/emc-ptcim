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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.emc.ptcim.ole.IPtcCollection;
import org.eclipse.epsilon.emc.ptcim.ole.IPtcComBridge;
import org.eclipse.epsilon.emc.ptcim.ole.IPtcModelManager;
import org.eclipse.epsilon.emc.ptcim.ole.IPtcObject;
import org.eclipse.epsilon.emc.ptcim.ole.IPtcPropertyManager;
import org.eclipse.epsilon.emc.ptcim.ole.impl.EpsilonCOMException;
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
 * The Class PtcimModel provides the EMC access to PTC Intregity Modeler models.
 */
public class PtcimModel extends CachedModel<IPtcObject> {

	/** The Constant PROPERTY_MODEL_REFERENCE refers to the name of the model in the PTC IM database. */
	public static final String PROPERTY_MODEL_REFERENCE = "modelRef";

	/** The Constant PROPERTY_SERVER_NAME refers to the name of the server in which a model is stored. */
	public static final String PROPERTY_SERVER_NAME = "server";

	/** The Constant PROPERTY_REPOSITORY_NAME refers to the name of the repository in which a model is stored. */
	public static final String PROPERTY_REPOSITORY_NAME = "repository";

	/** The Constant PROPERTY_VERSION_NUMBER refers to the version of a model. */
	public static final String PROPERTY_VERSION_NUMBER = "version";

	/** The Constant PROPERTY_FROM_SELECTION indicates whether the root of the model or the selected element is used
	 * as the source for element retreival from the model. */
	public static final String PROPERTY_FROM_SELECTION = "fromSelection";
	
	/** The Constant PROPERTY_ELEMENT_ID refers to the ID of the currently selected element. */
	public static final String PROPERTY_ELEMENT_ID = "elementId";
	
	/**  IAutomationCaseObject interface id. */
	public static final String DIID = "{c9ff8402-bb2e-11d0-8475-0080C82BFA0C}";

	public static final String PROPERTY_ELEMENT_NAME_AND_TYPE = "elementNameAndType";
	
	/** The bridge. */
	protected IPtcComBridge<IPtcObject> bridge;
	
	/**  The Project, needed for type testing and instantiation. */ 
	private IPtcObject theProject;
	
	/** The is initialized. */
	private boolean isInitialized = false;
	
	/**  The PTC IM Model handle. */
	private IPtcObject model = null;
	
	/** The model id. */
	private String modelId;

	/** The server. */
	private String server;

	/** The repository. */
	private String repository;

	/** The version. */
	private String version;

	/** The from selection. */
	private boolean fromSelection;

	/** The selected element id. */
	private String selectedElementId;

	/**
	 * Keeps a reference to the last object for which {@link #knowsAboutProperty(Object, String)}
	 * was invoked.
	 */
	private Object lastPropertyObject;
	
	/** The xetter cache. */
	private Map<Object, IPtcPropertyManager> xetterCache;

	/**
	 * Instantiates a new artisan model. Gets the COM helpers from the extension
	 */
	public PtcimModel() {
		
		xetterCache = new HashMap<Object, IPtcPropertyManager>();
		isInitialized = true;
	}

	/**
	 * If a transaction has been started on the model, this method will
	 * abort the transaction. As a result all the changes in the model
	 * that where part of the transaction would be discarded. See
	 * {@link #beginTransaction()}.
	 *
	 * @throws EpsilonCOMException If a transaction has not been started
	 */
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
	protected Collection<IPtcObject> allContentsFromModel() {
		assert model != null;
		Collection<? extends IPtcObject> elements;
		List<Object> args = new ArrayList<Object>();
		args.add("");
		try {
			IPtcObject res = (IPtcObject) model.invoke("Items", args);
			elements = res.wrapInColleciton(model, "");
		} catch (EpsilonCOMException e) {
			throw new IllegalStateException(e);
		}
		return (Collection<IPtcObject>) elements;
	}
	
	/**
	 * Begin a transaction in the model. All changes to the model
	 * during a transaction can either be committed or discarded
	 * by committing (see {@link #commitTransaction()} or aborting
	 * (see {@link #abortTransaction()} the transaction respectively.
	 * @throws EpsilonCOMException if the transaction can not be started
	 */
	private void beginTransaction() throws EpsilonCOMException {
		List<Object> args = new ArrayList<Object>();
		args.add("Transaction");
		args.add(0);
		args.add("Begin");
		theProject.invoke("PropertySet", args);
	}
	
	/**
	 * If a transaction has been started on the model, this method will
	 * commit the transaction. As a result all the changes in the model
	 * that where part of the transaction would be applied. See
	 * {@link #beginTransaction()}.
	 *
	 * @throws EpsilonCOMException If a transaction has not been started
	 */
	private void commitTransaction() throws EpsilonCOMException {
		List<Object> args = new ArrayList<Object>();
		args.add("Transaction");
		args.add(0);
		args.add("Commit");
		theProject.invoke("PropertySet", args);
	}

	/**
	 * Creates a new instances in a specific parent and association.
	 * In Artisan, elements created through the API can not be moved from the parent 
	 * in which they where created. This method allows to create elements in a 
	 * specific hierarchy. For Add operations the parameters must be the parent object.
	 * The type must match the name of the association. For AddByType operations,
	 * the parameters must be the parent object, and the association.
	 *
	 * @param type the type
	 * @param parameters the parameters
	 * @return the object
	 * @throws EolModelElementTypeNotFoundException the eol model element type not found exception
	 * @throws EolNotInstantiableModelElementTypeException the eol not instantiable model element type exception
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
		IPtcObject comParent = (IPtcObject) parent;
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
	protected IPtcObject createInstanceInModel(String type)
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
			// check that the instance is correct
//			Object ref = getElementById(((IPtcObject) newInstance).getId());
//			while (ref == null) {
//				// Some how the element was not created, try again
//				newInstance = model.invoke("Add", args);
//				setNewInstanceId(newInstance);
//				// check that the instance is correct
//				ref = getElementById(((IPtcObject) newInstance).getId());
//			}
		} catch (EpsilonCOMException e) {
			throw new EolModelElementTypeNotFoundException(getName(), type);
		}
		return (IPtcObject) newInstance;
	}
	
	
	/**
	 * Deletes an object from the model.
	 * Important:  If you delete an object and that object is linked to other objects through
	 * automation interface associations that have their Propagate Delete flag set to TRUE,
	 * objects that are linked through those associations will also be deleted. For example,
	 * a Class is related to its child Attributes through the Attribute association, which has
	 * its Propagate Delete flag set to TRUE. If you delete a Class, its Attributes will also
	 * be deleted.
	 *
	 * @param instance the instance
	 * @return true, if successful
	 * @throws EolRuntimeException the eol runtime exception
	 */
	@Override
	protected boolean deleteElementInModel(Object instance) throws EolRuntimeException {
		assert instance instanceof IPtcObject;
		boolean success = false;
		try {
			((IPtcObject) instance).invokeMethod("Delete");
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
		getPropertyManager().dispose();
		if (isInitialized()) {
			if (!storeOnDisposal) {
//				try {
//					abortTransaction();
//				} catch (EpsilonCOMException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
		}
		try {
			theProject.disconnect();
		} catch (EpsilonCOMException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Artisan does not provide support for all of kind, so for this models this method
	 * delegates to {@link #getAllOfTypeFromModel(String)}.
	 *
	 * @param kind the kind
	 * @return the all of kind from model
	 * @throws EolModelElementTypeNotFoundException the eol model element type not found exception
	 */
	@Override
	public Collection<IPtcObject> getAllOfKindFromModel(String kind) throws EolModelElementTypeNotFoundException {
		return getAllOfTypeFromModel(kind);
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.CachedModel#getAllOfType(java.lang.String)
	 */
	@Override
	public Collection<IPtcObject> getAllOfTypeFromModel(String type) throws EolModelElementTypeNotFoundException {
		assert model != null;
		if (!fromSelection) {
			List<? extends IPtcObject> elements;
			List<Object> args = new ArrayList<Object>();
			args.add(type);
			IPtcObject res;
			try {
				res = (IPtcObject) model.invoke("Items", args);	//, byRefArgs);
			} catch (EpsilonCOMException e) {
				throw new EolModelElementTypeNotFoundException(name, type);
			}
			elements = res.wrapInColleciton(model, type);	//new JawinCollection(res, model, type);
			return (List<IPtcObject>) elements;
		}
		else {
			if ("Package".equals(type)) {	// Important: When you retrieve the type of a Package, it is returned as Category.
				type = "Category";
			}
			IPtcObject root = (IPtcObject) getElementById(selectedElementId);
			List<IPtcObject> result = getOwnedContents(root, type);
			return result;
		}
	}
	
	/**
	 * Gets the owned contents.
	 *
	 * @param root the root
	 * @param type the type
	 * @return the owned contents
	 * @throws EolModelElementTypeNotFoundException the eol model element type not found exception
	 */
	private List<IPtcObject> getOwnedContents(IPtcObject root, String type) throws EolModelElementTypeNotFoundException {
		String rootType = null;
		try {
			rootType = (String) root.getAttribute("Property", "Type");
		} catch (EpsilonCOMException e2) {
			throw new EolModelElementTypeNotFoundException(name, type);
		}
		List<IPtcObject> result = new ArrayList<IPtcObject>();
		if ("Category".equals(rootType)) {	// Root is a package
			String asocName = "Package Item";
			result.addAll(associationToListRecursive(root, type, asocName));
		}
		else {
			// TODO Other types may need specific associations to get the contents
			String asocName = "Owned Contents";
			List<? extends IPtcObject> ownedContents = associationToList(root, asocName);
			result.addAll(filterByType(ownedContents, type));
		}
		return result;
	}
	
	/**
	 * Association to list recursive.
	 *
	 * @param root the root
	 * @param type the type
	 * @param asocName the asoc name
	 * @return the list
	 * @throws EolModelElementTypeNotFoundException the eol model element type not found exception
	 */
	private List<IPtcObject> associationToListRecursive(IPtcObject root, String type, String asocName)
			throws EolModelElementTypeNotFoundException {
		
		List<? extends IPtcObject> ownedContents = associationToList(root, asocName);
		List<IPtcObject> result = filterByType(ownedContents, type);
		for (IPtcObject e : ownedContents) {
			result.addAll(getOwnedContents(e, type));
		}
		return result;
	}

	/**
	 * Association to list.
	 *
	 * @param root the root
	 * @param asocName the asoc name
	 * @return the list<? extends I ptc object>
	 * @throws EolModelElementTypeNotFoundException the eol model element type not found exception
	 */
	private List<? extends IPtcObject> associationToList(IPtcObject root, String asocName)
			throws EolModelElementTypeNotFoundException {
		
		List<Object> args = new ArrayList<Object>();
		args.add(asocName);
		IPtcObject res = null;
		try {
			res = (IPtcObject) root.invoke("Items", args);
		} catch (EpsilonCOMException e1) {
			e1.printStackTrace();
		}
		if (res != null) {
			return res.wrapInColleciton(root, asocName);
		}
		return Collections.emptyList();
	}

	/**
	 * Filter by type.
	 *
	 * @param ptcCollection the ptc collection
	 * @param type the type
	 * @return the list
	 */
	private List<IPtcObject>  filterByType(List<? extends IPtcObject> ptcCollection, String type) {
		List<IPtcObject> result = new ArrayList<IPtcObject>();
		Iterator<? extends IPtcObject> it = ptcCollection.iterator();
		while (it.hasNext()) {
			IPtcObject e = it.next();
			Object etype = null;
			try {
				etype = e.getAttribute("Property", "Type");
			} catch (EpsilonCOMException e1) {
				e1.printStackTrace();
			}
			if (type.equals(etype)) {
				result.add(e);
			}
		}
		return result;
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

	/**
	 * Gets the COM model.
	 *
	 * @return the COM model
	 */
	public IPtcObject getCOMModel() {
		return model;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.IModel#getElementById(java.lang.String)
	 */
	@Override
	public Object getElementById(String id) {
		List<Object> args = new ArrayList<Object>();
		args.add(id);
		IPtcObject res = null;
		try {
			res = (IPtcObject) theProject.invoke("ItemById", args);
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
		assert instance instanceof IPtcObject;
		String id = ((IPtcObject) instance).getId();
		if (id == null)
		{
			List<Object> args = new ArrayList<Object>();
			args.add("Id");
			try {
				id = (String) ((IPtcObject) instance).getAttribute("Property", args);
				((IPtcObject) instance).setId(id);
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
	 * Get the Id of the model used for the PTC IM repository.
	 *
	 * @return the model id
	 */
	public String getModelId() {
		return modelId;
	}
	
	

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.Model#getPropertyGetter()
	 */
	@Override
	public IPropertyGetter getPropertyGetter() {
		if (xetterCache.containsKey(lastPropertyObject)) {
			return (IPropertyGetter) xetterCache.get(lastPropertyObject);
		}
		else {
			return null;
		}
	}

	/**
	 * Gets the property manager.
	 *
	 * @return the propertyManager
	 */
	protected IPtcPropertyManager getPropertyManager() {
		return Activator.getDefault().getFactory().getPropertyManager();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.Model#getPropertySetter()
	 */
	@Override
	public IPropertySetter getPropertySetter() {
		if (xetterCache.containsKey(lastPropertyObject)) {
			return (IPropertySetter) xetterCache.get(lastPropertyObject);
		}
		else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.IModel#getTypeNameOf(java.lang.Object)
	 */
	@Override
	public String getTypeNameOf(Object instance) {
		assert instance instanceof IPtcObject;
		String typeName;
		try {
			typeName = (String) ((IPtcObject) instance).getAttribute("Property", "Type");
		} catch (EpsilonCOMException e) {
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
			// FIXME Log me!
			e.printStackTrace();
		}
		return errDispPtr.length() > 0;
	}

	/**
	 * Checks if is initialized.
	 *
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

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.Model#isOfKind(java.lang.Object, java.lang.String)
	 */
	@Override
	public boolean isOfKind(Object instance, String metaClass) throws EolModelElementTypeNotFoundException {
		return isOfType(instance, metaClass);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.Model#isOfType(java.lang.Object, java.lang.String)
	 */
	@Override
	public boolean isOfType(Object instance, String metaClass) throws EolModelElementTypeNotFoundException {
		assert instance instanceof IPtcObject;
		String type = getTypeNameOf(instance);
		return metaClass.equals(type);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.Model#knowsAboutProperty(java.lang.Object, java.lang.String)
	 */
	@Override
	public boolean knowsAboutProperty(Object instance, String property) {
		Object p = null;
		if (instance instanceof IPtcObject) {
			Object pm = xetterCache.get(instance);
			if (pm == null) {
				pm = getPropertyManager();
				((IPropertySetter) pm).setObject(instance);
				xetterCache.put(instance, (IPtcPropertyManager) pm);
			}
			lastPropertyObject = instance;
			return ((IPtcPropertyManager) pm).knowsProperty(property);
		}
		return false;
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
		fromSelection = properties.getBooleanProperty(PROPERTY_FROM_SELECTION, false);
		selectedElementId = properties.getProperty(PROPERTY_ELEMENT_ID);
		load();
	}
	
	/**
	 * Load dictionary.
	 *
	 * @throws EolModelLoadingException the eol model loading exception
	 */
	public void loadDictionary() throws EolModelLoadingException {
		List<Object> byRefArgs = new ArrayList<Object>();
		byRefArgs.add("Dictionary");
		byRefArgs.add("Dictionary");
		try {
			IPtcObject res = (IPtcObject) theProject.invoke("Item", byRefArgs);
			model = res;
		} catch (EpsilonCOMException e) {
			try {
				bridge.uninitialiseCOM();
			} catch (EpsilonCOMException e1) {
				// FIXME Log me!
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
			Activator activator = Activator.getDefault();
			IPtcModelManager<? extends IPtcObject, ? extends IPtcCollection> manager;
			try {
				manager = activator.getFactory().getModelManager();
			} catch (EpsilonCOMException e1) {
				throw new EolModelLoadingException(e1, this);
			}
			if  (readOnLoad) {
				try {
					if (server.length() == 0) {
						theProject = manager.getProjectByTitle(modelId);
					}
					else {
						theProject = manager.getProjectByReference(modelId, server, repository, version);
					}
				} catch (EpsilonCOMException e) {
					throw new EolModelLoadingException(e, this);
				}
			}
			else if (storeOnDisposal) {
				// TODO Decide how the readOnLoad/storeOnDisposal flags control how the model is either
				// loaded or created. 
//				try {
//					manager.createModel(server, repository, getModelId());
//				} catch (EpsilonCOMException e) {
//					throw new EolModelLoadingException(e, this);
//				}
			}
//			try {
//				beginTransaction();
//			} catch (EpsilonCOMException e) {
//				throw new EolModelLoadingException(e, this);
//			}
			loadDictionary();
			try {
				manager.disconnect();
			} catch (EpsilonCOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.IModel#owns(java.lang.Object)
	 */
	@Override
	public boolean owns(Object instance) {
		if (instance instanceof IPtcObject) {
			if (((IPtcObject) instance).getId() == null) {
				throw new IllegalStateException("COMObjects can not be found without an Id");
			}
			Object other = getElementById(((IPtcObject) instance).getId());
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
	 * @param newInstance
	 * @throws EpsilonCOMException
	 */
	private void setNewInstanceId(Object newInstance) throws EpsilonCOMException {
		String id = (String) ((IPtcObject) newInstance).getAttribute("Property", "Id");
		((IPtcObject) newInstance).setId(id);
	}

		
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.IModel#store()
	 */
	@Override
	public boolean store() {
		assert storeOnDisposal;
//		try {
//			commitTransaction();
//		} catch (EpsilonCOMException e) {
//			// FIXME Log! or exception
//			System.err.println("There was an error finishing the transaction on model store. Changes to the model might have been partially commited.");
//			return false;
//		}
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