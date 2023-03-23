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

import org.w3c.dom.html.HTMLBodyElement;

@SuppressWarnings("nls")
public class SHBodyElement extends SHElement implements HTMLBodyElement {
	private static final long serialVersionUID = 7669838705165565862L;

	protected SHBodyElement(String tagName, SHDocument doc) {
		super(tagName, doc);
	}

	public String getALink() {
		return getAttribute("alink");
	}

	public String getBackground() {
		return getAttribute("background");
	}

	public String getBgColor() {
		return getAttribute("bgcolor");
	}

	public String getLink() {
		return getAttribute("link");
	}

	public String getText() {
		return getAttribute("text");
	}

	public String getVLink() {
		return getAttribute("vlink");
	}

	public void setALink(String aLink) {
		setAttribute("alink", aLink);
	}

	public void setBackground(String background) {
		setAttribute("background", background);
	}

	public void setBgColor(String bgColor) {
		setAttribute("bgcolor", bgColor);
	}

	public void setLink(String link) {
		setAttribute("link", link);
	}

	public void setText(String text) {
		setAttribute("text", text);
	}

	public void setVLink(String vLink) {
		setAttribute("vlink", vLink);
	}
}
