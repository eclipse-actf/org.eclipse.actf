/*******************************************************************************
 * Copyright (c) 1998, 2016 IBM Corporation and Others
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
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableRowElement;

@SuppressWarnings("nls")
public class SHTableCellElement extends SHElement implements HTMLTableCellElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4538969444602022762L;

	protected SHTableCellElement(String name, SHDocument doc) {
		super(name, doc);
	}

	public int getCellIndex() {
		Node parent = getParentNode();
		if (parent instanceof HTMLTableRowElement) {
			int ret = 0;
			for (Node prev = getPreviousSibling(); prev != null; prev = prev.getPreviousSibling())
				ret++;
			return ret;
		} else { // error.
			int ret = 0;
			for (Node prev = getPreviousSibling(); prev != null
					&& !(prev instanceof HTMLTableRowElement); prev = prev.getPreviousSibling())
				ret++;
			return ret;
		}
	}

	public void setCellIndex(int cellIndex) {
		if (cellIndex < 0)
			return;
		Node parent = getParentNode();
		if (parent instanceof HTMLTableRowElement) {
			parent.removeChild(this);
			NodeList siblings = parent.getChildNodes();
			if (siblings.getLength() <= cellIndex) {
				parent.insertBefore(this, null);
			} else {
				parent.insertBefore(this, siblings.item(cellIndex));
			}
		} else { // error.
			Node leftMost = this;
			// int currentIndex = 0;
			for (Node prev = getPreviousSibling(); prev != null
					&& !(prev instanceof HTMLTableRowElement); prev = prev.getPreviousSibling()) {
				leftMost = prev;
				// currentIndex++;
			}
			parent.removeChild(this);
			Node before = leftMost;
			for (int i = 0; i < cellIndex; i++) {
				Node next = before.getNextSibling();
				if (next == null || next instanceof HTMLTableRowElement) { // out
																			// of
																			// bounds
					parent.insertBefore(this, null);
					return;
				} else {
					before = before.getNextSibling();
				}
			}
			parent.insertBefore(this, before);
		}
	}

	public String getAbbr() {
		return getAttribute("abbr");
	}

	public void setAbbr(String abbr) {
		setAttribute("abbr", abbr);
	}

	public String getAlign() {
		return getAttribute("align");
	}

	public void setAlign(String align) {
		setAttribute("aling", align);
	}

	public String getAxis() {
		return getAttribute("axis");
	}

	public void setAxis(String axis) {
		setAttribute("axis", axis);
	}

	public String getBgColor() {
		return getAttribute("bgcolor");
	}

	public void setBgColor(String bgColor) {
		setAttribute("bgcolor", bgColor);
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

	/**
	 * @exception NumberFormatException
	 */
	public int getColSpan() {
		return Integer.parseInt(getAttribute("colspan"));
	}

	public void setColSpan(int colSpan) {
		setAttribute("colspan", Integer.toString(colSpan));
	}

	public String getHeaders() {
		return getAttribute("headers");
	}

	public void setHeaders(String headers) {
		setAttribute("headers", headers);
	}

	public String getHeight() {
		return getAttribute("height");
	}

	public void setHeight(String height) {
		setAttribute("height", height);
	}

	public boolean getNoWrap() {
		String val = getAttribute("nowrap");
		return val != null && val.length() != 0;
	}

	public void setNoWrap(boolean noWrap) {
		setAttribute("nowrap", noWrap ? "nowrap" : null);
	}

	/**
	 * @exception NumberFormatException
	 */
	public int getRowSpan() {
		return Integer.parseInt(getAttribute("rowspan"));
	}

	public void setRowSpan(int rowSpan) {
		setAttribute("rowspan", Integer.toString(rowSpan));
	}

	public String getScope() {
		return getAttribute("scope");
	}

	public void setScope(String scope) {
		setAttribute("scope", scope);
	}

	public String getVAlign() {
		return getAttribute("valign");
	}

	public void setVAlign(String vAlign) {
		setAttribute("valign", vAlign);
	}

	public String getWidth() {
		return getAttribute("width");
	}

	public void setWidth(String width) {
		setAttribute("width", width);
	}
}
