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
import java.util.List;

import org.eclipse.epsilon.eol.exceptions.EolInternalException;

/**
 * The Class JawinUserInterface is a Jawin implementation of the interface.
 */
public class PtcimUserInterface {
	
	PtcimObject studio;
	boolean isConnected = false;
	
	public PtcimUserInterface() {
	}
	
	public void connect(PtcimComBridge bridge) throws EolInternalException {
		if (!isConnected)
			studio = bridge.connectByProgId("Studio.Editor");
		isConnected = true;
	}

	public String createModel(String server, String repository, String name) throws EolInternalException {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public void disconnect() throws EolInternalException {
		if (isConnected) {
			studio.disconnect();
			isConnected = false;
		}
	}

	public boolean openDiagram(String id) {
		List<Object> args = new ArrayList<Object>();
		args.add(id);
		Object res = null;
		try {
			res = studio.invoke("OpenDiagram", args);
		} catch (EolInternalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (res == null) {
			return false;
		}
		else {
			// FIXME See how boolean values are returned and wrap in a java boolean
			return true;
		}
	}

	public boolean openModel(String name) {
		List<Object> args = new ArrayList<Object>();
		args.add(name);
		Object res = null;
		try {
			res = studio.invoke("OpenModel", args);
		} catch (EolInternalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (res == null) {
			return false;
		}
		else {
			// FIXME See how boolean values are returned and wrap in a java boolean
			return true;
		}
	}

	public boolean openModelLocation(String name, String directory) {
		List<Object> args = new ArrayList<Object>();
		args.add(name);
		args.add(directory);
		Object res = null;
		try {
			res = studio.invoke("OpenModel2", args);
		} catch (EolInternalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (res == null) {
			return false;
		}
		else {
			// FIXME See how boolean values are returned and wrap in a java boolean
			return true;
		}
	}

	public boolean selectBrowserItem(String itemId, String pane) {
		List<Object> args = new ArrayList<Object>();
		args.add(itemId);
		args.add(pane);
		Object res = null;
		try {
			res = studio.invoke("SelectBrowserItem", args);
		} catch (EolInternalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (res == null) {
			return false;
		}
		else {
			// FIXME See how boolean values are returned and wrap in a java boolean
			return true;
		}
	}

	public boolean selectSymbol(String diagramId, String itemId) {
		List<Object> args = new ArrayList<Object>();
		args.add(diagramId);
		args.add(itemId);
		Object res = null;
		try {
			res = studio.invoke("SelectSymbol2", args);
		} catch (EolInternalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (res == null) {
			return false;
		}
		else {
			// FIXME See how boolean values are returned and wrap in a java boolean
			return true;
		}
	}

	public void setForegroundWindow() {
		try {
			studio.invokeMethod("SetForegroundWindow");
		} catch (EolInternalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void showMainWindow() {
		try {
			studio.invokeMethod("ShowMainWindow");
		} catch (EolInternalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
