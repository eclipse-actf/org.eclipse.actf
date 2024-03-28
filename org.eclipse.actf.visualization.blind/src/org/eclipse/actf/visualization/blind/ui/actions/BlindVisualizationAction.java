/*******************************************************************************
 * Copyright (c) 2005, 2024 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.blind.ui.actions;

import org.eclipse.actf.ui.util.PlatformUIUtil;
import org.eclipse.actf.visualization.blind.ui.internal.Messages;
import org.eclipse.actf.visualization.blind.ui.internal.PartControlBlind;
import org.eclipse.actf.visualization.engines.blind.BlindVizResourceUtil;
import org.eclipse.actf.visualization.ui.IVisualizationView;
import org.eclipse.jface.action.Action;

public class BlindVisualizationAction extends Action {
	PartControlBlind prb;

	public BlindVisualizationAction(PartControlBlind prb) {
		setToolTipText(Messages.BlindView_Visualize_4); 
		setImageDescriptor(BlindVizResourceUtil
				.getImageDescriptor("icons/etool16/visualize.png")); //$NON-NLS-1$
		setText(Messages.BlindVisualizationAction_0); 
		this.prb = prb;
	}

	public void run() {
		
		PlatformUIUtil.createView(IVisualizationView.SUMMARY_REPORT_VIEW_ID);
		PlatformUIUtil.createView(IVisualizationView.DETAILED_REPROT_VIEW_ID);								

		prb.doVisualize();
	}

}
