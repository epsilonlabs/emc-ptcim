/*******************************************************************************
 * Copyright (c) 2012 The University of York.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
 ******************************************************************************/
package org.eclipse.epsilon.emc.ptcim;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.epsilon.eol.exceptions.EolInternalException;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.epsilon.emc.ptcim";
	
	// The Extension Point ID
	public static final String PTCIM_OLE_EP_ID = "org.eclipse.epsilon.emc.ptcim.ole";
	
	public static final String ATT_CLASS = "class";

	// The shared instance
	private static Activator plugin;

	private PtcimFrameworkFactory factory;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		factory = new PtcimFrameworkFactory();
		try {
			factory.startup();
		} catch (EolInternalException e) {
			throw new IllegalStateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		factory.shutdown();
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	public PtcimFrameworkFactory getFactory() {
		return factory;
	}

}
