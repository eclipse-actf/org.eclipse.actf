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
package org.eclipse.actf.model.dom.odf.form.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.base.impl.AbstractODFNodeFactory;
import org.eclipse.actf.model.dom.odf.form.FormConstants;
import org.w3c.dom.Element;

public class FormNodeFactory extends AbstractODFNodeFactory {
	private static final Map<String, Class<? extends ODFElement>> tagMap = new HashMap<String, Class<? extends ODFElement>>();

	static {
		tagMap.put(FormConstants.ELEMENT_BUTTON, ButtonElementImpl.class);
		tagMap.put(FormConstants.ELEMENT_CHECKBOX, CheckboxElementImpl.class);
		tagMap.put(FormConstants.ELEMENT_COLUMN, ColumnElementImpl.class);
		tagMap.put(FormConstants.ELEMENT_COMBOBOX, ComboboxElementImpl.class);
		tagMap.put(FormConstants.ELEMENT_DATE, DateElementImpl.class);
		tagMap
				.put(FormConstants.ELEMENT_FIXED_TEXT,
						FixedTextElementImpl.class);
		tagMap.put(FormConstants.ELEMENT_FORMATTED_TEXT,
				FormattedTextElementImpl.class);
		tagMap.put(FormConstants.ELEMENT_FORM, FormElementImpl.class);
		tagMap.put(FormConstants.ELEMENT_FRAME, FrameElementImpl.class);
		tagMap.put(FormConstants.ELEMENT_ITEM, ItemElementImpl.class);
		tagMap.put(FormConstants.ELEMENT_LISTBOX, ListboxElementImpl.class);
		tagMap.put(FormConstants.ELEMENT_NUMBER, NumberElementImpl.class);
		tagMap.put(FormConstants.ELEMENT_OPTION, OptionElementImpl.class);
		tagMap.put(FormConstants.ELEMENT_PROPERTY, PropertyElementImpl.class);
		tagMap.put(FormConstants.ELEMENT_RADIO, RadioElementImpl.class);
		tagMap.put(FormConstants.ELEMENT_TEXTAREA, TextareaElementImpl.class);
		tagMap.put(FormConstants.ELEMENT_TEXT, TextElementImpl.class);
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