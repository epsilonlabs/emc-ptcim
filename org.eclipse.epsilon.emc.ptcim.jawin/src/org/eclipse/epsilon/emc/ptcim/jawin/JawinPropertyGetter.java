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

import org.eclipse.epsilon.emc.ptcim.ole.COMCollection;
import org.eclipse.epsilon.emc.ptcim.ole.COMProperty;
import org.eclipse.epsilon.emc.ptcim.ole.COMPropertyManager;
import org.eclipse.epsilon.emc.ptcim.ole.EpsilonCOMException;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.introspection.AbstractPropertyGetter;

/**
 * The Class JawinPropertyGetter.
 */
public class JawinPropertyGetter extends AbstractPropertyGetter {

	/** The manager. */
	private final COMPropertyManager manager = JawinPropertyManager.INSTANCE;
	
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.execute.introspection.IPropertyGetter#invoke(java.lang.Object, java.lang.String)
	 */
	@Override
	public Object invoke(Object object, String property) throws EolRuntimeException {
		assert object instanceof JawinObject;
		JawinObject jObject = (JawinObject) object;
		Object o = null;
		try {
			COMProperty p = manager.getProperty(jObject, property);
			if (p == null) {
				throw new EolRuntimeException("No such property");
			}
			if (p.isAssociation()) {
				List<Object> args = new ArrayList<Object>();
				args.add(property);
				if (p.isMultiple()) {
					COMCollection elements;
					List<Object> byRefArgs = new ArrayList<Object>();
					byRefArgs.add("*");
					try {
						//Object res = jObject.invoke("Items", property, args, 2);
						Object res = jObject.invoke("Items", args);		//, byRefArgs);
						assert res instanceof JawinObject;
						elements = new JawinCollection((JawinObject) res, jObject, property);
					} catch (EpsilonCOMException e) {
						throw new EolRuntimeException(e.getMessage());
					}
					o = elements;
				}
				else {
					try {
						o = jObject.invoke("Item", args);		//, byRefArgs);
						if ( o instanceof JawinObject) {
							String strId = (String) ((JawinObject) o).get("Property", "Id");
							((JawinObject) o).setId(strId);
						}
					} catch (EpsilonCOMException e) {
						throw new EolRuntimeException(e.getMessage());
					}
				}
			}
			else {
				o = jObject.get("Property", property);
			}
		} catch (EpsilonCOMException e) {
			throw new EolRuntimeException(e.getMessage());
		}
		return o;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.execute.introspection.AbstractPropertyGetter#hasProperty(java.lang.Object, java.lang.String)
	 */
	@Override
	public boolean hasProperty(Object object, String property) {
		assert object instanceof JawinObject;
		JawinObject jObject = (JawinObject) object;
		COMProperty p = manager.getProperty(jObject, property);
		return p != null;
	}

}