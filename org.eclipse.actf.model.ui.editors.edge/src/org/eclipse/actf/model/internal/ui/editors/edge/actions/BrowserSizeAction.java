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

import org.eclipse.actf.model.internal.ui.editors.edge.WebBrowserEdgeImpl;
import org.eclipse.actf.model.ui.IModelService;
import org.eclipse.actf.model.ui.util.ModelServiceUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * Action to invoke setBrowserSize method of the current active
 * {@link IWebBrowserACTF}
 */
/**
 * @author KentarouFukuda
 *
 */
public class BrowserSizeAction extends Action {

	public static final String ID = BrowserSizeAction.class.getName();

	protected static boolean isFirst = true;
	private static boolean prevIsFree = true;
	private static int prevWidth = -1;
	private static int prevHeight = -1;

	private boolean isFree = true;
	private int width = -1;
	private int height = -1;

	/**
	 * Constructor of the action without image icon.
	 */
	public BrowserSizeAction(IWorkbenchWindow window) {
		super(ID, Action.AS_RADIO_BUTTON);
	}

	public void run() {
		IModelService modelService = ModelServiceUtils.getActiveModelService();

		// to avoid BrowserSize APIs into IWebBrowserACTF
		if (modelService instanceof WebBrowserEdgeImpl && checkPrev()) {

			WebBrowserEdgeImpl browserImpl = (WebBrowserEdgeImpl) modelService;
			browserImpl.setBrowserSize(isFree, width, height);
			prevIsFree = isFree;
			prevWidth = width;
			prevHeight = height;

			String urlS = browserImpl.getURL();
			IEditorPart editorPart = modelService.getModelServiceHolder().getEditorPart();
			ModelServiceUtils.launchNew(urlS);

			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(editorPart, false);

		}

	}

	/**
	 * Set target Browser Size
	 * 
	 * @param zoomFactor target zoomFactor
	 */
	public void setBrowserSize(boolean isFree, int width, int height) {
		this.isFree = isFree;
		this.width = width;
		this.height = height;
	}

	
	/**
	 * Temp private method to avoid invalid BrowserSize change call. 
	 * 
	 * @return true if BrowserSize change needed
	 */
	private boolean checkPrev() {
		if (isFirst) {
			isFirst = false;
			return true;
		}

		return !((prevIsFree == isFree) && (prevWidth == width) && (prevHeight == height));
	}

}
