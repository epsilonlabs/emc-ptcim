/*******************************************************************************
 * Copyright (c) 2016 University of York
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     ${me} - Initial API and implementation
 *******************************************************************************/
package org.eclipse.epsilon.emc.ptcim.ole;

import java.util.Collection;
import java.util.List;

import org.eclipse.epsilon.emc.ptcim.ole.impl.EpsilonCOMException;

/**
 * The IPtcObject Interface provides the high level methods to be used on PTC IM
 * objects.
 */
public interface IPtcObject {
	
	
	/**
	 * Creates an object using the specified association and returns the new object.
	 *
	 * @param association the association
	 * @return the COM object
	 * @throws EpsilonCOMException the epsilon COM exception
	 */
	Object add(String association) throws EpsilonCOMException;
	
	/**
	 * Adds a link to the existing object using a specified association.
	 *
	 * @param association the association
	 * @param object the object
	 * @return the COM object
	 * @throws EpsilonCOMException the epsilon COM exception
	 */
	Object add(String association, IPtcObject object) throws EpsilonCOMException;
	
	/**
	 * A new object is created with its default property set to the value passed,
	 * if the multiplicity and name uniqueness rules for the requested association allow.
	 * If an object already exists with the value passed, the existing object is returned.
	 *
	 * @param association the association
	 * @param name the name
	 * @return the COM object
	 * @throws EpsilonCOMException the epsilon COM exception
	 */
	Object add(String association, String name) throws EpsilonCOMException;
	
	/**
	 * Creates an object of a specified type through a specified association and returns the new object.
	 *
	 * @param association the association
	 * @param type the type
	 * @return the COM object
	 * @throws EpsilonCOMException the epsilon COM exception
	 */
	Object addByType(String association, String type) throws EpsilonCOMException;

	/**
	 * Let COM decrease the reference count for the COM-object, and ultimately destroy the object.
	 */
	void disconnect() throws EpsilonCOMException;
	
	/**
	 * Returns the value of the given property in the IPtcObject.
	 *
	 * @param attrName the attr name
	 * @param args the args
	 * @return the object
	 * @throws EpsilonCOMException the epsilon COM exception
	 */
	Object getAttribute(String attrName, List<Object> args) throws EpsilonCOMException;
	
	/**
	 * Returns the value of the given property in the IPtcObject.
	 *
	 * @param name the name
	 * @param arg the arg
	 * @return the object
	 * @throws EpsilonCOMException the epsilon COM exception
	 */
	Object getAttribute(String name, Object arg) throws EpsilonCOMException;
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	String getId();
	
	/**
	 * Invoke a method in the IPtcObject.
	 *
	 * @param string the string
	 * @param args the args
	 * @return the COM object
	 * @throws EpsilonCOMException the epsilon COM exception
	 */
	Object invoke(String string, List<Object> args) throws EpsilonCOMException;

	/**
	 * Invoke a method on the object. The args arguments are passed directly to the COM object. byRefArgs are
	 * wrapped in an implementation specific "by reference" wrapper. 
	 *
	 * @param methodName the method name
	 * @param args the args
	 * @param byRefArgs the by ref args
	 * @return the object
	 * @throws EpsilonCOMException the epsilon COM exception
	 */
	Object invoke(String methodName, List<Object> args, List<Object> byRefArgs) throws EpsilonCOMException;

	/**
	 * Invoke a method on the object. The args arguments are passed directly to the COM object. byRefArgs are
	 * wrapped in an implementation specific "by reference" wrapper. If the return value(s) is(are) by reference,
	 * the argsExpected parameter can be used to specify how many return arguments are expected. This should be
	 * bigger that the combined size of args and byRefArgs.  
	 *
	 * @param methodName the method name
	 * @param args the args
	 * @param byRefArgs the by ref args
	 * @param argsExpected the args expected
	 * @return the object
	 * @throws EpsilonCOMException the epsilon COM exception
	 */
	Object invoke(String methodName, List<Object> args, List<Object> byRefArgs, int argsExpected) throws EpsilonCOMException;

	/**
	 * Invoke a method in the IPtcObject.
	 *
	 * @param string the string
	 * @return the COM object
	 * @throws EpsilonCOMException the epsilon COM exception
	 */
	Object invokeMethod(String string) throws EpsilonCOMException;
	
	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	void setId(String id);

	/**
	 * Wrap in collection. The returned collection is directly controlled by the
	 * PTC IM service.
	 *
	 * @param owner the owner
	 * @param association the association
	 * @return the list<? extends IPtcObject>
	 */
	List<? extends IPtcObject> wrapInColleciton(IPtcObject owner, String association);
	
	/**
	 * Wrap in filtered collection. The returned collection is a Java collection.
	 *
	 * @param association the association
	 * @return the collection<? extends IPtcObject>
	 */
	Collection<? extends IPtcObject> wrapInFilteredColleciton(String association);
	
	
}
