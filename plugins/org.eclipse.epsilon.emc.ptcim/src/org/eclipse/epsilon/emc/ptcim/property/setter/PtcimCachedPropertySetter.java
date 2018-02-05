package org.eclipse.epsilon.emc.ptcim.property.setter;

import org.eclipse.epsilon.emc.ptcim.PtcimObject;
import org.eclipse.epsilon.emc.ptcim.models.PtcimModel;
import org.eclipse.epsilon.emc.ptcim.property.manager.PtcimPropertyManager;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;

public class PtcimCachedPropertySetter extends PtcimPropertySetter {
	private final PtcimModel model;
	
	public PtcimCachedPropertySetter(PtcimPropertyManager manager, PtcimModel model) {
		setManager(manager);
		this.model = model;
		//System.out.println("Just created a cached setter...");
	}
		
	/* (non-Javadoc)		
	 * @see org.eclipse.epsilon.eol.execute.introspection.IPropertySetter#invoke(java.lang.Object)		
	 */		
	@Override		
	public void invoke(Object value) throws EolRuntimeException {	
		super.invoke(value);
		String elementPropertyIdentifier = getManager().buildCachedElementPropertyIdentifier((PtcimObject) object, property);
		model.propertiesValuesCache.put(elementPropertyIdentifier, value);
	}		
}
