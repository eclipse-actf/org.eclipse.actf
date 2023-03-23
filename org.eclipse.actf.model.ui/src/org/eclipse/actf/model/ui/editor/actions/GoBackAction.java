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

package org.eclipse.actf.model.ui.editor.actions;

import org.eclipse.actf.model.ui.IModelService;
import org.eclipse.actf.model.ui.editor.browser.DefaultWebBrowserNavigationEventListener;
import org.eclipse.actf.model.ui.editor.browser.IWebBrowserACTF;
import org.eclipse.actf.model.ui.editor.browser.IWebBrowserNavigationEventListener;
import org.eclipse.actf.model.ui.editor.browser.WebBrowserNavigationEvent;
import org.eclipse.actf.model.ui.util.ModelServiceMessages;
import org.eclipse.actf.model.ui.util.ModelServiceUtils;
import org.eclipse.actf.ui.util.PlatformUIUtil;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.ISharedImages;

/**
 * Action to invoke goBackward method of the current active
 * {@link IWebBrowserACTF}
 */
public class GoBackAction extends Action {
	private String message = ModelServiceMessages.WebBrowser_Backward_4;

	private String message_tp = ModelServiceMessages.WebBrowser_Backward_4_tp;

	private IWebBrowserNavigationEventListener defaultListener = new DefaultWebBrowserNavigationEventListener();

	/**
	 * Constructor of the action with image icon.
	 */
	public GoBackAction() {
		this(true);
	}

	/**
	 * Constructor of the action.
	 * 
	 * @param flag
	 *            if true, set image icon to the Action
	 */
	public GoBackAction(boolean flag) {
		setText(message);
		setToolTipText(message_tp);
		if (flag)
			setImageDescriptor(PlatformUIUtil
					.getSharedImageDescriptor(ISharedImages.IMG_TOOL_BACK));
	}

	public void run() {
		IModelService modelService = ModelServiceUtils.getActiveModelService();
		if (modelService != null && modelService instanceof IWebBrowserACTF) {
			WebBrowserNavigationEvent event = new WebBrowserNavigationEvent(
					this, ((IWebBrowserACTF) modelService));
			if (IWebBrowserACTF.WebBrowserNavigationEventListnerHolder.LISTENER != null) {
				IWebBrowserACTF.WebBrowserNavigationEventListnerHolder.LISTENER
						.goBack(event);
			} else {
				defaultListener.goBack(event);
			}
		}
	}
}
