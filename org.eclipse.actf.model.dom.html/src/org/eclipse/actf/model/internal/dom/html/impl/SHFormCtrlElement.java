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
import org.w3c.dom.html.HTMLFormElement;

@SuppressWarnings("nls")
abstract class SHFormCtrlElement extends SHElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3729153275976107942L;

	SHFormCtrlElement(String name, SHDocument doc) {
		super(name, doc);
	}

	public HTMLFormElement getForm() {
		for (Node ret = getParentNode(); ret != null; ret = ret.getParentNode()) {
			if (ret instanceof HTMLFormElement)
				return (HTMLFormElement) ret;
		}
		return null;
	}

	public String getName() {
		return getAttribute("name");
	}

	public void setName(String name) {
		setAttribute("name", name);
	}

	public boolean getDisabled() {
		String val = getAttribute("disabled");
		return val != null && val.length() != 0;
	}

	public void setDisabled(boolean disabled) {
		setAttribute("disabled", disabled ? "disabled" : null);
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
}
