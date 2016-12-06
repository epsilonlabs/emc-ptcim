package org.eclipse.epsilon.emc.ptcim.jawin;

import org.eclipse.epsilon.eol.exceptions.EolInternalException;
import org.jawin.COMException;
import org.jawin.GUID;
import org.jawin.win32.Ole32;

public class JawinComBridge {
	
	public JawinObject connectByClsId(String clsId) throws EolInternalException {
		GUID ciid = new GUID(clsId);
		try {
			return new JawinObject(ciid);
		} catch (COMException e) {
			throw new EolInternalException(e);
		}
	}

	public JawinObject connectByProgId(String progId) throws EolInternalException {
		try {
			return new JawinObject(progId);
		} catch (COMException e) {
			throw new EolInternalException(e);
		}
	}

	public void initialiseCOM() throws EolInternalException {
		try {
			Ole32.CoInitialize();
		}
		catch (org.jawin.COMException e) {
			throw new EolInternalException(e);
		}
	}

	public void uninitialiseCOM() throws EolInternalException {
		try {
			Ole32.CoUninitialize();
		} catch (COMException e) {
			throw new EolInternalException(e);
		}
	}
}
