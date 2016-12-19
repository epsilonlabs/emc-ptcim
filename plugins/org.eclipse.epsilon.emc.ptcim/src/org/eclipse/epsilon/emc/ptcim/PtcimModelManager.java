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
package org.eclipse.epsilon.emc.ptcim;

import java.util.ArrayList;

import org.eclipse.epsilon.eol.exceptions.EolInternalException;

/**
 * The Class JawinModelManager.
 */
public class PtcimModelManager  {
	
	/**
	 * This is the root object for Modeler. It is a collection object for all
	 * the Project objects you can read, that is, the Models available in the
	 * repositories that are bookmarked in your Model Explorer. 
	 */
	private PtcimObject projects;
	boolean isConnected = false;

	public void connect(PtcimComBridge bridge) throws EolInternalException {
		if (!isConnected)
			projects = bridge.connectByProgId("OMTE.Projects");
		isConnected = true;
	}

	public void disconnect() throws EolInternalException {
		if (isConnected) {
			projects.disconnect();
			isConnected = false;
		}
	}

	public PtcimCollection getActiveDagrams() throws EolInternalException {
		ArrayList<Object> args = new ArrayList<Object>();
		args.add("Active Diagram");
		PtcimObject comCollection = (PtcimObject) projects.invoke("Items", args);
		return new PtcimCollection(comCollection, projects, "ActiveDiagram");
	}

	public PtcimCollection getActiveItems() throws EolInternalException {
		ArrayList<Object> args = new ArrayList<Object>();
		args.add("Active Dictionary Item");
		PtcimObject comCollection = (PtcimObject) projects.invoke("Items", args);
		return new PtcimCollection(comCollection, projects, "Active Dictionary Item");
	}

	public PtcimObject getActiveProjet() throws EolInternalException {
		ArrayList<Object> args = new ArrayList<Object>();
		args.add("Active Project");
		return (PtcimObject) projects.invoke("Item", args);
	}

	public PtcimCollection getActiveSelectinContext() throws EolInternalException {
		ArrayList<Object> args = new ArrayList<Object>();
		args.add("Active Selection Context");
		PtcimObject comCollection = (PtcimObject) projects.invoke("Items", args);
		return new PtcimCollection(comCollection, projects, "Active Selection Context");
	}

	public PtcimCollection getActiveSymbols() throws EolInternalException {
		ArrayList<Object> args = new ArrayList<Object>();
		args.add("Active Symbol");
		PtcimObject comCollection = (PtcimObject) projects.invoke("Items", args);
		return new PtcimCollection(comCollection, projects, "Active Symbol");
	}

	public PtcimObject getProjectByReference(String id, String server, String repository, String version)
			throws EolInternalException {
		String method = "";
		PtcimObject model;
		method = "Reference";
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(method);
		String modelPath = "\\\\Enabler\\" + server + "\\" + repository + "\\" + id;
		if (version.length() > 0) {
			modelPath += "\\" + version;
		}
		args.add(modelPath); 
		model = (PtcimObject) projects.invoke("Item", args);
		return model;
	}

	public PtcimObject getProjectByTitle(String title) throws EolInternalException {
		ArrayList<Object> args = new ArrayList<Object>();
		args.add("Project");
		args.add(title); 
		return (PtcimObject) projects.invoke("Item", args);
	}

	public PtcimCollection getProjects() throws EolInternalException {
		ArrayList<Object> args = new ArrayList<Object>();
		args.add("Project");
		PtcimObject comCollection = (PtcimObject) projects.invoke("Items", args);
		return new PtcimCollection(comCollection, projects, "Project");
	}
}
