package org.eclipse.epsilon.emc.ptcim.com4j;

import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;

public class Com4jPtcimCachedPropertySetter extends Com4jPtcimPropertySetter {
	private final Com4jPtcimModel model;
	
	public Com4jPtcimCachedPropertySetter(Com4jPtcimPropertyManager manager, Com4jPtcimModel model) {
		super.setManager(manager);
		this.model = model;
		//System.out.println("Just created a cached setter...");
	}
		
	/* (non-Javadoc)		
	 * @see org.eclipse.epsilon.eol.execute.introspection.IPropertySetter#invoke(java.lang.Object)		
	 */		
	@Override		
	public void invoke(Object value) throws EolRuntimeException {	
		super.invoke(value);
		String elementPropertyIdentifier = super.getManager().buildCachedElementPropertyIdentifier((Com4jPtcimObject) object, property);
		model.propertiesValuesCache.put(elementPropertyIdentifier, value);
	}		
}
