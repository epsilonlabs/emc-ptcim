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


/**
 * The Interface IPtcCollection defines the methods to work with collections that are provided
 * directly by the PTC IM API.
 */
public interface IPtcCollection {

	/**
	 * Gets the association name used to generate the collection.
	 *
	 * @return the association
	 */
	String getAssociation();

	/**
	 * Gets the owner of the association.
	 *
	 * @return the owner
	 */
	IPtcObject getOwner();

	/**
	 * Gets the object that points to the collection.
	 *
	 * @return the source
	 */
	IPtcObject getCOMObject();
	
	/**
	 * Returns true if the Collection has been filtered. Optimisations can't be done on filtered
	 * collections. 
	 *
	 * @return true, if is filtered
	 */
	boolean isFiltered();
		

}