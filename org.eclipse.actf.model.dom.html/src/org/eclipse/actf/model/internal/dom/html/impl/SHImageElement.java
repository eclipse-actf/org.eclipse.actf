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

import org.w3c.dom.html.HTMLImageElement;

@SuppressWarnings("nls")
public class SHImageElement extends SHElement implements HTMLImageElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7126159241890571195L;

	protected SHImageElement(String tagName, SHDocument doc) {
		super(tagName, doc);
	}

	public String getLowSrc() {
		return getAttribute("lowsrc");
	}

	public void setLowSrc(String lowSrc) {
		setAttribute("lowsrc", lowSrc);
	}

	public String getName() {
		return getAttribute("name");
	}

	public void setName(String name) {
		setAttribute("name", name);
	}

	public String getAlign() {
		return getAttribute("align");
	}

	public void setAlign(String align) {
		setAttribute("align", align);
	}

	public String getAlt() {
		return getAttribute("alt");
	}

	public void setAlt(String alt) {
		setAttribute("alt", alt);
	}

	public String getBorder() {
		return getAttribute("border");
	}

	public void setBorder(String border) {
		setAttribute("border", border);
	}

	public String getHeight() {
		return getAttribute("height");
	}

	public void setHeight(String height) {
		setAttribute("height", height);
	}

	public String getHspace() {
		return getAttribute("hspace");
	}

	public void setHspace(String hspace) {
		setAttribute("hspace", hspace);
	}

	public boolean getIsMap() {
		String val = getAttribute("ismap");
		return val != null && val.length() != 0;
	}

	public void setIsMap(boolean isMap) {
		setAttribute("ismap", isMap ? "ismap" : null);
	}

	public String getLongDesc() {
		return getAttribute("longdesc");
	}

	public void setLongDesc(String longDesc) {
		setAttribute("longdesc", longDesc);
	}

	public String getSrc() {
		return getAttribute("src");
	}

	public void setSrc(String src) {
		setAttribute("src", src);
	}

	public String getUseMap() {
		return getAttribute("usemap");
	}

	public void setUseMap(String useMap) {
		setAttribute("usemap", useMap);
	}

	public String getVspace() {
		return getAttribute("vsapce");
	}

	public void setVspace(String vspace) {
		setAttribute("vspace", vspace);
	}

	public String getWidth() {
		return getAttribute("width");
	}

	public void setWidth(String width) {
		setAttribute("width", width);
	}
}
