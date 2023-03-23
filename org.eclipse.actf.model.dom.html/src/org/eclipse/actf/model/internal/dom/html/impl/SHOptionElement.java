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
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLOptionElement;

@SuppressWarnings("nls")
public class SHOptionElement extends SHElement implements HTMLOptionElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3164490828413522255L;

	protected SHOptionElement(String tagName, SHDocument doc) {
		super(tagName, doc);
	}

	public HTMLFormElement getForm() {
		for (Node ret = getParentNode(); ret != null; ret = ret.getParentNode()) {
			if (ret instanceof HTMLFormElement)
				return (HTMLFormElement) ret;
		}
		return null;
	}

	private boolean defaultSelected;

	public boolean getDefaultSelected() {
		return defaultSelected;
	}

	public void setDefaultSelected(boolean defaultSelected) {
		this.defaultSelected = defaultSelected;
	}

	public String getText() {
		Node child = getFirstChild();
		return child == null ? null : child.getNodeValue();
	}

	/**
	 * @return if a SELECT node is not found in ancester, -1.
	 */
	public int getIndex() {
		SHSelectElement sel;
		for (Node anc = getParentNode(); anc != null; anc = anc.getParentNode()) {
			if (anc instanceof SHSelectElement) {
				sel = (SHSelectElement) anc;
				HTMLCollection options = sel.getOptions();
				for (int i = 0; i < options.getLength(); i++) {
					if (options.item(i) == this) {
						return i;
					}
				}
				return -1;
			}
		}
		return -1;
	}

	public void setIndex(int index) {
		SHSelectElement sel;
		for (Node anc = getParentNode(); anc != null; anc = anc.getParentNode()) {
			if (anc instanceof SHSelectElement) {
				sel = (SHSelectElement) anc;
				HTMLCollection options = sel.getOptions();
				if (index < 0 || options.getLength() <= index)
					return;
				SHOptionElement nextSibling = (SHOptionElement) options
						.item(index);
				Node newParent = nextSibling.getParentNode();
				getParentNode().removeChild(this);
				newParent.insertBefore(this, nextSibling);
			}
		}
	}

	public boolean getSelected() {
		String val = getAttribute("selected");
		return val != null && val.length() != 0;
	}

	public void setSelected(boolean selected) {
		setAttribute("selected", selected ? "selected" : null);
	}

	public boolean getDisabled() {
		String val = getAttribute("disabled");
		return val != null && val.length() != 0;
	}

	public void setDisabled(boolean disabled) {
		setAttribute("disabled", disabled ? "disabled" : null);
	}

	public String getLabel() {
		return getAttribute("label");
	}

	public void setLabel(String label) {
		setAttribute("label", label);
	}

	public String getValue() {
		return getAttribute("value");
	}

	public void setValue(String value) {
		setAttribute("value", value);
	}
}
