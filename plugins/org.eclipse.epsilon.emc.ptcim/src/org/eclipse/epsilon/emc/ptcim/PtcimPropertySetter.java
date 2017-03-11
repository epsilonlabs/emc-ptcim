package org.eclipse.epsilon.emc.ptcim;		
		
import java.util.ArrayList;		
import java.util.List;		
		
import org.eclipse.epsilon.eol.exceptions.EolIllegalPropertyAssignmentException;
import org.eclipse.epsilon.eol.exceptions.EolInternalException;
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
		//System.out.println("Just created a normal setter...");
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
		List<Object> args = new ArrayList<Object>();		
		args.add(comProperty.getName());		
		if (comProperty.isAssociation()) {		
			if (!(value instanceof PtcimObject)) {		
				throw new EolRuntimeException("Association (0..1) properties' values must be COM objects.");		
			}		
			try {		
				args.add(((PtcimObject) value));		
				((PtcimObject) object).invoke("Add", args);		
			} catch (EolInternalException e) {		
				// TODO Auto-generated catch block		
				System.err.println("Error for " + comProperty.getName() + " for value " + value);		
				e.printStackTrace();		
				throw new EolIllegalPropertyAssignmentException(getProperty(), getAst());		
			}		
		}		
		else {		
			args.add(0);		
			args.add(value);					
			try {		
				((PtcimObject) object).invoke("PropertySet", args);		
			} catch (EolInternalException e) {		
				throw new EolIllegalPropertyAssignmentException(getProperty(), getAst());		
			}		
		}		
	}		
}