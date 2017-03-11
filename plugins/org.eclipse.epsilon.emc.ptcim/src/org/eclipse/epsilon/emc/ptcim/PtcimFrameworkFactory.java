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

import org.eclipse.epsilon.eol.exceptions.EolInternalException;

public class PtcimFrameworkFactory {
	
	private PtcimComBridge bridge = new PtcimComBridge();
	private PtcimModelManager modelManager = new PtcimModelManager();
	private PtcimUserInterface userInterface = new PtcimUserInterface();
	private PtcimFileDialog fileDialog = new PtcimFileDialog();
	
	public PtcimFileDialog getFileDialogManager() throws EolInternalException {
		fileDialog.connect(bridge);
		return fileDialog;
	}

	public PtcimModelManager getModelManager() throws EolInternalException {
		modelManager.connect(bridge);
		return modelManager;
	}

	public PtcimUserInterface getUIManager() throws EolInternalException {
		userInterface.connect(bridge);
		return userInterface;
	}

	private void initialiseCOM() throws EolInternalException {
		bridge.initialiseCOM();

	}

	private void uninitialiseCOM() throws EolInternalException {
		bridge.uninitialiseCOM();
	}
	
	public void shutdown() {
		try {
			modelManager.disconnect();
		} catch (EolInternalException e) {
			e.printStackTrace();
		}
		try {
			userInterface.disconnect();
		} catch (EolInternalException e) {
			e.printStackTrace();
		}
		try {
			fileDialog.disconnect();
		} catch (EolInternalException e1) {
			e1.printStackTrace();
		}
		try {
			uninitialiseCOM();
		} catch (EolInternalException e) {
			e.printStackTrace();
		}
	}

	public PtcimPropertyManager getPropertyManager(boolean cache) {
		if (cache) {
			return (PtcimCachedPropertyManager) new PtcimCachedPropertyManager();
		} else {
			return (PtcimPropertyManager) new PtcimPropertyManager();
		}
	}

	public void startup() throws EolInternalException {
		initialiseCOM();
	}
}