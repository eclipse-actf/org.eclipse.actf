/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.internal.ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.actf.util.logging.DebugPrintUtil;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;

public class BrowserFavoritesExtension {
	private static final String EXTENSION_NAME = "browserFavorites"; //$NON-NLS-1$

	private static final String FAVORITES = "favorites"; //$NON-NLS-1$

	private static final String ATTR_PREF_FILE = "prefFile"; //$NON-NLS-1$

	private static BrowserFavoritesExtension[] extensions;

	private static URL[] prefFileURLs = null;
	
	public static URL[] getBrowserFavoritesPrefFileURLs() {
		if (prefFileURLs != null) {
			return prefFileURLs;
		}

		BrowserFavoritesExtension[] tmpExtensions = getExtensions();
		ArrayList<URL> tmpList = new ArrayList<URL>();
		if (tmpExtensions != null) {
			for (int i = 0; i < tmpExtensions.length; i++) {
				URL tmpInfo = tmpExtensions[i]
						.getPrefFileURL();
				if (tmpInfo != null) {
					tmpList.add(tmpInfo);
				}
			}
		}
		prefFileURLs = new URL[tmpList.size()];
		tmpList.toArray(prefFileURLs);
		return prefFileURLs;
	}

	private static BrowserFavoritesExtension[] getExtensions() {
		if (extensions != null)
			return extensions;

		IExtension[] tmpExtensions = Platform.getExtensionRegistry()
				.getExtensionPoint(ModelUIPlugin.PLUGIN_ID, EXTENSION_NAME)
				.getExtensions();

		DebugPrintUtil.devOrDebugPrintln(BrowserFavoritesExtension.class.getName()+":" //$NON-NLS-1$
				+ tmpExtensions.length);

		List<BrowserFavoritesExtension> l = new ArrayList<BrowserFavoritesExtension>();
		for (int i = 0; i < tmpExtensions.length; i++) {
			IConfigurationElement[] configElements = tmpExtensions[i]
					.getConfigurationElements();
			for (int j = 0; j < configElements.length; j++) {
				BrowserFavoritesExtension ex = parseExtension(configElements[j]);
				if (ex != null) {
					l.add(ex);
				}
			}
		}
		extensions = l.toArray(new BrowserFavoritesExtension[l.size()]);
		return extensions;
	}

	private static BrowserFavoritesExtension parseExtension(
			IConfigurationElement configElement) {
		if (!configElement.getName().equals(FAVORITES)) {
			return null;
		}
		try {
			return new BrowserFavoritesExtension(configElement);
		} catch (Exception e) {
		}
		return null;
	}

	private URL prefFileUrl = null;

	private BrowserFavoritesExtension(IConfigurationElement configElement) {
		try {
			IContributor contributor = configElement.getContributor();
			String prefFile = configElement.getAttribute(ATTR_PREF_FILE);
			prefFileUrl = FileLocator.resolve(Platform.getBundle(contributor.getName()).getEntry(prefFile));			
		} catch (Exception e) {
			DebugPrintUtil.devOrDebugPrintStackTrace(e);
		}

	}

	private URL getPrefFileURL() {
		return prefFileUrl;
	}

	
}
