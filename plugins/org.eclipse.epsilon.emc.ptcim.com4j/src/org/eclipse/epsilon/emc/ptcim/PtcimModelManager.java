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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import org.eclipse.epsilon.eol.exceptions.EolInternalException;
import org.eclipse.epsilon.eol.parse.Eol_EolParserRules.newExpression_return;

import com4j.ComThread;

public class PtcimModelManager {

	/**
	 * This is the root object for Modeler. It is a collection object for all
	 * the Project objects you can read, that is, the Models available in the
	 * repositories that are bookmarked in your Model Explorer.
	 */
	private IAutomationCaseObject projects;
	boolean isConnected = false;

	public void connect(boolean fromUI) throws EolInternalException {
		if (!isConnected) {
			if (fromUI) {
				isConnected = true;
			}
			else {
				projects = ClassFactory.createCCaseProjects();
				isConnected = true;
			}
		}
	}

	public PtcimCollection getActiveDagrams() throws EolInternalException {
		PtcimObject comCollection = new PtcimObject(
				projects.items("Active Diagram", null).queryInterface(IAutomationCaseObject.class));
		return new PtcimCollection(comCollection, (PtcimObject) projects, "ActiveDiagram");
	}

	public PtcimCollection getActiveItems() throws EolInternalException {
		PtcimObject comCollection = new PtcimObject(
				projects.items("Active Dictionary Item", null).queryInterface(IAutomationCaseObject.class));
		return new PtcimCollection(comCollection, (PtcimObject) projects, "Active Dictionary Item");
	}

	public PtcimObject getActiveProject() throws EolInternalException {
		return new PtcimObject(projects.item("Active Project", null).queryInterface(IAutomationCaseObject.class));
	}

	public PtcimCollection getActiveSelectinContext() throws EolInternalException {
		ArrayList<Object> args = new ArrayList<Object>();
		args.add("Active Selection Context");
		PtcimObject comCollection = new PtcimObject(
				projects.items("Active Selection Context", args).queryInterface(IAutomationCaseObject.class));
		return new PtcimCollection(comCollection, (PtcimObject) projects, "Active Selection Context");
	}

	public PtcimCollection getActiveSymbols() throws EolInternalException {
		PtcimObject comCollection = new PtcimObject(
				projects.items("Active Symbol", null).queryInterface(IAutomationCaseObject.class));
		return new PtcimCollection(comCollection, (PtcimObject) projects, "Active Symbol");
	}

	public PtcimObject getProjectByReference(String id, String server, String repository, String version)
			throws EolInternalException {
		String method = "";
		IAutomationCaseObject model;
		method = "Reference";
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

	public PtcimCollection getProjects() throws EolInternalException {
		PtcimObject comCollection = new PtcimObject(
				projects.items("Project", null).queryInterface(IAutomationCaseObject.class));
		return new PtcimCollection(comCollection, (PtcimObject) projects, "Project");
	}

	public void disconnect() {
		// TODO Auto-generated method stub

	}
}
