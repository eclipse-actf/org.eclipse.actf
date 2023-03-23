/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.actf.util.logging.DebugPrintUtil;
import org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer.IElementViewerInfoProvider;
import org.eclipse.actf.visualization.internal.engines.blind.html.HtmlVizPlugin;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;

public class ElementInfoProviderExtension {

	private static final String PROVIDER = "provider"; //$NON-NLS-1$

	private static final String ATTR_CLASS = "class"; //$NON-NLS-1$

	private static ElementInfoProviderExtension[] extensions;

	private static IElementViewerInfoProvider[] providers = null;

	public static IElementViewerInfoProvider[] getProviders() {
		if (providers != null) {
			return providers;
		}

		ElementInfoProviderExtension[] tmpExtensions = getExtensions();
		ArrayList<IElementViewerInfoProvider> tmpList = new ArrayList<IElementViewerInfoProvider>();
		if (tmpExtensions != null) {
			for (int i = 0; i < tmpExtensions.length; i++) {
				IElementViewerInfoProvider tmpProvider = tmpExtensions[i]
						.getElementViewerInfoProvider();
				if (tmpProvider != null) {
					tmpList.add(tmpProvider);
				}
			}
		}
		providers = new IElementViewerInfoProvider[tmpList.size()];
		tmpList.toArray(providers);
		return providers;
	}

	private static ElementInfoProviderExtension[] getExtensions() {
		if (extensions != null)
			return extensions;

		IExtension[] tmpExtensions = Platform.getExtensionRegistry()
				.getExtensionPoint(HtmlVizPlugin.PLUGIN_ID,
						"elementInfoProvider").getExtensions(); //$NON-NLS-1$

		DebugPrintUtil.devOrDebugPrintln("ElementInfo extensions:" //$NON-NLS-1$
				+ tmpExtensions.length);
		List<ElementInfoProviderExtension> l = new ArrayList<ElementInfoProviderExtension>();
		for (int i = 0; i < tmpExtensions.length; i++) {
			IConfigurationElement[] configElements = tmpExtensions[i]
					.getConfigurationElements();
			for (int j = 0; j < configElements.length; j++) {
				ElementInfoProviderExtension ex = parseExtension(configElements[j]);
				if (ex != null) {
					l.add(ex);
				}
			}
		}
		extensions = l.toArray(new ElementInfoProviderExtension[l.size()]);
		return extensions;
	}

	private static ElementInfoProviderExtension parseExtension(
			IConfigurationElement configElement) {
		if (!configElement.getName().equals(PROVIDER)) {
			return null;
		}
		try {
			return new ElementInfoProviderExtension(configElement);
		} catch (Exception e) {
		}
		return null;
	}

	private IElementViewerInfoProvider provider = null;

	private ElementInfoProviderExtension(IConfigurationElement configElement) {
		try {
			this.provider = (IElementViewerInfoProvider) configElement
					.createExecutableExtension(ATTR_CLASS);
		} catch (Exception e) {
		}
	}

	private IElementViewerInfoProvider getElementViewerInfoProvider() {
		return this.provider;
	}

}
