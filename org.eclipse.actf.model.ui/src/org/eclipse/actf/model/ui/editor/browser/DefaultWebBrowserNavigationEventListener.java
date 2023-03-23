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

package org.eclipse.actf.model.ui.editor.browser;

/**
 * Default event handler to dispatch navigation event to the current active
 * {@link IWebBrowserACTF}.
 * 
 * @see IWebBrowserACTFEventListener
 * @see WebBrowserNavigationEvent
 */
public class DefaultWebBrowserNavigationEventListener implements
		IWebBrowserNavigationEventListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.model.ui.editor.browser.IWebBrowserNavigationEventListener#goBack(org.eclipse.actf.model.ui.editor.browser.WebBrowserNavigationEvent)
	 */
	public void goBack(WebBrowserNavigationEvent e) {
		IWebBrowserACTF browser = e.getBrowser();
		if (browser != null) {
			browser.goBackward();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.model.ui.editor.browser.IWebBrowserNavigationEventListener#goForward(org.eclipse.actf.model.ui.editor.browser.WebBrowserNavigationEvent)
	 */
	public void goForward(WebBrowserNavigationEvent e) {
		IWebBrowserACTF browser = e.getBrowser();
		if (browser != null) {
			browser.goForward();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.model.ui.editor.browser.IWebBrowserNavigationEventListener#refresh(org.eclipse.actf.model.ui.editor.browser.WebBrowserNavigationEvent)
	 */
	public void refresh(WebBrowserNavigationEvent e) {
		IWebBrowserACTF browser = e.getBrowser();
		if (browser != null) {
			browser.navigateRefresh();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.model.ui.editor.browser.IWebBrowserNavigationEventListener#stop(org.eclipse.actf.model.ui.editor.browser.WebBrowserNavigationEvent)
	 */
	public void stop(WebBrowserNavigationEvent e) {
		IWebBrowserACTF browser = e.getBrowser();
		if (browser != null) {
			browser.navigateStop();
		}
	}

}
