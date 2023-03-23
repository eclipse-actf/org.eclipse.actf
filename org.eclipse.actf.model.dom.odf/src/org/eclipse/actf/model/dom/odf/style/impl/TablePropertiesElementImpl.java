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
import org.eclipse.actf.model.dom.odf.style.StyleConstants;
import org.eclipse.actf.model.dom.odf.style.TablePropertiesElement;
import org.eclipse.actf.model.dom.odf.table.TableConstants;
import org.w3c.dom.Element;


class TablePropertiesElementImpl extends ODFElementImpl implements
		TablePropertiesElement {
	private static final long serialVersionUID = 6683381119135165L;

	protected TablePropertiesElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean getAttrTableDisplay() {
		if (hasAttributeNS(TableConstants.TABLE_NAMESPACE_URI,
				TableConstants.ATTR_DISPLAY)) {
			return new Boolean(getAttributeNS(
					TableConstants.TABLE_NAMESPACE_URI,
					TableConstants.ATTR_DISPLAY)).booleanValue();
		}
		return false;
	}

	public String getAttrStyleWidth() {
		if (hasAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_WIDTH))
			return getAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_WIDTH);
		return null;
	}

	public String getAttrStyleWritingMode() {
		if (hasAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_WRITING_MODE))
			return getAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_WRITING_MODE);
		return null;
	}

	public String getAttrTableBorderModel() {
		if (hasAttributeNS(TableConstants.TABLE_NAMESPACE_URI,
				TableConstants.ATTR_BORDER_MODEL))
			return getAttributeNS(TableConstants.TABLE_NAMESPACE_URI,
					TableConstants.ATTR_BORDER_MODEL);
		return null;
	}
}