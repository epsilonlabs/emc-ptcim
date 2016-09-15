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

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.epsilon.emc.ptcim.ole.COMBridge;
import org.eclipse.epsilon.emc.ptcim.ole.COMObject;
import org.eclipse.epsilon.emc.ptcim.ole.EpsilonCOMException;
import org.eclipse.epsilon.eol.dt.launching.EclipseContextManager;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.EolContext;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.evl.dt.views.IConstraintTracer;
import org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint;

// TODO: Auto-generated Javadoc
/**
 * The Class PtcimConstraintTracer provides the link between an element in the eclipse
 * Epsilon scripts and the PTC Integrity Modeler modeler.
 */
public class PtcimConstraintTracer implements IConstraintTracer {
	
	/** The bridge. */
	COMBridge<COMObject, COMObject> bridge;

	
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.evl.dt.views.IConstraintTracer#traceConstraint(org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint, org.eclipse.debug.core.ILaunchConfiguration)
	 */
	@Override
	public void traceConstraint(UnsatisfiedConstraint constraint, ILaunchConfiguration configuration) {
		Object instance = constraint.getInstance();
		if (instance instanceof COMObject) {		// Ignore other objects
			NullProgressMonitor monitor = new NullProgressMonitor();
			EolContext context = new EolContext();
			try {
				EclipseContextManager.setup(context, configuration, monitor, null, true);
			} catch (EolRuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			IModel model = context.getModelRepository().getOwningModel(instance);
			Object currentInstance = model.getElementById(((COMObject) instance).getId());
			assert model instanceof PtcimModel;
			PtcimModel amodel = (PtcimModel) model;
			try {
				amodel.connectToStudio();
				amodel.showInStudio(currentInstance);
			} catch (EpsilonCOMException e) {
				// FIXME Log
				e.printStackTrace();
			}
			context.getModelRepository().dispose();
		}
	}

}
