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

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.base.impl.AbstractODFNodeFactory;
import org.eclipse.actf.model.dom.odf.table.TableConstants;
import org.w3c.dom.Element;

public class TableNodeFactory extends AbstractODFNodeFactory {
	private static final Map<String, Class<? extends ODFElement>> tagMap = new HashMap<String, Class<? extends ODFElement>>();

	static {
		tagMap.put(TableConstants.ELEMENT_TABLE, TableElementImpl.class);
		tagMap.put(TableConstants.ELEMENT_TABLE_ROW, TableRowElementImpl.class);
		tagMap.put(TableConstants.ELEMENT_TABLE_COLUMN,
				TableColumnElementImpl.class);
		tagMap.put(TableConstants.ELEMENT_TABLE_CELL,
				TableCellElementImpl.class);
		tagMap.put(TableConstants.ELEMENT_COVERED_TABLE_CELL,
				CoveredTableCellElementImpl.class);
		tagMap.put(TableConstants.ELEMENT_TABLE_HEADER_COLUMNS,
				TableHeaderColumnsElementImpl.class);
		tagMap.put(TableConstants.ELEMENT_TABLE_HEADER_ROWS,
				TableHeaderRowsElementImpl.class);
		tagMap.put(TableConstants.ELEMENT_TABLE_ROWS,
				TableRowsElementImpl.class);
		tagMap.put(TableConstants.ELEMENT_TABLE_COLUMNS,
				TableColumnsElementImpl.class);
	}

	public static Element createElement(ODFDocument odfDoc, String tagName,
			Element element) {
		Class<? extends ODFElement> cs = tagMap.get(tagName);
		if (null == cs) {
			return null;
		}

		try {
			return findElementConstractor(cs).newInstance(
					new Object[] { odfDoc, element });
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
}