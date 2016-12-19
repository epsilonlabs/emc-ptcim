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
package org.eclipse.epsilon.emc.ptcim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.eclipse.epsilon.common.module.ModuleElement;
import org.eclipse.epsilon.emc.ptcim.jawin.PtcimCollection;
import org.eclipse.epsilon.emc.ptcim.jawin.PtcimObject;
import org.eclipse.epsilon.emc.ptcim.jawin.PtcimPropertyManager;
import org.eclipse.epsilon.eol.exceptions.EolIllegalPropertyAssignmentException;
import org.eclipse.epsilon.eol.exceptions.EolInternalException;
import org.eclipse.epsilon.eol.exceptions.EolReadOnlyPropertyException;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.introspection.IPropertyGetter;
import org.eclipse.epsilon.eol.execute.introspection.IPropertySetter;

/**
 * The Class JawinCachedPropertyXetter.
 */
public class PtcimCachedPropertyXetter extends PtcimPropertyManager implements IPropertyGetter, IPropertySetter {

	// The xetter needs to know if the valueCache should be used.
	public PtcimCachedPropertyXetter() {
		super();
	}

	private static final Object ASSOCIATION_ROLE = "Association";
	
	public enum PtcPropertyEnum {
		IS_PUBLIC, IS_READ_ONLY, IS_MULTIPLE, IS_ASSOCIATION
	}

	private ModuleElement ast;
	private IEolContext context;
	private PtcimObject object;

	private String lastSetProperty; // Assumes invoke(object) always comes after setProperty

	@Override
	public ModuleElement getAst() {
		return ast;
	}

	@Override
	public IEolContext getContext() {
		return context;
	}

	public PtcimPropertyManager getInstance() {
		return null;
	}

	@Override
	public Object getObject() {
		return object;
	}

	@Override
	public String getProperty() {
		return null;
	}

	public PtcProperty getPtcProperty(PtcimObject object, String property) {
		List<Object> args = new ArrayList<Object>();
		args.add("All Property Descriptors");
		String descriptors = null;
		PtcProperty prop = null;
		try {
			descriptors = (String) object.getAttribute("Property", args);
		} catch (EolInternalException e) {
			// TODO We probably need better understanding of errors
			return null;
		}
		List<String> list = Arrays.asList(descriptors.split("\\n"));
		for (String d : list) {
			String[] info = d.split(",");
			String name = unQuote(info[0]);
			if (nameMatches(name, property)) {
				String role = unQuote(info[1]);
				boolean isAssociation = false;
				if (role.equals(ASSOCIATION_ROLE)) {
					isAssociation = true;
				}
				boolean isPublic = true;
				String access = unQuote(info[2]);
				if (access.length() > 2) {		// private access is 3 letters: xxP
					isPublic = false;
				}
				boolean readOnly = false;
				if (access.contains("O")) {		// RO or ROP
					readOnly = true;
				}
				boolean isMultiple = false;
				String multy = unQuote(info[3]);
				if (multy.contains("+")) {
					isMultiple = true;
				}
				prop = new PtcProperty(name, isPublic, readOnly, isMultiple, isAssociation);
				break;
			}
		}
 		return prop;
 	}
	
	private boolean nameMatches(String name, String property) {
	 	String noBlanks = name.replaceAll("\\s","");
	 	return noBlanks.compareToIgnoreCase(property) == 0;
	}
		 
 	private String unQuote(String name) {
 		return name.replaceAll("^\"|\"$", "");
 	}

