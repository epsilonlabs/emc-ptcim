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

import org.eclipse.epsilon.emc.ptcim.ole.IPtcComBridge;
import org.eclipse.epsilon.emc.ptcim.ole.IPtcModelManager;
import org.eclipse.epsilon.emc.ptcim.ole.impl.EpsilonCOMException;

/**
 * The Class JawinModelManager.
 */
public class JawinModelManager implements IPtcModelManager<JawinObject, JawinCollection> {
	
	private JawinObject projects;
	boolean isConnected = false;

	@Override
	public void connect(IPtcComBridge<JawinObject> bridge) throws EpsilonCOMException {
		if (!isConnected)
			projects = bridge.connectByProgId("OMTE.Projects");
		isConnected = true;
	}

	@Override
	public void disconnect() throws EpsilonCOMException {
		if (isConnected) {
			projects.disconnect();
			isConnected = false;
		}
	}

	@Override
	public JawinCollection getActiveDagrams() throws EpsilonCOMException {
		ArrayList<Object> args = new ArrayList<Object>();
		args.add("Active Diagram");
		JawinObject comCollection = (JawinObject) projects.invoke("Items", args);
		return new JawinCollection(comCollection, projects, "ActiveDiagram");
	}

	@Override
	public JawinCollection getActiveItems() throws EpsilonCOMException {
		ArrayList<Object> args = new ArrayList<Object>();
		args.add("Active Dictionary Item");
		JawinObject comCollection = (JawinObject) projects.invoke("Items", args);
		return new JawinCollection(comCollection, projects, "Active Dictionary Item");
	}

	@Override
	public JawinObject getActiveProjet() throws EpsilonCOMException {
		ArrayList<Object> args = new ArrayList<Object>();
		args.add("Active Project");
		return (JawinObject) projects.invoke("Item", args);
	}

	@Override
	public JawinCollection getActiveSelectinContext() throws EpsilonCOMException {
		ArrayList<Object> args = new ArrayList<Object>();
		args.add("Active Selection Context");
		JawinObject comCollection = (JawinObject) projects.invoke("Items", args);
		return new JawinCollection(comCollection, projects, "Active Selection Context");
	}

	@Override
	public JawinCollection getActiveSymbols() throws EpsilonCOMException {
		ArrayList<Object> args = new ArrayList<Object>();
		args.add("Active Symbol");
		JawinObject comCollection = (JawinObject) projects.invoke("Items", args);
		return new JawinCollection(comCollection, projects, "Active Symbol");
	}

	@Override
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

	@Override
	public JawinObject getProjectByTitle(String title) throws EpsilonCOMException {
		ArrayList<Object> args = new ArrayList<Object>();
		args.add("Project");
		args.add(title); 
		return (JawinObject) projects.invoke("Item", args);
	}

	@Override
	public JawinCollection getProjects() throws EpsilonCOMException {
		ArrayList<Object> args = new ArrayList<Object>();
		args.add("Project");
		JawinObject comCollection = (JawinObject) projects.invoke("Items", args);
		return new JawinCollection(comCollection, projects, "Project");
	}
}
