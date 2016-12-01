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

import org.eclipse.epsilon.emc.ptcim.ole.IPtcFileDialog;
import org.eclipse.epsilon.emc.ptcim.ole.IPtcFrameworkFactory;
import org.eclipse.epsilon.emc.ptcim.ole.IPtcModelManager;
import org.eclipse.epsilon.emc.ptcim.ole.IPtcPropertyManager;
import org.eclipse.epsilon.emc.ptcim.ole.IPtcUserInterface;
import org.eclipse.epsilon.emc.ptcim.ole.impl.EpsilonCOMException;
import org.eclipse.epsilon.eol.execute.introspection.IPropertyGetter;
import org.eclipse.epsilon.eol.execute.introspection.IPropertySetter;

/**
 * A factory for creating JawinFramework objects.
 */
public class JawinFrameworkFactory implements IPtcFrameworkFactory {
	
	private JawinComBridge bridge = new JawinComBridge();
	private JawinModelManager jawinModelManager = new JawinModelManager();
	private JawinPropertyGetter jawinPropertyGetter = new JawinPropertyGetter();
	private JawinPropertySetter jawinPropertySetter = new JawinPropertySetter();
	private JawinUserInterface jawinUserInterface = new JawinUserInterface();
	private JawinPropertyManager jawinPropertyManager = new JawinPropertyManager();
	private JawinFileDialog jawinFileDialog = new JawinFileDialog();
	
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.ptcim.ole.IPtcFrameworkFactory#getFileDialogManager()
	 */
	@Override
	public IPtcFileDialog<JawinObject> getFileDialogManager() throws EpsilonCOMException {
		jawinFileDialog.connect(bridge);
		return jawinFileDialog;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.ptcim.ole.IPtcFrameworkFactory#getModelManager()
	 */
	@Override
	public IPtcModelManager<JawinObject, JawinCollection> getModelManager() throws EpsilonCOMException {
		jawinModelManager.connect(bridge);
		return jawinModelManager;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.ptcim.ole.IPtcFrameworkFactory#getPropertyGetter()
	 */
	@Override
	public IPropertyGetter getPropertyGetter() {
		return jawinPropertyGetter;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.ptcim.ole.IPtcFrameworkFactory#getPropertySetter()
	 */
	@Override
	public IPropertySetter getPropertySetter() {
		return jawinPropertySetter;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.ptcim.ole.IPtcFrameworkFactory#getUIManager()
	 */
	@Override
	public IPtcUserInterface<JawinObject> getUIManager() throws EpsilonCOMException {
		jawinUserInterface.connect(bridge);
		return jawinUserInterface;
	}

	private void initialiseCOM() throws EpsilonCOMException {
		bridge.initialiseCOM();

	}

	private void uninitialiseCOM() throws EpsilonCOMException {
		bridge.uninitialiseCOM();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.ptcim.ole.IPtcFrameworkFactory#shutdown()
	 */
	@Override
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

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.ptcim.ole.IPtcFrameworkFactory#getPropertyManager()
	 */
	@Override
	public IPtcPropertyManager getPropertyManager() {
		return (IPtcPropertyManager) new JawinCachedPropertyXetter();
	}

	@Override
	public void startup() throws EpsilonCOMException {
		initialiseCOM();
	}
}