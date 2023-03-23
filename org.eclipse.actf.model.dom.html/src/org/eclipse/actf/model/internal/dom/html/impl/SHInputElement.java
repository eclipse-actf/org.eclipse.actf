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

import org.w3c.dom.html.HTMLInputElement;

@SuppressWarnings("nls")
public class SHInputElement extends SHFormCtrlElement implements
		HTMLInputElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4263591577916637321L;

	protected SHInputElement(String tagName, SHDocument doc) {
		super(tagName, doc);
	}

	private String defaultValue;

	public String getDefaultValue() {
		return this.defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	private boolean defaultChecked;

	public boolean getDefaultChecked() {
		return this.defaultChecked;
	}

	public void setDefaultChecked(boolean defaultChecked) {
		this.defaultChecked = defaultChecked;
	}

	public String getAccept() {
		return getAttribute("accept");
	}

	public void setAccept(String accept) {
		setAttribute("accept", accept);
	}

	public String getAccessKey() {
		return getAttribute("accesskey");
	}

	public void setAccessKey(String accessKey) {
		setAttribute("accesskey", accessKey);
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

	public boolean getChecked() {
		String val = getAttribute("checked");
		return val != null && val.length() != 0;
	}

	public void setChecked(boolean checked) {
		setAttribute("checked", checked ? "checked" : null);
	}

	/**
	 * @exception NumberFormatException
	 */
	public int getMaxLength() {
		return Integer.parseInt(getAttribute("maxlength"));
	}

	public void setMaxLength(int maxLength) {
		setAttribute("maxlength", Integer.toString(maxLength));
	}

	public boolean getReadOnly() {
		String val = getAttribute("readonly");
		return val != null && val.length() != 0;
	}

	public void setReadOnly(boolean readOnly) {
		setAttribute("readonly", readOnly ? "readonly" : null);
	}

	public String getSize() {
		return getAttribute("size");
	}

	public void setSize(String size) {
		setAttribute("size", size);
	}

	public String getSrc() {
		return getAttribute("src");
	}

	public void setSrc(String src) {
		setAttribute("src", src);
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

	public String getValue() {
		return getAttribute("value");
	}

	public void setValue(String value) {
		setAttribute("value", value);
	}

	/**
	 * does nothing.
	 */
	public void blur() {
	}

	/**
	 * does nothing.
	 */
	public void focus() {
	}

	/**
	 * does nothing.
	 */
	public void select() {
	}

	/**
	 * does nothing.
	 */
	public void click() {
	}
}
