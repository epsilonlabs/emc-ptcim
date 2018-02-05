package org.eclipse.epsilon.emc.ptcim.property.getter;

import org.eclipse.epsilon.emc.ptcim.PtcimObject;
import org.eclipse.epsilon.emc.ptcim.models.PtcimModel;
import org.eclipse.epsilon.emc.ptcim.property.manager.PtcimPropertyManager;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;

public class PtcimCachedPropertyGetter extends PtcimPropertyGetter {

	/** The property manager. */		
 	private final PtcimModel model;
 	
 	public PtcimCachedPropertyGetter(PtcimPropertyManager manager, PtcimModel model) {
		super.setManager(manager);
		this.model = model;
	}
 			
 	/* (nonJavadoc)		
 	 * @see org.eclipse.epsilon.eol.execute.introspection.IPropertyGetter#invoke(java.lang.Object, java.lang.String)		
 	 */		
 	@Override		
 	public Object invoke(Object object, String property) throws EolRuntimeException {		
 		String elementPropertyIdentifier = getManager().buildCachedElementPropertyIdentifier((PtcimObject) object, property);
 		Object invokedObject = model.propertiesValuesCache.get(elementPropertyIdentifier);		
 		if (invokedObject == null) {
 			invokedObject = super.invoke(object, property);
 			model.propertiesValuesCache.put(elementPropertyIdentifier, invokedObject);
 		}
 		return invokedObject;		
 	}		
}
