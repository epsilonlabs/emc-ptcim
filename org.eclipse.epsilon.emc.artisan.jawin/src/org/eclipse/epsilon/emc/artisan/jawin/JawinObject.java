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
package org.eclipse.epsilon.emc.artisan.jawin;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.emc.COM.COMObject;
import org.eclipse.epsilon.emc.COM.EpsilonCOMException;
import org.jawin.COMException;
import org.jawin.COMPtr;
import org.jawin.DispatchPtr;
import org.jawin.GUID;
import org.jawin.Variant;

/**
 * The Class JawinObject.
 */
public class JawinObject implements COMObject {

	/** The delegate. */
	private final DispatchPtr delegate;
	
	/** The id. */
	private String id;
	
	/**
	 * Instantiates a new jawin object.
	 */
	public JawinObject() {
		delegate = new DispatchPtr();
	}

	/**
	 * Instantiates a new jawin object.
	 *
	 * @param comObject the com object
	 * @throws COMException the COM exception
	 */
	public JawinObject(COMPtr comObject) throws COMException {
		delegate = new DispatchPtr(comObject);
	}

	/**
	 * Instantiates a new jawin object.
	 *
	 * @param delegate the delegate
	 */
	public JawinObject(DispatchPtr delegate) {
		super();
		this.delegate = delegate;
	}

	/**
	 * Instantiates a new jawin object.
	 *
	 * @param clsid the clsid
	 * @throws COMException the COM exception
	 */
	public JawinObject(GUID clsid) throws COMException {
		delegate = new DispatchPtr(clsid);
	}

	/**
	 * Instantiates a new jawin object.
	 *
	 * @param progid the progid
	 * @throws COMException the COM exception
	 */
	public JawinObject(String progid) throws COMException {
		delegate = new DispatchPtr(progid);
	}
	
