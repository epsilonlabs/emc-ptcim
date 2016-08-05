package org.eclipse.epsilon.emc.COM;


public interface COMBridge<S extends COMObject, T extends COMObject> {
	/**
	 * Invoke a method in the COMObject
	 * @param comObject
	 * @param methodName
	 * @param args
	 * @return
	 */
	T invoke(S comObject, String methodName, Object... args);
	
	/**
	 * Returns the value of the given property in the COMObject
	 * @param comObject
	 * @param attrName
	 * @param args
	 * @return
	 */
	T get(S comObject, String attrName, Object... args);
	
	T connectToCOM(String progid);
	
	T connectToCOM(COMGuid clsid);
	
	void initialiseCOM() throws EpsilonCOMException;
	
	void uninitializeCOM() throws EpsilonCOMException;
	
}
