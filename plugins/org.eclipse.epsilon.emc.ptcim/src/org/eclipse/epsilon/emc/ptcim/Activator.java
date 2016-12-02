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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.epsilon.emc.ptcim.jawin.JawinFrameworkFactory;
import org.eclipse.epsilon.emc.ptcim.ole.impl.EpsilonCOMException;
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

	private JawinFrameworkFactory factory;
	
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
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IExtensionPoint ep = reg.getExtensionPoint(PTCIM_OLE_EP_ID);
		IExtension[] extensions = ep.getExtensions();
		// There should only be one contributor
		IExtension ext = extensions[0];
		IConfigurationElement[] ce = ext.getConfigurationElements();
    	try {
    		factory = (JawinFrameworkFactory) ce[0].createExecutableExtension(ATT_CLASS);
		} catch (CoreException e) {
			throw e;
		}
		try {
			factory.startup();
		} catch (EpsilonCOMException e) {
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

	public JawinFrameworkFactory getFactory() {
		return factory;
	}

}
