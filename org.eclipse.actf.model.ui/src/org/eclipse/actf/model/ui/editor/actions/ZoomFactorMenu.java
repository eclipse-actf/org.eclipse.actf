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
package org.eclipse.actf.model.ui.editor.actions;

import org.eclipse.actf.model.ui.util.ModelServiceMessages;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * MenuManager for text size control
 */
public class ZoomFactorMenu extends MenuManager {

	private static final String[] MENUTEXTS = { "200%", "150%", "100%", "75%", "50%" };

	/**
	 * Constructor of the class
	 * 
	 * @param window target {@link IWorkbenchWindow}
	 */
	public ZoomFactorMenu(IWorkbenchWindow window) {
		super(ModelServiceMessages.MenuConst_Zoom);

		int menuNum = 5;
		ZoomAction[] displayTextSizeAction = new ZoomAction[menuNum];

		for (int i = 0; i < menuNum; i++) {
			displayTextSizeAction[i] = new ZoomAction(window);
			displayTextSizeAction[i].setText(MENUTEXTS[i]);
			add(displayTextSizeAction[i]);
			double zoomFactor = 1;
			switch (i) {
			case 0 -> zoomFactor = 2;
			case 1 -> zoomFactor = 1.5;
			case 2 -> zoomFactor = 1;
			case 3 -> zoomFactor = 0.75;
			case 4 -> zoomFactor = 0.5;
			default -> zoomFactor = 1;
			}
			displayTextSizeAction[i].setZoomFactor(zoomFactor);
			// if(currFontSize == fontSize){
			// }
		}
	}
	
	
}
