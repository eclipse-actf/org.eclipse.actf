/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.blind.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.actf.util.logging.DebugPrintUtil;
import org.eclipse.actf.visualization.blind.IBlindVisualizer;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;

public class BlindVisualizerExtension {

	private static final String VISUALIZER = "blindVisualizer"; //$NON-NLS-1$

	private static final String ATTR_CLASS = "class"; //$NON-NLS-1$

	private static BlindVisualizerExtension[] extensions;

	private static IBlindVisualizer[] visualizers = null;

	public static IBlindVisualizer[] getVisualizers() {
		if (visualizers != null) {
			return visualizers;
		}

		BlindVisualizerExtension[] tmpExtensions = getExtensions();
		ArrayList<IBlindVisualizer> tmpList = new ArrayList<IBlindVisualizer>();
		if (tmpExtensions != null) {
			for (int i = 0; i < tmpExtensions.length; i++) {
				IBlindVisualizer tmpProvider = tmpExtensions[i]
						.getElementViewerInfoProvider();
				if (tmpProvider != null) {
					tmpList.add(tmpProvider);
				}
			}
		}
		visualizers = new IBlindVisualizer[tmpList.size()];
		tmpList.toArray(visualizers);
		return visualizers;
	}

	private static BlindVisualizerExtension[] getExtensions() {
		if (extensions != null)
			return extensions;

		IExtension[] tmpExtensions = Platform.getExtensionRegistry()
				.getExtensionPoint(BlindVizPlugin.PLUGIN_ID, VISUALIZER)
				.getExtensions();

		DebugPrintUtil.devOrDebugPrintln("Blind Visualizer:" //$NON-NLS-1$
				+ tmpExtensions.length);
		List<BlindVisualizerExtension> l = new ArrayList<BlindVisualizerExtension>();
		for (int i = 0; i < tmpExtensions.length; i++) {
			IConfigurationElement[] configElements = tmpExtensions[i]
					.getConfigurationElements();
			for (int j = 0; j < configElements.length; j++) {
				BlindVisualizerExtension ex = parseExtension(configElements[j]);
				if (ex != null) {
					l.add(ex);
				}
			}
		}
		extensions = l.toArray(new BlindVisualizerExtension[l.size()]);
		return extensions;
	}

	private static BlindVisualizerExtension parseExtension(
			IConfigurationElement configElement) {
		if (!configElement.getName().equals(VISUALIZER)) {
			return null;
		}
		try {
			return new BlindVisualizerExtension(configElement);
		} catch (Exception e) {
		}
		return null;
	}

	private IBlindVisualizer visualizer = null;

	private BlindVisualizerExtension(IConfigurationElement configElement) {
		try {
			this.visualizer = (IBlindVisualizer) configElement
					.createExecutableExtension(ATTR_CLASS);
		} catch (Exception e) {
		}
	}

	private IBlindVisualizer getElementViewerInfoProvider() {
		return this.visualizer;
	}

}
