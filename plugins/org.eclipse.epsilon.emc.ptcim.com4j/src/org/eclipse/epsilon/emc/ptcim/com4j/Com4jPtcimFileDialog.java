package org.eclipse.epsilon.emc.ptcim.com4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.epsilon.eol.exceptions.EolInternalException;

import com4j.Holder;

public class Com4jPtcimFileDialog extends Observable{
	
	/**
	 * The ArtisanModelFileDialog COM object
	 */
	public static IArtisanModelFileDialog dialog;
	
	boolean isConnected = false;

	public void connect(Observer o) throws EolInternalException {
		
		this.addObserver(o);

	}

	public void disconnect() throws EolInternalException {
		if (isConnected) {
			//dialog.disconnect();
			isConnected = false;
		}
	}
	
	public String openDialog() throws EolInternalException {
		try {			
			new Thread (new Runnable() {
				
				@Override
				public void run() {
					String ref = dialog.create(true);
					setChanged();
					notifyObservers(ref);
				}
			}).start();
			System.out.println("before dialog");
			return (String) "hhh";
		} catch (Exception e) {
			throw new EolInternalException(e);
		} finally {
			System.out.println("finally");
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
