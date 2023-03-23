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

import org.w3c.dom.html.HTMLAppletElement;

@SuppressWarnings("nls")
public class SHAppletElement extends SHElement implements HTMLAppletElement {
	private static final long serialVersionUID = -7364854653311772304L;

	protected SHAppletElement(String tagName, SHDocument doc) {
		super(tagName, doc);
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

	public String getArchive() {
		return getAttribute("archive");
	}

	public void setArchive(String archive) {
		setAttribute("archive", archive);
	}

	public String getCode() {
		return getAttribute("code");
	}

	public void setCode(String code) {
		setAttribute("code", code);
	}

	public String getCodeBase() {
		return getAttribute("codebase");
	}

	public void setCodeBase(String codeBase) {
		setAttribute("codebase", codeBase);
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

	public String getName() {
		return getAttribute("name");
	}

	public void setName(String name) {
		setAttribute("name", name);
	}

	public String getObject() {
		return getAttribute("object");
	}

	public void setObject(String object) {
		setAttribute("object", object);
	}

	public String getVspace() {
		return getAttribute("vspace");
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
