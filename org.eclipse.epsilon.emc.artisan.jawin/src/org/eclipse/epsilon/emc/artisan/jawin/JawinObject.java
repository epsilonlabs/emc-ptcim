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

public class JawinObject implements COMObject {

	private final DispatchPtr delegate;
	
	public JawinObject() {
		delegate = new DispatchPtr();
	}

	public JawinObject(COMPtr comObject) throws COMException {
		delegate = new DispatchPtr(comObject);
	}

	public JawinObject(DispatchPtr delegate) {
		super();
		this.delegate = delegate;
	}

	public JawinObject(GUID clsid) throws COMException {
		delegate = new DispatchPtr(clsid);
	}

	public JawinObject(String progid) throws COMException {
		delegate = new DispatchPtr(progid);
	}
	
	public DispatchPtr getDelegate() {
		return delegate;
	}
	
	@Override
	public Object get(String name, List<Object> args) throws EpsilonCOMException {
		Object res;	// = new JawinObject();
		try {
			// FIXME is is DispatchPtr?
			//Object o = delegate.getN("Property", new Object[] { name, null });
			Object comres = delegate.getN(name, args.toArray());
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
	
	@Override
	public Object get(String name, Object arg) throws EpsilonCOMException {
		Object res;	// = new JawinObject();
		try {
			// FIXME is is DispatchPtr?
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
	
	@Override
	public JawinObject invoke(String methodName) throws EpsilonCOMException {
		JawinObject res = new JawinObject();
		try {
			// FIXME is it DispatchPtr?
			// FIXME it seems args is always just one, at least for artisan
			Object comres = delegate.invokeN(methodName, new Object[]{});
			if (comres instanceof DispatchPtr) {
				res.stealUnknown((DispatchPtr) comres);
			}
			else {
				res = new JawinPrimitive(comres);
			}
		} catch (COMException e) {
			throw new EpsilonCOMException(e);
		}
		return res;
	}

	@Override
	public JawinObject invoke(String methodName, List<Object> args) throws EpsilonCOMException {
		JawinObject res = new JawinObject();
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
				res.stealUnknown((DispatchPtr) comres);
			}
			else {
				res = new JawinPrimitive(comres);
			}
		} catch (COMException e) {
			throw new EpsilonCOMException(e);
		}
		return res;
	}

	// FIXME this seems to be a invoke with pointer args, not suer if we need a invoke by value
	// in which we wont need the Byref Holder
	@Override
	public JawinObject invoke(String methodName, String type, List<Object> args) throws EpsilonCOMException {
		JawinObject res = new JawinObject();
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
				res.stealUnknown((DispatchPtr) comres);
			}
			else {
				res = new JawinPrimitive(comres);
			}
		} catch (COMException e) {
			throw new EpsilonCOMException(e);
		}
		return res;
	}
	
	@Override
	public JawinObject invoke(String methodName, String type, List<Object> args, int index) throws EpsilonCOMException {
		JawinObject res = new JawinObject();
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
				res.stealUnknown((DispatchPtr) comres);
			}
			else {
				res = new JawinPrimitive(comres);
			}
		} catch (COMException e) {
			throw new EpsilonCOMException(e);
		}
		return res;
	}

	public void stealUnknown(JawinObject dispPtr) {
		delegate.stealUnknown(dispPtr.delegate);
	}
	
	public void stealUnknown(DispatchPtr dispPtr) {
		delegate.stealUnknown(dispPtr);
	}
	
}
