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
package org.eclipse.epsilon.emc.artisan.jawin;

import java.util.AbstractCollection;
import java.util.Iterator;

import org.eclipse.epsilon.emc.COM.COMCollection;
import org.eclipse.epsilon.emc.COM.COMObject;
import org.eclipse.epsilon.emc.COM.EpsilonCOMException;


/**
 * The Class JawinFilteredCollection.
 */
public class JawinFilteredCollection extends AbstractCollection<JawinObject> implements COMCollection {
	
	/** The object that points to the collection. */
	private final JawinObject comObject;
	
	/** The association. */
	private final String association;
	
	/**
	 * Instantiates a new jawin collection.
	 *
	 * @param comObject the com object
	 * @param association the association
	 */
	public JawinFilteredCollection(COMObject comObject, String association) {
		assert comObject instanceof JawinObject;
		this.comObject = (JawinObject) comObject;
		this.association = association;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.COM.COMCollection#getAssociation()
	 */
	@Override
	public String getAssociation() {
		return this.association;
	}

	
	/**
	 * Filtered Collections don't have an owner.
	 *
	 * @return the owner
	 */
	@Override
	public COMObject getOwner() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.COM.COMCollection#getCOMObject()
	 */
	@Override
	public COMObject getCOMObject() {
		return this.comObject;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.COM.COMCollection#isFiltered()
	 */
	@Override
	public boolean isFiltered() {
		return true;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#iterator()
	 */
	@Override
	public Iterator<JawinObject> iterator() {
		Iterator<JawinObject> iterator = new JawinIterator(comObject);
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
		Iterator<JawinObject> it = iterator();
		int size = 0;
		while (it.hasNext()) {
			it.next();
			size++;
		}
		return size;
	}

}
