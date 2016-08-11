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
import org.eclipse.epsilon.emc.COM.COMModel;
import org.eclipse.epsilon.emc.COM.COMObject;
import org.eclipse.epsilon.emc.COM.EpsilonCOMException;
import org.eclipse.epsilon.emc.artisan.jawin.JawinComBridge;
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
	 * Create model. Creates a model in a repository. Returns the reference of the model
	 * that is created.
	 *
	 * @param server A string that specifies the name of the server on which the Repository resides.
	 * 	The case of the name must be correct. 
	 * @param repository A string that specifies the name of the Repository in which you want to
	 * create the model. The case of the name must be correct. 
	 * @param name A string that specifies the name of the model you want to create.
	 * @return the artisan model 
	 * @throws EolRuntimeException the eol runtime exception if there is a problem creating the model
	 */
	public ArtisanModel createModel(String server, String repository, String name) throws EolRuntimeException  {
		COMBridge<COMObject, COMObject> bridge = new JawinComBridge();
		try {
			bridge.initialiseCOM();
		} catch (EpsilonCOMException e) {
			throw new EolRuntimeException(e.getMessage());
		}
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
		COMObject res = null;
		try {
			res = (COMObject) modelManager.invoke("AddModel", args);
		} catch (EpsilonCOMException e) {
			throw new EolRuntimeException(e.getMessage());
		}
		ArtisanModel model = new ArtisanModel(bridge);
		model.setTheProject(res);
		args.clear();
		args.add("Dictionary");
		COMModel dict;
		try {
			COMObject dictPtr = (COMObject) res.invoke("Item", "Dictionary", args);
			dict = bridge.wrapModel(dictPtr);
		} catch (EpsilonCOMException e) {
			throw new EolModelLoadingException(e, model);
		}
		model.setModel(dict);
		return model;
	}
	

}
