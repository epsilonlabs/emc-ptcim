package org.eclipse.epsilon.emc.artisan;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.epsilon.emc.COM.COMBridge;
import org.eclipse.epsilon.emc.COM.COMObject;
import org.eclipse.epsilon.emc.COM.EpsilonCOMException;
import org.eclipse.epsilon.eol.dt.launching.EclipseContextManager;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.EolContext;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.evl.dt.views.IConstraintTracer;
import org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint;

public class ArtisanConstraintTracer implements IConstraintTracer {
	
	/** The bridge. */
	COMBridge<COMObject, COMObject> bridge;

	
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
			assert model instanceof ArtisanModel;
			ArtisanModel amodel = (ArtisanModel) model;
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
