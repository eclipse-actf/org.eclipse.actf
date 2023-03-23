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
 * Interface to handle {@link WebBrowserNavigationEvent}.
 * 
 * @see WebBrowserNavigationEvent
 * @see DefaultWebBrowserNavigationEventListener
 */
public interface IWebBrowserNavigationEventListener {

	/**
	 * This method is called when user selects goBack navigation
	 * 
	 * @param e
	 *            event information including target {@link IWebBrowserACTF} to
	 *            handle this event
	 */
	public void goBack(WebBrowserNavigationEvent e);

	/**
	 * This method is called when user selects goForward navigation
	 * 
	 * @param e
	 *            event information including target {@link IWebBrowserACTF} to
	 *            handle this event
	 */
	public void goForward(WebBrowserNavigationEvent e);

	/**
	 * This method is called when user selects refresh navigation
	 * 
	 * @param e
	 *            event information including target {@link IWebBrowserACTF} to
	 *            handle this event
	 */
	public void refresh(WebBrowserNavigationEvent e);

	/**
	 * This method is called when user selects stop navigation
	 * 
	 * @param e
	 *            event information including target {@link IWebBrowserACTF} to
	 *            handle this event
	 */
	public void stop(WebBrowserNavigationEvent e);

}
