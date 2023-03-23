/*******************************************************************************
 * Copyright (c) 2010, 2014 Ministry of Internal Affairs and Communications (MIC),
 * IBM Corporation and Others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Yasuharu GOTOU (MIC) - initial API and implementation
 *    Kentarou FUKUDA (IBM) - [383882] - Eclipse 4.2 adaptation
 *******************************************************************************/

package org.eclipse.actf.examples.michecker;

import org.eclipse.actf.model.ui.editor.actions.FavoritesMenu;
import org.eclipse.actf.ui.util.ProgressContribution;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	private FavoritesMenu _favoritesMenu;

	private IWorkbenchAction _maximizeAction;

	private IWorkbenchAction _nextViewAction;

	private IWorkbenchAction _prevViewAction;

	private IWorkbenchAction _nextEditorAction;

	private IWorkbenchAction _prevEditorAction;

	private IWorkbenchAction _showViewMenuAction;

	private IWorkbenchAction _preferenceAction;

	private IWorkbenchAction _helpAction;

	private IWorkbenchAction _aboutAction;

	private IWorkbenchAction _quitAction;

	private IWorkbenchAction _closeAction;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	protected void makeActions(IWorkbenchWindow window) {

		this._favoritesMenu = new FavoritesMenu(window);

		this._maximizeAction = ActionFactory.MAXIMIZE.create(window);

		this._nextViewAction = ActionFactory.NEXT_PART.create(window);

		this._prevViewAction = ActionFactory.PREVIOUS_PART.create(window);

		this._nextEditorAction = ActionFactory.NEXT_EDITOR.create(window);

		this._prevEditorAction = ActionFactory.PREVIOUS_EDITOR.create(window);

		this._showViewMenuAction = ActionFactory.SHOW_VIEW_MENU.create(window);

		this._preferenceAction = ActionFactory.PREFERENCES.create(window);

		this._helpAction = ActionFactory.HELP_CONTENTS.create(window);

		this._aboutAction = ActionFactory.ABOUT.create(window);

		this._quitAction = ActionFactory.QUIT.create(window);

		this._closeAction = ActionFactory.CLOSE.create(window);
	}

	@SuppressWarnings("nls")
	protected void fillMenuBar(IMenuManager menuBar) {

		// File
		MenuManager fileMenu = new MenuManager(MiCheckerPlugin
				.getResourceString("menu.file"),
				IWorkbenchActionConstants.M_FILE);
		fileMenu.add(new Separator("fileGroup"));
		fileMenu.add(new Separator());
		fileMenu.add(new Separator("closeGroup"));
		fileMenu.add(_closeAction);
		fileMenu.add(new Separator());
		fileMenu.add(new Separator("smilGroup"));
		fileMenu.add(new Separator());
		fileMenu.add(new Separator("exitGroup"));
		fileMenu.add(_quitAction);
		menuBar.add(fileMenu);

		// Tool
		MenuManager toolMenu = new MenuManager(MiCheckerPlugin
				.getResourceString("menu.viz"), "visualization");
		menuBar.add(toolMenu);

		// Favorite
		menuBar.add(this._favoritesMenu);

		// Add a group marker indicating where action set menus will appear.
		menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));

		// Window
		MenuManager windowMenu = new MenuManager(MiCheckerPlugin
				.getResourceString("menu.window"),
				IWorkbenchActionConstants.M_WINDOW);
		windowMenu.add(new Separator());
		MenuManager navigationMenu = new MenuManager(MiCheckerPlugin
				.getResourceString("menu.navigation"),
				"navigationMenu");
		windowMenu.add(navigationMenu);
		navigationMenu.add(_showViewMenuAction);
		navigationMenu.add(new Separator());

		navigationMenu.add(_maximizeAction);
		navigationMenu.add(_nextEditorAction);
		navigationMenu.add(_prevEditorAction);

		navigationMenu.add(new Separator());
		navigationMenu.add(_nextViewAction);
		navigationMenu.add(_prevViewAction);

		windowMenu.add(new Separator());
		windowMenu.add(_preferenceAction);
		menuBar.add(windowMenu);

		MenuManager helpMenu = new MenuManager(MiCheckerPlugin
				.getResourceString("menu.help"),
				IWorkbenchActionConstants.M_HELP);
		helpMenu.add(new Separator(IWorkbenchActionConstants.HELP_START));
		helpMenu.add(_helpAction);
		helpMenu.add(new Separator(IWorkbenchActionConstants.HELP_END));
		helpMenu.add(_aboutAction);
		menuBar.add(helpMenu);

		register(_maximizeAction);
		register(_nextEditorAction);
		register(_prevEditorAction);
		register(_nextViewAction);
		register(_prevViewAction);
		register(_showViewMenuAction);
		register(_preferenceAction);
		register(_helpAction);
		register(_aboutAction);
		register(_quitAction);
		register(_closeAction);

		ActionFactory.linkCycleActionPair(_nextEditorAction, _prevEditorAction);
		ActionFactory.linkCycleActionPair(_nextViewAction, _prevViewAction);

	}

	@Override
	protected void fillStatusLine(IStatusLineManager statusLine) {
		super.fillStatusLine(statusLine);

		ProgressContribution pc = new ProgressContribution(
				ProgressContribution.PROGRESS_CONTRIBUTION_ID);
		pc.setVisible(false);
		statusLine.add(pc);
	}
	
	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) {
		super.fillCoolBar(coolBar);
		
		//to place actions left side (Quick Access) 
		coolBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
	}

}
