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

import org.eclipse.epsilon.emc.ptcim.ole.IPtcComBridge;
import org.eclipse.epsilon.emc.ptcim.ole.IPtcObject;
import org.eclipse.epsilon.emc.ptcim.ole.IPtcUserInterface;
import org.eclipse.epsilon.emc.ptcim.ole.impl.EpsilonCOMException;

/**
 * The Class JawinUserInterface is a Jawin implementation of the interface.
 */
public class JawinUserInterface implements IPtcUserInterface<JawinObject> {
	
	/** The studio. */
	IPtcObject studio;
	
	boolean isConnected = false;
	
	/**
	 * Instantiates a new jawin user interface.
	 */
	public JawinUserInterface() {
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.ptcim.ole.IPtcApp#connect(org.eclipse.epsilon.emc.ptcim.ole.IPtcComBridge)
	 */
	@Override
	public void connect(IPtcComBridge<JawinObject> bridge) throws EpsilonCOMException {
		if (!isConnected)
			studio = bridge.connectByProgId("Studio.ModelManager");
		isConnected = true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.ptcim.ole.IPtcUserInterface#createModel(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String createModel(String server, String repository, String name) throws EpsilonCOMException {
//		String fullName = "\\\\Enabler\\" + server + "\\" + repository; 
//		List<Object> args = new ArrayList<Object>();
//		args.add(fullName);
//		args.add(name);
//		Object res = null;
//		try {
//			res = manager.invoke("AddModel", args);
//		} catch (EpsilonCOMException e) {
//			// FIXME This results in a New Model being crated... we have to delete it or rename it or ?
//			if (!e.getMessage().contains("already exists")) {
//				throw new EolRuntimeException(e.getMessage());
//			}
//			else {
//				throw new EolRuntimeException("The model " + name + " already exits. Perhaps you ment to load the model instead?");
//			}
//		}
//		System.out.println(res);
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.ptcim.ole.IPtcApp#disconnect()
	 */
	@Override
	public void disconnect() throws EpsilonCOMException {
		if (isConnected) {
			studio.disconnect();
			isConnected = false;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.ptcim.ole.IPtcUserInterface#openDiagram(java.lang.String)
	 */
	@Override
	public boolean openDiagram(String id) {
		List<Object> args = new ArrayList<Object>();
		args.add(id);
		Object res = null;
		try {
			res = studio.invoke("OpenDiagram", args);
		} catch (EpsilonCOMException e) {
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

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.ptcim.ole.IPtcUserInterface#openModel(java.lang.String)
	 */
	@Override
	public boolean openModel(String name) {
		List<Object> args = new ArrayList<Object>();
		args.add(name);
		Object res = null;
		try {
			res = studio.invoke("OpenModel", args);
		} catch (EpsilonCOMException e) {
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

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.ptcim.ole.IPtcUserInterface#openModelLocation(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean openModelLocation(String name, String directory) {
		List<Object> args = new ArrayList<Object>();
		args.add(name);
		args.add(directory);
		Object res = null;
		try {
			res = studio.invoke("OpenModel2", args);
		} catch (EpsilonCOMException e) {
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

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.ptcim.ole.IPtcUserInterface#selectBrowserItem(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean selectBrowserItem(String itemId, String pane) {
		List<Object> args = new ArrayList<Object>();
		args.add(itemId);
		args.add(pane);
		Object res = null;
		try {
			res = studio.invoke("SelectBrowserItem", args);
		} catch (EpsilonCOMException e) {
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

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.ptcim.ole.IPtcUserInterface#selectSymbol(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean selectSymbol(String diagramId, String itemId) {
		List<Object> args = new ArrayList<Object>();
		args.add(diagramId);
		args.add(itemId);
		Object res = null;
		try {
			res = studio.invoke("SelectSymbol2", args);
		} catch (EpsilonCOMException e) {
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

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.ptcim.ole.IPtcUserInterface#setForegroundWindow()
	 */
	@Override
	public void setForegroundWindow() {
		try {
			studio.invokeMethod("SetForegroundWindow");
		} catch (EpsilonCOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.emc.ptcim.ole.IPtcUserInterface#showMainWindow()
	 */
	@Override
	public void showMainWindow() {
		try {
			studio.invokeMethod("ShowMainWindow");
		} catch (EpsilonCOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
