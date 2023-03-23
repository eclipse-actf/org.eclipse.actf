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

import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLOptionElement;
import org.w3c.dom.html.HTMLSelectElement;

@SuppressWarnings("nls")
public class SHSelectElement extends SHFormCtrlElement implements
		HTMLSelectElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7615986491286782573L;

	protected SHSelectElement(String name, SHDocument doc) {
		super(name, doc);
	}

	public String getType() {
		return getAttribute("type");
	}

	public int getSelectedIndex() {
		NodeList options = getElementsByTagName("OPTION");
		for (int i = 0; i < options.getLength(); i++) {
			if (((SHOptionElement) options.item(i)).getSelected())
				return i;
		}
		return -1;
	}

	public void setSelectedIndex(int selectedIndex) {
		NodeList options = getElementsByTagName("OPTION");
		if (selectedIndex < 0 || options.getLength() <= selectedIndex)
			return;
		((SHOptionElement) options.item(selectedIndex)).setAttribute(
				"selected", "selected");
	}

	public String getValue() {
		NodeList options = getElementsByTagName("OPTION");
		for (int i = 0; i < options.getLength(); i++) {
			SHOptionElement opt = (SHOptionElement) options.item(i);
			if (opt.getSelected()) {
				return opt.getValue();
			}
		}
		return null;
	}

	public void setValue(String value) {
		NodeList options = getElementsByTagName("OPTION");
		for (int i = 0; i < options.getLength(); i++) {
			SHOptionElement opt = (SHOptionElement) options.item(i);
			if (opt.getValue().equals(value)) {
				opt.setAttribute("selected", "selected");
			}
		}
	}

	public int getLength() {
		return getElementsByTagName("OPTION").getLength();
	}

	public HTMLCollection getOptions() {
		NodeList options = getElementsByTagName("OPTION");
		return ((SHDocument) getOwnerDocument()).createCollection(options);
	}

	public boolean getMultiple() {
		String mul = getAttribute("multiple");
		return mul != null && mul.length() != 0;
	}

	public void setMultiple(boolean multiple) {
		setAttribute("multiple", multiple ? "multiple" : null);
	}

	/**
	 * @exception NumberFormatException
	 */
	public int getSize() {
		String size = getAttribute("size");
		if (size != null && size.length() > 0) {
			return Integer.parseInt(size);
		} else {
			return -1;
		}
	}

	public void setSize(int size) {
		setAttribute("size", Integer.toString(size));
	}

	public void add(HTMLElement element, HTMLElement before) {
		if (before == null) {
			insertBefore(element, getFirstChild());
		} else {
			insertBefore(element, before);
		}
	}

	public void remove(int index) {
		NodeList options = getElementsByTagName("OPTION");
		if (index < 0 || options.getLength() <= index)
			return;
		HTMLOptionElement opt = (HTMLOptionElement) options.item(index);
		opt.getParentNode().removeChild(opt);
	}

	/**
	 * Does nothing.
	 */
	public void blur() {
	}

	/**
	 * does nothing.
	 */
	public void focus() {
	}
}
