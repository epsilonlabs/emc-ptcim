package org.eclipse.epsilon.emc.artisan.jawin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.epsilon.emc.COM.EpsilonCOMException;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.introspection.AbstractPropertyGetter;

public class JawinPropertyGetter extends AbstractPropertyGetter {

	@Override
	public Object invoke(Object object, String property) throws EolRuntimeException {
		assert object instanceof JawinObject;
		JawinObject jObject = (JawinObject) object;
		List<Object> args = new ArrayList<Object>();
		args.add(property);
		args.add(null);
		Object o;
		try {
			// TODO which is best?
			o = jObject.get("Property", args);
			Object o2 = jObject.get("Property", property);
			//System.out.println(o2);
		} catch (EpsilonCOMException e) {
			throw new EolRuntimeException(e.getMessage());
		}
		if (o instanceof JawinPrimitive) {
			return ((JawinPrimitive) o).getPrimitive();
		}
		else {
			return o;
		}
	}
	
	@Override
	public boolean hasProperty(Object object, String property) {
		assert object instanceof JawinObject;
		JawinObject jObject = (JawinObject) object;
		List<Object> args = new ArrayList<Object>();
		args.add("All Property Descriptors");
		Object props = null;
		try {
			// ("Property Descriptors", "Public") 
			// ("Property Descriptors", "Private") 
			props = jObject.get("Property", args);
		} catch (EpsilonCOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		List<String> list = Arrays.asList(((String) props).split("\\n"));
		for (String type : list) {
			String[] info = type.split(",");
			String name = info[0];
			name = name.replaceAll("^\"|\"$", "");
			if (name.contentEquals(property)) {
				return true;
			}
			
		}
		return false;
		
	}
	

}
