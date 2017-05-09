package org.eclipse.epsilon.emc.ptcim.com4j;

import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;

public class Com4jPtcimCachedPropertyGetter extends Com4jPtcimPropertyGetter {

	/** The property manager. */		
 	private final Com4jPtcimModel model;
 	
 	public Com4jPtcimCachedPropertyGetter(Com4jPtcimPropertyManager manager, Com4jPtcimModel model) {
		super.setManager(manager);
		this.model = model;
		//System.out.println("Just created a cached getter...");
	}
 			
 	/* (nonJavadoc)		
 	 * @see org.eclipse.epsilon.eol.execute.introspection.IPropertyGetter#invoke(java.lang.Object, java.lang.String)		
 	 */		
 	@Override		
 	public Object invoke(Object object, String property) throws EolRuntimeException {		
 		String elementPropertyIdentifier = super.getManager().buildCachedElementPropertyIdentifier((Com4jPtcimObject) object, property);
 		Object o = model.propertiesValuesCache.get(elementPropertyIdentifier);		
 		if (o == null) {
 			//System.out.println("I didn't knew the property value...");
 			o = super.invoke(object, property);
 			model.propertiesValuesCache.put(elementPropertyIdentifier, o);
 		} else {
 			//System.out.println("I knew the property value...");
 		}
 		return o;		
 	}		
}
