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

import org.w3c.dom.Document;
import org.w3c.dom.html.HTMLIFrameElement;

@SuppressWarnings("nls")
public class SHIFrameElement extends SHElement implements HTMLIFrameElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8835158980177200627L;

	protected SHIFrameElement(String tagName, SHDocument doc) {
		super(tagName, doc);
	}

	public String getAlign() {
		return getAttribute("align");
	}

	public void setAlign(String align) {
		setAttribute("align", align);
	}

	public String getFrameBorder() {
		return getAttribute("frameborfer");
	}

	public void setFrameBorder(String frameBorder) {
		setAttribute("frameborder", frameBorder);
	}

	public String getHeight() {
		return getAttribute("height");
	}

	public void setHeight(String height) {
		setAttribute("height", height);
	}

	public String getLongDesc() {
		return getAttribute("longdesc");
	}

	public void setLongDesc(String longDesc) {
		setAttribute("longDesc", longDesc);
	}

	public String getMarginHeight() {
		return getAttribute("marginheight");
	}

	public void setMarginHeight(String marginHeight) {
		setAttribute("marginheight", marginHeight);
	}

	public String getMarginWidth() {
		return getAttribute("marginwidth");
	}

	public void setMarginWidth(String marginWidth) {
		setAttribute("marginwidth", marginWidth);
	}

	public String getName() {
		return getAttribute("name");
	}

	public void setName(String name) {
		setAttribute("name", name);
	}

	public String getScrolling() {
		return getAttribute("scrolling");
	}

	public void setScrolling(String scrolling) {
		setAttribute("scrolling", scrolling);
	}

	public String getSrc() {
		return getAttribute("src");
	}

	public void setSrc(String src) {
		setAttribute("src", src);
	}

	public String getWidth() {
		return getAttribute("src");
	}

	public void setWidth(String width) {
		setAttribute("width", width);
	}

	private Document contentDoc;

	public Document getContentDocument() {
		return contentDoc;
	}

	/**
	 * Sets the content document this internal frame contains.
	 */
	public void setContentDocument(Document contentDoc) {
		this.contentDoc = contentDoc;
	}
}
