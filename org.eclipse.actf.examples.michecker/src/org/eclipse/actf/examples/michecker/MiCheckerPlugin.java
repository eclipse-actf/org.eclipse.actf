/*******************************************************************************
 * Copyright (c) 2010, 2011 Ministry of Internal Affairs and Communications (MIC).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Yasuharu GOTOU (MIC) - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.examples.michecker;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.actf.ui.util.AbstractUIPluginACTF;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

public class MiCheckerPlugin extends AbstractUIPluginACTF {
	public static final String PLUGIN_ID = "org.eclipse.actf.examples.michecker"; //$NON-NLS-1$

	private static MiCheckerPlugin plugin;

	private ResourceBundle _resourceBundle;

	public MiCheckerPlugin() {
		plugin = this;
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	public static MiCheckerPlugin getDefault() {
		return plugin;
	}

	public static String getResourceString(String key) {
		ResourceBundle bundle = MiCheckerPlugin.getDefault()
				.getResourceBundle();
		try {
			return (null != bundle) ? bundle.getString(key) : key;
		} catch (MissingResourceException mre) {
			return ""; //$NON-NLS-1$
		}
	}

	public ResourceBundle getResourceBundle() {
		if (null == _resourceBundle) {
			Bundle bundle = getBundle();
			if (null != bundle) {
				_resourceBundle = Platform.getResourceBundle(bundle);
			}
		}

		return _resourceBundle;
	}

}
