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
package org.eclipse.epsilon.emc.ptcim.jawin;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.eclipse.epsilon.emc.ptcim.ole.impl.EpsilonCOMException;


/**
 * The Class JawinIterator.
 *
 * @param <E> the element type
 */
public class JawinIterator implements Iterator<JawinObject> {
	
	/**
	 * The IPtcObject that points to the collection.
	 */
	private final JawinObject source;
	
	/** The next object returned from {@link #next()}. */
	private JawinObject next;
	
	/**
	 * Instantiates a new jawin iterator.
	 *
	 * @param source the source
	 * @param owner the owner
	 * @param association the association
	 */
	public JawinIterator(JawinObject source) {
		super();
		this.source = source;
		try {
			// Make sure the iterator is at the beginning of the collection
			source.invokeMethod("ResetQueryItems");
		} catch (EpsilonCOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		
		Object more;
		try {
			more = source.invokeMethod("MoreItems");
		} catch (EpsilonCOMException e) {
			// FIXME this should be logged
			return false;
		}
		int val = (Integer)more;
		return val != 0;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	@Override
	public JawinObject next() {
		try {
			next = (JawinObject) source.invokeMethod("NextItem");
			String strId = (String) next.getAttribute("Property", "Id");
			next.setId(strId);
		} catch (EpsilonCOMException e) {
			throw new NoSuchElementException(e.getMessage());
		}
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
