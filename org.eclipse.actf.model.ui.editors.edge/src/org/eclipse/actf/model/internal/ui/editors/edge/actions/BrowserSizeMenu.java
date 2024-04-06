/*******************************************************************************
 * Copyright (c) 2023, 2024 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.model.internal.ui.editors.edge.actions;

import org.eclipse.actf.model.internal.ui.editors.edge.Messages;
import org.eclipse.actf.model.ui.util.ModelServiceMessages;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * MenuManager for browser size control
 */
public class BrowserSizeMenu extends MenuManager {

	private static final String[] MENUTEXTS = { ModelServiceMessages.MenuConst_BrowserFreeSize, "360 * 760",
			"375 * 667", "412 * 915", "768 * 1024", "810 * 1080", Messages.BrowserSizeMenu_custom };

	/**
	 * Constructor of the class
	 * 
	 * @param window target {@link IWorkbenchWindow}
	 */
	public BrowserSizeMenu(IWorkbenchWindow window) {
		super(ModelServiceMessages.MenuConst_BrowserSize);

		int menuNum = 7;// 7
		BrowserSizeAction[] browserSizeAction = new BrowserSizeAction[menuNum];
		int width = -1;
		int height = -1;
		boolean free = true;

		for (int i = 0; i < menuNum; i++) {
			browserSizeAction[i] = new BrowserSizeAction(window);
			browserSizeAction[i].setText(MENUTEXTS[i]);
			add(browserSizeAction[i]);
			switch (i) {
			case 1 -> {
				free = false;
				width = 360;
				height = 760;
			}
			case 2 -> {
				free = false;
				width = 375;
				height = 667;
			}
			case 3 -> {
				free = false;
				width = 412;
				height = 915;
			}
			case 4 -> {
				free = false;
				width = 768;
				height = 1024;
			}
			case 5 -> {
				free = false;
				width = 810;
				height = 1080;
			}
			case 6 -> {
				browserSizeAction[i].setBrowserSizeToCustom();
			}
			default -> {
			}
			}

			if (i != 6) {
				browserSizeAction[i].setBrowserSize(free, width, height);
			}
			// System.out.println(i + ": " + free + " " + width + " " + height);
		}
	}

}
