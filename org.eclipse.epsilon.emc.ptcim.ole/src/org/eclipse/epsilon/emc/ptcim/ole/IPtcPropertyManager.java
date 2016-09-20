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

import org.eclipse.epsilon.emc.ptcim.ole.impl.PtcProperty;

/**
 * The Interface IPtcPropertyManager defines the methods to manage properties.
 * Management includes cached access of property definitions and cached access
 * to properties values. 
 */
public interface IPtcPropertyManager {

	/**
	 * Gets the single instance of IPtcPropertyManager.
	 *
	 * @return single instance of IPtcPropertyManager
	 */
	IPtcPropertyManager getInstance();

	/**
	 * Gets the property.
	 *
	 * @param object the object
	 * @param property the property
	 * @return the property
	 */
	PtcProperty getProperty(IPtcObject object, String property);
	
	/**
	 *  Clear the cache.
	 */
	void dispose();

}