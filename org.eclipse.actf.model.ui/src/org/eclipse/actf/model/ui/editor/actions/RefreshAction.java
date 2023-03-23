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

import org.eclipse.actf.model.internal.ui.ModelUIPlugin;
import org.eclipse.actf.model.ui.IModelService;
import org.eclipse.actf.model.ui.editor.browser.DefaultWebBrowserNavigationEventListener;
import org.eclipse.actf.model.ui.editor.browser.IWebBrowserACTF;
import org.eclipse.actf.model.ui.editor.browser.IWebBrowserNavigationEventListener;
import org.eclipse.actf.model.ui.editor.browser.WebBrowserNavigationEvent;
import org.eclipse.actf.model.ui.util.ModelServiceMessages;
import org.eclipse.actf.model.ui.util.ModelServiceUtils;
import org.eclipse.jface.action.Action;

/**
 * Action to invoke refresh method of the current active
 * {@link IWebBrowserACTF}
 */
public class RefreshAction extends Action {
	private String message = ModelServiceMessages.WebBrowser_Refresh;

	private String message_tp = ModelServiceMessages.WebBrowser_Refresh_tp;

	private IWebBrowserNavigationEventListener defaultListener = new DefaultWebBrowserNavigationEventListener();

	/**
	 * Constructor of the action with image icon.
	 */
	public RefreshAction() {
		this(true);
	}

	/**
	 * Constructor of the action.
	 * 
	 * @param flag
	 *            if true, set image icon to the Action
	 */
	public RefreshAction(boolean flag) {
		setText(message);
		setToolTipText(message_tp);
		if (flag)
			setImageDescriptor(ModelUIPlugin
					.getImageDescriptor("icons/toolbar/reload.png")); //$NON-NLS-1$
	}

	public void run() {
		IModelService modelService = ModelServiceUtils.getActiveModelService();
		if (modelService != null && modelService instanceof IWebBrowserACTF) {
			WebBrowserNavigationEvent event = new WebBrowserNavigationEvent(
					this, ((IWebBrowserACTF) modelService));
			if (IWebBrowserACTF.WebBrowserNavigationEventListnerHolder.LISTENER != null) {
				IWebBrowserACTF.WebBrowserNavigationEventListnerHolder.LISTENER
						.refresh(event);
			} else {
				defaultListener.refresh(event);
			}
		}
	}
}
