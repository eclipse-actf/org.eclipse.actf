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
import org.eclipse.actf.model.dom.odf.style.TableCellPropertiesElement;
import org.w3c.dom.Element;


class TableCellPropertiesElementImpl extends ODFElementImpl implements
		TableCellPropertiesElement {
	private static final long serialVersionUID = -6598063189899180220L;

	protected TableCellPropertiesElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAttrFormatBackgroundColor() {
		if (hasAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
				FormatConstants.ATTR_BACKGROUND_COLOR))
			return getAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
					FormatConstants.ATTR_BACKGROUND_COLOR);
		return null;
	}

	public String getAttrFormatBorder() {
		if (hasAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
				FormatConstants.ATTR_BORDER))
			return getAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
					FormatConstants.ATTR_BORDER);
		return null;
	}

	public String getAttrFormatBorderTop() {
		if (hasAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
				FormatConstants.ATTR_BORDER_TOP))
			return getAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
					FormatConstants.ATTR_BORDER_TOP);
		return null;
	}

	public String getAttrFormatBorderBottom() {
		if (hasAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
				FormatConstants.ATTR_BORDER_BOTTOM))
			return getAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
					FormatConstants.ATTR_BORDER_BOTTOM);
		return null;
	}

	public String getAttrFormatBorderLeft() {
		if (hasAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
				FormatConstants.ATTR_BORDER_LEFT))
			return getAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
					FormatConstants.ATTR_BORDER_LEFT);
		return null;
	}

	public String getAttrFormatBorderRight() {
		if (hasAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
				FormatConstants.ATTR_BORDER_RIGHT))
			return getAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
					FormatConstants.ATTR_BORDER_RIGHT);
		return null;
	}

	public String getAttrFormatPadding() {
		if (hasAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
				FormatConstants.ATTR_PADDING))
			return getAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
					FormatConstants.ATTR_PADDING);
		return null;
	}

	public String getAttrStyleShadow() {
		if (hasAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_SHADOW))
			return getAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_SHADOW);
		return null;
	}

	static private final String CELL_PROTECT_VALUE_NONE = "none"; //$NON-NLS-1$

	static private final String CELL_PROTECT_VALUE_PROTECTED = "protected"; //$NON-NLS-1$

	static private final String CELL_PROTECT_VALUE_FORMULA_HIDDEN = "formula-hidden"; //$NON-NLS-1$

	static private final String CELL_PROTECT_VALUE_HIDDEN_AND_PROTECTED = "hidden-and-protected"; //$NON-NLS-1$

	public int getAttrStyleCellProtect() {
		if (hasAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_CELL_PROTECT)) {
			String str = getAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_CELL_PROTECT);
			if (str.equals(CELL_PROTECT_VALUE_NONE)) {
				return StyleConstants.CELL_PROTECT_VALUE_NONE;
			} else if (str.equals(CELL_PROTECT_VALUE_HIDDEN_AND_PROTECTED)) {
				return StyleConstants.CELL_PROTECT_VALUE_HIDDEN_AND_PROTECTED;
			} else {
				int val = StyleConstants.CELL_PROTECT_VALUE_NOT_DEFINED;
				String[] values = str.split(" "); //$NON-NLS-1$
				for (int i = 0; i < values.length; i++) {
					if (values[i].equals(CELL_PROTECT_VALUE_PROTECTED)) {
						if (val == StyleConstants.CELL_PROTECT_VALUE_HIDDEN)
							val = StyleConstants.CELL_PROTECT_VALUE_HIDDEN_AND_PROTECTED;
						else
							val = StyleConstants.CELL_PROTECT_VALUE_PROTECTED;
					} else if (values[i]
							.equals(CELL_PROTECT_VALUE_FORMULA_HIDDEN)) {
						if (val == StyleConstants.CELL_PROTECT_VALUE_PROTECTED)
							val = StyleConstants.CELL_PROTECT_VALUE_HIDDEN_AND_PROTECTED;
						else
							val = StyleConstants.CELL_PROTECT_VALUE_HIDDEN;
					}
				}
				return val;
			}
		}
		return StyleConstants.CELL_PROTECT_VALUE_NOT_DEFINED;
	}
}