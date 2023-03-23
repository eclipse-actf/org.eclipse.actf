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

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.base.impl.ODFElementImpl;
import org.eclipse.actf.model.dom.odf.base.impl.ODFStylableElementImpl;
import org.eclipse.actf.model.dom.odf.content.IStyleListener;
import org.eclipse.actf.model.dom.odf.office.OfficeConstants;
import org.eclipse.actf.model.dom.odf.range.ITextElementContainer;
import org.eclipse.actf.model.dom.odf.range.impl.ITextElementContainerUtil;
import org.eclipse.actf.model.dom.odf.style.StyleConstants;
import org.eclipse.actf.model.dom.odf.style.StyleElement;
import org.eclipse.actf.model.dom.odf.table.TableCellElement;
import org.eclipse.actf.model.dom.odf.table.TableConstants;
import org.eclipse.actf.model.dom.odf.table.TableElement;
import org.eclipse.actf.model.dom.odf.table.TableRowElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

class TableCellElementImpl extends ODFStylableElementImpl implements
		TableCellElement {
	private static final long serialVersionUID = -4617536939760820397L;

	protected TableCellElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public String getAttrTableStyleName() {
		if (hasAttributeNS(TableConstants.TABLE_NAMESPACE_URI,
				TableConstants.ATTR_STYLE_NAME))
			return getAttributeNS(TableConstants.TABLE_NAMESPACE_URI,
					TableConstants.ATTR_STYLE_NAME);
		return null;
	}

	public int getTableIndex() {
		TableElement tableElement = getTableElement();
		return tableElement.getTableIndex();
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

	public int getTableColIndex() {
		TableElement tableElement = getTableElement();
		if (tableElement == null)
			return -1;

		List<TableRowElement> rowList = tableElement.getTableRowChildren();
		if (rowList == null)
			return -1;

		for (int i = 0; i < rowList.size(); i++) {
			TableRowElement row = rowList.get(i);
			List<TableCellElement> cellList = row.getTableCellChildren();
			if (cellList != null) {
				for (int j = 0; j < cellList.size(); j++) {
					if (cellList.get(j).equals(this)) {
						return j;
					}
				}
			}
		}

		return -1;
	}

	public int getTableRowIndex() {
		TableElement tableElement = getTableElement();
		if (tableElement == null)
			return -1;

		List<TableRowElement> rowList = tableElement.getTableRowChildren();
		if (rowList == null)
			return -1;

		for (int i = 0; i < rowList.size(); i++) {
			TableRowElement row = rowList.get(i);
			List<TableCellElement> cellList = row.getTableCellChildren();
			if (cellList != null) {
				for (int j = 0; j < cellList.size(); j++) {
					if (cellList.get(j).equals(this)) {
						return i;
					}
				}
			}
		}

		return -1;
	}

	public int getTableColSize() {
		TableElement tableElement = getTableElement();
		if (tableElement == null)
			return -1;

		List<TableRowElement> rowList = tableElement.getTableRowChildren();
		if (rowList == null)
			return -1;

		for (int i = 0; i < rowList.size(); i++) {
			TableRowElement row = rowList.get(i);
			List<TableCellElement> cellList = row.getTableCellChildren();
			if (cellList != null) {
				for (int j = 0; j < cellList.size(); j++) {
					if (cellList.get(j).equals(this)) {
						return cellList.size();
					}
				}
			}
		}
		return -1;
	}

	public int getTableRowSize() {
		TableElement tableElement = getTableElement();
		if (tableElement == null)
			return -1;

		List<TableRowElement> rowList = tableElement.getTableRowChildren();
		if (rowList == null)
			return -1;

		return rowList.size();
	}

	public String getAttrOfficeValue() {
		if (hasAttributeNS(OfficeConstants.OFFICE_NAMESPACE_URI,
				OfficeConstants.ATTR_VALUE))
			return getAttributeNS(OfficeConstants.OFFICE_NAMESPACE_URI,
					OfficeConstants.ATTR_VALUE);
		return null;
	}

	public String getAttrOfficeValueType() {
		if (hasAttributeNS(OfficeConstants.OFFICE_NAMESPACE_URI,
				OfficeConstants.ATTR_VALUE_TYPE))
			return getAttributeNS(OfficeConstants.OFFICE_NAMESPACE_URI,
					OfficeConstants.ATTR_VALUE_TYPE);
		return null;
	}

	public String getAttrTableFormula() {
		if (hasAttributeNS(TableConstants.TABLE_NAMESPACE_URI,
				TableConstants.ATTR_FORMULA))
			return getAttributeNS(TableConstants.TABLE_NAMESPACE_URI,
					TableConstants.ATTR_FORMULA);
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

	public int getAttrTableNumberColumnsSpanned() {
		if (hasAttributeNS(TableConstants.TABLE_NAMESPACE_URI,
				TableConstants.ATTR_NUMBER_COLUMNS_SPANNED)) {
			return new Integer(getAttributeNS(
					TableConstants.TABLE_NAMESPACE_URI,
					TableConstants.ATTR_NUMBER_COLUMNS_SPANNED)).intValue();
		}
		return -1;
	}

	public void setValue(String value) {
		setAttributeNS(OfficeConstants.OFFICE_NAMESPACE_URI,
				OfficeConstants.ATTR_VALUE, value);
	}

	public String getValue() {
		return getAttrOfficeValue();
	}

	public void setStyle(StyleElement style) {
		// TODO Auto-generated method stub

	}

	public StyleElement getStyle() {
		Element root = getOwnerDocument().getDocumentElement();
		if (root instanceof ODFElementImpl) {
			String styleName = getAttributeNS(
					TableConstants.TABLE_NAMESPACE_URI,
					TableConstants.ATTR_STYLE_NAME);
			ODFElement elem = ((ODFElementImpl) root).findElementByAttrValue(
					StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ELEMENT_STYLE,
					StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_NAME, styleName);
			if ((elem != null) && (elem instanceof StyleElement))
				return (StyleElement) elem;
		}
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