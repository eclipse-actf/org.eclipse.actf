/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.lowvision.ui.actions;

import org.eclipse.actf.visualization.lowvision.LowVisionVizPlugin;
import org.eclipse.actf.visualization.lowvision.ui.internal.Messages;
import org.eclipse.actf.visualization.lowvision.ui.internal.PartControlLowVision;
import org.eclipse.jface.action.Action;


public class LowVisionSimulateAction extends Action {

	private PartControlLowVision lowVisionCtrl;

	public LowVisionSimulateAction(PartControlLowVision lowVisionCtrl) {
		setToolTipText(Messages.LowVisionView_Simulate_2); 
		setImageDescriptor(LowVisionVizPlugin.imageDescriptorFromPlugin(
				LowVisionVizPlugin.PLUGIN_ID, "icons/etool16/simulation.png")); //$NON-NLS-1$
		setText(Messages.LVSimulationAction_0); 
		this.lowVisionCtrl = lowVisionCtrl;
	}

	public void run() {
		lowVisionCtrl.setLVParamStatus();
		lowVisionCtrl.doSimulate();
	}
}
