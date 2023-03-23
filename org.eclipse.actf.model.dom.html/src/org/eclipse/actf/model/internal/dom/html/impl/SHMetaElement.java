/*******************************************************************************
 * Copyright (c) 1998, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Goh KONDOH - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.internal.dom.html.impl;

import org.w3c.dom.html.HTMLMetaElement;

@SuppressWarnings("nls")
public class SHMetaElement extends SHElement implements HTMLMetaElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5032416167035491245L;

	protected SHMetaElement(String tagName, SHDocument doc) {
		super(tagName, doc);
	}

	public String getContent() {
		return getAttribute("content");
	}

	public String getHttpEquiv() {
		return getAttribute("http-equiv");
	}

	public String getName() {
		return getAttribute("name");
	}

	public String getScheme() {
		return getAttribute("scheme");
	}

	public void setContent(String content) {
		setAttribute("content", content);
	}

	public void setHttpEquiv(String httpEquiv) {
		setAttribute("http-equiv", httpEquiv);
	}

	public void setName(String name) {
		setAttribute("name", name);
	}

	public void setScheme(String scheme) {
		setAttribute("scheme", scheme);
	}
}
