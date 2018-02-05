package org.eclipse.epsilon.emc.ptcim.property.setter;		
		
import org.eclipse.epsilon.emc.ptcim.PtcimObject;
import org.eclipse.epsilon.emc.ptcim.property.PtcimProperty;
import org.eclipse.epsilon.emc.ptcim.property.manager.PtcimPropertyManager;
import org.eclipse.epsilon.eol.exceptions.EolReadOnlyPropertyException;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.introspection.AbstractPropertySetter;		
		
public class PtcimPropertySetter extends AbstractPropertySetter {		
		
	/** The property  manager. */		
	private PtcimPropertyManager manager;
	
	public PtcimPropertyManager getManager() {
		return manager;
	}

	public void setManager(PtcimPropertyManager manager) {
		this.manager = manager;
	}

	/** The COM property. */		
	private PtcimProperty comProperty;		
	
	public PtcimPropertySetter() {}
	
	public PtcimPropertySetter(PtcimPropertyManager manager) {
		this.manager = manager;
	}
		
	/* (non-Javadoc)		
	 * @see org.eclipse.epsilon.eol.execute.introspection.IPropertySetter#invoke(java.lang.Object)		
	 */		
	@Override		
	public void invoke(Object value) throws EolRuntimeException {		
		comProperty = manager.getPtcProperty((PtcimObject) object, property);		
		if (comProperty != null) {		
			super.setProperty(property);		
		}		
		else {		
			// FIXME It can be other reason, double check 		
			throw new IllegalArgumentException("The propery can't be found in the object");		
		}	
		
		// TODO Check if value matches property? See EMF Setter		
		if (comProperty.isReadOnly()) {		
			throw new EolReadOnlyPropertyException();		
		}		
		if (comProperty.isAssociation()) {		
			if (!(value instanceof PtcimObject)) {		
				throw new EolRuntimeException("Association (0..1) properties' values must be COM objects.");		
			}		
			((PtcimObject) object).add(comProperty.getName(), value);		
		}		
		else {		
			((PtcimObject) object).propertySet(comProperty.getName(), 0, value);		
		}		
	}		
}