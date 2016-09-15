package org.eclipse.epsilon.emc.ptcim.jawin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.epsilon.emc.ptcim.ole.COMBridge;
import org.eclipse.epsilon.emc.ptcim.ole.COMGuid;
import org.eclipse.epsilon.emc.ptcim.ole.COMModel;
import org.eclipse.epsilon.emc.ptcim.ole.COMObject;
import org.eclipse.epsilon.emc.ptcim.ole.EpsilonCOMException;
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
		//if(name.contains("\\")){
		//	method = "Reference";
		//}else{
			method = "Project";
		//}
		List<Object> args = new ArrayList<Object>();
		args.add(method);
		args.add(name); 
		model = (JawinObject) app.invoke("Item", args);
		return model;
	}
	
	@Override
	public JawinObject openModel(COMObject app, String id, String server, String repository, String version) throws EpsilonCOMException {
		String method = "";
		JawinObject model;
		//if(name.contains("\\")){
			method = "Reference";
		//}else{
			method = "Project";
		//}
		List<Object> args = new ArrayList<Object>();
		args.add(method);
		String modelPath = "\\\\Enabler\\" + server + "\\" + repository + "\\" + id;
		if (version.length() > 0) {
			modelPath += "\\" + version;
		}
		args.add(modelPath); 
		model = (JawinObject) app.invoke("Item", args);
		return model;
	}

	@Override
	public void uninitialiseCOM() throws EpsilonCOMException {
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
