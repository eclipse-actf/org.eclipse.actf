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
package org.eclipse.actf.model.dom.odf.number.impl;

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.impl.ODFElementImpl;
import org.eclipse.actf.model.dom.odf.number.NumberStyleElement;
import org.eclipse.actf.model.dom.odf.style.StyleConstants;
import org.w3c.dom.Element;


class NumberStyleElementImpl extends ODFElementImpl implements
		NumberStyleElement {
	private static final long serialVersionUID = -6996192017442148098L;

	protected NumberStyleElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public String getAttrStyleName() {
		if (hasAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_NAME))
			return getAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_NAME);
		return null;
	}
}
