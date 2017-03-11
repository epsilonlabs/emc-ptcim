package org.eclipse.epsilon.emc.ptcim;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.eol.exceptions.EolInternalException;

public class PtcimFileDialog {
	
	/**
	 * The ArtisanModelFileDialog COM object
	 */
	private PtcimObject dialog;
	
	boolean isConnected = false;

	public void connect(PtcimComBridge bridge) throws EolInternalException {
		if (!isConnected)
			dialog = bridge.connectByProgId("COMGUIUtil.ArtisanModelFileDialog");
		isConnected = true;
	}

	public void disconnect() throws EolInternalException {
		if (isConnected) {
			dialog.disconnect();
			isConnected = false;
		}
	}

	public String openDialog() throws EolInternalException {
		try {
			return (String) dialog.invoke("Create", "True");
		} catch (Exception e) {
			throw new EolInternalException(e);
		}
	}

	public String[] openDialogEx() throws EolInternalException {
		String ref = null, id = null, modelName = null;
		List<Object> args = new ArrayList<Object>();
		List<Object> byRefArgs = new ArrayList<Object>();
		byRefArgs.add(ref);
		byRefArgs.add(id);
		byRefArgs.add(modelName);
		try {
			dialog.invoke("CreateEx", args, byRefArgs);
		} catch (EolInternalException e) {
			throw new EolInternalException(e);
		}
		return byRefArgs.toArray(new String[] {});
	}
}
