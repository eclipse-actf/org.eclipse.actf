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
package org.eclipse.actf.model.dom.odf.style.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.base.impl.AbstractODFNodeFactory;
import org.eclipse.actf.model.dom.odf.style.StyleConstants;
import org.w3c.dom.Element;


public class StyleNodeFactory extends AbstractODFNodeFactory {
	private static final Map<String, Class<? extends ODFElement>> tagMap = new HashMap<String, Class<? extends ODFElement>>();

	static {
		tagMap.put(StyleConstants.ELEMENT_DEFAULT_STYLE,
				DefaultStyleElementImpl.class);
		tagMap.put(StyleConstants.ELEMENT_STYLE, StyleElementImpl.class);
		tagMap.put(StyleConstants.ELEMENT_TABLE_PROPERTIES,
				TablePropertiesElementImpl.class);
		tagMap.put(StyleConstants.ELEMENT_TABLE_COLUMN_PROPERTIES,
				TableColumnPropertiesElementImpl.class);
		tagMap.put(StyleConstants.ELEMENT_TABLE_ROW_PROPERTIES,
				TableRowPropertiesElementImpl.class);
		tagMap.put(StyleConstants.ELEMENT_TABLE_CELL_PROPERTIES,
				TableCellPropertiesElementImpl.class);
		tagMap.put(StyleConstants.ELEMENT_FONT_FACE, FontFaceElementImpl.class);
		tagMap.put(StyleConstants.ELEMENT_TEXT_PROPERTIES,
				TextPropertiesElementImpl.class);
		tagMap.put(StyleConstants.ELEMENT_CHART_PROPERTIES,
				ChartPropertiesElementImpl.class);
		tagMap.put(StyleConstants.ELEMENT_GRAPHIC_PROPERTIES,
				GraphicPropertiesElementImpl.class);
		tagMap.put(StyleConstants.ELEMENT_PARAGRAPH_PROPERTIES,
				ParagraphPropertiesElementImpl.class);
		tagMap.put(StyleConstants.ELEMENT_PAGE_LAYOUT,
				PageLayoutElementImpl.class);
		tagMap.put(StyleConstants.ELEMENT_PAGE_LAYOUT_PROPERTIES,
				PageLayoutPropertiesElementImpl.class);
		tagMap.put(StyleConstants.ELEMENT_DRAWING_PAGE_PROPERTIES,
				DrawingPagePropertiesElementImpl.class);
		tagMap.put(StyleConstants.ELEMENT_MASTER_PAGE,
				MasterPageElementImpl.class);
		tagMap.put(StyleConstants.ELEMENT_MAP, MapElementImpl.class);
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