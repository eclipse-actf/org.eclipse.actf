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

import org.w3c.dom.html.HTMLParamElement;

@SuppressWarnings("nls")
public class SHParamElement extends SHElement implements HTMLParamElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3769863277214472353L;

	protected SHParamElement(String name, SHDocument doc) {
		super(name, doc);
	}

	public String getName() {
		return getAttribute("name");
	}

	public void setName(String name) {
		setAttribute("name", name);
	}

	public String getType() {
		return getAttribute("type");
	}

	public void setType(String type) {
		setAttribute("type", type);
	}

	public String getValue() {
		return getAttribute("value");
	}

	public void setValue(String value) {
		setAttribute("value", value);
	}

	public String getValueType() {
		return getAttribute("valuetype");
	}

	public void setValueType(String valueType) {
		setAttribute("valuetype", valueType);
	}
}
