/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Hisashi MIYASHITA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.ui.editor.browser;

/**
 * Interface to listen events from {@link IWebBrowserACTF}
 */
public interface IWebBrowserACTFEventListener {
	/**
	 * This method is called when the navigation was completed
	 * 
	 * @param webBrowser
	 *            event source {@link IWebBrowserACTF}
	 * @param url
	 *            target URL
	 */
	void navigateComplete(IWebBrowserACTF webBrowser, String url);

	/**
	 * This method is called when the title was changed
	 * 
	 * @param webBrowser
	 *            event source {@link IWebBrowserACTF}
	 * @param title
	 *            new title
	 */
	void titleChange(IWebBrowserACTF webBrowser, String title);

	/**
	 * This method is called when the progress status was changed
	 * 
	 * @param webBrowser
	 *            event source {@link IWebBrowserACTF}
	 * @param progress
	 *            current progress
	 * @param progressMax
	 *            goal value of progress
	 */
	void progressChange(IWebBrowserACTF webBrowser, int progress,
			int progressMax);

	/**
	 * This method is called when the navigation of root document was completed
	 * 
	 * @param webBrowser
	 *            event source {@link IWebBrowserACTF}
	 */
	void rootDocumentComplete(IWebBrowserACTF webBrowser);

	/**
	 * Dispose the instance
	 */
	void dispose();

	/**
	 * This method is called when the browser got focus
	 * 
	 * @param webBrowser
	 *            event source {@link IWebBrowserACTF}
	 */
	void getFocus(IWebBrowserACTF webBrowser);

	/**
	 * This method is called when the browser is disposed
	 * 
	 * @param webBrowser
	 *            event source {@link IWebBrowserACTF}
	 * @param title
	 *            title of the current page
	 */
	void browserDisposed(IWebBrowserACTF webBrowser, String title);

	/**
	 * This method is called before the navigation start
	 * 
	 * @param webBrowser
	 *            event source {@link IWebBrowserACTF}
	 * @param url
	 *            target URL
	 * @param targetFrameName
	 *            target frame name, if available
	 * @param isInNavigation
	 *            true if the navigation is child of other navigation
	 */
	void beforeNavigate(IWebBrowserACTF webBrowser, String url,
			String targetFrameName, boolean isInNavigation);

	/**
	 * This method is called when the refresh was started
	 * 
	 * @param webBrowser
	 *            event source {@link IWebBrowserACTF}
	 */
	void refreshStart(IWebBrowserACTF webBrowser);

	/**
	 * This method is called when the refresh was completed
	 * 
	 * @param webBrowser
	 *            event source {@link IWebBrowserACTF}
	 */
	void refreshComplete(IWebBrowserACTF webBrowser);

	/**
	 * This method is called when the navigation was stopped
	 * 
	 * @param webBrowser
	 *            event source {@link IWebBrowserACTF}
	 */
	void navigateStop(IWebBrowserACTF webBrowser);

	/**
	 * This method is called when the address bar area of the browser gained the
	 * focus
	 * 
	 * @param webBrowser
	 *            event source {@link IWebBrowserACTF}
	 */
	void focusGainedOfAddressText(IWebBrowserACTF webBrowser);

	/**
	 * This method is called when the address bar area of the browser lost the
	 * focus
	 * 
	 * @param webBrowser
	 *            event source {@link IWebBrowserACTF}
	 */
	void focusLostOfAddressText(IWebBrowserACTF webBrowser);

	/**
	 * This method is called when the new instance of {@link IWebBrowserACTF}
	 * was created
	 * 
	 * @param webBrowser
	 *            event source {@link IWebBrowserACTF}
	 */
	void newWindow(IWebBrowserACTF webBrowser);
}
