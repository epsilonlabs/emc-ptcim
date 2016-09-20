package org.eclipse.epsilon.emc.ptcim.jawin;

import org.eclipse.epsilon.emc.ptcim.ole.IPtcComBridge;
import org.eclipse.epsilon.emc.ptcim.ole.impl.EpsilonCOMException;
import org.jawin.COMException;
import org.jawin.GUID;
import org.jawin.win32.Ole32;

public class JawinComBridge implements IPtcComBridge<JawinObject> {

	
	@Override
	public JawinObject connectByClsId(String clsId) throws EpsilonCOMException {
		GUID ciid = new GUID(clsId);
		try {
			return new JawinObject(ciid);
		} catch (COMException e) {
			throw new EpsilonCOMException(e);
		}
	}

	@Override
	public JawinObject connectByProgId(String progId) throws EpsilonCOMException {
		try {
			return new JawinObject(progId);
		} catch (COMException e) {
			throw new EpsilonCOMException(e);
		}
	}

//	
//	@Override
//	public JawinObject openModel(IPtcObject app, String id, String server, String repository, String version) throws EpsilonCOMException {
//		String method = "";
//		JawinObject model;
//		//if(name.contains("\\")){
//			method = "Reference";
//		//}else{
//			method = "Project";
//		//}
//		List<Object> args = new ArrayList<Object>();
//		args.add(method);
//		String modelPath = "\\\\Enabler\\" + server + "\\" + repository + "\\" + id;
//		if (version.length() > 0) {
//			modelPath += "\\" + version;
//		}
//		args.add(modelPath); 
//		model = (JawinObject) app.invoke("Item", args);
//		return model;
//	}

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
	public void uninitialiseCOM() throws EpsilonCOMException {
		try {
			Ole32.CoUninitialize();
		} catch (COMException e) {
			throw new EpsilonCOMException(e);
		}
	}

}
