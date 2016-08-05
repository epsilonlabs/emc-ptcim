package org.eclipse.epsilon.emc.artisan.jawin;

import org.eclipse.epsilon.emc.COM.COMBridge;
import org.eclipse.epsilon.emc.COM.COMGuid;
import org.eclipse.epsilon.emc.COM.EpsilonCOMException;
import org.jawin.COMException;
import org.jawin.DispatchPtr;
import org.jawin.win32.Ole32;

public class JawinComBridge implements COMBridge<JawinObject, JawinObject> {

	@Override
	public JawinObject invoke(JawinObject source, String name, Object... args) {
		try {
			// FIXME is is DispatchPtr?
			return (JawinObject) source.invokeN(name, args);
		} catch (COMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return source;
	}

	@Override
	public JawinObject get(JawinObject source, String name, Object... args) {
		try {
			// FIXME is is DispatchPtr?
			return (JawinObject) source.get(name, args);
		} catch (COMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return source;
	}

	@Override
	public JawinObject connectToCOM(String progid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JawinObject connectToCOM(COMGuid clsid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initialiseCOM() throws EpsilonCOMException {
		try {
			Ole32.CoInitialize();
		}
		catch (org.jawin.COMException e) {
			throw new EpsilonCOMException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public void uninitializeCOM() throws EpsilonCOMException {
		try {
			Ole32.CoUninitialize();
		} catch (COMException e) {
			// TODO Auto-generated catch block
			throw new EpsilonCOMException(e.getMessage(), e.getCause());
		}
	}

}
