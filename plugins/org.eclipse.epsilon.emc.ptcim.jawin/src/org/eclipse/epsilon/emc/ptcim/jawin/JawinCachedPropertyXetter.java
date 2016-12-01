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
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import org.eclipse.epsilon.common.module.ModuleElement;
import org.eclipse.epsilon.emc.ptcim.ole.IPtcObject;
import org.eclipse.epsilon.emc.ptcim.ole.IPtcPropertyManager;
import org.eclipse.epsilon.emc.ptcim.ole.impl.EpsilonCOMException;
import org.eclipse.epsilon.emc.ptcim.ole.impl.PtcProperty;
import org.eclipse.epsilon.eol.exceptions.EolIllegalPropertyAssignmentException;
import org.eclipse.epsilon.eol.exceptions.EolReadOnlyPropertyException;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.introspection.IPropertyGetter;
import org.eclipse.epsilon.eol.execute.introspection.IPropertySetter;

/**
 * The Class JawinCachedPropertyXetter.
 */
public class JawinCachedPropertyXetter implements IPtcPropertyManager, IPropertyGetter, IPropertySetter {

	private static final Object ASSOCIATION_ROLE = "Association";

	public enum PtcPropertyEnum {
		IS_PUBLIC, IS_READ_ONLY, IS_MULTIPLE, IS_ASSOCIATION
	}

	private ModuleElement ast;
	private IEolContext context;
	private IPtcObject object;

	private Map<String, PtcProperty> ptcCache = new HashMap<String, PtcProperty>();

	private Map<String, EnumSet<PtcPropertyEnum>> ptcCache2 = new HashMap<String, EnumSet<PtcPropertyEnum>>();

	private Map<String, Object> valueCache = new HashMap<String, Object>();

	private String lastSetProperty; // Assumes invoke(object) always comes after setProperty
	
	@Override
	public void dispose() {
		ptcCache.clear();
	}

	@Override
	public ModuleElement getAst() {
		return ast;
	}

	@Override
	public IEolContext getContext() {
		return context;
	}

