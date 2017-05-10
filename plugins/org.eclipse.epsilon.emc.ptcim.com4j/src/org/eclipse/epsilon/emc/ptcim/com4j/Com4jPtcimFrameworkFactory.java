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
package org.eclipse.epsilon.emc.ptcim.com4j;

import org.eclipse.epsilon.eol.exceptions.EolInternalException;

public class Com4jPtcimFrameworkFactory {
	
	private Com4jPtcimModelManager modelManager = new Com4jPtcimModelManager();
	private Com4jPtcimUserInterface userInterface = new Com4jPtcimUserInterface();
	private Com4jPtcimFileDialog fileDialog = new Com4jPtcimFileDialog();
	
	public Com4jPtcimFileDialog getFileDialogManager() throws EolInternalException {
		fileDialog.connect();
		return fileDialog;
	}

	public Com4jPtcimModelManager getModelManager() throws EolInternalException {
		modelManager.connect();
		return modelManager;
	}

	/*
	public Com4jPtcimUserInterface getUIManager() throws EolInternalException {
		userInterface.connect(bridge);
		return userInterface;
	}
	*/
	public void shutdown() {
		/*
		try {
			userInterface.disconnect();
		} catch (EolInternalException e) {
			e.printStackTrace();
		}
		*/
		try {
			fileDialog.disconnect();
		} catch (EolInternalException e1) {
			e1.printStackTrace();
		}
	}

	public Com4jPtcimPropertyManager getPropertyManager(boolean cache) {
		if (cache) {
			return (Com4jPtcimCachedPropertyManager) new Com4jPtcimCachedPropertyManager();
		} else {
			return (Com4jPtcimPropertyManager) new Com4jPtcimPropertyManager();
		}
	}

	public void startup() throws EolInternalException {
		//initialiseCOM();
	}
}