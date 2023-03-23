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

import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLButtonElement;
import org.w3c.dom.html.HTMLFormElement;

@SuppressWarnings("nls")
public class SHButtonElement extends SHElement implements HTMLButtonElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 767270667997721051L;

	protected SHButtonElement(String tagName, SHDocument doc) {
		super(tagName, doc);
	}

	public HTMLFormElement getForm() {
		for (Node ret = getParentNode(); ret != null; ret = ret.getParentNode()) {
			if (ret instanceof HTMLFormElement)
				return (HTMLFormElement) ret;
		}
		return null;
	}

	public String getAccessKey() {
		return getAttribute("accesskey");
	}

	public void setAccessKey(String accessKey) {
		setAttribute("accesskey", accessKey);
	}

	public boolean getDisabled() {
		String val = getAttribute("disabled");
		return val != null && val.length() != 0;
	}

	public void setDisabled(boolean disabled) {
		setAttribute("disabled", disabled ? "disabled" : null);
	}

	public String getName() {
		return getAttribute("name");
	}

	public void setName(String name) {
		setAttribute("name", name);
	}

	/**
	 * @exception NumberFormatException
	 */
	public int getTabIndex() {
		return Integer.parseInt(getAttribute("tabindex"));
	}

	public void setTabIndex(int tabIndex) {
		setAttribute("tabIndex", Integer.toString(tabIndex));
	}

	public String getType() {
		return getAttribute("type");
	}

	public String getValue() {
		return getAttribute("value");
	}

	public void setValue(String value) {
		setAttribute("value", value);
	}
}
