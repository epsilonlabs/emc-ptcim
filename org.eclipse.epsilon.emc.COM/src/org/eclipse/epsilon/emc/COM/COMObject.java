package org.eclipse.epsilon.emc.COM;

import java.util.List;


// TODO: Auto-generated Javadoc
/**
 * The Interface COMObject.
 */
public interface COMObject {
	
	
	/**
	 * Creates an object using the specified association and returns the new object.
	 *
	 * @param association the association
	 * @return the COM object
	 */
	Object add(String association) throws EpsilonCOMException;
	
	/**
	 * Adds a link to the existing object using a specified association.
	 *
	 * @param association the association
	 * @param object the object
	 * @return the COM object
	 * @throws EpsilonCOMException 
	 */
	Object add(String association, COMObject object) throws EpsilonCOMException;
	
	/**
	 * A new object is created with its default property set to the value passed,
	 * if the multiplicity and name uniqueness rules for the requested association allow.
	 * If an object already exists with the value passed, the existing object is returned.
	 *
	 * @param association the association
	 * @param name the name
	 * @return the COM object
	 */
	Object add(String association, String name) throws EpsilonCOMException;
	
	/**
	 * Creates an object of a specified type through a specified association and returns the new object.
	 *
	 * @param association the association
	 * @param type the type
	 * @return the COM object
	 */
	Object addByType(String association, String type) throws EpsilonCOMException;

	/**
	 * Returns the value of the given property in the COMObject.
	 *
	 * @param attrName the attr name
	 * @param args the args
	 * @return the object
	 * @throws EpsilonCOMException the epsilon COM exception
	 */
	Object get(String attrName, List<Object> args) throws EpsilonCOMException;
	
	/**
	 * Returns the value of the given property in the COMObject.
	 *
	 * @param name the name
	 * @param arg the arg
	 * @return the object
	 * @throws EpsilonCOMException the epsilon COM exception
	 */
	Object get(String name, Object arg) throws EpsilonCOMException;
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	String getId();
	
	/**
	 * Invoke a method in the COMObject.
	 *
	 * @param string the string
	 * @return the COM object
	 * @throws EpsilonCOMException the epsilon COM exception
	 */
	Object invoke(String string) throws EpsilonCOMException;
	
	/**
	 * Invoke a method in the COMObject.
	 *
	 * @param string the string
	 * @param args the args
	 * @return the COM object
	 * @throws EpsilonCOMException the epsilon COM exception
	 */
	Object invoke(String string, List<Object> args) throws EpsilonCOMException;

	Object invoke(String methodName, String arg) throws EpsilonCOMException;

	/**
	 * Invoke a method in the COMObject.
	 *
	 * @param methodName the method name
	 * @param type the type
	 * @param args the args
	 * @return the COM object
	 * @throws EpsilonCOMException the epsilon COM exception
	 */
	Object invoke(String methodName, String type, List<Object> args) throws EpsilonCOMException;

	/**
	 * Invoke.
	 *
	 * @param methodName the method name
	 * @param type the type
	 * @param args the args
	 * @param index the index
	 * @return the COM object
	 * @throws EpsilonCOMException the epsilon COM exception
	 */
	Object invoke(String methodName, String type, List<Object> args, int index) throws EpsilonCOMException;

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	void setId(String id);
	
	
}
