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
package org.eclipse.actf.visualization.presentation.ui.actions;

import org.eclipse.actf.ui.util.PlatformUIUtil;
import org.eclipse.actf.visualization.presentation.internal.Messages;
import org.eclipse.actf.visualization.presentation.internal.RoomPlugin;
import org.eclipse.actf.visualization.presentation.ui.internal.PartControlRoom;
import org.eclipse.actf.visualization.presentation.util.ParamRoom;
import org.eclipse.actf.visualization.ui.IVisualizationView;
import org.eclipse.jface.action.Action;

public class MiddleRoomSimulateAction extends Action {

	public MiddleRoomSimulateAction() {
		setToolTipText(Messages.RoomSimulationAction_Middle); 
		setImageDescriptor(RoomPlugin
				.getImageDescriptor("icons/etool16/LargeRoom.gif")); //$NON-NLS-1$
		setText(Messages.RoomSimulationAction_Middle); 
	}

	public void run() {
		PlatformUIUtil.showView(IVisualizationView.ID_PRESENTATIONVIEW);
		// TODO
		PartControlRoom.getDefaultInstance().getParamLowVision().setType(
				ParamRoom.ROOM_MIDDLE);
		PartControlRoom.getDefaultInstance().doSimulate();
	}

}
