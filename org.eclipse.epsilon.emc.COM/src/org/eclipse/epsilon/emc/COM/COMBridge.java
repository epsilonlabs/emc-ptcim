package org.eclipse.epsilon.emc.COM;

import java.util.Collection;

public interface COMBridge<S extends COMObject, T extends COMObject> {
	
	Collection<T> castToCollection(COMObject obj); 
	
	T connectToCOM(COMGuid clsid) throws EpsilonCOMException;
	
	T connectToCOM(String progid) throws EpsilonCOMException;

	
	/** Initalize a new COMModel from the given COMObject result 
	 * @return */
	COMModel wrapModel(COMObject res);

	void initialiseCOM() throws EpsilonCOMException;
	
	// FIXME move this to COMApp (create the class)
	T openModel(S app, String name) throws EpsilonCOMException;
	
	void uninitializeCOM() throws EpsilonCOMException;
	
}
