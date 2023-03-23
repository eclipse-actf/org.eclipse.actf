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
package org.eclipse.actf.model.dom.odf.text.impl;

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.impl.ODFElementImpl;
import org.eclipse.actf.model.dom.odf.text.SElement;
import org.eclipse.actf.model.dom.odf.text.TextConstants;
import org.w3c.dom.Element;


class SElementImpl extends ODFElementImpl implements SElement {
	private static final long serialVersionUID = 1865243428016460368L;

	protected SElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public int getAttrTextC() {
		if (hasAttributeNS(TextConstants.TEXT_NAMESPACE_URI,
				TextConstants.ATTR_C))
			return new Integer(getAttributeNS(TextConstants.TEXT_NAMESPACE_URI,
					TextConstants.ATTR_C)).intValue();
		return -1;
	}
}