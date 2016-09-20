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

/**
 * The Interface IPtcFrameworkFactory.
 */
public interface IPtcFrameworkFactory {
	
	IPtcFileDialog<? extends IPtcObject> getFileDialogManager();
	
	IPtcModelManager<? extends IPtcObject, ? extends IPtcCollection> getModelManager() throws EpsilonCOMException;
	
	IPropertyGetter getPropertyGetter();
	
	IPtcPropertyManager getPropertyManager();
	
	IPropertySetter getPropertySetter();
	
	IPtcUserInterface<? extends IPtcObject> getUIManager() throws EpsilonCOMException;

	void initialiseCOM() throws EpsilonCOMException;

	void uninitialiseCOM() throws EpsilonCOMException;

	void shutdown();

}
