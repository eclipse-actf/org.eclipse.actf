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

import org.w3c.dom.html.HTMLLinkElement;

@SuppressWarnings("nls")
public class SHLinkElement extends SHElement implements HTMLLinkElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4319262268754401952L;

	protected SHLinkElement(String tagName, SHDocument doc) {
		super(tagName, doc);
	}

	private boolean disabled;

	public boolean getDisabled() {
		return this.disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getCharset() {
		return getAttribute("charset");
	}

	public void setCharset(String charset) {
		setAttribute("charset", charset);
	}

	public String getHref() {
		return getAttribute("href");
	}

	public void setHref(String href) {
		setAttribute("href", href);
	}

	public String getHreflang() {
		return getAttribute("hreflang");
	}

	public void setHreflang(String hreflang) {
		setAttribute("hreflang", hreflang);
	}

	public String getMedia() {
		return getAttribute("media");
	}

	public void setMedia(String media) {
		setAttribute("media", media);
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

	public String getTarget() {
		return getAttribute("target");
	}

	public void setTarget(String target) {
		setAttribute("target", target);
	}

	public String getType() {
		return getAttribute("type");
	}

	public void setType(String type) {
		setAttribute("type", type);
	}
}
