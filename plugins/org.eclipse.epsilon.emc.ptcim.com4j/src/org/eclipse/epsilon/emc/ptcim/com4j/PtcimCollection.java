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

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.epsilon.eol.exceptions.EolInternalException;
import org.eclipse.epsilon.eol.execute.operations.AbstractOperation;
import org.eclipse.epsilon.eol.execute.operations.declarative.IAbstractOperationContributor;

/**
 * This collection only guarantees the implementation
 * of the methods used by Epsilon (Sequence type). All other operations can produce
 * unexpected results. Collections that are a result of a filtered Items only
 * provide iteration. 
 */
public class PtcimCollection extends AbstractList<PtcimObject> implements IAbstractOperationContributor {
	
	/** The object that points to the collection. */
	private final PtcimObject comObject;
	
	/** The owner. */
	private final PtcimObject owner;
	
	/** The association. */
	private final String association;
	
	/**
	 * Instantiates a new collection.
	 *
	 * @param comCollection the source
	 * @param owner the owner
	 * @param association the association
	 */
	public PtcimCollection(PtcimObject comCollection, PtcimObject owner, String association) {
		assert comCollection instanceof PtcimObject;
		assert owner instanceof PtcimObject;
		this.comObject = (PtcimObject) comCollection;
		this.owner = (PtcimObject) owner;
		this.association = association;
	}

	@Override
	public boolean add(PtcimObject e) {
		assert e.getId() != null;
		List<Object> args = new ArrayList<Object>();
		args.add(association);
		args.add(e);
		return true;
	}
	/**
	 * Important:  If you remove objects from an association that has its Propagate Delete flag set to TRUE,
	 * the objects will be deleted from the model. For example, a Class is related to its child Attributes
	 * through the Attribute association, which has its Propagate Delete flag set to TRUE.
	 * If you use the Remove function to remove owned Attributes from a Class, those Attributes will be
	 * deleted from the Model.
	 */
	@Override
	public void clear() {
		owner.remove(association, null);
	}

	/**
	 * Artisan Collections are designed for iterator access. Hence this method is equivalent to
	 * getting the iterator and iterating till the index
	 *
	 * @param index the index
	 * @return the object
	 */
	@Override
	public PtcimObject get(int index) {
		if (index >= size()) {
			throw new IndexOutOfBoundsException();
		}
		Iterator<PtcimObject> it = iterator();
		if (index == 0) {
			if (it.hasNext()) {
				return it.next();
			}
			else {
				return null;
			}
		}
		int itCount = 0;
		while (it.hasNext() && itCount++ < index) {
			it.next();
		}
		return it.next();
	}
	
	public String getAssociation() {
		return association;
	}

	public PtcimObject getCOMObject() {
		return comObject;
	}

	public PtcimObject getOwner() {
		return owner;
	}
	
	/**
     * {@inheritDoc}
     *
     * <p>This implementation first gets an iterator (with
     * {@code iterator()}).  Then, it iterates over the list until the
     * specified element is found or the end of the list is reached.
     *
     * @throws ClassCastException   {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
	@Override
	public int indexOf(Object o) {
		Iterator<PtcimObject> e = iterator();
		if (o==null) {
		    return -1;
		} 
		else {
			int index = 0;
		    while (e.hasNext()) {
				if (o.equals(e.next()))
				    return index;
				index++;
			}
		}
		return -1;
	}

	public boolean isFiltered() {
		return false;
	}

	@Override
	public Iterator<PtcimObject> iterator() {
		Iterator<PtcimObject> iterator = new PtcimIterator(comObject);
		return iterator;
	}
	
	@Override
	public PtcimObject remove(int index) {
		PtcimObject obj = get(index);
		remove(obj);
		return obj;
	}

	@Override
	public boolean remove(Object o) {
		owner.remove(association, o);
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
		boolean modified = false;
		Iterator<?> e = c.iterator();
		while (e.hasNext()) {
			modified |= remove(e.next());
		}
		return modified;
	}

	@Override
	public int size() {
		Object resCount;
		resCount = owner.itemCount(association);
		// itemCount return long. Danger for integer overflow here.
		return Integer.parseInt(resCount.toString());
	}

	@Override
	public AbstractOperation getAbstractOperation(String name) {
		if ("select".equals(name)) {
			return new PtcimCollectionSelectOperation();
		}
		else if( "selectOne".equals(name)) {
			return new PtcimCollectionSelectOneOperation();
		}
		else
			return null;
	}

	public void disconnect() {
		// TODO Auto-generated method stub
		
	}
}