	/**
	 * Gets the delegate.
	 *
	 * @return the delegate
	 */
	public DispatchPtr getDelegate() {
		return delegate;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.COM.COMObject#get(java.lang.String, java.util.List)
	 */
	@Override
	public Object get(String name, List<Object> args) throws EpsilonCOMException {
		Object res;	// = new JawinObject();
		try {
			//Object o = delegate.getN("Property", new Object[] { name, null });
			Object comres = delegate.getN(name, args.toArray());
			if (comres instanceof DispatchPtr) {
				res = new JawinObject();
				((JawinObject) res).stealUnknown((DispatchPtr) comres);
			}
			else {
				res = comres; 
			}
		} catch (COMException e) {
			throw new EpsilonCOMException(e);
		}
		return res;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.COM.COMObject#get(java.lang.String, java.lang.Object)
	 */
	@Override
	public Object get(String name, Object arg) throws EpsilonCOMException {
		Object res;	// = new JawinObject();
		try {
			//Object o = delegate.getN("Property", new Object[] { name, null });
			Object comres = delegate.get(name, arg);
			if (comres instanceof DispatchPtr) {
				res = new JawinObject();
				((JawinObject) res).stealUnknown((DispatchPtr) comres);
			}
			else {
				//res = new JawinPrimitive(comres);
				res = comres;
			}
		} catch (COMException e) {
			throw new EpsilonCOMException(e);
		}
		return res;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.COM.COMObject#invoke(java.lang.String)
	 */
	@Override
	public Object invoke(String methodName) throws EpsilonCOMException {
		Object res;
		try {
			// FIXME is it DispatchPtr?
			// FIXME it seems args is always just one, at least for artisan
			Object comres = delegate.invokeN(methodName, new Object[]{});
			if (comres instanceof DispatchPtr) {
				res = new JawinObject();
				((JawinObject) res).stealUnknown((DispatchPtr) comres);
			}
			else {
				//res = new JawinPrimitive(comres);
				res = comres;
			}
		} catch (COMException e) {
			throw new EpsilonCOMException(e);
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.COM.COMObject#invoke(java.lang.String, java.util.List)
	 */
	@Override
	// FIXME We should return Object so primitives are handled easily
	public Object invoke(String methodName, List<Object> args) throws EpsilonCOMException {
		Object res = null;
		try {
			// FIXME is it DispatchPtr?
			// FIXME it seems args is always just one, at least for artisan
			List<Object> comArgs = new ArrayList<Object>();
			for (Object arg : args) {
				Variant.ByrefHolder varIndex = new Variant.ByrefHolder(arg);
				comArgs.add(varIndex);
			}
			// An invocation can return a pointer or a Primitive type
			Object comres = delegate.invokeN(methodName, comArgs.toArray());
			if (comres instanceof DispatchPtr) {
				res = new JawinObject();
				((JawinObject) res).stealUnknown((DispatchPtr) comres);
			}
			else {
				//res = new JawinPrimitive(comres);
				res = comres;
			}
		} catch (COMException e) {
			throw new EpsilonCOMException(e);
		}
		return res;
	}

	// FIXME this seems to be a invoke with pointer args, not suer if we need a invoke by value
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.COM.COMObject#invoke(java.lang.String, java.lang.String, java.util.List)
	 */
	// in which we wont need the Byref Holder
	@Override
	public Object invoke(String methodName, String type, List<Object> args) throws EpsilonCOMException {
		Object res;
		try {
			// FIXME is it DispatchPtr?
			// FIXME it seems args is always just one, at least for artisan
			List<Object> comArgs = new ArrayList<Object>();
			comArgs.add(type);
			for (Object arg : args) {
				Variant.ByrefHolder varIndex = new Variant.ByrefHolder(arg);
				comArgs.add(varIndex);
			}
			Object comres = delegate.invokeN(methodName, comArgs.toArray());
			if (comres instanceof DispatchPtr) {
				res = new JawinObject();
				((JawinObject) res).stealUnknown((DispatchPtr) comres);
			}
			else {
				//res = new JawinPrimitive(comres);
				res = comres;
			}
		} catch (COMException e) {
			throw new EpsilonCOMException(e);
		}
		return res;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.COM.COMObject#invoke(java.lang.String, java.lang.String, java.util.List, int)
	 */
	@Override
	public Object invoke(String methodName, String type, List<Object> args, int index) throws EpsilonCOMException {
		Object res;
		try {
			// FIXME is it DispatchPtr?
			// FIXME it seems args is always just one, at least for artisan
			List<Object> comArgs = new ArrayList<Object>();
			comArgs.add(type);
			for (Object arg : args) {
				Variant.ByrefHolder varIndex = new Variant.ByrefHolder(arg);
				comArgs.add(varIndex);
			}
			Object comres = delegate.invokeN(methodName, comArgs.toArray(), index);
			if (comres instanceof DispatchPtr) {
				res = new JawinObject();
				((JawinObject) res).stealUnknown((DispatchPtr) comres);
			}
			else {
				//res = new JawinPrimitive(comres);
				res = comres;
			}
		} catch (COMException e) {
			throw new EpsilonCOMException(e);
		}
		return res;
	}

	/**
	 * Steal unknown.
	 *
	 * @param dispPtr the disp ptr
	 */
	public void stealUnknown(JawinObject dispPtr) {
		delegate.stealUnknown(dispPtr.delegate);
	}
	
	/**
	 * Steal unknown.
	 *
	 * @param dispPtr the disp ptr
	 */
	public void stealUnknown(DispatchPtr dispPtr) {
		delegate.stealUnknown(dispPtr);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.COM.COMObject#getId()
	 */
	@Override
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.COM.COMObject#setId(java.lang.String)
	 */
	@Override
	public void setId(String id) {
		// We should only set it once
		if (this.id != null) {
			throw new IllegalStateException("The id should only be set once");
		}
		this.id = id;
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

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.COM.COMObject#add(java.lang.String)
	 */
	@Override
	public Object add(String association) throws EpsilonCOMException {
		List<Object> args = new ArrayList<Object>();
		args.add(association);
		Object res = invoke("Add", args);
		return res;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.COM.COMObject#add(java.lang.String, org.eclipse.epsilon.emc.COM.COMObject)
	 */
	@Override
	public Object add(String association, COMObject object) throws EpsilonCOMException {
		List<Object> args = new ArrayList<Object>();
		args.add(association);
		args.add(object);
		Object res = invoke("Add", args);
		return res;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.COM.COMObject#add(java.lang.String, java.lang.String)
	 */
	@Override
	public Object add(String association, String name) throws EpsilonCOMException {
		List<Object> args = new ArrayList<Object>();
		args.add(association);
		args.add(name);
		Object res = invoke("Add", args);
		return res;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.COM.COMObject#addByType(java.lang.String, java.lang.String)
	 */
	@Override
	public Object addByType(String association, String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		if (id != null) {
			return id;
		}
		else {
			return super.toString();
		}
	}
	
	
	
}
