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
import org.eclipse.actf.model.dom.odf.style.StyleConstants;
import org.eclipse.actf.model.dom.odf.style.impl.StyleElementImpl;
import org.eclipse.actf.model.dom.odf.text.ListLevelStyleNumberElement;
import org.eclipse.actf.model.dom.odf.text.TextConstants;
import org.w3c.dom.Element;


class ListLevelStyleNumberElementImpl extends StyleElementImpl implements
		ListLevelStyleNumberElement {
	private static final long serialVersionUID = 1887105389434803256L;

	protected ListLevelStyleNumberElementImpl(ODFDocument odfDoc,
			Element element) {
		super(odfDoc, element);
	}

	public long getAttrLevel() {
		return new Integer(getAttributeNS(TextConstants.TEXT_NAMESPACE_URI,
				TextConstants.ATTR_LEVEL)).intValue();
	}

	public String getAttrNumFormat() {
		return getAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_NUM_FORMAT);
	}

	public String getAttrNumPrefix() {
		return getAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_NUM_PREFIX);
	}

	public String getAttrNumSuffix() {
		return getAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_NUM_SUFFIX);
	}
}