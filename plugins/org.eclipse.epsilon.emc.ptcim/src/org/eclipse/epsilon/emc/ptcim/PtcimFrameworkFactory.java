/*******************************************************************************
 * Copyright (c) 2016 University of York
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Horacio Hoyos - Initial API and implementation
 *******************************************************************************/
package org.eclipse.epsilon.emc.ptcim;

import org.eclipse.epsilon.emc.ptcim.jawin.PtcimComBridge;
import org.eclipse.epsilon.emc.ptcim.jawin.PtcimFileDialog;
import org.eclipse.epsilon.emc.ptcim.jawin.PtcimModelManager;
import org.eclipse.epsilon.emc.ptcim.jawin.PtcimPropertyManager;
import org.eclipse.epsilon.emc.ptcim.jawin.PtcimUserInterface;
import org.eclipse.epsilon.eol.exceptions.EolInternalException;

/**
 * A factory for creating JawinFramework objects.
 */
public class PtcimFrameworkFactory {
	
	private PtcimComBridge bridge = new PtcimComBridge();
	private PtcimModelManager jawinModelManager = new PtcimModelManager();
	private PtcimUserInterface jawinUserInterface = new PtcimUserInterface();
	private PtcimFileDialog jawinFileDialog = new PtcimFileDialog();
	
	public PtcimFileDialog getFileDialogManager() throws EolInternalException {
		jawinFileDialog.connect(bridge);
		return jawinFileDialog;
	}

	public PtcimModelManager getModelManager() throws EolInternalException {
		jawinModelManager.connect(bridge);
		return jawinModelManager;
	}

	public PtcimUserInterface getUIManager() throws EolInternalException {
		jawinUserInterface.connect(bridge);
		return jawinUserInterface;
	}

	private void initialiseCOM() throws EolInternalException {
		bridge.initialiseCOM();

	}

	private void uninitialiseCOM() throws EolInternalException {
		bridge.uninitialiseCOM();
	}
	
	public void shutdown() {
		try {
			jawinModelManager.disconnect();
		} catch (EolInternalException e) {
			e.printStackTrace();
		}
		try {
			jawinUserInterface.disconnect();
		} catch (EolInternalException e) {
			e.printStackTrace();
		}
		try {
			jawinFileDialog.disconnect();
		} catch (EolInternalException e1) {
			e1.printStackTrace();
		}
		try {
			uninitialiseCOM();
		} catch (EolInternalException e) {
			e.printStackTrace();
		}
	}

	public PtcimPropertyManager getPropertyManager() {
		return (PtcimPropertyManager) new PtcimCachedPropertyXetter();
	}

	public void startup() throws EolInternalException {
		initialiseCOM();
	}
}