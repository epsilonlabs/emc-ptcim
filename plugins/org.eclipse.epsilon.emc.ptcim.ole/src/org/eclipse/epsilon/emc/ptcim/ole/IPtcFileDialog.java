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

/**
 * The Interface IPtcFileDialog allows you to open the Modeler Open Model dialog.
 * It has two functions named Create and CreateEx:
 * <ul>
 * <li> The Open function opens the dialog and returns the reference of the
 * 	selected model.
 * <li> The CreateEx function opens the dialog and returns the reference, id and
 * 	name of the selected model.
 * </ul>
 *
 * @param <T> the generic type
 */
public interface IPtcFileDialog<T extends IPtcObject> extends IPtcApp<T> {
	
	/** The model reference index. */
	public final Integer MODEL_REFERENCE_INDEX = 0;
	
	/** The model id index. */
	public final Integer MODEL_ID_INDEX = 1;
	
	/** The model name index. */
	public final Integer MODEL_NAME_INDEX = 2;
	
	/**
	 * Opens the Open Model dialog for you to select a model to open in Modeler, and returns the reference of the
	 * selected model. 
	 *
	 * @return the reference
	 * @throws EpsilonCOMException 
	 */
	String openDialog() throws EpsilonCOMException;
	
	/**
	 * Opens the Open Model dialog for you to select a model to open in Modeler, and returns the reference, id and name
	 * of the selected model. 
	 *
	 * @return the string[]
	 */
	String[] openDialogEx() throws EpsilonCOMException;

}
