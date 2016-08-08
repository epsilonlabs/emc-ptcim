package org.eclipse.epsilon.emc.COM;

import java.util.List;


public interface COMObject {

	/**
	 * Returns the value of the given property in the COMObject
	 * @param comObject
	 * @param attrName
	 * @param args
	 * @return
	 */
	Object get(String attrName, List<Object> args) throws EpsilonCOMException;
	
	COMObject invoke(String string) throws EpsilonCOMException;
	
	COMObject invoke(String string, List<Object> args) throws EpsilonCOMException;

	/**
	 * Invoke a method in the COMObject
	 * @param comObject
	 * @param methodName
	 * @param args
	 * @return
	 */
	COMObject invoke(String methodName, String type, List<Object> args) throws EpsilonCOMException;

	COMObject invoke(String methodName, String type, List<Object> args, int index) throws EpsilonCOMException;

	Object get(String name, Object arg) throws EpsilonCOMException;
	
	// This info is needed to access elements after the connection is lost
//	String getId();
//	COMModel getModel();
}
