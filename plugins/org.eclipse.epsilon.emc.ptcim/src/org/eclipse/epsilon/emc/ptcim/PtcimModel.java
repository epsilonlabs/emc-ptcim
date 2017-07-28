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
public class PtcimModel extends CachedModel<PtcimObject> {

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
	private PtcimObject theProject;
	
	private boolean isInitialized = false;
	
	/**  The PTC IM Model handle. */
	private PtcimObject model = null;
	
	/** The property manager class includes utility methods for both the getter and the setter. */
	private PtcimPropertyManager manager; //Activator.getDefault().getFactory().getPropertyManager(isPropertiesAttributesCacheEnabled());
	
	/** The getter. One getter per model. */
	private AbstractPropertyGetter getter = null; //new PtcimPropertyGetter(manager);
	
	/** The setter. One setter per model. */
	private AbstractPropertySetter setter = null; //new PtcimPropertySetter(manager);
	
	/**This map is the cache for the values of each property. The properties are identified by concatenating the id of the element 
	 * with the symbol "." followed by the name of the property (e.g., edao08qwdkdas92.name) 
	 */
	public WeakHashMap<String, Object> propertiesValuesCache = new WeakHashMap<String, Object>();
	
	public PtcimFrameworkFactory factory = new PtcimFrameworkFactory();

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
	public PtcimModel() {
		isInitialized = true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.CachedModel#allContentsFromModel()
	 */
	@Override
	protected Collection<PtcimObject> allContentsFromModel() {
		assert model != null;
		Collection<PtcimObject> elements;
		PtcimObject res = new PtcimObject(model.items("", null).queryInterface(IAutomationCaseObject.class));
		elements = res.wrapInCollection(model, "");
		return (Collection<PtcimObject>) elements;
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
		PtcimObject newInstance = null;
		Iterator<Object> it = parameters.iterator();
		Object parent = it.next();
		PtcimObject comParent = (PtcimObject) parent;
		if (parameters.size() == 1) {		// Add
			newInstance.setTheIaco(comParent.add(type, null).queryInterface(IAutomationCaseObject.class));
			setNewInstanceId(newInstance);
		}
		else {								// AddByType
			Object association = it.next();
			newInstance.setTheIaco(comParent.addByType(type, association.toString()).queryInterface(IAutomationCaseObject.class));
			setNewInstanceId(newInstance);
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
	protected PtcimObject createInstanceInModel(String type)
			throws EolModelElementTypeNotFoundException, EolNotInstantiableModelElementTypeException {
		if (!isInstantiable(type)) {
			throw new EolNotInstantiableModelElementTypeException(getName(), type);
		}
		PtcimObject newInstance = new PtcimObject(model.add(type, null).queryInterface(IAutomationCaseObject.class));
		setNewInstanceId(newInstance);
		return newInstance;
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
		assert instance instanceof PtcimObject;
		boolean success = false;
		((PtcimObject) instance).delete();
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
	public Collection<PtcimObject> getAllOfKindFromModel(String kind) throws EolModelElementTypeNotFoundException {
		return getAllOfTypeFromModel(kind);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.CachedModel#getAllOfType(java.lang.String)
	 */
	@Override
	public Collection<PtcimObject> getAllOfTypeFromModel(String type) throws EolModelElementTypeNotFoundException {
		assert model != null;
		if (!fromSelection) {
			List<? extends PtcimObject> elements;
			PtcimObject res;
			res = new PtcimObject(model.items(type, null).queryInterface(IAutomationCaseObject.class));	//, byRefArgs);
			elements = res.wrapInCollection(model, type);
			return (List<PtcimObject>) elements;
		}
		else {
			if ("Package".equals(type)) {	// Important: When you retrieve the type of a Package, it is returned as Category.
				type = "Category";
			}
			System.out.println("Hi!!! " + type + " " + selectedElementId);
			PtcimObject root = (PtcimObject) getElementById(selectedElementId);
			System.out.println(root);
			List<PtcimObject> result = getOwnedContents(root, type);
			return result;
		}
	}
	
	private List<PtcimObject> getOwnedContents(PtcimObject root, String type) throws EolModelElementTypeNotFoundException {
		String rootType = null;
		rootType = (String) root.property("Type", null);
		List<PtcimObject> result = new ArrayList<PtcimObject>();
		if ("Category".equals(rootType)) {	// Root is a package
			String asocName = "Package Item";
			result.addAll(associationToListRecursive(root, type, asocName));
		}
		else {
			// TODO Other types may need specific associations to get the contents
			String asocName = "Owned Contents";
			List<PtcimObject> ownedContents = associationToList(root, asocName);
			result.addAll(filterByType(ownedContents, type));
		}
		return result;
	}
	
	private List<PtcimObject> associationToListRecursive(PtcimObject root, String type, String asocName)
			throws EolModelElementTypeNotFoundException {
		
		List<PtcimObject> ownedContents = associationToList(root, asocName);
		List<PtcimObject> result = filterByType(ownedContents, type);
		for (PtcimObject e : ownedContents) {
			result.addAll(getOwnedContents(e, type));
		}
		return result;
	}

	private List<PtcimObject> associationToList(PtcimObject root, String asocName)
			throws EolModelElementTypeNotFoundException {
		
		List<Object> args = new ArrayList<Object>();
		args.add(asocName);
		PtcimObject res = null;
		res = new PtcimObject(root.items(asocName, null).queryInterface(IAutomationCaseObject.class));
		if (res != null) {
			return res.wrapInCollection(root, asocName);
		}
		return Collections.emptyList();
	}

	private List<PtcimObject> filterByType(List<PtcimObject> ptcCollection, String type) {
		List<PtcimObject> result = new ArrayList<PtcimObject>();
		Iterator<PtcimObject> it = ptcCollection.iterator();
		while (it.hasNext()) {
			PtcimObject e = it.next();
			Object etype = null;
			try {
				etype = e.property("Type", null);
				if (type.equals(etype)) {
					result.add(e);
				}
			} catch (com4j.ComException exc) {
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
		Com4jObject res_ = null;
		PtcimObject res = null;
		try {
			res_ = theProject.itemByID(id);
			if (res_ != null) {
				res = new PtcimObject(res_.queryInterface(IAutomationCaseObject.class));
				if (res != null)
					res.setId(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.IModel#getElementId(java.lang.Object)
	 */
	@Override
	public String getElementId(Object instance) {
		assert instance instanceof PtcimObject;
		String id = ((PtcimObject) instance).getId();
		if (id == null) {
			id = (String) ((PtcimObject) instance).property("Id", null);
			((PtcimObject) instance).setId(id);
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

	protected PtcimPropertyManager getPropertyManager() {
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
		assert instance instanceof PtcimObject;
		String typeName;
		typeName = (String) ((PtcimObject) instance).property("Type", null);
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
		assert instance instanceof PtcimObject;
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
			getter = new PtcimCachedPropertyGetter(manager, this);
			setter = new PtcimCachedPropertySetter(manager, this);
		} else {
			getter = new PtcimPropertyGetter(manager);
			setter = new PtcimPropertySetter(manager);
		}
		load();
	}
	
	public void loadDictionary() throws EolModelLoadingException {
		model = new PtcimObject(theProject.item("Dictionary", "Dictionary").queryInterface(IAutomationCaseObject.class));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.CachedModel#loadModel()
	 */
	@Override
	protected void loadModel() throws EolModelLoadingException {
		if (isInitialized()) {
				PtcimModelManager manager;
				try {
					manager = factory.getModelManager(false);
				} catch (Exception e1) {
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
				}
				else if (storeOnDisposal) {
					// TODO Decide how the readOnLoad/storeOnDisposal flags control how the model is either
					// loaded or created. 
				}
				loadDictionary();
			//}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.IModel#owns(java.lang.Object)
	 */
	@Override
	public boolean owns(Object instance) {
		if (instance instanceof PtcimObject) {
			if (((PtcimObject) instance).getId() == null) {
				throw new IllegalStateException("COMObjects can not be found without an Id");
			}
			Object other = getElementById(((PtcimObject) instance).getId());
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
	private void setNewInstanceId(PtcimObject newInstance) {
		String id = (String) newInstance.property("Id", null);
		newInstance.setId(id);
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
