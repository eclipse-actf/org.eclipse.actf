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
import org.eclipse.actf.model.dom.odf.number.NumberConstants;
import org.eclipse.actf.model.dom.odf.number.NumberElement;
import org.w3c.dom.Element;


class NumberElementImpl extends ODFElementImpl implements NumberElement {
	private static final long serialVersionUID = -7712632967403051489L;

	protected NumberElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public int getAttrNumberMinIntegerDigits() {
		if (hasAttributeNS(NumberConstants.NUMBER_NAMESPACE_URI,
				NumberConstants.ATTR_MIN_INTEGER_DIGITS)) {
			return new Integer(getAttributeNS(
					NumberConstants.NUMBER_NAMESPACE_URI,
					NumberConstants.ATTR_MIN_INTEGER_DIGITS)).intValue();
		}
		return -1;
	}

	public int getAttrNumberDecimalPlaces() {
		if (hasAttributeNS(NumberConstants.NUMBER_NAMESPACE_URI,
				NumberConstants.ATTR_DECIMAL_PLACES)) {
			return new Integer(getAttributeNS(
					NumberConstants.NUMBER_NAMESPACE_URI,
					NumberConstants.ATTR_DECIMAL_PLACES)).intValue();
		}
		return -1;
	}

	public boolean getAttrNumberGrouping() {
		if (hasAttributeNS(NumberConstants.NUMBER_NAMESPACE_URI,
				NumberConstants.ATTR_GROUPING)) {
			return new Boolean(getAttributeNS(
					NumberConstants.NUMBER_NAMESPACE_URI,
					NumberConstants.ATTR_GROUPING)).booleanValue();
		}
		return false;
	}
}