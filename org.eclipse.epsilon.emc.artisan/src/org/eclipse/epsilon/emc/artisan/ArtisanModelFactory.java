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
package org.eclipse.epsilon.emc.artisan;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.emc.COM.COMBridge;
import org.eclipse.epsilon.emc.COM.COMObject;
import org.eclipse.epsilon.emc.COM.EpsilonCOMException;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;

/**
 * A factory for creating ArtisanModel objects.
 */
public class ArtisanModelFactory {
	
	
	/** The instance. */
	private static ArtisanModelFactory instance = new ArtisanModelFactory();
	
	/**
	 * Instantiates a new artisan model factory.
	 */
	private ArtisanModelFactory() {}
	
	/**
	 * Gets the single instance of ArtisanModelFactory.
	 *
	 * @return single instance of ArtisanModelFactory
	 */
	public static ArtisanModelFactory getInstance() {
		return instance;
	}
	
	
	/**
	 * Load model.
	 *
	 * @param name the name
	 * @return the artisan model
	 * @throws EolModelLoadingException the eol model loading exception
	 */
	public ArtisanModel loadModel(String name) throws EolModelLoadingException {
		
		ArtisanModel model = new ArtisanModel();
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
	public static void createModel(ArtisanModel model, String server, String repository, String name) throws EolRuntimeException  {
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
