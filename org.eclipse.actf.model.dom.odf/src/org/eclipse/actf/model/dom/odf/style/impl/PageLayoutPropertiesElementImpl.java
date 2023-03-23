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

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.impl.ODFElementImpl;
import org.eclipse.actf.model.dom.odf.format.FormatConstants;
import org.eclipse.actf.model.dom.odf.style.PageLayoutPropertiesElement;
import org.eclipse.actf.model.dom.odf.style.StyleConstants;
import org.w3c.dom.Element;


class PageLayoutPropertiesElementImpl extends ODFElementImpl implements
		PageLayoutPropertiesElement {
	private static final long serialVersionUID = 8910766683962119790L;

	protected PageLayoutPropertiesElementImpl(ODFDocument odfDoc,
			Element element) {
		super(odfDoc, element);
	}

	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAttrFormatMarginTop() {
		if (hasAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
				FormatConstants.ATTR_MARGIN_TOP))
			return getAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
					FormatConstants.ATTR_MARGIN_TOP);
		return null;
	}

	public String getAttrFormatMarginBottom() {
		if (hasAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
				FormatConstants.ATTR_MARGIN_BOTTOM))
			return getAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
					FormatConstants.ATTR_MARGIN_BOTTOM);
		return null;
	}

	public String getAttrFormatMarginLeft() {
		if (hasAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
				FormatConstants.ATTR_MARGIN_LEFT))
			return getAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
					FormatConstants.ATTR_MARGIN_LEFT);
		return null;
	}

	public String getAttrFormatMarginRight() {
		if (hasAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
				FormatConstants.ATTR_MARGIN_RIGHT))
			return getAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
					FormatConstants.ATTR_MARGIN_RIGHT);
		return null;
	}

	public String getAttrFormatPageWidth() {
		if (hasAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
				FormatConstants.ATTR_PAGE_WIDTH))
			return getAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
					FormatConstants.ATTR_PAGE_WIDTH);
		return null;
	}

	public String getAttrFormatPageHeight() {
		if (hasAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
				FormatConstants.ATTR_PAGE_HEIGHT))
			return getAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
					FormatConstants.ATTR_PAGE_HEIGHT);
		return null;
	}

	public String getAttrStylePrintOrientation() {
		if (hasAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_PRINT_ORIENTATION))
			return getAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_PRINT_ORIENTATION);
		return null;
	}
}