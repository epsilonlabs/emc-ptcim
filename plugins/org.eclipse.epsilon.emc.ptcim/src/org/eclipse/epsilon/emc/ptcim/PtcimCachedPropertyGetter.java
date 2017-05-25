package org.eclipse.epsilon.emc.ptcim;

import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;

public class PtcimCachedPropertyGetter extends PtcimPropertyGetter {

	/** The property manager. */		
 	private final PtcimModel model;
 	
 	public PtcimCachedPropertyGetter(PtcimPropertyManager manager, PtcimModel model) {
		super.setManager(manager);
		this.model = model;
		//System.out.println("Just created a cached getter...");
	}
 			
 	/* (nonJavadoc)		
 	 * @see org.eclipse.epsilon.eol.execute.introspection.IPropertyGetter#invoke(java.lang.Object, java.lang.String)		
 	 */		
 	@Override		
 	public Object invoke(Object object, String property) throws EolRuntimeException {
 		String normalisedProperty = super.getManager().normalise(property);
 		String elementPropertyIdentifier = super.getManager().buildCachedElementPropertyIdentifier((PtcimObject) object, normalisedProperty);
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
