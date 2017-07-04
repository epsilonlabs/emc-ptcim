package org.eclipse.epsilon.emc.ptcim;

import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;

public class PtcimCachedPropertySetter extends PtcimPropertySetter {
	private final PtcimModel model;
	
	public PtcimCachedPropertySetter(PtcimPropertyManager manager, PtcimModel model) {
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
		String elementPropertyIdentifier = super.getManager().buildCachedElementPropertyIdentifier((PtcimObject) object, property);
		model.propertiesValuesCache.put(elementPropertyIdentifier, value);
	}		
}
