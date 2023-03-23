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

import org.w3c.dom.html.HTMLAnchorElement;

@SuppressWarnings("nls")
public class SHAnchorElement extends SHALinkElement implements
		HTMLAnchorElement {
	private static final long serialVersionUID = -3791078018472111266L;

	protected SHAnchorElement(String tagName, SHDocument doc) {
		super(tagName, doc);
	}

	public String getCharset() {
		return getAttribute("charset");
	}

	public void setCharset(String charset) {
		setAttribute("charset", charset);
	}

	public String getHreflang() {
		return getAttribute("hreflang");
	}

	public void setHreflang(String hreflang) {
		setAttribute("hreflang", hreflang);
	}

	public String getName() {
		return getAttribute("name");
	}

	public void setName(String name) {
		setAttribute("name", name);
	}

	public String getRel() {
		return getAttribute("rel");
	}

	public void setRel(String rel) {
		setAttribute("rel", rel);
	}

	public String getRev() {
		return getAttribute("rev");
	}

	public void setRev(String rev) {
		setAttribute("rev", rev);
	}

	public String getType() {
		return getAttribute("type");
	}

	public void setType(String type) {
		setAttribute("type", type);
	}

	public void blur() {
	}

	public void focus() {
	}
}
