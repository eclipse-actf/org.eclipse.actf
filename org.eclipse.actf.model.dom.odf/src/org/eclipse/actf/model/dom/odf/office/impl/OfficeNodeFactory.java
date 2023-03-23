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

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.base.impl.AbstractODFNodeFactory;
import org.eclipse.actf.model.dom.odf.office.OfficeConstants;
import org.w3c.dom.Element;


public class OfficeNodeFactory extends AbstractODFNodeFactory {
	private static final Map<String, Class<? extends ODFElement>> tagMap = new HashMap<String, Class<? extends ODFElement>>();

	static {
		tagMap.put(OfficeConstants.ELEMENT_AUTOMATIC_STYLES,
				AutomaticStylesElementImpl.class);
		tagMap.put(OfficeConstants.ELEMENT_BODY, BodyElementImpl.class);
		tagMap.put(OfficeConstants.ELEMENT_CHART, ChartElementImpl.class);
		tagMap.put(OfficeConstants.ELEMENT_DOCUMENT_CONTENT,
				DocumentContentElementImpl.class);
		tagMap.put(OfficeConstants.ELEMENT_DOCUMENT_STYLES,
				DocumentStylesElementImpl.class);
		tagMap.put(OfficeConstants.ELEMENT_DRAWING, DrawingElementImpl.class);
		tagMap.put(OfficeConstants.ELEMENT_FONT_FACE_DECLS,
				FontFaceDeclsElementImpl.class);
		tagMap.put(OfficeConstants.ELEMENT_FORMS, FormsElementImpl.class);
		tagMap.put(OfficeConstants.ELEMENT_MASTER_STYLES,
				MasterStylesElementImpl.class);
		tagMap.put(OfficeConstants.ELEMENT_PRESENTATION,
				PresentationElementImpl.class);
		tagMap.put(OfficeConstants.ELEMENT_SPREADSHEET,
				SpreadSheetElementImpl.class);
		tagMap.put(OfficeConstants.ELEMENT_TEXT, TextElementImpl.class);
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