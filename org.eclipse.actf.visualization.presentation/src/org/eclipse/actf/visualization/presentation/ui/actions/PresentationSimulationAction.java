/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and Others
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
import org.eclipse.actf.visualization.presentation.ui.internal.PartControlRoom;
import org.eclipse.actf.visualization.presentation.util.ParamRoom;
import org.eclipse.actf.visualization.ui.IVisualizationView;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowPulldownDelegate2;

public class PresentationSimulationAction implements
		IWorkbenchWindowPulldownDelegate2 {

	Menu menu;

	public void dispose() {
		menu.dispose();
	}

	public Menu getMenu(Control arg0) {
		menu = new Menu(arg0);
		ActionContributionItem itemL = new ActionContributionItem(
				new LargeRoomSimulateAction());
		itemL.fill(menu, -1);
		ActionContributionItem itemM = new ActionContributionItem(
				new MiddleRoomSimulateAction());
		itemM.fill(menu, -1);
		ActionContributionItem itemS = new ActionContributionItem(
				new SmallRoomSimulateAction());
		itemS.fill(menu, -1);
		return menu;
	}

	public Menu getMenu(Menu arg0) {
		menu = new Menu(arg0);
		ActionContributionItem itemL = new ActionContributionItem(
				new LargeRoomSimulateAction());
		itemL.fill(menu, -1);
		ActionContributionItem itemM = new ActionContributionItem(
				new MiddleRoomSimulateAction());
		itemM.fill(menu, -1);
		ActionContributionItem itemS = new ActionContributionItem(
				new SmallRoomSimulateAction());
		itemS.fill(menu, -1);
		return menu;
	}

	public void run(IAction arg0) {
		// TODO
		PlatformUIUtil.showView(IVisualizationView.ID_PRESENTATIONVIEW);
		PartControlRoom.getDefaultInstance().getParamLowVision().setType(
				ParamRoom.ROOM_LARGE);
		PartControlRoom.getDefaultInstance().doSimulate();
	}

	public void init(IWorkbenchWindow arg0) {
	}

	public void selectionChanged(IAction arg0, ISelection arg1) {
	}
}
