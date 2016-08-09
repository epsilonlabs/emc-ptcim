package org.eclipse.epsilon.emc.artisan.jawin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.epsilon.emc.COM.COMObject;
import org.eclipse.epsilon.emc.COM.COMProperty;
import org.eclipse.epsilon.emc.COM.EpsilonCOMException;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.introspection.AbstractPropertyGetter;

public class JawinPropertyGetter extends AbstractPropertyGetter {

	private final JawinPropertyManager manager = JawinPropertyManager.INSTANCE;
	
	@Override
	public Object invoke(Object object, String property) throws EolRuntimeException {
		assert object instanceof JawinObject;
		JawinObject jObject = (JawinObject) object;
		Object o = null;
		try {
			COMProperty p = manager.getProperty(jObject, property);
			if (p == null) {
				throw new EolRuntimeException("No such property");
			}
			if (p.isMultiple()) {
				JawinCollection<COMObject> elements;
				List<Object> args = new ArrayList<Object>();
				args.add("*");
				try {
					COMObject res = jObject.invoke("Items", property, args, 2);
					elements = new JawinCollection<COMObject>(res, jObject, property);
				} catch (EpsilonCOMException e) {
					throw new EolRuntimeException(e.getMessage());
				}
				o = elements;
			}
			else {
				List<Object> args = new ArrayList<Object>();
				args.add(property);
				args.add(null);
				// TODO which is best/correct?
				o = jObject.get("Property", args);
				//Object o2 = jObject.get("Property", property);
			}
		} catch (EpsilonCOMException e) {
			throw new EolRuntimeException(e.getMessage());
		}
		return o;
	}
	
	@Override
	public boolean hasProperty(Object object, String property) {
		assert object instanceof JawinObject;
		JawinObject jObject = (JawinObject) object;
		COMProperty p = manager.getProperty(jObject, property);
		return p != null;
	}

}
