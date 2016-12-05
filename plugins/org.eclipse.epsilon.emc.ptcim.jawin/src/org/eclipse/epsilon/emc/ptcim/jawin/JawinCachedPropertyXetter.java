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


import org.eclipse.epsilon.common.module.ModuleElement;
import org.eclipse.epsilon.emc.ptcim.ole.impl.EpsilonCOMException;
import org.eclipse.epsilon.eol.exceptions.EolIllegalPropertyAssignmentException;
import org.eclipse.epsilon.eol.exceptions.EolReadOnlyPropertyException;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.introspection.IPropertyGetter;
import org.eclipse.epsilon.eol.execute.introspection.IPropertySetter;

/**
 * The Class JawinCachedPropertyXetter.
 */
public class JawinCachedPropertyXetter extends JawinPropertyManager implements IPropertyGetter, IPropertySetter {

	private boolean propertiesValuesCacheEnabled;

	// The xetter needs to know if the valueCache should be used.
	public JawinCachedPropertyXetter(boolean propertiesValuesCacheEnabled) {
		super();
		this.propertiesValuesCacheEnabled = propertiesValuesCacheEnabled;
	}

	private static final Object ASSOCIATION_ROLE = "Association";
	
	private static final boolean USE_CACHE = false;

	public enum PtcPropertyEnum {
		IS_PUBLIC, IS_READ_ONLY, IS_MULTIPLE, IS_ASSOCIATION
	}

	private ModuleElement ast;
	private IEolContext context;
	private JawinObject object;

	// This is the cache the stores the names of the properties that each element has. For example the class element has a property called name, child objects, etc.
	// This way we don't need to query through the COM if an element has the requested property. This is actually the cache the stores the "metamodel" of PTC IM models.
	private Map<String, EnumSet<PtcPropertyEnum>> elementPropertiesNamesCache = new HashMap<String, EnumSet<PtcPropertyEnum>>();

	// In this cache we store the values of the properties that we have visited before. It is optional (is enabled/disabled through a checkbox in the 
	// run config. for each model) as in script where writing is performed, it will lead to inconsistencies when accessing the values using opposite relationships.
	private Map<String, Object> propertiesValuesCache = new HashMap<String, Object>();

	private String lastSetProperty; // Assumes invoke(object) always comes after setProperty

	@Override
	public ModuleElement getAst() {
		return ast;
	}

	@Override
	public IEolContext getContext() {
		return context;
	}

	public JawinPropertyManager getInstance() {
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

	public void getPtcProperty(String property) {
		EnumSet<PtcPropertyEnum> cachedProp = elementPropertiesNamesCache.get(property);
		if (cachedProp == null) {
			// long substart = System.nanoTime();
			List<Object> args = new ArrayList<Object>();
			args.add("All Property Descriptors");
			String descriptors = null;
			try {
				descriptors = (String) ((JawinObject) object).getAttribute("Property", args);
			} catch (EpsilonCOMException e) {
				// TODO We probably need better understanding of errors
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
				elementPropertiesNamesCache.put(name, ptcProp);
			}
		}
	}

	@Override
	public boolean hasProperty(Object object, String property) {
		assert object.equals(this.object);
		property = normalise(property);
		if (elementPropertiesNamesCache.containsKey(property)) {
			return true;
		}
		getPtcProperty(property);
		return elementPropertiesNamesCache.containsKey(property);
	}

	@Override
	public void invoke(final Object value) throws EolRuntimeException {
		new Thread(new Runnable() {
		    public void run() {
				try {
					EnumSet<PtcPropertyEnum> props = elementPropertiesNamesCache.get(lastSetProperty);
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
							((JawinObject) object).invoke("Remove", args);
							for (Object aValue : (Collection<Object>) value) {
								// TODO Change that to an if statement and throw the correct
								// exception
								assert aValue instanceof JawinObject;
								args.clear();
								args.add(lastSetProperty);
								args.add(aValue);
								object.invoke("Add", args);
							}
							if (!elementPropertiesNamesCache.containsKey(lastSetProperty)) {
								args.clear();
								args.add(lastSetProperty);
								JawinObject allItems = (JawinObject) object.invoke("Items", args);
								JawinCollection allItemsJawin = new JawinCollection(allItems, object, lastSetProperty);
								// The values cache is updated either when the getter is called or when the setter is called (this case). We don't  want to consume
								// unnecessary memory in storing the cached values that we don't want to retrieve so we only save if we the user requested caching.
								if (propertiesValuesCacheEnabled) {
									propertiesValuesCache.put(lastSetProperty, allItemsJawin);
								}
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
							((JawinObject) object).invoke("PropertySet", args);
						} catch (EpsilonCOMException e) {
							System.err.println(Thread.currentThread().getName());
							throw new EolIllegalPropertyAssignmentException(getProperty(), getAst());
						}
						// The values cache is updated either when the getter is called or when the setter is called (this case). We don't  want to consume
						// unnecessary memory in storing the cached values that we don't want to retrieve so we only save if we the user requested caching.
						if (propertiesValuesCacheEnabled) {
							propertiesValuesCache.put(lastSetProperty, value);
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
		o = propertiesValuesCache.get(property);
		// If the value is not in the cache OR we don't want to get it from the cache (because user asked not to use the cache) then retrieve it using COM.
		// The first condition (o == null) is enough because we don't update the propertiesValuesCache map if caching is disabled but I include
		// this extra condition for completeness and better understanding of how the code works.
		if (o == null || !propertiesValuesCacheEnabled) {
			assert elementPropertiesNamesCache.containsKey(property); // knowsProperty always invoked first, which populates the cache
			try {
				o = queryPtcPropertyValue(property);
			} catch (EpsilonCOMException e) {
				throw new EolRuntimeException(e.getMessage());
			}
			// The values cache is updated either when the getter is called (this case) or when the setter is called. We don't  want to consume
			// unnecessary memory in storing the cached values that we don't want to retrieve so we only save if we the user requested caching.
			if (propertiesValuesCacheEnabled) {
				propertiesValuesCache.put(property, o);
			}
		}
		return o;
	}

	private Object queryPtcPropertyValue(String property) throws EolRuntimeException, EpsilonCOMException {
		Object o;
		property = normalise(property);
		EnumSet<PtcPropertyEnum> ptcprop = elementPropertiesNamesCache.get(property);
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
		assert object instanceof JawinObject;
		this.object = (JawinObject) object;
	}

	@Override
	public void setProperty(String property) {
		getPtcProperty(property);
		property = normalise(property);
		lastSetProperty = property;
	}

	public boolean knowsProperty(String property) {
		String normalisedProperty = normalise(property);
		getPtcProperty(normalisedProperty);
		return elementPropertiesNamesCache.containsKey(normalisedProperty);
	}
	
	// Normalisation: we assume that for example "Child Object" is treated the same as "child object", 
	// "childObject" or "childobject" so we remove the spaces and transform the input to lowercase. 
	public static String normalise(String theString) {
		return theString.replaceAll("\\s", "").toLowerCase();
	}
}
