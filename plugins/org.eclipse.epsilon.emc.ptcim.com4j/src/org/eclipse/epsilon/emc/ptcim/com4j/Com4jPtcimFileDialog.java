package org.eclipse.epsilon.emc.ptcim.com4j;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.eol.exceptions.EolInternalException;

import com4j.Holder;

public class Com4jPtcimFileDialog {
	
	/**
	 * The ArtisanModelFileDialog COM object
	 */
	private IArtisanModelFileDialog dialog;
	
	boolean isConnected = false;

	
	public void connect() throws EolInternalException {
		if (!isConnected)
			dialog = ClassFactory.createArtisanModelFileDialog();
		isConnected = true;
	}

	public void disconnect() throws EolInternalException {
		if (isConnected) {
			//dialog.disconnect();
			isConnected = false;
		}
	}
	
	public String openDialog() throws EolInternalException {
		try {
			return (String) dialog.create(true);
		} catch (Exception e) {
			throw new EolInternalException(e);
		}
	}

	public String[] openDialogEx() throws EolInternalException {
		Holder<String> ref = null, id = null, modelName = null;
		List<Object> byRefArgs = new ArrayList<Object>();
		byRefArgs.add(ref);
		byRefArgs.add(id);
		byRefArgs.add(modelName);
		dialog.createEx(true, ref, id, modelName);
		return byRefArgs.toArray(new String[] {});
	}
}
