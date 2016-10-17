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
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		IS_PUBLIC,
		IS_READ_ONLY,
		IS_MULTIPLE,
		IS_ASSOCIATION
	}
	
	private ModuleElement ast;
	private IEolContext context;
	private IPtcObject object;
	
	private Map<String, PtcProperty> ptcCache
		=  new HashMap<String,PtcProperty>();
	
	private Map<String, EnumSet<PtcPropertyEnum>> ptcCache2
	=  new HashMap<String,EnumSet<PtcPropertyEnum>>();
	
	private Map<String, Object> valueCache = new HashMap<String, Object>();

	private String lastSetProperty;		// Assumes invoke(object) always comes after setProperty

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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PtcProperty getPtcProperty(String property) {
//		System.out.println("getPtcProperty");
		long start = System.nanoTime();
		//PtcProperty cachedProp = ptcCache.get(property);
		EnumSet<PtcPropertyEnum> cachedProp = ptcCache2.get(property);
		if (cachedProp == null) {
//			long substart = System.nanoTime();
			List<Object> args = new ArrayList<Object>();
			args.add("All Property Descriptors");
			String descriptors = null;
			try {
				descriptors = (String) ((IPtcObject) object).getAttribute("Property", args);
			} catch (EpsilonCOMException e) {
				// TODO We probably need better understanding of errors
				return null;
			}
//			long subtotal = System.nanoTime() - substart;
//			StringBuilder sb = new StringBuilder("COMCAll,");
//			sb.append(subtotal);
//			System.out.println(sb);
			// Count find property with split.
//			substart = System.nanoTime();
			List<String> list = Arrays.asList(descriptors.split("\\n"));
			//subtotal = System.nanoTime() - substart;
			//sb = new StringBuilder("AllSplit,");
			//sb.append(subtotal);
			//System.out.println(sb);
			//substart = System.nanoTime();
//			for (String d : list) {
//				String[] info = d.split(",");
//				String name = info[0].substring(1, info[0].length()-1);
//				if (nameMatches(name, property)) {
//					final Set<PtcPropertyEnum> ptcProp = EnumSet.noneOf(PtcPropertyEnum.class);
//					String role = info[1].substring(1, info[1].length()-1);
//					boolean isAssociation = false;
//					if (role.equals(ASSOCIATION_ROLE)) {
//						isAssociation = true;
//						ptcProp.add(PtcPropertyEnum.IS_ASSOCIATION);
//					}
//					boolean isPublic = true;
//					String access = info[2].substring(1, info[2].length()-1);
//					if (access.length() > 2) {		// private access is 3 letters: xxP
//						isPublic = false;
//					}
//					boolean readOnly = false;
//					if (access.contains("O")) {		// RO or ROP
//						readOnly = true;
//						ptcProp.add(PtcPropertyEnum.IS_READ_ONLY);
//					}
//					boolean isMultiple = false;
//					String multy = info[3].substring(1, info[3].length()-1);
//					if (multy.contains("+")) {
//						isMultiple = true;
//						ptcProp.add(PtcPropertyEnum.IS_MULTIPLE);
//					}
//					cachedProp = new PtcProperty(name, isPublic, readOnly, isMultiple, isAssociation);
//					break;
//				}
//			}
//			subtotal = System.nanoTime() - substart;
//			sb = new StringBuilder("FindProperty,");
//			sb.append(subtotal);
//			System.out.println(sb);
			
			// Compare Init all props
//			substart = System.nanoTime();
			for (String d : list) {
				String[] info = d.split(",");
				String name = info[0].substring(1, info[0].length()-1);
				String noBlanks = name.replaceAll("\\s","");
				//if (nameMatches(name, property)) {
					final EnumSet<PtcPropertyEnum> ptcProp = EnumSet.noneOf(PtcPropertyEnum.class);
					String role = info[1].substring(1, info[1].length()-1);
					if (role.equals(ASSOCIATION_ROLE)) {
						ptcProp.add(PtcPropertyEnum.IS_ASSOCIATION);
					}
					String access = info[2];
					if (access.length() == 4) {		// private access is 5 letters: "xxP"
						ptcProp.add(PtcPropertyEnum.IS_PUBLIC);
					}
					if (access.contains("O")) {		// RO or ROP
						ptcProp.add(PtcPropertyEnum.IS_READ_ONLY);
					}
					String multy = info[3];
					if (multy.contains("+")) {
						ptcProp.add(PtcPropertyEnum.IS_MULTIPLE);
					}
					ptcCache2.put(noBlanks, ptcProp);
					//cachedProp = new PtcProperty(name, isPublic, readOnly, isMultiple, isAssociation);
				//}
			}
//			subtotal = System.nanoTime() - substart;
//			sb = new StringBuilder("CacheAll,");
//			sb.append(subtotal);
//			System.out.println(sb);
			//ptcCache.put(property, cachedProp);
		}
		long total = System.nanoTime() - start;
		StringBuilder sb = new StringBuilder("getPtcProperty,");
		sb.append(total);
		System.out.println(sb);
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.execute.introspection.AbstractPropertyGetter#hasProperty(java.lang.Object, java.lang.String)
	 */
	@Override
	public boolean hasProperty(Object object, String property) {
		assert object.equals(this.object);
		if (ptcCache2.containsKey(property)) {
			return true;
		}
		getPtcProperty(property);
		return ptcCache2.containsKey(property);
	}

//	@Override
//	public void invoke(Object value) throws EolRuntimeException {
//		
//		PtcProperty comProperty = getPtcProperty(lastSetProperty);
//		// TODO Check if value matches property? See EMF Setter
//		if (comProperty .isReadOnly()) {
//			throw new EolReadOnlyPropertyException();
//		}
//		List<Object> args = new ArrayList<Object>();
//		args.add(comProperty.getName());
//		if (comProperty.isAssociation()) {
//			if (!(value instanceof JawinObject)) {
//				throw new EolRuntimeException("Association (0..1) properties' values must be COM objects.");
//			}
//			try {
//				args.add(((JawinObject) value));
//				((IPtcObject) object).invoke("Add", args);
//			} catch (EpsilonCOMException e) {
//				// TODO Auto-generated catch block
//				System.err.println("Error for " + comProperty.getName() + " for value " + value);
//				e.printStackTrace();
//				throw new EolIllegalPropertyAssignmentException(getProperty(), getAst());
//			}
//		}
//		else {
//			args.add(0);
//			args.add(value);			
//			try {
//				((IPtcObject) object).invoke("PropertySet", args);
//			} catch (EpsilonCOMException e) {
//				// Get additional information about the error
////						Object extendedErr = null;
////						try {
////							extendedErr = ((IPtcObject) object).get("Property", "ExtendedErrorInfo");
////						} catch (EpsilonCOMException e1) {
////							// TODO Auto-generated catch block
////							e1.printStackTrace();
////						}
////						// objItem.Property("ExtendedErrorInfo") to get more info?
////						System.err.println("Error for " + comProperty.getName() + " for value " + value + ". Err " + extendedErr );
////						e.printStackTrace();
//				throw new EolIllegalPropertyAssignmentException(getProperty(), getAst());
//			}
//		}
//		// If all good, update cache
//		valueCache.put(lastSetProperty, value);
//	}
	
	@Override
	public void invoke(Object value) throws EolRuntimeException {
		
		PtcProperty comProperty = getPtcProperty(lastSetProperty);
		// TODO Check if value matches property? See EMF Setter
		if (comProperty .isReadOnly()) {
			throw new EolReadOnlyPropertyException();
		}
		List<Object> args = new ArrayList<Object>();
		args.add(comProperty.getName());
		if (comProperty.isAssociation()) {
			if (!(value instanceof JawinObject)) {
				throw new EolRuntimeException("Association (0..1) properties' values must be COM objects.");
			}
			try {
				args.add(((JawinObject) value));
				((IPtcObject) object).invoke("Add", args);
			} catch (EpsilonCOMException e) {
				// TODO Auto-generated catch block
				System.err.println("Error for " + comProperty.getName() + " for value " + value);
				e.printStackTrace();
				throw new EolIllegalPropertyAssignmentException(getProperty(), getAst());
			}
		}
		else {
			args.add(0);
			args.add(value);			
			try {
				((IPtcObject) object).invoke("PropertySet", args);
			} catch (EpsilonCOMException e) {
				// Get additional information about the error
//						Object extendedErr = null;
//						try {
//							extendedErr = ((IPtcObject) object).get("Property", "ExtendedErrorInfo");
//						} catch (EpsilonCOMException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
//						// objItem.Property("ExtendedErrorInfo") to get more info?
//						System.err.println("Error for " + comProperty.getName() + " for value " + value + ". Err " + extendedErr );
//						e.printStackTrace();
				throw new EolIllegalPropertyAssignmentException(getProperty(), getAst());
			}
		}
		// If all good, update cache
		valueCache.put(lastSetProperty, value);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.eol.execute.introspection.IPropertyGetter#invoke(java.lang.Object, java.lang.String)
	 */
	@Override
	public Object invoke(Object object, String property) throws EolRuntimeException {
		long start = System.nanoTime();
		Object o = null;
		o = valueCache.get(property);
		if (o == null) {
//			assert object instanceof JawinObject;
//			JawinObject jObject = (JawinObject) object;
//			try {
//				PtcProperty p = getPtcProperty(property);
//				if (p == null) {
//					throw new EolRuntimeException("No such property");
//				}
//				o = queryPtcPropertyValue(property, jObject, p);
//			} catch (EpsilonCOMException e) {
//				throw new EolRuntimeException(e.getMessage());
//			}
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
		EnumSet<PtcPropertyEnum> ptcprop = ptcCache2.get(property);
		if (ptcprop.contains(PtcPropertyEnum.IS_ASSOCIATION)) {
			List<Object> args = new ArrayList<Object>();
			args.add(property);
			if (ptcprop.contains(PtcPropertyEnum.IS_MULTIPLE)) {
				JawinCollection elements;
				try {
					Object res = object.invoke("Items", args);
					assert res instanceof JawinObject;
					elements = new JawinCollection((JawinObject) res,  object, property);
				} catch (EpsilonCOMException e) {
					throw new EolRuntimeException(e.getMessage());
				}
				o = elements;
			}
			else {
				try {
					o = object.invoke("Item", args);
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
			o = object.getAttribute("Property", property);
		}
		return o;
	}

	/**
	 * Remove spaces from the Artisan name and compare case insensitive
	 * @param name
	 * @param property
	 * @return
	 */
	private boolean nameMatches(String name, String property) {
		String noBlanks = name.replaceAll("\\s","");
//		String p = property.toLowerCase()
//		
//		List<Integer> idx = new ArrayList&ltInteger>();
//		03
//		  int id = -1;
//		04
//		  int shift = pattern.length();
//		05
//		  int scnIdx = -shift;
//		06
//		  while (scnIdx != -1 || id == -1) {
//		07
//		   idx.add(scnIdx);
//		08
//		   id = scnIdx + shift;
//		09
//		   scnIdx = source.indexOf(pattern, id);
//		10
//		  }
//		11
//		  idx.remove(0);
//		12
//		 
//		13
//		  return idx;
//		long substart = System.nanoTime();
//		long subtotal = System.nanoTime() - substart;
//		StringBuilder sb = new StringBuilder("nameMatches,");
//		sb.append(subtotal);
//		System.out.println(sb);
		return noBlanks.compareToIgnoreCase(property) == 0;
	}

	/**
	 * @param property
	 * @param o
	 * @param jObject
	 * @param p
	 * @return
	 * @throws EolRuntimeException
	 * @throws EpsilonCOMException
	 */
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
			}
			else {
				try {
					o = jObject.invoke("Item", args);		//, byRefArgs);
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
		lastSetProperty = property;
	}

	@Override
	public boolean knowsProperty(String property) {
		getPtcProperty(property);
		return ptcCache2.containsKey(property);
	}

}