	@Override
	public boolean hasProperty(Object object, String property) {
		assert object.equals(this.object);
		property = normalise(property);
		if (getPtcProperty((PtcimObject) object, property) != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void invoke(final Object value) throws EolRuntimeException {
		
		new Thread(new Runnable() {
		    public void run() {
				try {
					PtcProperty props = getPtcProperty((PtcimObject) object, lastSetProperty);
					if (props.isReadOnly()) {
						throw new EolReadOnlyPropertyException();
					}
					List<Object> args = new ArrayList<Object>();
					args.add(lastSetProperty);
					// Caution: the ReturnType (for type Operation) is both an association and an attribute.
					// So, when we store it, we store the first type once and then the second ovewrites (race condition).
					// To solve it here we additionally check that if it IS_ASSOCIATION, the value is a collection.
					if (props.isAssociation() && (value instanceof Collection)) {
						try {
							args.add(lastSetProperty);
							((PtcimObject) object).invoke("Remove", args);
							for (Object aValue : (Collection<Object>) value) {
								// TODO Change that to an if statement and throw the correct
								// exception
								assert aValue instanceof PtcimObject;
								args.clear();
								args.add(lastSetProperty);
								args.add(aValue);
								object.invoke("Add", args);
							}
							if (props != null) {
								args.clear();
								args.add(lastSetProperty);
								PtcimObject allItems = (PtcimObject) object.invoke("Items", args);
								PtcimCollection allItemsJawin = new PtcimCollection(allItems, object, lastSetProperty);
							}
						} catch (EolInternalException e) {
							System.err.println("Error for " + lastSetProperty + " for value " + value);
							e.printStackTrace();
							throw new EolIllegalPropertyAssignmentException(getProperty(), getAst());
						}
						// End of FIX ME
					} else {
						args.add(0);
						args.add(value);
						try {
							System.err.println(Thread.currentThread().getName());
							((PtcimObject) object).invoke("PropertySet", args);
						} catch (EolInternalException e) {
							System.err.println(Thread.currentThread().getName());
							throw new EolIllegalPropertyAssignmentException(getProperty(), getAst());
						}
					}
				} catch (EolRuntimeException e) {
					e.printStackTrace();
				}
		    }
		}).start();
	}

	@Override
	public Object invoke(Object object, String property) throws EolRuntimeException {
		Object o = null;
		property = normalise(property);
		if (o == null) {
			//assert elementPropertiesNamesCache.containsKey(property); // knowsProperty always invoked first, which populates the cache
			try {
				o = queryPtcPropertyValue(property);
			} catch (EolInternalException e) {
				throw new EolRuntimeException(e.getMessage());
			}
		}
		return o;
	}

	private Object queryPtcPropertyValue(String property) throws EolRuntimeException, EolInternalException {
		Object o;
		property = normalise(property);
		PtcProperty ptcprop = getPtcProperty(object, property);
		if (ptcprop.isAssociation()) {
			List<Object> args = new ArrayList<Object>();
			args.add(property);
			if (ptcprop.isMultiple()) {
				PtcimCollection elements;
				try {
					Object res = object.invoke("Items", args);
					assert res instanceof PtcimObject;
					elements = new PtcimCollection((PtcimObject) res, object, property);
				} catch (EolInternalException e) {
					throw new EolRuntimeException(e.getMessage());
				}
				o = elements;
			} else {
				try {
					o = object.invoke("Item", args);
					if (o instanceof PtcimObject) {
						String strId = (String) ((PtcimObject) o).getAttribute("Property", "Id");
						((PtcimObject) o).setId(strId);
					}
				} catch (EolInternalException e) {
					throw new EolRuntimeException(e.getMessage());
				}
			}
		} else {
			o = object.getAttribute("Property", property);
		}
		return o;
	}

	@Override
	public void setAst(ModuleElement ast) {
		this.ast = ast;
	}

	@Override
	public void setContext(IEolContext context) {
		this.context = context;
	}

	@Override
	public void setObject(Object object) {
		assert object instanceof PtcimObject;
		this.object = (PtcimObject) object;
	}

	@Override
	public void setProperty(String property) {
		getPtcProperty((PtcimObject) getObject(), property);
		property = normalise(property);
		lastSetProperty = property;
	}

	public boolean knowsProperty(Object object, String property) {
		String normalisedProperty = normalise(property);
		if (getPtcProperty((PtcimObject) object, normalisedProperty) != null) {
			return true;
		} else {
			return false;
		}
	}
	
	// Normalisation: we assume that for example "Child Object" is treated the same as "child object", 
	// "childObject" or "childobject" so we remove the spaces and transform the input to lowercase. 
	public static String normalise(String theString) {
		return theString.replaceAll("\\s", "").toLowerCase();
	}
}
