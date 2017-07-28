package org.eclipse.epsilon.emc.ptcim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.WeakHashMap;

public class PtcimCachedPropertyManager extends PtcimPropertyManager {
	
public static final PtcimCachedPropertyManager INSTANCE = new PtcimCachedPropertyManager();
	
	private static final Object ASSOCIATION_ROLE = "Association";
	
	public PtcimCachedPropertyManager getInstance() {
		System.out.println("This runs with a cached property manager.");
		return INSTANCE;
	}
	
	public WeakHashMap<String, PtcimProperty> elementPropertiesNamesCache = new WeakHashMap<String, PtcimProperty>();
	
	public PtcimProperty getPtcProperty(PtcimObject object, String property) {
		List<Object> args = new ArrayList<Object>();
		args.add("All Property Descriptors");
		String descriptors = null;
		PtcimProperty prop = null;
		String typeDotPropertyNameId = buildCachedTypePropertyIdentifier(object, property);
		prop = elementPropertiesNamesCache.get(typeDotPropertyNameId);
		if (prop == null) {
			descriptors = (String) object.property("All Property Descriptors", null);
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
					prop = new PtcimProperty(name, isPublic, readOnly, isMultiple, isAssociation);
					elementPropertiesNamesCache.put(buildCachedTypePropertyIdentifier(object, property), prop);
					break;
				}
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
 	
 	// Normalisation: we assume that for example "Child Object" is treated the same as "child object", 
 	// "childObject" or "childobject" so we remove the spaces and transform the input to lowercase. 
 	public static String normalise(String theString) {
 		return theString.replaceAll("\\s", "").toLowerCase();
 	}
 
}
