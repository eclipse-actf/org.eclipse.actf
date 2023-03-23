/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.engines.blind.ui.actions;

import org.eclipse.actf.ui.util.PlatformUIUtil;
import org.eclipse.actf.visualization.engines.blind.BlindVizResourceUtil;
import org.eclipse.actf.visualization.engines.blind.ParamBlind;
import org.eclipse.actf.visualization.engines.blind.ui.preferences.IBlindPreferenceConstants;
import org.eclipse.actf.visualization.internal.engines.blind.BlindVizEnginePlugin;
import org.eclipse.actf.visualization.internal.engines.blind.Messages;
import org.eclipse.actf.visualization.ui.IVisualizationView;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IViewPart;

public class BlindVisualizationBrowserModeAction extends Action {

	public BlindVisualizationBrowserModeAction() {
		setToolTipText(Messages.VisualizeBrowserModeTooltip);
		setImageDescriptor(BlindVizResourceUtil
				.getImageDescriptor("icons/etool16/wh_16.png")); //$NON-NLS-1$
		setText(Messages.VisualizeBrowserMode);
	}

	public void run() {
		
		IPreferenceStore ps = BlindVizEnginePlugin.getDefault().getPreferenceStore();
		ps.setValue(IBlindPreferenceConstants.BLIND_MODE, IBlindPreferenceConstants.BLIND_BROWSER_MODE);
		ParamBlind.refresh();
		
		IViewPart viewPart = PlatformUIUtil
				.showView(IVisualizationView.ID_BLINDVIEW);
		if (viewPart instanceof IVisualizationView) {
			((IVisualizationView) viewPart).doVisualize();
		}
		ps.setValue(IBlindPreferenceConstants.BLIND_MODE, IBlindPreferenceConstants.BLIND_LAYOUT_MODE);
		ParamBlind.refresh();
	}

}
