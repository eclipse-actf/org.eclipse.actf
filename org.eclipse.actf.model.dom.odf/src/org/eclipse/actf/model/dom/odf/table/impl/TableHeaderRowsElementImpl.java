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

import java.util.List;
import java.util.Vector;

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.impl.ODFElementImpl;
import org.eclipse.actf.model.dom.odf.table.TableElement;
import org.eclipse.actf.model.dom.odf.table.TableHeaderRowsElement;
import org.eclipse.actf.model.dom.odf.table.TableRowElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class TableHeaderRowsElementImpl extends ODFElementImpl implements
		TableHeaderRowsElement {
	private static final long serialVersionUID = 8078924819161617838L;

	protected TableHeaderRowsElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public int getTableIndex() {
		return getTableElement().getTableIndex();
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

	public List<TableRowElement> getTableRowChildren() {
		List<TableRowElement> rowList = null;

		NodeList children = getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			if (children.item(i) instanceof TableRowElement) {
				if (rowList == null)
					rowList = new Vector<TableRowElement>();
				rowList.add((TableRowElement) children.item(i));
			}
		}

		return rowList;
	}
}
