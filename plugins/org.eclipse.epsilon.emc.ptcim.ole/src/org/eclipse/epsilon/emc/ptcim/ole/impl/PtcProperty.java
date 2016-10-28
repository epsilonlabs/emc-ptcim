/*******************************************************************************
 * Copyright (c) 2016 University of York
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Horacio Hoyos - Initial API and implementation
 *******************************************************************************/
package org.eclipse.epsilon.emc.ptcim.ole.impl;


/**
 * The Class PtcProperty is used to access PTC IM properties, using their characteristics
 * (multiplicity, visibility, etc.) to return the correct Java wrapper objects. 
 */
@Deprecated
public class PtcProperty {
	
		
		
		/** The name. */
		private final String name;
		
		/** The is public. */
		private final boolean isPublic;		// Not sure this is of use
		
		/** The read only. */
		private final boolean readOnly;
		
		/** The is multiple. */
		private final boolean isMultiple;
		
		/** The is association. */
		private final boolean isAssociation;
		
		/**
		 * Instantiates a new COM property.
		 *
		 * @param name the name
		 * @param isPublic the is public
		 * @param readOnly the read only
		 * @param isMultiple the is multiple
		 * @param isAssociation the is association
		 */
		public PtcProperty(String name, boolean isPublic, boolean readOnly, boolean isMultiple, boolean isAssociation) {
			super();
			this.name = name;
			this.isPublic = isPublic;
			this.readOnly = readOnly;
			this.isMultiple = isMultiple;
			this.isAssociation = isAssociation;
		}

		/**
		 * Gets the name.
		 *
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Checks if is association.
		 *
		 * @return true, if is association
		 */
		public boolean isAssociation() {
			return isAssociation;
		}

		/**
		 * Checks if is public.
		 *
		 * @return true, if is public
		 */
		public boolean isPublic() {
			return isPublic;
		}

		/**
		 * Checks if is read only.
		 *
		 * @return true, if is read only
		 */
		public boolean isReadOnly() {
			return readOnly;
		}

		/**
		 * Checks if is multiple.
		 *
		 * @return true, if is multiple
		 */
		public boolean isMultiple() {
			return isMultiple;
		}
			
	} 