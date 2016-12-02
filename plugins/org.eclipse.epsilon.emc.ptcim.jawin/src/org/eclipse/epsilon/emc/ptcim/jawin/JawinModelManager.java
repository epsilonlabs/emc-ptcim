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

import java.util.ArrayList;

import org.eclipse.epsilon.emc.ptcim.ole.impl.EpsilonCOMException;

/**
 * The Class JawinModelManager.
 */
public class JawinModelManager  {
	
	/**
	 * This is the root object for Modeler. It is a collection object for all
	 * the Project objects you can read, that is, the Models available in the
	 * repositories that are bookmarked in your Model Explorer. 
	 */
	private JawinObject projects;
	boolean isConnected = false;

	public void connect(JawinComBridge bridge) throws EpsilonCOMException {
		if (!isConnected)
			projects = bridge.connectByProgId("OMTE.Projects");
		isConnected = true;
	}

	public void disconnect() throws EpsilonCOMException {
		if (isConnected) {
			projects.disconnect();
			isConnected = false;
		}
	}

	public JawinCollection getActiveDagrams() throws EpsilonCOMException {
		ArrayList<Object> args = new ArrayList<Object>();
		args.add("Active Diagram");
		JawinObject comCollection = (JawinObject) projects.invoke("Items", args);
		return new JawinCollection(comCollection, projects, "ActiveDiagram");
	}

	public JawinCollection getActiveItems() throws EpsilonCOMException {
		ArrayList<Object> args = new ArrayList<Object>();
		args.add("Active Dictionary Item");
		JawinObject comCollection = (JawinObject) projects.invoke("Items", args);
		return new JawinCollection(comCollection, projects, "Active Dictionary Item");
	}

	public JawinObject getActiveProjet() throws EpsilonCOMException {
		ArrayList<Object> args = new ArrayList<Object>();
		args.add("Active Project");
		return (JawinObject) projects.invoke("Item", args);
	}

	public JawinCollection getActiveSelectinContext() throws EpsilonCOMException {
		ArrayList<Object> args = new ArrayList<Object>();
		args.add("Active Selection Context");
		JawinObject comCollection = (JawinObject) projects.invoke("Items", args);
		return new JawinCollection(comCollection, projects, "Active Selection Context");
	}

	public JawinCollection getActiveSymbols() throws EpsilonCOMException {
		ArrayList<Object> args = new ArrayList<Object>();
		args.add("Active Symbol");
		JawinObject comCollection = (JawinObject) projects.invoke("Items", args);
		return new JawinCollection(comCollection, projects, "Active Symbol");
	}

	public JawinObject getProjectByReference(String id, String server, String repository, String version)
			throws EpsilonCOMException {
		String method = "";
		JawinObject model;
		method = "Reference";
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(method);
		String modelPath = "\\\\Enabler\\" + server + "\\" + repository + "\\" + id;
		if (version.length() > 0) {
			modelPath += "\\" + version;
		}
		args.add(modelPath); 
		model = (JawinObject) projects.invoke("Item", args);
		return model;
	}

	public JawinObject getProjectByTitle(String title) throws EpsilonCOMException {
		ArrayList<Object> args = new ArrayList<Object>();
		args.add("Project");
		args.add(title); 
		return (JawinObject) projects.invoke("Item", args);
	}

	public JawinCollection getProjects() throws EpsilonCOMException {
		ArrayList<Object> args = new ArrayList<Object>();
		args.add("Project");
		JawinObject comCollection = (JawinObject) projects.invoke("Items", args);
		return new JawinCollection(comCollection, projects, "Project");
	}
}
