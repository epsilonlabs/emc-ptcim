/*******************************************************************************
 * Copyright (c) 2016 University of York
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Horacio Hoyos - Initial API and implementation
 *******************************************************************************/
package org.eclipse.epsilon.emc.ptcim.ole;

import java.util.Collection;

import org.eclipse.epsilon.emc.ptcim.ole.impl.EpsilonCOMException;

/**
 * The Interface IPtcComBridge.
 *
 * @param <S> the generic type
 * @param <T> the generic type
 */
public interface IPtcComBridge<T extends IPtcObject> {
	
	/**
	 * Connect to COM Object using a CLSID
	 *
	 * @param clsid the CLSID in the format {4 bytes - 2 bytes - 2 bytes - 2 bytes - 6 bytes}
	 * @return the COM object
	 * @throws EpsilonCOMException If an error using the COM connection
	 */
	T connectByClsId(String clsid) throws EpsilonCOMException;
	
	/**
	 * Connect to COM Object using the Program Id .
	 *
	 * @param progId the program id of the form &lt;vendor&gt;.&lt;component&gt;.&lt;version&gt;
	 * @return the t
	 * @throws EpsilonCOMException If an error using the COM connection
	 */
	T connectByProgId(String progId) throws EpsilonCOMException;

	
	/**
	 * Initialise COM.
	 *
	 * @throws EpsilonCOMException If an error using the COM connection
	 */
	void initialiseCOM() throws EpsilonCOMException;

	
	/**
	 * Uninitialise COM.
	 *
	 * @throws EpsilonCOMException If an error using the COM connection
	 */
	void uninitialiseCOM() throws EpsilonCOMException;
	
}
