package org.eclipse.epsilon.emc.ptcim.property.manager;

import java.util.Arrays;
import java.util.List;

import org.eclipse.epsilon.emc.ptcim.PtcimObject;
import org.eclipse.epsilon.emc.ptcim.property.PtcimProperty;

public class PtcimPropertyManager {
	
	public static final PtcimPropertyManager INSTANCE = new PtcimPropertyManager();
	
	protected static final Object ASSOCIATION_ROLE = "Association";
	
	public PtcimPropertyManager getInstance() {
		System.out.println("This runs with a normal property manager.");
		return INSTANCE;
	}
	
	public PtcimProperty getPtcProperty(PtcimObject object, String property) {
		PtcimProperty prop = null;
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
				prop = new PtcimProperty(name, isPublic, readOnly, isMultiple, isAssociation);
				break;
			}
		}
		return prop;
 	}
	
		 
 	protected String unQuote(String name) {
 		return name.replaceAll("^\"|\"$", "");
 	}
 	
 	// Normalisation: we assume that for example "Child Object" is treated the same as "child object", 
 	// "childObject" or "childobject" so we remove the spaces and transform the input to lowercase. 
 	public static String normalise(String theString) {
 		return theString.replaceAll("\\s", "").toLowerCase();
 	}

 	protected boolean nameMatches(String name, String property) {
	 	return normalise(name).compareToIgnoreCase(normalise(property)) == 0;
	}

 	public String buildCachedTypePropertyIdentifier(PtcimObject object, String property) {
 		return normalise( ((PtcimObject) object).getType() + "." + property );
 	}
 	
 	public String buildCachedElementPropertyIdentifier(PtcimObject object, String property) {
 		return normalise( ((PtcimObject) object).getId() + "." + property );
 	}
}
