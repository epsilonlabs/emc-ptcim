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
package org.eclipse.epsilon.emc.ptcim;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.epsilon.eol.exceptions.EolInternalException;

import com4j.Com4jObject;
import com4j.ComThread;
import com4j.EventCookie;

public class PtcimObject implements IAutomationCaseObject {

	private String id;
	private IAutomationCaseObject theIaco; 
	
	public void setTheIaco(IAutomationCaseObject theIaco) {
		this.theIaco = theIaco;
	}


	public IAutomationCaseObject getTheIaco() {
		return theIaco;
	}


	public PtcimObject(IAutomationCaseObject iaco) {
		this.theIaco = iaco;
	}

	
	public String getType() {
		String strType = null;
		strType = (String) theIaco.property("Type", null);
		return strType;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (theIaco == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PtcimObject other = (PtcimObject) obj;
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
	
	public List<PtcimObject> wrapInCollection(PtcimObject owner, String association) {
		return new PtcimCollection(this, owner, association);
	}
	
	public Collection<PtcimObject> wrapInFilteredColleciton(String association) {
		return new PtcimFilteredCollection(this, association);
	}

	@Override
	public <T> EventCookie advise(Class<T> arg0, T arg1) {
		return theIaco.advise(arg0, arg1);
	}

	@Override
	public void dispose() {
		theIaco.dispose();
		
	}

	@Override
	public ComThread getComThread() {
		return theIaco.getComThread();
	}

	@Override
	public long getIUnknownPointer() {
		return theIaco.getIUnknownPointer();
	}

	@Override
	public long getPointer() {
		return theIaco.getPointer();
	}

	@Override
	public int getPtr() {
		return theIaco.getPtr();
	}

	@Override
	public <T extends Com4jObject> boolean is(Class<T> arg0) {
		return theIaco.is(arg0);
	}

	@Override
	public <T extends Com4jObject> T queryInterface(Class<T> arg0) {
		return queryInterface(arg0);
	}

	@Override
	public void setName(String arg0) {
		theIaco.setName(arg0);
	}

	@Override
	public void property(Object type, Object index, Object retval) {
		theIaco.property(type, index, retval);
		
	}

	@Override
	public Object property(Object type, Object index) {
		return theIaco.property(type, index);
	}

	@Override
	public Com4jObject item(String role, Object index) {
		return theIaco.item(role, index);
	}

	@Override
	public Com4jObject items(String role, Object index) {
		return theIaco.items(role, index);
	}

	@Override
	public Com4jObject add(String role, Object value) {
		return theIaco.add(role, value);
	}

	@Override
	public void remove(String role, Object index) {
		theIaco.remove(role, index);
	}

	@Override
	public Com4jObject link(String role, Object index) {
		return theIaco.link(role, index);
	}

	@Override
	public void resetQueryItems() {
		theIaco.resetQueryItems();
	}

	@Override
	public Com4jObject nextItem() {
		return theIaco.nextItem();
	}

	@Override
	public int moreItems() {
		return theIaco.moreItems();
	}

	@Override
	public void propertySet(Object type, Object index, Object data) {
		theIaco.propertySet(type, index, data);
	}

	@Override
	public Object propertyGet(Object type, Object index) {
		return theIaco.propertyGet(type, index);
	}

	@Override
	public Iterator<Com4jObject> iterator() {
		return theIaco.iterator();
	}

	@Override
	public Com4jObject findObject(String itemId) {
		return theIaco.findObject(itemId);
	}

	@Override
	public Object getClassProperties(String className, Object index) {
		return theIaco.getClassProperties(className, index);
	}

	@Override
	public void checkLicenses(int bCheck) {
		theIaco.checkLicenses(bCheck);
	}

	@Override
	public Com4jObject itemByID(String id) {
		return theIaco.itemByID(id);
	}

	@Override
	public Com4jObject itemEx(String role, Object index, Object searchProperty) {
		return theIaco.itemEx(role, index, searchProperty);
	}

	@Override
	public void refresh() {
		theIaco.refresh();
	}

	@Override
	public void export(String directory) {
		theIaco.export(directory);
	}

	@Override
	public void _import(String directory) {
		theIaco._import(directory);
	}

	@Override
	public void diff(String directory) {
		theIaco.diff(directory);
	}

	@Override
	public Object itemCount(String role) {
		return theIaco.itemCount(role);
	}

	@Override
	public void reorderItem(String role, Com4jObject pObject, Com4jObject pPredecessorObject,
			Com4jObject pSuccessorObject) {
		theIaco.reorderItem(role, pObject, pPredecessorObject, pSuccessorObject);
	}

	@Override
	public Object getExtendedClassProperties(String className) {
		return theIaco.getExtendedClassProperties(className);
	}

	@Override
	public Com4jObject addDirected(String role, Com4jObject pHint) {
		return theIaco.addDirected(role, pHint);
	}

	@Override
	public void exportEx(String directory, int bDoSubDirs) {
		theIaco.exportEx(directory, bDoSubDirs);
	}

	@Override
	public void delete() {
		theIaco.delete();
	}

	@Override
	public String displayName(String tagName) {
		return theIaco.displayName(tagName);
	}

	@Override
	public Com4jObject createClone(Com4jObject pCloneThis) {
		return theIaco.createClone(pCloneThis);
	}

	@Override
	public void merge(Com4jObject pSrcItem) {
		theIaco.merge(pSrcItem);
	}

	@Override
	public Com4jObject addByType(String type, String role) {
		return theIaco.addByType(type, role);
	}

	@Override
	public int isConnectedTo(String role, Com4jObject pOtherObject) {
		return theIaco.isConnectedTo(role, pOtherObject);
	}
	
}
