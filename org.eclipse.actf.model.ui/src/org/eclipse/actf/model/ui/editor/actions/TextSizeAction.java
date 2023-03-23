/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.model.ui.editor.actions;

import org.eclipse.actf.model.ui.IModelService;
import org.eclipse.actf.model.ui.editor.browser.IWebBrowserACTF;
import org.eclipse.actf.model.ui.util.ModelServiceUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * Action to invoke setFontSize method of the current active
 * {@link IWebBrowserACTF}
 */
public class TextSizeAction extends Action {

	public static final String ID = TextSizeAction.class.getName();

	private int _fontSize = 2;

	/**
	 * Constructor of the action with image icon.
	 */
	public TextSizeAction(IWorkbenchWindow window) {
		super(ID, Action.AS_RADIO_BUTTON);
	}

	public void run() {
		IModelService modelService = ModelServiceUtils.getActiveModelService();
		if (modelService != null && modelService instanceof IWebBrowserACTF) {
			((IWebBrowserACTF) modelService).setFontSize(this._fontSize);
		}
	}

	/**
	 * Set target font size
	 * 
	 * @param fontSize
	 *            target font size
	 */
	public void setFontSize(int fontSize) {
		this._fontSize = fontSize;
	}

	/**
	 * Get target font size
	 * 
	 * @return font size
	 */
	public int getFontSize() {
		return this._fontSize;
	}
}
