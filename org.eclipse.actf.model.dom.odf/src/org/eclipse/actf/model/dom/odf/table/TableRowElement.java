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

import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.content.IStylable;
import org.eclipse.actf.model.dom.odf.range.ITextElementContainer;

/**
 * Interface for &lt;table:table-row&gt; element.
 */
public interface TableRowElement extends ODFElement, IStylable,
		ITextElementContainer {
	public String getAttrTableStyleName();

	public int getAttrTableNumberRowsRepeated();

	public int getTableIndex();

	public int getRowIndex();

	public TableElement getTableElement();

	public List<TableCellElement> getTableCellChildren();
}