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
package org.eclipse.epsilon.emc.ptcim.ole;

import org.eclipse.epsilon.emc.ptcim.ole.impl.EpsilonCOMException;
import org.eclipse.epsilon.eol.execute.introspection.IPropertyGetter;
import org.eclipse.epsilon.eol.execute.introspection.IPropertySetter;

// TODO: Auto-generated Javadoc
/**
 * The Interface IPtcFrameworkFactory.
 */
public interface IPtcFrameworkFactory {
	
	/**
	 * Gets the file dialog manager.
	 *
	 * @return the file dialog manager
	 */
	IPtcFileDialog<? extends IPtcObject> getFileDialogManager();
	
	/**
	 * Gets the model manager.
	 *
	 * @return the model manager
	 * @throws EpsilonCOMException the epsilon COM exception
	 */
	IPtcModelManager<? extends IPtcObject, ? extends IPtcCollection<? extends IPtcObject>> getModelManager() throws EpsilonCOMException;
	
	/**
	 * Gets the property getter.
	 *
	 * @return the property getter
	 */
	IPropertyGetter getPropertyGetter();
	
	/**
	 * Gets the property manager.
	 *
	 * @return the property manager
	 */
	IPtcPropertyManager getPropertyManager();
	
	/**
	 * Gets the property setter.
	 *
	 * @return the property setter
	 */
	IPropertySetter getPropertySetter();
	
	/**
	 * Gets the UI manager.
	 *
	 * @return the UI manager
	 * @throws EpsilonCOMException the epsilon COM exception
	 */
	IPtcUserInterface<? extends IPtcObject> getUIManager() throws EpsilonCOMException;

	/**
	 * Shutdown.
	 */
	void shutdown() throws EpsilonCOMException;

	/**
	 * Startup.
	 */
	void startup() throws EpsilonCOMException;

}
