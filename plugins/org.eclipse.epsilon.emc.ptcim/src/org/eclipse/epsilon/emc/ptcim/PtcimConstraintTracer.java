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
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.epsilon.emc.ptcim.jawin.JawinObject;
import org.eclipse.epsilon.emc.ptcim.jawin.JawinUserInterface;
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
public class PtcimConstraintTracer implements IConstraintTracer {
	

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.evl.dt.views.IConstraintTracer#traceConstraint(org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint, org.eclipse.debug.core.ILaunchConfiguration)
	 */
	@Override
	public void traceConstraint(UnsatisfiedConstraint constraint, ILaunchConfiguration configuration) {
		Object instance = constraint.getInstance();
		if (instance instanceof JawinObject) {		// Ignore other objects
			NullProgressMonitor monitor = new NullProgressMonitor();
			EolContext context = new EolContext();
			try {
				EclipseContextManager.setup(context, configuration, monitor, null, true);
			} catch (EolRuntimeException e) {
				e.printStackTrace();
				return;
			}
			JawinUserInterface studio;
			try {
				studio = Activator.getDefault().getFactory().getUIManager();
			} catch (EolInternalException e1) {
				e1.printStackTrace();
				return;
			}
			try {
				showInModeler(instance, context, studio);
			} catch (EolInternalException e) {
				e.printStackTrace();
				return;
			}
			// Free the memory of the newly created context
			context.getModelRepository().dispose();
			context.dispose();
		}
	}

	/**
	 * Show an specific model element in the Artisan Modeler. If the object is associated with a diagram, the first 
	 * diagram in the list of associated diagrams is opened and then a visual object related to the element is selected.
	 * If the element does not have an associated diagram, then we show it in the Packages tree view. 
	 * @param instance
	 * @param context
	 * @param studio
	 * @throws EpsilonCOMException 
	 */
	private void showInModeler(Object instance, EolContext context, JawinUserInterface studio) throws EolInternalException {
		IModel model = context.getModelRepository().getOwningModel(instance);
		assert model instanceof PtcimModel;
		String modelId = ((PtcimModel) model).getModelId();
		String itemId = ((JawinObject) instance).getId();
		JawinObject item = (JawinObject) ((PtcimModel) model).getElementById(itemId);
		studio.showMainWindow();
		studio.openModel(modelId);
		List<Object> args = new ArrayList<Object>();
		args.clear();
		args.add("Using Diagram");
		JawinObject diag;
		diag = (JawinObject) item.invoke("Item", args);
		if (diag != null) {
			String diagId = (String) diag.getAttribute("Property", "Id");
			//String diagId = diag.getId();
			args.clear();
			args.add("Representing Symbol");
			JawinObject objSymbol;
			objSymbol = (JawinObject) item.invoke("Item", args);
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

}
