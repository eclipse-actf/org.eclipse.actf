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
import org.eclipse.actf.model.dom.odf.style.FontFaceElement;
import org.eclipse.actf.model.dom.odf.style.StyleConstants;
import org.eclipse.actf.model.dom.odf.svg.SVGConstants;
import org.w3c.dom.Element;


class FontFaceElementImpl extends ODFElementImpl implements FontFaceElement {
	private static final long serialVersionUID = 6886236663919077061L;

	protected FontFaceElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAttrStyleName() {
		if (hasAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_NAME))
			return getAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_NAME);
		return null;
	}

	public String getAttrSvgFontFamily() {
		if (hasAttributeNS(SVGConstants.SVG_NAMESPACE_URI,
				SVGConstants.ATTR_FONT_FAMILY))
			return getAttributeNS(SVGConstants.SVG_NAMESPACE_URI,
					SVGConstants.ATTR_FONT_FAMILY);
		return null;
	}

	public String getAttrSvgFontFamilyGeneric() {
		if (hasAttributeNS(SVGConstants.SVG_NAMESPACE_URI,
				SVGConstants.ATTR_FONT_FAMILY_GENERIC))
			return getAttributeNS(SVGConstants.SVG_NAMESPACE_URI,
					SVGConstants.ATTR_FONT_FAMILY_GENERIC);
		return null;
	}

	public String getAttrStyleFontPitch() {
		if (hasAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_FONT_PITCH))
			return getAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_FONT_PITCH);
		return null;
	}
}