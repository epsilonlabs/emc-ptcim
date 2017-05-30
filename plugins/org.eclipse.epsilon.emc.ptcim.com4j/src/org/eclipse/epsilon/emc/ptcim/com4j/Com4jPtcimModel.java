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
package org.eclipse.epsilon.emc.ptcim.com4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;

import com4j.Com4jObject;

import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.eol.exceptions.EolInternalException;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.exceptions.models.EolEnumerationValueNotFoundException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelElementTypeNotFoundException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.exceptions.models.EolNotInstantiableModelElementTypeException;
import org.eclipse.epsilon.eol.execute.introspection.AbstractPropertyGetter;
import org.eclipse.epsilon.eol.execute.introspection.AbstractPropertySetter;
import org.eclipse.epsilon.eol.execute.introspection.IPropertyGetter;
import org.eclipse.epsilon.eol.execute.introspection.IPropertySetter;
import org.eclipse.epsilon.eol.models.CachedModel;
import org.eclipse.epsilon.eol.models.IRelativePathResolver;

/**
 * The Class PtcimModel provides the EMC access to PTC Intregity Modeler models.
 */
public class Com4jPtcimModel extends CachedModel<Com4jPtcimObject> {

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
	
	/** Optional cache of property attributes (e.g., isPublic(), isAssociation(), ...) is supported: */
	public static final String PROPERTY_PROPERTIES_ATTRIBUTES_CACHE_ENABLED = "propertiesAttributesCacheEnabled";
	
	/** Optional cache of property values is supported: */
	public static final String PROPERTY_PROPERTIES_VALUES_CACHE_ENABLED = "propertiesValuesCacheEnabled";
	
	/** The Constant PROPERTY_ELEMENT_ID refers to the ID of the currently selected element. */
	public static final String PROPERTY_ELEMENT_ID = "elementId";
	
	/** IAutomationCaseObject interface id. */
	public static final String DIID = "{c9ff8402-bb2e-11d0-8475-0080C82BFA0C}";

	public static final String PROPERTY_ELEMENT_NAME_AND_TYPE = "elementNameAndType";
	
	/**  The Project, needed for type testing and instantiation. */ 
	private Com4jPtcimObject theProject;
	
	private boolean isInitialized = false;
	
	/**  The PTC IM Model handle. */
	private Com4jPtcimObject model = null;
	
	/** The property manager class includes utility methods for both the getter and the setter. */
	private Com4jPtcimPropertyManager manager; //Activator.getDefault().getFactory().getPropertyManager(isPropertiesAttributesCacheEnabled());
	
	/** The getter. One getter per model. */
	private AbstractPropertyGetter getter = null; //new PtcimPropertyGetter(manager);
	
	/** The setter. One setter per model. */
	private AbstractPropertySetter setter = null; //new PtcimPropertySetter(manager);
	
	/**This map is the cache for the values of each property. The properties are identified by concatenating the id of the element 
	 * with the symbol "." followed by the name of the property (e.g., edao08qwdkdas92.name) 
	 */
	public WeakHashMap<String, Object> propertiesValuesCache = new WeakHashMap<String, Object>();
	
	public Com4jPtcimFrameworkFactory factory = new Com4jPtcimFrameworkFactory();

	private String modelId;
	private String server;
	private String repository;
	private String version;
	private boolean fromSelection;
	private String selectedElementId;
	private boolean propertiesAttributesCacheEnabled;
	private boolean propertiesValuesCacheEnabled;
	
	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public boolean isFromSelection() {
		return fromSelection;
	}

	public void setFromSelection(boolean fromSelection) {
		this.fromSelection = fromSelection;
	}

	public void setPropertiesAttributesCacheEnabled(boolean propertiesAttributesCacheEnabled) {
		this.propertiesAttributesCacheEnabled = propertiesAttributesCacheEnabled;
	}

