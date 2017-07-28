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
package org.eclipse.epsilon.emc.ptcim;

import java.util.Iterator;

public class PtcimIterator implements Iterator<PtcimObject> {
	
	/**
	 * The object that points to the collection.
	 */
	private final PtcimObject source;
	
	/** The next object returned from {@link #next()}. */
	private PtcimObject next;
	
	/**
	 * Instantiates a new iterator.
	 *
	 * @param source the source
	 * @param owner the owner
	 * @param association the association
	 */
	public PtcimIterator(PtcimObject source) {
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
	public PtcimObject next() {
		next = new PtcimObject(source.nextItem().queryInterface(IAutomationCaseObject.class));
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
