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
package org.eclipse.epsilon.emc.ptcim.jawin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.epsilon.eol.exceptions.EolInternalException;
import org.jawin.COMException;
import org.jawin.COMPtr;
import org.jawin.DispatchPtr;
import org.jawin.GUID;
import org.jawin.Variant;

public class JawinObject extends DispatchPtr {

	private String id;
	
	public JawinObject() { }
	
	public JawinObject(COMPtr comObject) throws COMException {
		super(comObject);
	}

	public JawinObject(GUID clsid) throws COMException {
		super(clsid);
	}

	public JawinObject(String progid) throws COMException {
		super(progid);
	}

	public Object add(String association) throws EolInternalException {
		List<Object> args = new ArrayList<Object>();
		args.add(association);
		Object res = invoke("Add", args);
		return res;
	}
	
	public Object add(String association, JawinObject object) throws EolInternalException {
		List<Object> args = new ArrayList<Object>();
		args.add(association);
		args.add(object);
		Object res = invoke("Add", args);
		return res;
	}
	
	public Object add(String association, String name) throws EolInternalException {
		List<Object> args = new ArrayList<Object>();
		args.add(association);
		args.add(name);
		Object res = invoke("Add", args);
		return res;
	}
	
	public Object addByType(String association, String type) {
		return null;
	}
	
	public void disconnect() throws EolInternalException {
		try {
			super.close();
		} catch (COMException e) {
			throw new EolInternalException(e);
		}
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
		JawinObject other = (JawinObject) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Object getAttribute(String name, List<Object> args) throws EolInternalException {
		Object res;	// = new JawinObject();
		try {
			Object comres = getN(name, args.toArray());
			if (comres instanceof DispatchPtr) {
				res = new JawinObject();
				((JawinObject) res).stealUnknown((DispatchPtr) comres);
			}
			else {
				res = comres; 
			}
		} catch (COMException e) {
			throw new EolInternalException(e);
		}
		return res;
	}

	public Object getAttribute(String name, Object arg) throws EolInternalException {
		Object res;	// = new JawinObject();
		try {
			Object comres = super.get(name, arg);
			if (comres instanceof DispatchPtr) {
				res = new JawinObject();
				((JawinObject) res).stealUnknown((DispatchPtr) comres);
			}
			else {
				res = comres;
			}
		} catch (COMException e) {
			throw new EolInternalException(e);
		}
		return res;
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

	public Object invoke(String methodName, List<Object> args) throws EolInternalException {
		Object res = null;
		try {
			Object comres = invokeN(methodName, args.toArray());
			if (comres instanceof DispatchPtr) {
				if (((DispatchPtr) comres).getUnknown() != 0) {
					res = new JawinObject();
					((JawinObject) res).stealUnknown((DispatchPtr) comres);
				}
			}
			else {
				res = comres;
			}
		} catch (COMException e) {
			throw new EolInternalException(e);
		}
		return res;
	}

	public Object invoke(String methodName, List<Object> args, List<Object> byRefArgs) throws EolInternalException {
		int len = args.size() + byRefArgs.size();
		return invoke(methodName, args, byRefArgs, len);
	}

	public Object invoke(String methodName, List<Object> args, List<Object> byRefArgs, int argsExpected)
			throws EolInternalException {
		Object res;
		try {
			// FIXME is it DispatchPtr?
			// FIXME it seems args is always just one, at least for artisan
			List<Object> comArgs = new ArrayList<Object>();
			for (Object a : args) {
				comArgs.add(a);
			}
			for (Object refA : byRefArgs) {
				Variant.ByrefHolder varIndex = new Variant.ByrefHolder(refA);
				comArgs.add(varIndex);
			}
			Object comres = invokeN(methodName, comArgs.toArray(), argsExpected);
			if (comres instanceof DispatchPtr) {
				res = new JawinObject();
				((JawinObject) res).stealUnknown((DispatchPtr) comres);
			}
			else {
				res = comres;
			}
		} catch (COMException e) {
			throw new EolInternalException(e);
		}
		return res;
	}

	public Object invokeMethod(String methodName) throws EolInternalException {
		Object res;
		try {
			Object comres = invokeN(methodName, new Object[]{});
			if (comres instanceof DispatchPtr) {
				res = new JawinObject();
				((JawinObject) res).stealUnknown((DispatchPtr) comres);
			}
			else {
				res = comres;
			}
		} catch (COMException e) {
			throw new EolInternalException(e);
		}
		return res;
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
	
	public List<JawinObject> wrapInColleciton(JawinObject owner, String association) {
		return new JawinCollection(this, owner, association);
	}
	
	public Collection<JawinObject> wrapInFilteredColleciton(String association) {
		return new JawinFilteredCollection(this, association);
	}
	
}
