package org.eclipse.epsilon.emc.ptcim.com4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.epsilon.eol.exceptions.EolInternalException;

public class Com4jPtcimPropertyManager {
	
	public static final Com4jPtcimPropertyManager INSTANCE = new Com4jPtcimPropertyManager();
	
	private static final Object ASSOCIATION_ROLE = "Association";
	
	public Com4jPtcimPropertyManager getInstance() {
		System.out.println("This runs with a normal property manager.");
		return INSTANCE;
	}
	
	public Com4jPtcimProperty getPtcProperty(Com4jPtcimObject object, String property) {
		Com4jPtcimProperty prop = null;
		String descriptors = (String) object.property("All Property Descriptors", null);
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
				prop = new Com4jPtcimProperty(name, isPublic, readOnly, isMultiple, isAssociation);
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
 	
 	// Normalisation: we assume that for example "Child Object" is treated the same as "child object", 
 	// "childObject" or "childobject" so we remove the spaces and transform the input to lowercase. 
 	public static String normalise(String theString) {
 		return theString.replaceAll("\\s", "").toLowerCase();
 	}
	
 	public String buildCachedTypePropertyIdentifier(Com4jPtcimObject object, String property) {
 		return ((Com4jPtcimObject) object).getType() + "." + property;
 	}
 	
 	public String buildCachedElementPropertyIdentifier(Com4jPtcimObject object, String property) {
 		return ((Com4jPtcimObject) object).getId() + "." + property;
 	}
}
