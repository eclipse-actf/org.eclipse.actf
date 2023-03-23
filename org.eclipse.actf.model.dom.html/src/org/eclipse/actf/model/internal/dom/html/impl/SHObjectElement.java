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
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLObjectElement;

@SuppressWarnings("nls")
public class SHObjectElement extends SHElement implements HTMLObjectElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1704784977052946752L;

	protected SHObjectElement(String tagName, SHDocument doc) {
		super(tagName, doc);
	}

	public HTMLFormElement getForm() {
		for (Node ret = getParentNode(); ret != null; ret = ret.getParentNode()) {
			if (ret instanceof HTMLFormElement)
				return (HTMLFormElement) ret;
		}
		return null;
	}

	public String getCode() {
		return getAttribute("code");
	}

	public void setCode(String code) {
		setAttribute("code", code);
	}

	public String getAlign() {
		return getAttribute("align");
	}

	public void setAlign(String align) {
		setAttribute("align", align);
	}

	public String getArchive() {
		return getAttribute("archive");
	}

	public void setArchive(String archive) {
		setAttribute("archive", archive);
	}

	public String getBorder() {
		return getAttribute("border");
	}

	public void setBorder(String border) {
		setAttribute("border", border);
	}

	public String getCodeBase() {
		return getAttribute("codebase");
	}

	public void setCodeBase(String codeBase) {
		setAttribute("codebase", codeBase);
	}

	public String getCodeType() {
		return getAttribute("codetype");
	}

	public void setCodeType(String codeType) {
		setAttribute("codetype", codeType);
	}

	public String getData() {
		return getAttribute("data");
	}

	public void setData(String data) {
		setAttribute("data", data);
	}

	public boolean getDeclare() {
		String val = getAttribute("declare");
		return val != null && val.length() != 0;
	}

	public void setDeclare(boolean declare) {
		setAttribute("declare", declare ? "declare" : null);
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

	public String getStandby() {
		return getAttribute("standby");
	}

	public void setStandby(String standby) {
		setAttribute("standby", standby);
	}

	/**
	 * @exception NumberFormatException
	 */
	public int getTabIndex() {
		return Integer.parseInt(getAttribute("tabindex"));
	}

	public void setTabIndex(int tabIndex) {
		setAttribute("tabindex", Integer.toString(tabIndex));
	}

	public String getType() {
		return getAttribute("type");
	}

	public void setType(String type) {
		setAttribute("type", type);
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
		setAttribute("vsapce", vspace);
	}

	public String getWidth() {
		return getAttribute("width");
	}

	public void setWidth(String width) {
		setAttribute("width", width);
	}

	private Document contentDoc;

	public Document getContentDocument() {
		return contentDoc;
	}

	/**
	 * Sets the content document this object contains.
	 */
	public void setContentDocument(Document contentDoc) {
		this.contentDoc = contentDoc;
	}
}
