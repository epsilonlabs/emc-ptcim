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

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.epsilon.common.dt.locators.IModelElementLocator;
import org.eclipse.epsilon.emc.ptcim.PtcimModel;
import org.eclipse.epsilon.emc.ptcim.PtcimObject;
import org.eclipse.epsilon.emc.ptcim.PtcimUserInterface;
import org.eclipse.epsilon.eol.dt.launching.EclipseContextManager;
import org.eclipse.epsilon.eol.exceptions.EolInternalException;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.EolContext;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.evl.dt.views.IConstraintTracer;
import org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint;

/**
 * The Class PtcimConstraintTracer provides the link between an element in the eclipse
 * Epsilon scripts and the PTC Integrity Modeler modeler.
 */
public class PtcimModelElementLocator implements IModelElementLocator {

	/**
	 * Show an specific model element in the Artisan Modeler. If the object is associated with a diagram, the first 
	 * diagram in the list of associated diagrams is opened and then a visual object related to the element is selected.
	 * If the element does not have an associated diagram, then we show it in the Packages tree view. 
	 * @param instance
	 * @param context
	 * @param studio
	 * @throws EpsilonCOMException 
	 */
	private void showInModeler(PtcimObject item, EolContext context, PtcimUserInterface studio) throws EolInternalException {
		//IModel model = context.getModelRepository().getOwningModel(instance);
		//assert model instanceof PtcimModel;
		//String modelId = ((PtcimModel) model).getModelId();
		String itemId = ((PtcimObject) item).getId();
		//PtcimObject item = (PtcimObject) ((PtcimModel) model).getElementById(itemId);
		studio.showMainWindow();
		//studio.openModel(modelId);
		List<Object> args = new ArrayList<Object>();
		args.clear();
		args.add("Using Diagram");
		PtcimObject diag;
		diag = (PtcimObject) item.invoke("Item", args);
		if (diag != null) {
			String diagId = (String) diag.getAttribute("Property", "Id");
			//String diagId = diag.getId();
			args.clear();
			args.add("Representing Symbol");
			PtcimObject objSymbol;
			objSymbol = (PtcimObject) item.invoke("Item", args);
			// FIXME What if the objSymbol does not exist? Test for Null
			//String symboldId = objSymbol.getId();
			String symboldId = (String) objSymbol.getAttribute("Property", "Id");
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

	@Override
	public boolean canLocate(Object o) {
		return o instanceof PtcimObject;
	}

	@Override
	public void locate(Object instance) {
		NullProgressMonitor monitor = new NullProgressMonitor();
		EolContext context = new EolContext();
		//try {
			//EclipseContextManager.setup(context, configuration, monitor, null, true);
		//} catch (EolRuntimeException e) {
		//	e.printStackTrace();
		//	return;
		//}
		PtcimUserInterface studio = new PtcimUserInterface();
		
		try {
			showInModeler((PtcimObject) instance, context, studio);
		} catch (EolInternalException e) {
			e.printStackTrace();
			return;
		}
		// Free the memory of the newly created context
		context.getModelRepository().dispose();
		context.dispose();
	}

}
