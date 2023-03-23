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

import org.eclipse.actf.model.ui.IModelService;
import org.eclipse.actf.model.ui.editor.browser.IWebBrowserACTF;
import org.eclipse.actf.model.ui.util.ModelServiceUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * Action to invoke setZoomFactor method of the current active
 * {@link IWebBrowserACTF}
 */
public class ZoomAction extends Action {

	public static final String ID = ZoomAction.class.getName();

	private double _zoom = 1;

	/**
	 * Constructor of the action with image icon.
	 */
	public ZoomAction(IWorkbenchWindow window) {
		super(ID, Action.AS_RADIO_BUTTON);
	}

	public void run() {
		IModelService[] modelServices = ModelServiceUtils.getModelServices();
		for (IModelService modelService : modelServices) {
			if (modelService instanceof IWebBrowserACTF) {
				((IWebBrowserACTF) modelService).setZoomFactor(_zoom);
			}
		}
	}

	/**
	 * Set target zoomFactor
	 * 
	 * @param zoomFactor target zoomFactor
	 */
	public void setZoomFactor(double zoomFactor) {
		this._zoom = zoomFactor;
	}

	/**
	 * Get target zoomFactor
	 * 
	 * @return zoomFactor
	 */
	public double getZoomFactor() {
		return this._zoom;
	}
}
