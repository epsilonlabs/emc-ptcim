package org.eclipse.epsilon.emc.ptcim.jawin;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.emc.ptcim.ole.IPtcComBridge;
import org.eclipse.epsilon.emc.ptcim.ole.IPtcFileDialog;
import org.eclipse.epsilon.emc.ptcim.ole.impl.EpsilonCOMException;
import org.jawin.COMException;

public class JawinFileDialog implements IPtcFileDialog<JawinObject> {
	
	/**
	 * The ArtisanModelFileDialog COM object
	 */
	private JawinObject dialog;
	
	boolean isConnected = false;

	@Override
	public void connect(IPtcComBridge<JawinObject> bridge) throws EpsilonCOMException {
		if (!isConnected)
			dialog = bridge.connectByProgId("COMGUIUtil.ArtisanModelFileDialog");
		isConnected = true;
	}

	@Override
	public void disconnect() throws EpsilonCOMException {
		if (isConnected) {
			dialog.disconnect();
			isConnected = false;
		}
	}

	@Override
	public String openDialog() throws EpsilonCOMException {
		try {
			return (String) dialog.invoke("Create", "True");
		} catch (COMException e) {
			throw new EpsilonCOMException(e);
		}
	}

	@Override
	public String[] openDialogEx() throws EpsilonCOMException {
		String ref = null, id = null, modelName = null;
		List<Object> args = new ArrayList<Object>();
		List<Object> byRefArgs = new ArrayList<Object>();
		byRefArgs.add(ref);
		byRefArgs.add(id);
		byRefArgs.add(modelName);
		try {
			dialog.invoke("CreateEx", args, byRefArgs);
		} catch (EpsilonCOMException e) {
			throw new EpsilonCOMException(e);
		}
		return byRefArgs.toArray(new String[] {});
	}

}
