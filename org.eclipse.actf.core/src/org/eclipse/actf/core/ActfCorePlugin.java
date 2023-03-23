/*******************************************************************************
 * Copyright (c) 2004, 2023 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.core;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

public class ActfCorePlugin extends Plugin {

	public static final String ACTFCORE_PLUGIN_ID = "org.eclipse.actf.core";

	protected String getPluginId() {
		return ACTFCORE_PLUGIN_ID;
	}

	// The shared instance.
	private static ActfCorePlugin plugin;

	/**
	 * Returns the shared instance.
	 * 
	 * @return plugin
	 */
	public static ActfCorePlugin getDefault() {
		if (plugin == null) {
			plugin = new ActfCorePlugin();
		}
		return plugin;
	}

	/**
	 * The constructor.
	 */
	public ActfCorePlugin() {
		super();
	}

	/**
	 * This method is called upon plug-in activation
	 * 
	 * @param context bundle context
	 * @throws Exception
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	public void stop(BundleContext context) throws Exception {
		super.stop(context);
	}

}