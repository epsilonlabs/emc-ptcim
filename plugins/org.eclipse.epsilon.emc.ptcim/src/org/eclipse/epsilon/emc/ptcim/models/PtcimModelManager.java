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
package org.eclipse.epsilon.emc.ptcim.models;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.eclipse.epsilon.emc.ptcim.PtcimObject;
import org.eclipse.epsilon.emc.ptcim.operations.contributors.PtcimCollectionOperationContributor;
import org.eclipse.epsilon.emc.ptcim.util.ClassFactory;
import org.eclipse.epsilon.emc.ptcim.util.com4j.IAutomationCaseObject;
import org.eclipse.epsilon.eol.exceptions.EolInternalException;

public class PtcimModelManager {

	/**
	 * This is the root object for Modeler. It is a collection object for all
	 * the Project objects you can read, that is, the Models available in the
	 * repositories that are bookmarked in your Model Explorer.
	 */
	private IAutomationCaseObject projects;
	public IAutomationCaseObject getProjects() {
		return projects;
	}

	public void setProjects(IAutomationCaseObject projects) {
		this.projects = projects;
	}

	boolean isConnected = false;

	public void connect(boolean fromUI) throws EolInternalException {
		if (!isConnected) {
			if (fromUI) {
				isConnected = true;
			} else {
				try {
					projects = ClassFactory.createCCaseProjects();
					isConnected = true;
				} catch (Exception e) {
					Object[] options = {"Close", "Read more..."};
					int n = JOptionPane.showOptionDialog(null,
					    "Running the driver from 64-bit Java needs some registry changes. Click the read more button to read more.",
					    "64-bit compatibility [CCaseObject]",
					    JOptionPane.YES_NO_OPTION,
					    JOptionPane.INFORMATION_MESSAGE,
					    null,     //do not use a custom Icon
					    options,  //the titles of buttons
					    options[0]); //default button title
					if (n == 1) {
						try {
							Desktop.getDesktop().browse(new URL("https://github.com/epsilonlabs/emc-ptcim/blob/master/README.md#running-from-64-bit-java-environments").toURI());
						} catch (MalformedURLException e1) {
							e.printStackTrace();
						} catch (IOException e1) {
							e.printStackTrace();
						} catch (URISyntaxException e1) {
							e.printStackTrace();
						}
					}
					e.printStackTrace();
				}
			}
		}
	}

	public PtcimCollectionOperationContributor getActiveDagrams() throws EolInternalException {
		PtcimObject comCollection = new PtcimObject(
				projects.items("Active Diagram", null).queryInterface(IAutomationCaseObject.class));
		return new PtcimCollectionOperationContributor(comCollection, new PtcimObject(projects), "ActiveDiagram");
	}

	public PtcimCollectionOperationContributor getActiveItems() throws EolInternalException {
		PtcimObject comCollection = new PtcimObject(
				projects.items("Active Dictionary Item", null).queryInterface(IAutomationCaseObject.class));
		return new PtcimCollectionOperationContributor(comCollection, new PtcimObject(projects), "Active Dictionary Item");
	}

	public PtcimObject getActiveProject() throws EolInternalException {
		System.out.println("Projects: " + projects);
		return new PtcimObject(projects.item("Active Project", null).queryInterface(IAutomationCaseObject.class));
	}

	public PtcimCollectionOperationContributor getActiveSelectionContext() throws EolInternalException {
		ArrayList<Object> args = new ArrayList<Object>();
		args.add("Active Selection Context");
		PtcimObject comCollection = new PtcimObject(
				projects.items("Active Selection Context", args).queryInterface(IAutomationCaseObject.class));
		return new PtcimCollectionOperationContributor(comCollection, (PtcimObject) projects, "Active Selection Context");
	}

	public PtcimCollectionOperationContributor getActiveSymbols() throws EolInternalException {
		PtcimObject comCollection = new PtcimObject(
				projects.items("Active Symbol", null).queryInterface(IAutomationCaseObject.class));
		return new PtcimCollectionOperationContributor(comCollection, (PtcimObject) projects, "Active Symbol");
	}

	public PtcimObject getProjectByReference(String id, String server, String repository, String version)
			throws EolInternalException {
		IAutomationCaseObject model;
		String modelPath = "\\\\Enabler\\" + server + "\\" + repository + "\\" + id;
		if (version.length() > 0) {
			modelPath += "\\" + version;
		}
		model = projects.item("Reference", modelPath).queryInterface(IAutomationCaseObject.class);
		PtcimObject theModel = new PtcimObject(model);
		return theModel;
	}

	public PtcimObject getProjectByTitle(String title) throws EolInternalException {
		return new PtcimObject(projects.item("Project", title).queryInterface(IAutomationCaseObject.class));
	}

	public PtcimCollectionOperationContributor getAllProjects() throws EolInternalException {
		PtcimObject comCollection = new PtcimObject(
				projects.items("Project", null).queryInterface(IAutomationCaseObject.class));
		return new PtcimCollectionOperationContributor(comCollection, (PtcimObject) projects, "Project");
	}

	public void disconnect() {
		// TODO do something?

	}
}
