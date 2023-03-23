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
package org.eclipse.actf.visualization.eval;

import org.eclipse.actf.model.ui.editor.browser.IWebBrowserStyleInfo;
import org.eclipse.actf.visualization.eval.html.HtmlEvalUtil;
import org.eclipse.actf.visualization.internal.eval.CheckTargetImpl;
import org.eclipse.actf.visualization.internal.eval.HtmlCheckTargetImpl;
import org.w3c.dom.Document;

/**
 * Factory class for {@link ICheckTarget}
 */
public class CheckTargetFactory {

	/**
	 * Constructor of {@link ICheckTarget}
	 * 
	 * @param target
	 *            evaluation target {@link Document}
	 * @param targetUrl
	 *            target URL
	 */
	public static ICheckTarget createCheckTarget(Document target,
			String targetUrl) {
		return new CheckTargetImpl(target, targetUrl);
	}

	/**
	 * Constructor of {@link IHtmlCheckTarget}
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
	public static IHtmlCheckTarget createHtmlCheckTarget(Document target,
			String targetUrl, IWebBrowserStyleInfo browserStyleInfo,
			HtmlEvalUtil htmlEvalUtil) {
		return new HtmlCheckTargetImpl(target, targetUrl, browserStyleInfo,
				htmlEvalUtil);
	}

}