	public void setPropertiesValuesCacheEnabled(boolean propertiesValuesCacheEnabled) {
		this.propertiesValuesCacheEnabled = propertiesValuesCacheEnabled;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Instantiates a new artisan model. Gets the COM helpers from the extension
	 */
	public Com4jPtcimModel() {
		isInitialized = true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.CachedModel#allContentsFromModel()
	 */
	@Override
	protected Collection<Com4jPtcimObject> allContentsFromModel() {
		assert model != null;
		Collection<Com4jPtcimObject> elements;
		Com4jPtcimObject res = new Com4jPtcimObject(model.items("", null).queryInterface(IAutomationCaseObject.class));
		elements = res.wrapInCollection(model, "");
		return (Collection<Com4jPtcimObject>) elements;
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
		Com4jPtcimObject comParent = (Com4jPtcimObject) parent;
		if (parameters.size() == 1) {		// Add
			try {
				newInstance = comParent.add(type, null);
				setNewInstanceId(newInstance);
			} catch (EolInternalException e) {
				throw new EolModelElementTypeNotFoundException(this.name, type);
			}
		}
		else {								// AddByType
			Object association = it.next();
			try {
				newInstance = comParent.addByType(type, association.toString());
				setNewInstanceId(newInstance);
			} catch (EolInternalException e) {
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
	protected Com4jPtcimObject createInstanceInModel(String type)
			throws EolModelElementTypeNotFoundException, EolNotInstantiableModelElementTypeException {
		if (!isInstantiable(type)) {
			throw new EolNotInstantiableModelElementTypeException(getName(), type);
		}
		Object newInstance = null;
		try {
			newInstance = model.add(type, null);
			setNewInstanceId(newInstance);
		} catch (EolInternalException e) {
			throw new EolModelElementTypeNotFoundException(getName(), type);
		}
		return (Com4jPtcimObject) newInstance;
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
		assert instance instanceof Com4jPtcimObject;
		boolean success = false;
		((Com4jPtcimObject) instance).delete();
		success = true;
		return success;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.CachedModel#disposeModel()
	 */
	@Override
	protected void disposeModel() {
		if (isInitialized()) {
			if (!storeOnDisposal) {
				// TODO: Discuss with Dimitris about store on disposal strategy
			}
		}
		theProject.disconnect();
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
	public Collection<Com4jPtcimObject> getAllOfKindFromModel(String kind) throws EolModelElementTypeNotFoundException {
		return getAllOfTypeFromModel(kind);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.CachedModel#getAllOfType(java.lang.String)
	 */
	@Override
	public Collection<Com4jPtcimObject> getAllOfTypeFromModel(String type) throws EolModelElementTypeNotFoundException {
		assert model != null;
		if (!fromSelection) {
			List<? extends Com4jPtcimObject> elements;
			Com4jPtcimObject res;
			res = new Com4jPtcimObject(model.items(type, null).queryInterface(IAutomationCaseObject.class));	//, byRefArgs);
			elements = res.wrapInCollection(model, type);
			return (List<Com4jPtcimObject>) elements;
		}
		else {
			if ("Package".equals(type)) {	// Important: When you retrieve the type of a Package, it is returned as Category.
				type = "Category";
			}
			Com4jPtcimObject root = (Com4jPtcimObject) getElementById(selectedElementId);
			List<Com4jPtcimObject> result = getOwnedContents(root, type);
			return result;
		}
	}
	
	private List<Com4jPtcimObject> getOwnedContents(Com4jPtcimObject root, String type) throws EolModelElementTypeNotFoundException {
		String rootType = null;
		rootType = (String) root.property("Type", null);
		List<Com4jPtcimObject> result = new ArrayList<Com4jPtcimObject>();
		if ("Category".equals(rootType)) {	// Root is a package
			String asocName = "Package Item";
			result.addAll(associationToListRecursive(root, type, asocName));
		}
		else {
			// TODO Other types may need specific associations to get the contents
			String asocName = "Owned Contents";
			List<Com4jPtcimObject> ownedContents = associationToList(root, asocName);
			result.addAll(filterByType(ownedContents, type));
		}
		return result;
	}
	
	private List<Com4jPtcimObject> associationToListRecursive(Com4jPtcimObject root, String type, String asocName)
			throws EolModelElementTypeNotFoundException {
		
		List<Com4jPtcimObject> ownedContents = associationToList(root, asocName);
		List<Com4jPtcimObject> result = filterByType(ownedContents, type);
		for (Com4jPtcimObject e : ownedContents) {
			result.addAll(getOwnedContents(e, type));
		}
		return result;
	}

	private List<Com4jPtcimObject> associationToList(Com4jPtcimObject root, String asocName)
			throws EolModelElementTypeNotFoundException {
		
		List<Object> args = new ArrayList<Object>();
		args.add(asocName);
		Com4jPtcimObject res = null;
		res = new Com4jPtcimObject(root.items(asocName, null).queryInterface(IAutomationCaseObject.class));
		if (res != null) {
			return res.wrapInCollection(root, asocName);
		}
		return Collections.emptyList();
	}

	private List<Com4jPtcimObject>  filterByType(List<Com4jPtcimObject> ptcCollection, String type) {
		List<Com4jPtcimObject> result = new ArrayList<Com4jPtcimObject>();
		Iterator<Com4jPtcimObject> it = ptcCollection.iterator();
		while (it.hasNext()) {
			Com4jPtcimObject e = it.next();
			Object etype = null;
			etype = e.property("Type", null);
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


	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.IModel#getElementById(java.lang.String)
	 */
	@Override
	public Object getElementById(String id) {
		Com4jPtcimObject res = null;
		System.out.println("id: " + id);
		System.out.println("New the project: " + theProject);
		res = new Com4jPtcimObject(theProject.itemByID(id).queryInterface(IAutomationCaseObject.class));
		System.out.println(res);
		if (res != null)
			res.setId(id);
		return res;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.IModel#getElementId(java.lang.Object)
	 */
	@Override
	public String getElementId(Object instance) {
		assert instance instanceof Com4jPtcimObject;
		String id = ((Com4jPtcimObject) instance).getId();
		if (id == null) {
			id = (String) ((Com4jPtcimObject) instance).property("Id", null);
			((Com4jPtcimObject) instance).setId(id);
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
	 * Checks if the user wants to use the PTC driver to cache property values during execution.
	 * @return true is the cache should be used, false otherwise
	 */
	public boolean isPropertiesAttributesCacheEnabled() {
		return propertiesAttributesCacheEnabled;
	}
	
	/**
 	 * Checks if the user wants to use the PTC driver to cache property values during execution.
  	 * @return true is the cache should be used, false otherwise
  	 */
	public boolean isPropertiesValuesCacheEnabled() {
		return propertiesValuesCacheEnabled;
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
		return getter;
	}

	protected Com4jPtcimPropertyManager getPropertyManager() {
		return factory.getPropertyManager(isPropertiesAttributesCacheEnabled());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.Model#getPropertySetter()
	 */
	@Override
	public IPropertySetter getPropertySetter() {
		return setter;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.IModel#getTypeNameOf(java.lang.Object)
	 */
	@Override
	public String getTypeNameOf(Object instance) {
		assert instance instanceof Com4jPtcimObject;
		String typeName;
		typeName = (String) ((Com4jPtcimObject) instance).property("Type", null);
		return typeName;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.IModel#hasType(java.lang.String)
	 */
	@Override
	public boolean hasType(String type) {
		String errDispPtr = "";
		errDispPtr = (String) theProject.getClassProperties(type, null);
		return errDispPtr.length() > 0;
	}

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
		assert instance instanceof Com4jPtcimObject;
		String type = getTypeNameOf(instance);
		return metaClass.equals(type);
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
		propertiesAttributesCacheEnabled = properties.getBooleanProperty(PROPERTY_PROPERTIES_ATTRIBUTES_CACHE_ENABLED, false);
		propertiesValuesCacheEnabled = properties.getBooleanProperty(PROPERTY_PROPERTIES_VALUES_CACHE_ENABLED, false);
		manager = factory.getPropertyManager(isPropertiesAttributesCacheEnabled());
		if (isPropertiesValuesCacheEnabled()) {
			getter = new Com4jPtcimCachedPropertyGetter(manager, this);
			setter = new Com4jPtcimCachedPropertySetter(manager, this);
		} else {
			getter = new Com4jPtcimPropertyGetter(manager);
			setter = new Com4jPtcimPropertySetter(manager);
		}
		load();
	}
	
	public void loadDictionary() throws EolModelLoadingException {
		model = new Com4jPtcimObject(theProject.item("Dictionary", "Dictionary").queryInterface(IAutomationCaseObject.class));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.CachedModel#loadModel()
	 */
	@Override
	protected void loadModel() throws EolModelLoadingException {
		if (isInitialized()) {

			Com4jPtcimModelManager manager;
			try {
				manager = factory.getModelManager(false);
			} catch (EolInternalException e1) {
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
				} catch (EolInternalException e) {
					throw new EolModelLoadingException(e, this);
				}
				System.out.println("theProject: " + theProject);

			}
			else if (storeOnDisposal) {
				// TODO Decide how the readOnLoad/storeOnDisposal flags control how the model is either
				// loaded or created. 
			}
			loadDictionary();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.IModel#owns(java.lang.Object)
	 */
	@Override
	public boolean owns(Object instance) {
		if (instance instanceof Com4jPtcimObject) {
			if (((Com4jPtcimObject) instance).getId() == null) {
				throw new IllegalStateException("COMObjects can not be found without an Id");
			}
			Object other = getElementById(((Com4jPtcimObject) instance).getId());
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
	private void setNewInstanceId(Object newInstance) throws EolInternalException {
		String id = (String) ((Com4jPtcimObject) newInstance).property("Id", null);
		((Com4jPtcimObject) newInstance).setId(id);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.IModel#store()
	 */
	@Override
	public boolean store() {
		assert storeOnDisposal;
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
