package org.eclipse.epsilon.emc.ptcim.com4j;		
		
import java.util.ArrayList;		
import java.util.List;		
		
import org.eclipse.epsilon.eol.exceptions.EolIllegalPropertyAssignmentException;
import org.eclipse.epsilon.eol.exceptions.EolInternalException;
import org.eclipse.epsilon.eol.exceptions.EolReadOnlyPropertyException;		
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;		
import org.eclipse.epsilon.eol.execute.introspection.AbstractPropertySetter;		
		
public class Com4jPtcimPropertySetter extends AbstractPropertySetter {		
		
	/** The property  manager. */		
	private Com4jPtcimPropertyManager manager;
	
	public Com4jPtcimPropertyManager getManager() {
		return manager;
	}

	public void setManager(Com4jPtcimPropertyManager manager) {
		this.manager = manager;
	}

	/** The COM property. */		
	private Com4jPtcimProperty comProperty;		
	
	public Com4jPtcimPropertySetter() {}
	
	public Com4jPtcimPropertySetter(Com4jPtcimPropertyManager manager) {
		//System.out.println("Just created a normal setter...");
		this.manager = manager;
	}
		
	/* (non-Javadoc)		
	 * @see org.eclipse.epsilon.eol.execute.introspection.IPropertySetter#invoke(java.lang.Object)		
	 */		
	@Override		
	public void invoke(Object value) throws EolRuntimeException {		
		
		comProperty = manager.getPtcProperty((Com4jPtcimObject) object, property);		
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
			if (!(value instanceof Com4jPtcimObject)) {		
				throw new EolRuntimeException("Association (0..1) properties' values must be COM objects.");		
			}		
			((Com4jPtcimObject) object).add(comProperty.getName(), value);		
		}		
		else {		
			((Com4jPtcimObject) object).propertySet(comProperty.getName(), 0, value);		
		}		
	}		
}