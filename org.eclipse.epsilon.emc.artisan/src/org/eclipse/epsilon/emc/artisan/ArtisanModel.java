package org.eclipse.epsilon.emc.artisan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.epsilon.emc.COM.COMBridge;
import org.eclipse.epsilon.emc.COM.COMModel;
import org.eclipse.epsilon.emc.COM.COMObject;
import org.eclipse.epsilon.emc.COM.EpsilonCOMException;
import org.eclipse.epsilon.emc.artisan.jawin.JawinCollection;
import org.eclipse.epsilon.emc.artisan.jawin.JawinObject;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.exceptions.models.EolEnumerationValueNotFoundException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelElementTypeNotFoundException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.exceptions.models.EolNotInstantiableModelElementTypeException;
import org.eclipse.epsilon.eol.execute.introspection.IPropertyGetter;
import org.eclipse.epsilon.eol.execute.introspection.IPropertySetter;
import org.eclipse.epsilon.eol.models.CachedModel;

public class ArtisanModel extends CachedModel<COMObject> {
	
	// FIXME How to inject the correct implementation?
	COMBridge<COMObject, COMObject> bridge;
	
	private boolean isInitialized = false;
	
	/** The ArtisanModel */
	private COMModel model = null;
	
	
	// FIXME How to inject the corect implementation?
	private IPropertyGetter propertyGetter;

	private IPropertySetter propertySetter;
	
	
	public ArtisanModel(COMBridge<COMObject, COMObject> bridge) {
		super();
		// Can we have an extension point for this?
		this.bridge = bridge;
		cachingEnabled = false;
	}

	@Override
	protected Collection<COMObject> allContentsFromModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected COMObject createInstanceInModel(String type)
			throws EolModelElementTypeNotFoundException, EolNotInstantiableModelElementTypeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean deleteElementInModel(Object instance) throws EolRuntimeException {
		// TODO Auto-generated method stub
		return false;
	}

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

	@Override
	protected Collection<COMObject> getAllOfKindFromModel(String kind)
			throws EolModelElementTypeNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Collection<COMObject> getAllOfType(String type) throws EolModelElementTypeNotFoundException {
		if (!isInitialized) {
			// FIXME through exception?
			return Collections.emptyList();
		}
		assert model != null;
		JawinCollection<COMObject> elements;
		List<Object> args = new ArrayList<Object>();
		args.add("*");
		try {
			COMObject res = model.invoke("Items", type, args, 2);
			elements = new JawinCollection<COMObject>(res, model, type);
		} catch (EpsilonCOMException e) {
			throw new EolModelElementTypeNotFoundException(name, type);
		}
		return elements;
	}


	@Override
	protected Collection<? extends COMObject> getAllOfTypeFromModel(String type)
			throws EolModelElementTypeNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Collection<String> getAllTypeNamesOf(Object instance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object getCacheKeyForType(String type) throws EolModelElementTypeNotFoundException {
		return type;
	}

	@Override
	public Object getElementById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getElementId(Object instance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getEnumerationValue(String enumeration, String label) throws EolEnumerationValueNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPropertyGetter getPropertyGetter() {
		return propertyGetter;
	}
	

	@Override
	public IPropertySetter getPropertySetter() {
		return propertySetter;
	}

	@Override
	public String getTypeNameOf(Object instance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasType(String type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInstantiable(String type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void loadModel() throws EolModelLoadingException {
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
		COMObject modelRef;
		try {
			modelRef = bridge.openModel(artisanApp, name);
		} catch (EpsilonCOMException e) {
			throw new EolModelLoadingException(e, this);
		}
		
		List<Object> args = new ArrayList<Object>();
		args.add("Dictionary");
		try {
			COMObject res = modelRef.invoke("Item", "Dictionary", args);
			model = bridge.wrapModel(res);
		} catch (EpsilonCOMException e) {
			throw new EolModelLoadingException(e, this);
		}
		
	}

	@Override
	public boolean owns(Object instance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setElementId(Object instance, String newId) {
		// TODO Auto-generated method stub
		
	}

	public void setPropertyGetter(IPropertyGetter propertyGetter) {
		this.propertyGetter = propertyGetter;
	}

	public void setPropertySetter(IPropertySetter propertySetter) {
		this.propertySetter = propertySetter;
	}

	@Override
	public boolean store() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean store(String location) {
		// TODO Auto-generated method stub
		return false;
	}

}
