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

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.impl.ODFStylableElementImpl;
import org.eclipse.actf.model.dom.odf.content.IStyleListener;
import org.eclipse.actf.model.dom.odf.style.StyleElement;
import org.eclipse.actf.model.dom.odf.table.TableColumnElement;
import org.eclipse.actf.model.dom.odf.table.TableConstants;
import org.eclipse.actf.model.dom.odf.table.TableElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


class TableColumnElementImpl extends ODFStylableElementImpl implements
		TableColumnElement {
	private static final long serialVersionUID = -6481432496309846401L;

	protected TableColumnElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public String getAttrTableStyleName() {
		if (hasAttributeNS(TableConstants.TABLE_NAMESPACE_URI,
				TableConstants.ATTR_STYLE_NAME))
			return getAttributeNS(TableConstants.TABLE_NAMESPACE_URI,
					TableConstants.ATTR_STYLE_NAME);
		return null;
	}

	public int getAttrTableNumberColumnsRepeated() {
		if (hasAttributeNS(TableConstants.TABLE_NAMESPACE_URI,
				TableConstants.ATTR_NUMBER_COLUMNS_REPEATED)) {
			return new Integer(getAttributeNS(
					TableConstants.TABLE_NAMESPACE_URI,
					TableConstants.ATTR_NUMBER_COLUMNS_REPEATED)).intValue();
		}
		return -1;
	}

	public String getAttrTableDefaultCellStyleName() {
		if (hasAttributeNS(TableConstants.TABLE_NAMESPACE_URI,
				TableConstants.ATTR_DEFAULT_CELL_STYLE_NAME))
			return getAttributeNS(TableConstants.TABLE_NAMESPACE_URI,
					TableConstants.ATTR_DEFAULT_CELL_STYLE_NAME);
		return null;
	}

	public int getTableIndex() {
		TableElement table = getTableElement();
		if (table == null)
			return -1;
		return table.getTableIndex();
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

	public int getTableColumnIndex() {
		int columnStartIndex = 0;
		NodeList list = getTableElement().getElementsByTagNameNS(
				TableConstants.TABLE_NAMESPACE_URI, TableConstants.ELEMENT_TABLE_COLUMN);
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).equals(this)) {
				return columnStartIndex;
			}
			if (((TableColumnElement) list.item(i)).hasAttributeNS(
					TableConstants.TABLE_NAMESPACE_URI,
					TableConstants.ATTR_NUMBER_COLUMNS_REPEATED)) {
				columnStartIndex += ((TableColumnElement) list.item(i))
						.getAttrTableNumberColumnsRepeated();
			} else {
				columnStartIndex++;
			}
		}
		return -1;
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
}