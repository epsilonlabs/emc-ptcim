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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.epsilon.emc.ptcim.operations.contributors.PtcimCollectionOperationContributor;
import org.eclipse.epsilon.emc.ptcim.util.com4j.IAutomationCaseObject;

import com4j.Com4jObject;
import com4j.ComException;
import com4j.ComThread;
import com4j.EventCookie;

//Alternative PtcimObject
public class PtcimObject {

	private String id;
	private InnerAutomationCaseObject iaco;

	public PtcimObject(IAutomationCaseObject iaco) {
		this.iaco = new InnerAutomationCaseObject(iaco);
	}

	public void setTheIaco(IAutomationCaseObject theIaco) {
		getInner().setTheIaco(theIaco);
	}

	public IAutomationCaseObject getTheIaco() {
		return getInner().getTheIaco();
	}

	public static PtcimObject create(Com4jObject object) {
		return new PtcimObject(object.queryInterface(IAutomationCaseObject.class));
	}
	
	public static PtcimObject create(IAutomationCaseObject object) {
		return new PtcimObject(object);
	}

	private InnerAutomationCaseObject getInner() {
		return this.iaco;
	}

	public String getType() {
		String strType = null;
		strType = (String) getInner().getTheIaco().property("Type", null);
		return strType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (getInner().getTheIaco() == obj)
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jawin.COMPtr#toString()
	 */
	@Override
	public String toString() {
		if (id != null) {
			return id;
		} else {
			return super.toString();
		}
	}

	public List<PtcimObject> wrapInCollection(PtcimObject owner, String association) {
		return new PtcimCollectionOperationContributor(this, owner, association);
	}

	public Collection<PtcimObject> wrapInFilteredColleciton(String association) {
		return new PtcimFilteredCollection(this, association);
	}

	// Wrappers

	public <T> EventCookie advise(Class<T> arg0, T arg1) {
		return getInner().advise(arg0, arg1);
	}

	public void dispose() {
		getInner().dispose();
	}

	public ComThread getComThread() {
		return getInner().getComThread();
	}

	public long getIUnknownPointer() {
		return getInner().getIUnknownPointer();
	}

	public long getPointer() {
		return getInner().getPointer();
	}

	public int getPtr() {
		return getInner().getPtr();
	}

	public <T extends Com4jObject> boolean is(Class<T> arg0) {
		return getInner().is(arg0); // FIXME should be PtcimObject instead of Com4j
	}

	public void setName(String arg0) {
		getInner().setName(arg0);
	}

	public void property(Object type, Object index, Object retval) {
		getInner().property(type, index, retval);
	}

	public Object property(Object type, Object index) {
		return getInner().property(type, index);
	}

	public PtcimObject item(String role, Object index) {
		return PtcimObject.create(getInner().item(role, index).queryInterface(IAutomationCaseObject.class));
	}

	public PtcimObject items(String role, Object index) {
		return PtcimObject.create(getInner().item(role, index).queryInterface(IAutomationCaseObject.class));
	}

	public PtcimObject add(String role, Object value) {
		return PtcimObject.create(getInner().add(role, value).queryInterface(IAutomationCaseObject.class));
	}

	public void remove(String role, Object index) {
		getInner().remove(role, index);
	}

	public PtcimObject link(String role, Object index) {
		return PtcimObject.create(getInner().link(role, index).queryInterface(IAutomationCaseObject.class));
	}

	public void resetQueryItems() {
		getInner().resetQueryItems();
	}

	public PtcimObject nextItem() {
		return PtcimObject.create(getInner().nextItem().queryInterface(IAutomationCaseObject.class));
	}

	public int moreItems() {
		return getInner().moreItems();
	}

	public void propertySet(Object type, Object index, Object data) {
		getInner().propertySet(type, index, data);
	}

	public Object propertyGet(Object type, Object index) {
		return getInner().propertyGet(type, index);
	}

	public Iterator<Com4jObject> iterator() {
		return getInner().iterator();
	}
	
	/*public Iterator<PtcimObject> iterator() {
		return new Iterator<PtcimObject>() {

			Iterator<Com4jObject> iterator = getInner().iterator();

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public PtcimObject next() {
				return PtcimObject.create(iterator.next());
			}
		};
	}*/

	public PtcimObject findObject(String itemId) {
		return PtcimObject.create(getInner().findObject(itemId).queryInterface(IAutomationCaseObject.class));
	}

	public Object getClassProperties(String className, Object index) {
		return getInner().getClassProperties(className, index);
	}

	public void checkLicenses(int bCheck) {
		getInner().checkLicenses(bCheck);
	}

	public PtcimObject itemByID(String id) {
		return PtcimObject.create(getInner().itemByID(id).queryInterface(IAutomationCaseObject.class));
	}

	public PtcimObject itemEx(String role, Object index, Object searchProperty) {
		return PtcimObject.create(
				getInner().itemEx(role, index, searchProperty).queryInterface(IAutomationCaseObject.class));
	}

	public void refresh() {
		getInner().refresh();
	}

	public void export(String directory) {
		getInner().export(directory);
	}

	public void _import(String directory) {
		getInner()._import(directory);
	}

	public void diff(String directory) {
		getInner().diff(directory);
	}

	public Object itemCount(String role) {
		return getInner().itemCount(role);
	}

	public void reorderItem(String role, PtcimObject pObject, PtcimObject pPredecessorObject,
			PtcimObject pSuccessorObject) {
		getInner().reorderItem(role, pObject.getInner(), pPredecessorObject.getInner(), pSuccessorObject.getInner());
	}

	public Object getExtendedClassProperties(String className) {
		return getInner().getExtendedClassProperties(className);
	}

	public PtcimObject addDirected(String role, PtcimObject pHint) {
		return PtcimObject.create(
				getInner().addDirected(role, pHint.getInner()).queryInterface(IAutomationCaseObject.class));
	}

	public void exportEx(String directory, int bDoSubDirs) {
		getInner().exportEx(directory, bDoSubDirs);
	}

	public void delete() {
		getInner().delete();
	}

	public String displayName(String tagName) {
		return getInner().displayName(tagName);
	}

	public PtcimObject createClone(PtcimObject pCloneThis) {
		return null;
	}

	public void merge(PtcimObject pSrcItem) {
		getInner().merge(pSrcItem.getInner());

	}

	public PtcimObject addByType(String type, String role) {
		return PtcimObject
				.create(getInner().addByType(type, role).queryInterface(IAutomationCaseObject.class));
	}

	public int isConnectedTo(String role, PtcimObject pOtherObject) {
		return getInner().isConnectedTo(role, pOtherObject.getInner());
	}

	private class InnerAutomationCaseObject implements IAutomationCaseObject {

		private IAutomationCaseObject theIaco;

		public InnerAutomationCaseObject(IAutomationCaseObject theIaco) {
			this.theIaco = theIaco;
		}

		public IAutomationCaseObject getTheIaco() {
			return theIaco;
		}

		public void setTheIaco(IAutomationCaseObject theIaco) {
			this.theIaco = theIaco;
		}

		@Override
		public <T> EventCookie advise(Class<T> arg0, T arg1) {
			try {
				return theIaco.advise(arg0, arg1);
			} catch (ComException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}

		@Override
		public void dispose() {
			theIaco.dispose();

		}

		@Override
		public ComThread getComThread() {
			try {
				return theIaco.getComThread();
			} catch (ComException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}

		@Override
		public long getIUnknownPointer() {
			try {
				return theIaco.getIUnknownPointer();
			} catch (ComException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}

		@Override
		public long getPointer() {
			try {
				return theIaco.getPointer();
			} catch (ComException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}

		@Override
		public int getPtr() {
			try {
				return theIaco.getPtr();
			} catch (ComException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}

		@Override
		public <T extends Com4jObject> boolean is(Class<T> arg0) {
			try {
				return theIaco.is(arg0);
			} catch (ComException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				throw e;
			}
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
			try {
				return theIaco.property(type, index);
			} catch (ComException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}

		@Override
		public Com4jObject item(String role, Object index) {
			try {
				return theIaco.item(role, index);
			} catch (ComException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}

		@Override
		public Com4jObject items(String role, Object index) {
			try {
				return theIaco.items(role, index);
			} catch (ComException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}

		@Override
		public Com4jObject add(String role, Object value) {
			try {
				return theIaco.add(role, value);
			} catch (ComException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}

		@Override
		public void remove(String role, Object index) {
			theIaco.remove(role, index);
		}

		@Override
		public Com4jObject link(String role, Object index) {
			try {
				return theIaco.link(role, index);
			} catch (ComException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}

		@Override
		public void resetQueryItems() {
			theIaco.resetQueryItems();
		}

		@Override
		public Com4jObject nextItem() {
			try {
				return theIaco.nextItem();
			} catch (ComException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}

		@Override
		public int moreItems() {
			try {
				return theIaco.moreItems();
			} catch (ComException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}

		@Override
		public void propertySet(Object type, Object index, Object data) {
			theIaco.propertySet(type, index, data);
		}

		@Override
		public Object propertyGet(Object type, Object index) {
			try {
				return theIaco.propertyGet(type, index);
			} catch (ComException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}

		@Override
		public Iterator<Com4jObject> iterator() {
			try {
				return theIaco.iterator();
			} catch (ComException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}

		@Override
		public Com4jObject findObject(String itemId) {
			try {
				return theIaco.findObject(itemId);
			} catch (ComException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}

		@Override
		public Object getClassProperties(String className, Object index) {
			try {
				return theIaco.getClassProperties(className, index);
			} catch (ComException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}

		@Override
		public void checkLicenses(int bCheck) {
			theIaco.checkLicenses(bCheck);
		}

		@Override
		public Com4jObject itemByID(String id) {
			try {
				return theIaco.itemByID(id);
			} catch (ComException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}

		@Override
		public Com4jObject itemEx(String role, Object index, Object searchProperty) {
			try {
				return theIaco.itemEx(role, index, searchProperty);
			} catch (ComException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				throw e;
			}
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
			try {
				return theIaco.itemCount(role);
			} catch (ComException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}

		@Override
		public void reorderItem(String role, Com4jObject pObject, Com4jObject pPredecessorObject,
				Com4jObject pSuccessorObject) {
			theIaco.reorderItem(role, pObject, pPredecessorObject, pSuccessorObject);
		}

		@Override
		public Object getExtendedClassProperties(String className) {
			try {
				return theIaco.getExtendedClassProperties(className);
			} catch (ComException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}

		@Override
		public Com4jObject addDirected(String role, Com4jObject pHint) {
			try {
				return theIaco.addDirected(role, pHint);
			} catch (ComException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				throw e;
			}
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
			try {
				return theIaco.displayName(tagName);
			} catch (ComException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}

		@Override
		public Com4jObject createClone(Com4jObject pCloneThis) {
			try {
				return theIaco.createClone(pCloneThis);
			} catch (ComException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}

		@Override
		public void merge(Com4jObject pSrcItem) {
			theIaco.merge(pSrcItem);
		}

		@Override
		public Com4jObject addByType(String type, String role) {
			try {
				return theIaco.addByType(type, role);
			} catch (ComException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}

		@Override
		public int isConnectedTo(String role, Com4jObject pOtherObject) {
			try {
				return theIaco.isConnectedTo(role, pOtherObject);
			} catch (ComException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}

	}

}
