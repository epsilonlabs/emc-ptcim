package org.eclipse.epsilon.emc.ptcim.jawin;

import org.eclipse.epsilon.emc.ptcim.ole.impl.EpsilonCOMException;
import org.jawin.COMException;
import org.jawin.GUID;
import org.jawin.win32.Ole32;

public class JawinComBridge {
	
	public JawinObject connectByClsId(String clsId) throws EpsilonCOMException {
		GUID ciid = new GUID(clsId);
		try {
			return new JawinObject(ciid);
		} catch (COMException e) {
			throw new EpsilonCOMException(e);
		}
	}

	public JawinObject connectByProgId(String progId) throws EpsilonCOMException {
		try {
			return new JawinObject(progId);
		} catch (COMException e) {
			throw new EpsilonCOMException(e);
		}
	}

	public void initialiseCOM() throws EpsilonCOMException {
		try {
			Ole32.CoInitialize();
		}
		catch (org.jawin.COMException e) {
			throw new EpsilonCOMException(e.getMessage(), e.getCause());
		}
	}

	public void uninitialiseCOM() throws EpsilonCOMException {
		try {
			Ole32.CoUninitialize();
		} catch (COMException e) {
			throw new EpsilonCOMException(e);
		}
	}
}
