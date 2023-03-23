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
import org.eclipse.actf.model.dom.odf.style.TableRowPropertiesElement;
import org.w3c.dom.Element;


class TableRowPropertiesElementImpl extends ODFElementImpl implements
		TableRowPropertiesElement {
	private static final long serialVersionUID = -5351217882394300961L;

	protected TableRowPropertiesElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAttrStyleRowHeight() {
		if (hasAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_ROW_HEIGHT))
			return getAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_ROW_HEIGHT);
		return null;
	}

	public String getAttrFormatBreakBefore() {
		if (hasAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
				FormatConstants.ATTR_BREAK_BEFORE))
			return getAttributeNS(FormatConstants.FORMAT_NAMESPACE_URI,
					FormatConstants.ATTR_BREAK_BEFORE);
		return null;
	}

	public boolean getAttrStyleUseOptimalRowHeight() {
		if (hasAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_USE_OPTIMAL_ROW_HEIGHT)) {
			return new Boolean(getAttributeNS(
					StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_USE_OPTIMAL_ROW_HEIGHT)).booleanValue();
		}
		return false;
	}
}