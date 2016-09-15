package org.eclipse.epsilon.emc.ptcim.jawin;

import java.util.List;

import org.eclipse.epsilon.emc.ptcim.ole.COMObject;
import org.eclipse.epsilon.emc.ptcim.ole.EpsilonCOMException;

public class JawinPrimitive  extends JawinObject implements COMObject {
	
	private final Object primitive;
	
	public JawinPrimitive(Object primitive) {
		super();
		this.primitive = primitive;
	}
	
	public Object getPrimitive() {
		return primitive;
	}


	@Override
	public JawinObject get(String attrName, List<Object> args) throws EpsilonCOMException {
		Exception ex = new UnsupportedOperationException("Primitives do not support this method");
		throw new EpsilonCOMException(ex);
	}

	@Override
	public JawinObject invoke(String string) throws EpsilonCOMException {
		Exception ex = new UnsupportedOperationException("Primitives do not support this method");
		throw new EpsilonCOMException(ex);
	}

	@Override
	public JawinObject invoke(String string, List<Object> args) throws EpsilonCOMException {
		Exception ex = new UnsupportedOperationException("Primitives do not support this method");
		throw new EpsilonCOMException(ex);
	}

	@Override
	public JawinObject invoke(String methodName, String type, List<Object> args) throws EpsilonCOMException {
		Exception ex = new UnsupportedOperationException("Primitives do not support this method");
		throw new EpsilonCOMException(ex);
	}

}
