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
import org.eclipse.actf.model.dom.odf.base.impl.EmbedDrawingObjectElementImpl;
import org.eclipse.actf.model.dom.odf.draw.ImageElement;
import org.eclipse.actf.model.dom.odf.xlink.XLinkConstants;
import org.w3c.dom.Element;


class ImageElementImpl extends EmbedDrawingObjectElementImpl implements
		ImageElement {
	private static final long serialVersionUID = -2572794068793718946L;

	protected ImageElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public String getAttrXlinkHref() {
		if (hasAttributeNS(XLinkConstants.XLINK_NAMESPACE_URI,
				XLinkConstants.ATTR_HREF))
			return getAttributeNS(XLinkConstants.XLINK_NAMESPACE_URI,
					XLinkConstants.ATTR_HREF);
		return null;
	}

	public String getAttrXlinkType() {
		if (hasAttributeNS(XLinkConstants.XLINK_NAMESPACE_URI,
				XLinkConstants.ATTR_TYPE))
			return getAttributeNS(XLinkConstants.XLINK_NAMESPACE_URI,
					XLinkConstants.ATTR_TYPE);
		return null;
	}

	public String getAttrXlinkShow() {
		if (hasAttributeNS(XLinkConstants.XLINK_NAMESPACE_URI,
				XLinkConstants.ATTR_SHOW))
			return getAttributeNS(XLinkConstants.XLINK_NAMESPACE_URI,
					XLinkConstants.ATTR_SHOW);
		return null;
	}

	public String getAttrXlinkActuate() {
		if (hasAttributeNS(XLinkConstants.XLINK_NAMESPACE_URI,
				XLinkConstants.ATTR_ACTUATE))
			return getAttributeNS(XLinkConstants.XLINK_NAMESPACE_URI,
					XLinkConstants.ATTR_ACTUATE);
		return null;
	}
}