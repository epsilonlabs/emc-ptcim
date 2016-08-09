package org.eclipse.epsilon.emc.artisan.jawin;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.emc.COM.COMObject;
import org.eclipse.epsilon.emc.COM.COMProperty;
import org.eclipse.epsilon.emc.COM.EpsilonCOMException;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.introspection.AbstractPropertySetter;

public class JawinPropertySetter extends AbstractPropertySetter {
	
	private final JawinPropertyManager manager = JawinPropertyManager.INSTANCE;
	private COMProperty comProperty;
	
	

	@Override
	public void setProperty(String property) {
		comProperty = manager.getProperty((COMObject) object, property);
		if (comProperty != null) {
			super.setProperty(property);
		}
		else {
			// FIXME It can be other reason, double check 
			throw new IllegalArgumentException("The propery can't be found in the object");
		}
	}



	@Override
	public void invoke(Object value) throws EolRuntimeException {
		// TODO Check if value matches property? See EMF Setter
		List<Object> args = new ArrayList<Object>();
		args.add(0);
		args.add(value);
		try {
			((COMObject) object).invoke("PropertySet", comProperty.getName(), args);
		} catch (EpsilonCOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
