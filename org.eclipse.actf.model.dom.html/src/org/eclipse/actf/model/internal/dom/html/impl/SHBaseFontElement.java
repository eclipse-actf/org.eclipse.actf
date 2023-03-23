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

import org.w3c.dom.html.HTMLBaseFontElement;

@SuppressWarnings("nls")
public class SHBaseFontElement extends SHElement implements HTMLBaseFontElement {
	private static final long serialVersionUID = 476496807620515231L;

	protected SHBaseFontElement(String tagName, SHDocument doc) {
		super(tagName, doc);
	}

	public String getColor() {
		return getAttribute("color");
	}

	public void setColor(String color) {
		setAttribute("color", color);
	}

	public String getFace() {
		return getAttribute("face");
	}

	public void setFace(String face) {
		setAttribute("face", face);
	}

	public String getSize() {
		return getAttribute("size");
	}

	public void setSize(String size) {
		setAttribute("size", size);
	}
}
