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
package org.eclipse.epsilon.emc.ptcim.jawin;

import org.eclipse.epsilon.eol.exceptions.EolInternalException;

/**
 * A factory for creating JawinFramework objects.
 */
public class JawinFrameworkFactory {
	
	private JawinComBridge bridge = new JawinComBridge();
	private JawinModelManager jawinModelManager = new JawinModelManager();
	private JawinUserInterface jawinUserInterface = new JawinUserInterface();
	private JawinFileDialog jawinFileDialog = new JawinFileDialog();
	
	public JawinFileDialog getFileDialogManager() throws EolInternalException {
		jawinFileDialog.connect(bridge);
		return jawinFileDialog;
	}

	public JawinModelManager getModelManager() throws EolInternalException {
		jawinModelManager.connect(bridge);
		return jawinModelManager;
	}

	public JawinUserInterface getUIManager() throws EolInternalException {
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

	public JawinPropertyManager getPropertyManager(boolean propertiesValuesCacheEnabled) {
		return (JawinPropertyManager) new JawinCachedPropertyXetter(propertiesValuesCacheEnabled);
	}

	public void startup() throws EolInternalException {
		initialiseCOM();
	}
}