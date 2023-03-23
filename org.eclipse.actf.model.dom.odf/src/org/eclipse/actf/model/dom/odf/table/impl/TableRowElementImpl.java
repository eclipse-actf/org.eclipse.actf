/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tatsuya ISHIHARA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.model.dom.odf.table.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.impl.ODFStylableElementImpl;
import org.eclipse.actf.model.dom.odf.content.IStyleListener;
import org.eclipse.actf.model.dom.odf.range.ITextElementContainer;
import org.eclipse.actf.model.dom.odf.range.impl.ITextElementContainerUtil;
import org.eclipse.actf.model.dom.odf.style.StyleElement;
import org.eclipse.actf.model.dom.odf.table.TableCellElement;
import org.eclipse.actf.model.dom.odf.table.TableConstants;
import org.eclipse.actf.model.dom.odf.table.TableElement;
import org.eclipse.actf.model.dom.odf.table.TableRowElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class TableRowElementImpl extends ODFStylableElementImpl implements
		TableRowElement {
	private static final long serialVersionUID = 4515268211599720829L;

	protected TableRowElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public String getAttrTableStyleName() {
		if (hasAttributeNS(TableConstants.TABLE_NAMESPACE_URI,
				TableConstants.ATTR_STYLE_NAME))
			return getAttributeNS(TableConstants.TABLE_NAMESPACE_URI,
					TableConstants.ATTR_STYLE_NAME);
		return null;
	}

	public int getAttrTableNumberRowsRepeated() {
		if (hasAttributeNS(TableConstants.TABLE_NAMESPACE_URI,
				TableConstants.ATTR_NUMBER_ROWS_REPEATED)) {
			return new Integer(getAttributeNS(
					TableConstants.TABLE_NAMESPACE_URI,
					TableConstants.ATTR_NUMBER_ROWS_REPEATED)).intValue();
		}
		return -1;
	}

	public int getTableIndex() {
		TableElement tableElement = getTableElement();
		return tableElement.getTableIndex();
	}

	public int getRowIndex() {
		TableElement tableElement = getTableElement();
		if (tableElement == null)
			return -1;

		NodeList list = tableElement.getElementsByTagNameNS(
				TableConstants.TABLE_NAMESPACE_URI, TableConstants.ELEMENT_TABLE_ROW);
		if (list == null)
			return -1;

		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).equals(this)) {
				return i;
			}
		}
		return -1;
	}

	public TableElement getTableElement() {
		TableElement tableElement = null;

		Node parent = getParentNode();
		while (parent != null) {
			if (parent instanceof TableElement) {
				tableElement = (TableElement) parent;
				break;
			}
			parent = parent.getParentNode();
		}

		return tableElement;
	}

	public List<TableCellElement> getTableCellChildren() {
		List<TableCellElement> cellList = null;

		NodeList children = getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			if (children.item(i) instanceof TableCellElement) {
				if (cellList == null)
					cellList = new Vector<TableCellElement>();
				cellList.add((TableCellElement) children.item(i));
			}
		}

		return cellList;
	}

	public void setStyle(StyleElement style) {
		// TODO Auto-generated method stub

	}

	public StyleElement getStyle() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addStyleListener(IStyleListener listener, String topic) {
		// TODO Auto-generated method stub

	}

	public void removeStyleListener(IStyleListener listener, String topic) {
		// TODO Auto-generated method stub

	}

	public long getContentSize() {
		return ITextElementContainerUtil.getContentSize(this);
	}

	public Iterator<ITextElementContainer> getChildIterator() {
		return ITextElementContainerUtil.getChildIterator(this);
	}
}