package org.eclipse.epsilon.emc.ptcim.jawin;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.eol.exceptions.EolInternalException;
import org.jawin.COMException;

public class JawinFileDialog {
	
	/**
	 * The ArtisanModelFileDialog COM object
	 */
	private JawinObject dialog;
	
	boolean isConnected = false;

	public void connect(JawinComBridge bridge) throws EolInternalException {
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
		} catch (COMException e) {
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
