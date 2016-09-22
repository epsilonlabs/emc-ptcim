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
 * The Interface IPtcCollection defines the methods to work with collections that are provided
 * directly by the PTC IM API.
 */
public interface IPtcCollection<T extends IPtcObject> extends Collection<T> {

	/**
	 * Let COM decrease the reference count for the COM-object, and ultimately destroy the object.
	 */
	void disconnect() throws EpsilonCOMException;

	/**
	 * Gets the association name used to generate the collection.
	 *
	 * @return the association
	 */
	String getAssociation();

	/**
	 * Gets the object that points to the collection.
	 *
	 * @return the source
	 */
	T getCOMObject();
	
	/**
	 * Gets the owner of the association.
	 *
	 * @return the owner
	 */
	T getOwner();
	
	/**
	 * Returns true if the Collection has been filtered. Optimisations can't be done on filtered
	 * collections. 
	 *
	 * @return true, if is filtered
	 */
	boolean isFiltered();
		

}