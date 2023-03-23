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

import java.util.EventObject;

/**
 * A WebBrowserNavigationEvent includes target {@link IWebBrowserACTF} to handle
 * user navigation
 */
public class WebBrowserNavigationEvent extends EventObject {

	private static final long serialVersionUID = 4659419119012003840L;

	private IWebBrowserACTF browser;

	/**
	 * Constructor of the class
	 * 
	 * @param source
	 *            event source
	 * @param browser
	 *            target {@link IWebBrowserACTF}
	 */
	public WebBrowserNavigationEvent(Object source, IWebBrowserACTF browser) {
		super(source);
		this.browser = browser;
	}

	/**
	 * Get target {@link IWebBrowserACTF}
	 * @return {@link IWebBrowserACTF}
	 */
	public IWebBrowserACTF getBrowser() {
		return browser;
	}

}
