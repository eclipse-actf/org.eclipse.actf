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
import org.eclipse.actf.model.dom.odf.style.ParagraphPropertiesElement;
import org.eclipse.actf.model.dom.odf.style.StyleConstants;
import org.eclipse.actf.model.dom.odf.style.TabStopsElement;
import org.w3c.dom.Element;


class ParagraphPropertiesElementImpl extends ODFElementImpl implements
		ParagraphPropertiesElement {
	private static final long serialVersionUID = -5647687285573197891L;

	protected ParagraphPropertiesElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public String getAttrFormatLineHeight() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAttrStyleLineHeightAtLeast() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAttrFormatTextAlign() {
		if (hasAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
				FormatConstants.ATTR_TEXT_ALIGN))
			return getAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
					FormatConstants.ATTR_TEXT_ALIGN);
		return null;
	}

	public String getAttrStyleVerticalAlign() {
		if (hasAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_VERTICAL_ALIGN))
			return getAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_VERTICAL_ALIGN);
		return null;
	}

	public TabStopsElement getTabStopsElement() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setAttrFormatLineHeight(String height) {
		// TODO Auto-generated method stub

	}

	public void setAttrStyleLineHeightAtLeast(String height) {
		// TODO Auto-generated method stub
	}

	public void setAttrFormatTextAlign(String align) {
		if (align != null)
			setAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_VERTICAL_ALIGN, align);
	}

	public void setAttrStyleVerticalAlign(String align) {
		if (align != null)
			setAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
					FormatConstants.ATTR_TEXT_ALIGN, align);
	}

	public String getType() {
		// TODO Auto-generated method stub
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

	public String getAttrFormatBackgroundColor() {
		if (hasAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
				FormatConstants.ATTR_BACKGROUND_COLOR))
			return getAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
					FormatConstants.ATTR_BACKGROUND_COLOR);
		return null;
	}
}