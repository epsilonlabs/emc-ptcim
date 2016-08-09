package org.eclipse.epsilon.emc.artisan.jawin;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.eclipse.epsilon.emc.COM.COMObject;
import org.eclipse.epsilon.emc.COM.EpsilonCOMException;


public class JawinIterator<E extends COMObject> implements Iterator<E> {
	
	private final JawinObject source;
	
	public JawinIterator(JawinObject source) {
		super();
		this.source = source;
		try {
			// Make sure the iterator
			source.invoke("ResetQueryItems");
		} catch (EpsilonCOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean hasNext() {
		
		JawinPrimitive next;
		try {
			next = (JawinPrimitive) source.invoke("MoreItems");
		} catch (EpsilonCOMException e) {
			// FIXME this should be logged
			return false;
		}
		int val = ((Integer)next.getPrimitive());
		return val != 0;
	}

	@Override
	public E next() {
		JawinObject res = new JawinObject();
		try {
			res = source.invoke("NextItem");
		} catch (EpsilonCOMException e) {
			throw new NoSuchElementException(e.getMessage());
		}
		return (E) res;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not implemented yet");
	}

}
