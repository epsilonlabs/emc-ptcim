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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.emc.ptcim.ole.impl.EpsilonCOMException;
import org.eclipse.epsilon.emc.ptcim.ole.impl.PtcProperty;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.introspection.AbstractPropertyGetter;

/**
 * The Class JawinPropertyGetter.
 */
public class JawinPropertyGetter extends AbstractPropertyGetter {

	/** The manager. */
	private final JawinPropertyManager manager = JawinPropertyManager.INSTANCE;
	
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.execute.introspection.IPropertyGetter#invoke(java.lang.Object, java.lang.String)
	 */
	@Override
	public Object invoke(Object object, String property) throws EolRuntimeException {
		long start = System.nanoTime();
		assert object instanceof JawinObject;
		JawinObject jObject = (JawinObject) object;
		Object o = null;
		try {
			PtcProperty p = manager.getPtcProperty(jObject, property);
			if (p == null) {
				throw new EolRuntimeException("No such property");
			}
			if (p.isAssociation()) {
				List<Object> args = new ArrayList<Object>();
				args.add(property);
				if (p.isMultiple()) {
					JawinCollection elements;
					List<Object> byRefArgs = new ArrayList<Object>();
					byRefArgs.add("*");
					try {
						Object res = jObject.invoke("Items", args);
						assert res instanceof JawinObject;
						elements = new JawinCollection((JawinObject) res, jObject, property);
					} catch (EpsilonCOMException e) {
						throw new EolRuntimeException(e.getMessage());
					}
					o = elements;
				}
				else {
					try {
						o = jObject.invoke("Item", args);
						if ( o instanceof JawinObject) {
							String strId = (String) ((JawinObject) o).getAttribute("Property", "Id");
							((JawinObject) o).setId(strId);
						}
					} catch (EpsilonCOMException e) {
						throw new EolRuntimeException(e.getMessage());
					}
				}
			}
			else {
				o = jObject.getAttribute("Property", property);
			}
		} catch (EpsilonCOMException e) {
			throw new EolRuntimeException(e.getMessage());
		}
		long total = System.nanoTime() - start;
		StringBuilder sb = new StringBuilder("JawinPropertyGetter,");
		sb.append(total);
		System.out.println(sb);
		return o;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.execute.introspection.AbstractPropertyGetter#hasProperty(java.lang.Object, java.lang.String)
	 */
	@Override
	public boolean hasProperty(Object object, String property) {
		assert object instanceof JawinObject;
		JawinObject jObject = (JawinObject) object;
		PtcProperty p = manager.getPtcProperty(jObject, property);
		return p != null;
	}
}
