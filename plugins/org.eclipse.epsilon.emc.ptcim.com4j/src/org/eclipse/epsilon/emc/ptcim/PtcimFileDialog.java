package org.eclipse.epsilon.emc.ptcim;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import org.eclipse.epsilon.eol.exceptions.EolInternalException;
import com4j.Holder;

public class PtcimFileDialog extends Observable{
	
	/**
	 * The ArtisanModelFileDialog COM object
	 */
	public static IArtisanModelFileDialog dialog;
	
	boolean isConnected = false;

	/* Com4j is not able to run from main/UI thread. As as result a new Thread should start to execute com4j commands. This creates race conditions
	 * which cannot be solved by any of the known Java solutions (e.g., wait, join, Futures, forks, etc.). For a reason unknown to me the new thread
	 * only executes if the main thread is running as well. As a result we attach an observer to the file dialog, so when the use rhas selected 
	 * a model, it notifies the UI to populate the model configuration with the model details (e.g., server, repository, etc.)
	 */
	public void connect(Observer o) throws EolInternalException {
			this.addObserver(o);
	}

	public void disconnect() throws EolInternalException {
		if (isConnected) {
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
			return (String) "";
					
		} catch (Exception e) {
			throw new EolInternalException(e);
		} finally {
			System.out.println("");
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
