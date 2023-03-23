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
package org.eclipse.actf.model.dom.odf.table;

import java.util.List;

import org.eclipse.actf.model.dom.odf.base.ContentBaseElement;
import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.content.IStylable;
import org.eclipse.actf.model.dom.odf.range.ITextElementContainer;
import org.eclipse.actf.model.dom.odf.text.SequenceElement;
import org.w3c.dom.NodeList;

/**
 * Interface for &lt;table:table&gt; element.
 */
public interface TableElement extends ODFElement, IStylable,
		ITextElementContainer {
	public String getAttrTableStyleName();

	public String getAttrTableName();

	public boolean getAttrTableAttrPrint();

	public boolean getAttrTableProtected();

	public ContentBaseElement getContent();

	public int getTableIndex();

	public TableColumnElement getTableColumnChild(int column);

	public TableRowElement getTableRowChild(int row);

	public int getTableColumnSize();

	public List<TableColumnElement> getTableColumnChildren();

	public int getTableRowSize();

	public List<TableRowElement> getTableRowChildren();

	public boolean hasTableCellStyle(int column, int row);

	public TableRowElement appendNewTableRowElement();

	public NodeList getTableHeaderColumns();

	public NodeList getTableHeaderRows();

	public SequenceElement getTextSequenceElement();
}