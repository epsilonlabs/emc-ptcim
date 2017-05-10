/*******************************************************************************
 * Copyright (c) 2016 University of York
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Hoacio Hoyos Rodriguez - Initial API and implementation
 *******************************************************************************/
package org.eclipse.epsilon.emc.ptcim.com4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.epsilon.eol.exceptions.EolInternalException;
import org.jawin.COMException;
import org.jawin.COMPtr;
import org.jawin.DispatchPtr;
import org.jawin.GUID;
import org.jawin.Variant;

import com4j.Com4jObject;
import com4j.ComThread;
import com4j.EventCookie;

public abstract class Com4jPtcimObject implements IAutomationCaseObject {

	private String id;
	
	public Com4jPtcimObject() { }
	
	public String getType() {
		String strType = null;
		strType = (String) this.property("Type", null);
		return strType;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Com4jPtcimObject other = (Com4jPtcimObject) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getId() {
		return id;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public void setId(String id) {
		// We should only set it once
		if (this.id != null) {
			throw new IllegalStateException("The id should only be set once");
		}
		this.id = id;
	}
	
	/* (non-Javadoc)
	 * @see org.jawin.COMPtr#toString()
	 */
	@Override
	public String toString() {
		if (id != null) {
			return id;
		}
		else {
			return super.toString();
		}
	}
	
	public List<Com4jPtcimObject> wrapInCollection(Com4jPtcimObject owner, String association) {
		return new Com4jPtcimCollection(this, owner, association);
	}
	
	public Collection<Com4jPtcimObject> wrapInFilteredColleciton(String association) {
		return new Com4jPtcimFilteredCollection(this, association);
	}

	public void disconnect() {
		// TODO Auto-generated method stub
		
	}
	
}
