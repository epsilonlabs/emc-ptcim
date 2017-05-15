/*******************************************************************************
 * Copyright (c) 2016 University of York
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Hoacio Hoyos Rodriguez - Initial API and implementation
 *******************************************************************************/
package org.eclipse.epsilon.emc.ptcim.com4j;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.eclipse.epsilon.eol.exceptions.EolInternalException;

public class Com4jPtcimIterator implements Iterator<Com4jPtcimObject> {
	
	/**
	 * The object that points to the collection.
	 */
	private final Com4jPtcimObject source;
	
	/** The next object returned from {@link #next()}. */
	private Com4jPtcimObject next;
	
	/**
	 * Instantiates a new iterator.
	 *
	 * @param source the source
	 * @param owner the owner
	 * @param association the association
	 */
	public Com4jPtcimIterator(Com4jPtcimObject source) {
		super();
		this.source = source;
		// Make sure the iterator is at the beginning of the collection
		source.resetQueryItems();
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		Object more;
		more = source.moreItems();
		int val = (Integer)more;
		return val != 0;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	@Override
	public Com4jPtcimObject next() {
		next = new Com4jPtcimObject(source.nextItem().queryInterface(IAutomationCaseObject.class));
		String strId = (String) next.property("Id", null);
		next.setId(strId);
		return next;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException("Not supported");
	}
}
