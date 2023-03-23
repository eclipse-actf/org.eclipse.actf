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
package org.eclipse.actf.model.dom.odf.draw.impl;

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.impl.ODFElementImpl;
import org.eclipse.actf.model.dom.odf.draw.DrawConstants;
import org.eclipse.actf.model.dom.odf.draw.FillImageElement;
import org.eclipse.actf.model.dom.odf.xlink.XLinkConstants;
import org.w3c.dom.Element;


class FillImageElementImpl extends ODFElementImpl implements FillImageElement {
	private static final long serialVersionUID = 5874806534330621667L;

	protected FillImageElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public String getAttrDrawName() {
		if (hasAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ATTR_NAME))
			return getAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ATTR_NAME);
		return null;
	}

	public String getAttrDrawDisplayName() {
		if (hasAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ATTR_DISPLAY_NAME))
			return getAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ATTR_DISPLAY_NAME);
		return null;
	}

	public String getAttrXLinkHref() {
		if (hasAttributeNS(XLinkConstants.XLINK_NAMESPACE_URI,
				XLinkConstants.ATTR_HREF))
			return getAttributeNS(XLinkConstants.XLINK_NAMESPACE_URI,
					XLinkConstants.ATTR_HREF);
		return null;
	}

	public String getAttrXLinkType() {
		if (hasAttributeNS(XLinkConstants.XLINK_NAMESPACE_URI,
				XLinkConstants.ATTR_TYPE))
			return getAttributeNS(XLinkConstants.XLINK_NAMESPACE_URI,
					XLinkConstants.ATTR_TYPE);
		return null;
	}

	public String getAttrXLinkShow() {
		if (hasAttributeNS(XLinkConstants.XLINK_NAMESPACE_URI,
				XLinkConstants.ATTR_SHOW))
			return getAttributeNS(XLinkConstants.XLINK_NAMESPACE_URI,
					XLinkConstants.ATTR_SHOW);
		return null;
	}

	public String getAttrXLinkActuate() {
		if (hasAttributeNS(XLinkConstants.XLINK_NAMESPACE_URI,
				XLinkConstants.ATTR_ACTUATE))
			return getAttributeNS(XLinkConstants.XLINK_NAMESPACE_URI,
					XLinkConstants.ATTR_ACTUATE);
		return null;
	}
}