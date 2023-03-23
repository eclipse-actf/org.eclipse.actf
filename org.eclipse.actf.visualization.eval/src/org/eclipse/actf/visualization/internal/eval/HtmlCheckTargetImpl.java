/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.eval;

import org.eclipse.actf.model.ui.editor.browser.IWebBrowserStyleInfo;
import org.eclipse.actf.visualization.eval.IHtmlCheckTarget;
import org.eclipse.actf.visualization.eval.html.HtmlEvalUtil;
import org.w3c.dom.Document;

/**
 * Default implementation of {@link IHtmlCheckTarget}
 */
public class HtmlCheckTargetImpl extends CheckTargetImpl implements
		IHtmlCheckTarget {

	private IWebBrowserStyleInfo browserStyleInfo;

	private HtmlEvalUtil htmlEvalUtil;

	/**
	 * Constructor of the class
	 * 
	 * @param target
	 *            target {@link Document}
	 * @param targetUrl
	 *            target URL
	 * @param browserStyleInfo
	 *            current style information
	 * @param htmlEvalUtil
	 *            target {@link HtmlEvalUtil}
	 */
	public HtmlCheckTargetImpl(Document target, String targetUrl,
			IWebBrowserStyleInfo browserStyleInfo, HtmlEvalUtil htmlEvalUtil) {
		super(target, targetUrl);
		this.browserStyleInfo = browserStyleInfo;
		this.htmlEvalUtil = htmlEvalUtil;
	}

	public HtmlEvalUtil getHtmlEvalUtil() {
		return htmlEvalUtil;
	}

	public IWebBrowserStyleInfo getBrowserAndStyleInfo() {
		return browserStyleInfo;
	}

}
