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
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableSectionElement;

@SuppressWarnings("nls")
public class SHTableSectionElement extends SHElement implements
		HTMLTableSectionElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6890717929157559373L;

	protected SHTableSectionElement(String name, SHDocument doc) {
		super(name, doc);
	}

	public String getAlign() {
		return getAttribute("align");
	}

	public void setAlign(String align) {
		setAttribute("align", align);
	}

	public String getCh() {
		return getAttribute("char");
	}

	public void setCh(String ch) {
		setAttribute("char", ch);
	}

	public String getChOff() {
		return getAttribute("charoff");
	}

	public void setChOff(String chOff) {
		setAttribute("charoff", chOff);
	}

	public String getVAlign() {
		return getAttribute("valign");
	}

	public void setVAlign(String vAlign) {
		setAttribute("valign", vAlign);
	}

	public HTMLCollection getRows() {
		int len = 0;
		HTMLTableRowElement rows[] = new HTMLTableRowElement[8];
		for (Node child = getFirstChild(); child != null; child = child
				.getNextSibling()) {
			if (child instanceof HTMLTableRowElement) {
				if (rows.length == len) {
					HTMLTableRowElement buf[] = new HTMLTableRowElement[len * 2];
					System.arraycopy(rows, 0, buf, 0, len);
					rows = buf;
				}
				rows[len++] = (HTMLTableRowElement) child;
			}
		}
		return ((SHDocument) getOwnerDocument()).createCollection(rows, len);
	}

	/**
	 * inserts a new row element whose tag name is uppercased.
	 */
	public HTMLElement insertRow(int index) {
		if (index < 0)
			return null;
		int i = 0;
		for (Node after = getFirstChild(); after != null; after = after
				.getNextSibling()) {
			if (i == index) {
				HTMLElement ret = (HTMLElement) getOwnerDocument()
						.createElement("TR");
				insertBefore(ret, after);
				return ret;
			} else {
				i++;
			}
		}
		if (i == 0) {
			HTMLElement ret = (HTMLElement) getOwnerDocument().createElement(
					"TR");
			insertBefore(ret, null);
			return ret;
		}
		return null;
	}

	public void deleteRow(int index) {
		if (index < 0)
			return;
		int i = 0;
		for (Node child = getFirstChild(); child != null; child = child
				.getNextSibling()) {
			if (i == index) {
				removeChild(child);
				return;
			} else {
				i++;
			}
		}
		return;
	}
}
