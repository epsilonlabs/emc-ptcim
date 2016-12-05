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

import org.eclipse.epsilon.emc.ptcim.ole.impl.EpsilonCOMException;

/**
 * A factory for creating JawinFramework objects.
 */
public class JawinFrameworkFactory {
	
	private JawinComBridge bridge = new JawinComBridge();
	private JawinModelManager jawinModelManager = new JawinModelManager();
	private JawinUserInterface jawinUserInterface = new JawinUserInterface();
	private JawinFileDialog jawinFileDialog = new JawinFileDialog();
	
	public JawinFileDialog getFileDialogManager() throws EpsilonCOMException {
		jawinFileDialog.connect(bridge);
		return jawinFileDialog;
	}

	public JawinModelManager getModelManager() throws EpsilonCOMException {
		jawinModelManager.connect(bridge);
		return jawinModelManager;
	}

	public JawinUserInterface getUIManager() throws EpsilonCOMException {
		jawinUserInterface.connect(bridge);
		return jawinUserInterface;
	}

	private void initialiseCOM() throws EpsilonCOMException {
		bridge.initialiseCOM();

	}

	private void uninitialiseCOM() throws EpsilonCOMException {
		bridge.uninitialiseCOM();
	}
	
	public void shutdown() {
		try {
			jawinModelManager.disconnect();
		} catch (EpsilonCOMException e) {
			e.printStackTrace();
		}
		try {
			jawinUserInterface.disconnect();
		} catch (EpsilonCOMException e) {
			e.printStackTrace();
		}
		try {
			jawinFileDialog.disconnect();
		} catch (EpsilonCOMException e1) {
			e1.printStackTrace();
		}
		try {
			uninitialiseCOM();
		} catch (EpsilonCOMException e) {
			e.printStackTrace();
		}
	}

	public JawinPropertyManager getPropertyManager(boolean propertiesValuesCacheEnabled) {
		return (JawinPropertyManager) new JawinCachedPropertyXetter(propertiesValuesCacheEnabled);
	}

	public void startup() throws EpsilonCOMException {
		initialiseCOM();
	}
}