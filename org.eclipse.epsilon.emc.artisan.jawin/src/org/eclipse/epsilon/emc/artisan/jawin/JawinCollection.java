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
package org.eclipse.epsilon.emc.artisan.jawin;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.epsilon.emc.COM.COMObject;
import org.eclipse.epsilon.emc.COM.EpsilonCOMException;

/**
 * The Class JawinCollection.
 */
public class JawinCollection extends AbstractCollection<JawinObject> {
	
	/** The source. */
	private final JawinObject source;
	
	/** The owner. */
	private final JawinObject owner;
	
	/** The association. */
	private final String association;
	
	/**
	 * Instantiates a new jawin collection.
	 *
	 * @param comCollection the source
	 * @param owner2 the owner
	 * @param association the association
	 */
	public JawinCollection(COMObject comCollection, COMObject owner, String association) {
		assert comCollection instanceof JawinObject;
		assert owner instanceof JawinObject;
		this.source = (JawinObject) comCollection;
		this.owner = (JawinObject) owner;
		this.association = association;
	}
	

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#add(java.lang.Object)
	 */
	@Override
	public boolean add(JawinObject e) {
		assert e.getId() != null;
		try {
			List<Object> args = new ArrayList<Object>();
			args.add(e);
			owner.invoke("Add", association, args);
		} catch (EpsilonCOMException ex) {
			// TODO Can we check if message has 'Failed to add item' and do a
			// objItem.Property("ExtendedErrorInfo") to get more info?
			throw new IllegalStateException(ex);
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#iterator()
	 */
	@Override
	public Iterator<JawinObject> iterator() {
		Iterator<JawinObject> iterator = new JawinIterator(source);
		return iterator;
	}


	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Object o) {
		//COMObject resCount;
		try {
			List<Object> args = new ArrayList<Object>();
			args.add(o);
			owner.invoke("Remove", association, args);
		} catch (EpsilonCOMException ex) {
			throw new IllegalStateException(ex);
		}
		return true;
	}


	/**
	 * The Artisan API does not provide this functionality.
	 *
	 * @param c the c
	 * @return true, if successful
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("This collection does not allow this operation");
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size() {
		Object resCount;
		try {
			resCount = owner.invoke("ItemCount", association, Collections.emptyList());
		} catch (EpsilonCOMException e) {
			throw new IllegalStateException(e);
		}
		return (Integer)resCount;
	}
}
