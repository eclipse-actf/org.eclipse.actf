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
package org.eclipse.actf.visualization.eval;

import org.eclipse.actf.model.ui.editor.browser.IWebBrowserACTF;
import org.eclipse.actf.model.ui.editor.browser.IWebBrowserStyleInfo;
import org.eclipse.actf.visualization.eval.html.HtmlEvalUtil;

/**
 * Interface to hold information about target HTML content
 * 
 * @see ICheckTarget
 */
public interface IHtmlCheckTarget extends ICheckTarget {

	/**
	 * Get current style information of content and {@link IWebBrowserACTF}
	 * 
	 * @return current style information
	 */
	IWebBrowserStyleInfo getBrowserAndStyleInfo();

	/**
	 * Get {@link HtmlEvalUtil} that have useful information to evaluate
	 * accessibility of target page
	 * 
	 * @return
	 */
	HtmlEvalUtil getHtmlEvalUtil();

}
