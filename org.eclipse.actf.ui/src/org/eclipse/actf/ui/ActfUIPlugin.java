/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.ui;

import org.eclipse.actf.ui.util.timer.Yield;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ActfUIPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String ACTF_PLUGIN_ID = "org.eclipse.actf.ui"; //$NON-NLS-1$

	public static final String ACCESSIBILITY_TOOLS_MENU = "org.eclipse.actf.ui.actfContextMenu"; //$NON-NLS-1$
	
	public static final String ROOT_PREFRENCE_PAGE_ID = "org.eclipse.actf.ui.preferences.RootPreferencePage"; //$NON-NLS-1$

	// The shared instance
	private static ActfUIPlugin plugin;
	
	/**
	 * The constructor
	 */
	public ActfUIPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		Yield.initialize();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static ActfUIPlugin getDefault() {
		return plugin;
	}

}
