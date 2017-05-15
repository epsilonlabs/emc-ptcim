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
public class Com4jPtcimCollection extends AbstractList<Com4jPtcimObject> implements IAbstractOperationContributor {
	
	/** The object that points to the collection. */
	private final Com4jPtcimObject comObject;
	
	/** The owner. */
	private final Com4jPtcimObject owner;
	
	/** The association. */
	private final String association;
	
	/**
	 * Instantiates a new collection.
	 *
	 * @param comCollection the source
	 * @param owner the owner
	 * @param association the association
	 */
	public Com4jPtcimCollection(Com4jPtcimObject comCollection, Com4jPtcimObject owner, String association) {
		assert comCollection instanceof Com4jPtcimObject;
		assert owner instanceof Com4jPtcimObject;
		this.comObject = (Com4jPtcimObject) comCollection;
		this.owner = (Com4jPtcimObject) owner;
		this.association = association;
	}

	@Override
	public boolean add(Com4jPtcimObject e) {
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
	public Com4jPtcimObject get(int index) {
		if (index >= size()) {
			throw new IndexOutOfBoundsException();
		}
		Iterator<Com4jPtcimObject> it = iterator();
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

	public Com4jPtcimObject getCOMObject() {
		return comObject;
	}

	public Com4jPtcimObject getOwner() {
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
		Iterator<Com4jPtcimObject> e = iterator();
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
	public Iterator<Com4jPtcimObject> iterator() {
		Iterator<Com4jPtcimObject> iterator = new Com4jPtcimIterator(comObject);
		return iterator;
	}
	
	@Override
	public Com4jPtcimObject remove(int index) {
		Com4jPtcimObject obj = get(index);
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
			return new Com4jPtcimCollectionSelectOperation();
		}
		else if( "selectOne".equals(name)) {
			return new Com4jPtcimCollectionSelectOneOperation();
		}
		else
			return null;
	}

	public void disconnect() {
		// TODO Auto-generated method stub
		
	}
}
