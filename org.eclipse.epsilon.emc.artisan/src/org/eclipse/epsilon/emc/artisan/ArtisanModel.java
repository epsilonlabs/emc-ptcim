package org.eclipse.epsilon.emc.artisan;

import java.util.Collection;

import org.eclipse.epsilon.emc.COM.COMBridge;
import org.eclipse.epsilon.emc.COM.COMModel;
import org.eclipse.epsilon.emc.COM.COMObject;
import org.eclipse.epsilon.emc.COM.EpsilonCOMException;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.exceptions.models.EolEnumerationValueNotFoundException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelElementTypeNotFoundException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.exceptions.models.EolNotInstantiableModelElementTypeException;
import org.eclipse.epsilon.eol.models.CachedModel;

public class ArtisanModel extends CachedModel<Object> {
	
	// FIXME How to inject the correct implementation?
	COMBridge<?, ?> bridge;
	
	private boolean isInitialized = false;
	
	/** The ArtisanModel */
	private COMModel model;
	
	@Override
	protected Collection<? extends Object> allContentsFromModel() {
		// TODO Auto-generated method stub
		return null;
	}

	private void connectToModel(String name) {
		COMObject artisanApp = bridge.connectToCOM(name);
		
	}

	private void connectToModelviaReference(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Object createInstanceInModel(String type)
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
	protected Collection<? extends Object> getAllOfKindFromModel(String kind)
			throws EolModelElementTypeNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Collection<? extends Object> getAllOfTypeFromModel(String type)
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
		// TODO Auto-generated method stub
		return null;
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
		if(name.contains("\\")) {
			connectToModelviaReference(name);
		}
		else {
			connectToModel(name);
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
