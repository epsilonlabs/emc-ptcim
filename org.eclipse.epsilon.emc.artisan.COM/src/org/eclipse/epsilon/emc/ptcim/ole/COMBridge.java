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

import java.util.Collection;

/**
 * The Interface COMBridge.
 *
 * @param <S> the generic type
 * @param <T> the generic type
 */
public interface COMBridge<S extends COMObject, T extends COMObject> {
	
	/**
	 * Cast to collection.
	 *
	 * @param obj the obj
	 * @return the collection
	 */
	Collection<T> castToCollection(COMObject obj); 
	
	/**
	 * Connect to COM.
	 *
	 * @param clsid the clsid
	 * @return the t
	 * @throws EpsilonCOMException If an error using the COM connection
	 */
	T connectToCOM(COMGuid clsid) throws EpsilonCOMException;
	
	/**
	 * Connect to COM.
	 *
	 * @param progid the progid
	 * @return the t
	 * @throws EpsilonCOMException If an error using the COM connection
	 */
	T connectToCOM(String progid) throws EpsilonCOMException;

	
	/**
	 * Initialise COM.
	 *
	 * @throws EpsilonCOMException If an error using the COM connection
	 */
	void initialiseCOM() throws EpsilonCOMException;

	/**
	 * Open a model in the Artisan Repository using the Reference attribute of a Model. 
	 * When you use Reference, you can specify the model version or not, in which case
	 * the latest version of the model is used.
	 * Note that the reference string in the format "\\Enabler\&lt;server&gt\&lt;repository&gt\&lt;model&gt\&lt;version&gt"
	 * is passed as individual parameters. 
	 *
	 * @param app the app
	 * @param id the id of the model 
	 * @param server the server
	 * @param repository the repository
	 * @param version the version number
	 * @return the Model reference
	 * @throws EpsilonCOMException If an error using the COM connection
	 */
	T openModel(COMObject app, String id, String server, String repository, String version)
			throws EpsilonCOMException;
	
	/**
	 * Open a model in the Artisan repository by using the Title of the model.
	 * If the Model Explorer has another Model of the same title, the first Model with
	 * a matching title is obtained. 
	 *
	 * @param app the app
	 * @param title the title
	 * @return the Model reference
	 * @throws EpsilonCOMException If an error using the COM connection
	 */
	// FIXME move this to COMApp (create the class)
	T openModel(S app, String title) throws EpsilonCOMException;
	
	/**
	 * Uninitialise COM.
	 *
	 * @throws EpsilonCOMException If an error using the COM connection
	 */
	void uninitialiseCOM() throws EpsilonCOMException;

	/**
	 *  Initialise a new COMModel from the given COMObject result .
	 *
	 * @param res the res
	 * @return the COM model
	 */
	COMModel wrapModel(COMObject res);
	
}
