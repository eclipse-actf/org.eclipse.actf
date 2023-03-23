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
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableRowElement;

@SuppressWarnings("nls")
public class SHTableRowElement extends SHElement implements HTMLTableRowElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8399058496811877036L;

	protected SHTableRowElement(String name, SHDocument doc) {
		super(name, doc);
	}

	public int getRowIndex() {
		Node table = getParentNode().getParentNode();
		if (table instanceof HTMLTableElement) {
			HTMLCollection rows = ((HTMLTableElement) table).getRows();
			for (int ret = 0; ret < rows.getLength(); ret++) {
				if (rows.item(ret) == this)
					return ret;
			}
			throw new RuntimeException("Internal DOM Implementation Error.");
		} else { // invalid dom structure.
			int ret = 0;
			for (Node prev = getPreviousSibling(); prev != null; prev = prev
					.getPreviousSibling()) {
				if (prev instanceof HTMLTableRowElement)
					ret++;
			}
			return ret;
		}
	}

	public void setRowIndex(int rowIndex) {
		if (rowIndex < 0)
			return;
		Node table = getParentNode().getParentNode();
		if (table instanceof HTMLTableElement) {
			getParentNode().removeChild(this);
			HTMLCollection rows = ((HTMLTableElement) table).getRows();
			if (rows.getLength() < rowIndex)
				return;
			Node after = rows.item(rowIndex);
			Node newParent = after.getParentNode();
			newParent.insertBefore(this, after);
		} else { // invalid dom structure.
			Node after = getParentNode().getFirstChild();
			int count = 0;
			while (count < rowIndex && after != null) {
				if (after instanceof HTMLTableRowElement) {
					count++;
				}
				after = after.getNextSibling();
			}
			if (after == null)
				return;
			getParentNode().removeChild(this);
			after.getParentNode().insertBefore(this, after);
		}
	}

	public int getSectionRowIndex() {
		int ret = 0;
		for (Node prev = getPreviousSibling(); prev != null; prev = prev
				.getPreviousSibling()) {
			if (prev instanceof HTMLTableRowElement) {
				ret++;
			}
		}
		return ret;
	}

	public void setSectionRowIndex(int sectionRowIndex) {
		if (sectionRowIndex < 0)
			return;
		Node after = getParentNode().getFirstChild();
		int count = 0;
		while (count < sectionRowIndex && after != null) {
			if (after instanceof HTMLTableRowElement) {
				count++;
			}
			after = after.getNextSibling();
		}
		if (after == null)
			return;
		getParentNode().removeChild(this);
		after.getParentNode().insertBefore(this, after);
	}

	public HTMLCollection getCells() {
		int len = 0;
		HTMLTableCellElement cells[] = new HTMLTableCellElement[8];
		for (Node child = getFirstChild(); child != null; child = child
				.getNextSibling()) {
			if (child instanceof HTMLTableCellElement) {
				if (cells.length == len) {
					HTMLTableCellElement buf[] = new HTMLTableCellElement[len * 2];
					System.arraycopy(cells, 0, buf, 0, len);
					cells = buf;
				}
				cells[len++] = (HTMLTableCellElement) child;
			}
		}
		return ((SHDocument) getOwnerDocument()).createCollection(cells, len);
	}

	public void setCells(HTMLCollection cells) {
		while (hasChildNodes())
			removeChild(getFirstChild());
		for (int i = 0; i < cells.getLength(); i++) {
			insertBefore(cells.item(i), null);
		}
	}

	public String getAlign() {
		return getAttribute("align");
	}

	public void setAlign(String align) {
		setAttribute("align", align);
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

	public String getVAlign() {
		return getAttribute("valing");
	}

	public void setVAlign(String vAlign) {
		setAttribute("valing", vAlign);
	}

	/**
	 * Insert an empty <code>TD</code> cell whose tagname is uppercased into
	 * this row.
	 */
	public HTMLElement insertCell(int index) {
		if (index < 0)
			return null;
		Node after = getFirstChild();
		int i = 0;
		while (i < index && after != null) {
			after = after.getNextSibling();
			i++;
		}
		if (after == null && i != 0)
			return null;
		HTMLElement ret = (HTMLElement) getOwnerDocument().createElement("TD");
		insertBefore(ret, after);
		return ret;
	}

	public void deleteCell(int index) {
		if (index < 0)
			return;
		Node target = getFirstChild();
		for (int i = 0; i < index; i++) {
			if (target == null)
				return;
		}
		removeChild(target);
	}
}
