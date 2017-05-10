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
package org.eclipse.epsilon.emc.ptcim.dt;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.common.dt.locators.IModelElementLocator;
import org.eclipse.epsilon.emc.ptcim.com4j.Com4jPtcimModel;
import org.eclipse.epsilon.emc.ptcim.com4j.Com4jPtcimObject;
import org.eclipse.epsilon.emc.ptcim.com4j.Com4jPtcimUserInterface;
import org.eclipse.epsilon.eol.models.IModel;

/**
 * The Class PtcimConstraintTracer provides the link between an element in the eclipse
 * Epsilon scripts and the PTC Integrity Modeler modeler.
 */
public class PtcimModelElementLocator implements IModelElementLocator {

	@Override
	public boolean canLocate(Object o) {
		return o instanceof Com4jPtcimObject;
	}

	@Override
	public void locate(Object instance) {
		/*
		try { locateImpl(instance, model); }
		catch (Exception ex) { 
			LogUtil.log(ex);
		}*/
	}
	
	/**
	 * Show an specific model element in the Artisan Modeler. If the object is associated with a diagram, the first 
	 * diagram in the list of associated diagrams is opened and then a visual object related to the element is selected.
	 * If the element does not have an associated diagram, then we show it in the Packages tree view. 
	 */
	protected void locateImpl(Object instance, IModel model) throws Exception {

		Com4jPtcimUserInterface studio = new Com4jPtcimUserInterface();
		String modelId = ((Com4jPtcimModel) model).getModelId();
		Com4jPtcimObject item = (Com4jPtcimObject) instance;
		String itemId =item.getId();
		studio.showMainWindow();
		studio.openModel(modelId);
		List<Object> args = new ArrayList<Object>();
		args.clear();
		args.add("Using Diagram");
		Com4jPtcimObject diag;
		diag = (Com4jPtcimObject) item.item("Using Diagram", null);
		if (diag != null) {
			String diagId = (String) diag.property("Id", null);
			//String diagId = diag.getId();
			args.clear();
			args.add("Representing Symbol");
			Com4jPtcimObject objSymbol;
			objSymbol = (Com4jPtcimObject) item.item("Representing Symbol", null);
			// FIXME What if the objSymbol does not exist? Test for Null
			//String symboldId = objSymbol.getId();
			String symboldId = (String) objSymbol.property("Id", null);
			args.clear();
			args.add(diagId);
			studio.openDiagram(diagId);
			studio.selectSymbol(diagId, symboldId);
		}
		else {		// There is no diagram, use the project tree
			studio.selectBrowserItem(itemId, "Packages");
		}
		studio.setForegroundWindow();
	}

}
