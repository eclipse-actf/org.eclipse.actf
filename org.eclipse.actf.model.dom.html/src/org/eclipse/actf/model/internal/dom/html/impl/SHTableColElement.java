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

import org.w3c.dom.html.HTMLTableColElement;

@SuppressWarnings("nls")
public class SHTableColElement extends SHElement implements HTMLTableColElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1460706571643757807L;

	protected SHTableColElement(String name, SHDocument doc) {
		super(name, doc);
	}

	public String getAlign() {
		return getAttribute("align");
	}

	public void setAlign(String align) {
		setAttribute("align", align);
	}

	public String getCh() {
		return getAttribute("char");
	}

	public void setCh(String ch) {
		setAttribute("char", ch);
	}

	public String getChOff() {
		return getAttribute("charoff");
	}

	public void setChOff(String chOff) {
		setAttribute("charoff", chOff);
	}

	/**
	 * @exception NumberFormatException
	 */
	public int getSpan() {
		return Integer.parseInt(getAttribute("span"));
	}

	public void setSpan(int span) {
		setAttribute("span", Integer.toString(span));
	}

	public String getVAlign() {
		return getAttribute("valign");
	}

	public void setVAlign(String vAlign) {
		setAttribute("valign", vAlign);
	}

	public String getWidth() {
		return getAttribute("width");
	}

	public void setWidth(String width) {
		setAttribute("width", width);
	}
}
