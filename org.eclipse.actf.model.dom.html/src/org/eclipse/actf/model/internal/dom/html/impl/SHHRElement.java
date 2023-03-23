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

import org.w3c.dom.html.HTMLHRElement;

@SuppressWarnings("nls")
public class SHHRElement extends SHElement implements HTMLHRElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 37030080476765859L;

	protected SHHRElement(String tagName, SHDocument doc) {
		super(tagName, doc);
	}

	public String getAlign() {
		return getAttribute("align");
	}

	public void setAlign(String align) {
		setAttribute("align", align);
	}

	public boolean getNoShade() {
		String val = getAttribute("noshade");
		return val != null && val.length() != 0;
	}

	public void setNoShade(boolean noShade) {
		setAttribute("noshade", noShade ? "noshade" : null);
	}

	public String getSize() {
		return getAttribute("size");
	}

	public void setSize(String size) {
		setAttribute("size", size);
	}

	public String getWidth() {
		return getAttribute("width");
	}

	public void setWidth(String width) {
		setAttribute("width", width);
	}
}
