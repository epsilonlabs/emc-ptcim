package org.eclipse.epsilon.emc.artisan.jawin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.epsilon.emc.COM.COMObject;
import org.eclipse.epsilon.emc.COM.EpsilonCOMException;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.introspection.AbstractPropertyGetter;

public class JawinPropertyGetter extends AbstractPropertyGetter {

	private static final Object ASSOCIATION_ROLE = "Association";

	@Override
	public Object invoke(Object object, String property) throws EolRuntimeException {
		assert object instanceof JawinObject;
		JawinObject jObject = (JawinObject) object;
		Object o = null;
		try {
			Property p = getProperty(object, property);
			if (p == null) {
				throw new EolRuntimeException("No such property");
			}
			if (p.isMultiple) {
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
			// Do we need to cast/something else?
			if (!p.isAssociation) {
				// Test if it is a Cr/Lf to return the list?
				assert o instanceof String;
				String[] multivalues = ((String) o).split("\\n");
				if (multivalues.length > 1) {
					List<String> list = Arrays.asList(multivalues);
					o = list;
				}				
			}
		} catch (EpsilonCOMException e) {
			throw new EolRuntimeException(e.getMessage());
		}
		return o;
	}
	
	@Override
	public boolean hasProperty(Object object, String property) {
		Property p = getProperty(object, property);
		return p != null;
	}
	
	private Property getProperty(Object object, String property) {
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
			throw new IllegalArgumentException("Failed to get the Property Descriptors of the Object", e);
		}
		List<String> list = Arrays.asList(((String) props).split("\\n"));
		Property p = null;
		for (String type : list) {
			String[] info = type.split(",");
			String name = unQuote(info[0]);
			if (name.contentEquals(property)) {
				String role = unQuote(info[1]);
				boolean isAssociation = false;
				if (role.equals(ASSOCIATION_ROLE)) {
					isAssociation = true;
				}
				boolean isPublic = true;
				String access = unQuote(info[2]);
				if (access.length() > 2) {		// private access is 3 letters: xxP
					isPublic = false;
				}
				boolean readOnly = false;
				if (access.contains("O")) {		// RO or ROP
					readOnly = true;
				}
				boolean isMultiple = false;
				String multy = unQuote(info[3]);
				if (multy.contains("+")) {
					isMultiple = true;
				}
				p = new Property(property, isPublic, readOnly, isMultiple, isAssociation);
				break;
			}
		}
		return p;
	}

	private String unQuote(String name) {
		return name.replaceAll("^\"|\"$", "");
	}
	
	private class Property {
		private final String name;
		private final boolean isPublic;		// Not sure this is of use
		private final boolean readOnly;
		private final boolean isMultiple;
		private final boolean isAssociation;
		
		public Property(String name, boolean isPublic, boolean readOnly, boolean isMultiple, boolean isAssociation) {
			super();
			this.name = name;
			this.isPublic = isPublic;
			this.readOnly = readOnly;
			this.isMultiple = isMultiple;
			this.isAssociation = isAssociation;
		}

		public String getName() {
			return name;
		}

		public boolean isAssociation() {
			return isAssociation;
		}

		public boolean isPublic() {
			return isPublic;
		}

		public boolean isReadOnly() {
			return readOnly;
		}

		public boolean isMultiple() {
			return isMultiple;
		}
			
	} 
	

}