	@Override
	public IPtcPropertyManager getInstance() {
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

	@Override
	public PtcProperty getPtcProperty(IPtcObject object, String property) {
		return null;
	}

	@Override
	public PtcProperty getPtcProperty(String property) {
		long start = System.nanoTime();
		EnumSet<PtcPropertyEnum> cachedProp = ptcCache2.get(property);
		if (cachedProp == null) {
			// long substart = System.nanoTime();
			List<Object> args = new ArrayList<Object>();
			args.add("All Property Descriptors");
			String descriptors = null;
			try {
				descriptors = (String) ((IPtcObject) object).getAttribute("Property", args);
			} catch (EpsilonCOMException e) {
				// TODO We probably need better understanding of errors
				return null;
			}
			List<String> list = Arrays.asList(descriptors.split("\\n"));
			for (String d : list) {
				String[] info = d.split(",");
				String name = info[0].substring(1, info[0].length() - 1);
				name = normalise(name);
				// if (nameMatches(name, property)) {
				final EnumSet<PtcPropertyEnum> ptcProp = EnumSet.noneOf(PtcPropertyEnum.class);
				String role = info[1].substring(1, info[1].length() - 1);
				if (role.equals(ASSOCIATION_ROLE)) {
					ptcProp.add(PtcPropertyEnum.IS_ASSOCIATION);
				}
				String access = info[2];
				if (access.length() == 4) { // private access is 5 letters:
											// "xxP"
					ptcProp.add(PtcPropertyEnum.IS_PUBLIC);
				}
				if (access.contains("O")) { // RO or ROP
					ptcProp.add(PtcPropertyEnum.IS_READ_ONLY);
				}
				String multy = info[3];
				if (multy.contains("+")) {
					ptcProp.add(PtcPropertyEnum.IS_MULTIPLE);
				}
				ptcCache2.put(name, ptcProp);
			}
		}
		long total = System.nanoTime() - start;
		StringBuilder sb = new StringBuilder("getPtcProperty,");
		sb.append(total);
		System.out.println(sb);
		return null;
	}

	@Override
	public boolean hasProperty(Object object, String property) {
		assert object.equals(this.object);
		property = normalise(property);
		if (ptcCache2.containsKey(property)) {
			return true;
		}
		getPtcProperty(property);
		return ptcCache2.containsKey(property);
	}

	@Override
	public void invoke(final Object value) throws EolRuntimeException {
		new Thread(new Runnable() {
		    public void run() {
				try {
					EnumSet<PtcPropertyEnum> props = ptcCache2.get(lastSetProperty);
					if (props.contains(PtcPropertyEnum.IS_READ_ONLY)) {
						throw new EolReadOnlyPropertyException();
					}
					
					List<Object> args = new ArrayList<Object>();
					args.add(lastSetProperty);
			
					// Caution: the ReturnType (for type Operation) is both an association and an attribute.
					// So, when we store it, we store the first type once and then the second ovewrites (race condition).
					// To solve it here we additionally check that if it IS_ASSOCIATION, the value is a collection.
					if (props.contains(PtcPropertyEnum.IS_ASSOCIATION) && (value instanceof Collection)) {
						try {
							args.add(lastSetProperty);
							((IPtcObject) object).invoke("Remove", args);
							for (Object aValue : (Collection<Object>) value) {
								// TODO Change that to an if statement and throw the correct
								// exception
								assert aValue instanceof IPtcObject;
								args.clear();
								args.add(lastSetProperty);
								args.add(aValue);
								object.invoke("Add", args);
							}
							if (!ptcCache2.containsKey(lastSetProperty)) {
								args.clear();
								args.add(lastSetProperty);
								IPtcObject allItems = (IPtcObject) object.invoke("Items", args);
								JawinCollection allItemsJawin = new JawinCollection(allItems, object, lastSetProperty);
								valueCache.put(lastSetProperty, allItemsJawin);
							}
			
						} catch (EpsilonCOMException e) {
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
							((IPtcObject) object).invoke("PropertySet", args);
						} catch (EpsilonCOMException e) {
							System.err.println(Thread.currentThread().getName());
							throw new EolIllegalPropertyAssignmentException(getProperty(), getAst());
						}
						valueCache.put(lastSetProperty, value);
					}
				} catch (EolRuntimeException e) {
					e.printStackTrace();
				}
		    }
		}).start();
	}

	@Override
	public Object invoke(Object object, String property) throws EolRuntimeException {
		long start = System.nanoTime();
		Object o = null;
		property = normalise(property);
		o = valueCache.get(property);
		if (o == null) {
			assert ptcCache2.containsKey(property); // knowsProperty always invoked first, which populates the cache
			try {
				o = queryPtcPropertyValue(property);
			} catch (EpsilonCOMException e) {
				throw new EolRuntimeException(e.getMessage());
			}
			valueCache.put(property, o);
		}
		long total = System.nanoTime() - start;
		System.out.println("JawinPropertyGetter," + total);
		return o;
	}

	private Object queryPtcPropertyValue(String property) throws EolRuntimeException, EpsilonCOMException {
		Object o;
		property = normalise(property);
		EnumSet<PtcPropertyEnum> ptcprop = ptcCache2.get(property);
		if (ptcprop.contains(PtcPropertyEnum.IS_ASSOCIATION)) {
			List<Object> args = new ArrayList<Object>();
			args.add(property);
			if (ptcprop.contains(PtcPropertyEnum.IS_MULTIPLE)) {
				JawinCollection elements;
				try {
					Object res = object.invoke("Items", args);
					assert res instanceof JawinObject;
					elements = new JawinCollection((JawinObject) res, object, property);
				} catch (EpsilonCOMException e) {
					throw new EolRuntimeException(e.getMessage());
				}
				o = elements;
			} else {
				try {
					o = object.invoke("Item", args);
					if (o instanceof JawinObject) {
						String strId = (String) ((JawinObject) o).getAttribute("Property", "Id");
						((JawinObject) o).setId(strId);
					}
				} catch (EpsilonCOMException e) {
					throw new EolRuntimeException(e.getMessage());
				}
			}
		} else {
			o = object.getAttribute("Property", property);
		}
		return o;
	}

	/**
	 * Remove spaces from the Artisan name and compare case insensitive
	 * 
	 * @param name
	 * @param property
	 * @return
	 */
	private boolean nameMatches(String name, String property) {
		String noBlanks = normalise(name);
		return noBlanks.compareToIgnoreCase(property) == 0;
	}

	private Object queryPtcPropertyValue(String property, JawinObject jObject, PtcProperty p)
			throws EolRuntimeException, EpsilonCOMException {

		Object o;
		if (p.isAssociation()) {
			List<Object> args = new ArrayList<Object>();
			args.add(property);
			if (p.isMultiple()) {
				JawinCollection elements;
				try {
					Object res = jObject.invoke("Items", args);
					assert res instanceof JawinObject;
					elements = new JawinCollection((JawinObject) res, jObject, property);
				} catch (EpsilonCOMException e) {
					throw new EolRuntimeException(e.getMessage());
				}
				o = elements;
			} else {
				try {
					o = jObject.invoke("Item", args); // , byRefArgs);
					if (o instanceof JawinObject) {
						String strId = (String) ((JawinObject) o).getAttribute("Property", "Id");
						((JawinObject) o).setId(strId);
					}
				} catch (EpsilonCOMException e) {
					throw new EolRuntimeException(e.getMessage());
				}
			}
		} else {
			o = jObject.getAttribute("Property", property);
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
		assert object instanceof IPtcObject;
		this.object = (IPtcObject) object;
	}

	@Override
	public void setProperty(String property) {
		getPtcProperty(property);
		property = normalise(property);
		lastSetProperty = property;
	}

	@Override
	public boolean knowsProperty(String property) {
		String normalisedProperty = normalise(property);
		getPtcProperty(normalisedProperty);
		return ptcCache2.containsKey(normalisedProperty);
	}
	
	// Normalisation: we assume that for example "Child Object" is treated the same as "child object", 
	// "childObject" or "childobject" so we remove the spaces and transform the input to lowercase. 
	public static String normalise(String theString) {
		return theString.replaceAll("\\s", "").toLowerCase();
	}
}
