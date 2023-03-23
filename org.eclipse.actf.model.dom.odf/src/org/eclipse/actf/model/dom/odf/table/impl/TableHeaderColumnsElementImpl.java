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
import org.eclipse.actf.model.dom.odf.table.TableColumnElement;
import org.eclipse.actf.model.dom.odf.table.TableElement;
import org.eclipse.actf.model.dom.odf.table.TableHeaderColumnsElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class TableHeaderColumnsElementImpl extends ODFElementImpl implements
		TableHeaderColumnsElement {
	private static final long serialVersionUID = -2836780502407580629L;

	protected TableHeaderColumnsElementImpl(ODFDocument odfDoc, Element element) {
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

	public List<TableColumnElement> getTableColumChildren() {
		List<TableColumnElement> columnList = null;

		NodeList children = getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			if (children.item(i) instanceof TableColumnElement) {
				if (columnList == null)
					columnList = new Vector<TableColumnElement>();
				columnList.add((TableColumnElement) children.item(i));
			}
		}

		return columnList;
	}
}
