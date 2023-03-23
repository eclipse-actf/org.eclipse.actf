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

import org.eclipse.actf.model.dom.odf.ODFConstants;
import org.eclipse.actf.model.dom.odf.ODFParser;
import org.eclipse.actf.model.dom.odf.ODFConstants.ContentType;
import org.eclipse.actf.model.dom.odf.base.ContentBaseElement;
import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.impl.EmbedDrawingObjectElementImpl;
import org.eclipse.actf.model.dom.odf.draw.DrawConstants;
import org.eclipse.actf.model.dom.odf.draw.ObjectElement;
import org.eclipse.actf.model.dom.odf.office.BodyElement;
import org.eclipse.actf.model.dom.odf.office.DocumentContentElement;
import org.eclipse.actf.model.dom.odf.xlink.XLinkConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


class ObjectElementImpl extends EmbedDrawingObjectElementImpl implements
		ObjectElement {
	private static final long serialVersionUID = 3713511152465032919L;

	protected ObjectElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public void setAttrXlinkHref(String href) {
		if (href == null) {
			removeAttributeNS(XLinkConstants.XLINK_NAMESPACE_URI,
					XLinkConstants.ATTR_HREF);
			return;
		}
		setAttributeNS(XLinkConstants.XLINK_NAMESPACE_URI,
				XLinkConstants.ATTR_HREF, href.trim());
	}

	public String getAttrDrawNotifyOnUpdateOfRanges() {
		if (hasAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ATTR_NOTIFY_ON_UPDATE_OF_RANGES))
			return getAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ATTR_NOTIFY_ON_UPDATE_OF_RANGES);
		return null;
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

	public boolean isPresentationTable() {
		ODFDocument doc = (ODFDocument) this.getOwnerDocument();
		DocumentContentElement content = (DocumentContentElement) doc
				.getDocumentElement();
		ContentType type = content.getBodyElement().getContent()
				.getContentType();
		if (type != ContentType.PRESENTATION)
			return false;

		String fileUrl = doc.getURL();
		String objUrl = this.getAttrXlinkHref();
		if (objUrl.startsWith("./")) { //$NON-NLS-1$
			objUrl = objUrl.substring(2);
		}
		objUrl = objUrl + "/" + ODFConstants.ODF_CONTENT_FILENAME; //$NON-NLS-1$

		ODFParser parser = new ODFParser();
		Document objDoc = parser.getDocument(fileUrl, objUrl);
		if (objDoc == null)
			return false;

		Element objRoot = objDoc.getDocumentElement();
		if (!(objRoot instanceof DocumentContentElement))
			return false;
		BodyElement objBody = ((DocumentContentElement) objRoot)
				.getBodyElement();
		if (objBody == null)
			return false;
		ContentBaseElement objContentBase = objBody.getContent();
		if ((objContentBase != null)
				&& (objContentBase.getContentType() == ContentType.SPREADSHEET))
			return true;

		return false;
	}
}