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
import org.eclipse.actf.model.dom.odf.style.StyleConstants;
import org.eclipse.actf.model.dom.odf.style.TextPropertiesElement;
import org.w3c.dom.Element;


class TextPropertiesElementImpl extends ODFElementImpl implements
		TextPropertiesElement {
	private static final long serialVersionUID = -5248765049107368279L;

	protected TextPropertiesElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAttrStyleFontName() {
		if (hasAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_FONT_NAME))
			return getAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_FONT_NAME);
		return null;
	}

	public String getAttrFormatFontSize() {
		if (hasAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
				FormatConstants.ATTR_FONT_SIZE))
			return getAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
					FormatConstants.ATTR_FONT_SIZE);
		return null;
	}

	public String getAttrFormatFontStyle() {
		if (hasAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
				FormatConstants.ATTR_FONT_STYLE))
			return getAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
					FormatConstants.ATTR_FONT_STYLE);
		return null;
	}

	public String getAttrFormatFontWeight() {
		if (hasAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
				FormatConstants.ATTR_FONT_WEIGHT))
			return getAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
					FormatConstants.ATTR_FONT_WEIGHT);
		return null;
	}

	public String getAttrFormatColor() {
		if (hasAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
				FormatConstants.ATTR_COLOR))
			return getAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
					FormatConstants.ATTR_COLOR);
		return null;
	}

	public String getAttrFormatBackgroundColor() {
		if (hasAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
				FormatConstants.ATTR_BACKGROUND_COLOR))
			return getAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
					FormatConstants.ATTR_BACKGROUND_COLOR);
		return null;
	}

	public String getAttrFormatFontFamily() {
		if (hasAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
				FormatConstants.ATTR_FONT_FAMILY))
			return getAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
					FormatConstants.ATTR_FONT_FAMILY);
		return null;
	}

	public String getAttrStyleFontFamilyGeneric() {
		if (hasAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_FONT_FAMILY_GENERIC))
			return getAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_FONT_FAMILY_GENERIC);
		return null;
	}

	public String getAttrStyleFontPitch() {
		if (hasAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_FONT_PITCH))
			return getAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_FONT_PITCH);
		return null;
	}

	public String getAttrStyleFontFamilyAsian() {
		if (hasAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_FONT_FAMILY_ASIAN))
			return getAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_FONT_FAMILY_ASIAN);
		return null;
	}

	public String getAttrStyleFontPitchAsian() {
		if (hasAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_FONT_PITCH_ASIAN))
			return getAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_FONT_PITCH_ASIAN);
		return null;
	}

	public String getAttrStyleFontSizeAsian() {
		if (hasAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_FONT_SIZE_ASIAN))
			return getAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_FONT_SIZE_ASIAN);
		return null;
	}

	public String getAttrStyleFontFamilyComplex() {
		if (hasAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_FONT_FAMILY_COMPLEX))
			return getAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_FONT_FAMILY_COMPLEX);
		return null;
	}

	public String getAttrStyleFontPitchComplex() {
		if (hasAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_FONT_PITCH_COMPLEX))
			return getAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_FONT_PITCH_COMPLEX);
		return null;
	}

	public String getAttrStyleFontSizeComplex() {
		if (hasAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_FONT_SIZE_COMPLEX))
			return getAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_FONT_SIZE_COMPLEX);
		return null;
	}

	public String getAttrStyleTextUnderlineStyle() {
		if (hasAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_TEXT_UNDERLINE_STYLE))
			return getAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_TEXT_UNDERLINE_STYLE);
		return null;
	}

	public String getAttrStyleTextPosition() {
		if (hasAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_TEXT_POSITION))
			return getAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_TEXT_POSITION);
		return null;
	}
}