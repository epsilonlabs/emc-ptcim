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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.emc.COM.COMObject;
import org.eclipse.epsilon.emc.COM.COMProperty;
import org.eclipse.epsilon.emc.COM.EpsilonCOMException;
import org.eclipse.epsilon.eol.exceptions.EolIllegalPropertyAssignmentException;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.introspection.AbstractPropertySetter;

/**
 * The Class JawinPropertySetter.
 */
public class JawinPropertySetter extends AbstractPropertySetter {
	
	/** The property  manager (cache). */
	private final JawinPropertyManager manager = JawinPropertyManager.INSTANCE;
	
	/** The COM property. */
	private COMProperty comProperty;
	

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.execute.introspection.AbstractPropertySetter#setProperty(java.lang.String)
	 */
	@Override
	public void setProperty(String property) {
		comProperty = manager.getProperty((COMObject) object, property);
		if (comProperty != null) {
			super.setProperty(property);
		}
		else {
			// FIXME It can be other reason, double check 
			throw new IllegalArgumentException("The propery can't be found in the object");
		}
	}


	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.execute.introspection.IPropertySetter#invoke(java.lang.Object)
	 */
	@Override
	public void invoke(Object value) throws EolRuntimeException {
		// TODO Check if value matches property? See EMF Setter
		List<Object> args = new ArrayList<Object>();
		args.add(0);
		args.add(value);
		try {
			((COMObject) object).invoke("PropertySet", comProperty.getName(), args);
		} catch (EpsilonCOMException e) {
			// TODO Can we check if message has 'Failed to add item' and do a
			// objItem.Property("ExtendedErrorInfo") to get more info?
			throw new EolIllegalPropertyAssignmentException(getProperty(), getAst());
		}
	}

}
