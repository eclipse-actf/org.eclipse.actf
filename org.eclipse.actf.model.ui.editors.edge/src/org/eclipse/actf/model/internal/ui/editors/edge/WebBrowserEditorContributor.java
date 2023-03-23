/*******************************************************************************
 * Copyright (c) 2007, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.internal.ui.editors.edge;

import org.eclipse.actf.model.ui.editor.actions.DisableDebugMessageAction;
import org.eclipse.actf.model.ui.editor.actions.GoBackAction;
import org.eclipse.actf.model.ui.editor.actions.GoForwardAction;
import org.eclipse.actf.model.ui.editor.actions.RefreshAction;
import org.eclipse.actf.model.ui.editor.actions.StopAction;
import org.eclipse.actf.model.ui.editor.actions.TextSizeMenu;
import org.eclipse.actf.model.ui.editor.actions.ZoomFactorMenu;
import org.eclipse.actf.ui.util.Messages;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorActionBarContributor;

public class WebBrowserEditorContributor extends EditorActionBarContributor {

	private DisableDebugMessageAction silentAction;

	private DisableDebugMessageAction silentAction2;

	public WebBrowserEditorContributor() {
		silentAction = new DisableDebugMessageAction();
		silentAction2 = new DisableDebugMessageAction(false);
		silentAction.addPropertyChangeListener(silentAction2);
		silentAction2.addPropertyChangeListener(silentAction);
	}

	public void init(IActionBars bars, IWorkbenchPage page) {
		super.init(bars, page);
		// System.out.println(page);

	}

	public void contributeToCoolBar(ICoolBarManager coolBarManager) {
	}

	public void setActiveEditor(IEditorPart targetEditor) {
		// TODO use this to get IWebBrowser for Actions. don't depend on
		// Mediator
	}

	public void dispose() {
	}

	public void contributeToToolBar(IToolBarManager toolBarManager) {
		/* moved to editor area toolbar 
		toolBarManager.add(new GoBackAction());
		toolBarManager.add(new GoForwardAction());
		toolBarManager.add(new RefreshAction());
		toolBarManager.add(new StopAction());
		*/
		if (Platform.inDebugMode()) {
			toolBarManager.add(silentAction);
		}
	}

	public void contributeToMenu(IMenuManager menuManager) {
		// TODO consider aiBrowser
		// menuManager.add(new
		// FavoritesMenu(PlatformUI.getWorkbench().getActiveWorkbenchWindow()));
		// Display TODO only for Browser
		// TODO
		MenuManager displayMenu = new MenuManager(
				Messages.MenuConst__Display_1, "displayMenu"); //$NON-NLS-1$
		displayMenu.add(new Separator("navigate")); //$NON-NLS-1$
		displayMenu.add(new GoBackAction(false));
		displayMenu.add(new GoForwardAction(false));
		displayMenu.add(new RefreshAction(false));
		displayMenu.add(new StopAction(false));
		if (Platform.inDebugMode()) {
			displayMenu.add(silentAction2);
		}

		displayMenu.add(new Separator());
		displayMenu.add(new Separator("view")); //$NON-NLS-1$
		displayMenu.add(new ZoomFactorMenu(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow()));
		menuManager.insertAfter(IWorkbenchActionConstants.M_FILE, displayMenu);

	}

}
