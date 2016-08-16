package org.eclipse.epsilon.emc.artisan.jawin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.epsilon.emc.COM.COMBridge;
import org.eclipse.epsilon.emc.COM.COMGuid;
import org.eclipse.epsilon.emc.COM.COMModel;
import org.eclipse.epsilon.emc.COM.COMObject;
import org.eclipse.epsilon.emc.COM.EpsilonCOMException;
import org.jawin.COMException;
import org.jawin.win32.Ole32;

public class JawinComBridge implements COMBridge<COMObject, COMObject> {
	
	
	@Override
	public COMObject connectToCOM(COMGuid clsid) throws EpsilonCOMException {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public COMObject connectToCOM(String progid) throws EpsilonCOMException {
		try {
			// FIXME Why do they use the DIID? Is the the jawin dll GUID after registering?
			// This would need to be a per-user setting... iugh
//			return new JawinObject(progid, DIID);
			return new JawinObject(progid);
		} catch (COMException e) {
			throw new EpsilonCOMException(e);
		}
	}

	@Override
	public COMModel wrapModel(COMObject res) {
		assert res instanceof JawinObject;
		JawinObject jObject = (JawinObject) res;
		return new JawinModel(jObject.getDelegate());
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
	public JawinObject openModel(COMObject app, String name) throws EpsilonCOMException {
		String method = "";
		JawinObject model;
		if(name.contains("\\")){
			method = "Reference";
		}else{
			method = "Project";
		}
		List<Object> args = new ArrayList<Object>();
		args.add(method);
		args.add(name); 
		model = (JawinObject) app.invoke("Item", args);
		return model;
	}

	@Override
	public void uninitializeCOM() throws EpsilonCOMException {
		try {
			Ole32.CoUninitialize();
		} catch (COMException e) {
			throw new EpsilonCOMException(e);
		}
	}

	@Override
	@Deprecated
	public Collection<COMObject> castToCollection(COMObject obj) {
		throw new UnsupportedOperationException("Deprecated");
	}


}
