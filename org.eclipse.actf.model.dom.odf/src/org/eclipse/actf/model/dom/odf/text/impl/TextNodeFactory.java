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
package org.eclipse.actf.model.dom.odf.text.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.base.impl.AbstractODFNodeFactory;
import org.eclipse.actf.model.dom.odf.text.TextConstants;
import org.w3c.dom.Element;

public class TextNodeFactory extends AbstractODFNodeFactory {
	private static final Map<String, Class<? extends ODFElement>> tagMap = new HashMap<String, Class<? extends ODFElement>>();

	static {
		tagMap.put(TextConstants.ELEMENT_A, AElementImpl.class);
		tagMap.put(TextConstants.ELEMENT_ALPHABETICAL_INDEX,
				AlphabeticalIndexElementImpl.class);
		tagMap.put(TextConstants.ELEMENT_ALPHABETICAL_INDEX_SOURCE,
				AlphabeticalIndexSourceElementImpl.class);
		tagMap.put(TextConstants.ELEMENT_BOOKMARK, BookmarkElementImpl.class);
		tagMap.put(TextConstants.ELEMENT_H, HElementImpl.class);
		tagMap
				.put(TextConstants.ELEMENT_INDEX_BODY,
						IndexBodyElementImpl.class);
		tagMap.put(TextConstants.ELEMENT_INDEX_TITLE,
				IndexTitleElementImpl.class);
		tagMap.put(TextConstants.ELEMENT_LIST, ListElementImpl.class);
		tagMap.put(TextConstants.ELEMENT_LIST_ITEM, ListItemElementImpl.class);
		tagMap.put(TextConstants.ELEMENT_LIST_LEVEL_STYLE_BULLET,
				ListLevelStyleBulletElementImpl.class);
		tagMap.put(TextConstants.ELEMENT_LIST_LEVEL_STYLE_NUMBER,
				ListLevelStyleNumberElementImpl.class);
		tagMap
				.put(TextConstants.ELEMENT_LIST_STYLE,
						ListStyleElementImpl.class);
		tagMap.put(TextConstants.ELEMENT_LIST_HEADER,
				ListHeaderElementImpl.class);
		tagMap.put(TextConstants.ELEMENT_P, PElementImpl.class);
		tagMap.put(TextConstants.ELEMENT_S, SElementImpl.class);
		tagMap.put(TextConstants.ELEMENT_SECTION, SectionElementImpl.class);
		tagMap.put(TextConstants.ELEMENT_SEQUENCE, SequenceElementImpl.class);
		tagMap.put(TextConstants.ELEMENT_SPAN, SpanElementImpl.class);
		tagMap.put(TextConstants.ELEMENT_TAB, TabElementImpl.class);
		tagMap.put(TextConstants.ELEMENT_TABLE_OF_CONTENT,
				TableOfContentElementImpl.class);
		tagMap.put(TextConstants.ELEMENT_TABLE_OF_CONTENT_SOURCE,
				TableOfContentSourceElementImpl.class);
		tagMap.put(TextConstants.ELEMENT_TRACKED_CHANGES,
				TrackedChangesElementImpl.class);
		tagMap.put(TextConstants.ELEMENT_CHANGE, ChangeElementImpl.class);
		tagMap.put(TextConstants.ELEMENT_CHANGE_START,
				ChangeStartElementImpl.class);
		tagMap
				.put(TextConstants.ELEMENT_CHANGE_END,
						ChangeEndElementImpl.class);
		tagMap.put(TextConstants.ELEMENT_PAGE_NUMBER,
				PageNumberElementImpl.class);
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