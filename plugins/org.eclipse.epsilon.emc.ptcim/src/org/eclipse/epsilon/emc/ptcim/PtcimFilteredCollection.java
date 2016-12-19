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
package org.eclipse.epsilon.emc.ptcim;

import java.util.AbstractCollection;
import java.util.Iterator;

import org.eclipse.epsilon.eol.exceptions.EolInternalException;
import org.jawin.COMException;


/**
 * The Class JawinFilteredCollection.
 */
public class PtcimFilteredCollection extends AbstractCollection<PtcimObject> {
	
	/** The object that points to the collection. */
	private final PtcimObject comObject;
	
	private final String association;
	
	/**
	 * Instantiates a new jawin collection.
	 *
	 * @param comObject the com object
	 * @param association the association
	 */
	public PtcimFilteredCollection(PtcimObject comObject, String association) {
		assert comObject instanceof PtcimObject;
		this.comObject = (PtcimObject) comObject;
		this.association = association;
	}
	
	public void disconnect() throws EolInternalException {
		try {
			comObject.close();
		} catch (COMException e) {
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
	public PtcimObject getOwner() {
		return null;
	}

	public PtcimObject getCOMObject() {
		return this.comObject;
	}

	public boolean isFiltered() {
		return true;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#iterator()
	 */
	@Override
	public Iterator<PtcimObject> iterator() {
		Iterator<PtcimObject> iterator = new PtcimIterator(comObject);
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
		Iterator<PtcimObject> it = iterator();
		int size = 0;
		while (it.hasNext()) {
			it.next();
			size++;
		}
		return size;
	}
}
