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
import org.w3c.dom.html.HTMLTableCaptionElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableSectionElement;

@SuppressWarnings("nls")
public class SHTableElement extends SHElement implements HTMLTableElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4310847532198779587L;

	protected SHTableElement(String name, SHDocument doc) {
		super(name, doc);
	}

	public HTMLTableCaptionElement getCaption() {
		for (Node ret = getFirstChild(); ret != null; ret = ret
				.getNextSibling()) {
			if (ret instanceof HTMLTableCaptionElement) {
				return (HTMLTableCaptionElement) ret;
			}
		}
		return null;
	}

	public void setCaption(HTMLTableCaptionElement caption) {
		insertBefore(caption, getFirstChild());
	}

	public HTMLTableSectionElement getTHead() {
		for (Node ret = getFirstChild(); ret != null; ret = ret
				.getNextSibling()) {
			if (ret instanceof HTMLTableSectionElement
					&& ret.getNodeName().equalsIgnoreCase("THEAD")) {
				return (HTMLTableSectionElement) ret;
			}
		}
		return null;
	}

	public void setTHead(HTMLTableSectionElement tHead) {
		for (Node after = getFirstChild(); after != null; after = after
				.getNextSibling()) {
			if (after instanceof HTMLTableSectionElement) {
				insertBefore(tHead, after);
			}
		}
		insertBefore(tHead, getLastChild());
	}

	public HTMLTableSectionElement getTFoot() {
		for (Node ret = getFirstChild(); ret != null; ret = ret
				.getNextSibling()) {
			if (ret instanceof HTMLTableSectionElement
					&& ret.getNodeName().equalsIgnoreCase("TFOOT")) {
				return (HTMLTableSectionElement) ret;
			}
		}
		return null;
	}

	public void setTFoot(HTMLTableSectionElement tFoot) {
		for (Node after = getFirstChild(); after != null; after = after
				.getNextSibling()) {
			if (after instanceof HTMLTableSectionElement
					&& after.getNodeName().equalsIgnoreCase("TBODY")) {
				insertBefore(tFoot, after);
			}
		}
		insertBefore(tFoot, getLastChild());
	}

	public HTMLCollection getRows() {
		int len = 0;
		HTMLTableRowElement rows[] = new HTMLTableRowElement[16];
		for (Node child = getFirstChild(); child != null; child = child
				.getNextSibling()) {
			if (child instanceof HTMLTableSectionElement) {
				for (Node row = child.getFirstChild(); row != null; row = row
						.getNextSibling()) {
					if (row instanceof HTMLTableRowElement) {
						if (rows.length == len) {
							HTMLTableRowElement buf[] = new HTMLTableRowElement[len * 2];
							System.arraycopy(rows, 0, buf, 0, len);
							rows = buf;
						}
						rows[len++] = (HTMLTableRowElement) row;
					}
				}
			} else if (child instanceof HTMLTableRowElement) {
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

	public HTMLCollection getTBodies() {
		int len = 0;
		HTMLTableSectionElement tbodies[] = new HTMLTableSectionElement[16];
		for (Node child = getFirstChild(); child != null; child = child
				.getNextSibling()) {
			if (child instanceof HTMLTableSectionElement
					&& child.getNodeName().equalsIgnoreCase("TBODY")) {
				if (tbodies.length == len) {
					HTMLTableSectionElement buf[] = new HTMLTableSectionElement[len * 2];
					System.arraycopy(tbodies, 0, buf, 0, len);
					tbodies = buf;
				}
				tbodies[len++] = (HTMLTableSectionElement) child;
			}
		}
		return ((SHDocument) getOwnerDocument()).createCollection(tbodies, len);
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

	public String getBorder() {
		return getAttribute("border");
	}

	public void setBorder(String border) {
		setAttribute("border", border);
	}

	public String getCellPadding() {
		return getAttribute("cellpadding");
	}

	public void setCellPadding(String cellPadding) {
		setAttribute("cellpadding", cellPadding);
	}

	public String getCellSpacing() {
		return getAttribute("cellspacing");
	}

	public void setCellSpacing(String cellSpacing) {
		setAttribute("cellspacing", cellSpacing);
	}

	public String getFrame() {
		return getAttribute("frame");
	}

	public void setFrame(String frame) {
		setAttribute("frame", frame);
	}

	public String getRules() {
		return getAttribute("rules");
	}

	public void setRules(String rules) {
		setAttribute("rules", rules);
	}

	public String getSummary() {
		return getAttribute("summary");
	}

	public void setSummary(String summary) {
		setAttribute("summary", summary);
	}

	public String getWidth() {
		return getAttribute("width");
	}

	public void setWidth(String width) {
		setAttribute("width", width);
	}

	/**
	 * creates a THEAD element whose tagname is uppercased.
	 */
	public HTMLElement createTHead() {
		return (HTMLElement) getOwnerDocument().createElement("THEAD");
	}

	public void deleteTHead() {
		for (Node tHead = getFirstChild(); tHead != null; tHead = tHead
				.getNextSibling()) {
			if (tHead instanceof HTMLTableSectionElement
					&& tHead.getNodeName().equalsIgnoreCase("THEAD")) {
				removeChild(tHead);
				return;
			}
		}
	}

	/**
	 * creates a TFOOT element whose tagname is uppercased.
	 */
	public HTMLElement createTFoot() {
		return (HTMLElement) getOwnerDocument().createElement("TFOOT");
	}

	public void deleteTFoot() {
		for (Node tFoot = getFirstChild(); tFoot != null; tFoot = tFoot
				.getNextSibling()) {
			if (tFoot instanceof HTMLTableSectionElement
					&& tFoot.getNodeName().equalsIgnoreCase("TFOOT")) {
				removeChild(tFoot);
				return;
			}
		}
	}

	/**
	 * creates a CAPTION element whose tagname is uppercased.
	 */
	public HTMLElement createCaption() {
		return (HTMLElement) getOwnerDocument().createElement("CAPTION");
	}

	public void deleteCaption() {
		for (Node caption = getFirstChild(); caption != null; caption = caption
				.getNextSibling()) {
			if (caption instanceof HTMLTableSectionElement
					&& caption.getNodeName().equalsIgnoreCase("CAPTION")) {
				removeChild(caption);
				return;
			}
		}
	}

	/**
	 * inserts a new empty row whose tagname is uppercased.
	 */
	public HTMLElement insertRow(int index) {
		if (index < 0)
			return null;
		HTMLCollection rows = getRows();
		if (rows.getLength() <= index) {
			return null;
		} else {
			Node after = rows.item(index);
			Node parent = after.getParentNode();
			HTMLElement ret = (HTMLElement) getOwnerDocument().createElement(
					"TR");
			parent.insertBefore(ret, after);
			return ret;
		}
	}

	public void deleteRow(int index) {
		if (index < 0)
			return;
		HTMLCollection rows = getRows();
		if (rows.getLength() <= index)
			return;
		Node row = rows.item(index);
		row.getParentNode().removeChild(row);
	}
}
