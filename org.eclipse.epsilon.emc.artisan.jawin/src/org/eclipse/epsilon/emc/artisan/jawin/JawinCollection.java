package org.eclipse.epsilon.emc.artisan.jawin;

import java.util.AbstractCollection;
import java.util.Iterator;

import org.eclipse.epsilon.emc.COM.COMObject;

public class JawinCollection<E extends COMObject> extends AbstractCollection<E> {
	
	private final E source;
	
	public JawinCollection(E source) {
		assert source instanceof JawinObject;
		this.source = source;
	}

	@Override
	public Iterator<E> iterator() {
		return (Iterator<E>) new JawinIterator((JawinObject) source);
	}

	@Override
	public int size() {
		// objItem.ItemCount("Operation")
		//source.invoke("ItemCount", "Operation", new Object[]{});
		throw new UnsupportedOperationException("Artisan Collections don't support size querying");
	}
}
