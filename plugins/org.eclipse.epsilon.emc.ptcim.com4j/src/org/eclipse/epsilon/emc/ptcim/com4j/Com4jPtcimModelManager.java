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
package org.eclipse.epsilon.emc.ptcim.com4j;

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

public class Com4jPtcimModelManager {

	/**
	 * This is the root object for Modeler. It is a collection object for all
	 * the Project objects you can read, that is, the Models available in the
	 * repositories that are bookmarked in your Model Explorer.
	 */
	private IAutomationCaseObject projects;
	boolean isConnected = false;

	public void connect(boolean fromUI) throws EolInternalException {
		if (!isConnected) {
			System.out.println("Here");
			if (fromUI) {
				isConnected = true;
			}
			else {
				projects = ClassFactory.createCCaseProjects();
				isConnected = true;
			}
			System.out.println("Before: " + isConnected);
			System.out.println("After: " + isConnected);
		}
	}

	public Com4jPtcimCollection getActiveDagrams() throws EolInternalException {
		Com4jPtcimObject comCollection = new Com4jPtcimObject(
				projects.items("Active Diagram", null).queryInterface(IAutomationCaseObject.class));
		return new Com4jPtcimCollection(comCollection, (Com4jPtcimObject) projects, "ActiveDiagram");
	}

	public Com4jPtcimCollection getActiveItems() throws EolInternalException {
		Com4jPtcimObject comCollection = new Com4jPtcimObject(
				projects.items("Active Dictionary Item", null).queryInterface(IAutomationCaseObject.class));
		return new Com4jPtcimCollection(comCollection, (Com4jPtcimObject) projects, "Active Dictionary Item");
	}

	public Com4jPtcimObject getActiveProject() throws EolInternalException {
		return new Com4jPtcimObject(projects.item("Active Project", null).queryInterface(IAutomationCaseObject.class));
	}

	public Com4jPtcimCollection getActiveSelectinContext() throws EolInternalException {
		ArrayList<Object> args = new ArrayList<Object>();
		args.add("Active Selection Context");
		Com4jPtcimObject comCollection = new Com4jPtcimObject(
				projects.items("Active Selection Context", args).queryInterface(IAutomationCaseObject.class));
		return new Com4jPtcimCollection(comCollection, (Com4jPtcimObject) projects, "Active Selection Context");
	}

	public Com4jPtcimCollection getActiveSymbols() throws EolInternalException {
		Com4jPtcimObject comCollection = new Com4jPtcimObject(
				projects.items("Active Symbol", null).queryInterface(IAutomationCaseObject.class));
		return new Com4jPtcimCollection(comCollection, (Com4jPtcimObject) projects, "Active Symbol");
	}

	public Com4jPtcimObject getProjectByReference(String id, String server, String repository, String version)
			throws EolInternalException {
		String method = "";
		IAutomationCaseObject model;
		method = "Reference";
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(method);
		String modelPath = "\\\\Enabler\\" + server + "\\" + repository + "\\" + id;
		if (version.length() > 0) {
			modelPath += "\\" + version;
		}
		args.add(modelPath);
		model = projects.item("Reference", modelPath).queryInterface(IAutomationCaseObject.class);
		return new Com4jPtcimObject(model);
	}

	public Com4jPtcimObject getProjectByTitle(String title) throws EolInternalException {
		return new Com4jPtcimObject(projects.item("Project", title).queryInterface(IAutomationCaseObject.class));
	}

	public Com4jPtcimCollection getProjects() throws EolInternalException {
		Com4jPtcimObject comCollection = new Com4jPtcimObject(
				projects.items("Project", null).queryInterface(IAutomationCaseObject.class));
		return new Com4jPtcimCollection(comCollection, (Com4jPtcimObject) projects, "Project");
	}

	public void disconnect() {
		// TODO Auto-generated method stub

	}
}
