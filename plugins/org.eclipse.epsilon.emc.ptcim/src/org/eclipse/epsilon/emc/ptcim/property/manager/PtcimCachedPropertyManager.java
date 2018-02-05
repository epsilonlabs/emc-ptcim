package org.eclipse.epsilon.emc.ptcim.property.manager;

import java.util.WeakHashMap;

import org.eclipse.epsilon.emc.ptcim.PtcimObject;
import org.eclipse.epsilon.emc.ptcim.property.PtcimProperty;

public class PtcimCachedPropertyManager extends PtcimPropertyManager {
	
public static final PtcimCachedPropertyManager INSTANCE = new PtcimCachedPropertyManager();
		
	public PtcimCachedPropertyManager getInstance() {
		System.out.println("This runs with a cached property manager.");
		return INSTANCE;
	}
	
	public WeakHashMap<String, PtcimProperty> elementPropertiesNamesCache = new WeakHashMap<String, PtcimProperty>();
	
	@Override
	public PtcimProperty getPtcProperty(PtcimObject object, String property) {
		String typeDotPropertyNameId = buildCachedTypePropertyIdentifier(object, property);
		PtcimProperty prop = elementPropertiesNamesCache.get(typeDotPropertyNameId);
		if (prop == null) {
			prop = super.getPtcProperty(object, property);
			elementPropertiesNamesCache.put(buildCachedTypePropertyIdentifier(object, property), prop);
		} 
 		return prop;
 	}
	
}
