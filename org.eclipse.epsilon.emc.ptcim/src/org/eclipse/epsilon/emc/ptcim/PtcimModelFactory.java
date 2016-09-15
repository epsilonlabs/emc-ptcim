/*******************************************************************************
 * Copyright (c) 2016 University of York
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     ${me} - Initial API and implementation
 *******************************************************************************/
package org.eclipse.epsilon.emc.ptcim;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.emc.ptcim.ole.COMBridge;
import org.eclipse.epsilon.emc.ptcim.ole.COMObject;
import org.eclipse.epsilon.emc.ptcim.ole.EpsilonCOMException;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;

/**
 * A factory for creating PtcimModelFactory models in the PTC Integrity
 * Modeler Enabler databse
 */
public class PtcimModelFactory {
	
	
	/** The instance. */
	private static PtcimModelFactory instance = new PtcimModelFactory();
	
	/**
	 * Instantiates a new artisan model factory.
	 */
	private PtcimModelFactory() {}
	
	/**
	 * Gets the single instance of PtcimModelFactory.
	 *
	 * @return single instance of PtcimModelFactory
	 */
	public static PtcimModelFactory getInstance() {
		return instance;
	}
	
	
	/**
	 * Load model.
	 *
	 * @param name the name
	 * @return the artisan model
	 * @throws EolModelLoadingException the eol model loading exception
	 */
	public PtcimModel loadModel(String name) throws EolModelLoadingException {
		
		PtcimModel model = new PtcimModel();
		model.setName(name);
		model.load();
		return model;
	}
	
	/**
	 * Create model. Creates a model in the Enabler repository and assigns the reference to this model as
	 * the Artisan Model's project. 
	 *
	 * @param model the model
	 * @param server A string that specifies the name of the server on which the Repository resides.
	 * 	The case of the name must be correct. 
	 * @param repository A string that specifies the name of the Repository in which you want to
	 * create the model. The case of the name must be correct. 
	 * @param name A string that specifies the name of the model you want to create.
	 * @throws EolRuntimeException the eol runtime exception if there is a problem creating the model
	 */
	public static void createModel(PtcimModel model, String server, String repository, String name) throws EolRuntimeException  {
		assert model.isInitialized();
		COMBridge<COMObject, COMObject> bridge = model.bridge;
		COMObject modelManager;
		try {
			modelManager = bridge.connectToCOM("Studio.ModelManager");
		} catch (EpsilonCOMException e) {
			throw new EolRuntimeException(e.getMessage());
		}
		String fullName = "\\\\Enabler\\" + server + "\\" + repository; 
		List<Object> args = new ArrayList<Object>();
		args.add(fullName);
		args.add(name);
		Object res = null;
		try {
			res = modelManager.invoke("AddModel", args);
		} catch (EpsilonCOMException e) {
			// FIXME This results in a New Model being crated... we have to delete it or rename it or ?
			if (!e.getMessage().contains("already exists")) {
				throw new EolRuntimeException(e.getMessage());
			}
			else {
				throw new EolRuntimeException("The model " + name + " already exits. Perhaps you ment to load the model instead?");
			}
		}
		System.out.println(res);
		COMObject artisanApp;
		try {
			artisanApp = bridge.connectToCOM("OMTE.Projects");
		} catch (EpsilonCOMException e) {
			throw new EolRuntimeException(e.getMessage());
		}
		try {
			model.setTheProject(bridge.openModel(artisanApp, (String) res));
		} catch (EpsilonCOMException e) {
			throw new EolRuntimeException(e.getMessage());
		}
	}
	

}
