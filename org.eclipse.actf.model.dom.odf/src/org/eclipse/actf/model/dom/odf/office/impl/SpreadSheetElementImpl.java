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
package org.eclipse.actf.model.dom.odf.office.impl;

import org.eclipse.actf.model.dom.odf.ODFConstants.ContentType;
import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.impl.ODFElementImpl;
import org.eclipse.actf.model.dom.odf.office.SpreadSheetElement;
import org.eclipse.actf.model.dom.odf.range.IContentRange;
import org.eclipse.actf.model.dom.odf.table.TableConstants;
import org.eclipse.actf.model.dom.odf.table.TableElement;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


class SpreadSheetElementImpl extends ODFElementImpl implements
		SpreadSheetElement {
	private static final long serialVersionUID = -1698659854810052305L;

	protected SpreadSheetElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public ContentType getContentType() {
		return ContentType.SPREADSHEET;
	}

	public IContentRange createRange() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean getAttrTableStructureProtected() {
		if (hasAttributeNS(TableConstants.TABLE_NAMESPACE_URI,
				TableConstants.ATTR_STRUCTURE_PROTECTED)) {
			return new Boolean(getAttributeNS(
					TableConstants.TABLE_NAMESPACE_URI,
					TableConstants.ATTR_STRUCTURE_PROTECTED)).booleanValue();
		}
		return false;
	}

	public TableElement getTable(long idx) {
		NodeList nl = getElementsByTagNameNS(
				TableConstants.TABLE_NAMESPACE_URI,
				TableConstants.ELEMENT_TABLE);
		return (TableElement) nl.item((int) idx);
	}

	public TableElement getTableByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getTableSize() {
		NodeList nl = getElementsByTagNameNS(
				TableConstants.TABLE_NAMESPACE_URI,
				TableConstants.ELEMENT_TABLE);
		return nl.getLength();
	}

	public TableElement createTable(long idx, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public TableElement getActiveTable() {
		// TODO Auto-generated method stub
		return null;
	}
}