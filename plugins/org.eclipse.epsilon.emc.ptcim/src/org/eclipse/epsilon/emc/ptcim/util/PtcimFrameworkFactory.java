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
package org.eclipse.epsilon.emc.ptcim.util;

import java.util.Observer;

import org.eclipse.epsilon.emc.ptcim.models.PtcimModelManager;
import org.eclipse.epsilon.emc.ptcim.property.manager.PtcimCachedPropertyManager;
import org.eclipse.epsilon.emc.ptcim.property.manager.PtcimPropertyManager;
import org.eclipse.epsilon.eol.exceptions.EolInternalException;

public class PtcimFrameworkFactory {
	
	private PtcimModelManager modelManager = new PtcimModelManager();
	private PtcimUserInterface userInterface = new PtcimUserInterface();
	private PtcimFileDialog fileDialog = new PtcimFileDialog();
	
	public PtcimFileDialog getFileDialogManager(Observer o) throws EolInternalException {
		fileDialog.connect(o);
		return fileDialog;
	}

	public PtcimModelManager getModelManager(boolean fromUI) throws Exception {
		modelManager.connect(fromUI);
		return modelManager;
	}

	/*
	public Com4jPtcimUserInterface getUIManager() throws EolInternalException {
		userInterface.connect(bridge);
		return userInterface;
	}
	*/
	public void shutdown() {
		try {
			fileDialog.disconnect();
		} catch (EolInternalException e1) {
			e1.printStackTrace();
		}
	}

	public PtcimPropertyManager getPropertyManager(boolean cache) {
		if (cache) {
			return (PtcimCachedPropertyManager) new PtcimCachedPropertyManager();
		} else {
			return (PtcimPropertyManager) new PtcimPropertyManager();
		}
	}
}