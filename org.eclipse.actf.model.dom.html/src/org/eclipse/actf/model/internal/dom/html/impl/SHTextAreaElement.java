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

import org.w3c.dom.html.HTMLTextAreaElement;

@SuppressWarnings("nls")
public class SHTextAreaElement extends SHFormCtrlElement implements
		HTMLTextAreaElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3414443046158548810L;

	protected SHTextAreaElement(String tagName, SHDocument doc) {
		super(tagName, doc);
	}

	private String defaultValue;

	public String getDefaultValue() {
		return this.defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getAccessKey() {
		return getAttribute("accesskey");
	}

	public void setAccessKey(String accessKey) {
		setAttribute("accesskey", accessKey);
	}

	/**
	 * @exception NumberFormatException
	 */
	public int getCols() {
		return Integer.parseInt(getAttribute("cols"));
	}

	public void setCols(int cols) {
		setAttribute("cols", Integer.toString(cols));
	}

	public boolean getReadOnly() {
		String val = getAttribute("readonly");
		return val != null && val.length() != 0;
	}

	public void setReadOnly(boolean readOnly) {
		setAttribute("readonly", readOnly ? "readonly" : null);
	}

	public int getRows() {
		return Integer.parseInt(getAttribute("rows"));
	}

	public void setRows(int rows) {
		setAttribute("rows", Integer.toString(rows));
	}

	public String getType() {
		return "textarea";
	}

	private String value;

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Does nothing. Override this method.
	 */
	public void blur() {
	}

	/**
	 * Does nothing. Override this method.
	 */
	public void focus() {
	}

	/**
	 * Does nothing. Override this method.
	 */
	public void select() {
	}

}
