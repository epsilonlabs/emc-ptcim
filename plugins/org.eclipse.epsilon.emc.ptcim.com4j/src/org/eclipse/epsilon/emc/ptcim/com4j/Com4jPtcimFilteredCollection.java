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
package org.eclipse.epsilon.emc.ptcim.com4j;

import java.util.AbstractCollection;
import java.util.Iterator;

import org.eclipse.epsilon.eol.exceptions.EolInternalException;


public class Com4jPtcimFilteredCollection extends AbstractCollection<Com4jPtcimObject> {
	
	/** The object that points to the collection. */
	private final Com4jPtcimObject comObject;
	
	private final String association;
	
	/**
	 * Instantiates a new collection.
	 *
	 * @param comObject the com object
	 * @param association the association
	 */
	public Com4jPtcimFilteredCollection(Com4jPtcimObject comObject, String association) {
		assert comObject instanceof Com4jPtcimObject;
		this.comObject = (Com4jPtcimObject) comObject;
		this.association = association;
	}
	
	public void disconnect() throws EolInternalException {
		try {
			System.out.println("Closing");
			//comObject.close();
		} catch (Exception e) {
			throw new EolInternalException(e);
		}
	}

	public String getAssociation() {
		return this.association;
	}

	/**
	 * Filtered Collections don't have an owner.
	 *
	 * @return the owner
	 */
	public Com4jPtcimObject getOwner() {
		return null;
	}

	public Com4jPtcimObject getCOMObject() {
		return this.comObject;
	}

	public boolean isFiltered() {
		return true;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#iterator()
	 */
	@Override
	public Iterator<Com4jPtcimObject> iterator() {
		Iterator<Com4jPtcimObject> iterator = new Com4jPtcimIterator(comObject);
		return iterator;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	/**
	 * Filtered collections can not be queried for size from their owner. This
	 * implementation used the iterator;
	 */
	public int size() {
		Iterator<Com4jPtcimObject> it = iterator();
		int size = 0;
		while (it.hasNext()) {
			it.next();
			size++;
		}
		return size;
	}
}
