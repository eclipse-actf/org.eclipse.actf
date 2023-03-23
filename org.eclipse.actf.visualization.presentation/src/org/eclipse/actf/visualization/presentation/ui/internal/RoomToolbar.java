/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Daisuke SATO - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.presentation.ui.internal;

import org.eclipse.actf.visualization.presentation.ui.actions.LargeRoomSimulateAction;
import org.eclipse.actf.visualization.presentation.ui.actions.MiddleRoomSimulateAction;
import org.eclipse.actf.visualization.presentation.ui.actions.SmallRoomSimulateAction;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;


public class RoomToolbar extends Composite {

	public RoomToolbar(Composite parent, int style) {
		super(parent, style);
		initLayout(parent);
	}

	private void initLayout(Composite parent) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginTop = 0;
		gridLayout.marginBottom = 1;
		gridLayout.marginHeight = gridLayout.marginWidth = 1;
		gridLayout.numColumns = 3;
		setLayout(gridLayout);
		
		ToolBar toolBar = new ToolBar(this, SWT.RIGHT);
		ToolBarManager toolBarManager = new ToolBarManager(toolBar);

		ActionContributionItem smallRoomActionItem = new ActionContributionItem(new SmallRoomSimulateAction());
		smallRoomActionItem.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		toolBarManager.add(smallRoomActionItem);
		toolBarManager.add(new Separator());

		ActionContributionItem middleRoomActionItem = new ActionContributionItem(new MiddleRoomSimulateAction());
		middleRoomActionItem.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		toolBarManager.add(middleRoomActionItem);
		toolBarManager.add(new Separator());

		ActionContributionItem largeRoomActionItem = new ActionContributionItem(new LargeRoomSimulateAction());
		largeRoomActionItem.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		toolBarManager.add(largeRoomActionItem);

		toolBarManager.update(true);		
	}
	
}
