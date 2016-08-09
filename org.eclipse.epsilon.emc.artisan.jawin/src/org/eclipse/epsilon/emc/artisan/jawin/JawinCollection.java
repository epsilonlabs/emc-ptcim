package org.eclipse.epsilon.emc.artisan.jawin;

import java.util.AbstractCollection;
import java.util.Collections;
import java.util.Iterator;

import org.eclipse.epsilon.emc.COM.COMObject;
import org.eclipse.epsilon.emc.COM.EpsilonCOMException;

public class JawinCollection<E extends COMObject> extends AbstractCollection<E> {
	
	private final E source;
	private final E owner;
	private final String association;
	
	public JawinCollection(E source, E owner, String association) {
		assert source instanceof JawinObject;
		assert source instanceof JawinObject;
		this.source = source;
		this.owner = owner;
		this.association = association;
	}

	@Override
	public Iterator<E> iterator() {
		Iterator<E> iterator = new JawinIterator<E>((JawinObject) source);
		return iterator;
	}

	@Override
	public int size() {
		COMObject resCount;
		try {
			resCount = owner.invoke("ItemCount", association, Collections.emptyList());
		} catch (EpsilonCOMException e) {
			throw new IllegalStateException(e);
		}
		return (Integer) ((JawinPrimitive) resCount).getPrimitive();
	}
}
