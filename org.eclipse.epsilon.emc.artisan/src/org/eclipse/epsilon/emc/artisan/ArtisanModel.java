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
import java.util.Collections;
import java.util.List;

import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.emc.COM.COMBridge;
import org.eclipse.epsilon.emc.COM.COMModel;
import org.eclipse.epsilon.emc.COM.COMObject;
import org.eclipse.epsilon.emc.COM.EpsilonCOMException;
import org.eclipse.epsilon.emc.artisan.jawin.JawinComBridge;
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
	
	/** The bridge. */
	// FIXME How to inject the correct implementation?
	COMBridge<COMObject, COMObject> bridge;
	
	/** The Project, needed for type testing and instantiation */ 
	private COMObject theProject;
	
	/** The is initialized. */
	private boolean isInitialized = false;
	
	/**  The ArtisanModel. */
	private COMModel model = null;
	
	
	/** The property getter. */
	// FIXME How to inject the corect implementation?
	private IPropertyGetter propertyGetter;

	/** The property setter. */
	private IPropertySetter propertySetter;

	
	protected void setTheProject(COMObject theProject) {
		this.theProject = theProject;
	}

	protected void setModel(COMModel model) {
		this.model = model;
	}

	public ArtisanModel() {
		this(new JawinComBridge());
	}
	
	/**
	 * Instantiates a new artisan model.
	 *
	 * @param bridge the bridge
	 */
	public ArtisanModel(COMBridge<COMObject, COMObject> bridge) {
		super();
		// Can we have an extension point for this?
		this.bridge = bridge;
		cachingEnabled = false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.CachedModel#allContentsFromModel()
	 */
	@Override
	protected Collection<COMObject> allContentsFromModel() {
		if (!isInitialized) {
			// FIXME throw exception?
			return Collections.emptyList();
		}
		assert model != null;
		Collection<? extends COMObject> elements;
		List<Object> args = new ArrayList<Object>();
		args.add("*");
		try {
			COMObject res = (COMObject) model.invoke("Items", "Owned Contents", args, 2);
			elements = model.wrapInColleciton(res, model, "Owned Contents");
		} catch (EpsilonCOMException e) {
			throw new IllegalStateException(e);
		}
		return (Collection<COMObject>) elements;
	}

	/**
	 * In Artisan the type is the same name as the association name in the model (dictionary)
	 * Specialised classes are obtained by using attribute settings (see AddByType)
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

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.CachedModel#deleteElementInModel(java.lang.Object)
	 */
	@Override
	protected boolean deleteElementInModel(Object instance) throws EolRuntimeException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.CachedModel#disposeModel()
	 */
	@Override
	protected void disposeModel() {
		if (isInitialized) {
			try {
				bridge.uninitializeCOM();
			} catch (EpsilonCOMException e) {
				// FIXME Does Epsilon has a logger for this?
				//e.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.CachedModel#getAllOfKindFromModel(java.lang.String)
	 */
	@Override
	protected Collection<COMObject> getAllOfKindFromModel(String kind)
			throws EolModelElementTypeNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Collection<COMObject> getAllOfKind(String kind) throws EolModelElementTypeNotFoundException {
		throw new UnsupportedOperationException("Access to the Artisan Model Metamodel is restricted.");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.CachedModel#getAllOfType(java.lang.String)
	 */
	@Override
	public Collection<COMObject> getAllOfType(String type) throws EolModelElementTypeNotFoundException {
		if (!isInitialized) {
			// FIXME throw exception?
			return Collections.emptyList();
		}
		assert model != null;
		Collection<? extends COMObject> elements;
		List<Object> args = new ArrayList<Object>();
		args.add("*");
		try {
			COMObject res = (COMObject) model.invoke("Items", type, args, 2);
			elements = model.wrapInColleciton(res, model, type);	//new JawinCollection(res, model, type);
		} catch (EpsilonCOMException e) {
			throw new EolModelElementTypeNotFoundException(name, type);
		}
		return (Collection<COMObject>) elements;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.CachedModel#getAllOfTypeFromModel(java.lang.String)
	 */
	@Override
	protected Collection<? extends COMObject> getAllOfTypeFromModel(String type)
			throws EolModelElementTypeNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.CachedModel#getAllTypeNamesOf(java.lang.Object)
	 */
	@Override
	protected Collection<String> getAllTypeNamesOf(Object instance) {
		throw new UnsupportedOperationException("Artisan Model does not support enumerations");
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
		try {
			return model.invoke("ItemById", args);
		} catch (EpsilonCOMException e) {
			// FIXME Log me!
			e.printStackTrace();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.models.IModel#getElementId(java.lang.Object)
	 */
	@Override
	public String getElementId(Object instance) {
		assert instance instanceof COMObject;
		if (((COMObject) instance).getId() != null) {
			return ((COMObject) instance).getId();
		}
		else {
			List<Object> args = new ArrayList<Object>();
			args.add("Id");
			String id = null;
			try {
				id = (String) ((COMObject) instance).get("Property", args);
				((COMObject) instance).setId(id);
			} catch (EpsilonCOMException e) {
				// FIXME Log me!
				e.printStackTrace();
			}
			return id;
		}
		
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
		// TODO Auto-generated method stub
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
		// TODO if the model is not read on load we need to create it
		/*
		 * Set objManager = CreateObject("Studio.ModelManager")
		 * objManager.AddModel("\\Enabler\MyServer\MyRepository","MyModel")
		 */
		if (!isInitialized) {
			try {
				bridge.initialiseCOM();
			} catch (EpsilonCOMException e) {
				throw new EolModelLoadingException(e, this);
			}
		}
		isInitialized = true;
		COMObject artisanApp;
		try {
			artisanApp = bridge.connectToCOM("OMTE.Projects");
		} catch (EpsilonCOMException e) {
			throw new EolModelLoadingException(e, this);
		}
		try {
			theProject = bridge.openModel(artisanApp, name);
		} catch (EpsilonCOMException e) {
			throw new EolModelLoadingException(e, this);
		}
		List<Object> args = new ArrayList<Object>();
		args.add("Dictionary");
		try {
			COMObject res = (COMObject) theProject.invoke("Item", "Dictionary", args);
			model = bridge.wrapModel(res);
		} catch (EpsilonCOMException e) {
			throw new EolModelLoadingException(e, this);
		}
		
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
		throw new UnsupportedOperationException("Artisan objects' Id is read only.");
		
	}

	/**
	 * Sets the property getter.
	 *
	 * @param propertyGetter the new property getter
	 */
	public void setPropertyGetter(IPropertyGetter propertyGetter) {
		this.propertyGetter = propertyGetter;
	}

	/**
	 * Sets the property setter.
	 *
	 * @param propertySetter the new property setter
	 */
	public void setPropertySetter(IPropertySetter propertySetter) {
		this.propertySetter = propertySetter;
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
