package org.eclipse.epsilon.emc.ptcim.jawin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.epsilon.emc.ptcim.ole.impl.EpsilonCOMException;
import org.eclipse.epsilon.emc.ptcim.ole.impl.PtcProperty;

public class JawinPropertyManager {
	
	public static final JawinPropertyManager INSTANCE = new JawinPropertyManager();
	
	private static final Object ASSOCIATION_ROLE = "Association";
	
	public JawinPropertyManager getInstance() {
		return INSTANCE;
	}
	
	private Map<JawinObject, Map<String, PtcProperty>> cache;
	
	public JawinPropertyManager() {
		super();
		this.cache = new HashMap<JawinObject, Map<String,PtcProperty>>();
	}
	
	public void dispose() {
		cache.clear();
	}
	
	public PtcProperty getPtcProperty(JawinObject object, String property) {
		long start = System.nanoTime();
		Map<String, PtcProperty> cachedProps = cache.get(object);
		if (cachedProps == null) {
			cachedProps = new HashMap<String, PtcProperty>();
			cache.put(object, cachedProps);
		}
		PtcProperty prop = cachedProps.get(property);
		if (prop == null) {
			List<Object> args = new ArrayList<Object>();
			args.add("All Property Descriptors");
			String descriptors = null;
			try {
				descriptors = (String) object.getAttribute("Property", args);
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
					prop = new PtcProperty(name, isPublic, readOnly, isMultiple, isAssociation);
					break;
				}
			}
			cachedProps.put(property, prop);
		}
		long total = System.nanoTime() - start;
		System.out.println("JawinPropertyManager,getProperty," + object + "," + property + "," + total);
		return prop;
	}
	
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

	public PtcProperty getPtcProperty(String property) {
		return null;
	}

	public boolean knowsProperty(String property) {
		return false;
	}
}
