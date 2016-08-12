package org.eclipse.epsilon.emc.artisan.jawin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.epsilon.emc.COM.COMObject;
import org.eclipse.epsilon.emc.COM.COMProperty;
import org.eclipse.epsilon.emc.COM.COMPropertyManager;
import org.eclipse.epsilon.emc.COM.EpsilonCOMException;

public class JawinPropertyManager implements COMPropertyManager {
	
	public static final COMPropertyManager INSTANCE = new JawinPropertyManager();
	
	private static final Object ASSOCIATION_ROLE = "Association";
	
	@Override
	public COMPropertyManager getInstance() {
		return INSTANCE;
	}
	
	private Map<COMObject, Map<String, COMProperty>> cache;
	
	public JawinPropertyManager() {
		super();
		this.cache = new HashMap<COMObject, Map<String,COMProperty>>();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.artisan.jawin.COMPropertyManager#getProperty(org.eclipse.epsilon.emc.COM.COMObject, java.lang.String)
	 */
	@Override
	public COMProperty getProperty(COMObject object, String property) {
		Map<String, COMProperty> cachedProps = cache.get(object);
		if (cachedProps == null) {
			cachedProps = new HashMap<String, COMProperty>();
			cache.put(object, cachedProps);
		}
		COMProperty prop = cachedProps.get(property);
		if (prop == null) {
			List<Object> args = new ArrayList<Object>();
			args.add("All Property Descriptors");
			String descriptors = null;
			try {
				descriptors = (String) object.get("Property", args);
			} catch (EpsilonCOMException e) {
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
					prop = new COMProperty(name, isPublic, readOnly, isMultiple, isAssociation);
					break;
				}
			}
			cachedProps.put(property, prop);
		}
		return prop;
	}



//	public COMProperty getProperty(String type, String property) {
//		Map<String, COMProperty> cachedProps = cache.get(type);
//		if (cachedProps == null) {
//			cachedProps = new HashMap<String, COMProperty>();
//			cache.put(type, cachedProps);
//		}
//		COMProperty prop = cachedProps.get(property);
//		if (prop == null) {
//			List<Object> args = new ArrayList<Object>();
//			args.add(type);
//			String descriptors = null;
//			try {
//				COMObject res = model.invoke("GetClassProperties", args);
//				assert res instanceof JawinPrimitive;
//				descriptors = (String) ((JawinPrimitive) res).getPrimitive();
//			} catch (EpsilonCOMException e) {
//				// TODO We probaly need better understanding of errors
//				e.printStackTrace();
//			}
//			List<String> list = Arrays.asList(descriptors.split("\\n"));
//			for (String d : list) {
//				String[] info = d.split(",");
//				String name = unQuote(info[0]);
//				if (nameMatches(name, property)) {
//					String role = unQuote(info[1]);
//					boolean isAssociation = false;
//					if (role.equals(ASSOCIATION_ROLE)) {
//						isAssociation = true;
//					}
//					boolean isPublic = true;
//					String access = unQuote(info[2]);
//					if (access.length() > 2) {		// private access is 3 letters: xxP
//						isPublic = false;
//					}
//					boolean readOnly = false;
//					if (access.contains("O")) {		// RO or ROP
//						readOnly = true;
//					}
//					boolean isMultiple = false;
//					String multy = unQuote(info[3]);
//					if (multy.contains("+")) {
//						isMultiple = true;
//					}
//					prop = new COMProperty(name, isPublic, readOnly, isMultiple, isAssociation);
//					break;
//				}
//			}
//			cachedProps.put(property, prop);
//		}
//		return prop;
//	}
	
	/**
	 * Remove spaces from the Artisan name and compare case insensitive
	 * @param name
	 * @param property
	 * @return
	 */
	private boolean nameMatches(String name, String property) {
		String noBlanks = name.replaceAll("\\s","");
		return noBlanks.compareToIgnoreCase(property) == 0;
	}



	private String unQuote(String name) {
		return name.replaceAll("^\"|\"$", "");
	}
	

}
