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

import org.eclipse.epsilon.emc.ptcim.ole.impl.EpsilonCOMException;

/**
 * The Interface IPtcApp provides the methods to connect to PTC IM applications.
 */
public interface IPtcApp<T extends IPtcObject> {
	
	/**
	 * Connect to a specific PTC IM application/program. Implementations
	 * are responsible for specifying the correct CLSID, IID and ProgID to
	 * use for the connection.
	 *
	 * @param bridge the bridge
	 */
	void connect(IPtcComBridge<T> bridge) throws EpsilonCOMException;
	
	
	/**
	 * Disconnect from the app. Implementations should close the COM object
	 * created during the connect method. 
	 * @throws EpsilonCOMException 
	 */
	void disconnect() throws EpsilonCOMException;

}
