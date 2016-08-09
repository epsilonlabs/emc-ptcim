package org.eclipse.epsilon.emc.COM;


public class COMProperty {
		
		private final String name;
		private final boolean isPublic;		// Not sure this is of use
		private final boolean readOnly;
		private final boolean isMultiple;
		private final boolean isAssociation;
		
		public COMProperty(String name, boolean isPublic, boolean readOnly, boolean isMultiple, boolean isAssociation) {
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