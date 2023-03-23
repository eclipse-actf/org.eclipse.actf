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
package org.eclipse.actf.model.dom.odf.base.impl;

import org.eclipse.actf.model.dom.odf.ODFException;
import org.eclipse.actf.model.dom.odf.ODFConstants.ContentType;
import org.eclipse.actf.model.dom.odf.base.DrawingObjectElement;
import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.draw.DrawConstants;
import org.eclipse.actf.model.dom.odf.draw.GElement;
import org.eclipse.actf.model.dom.odf.draw.PageElement;
import org.eclipse.actf.model.dom.odf.draw.TextBoxElement;
import org.eclipse.actf.model.dom.odf.office.BodyElement;
import org.eclipse.actf.model.dom.odf.office.DocumentContentElement;
import org.eclipse.actf.model.dom.odf.office.SpreadSheetElement;
import org.eclipse.actf.model.dom.odf.svg.DescElement;
import org.eclipse.actf.model.dom.odf.svg.SVGConstants;
import org.eclipse.actf.model.dom.odf.svg.TitleElement;
import org.eclipse.actf.model.dom.odf.table.TableElement;
import org.eclipse.actf.model.dom.odf.text.TextConstants;
import org.eclipse.actf.util.xpath.XPathService;
import org.eclipse.actf.util.xpath.XPathServiceFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@SuppressWarnings("nls")
public abstract class DrawingObjectElementImpl extends
		DrawingObjectBaseElementImpl implements DrawingObjectElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = -934587640960571482L;
	
	private static final XPathService xpathService = XPathServiceFactory
			.newService();
	private static final Object EXP1 = xpathService
			.compile("./*[(namespace-uri()='" + SVGConstants.SVG_NAMESPACE_URI
					+ "' and local-name()='" + SVGConstants.ELEMENT_TITLE
					+ "')]");

	private static final Object EXP2 = xpathService
			.compile("./*[(namespace-uri()='" + SVGConstants.SVG_NAMESPACE_URI
					+ "' and local-name()='" + SVGConstants.ELEMENT_DESC
					+ "')]");

	protected DrawingObjectElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public String getAttrDrawCaptionId() {
		if (hasAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ATTR_CAPTION_ID))
			return getAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ATTR_CAPTION_ID);
		return null;
	}

	public long getHeight() {
		// TODO Auto-generated method stub
		return -1;
	}

	public long getWidth() {
		// TODO Auto-generated method stub
		return -1;
	}

	public long getX() {
		// TODO Auto-generated method stub
		return -1;
	}

	public long getY() {
		// TODO Auto-generated method stub
		return -1;
	}

	public void setHeight(long height) {
		// TODO Auto-generated method stub

	}

	public void setWidth(long width) {
		// TODO Auto-generated method stub

	}

	public void setX(long x) {
		// TODO Auto-generated method stub

	}

	public void setY(long y) {
		// TODO Auto-generated method stub

	}

	public long getPageIndex() {
		// if presentation doc
		PageElement page = null;
		Node parent = getParentNode();
		while (null != parent) {
			if (parent instanceof PageElement) {
				page = (PageElement) parent;
				break;
			}
			if (parent instanceof BodyElement)
				break;
			parent = parent.getParentNode();
		}
		if (null != page)
			return page.getPageIndex();

		// if spreadsheet doc
		TableElement sheet = null;
		parent = getParentNode();
		while (null != parent) {
			if (parent instanceof TableElement) {
				Node gparent = parent.getParentNode();
				if (gparent instanceof SpreadSheetElement) {
					sheet = (TableElement) parent;
					break;
				}
			}
			if (parent instanceof BodyElement)
				break;
			parent = parent.getParentNode();
		}
		if (null != sheet) {
			int sheetIndex = 0;

			Node preSib = sheet.getPreviousSibling();
			while (preSib != null) {
				if (preSib instanceof TableElement)
					sheetIndex++;
				preSib = preSib.getPreviousSibling();
			}

			return sheetIndex;
		}

		return -1;
	}

	public long getZIndex() {
		// OpenOffice.org save draw:z-index attribute for all graphic object in
		// ODT and ODS
		if (hasAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ATTR_Z_INDEX)) {
			try {
				return new Integer(getAttributeNS(
						DrawConstants.DRAW_NAMESPACE_URI,
						DrawConstants.ATTR_Z_INDEX)).intValue();
			} catch (NumberFormatException e) {
			}
		}

		// OpenOffice.org don't save draw:z-index attribute for all graphic
		// object in ODP
		DocumentContentElement content = (DocumentContentElement) odfDoc
				.getDocumentElement();
		ContentType type = content.getBodyElement().getContent()
				.getContentType();
		if ((type == ContentType.PRESENTATION) || (type == ContentType.DRAW)) {
			Node parent = getParentNode();
			if (parent instanceof PageElement) {
				int zIndex = 0;
				Node preNode = this.getPreviousSibling();
				while (preNode != null) {
					if (preNode instanceof DrawingObjectElement) {
						zIndex++;
					} else if (preNode instanceof Element) {
						NodeList nl = ((Element) preNode)
								.getElementsByTagNameNS(
										DrawConstants.DRAW_NAMESPACE_URI,
										DrawConstants.ELEMENT_CONTROL);
						if ((null != nl) && (nl.getLength() > 0))
							zIndex += nl.getLength();
					}
					preNode = preNode.getPreviousSibling();
				}
				return zIndex;
			} else if (parent instanceof GElement) {
				return ((GElement) parent).getZIndex();
			}
		}
		return -1;
	}

	public TextBoxElement getBoundCaptionTextBoxElement() {
		ODFDocument odfDoc = (ODFDocument) getOwnerDocument();
		return getBoundCaptionTextBoxElement(odfDoc.getODFVersion());
	}

	public TextBoxElement getBoundCaptionTextBoxElement(double version) {
		if (version == 1.0)
			return null;

		String captionId = getAttrDrawCaptionId();
		if (captionId == null)
			return null;

		ODFDocument odfDoc = (ODFDocument) getOwnerDocument();
		ODFElementImpl root = (ODFElementImpl) odfDoc.getDocumentElement();
		ODFElement elem = root.findElementByAttrValue(
				DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ELEMENT_TEXT_BOX,
				TextConstants.TEXT_NAMESPACE_URI, TextConstants.ATTR_ID,
				captionId);
		if ((elem != null) && (elem instanceof TextBoxElement)) {
			return (TextBoxElement) elem;
		}
		new ODFException(
				"'draw:caption-id' does not point to text:box element.")
				.printStackTrace();
		return null;
	}

	public TitleElement getSVGTitleElement() {
		NodeList nl = xpathService.evalForNodeList(EXP1, this);
		if ((nl != null) && (nl.getLength() == 1))
			return (TitleElement) nl.item(0);
		if ((nl != null) && (nl.getLength() > 1)) {
			new ODFException(
					"drawing object has more than one svg:title elements.")
					.printStackTrace();
		}
		return null;
	}

	public DescElement getSVGDescElement() {
		NodeList nl = xpathService.evalForNodeList(EXP2, this);
		if ((nl != null) && (nl.getLength() == 1))
			return (DescElement) nl.item(0);
		if ((nl != null) && (nl.getLength() > 1)) {
			new ODFException(
					"drawing object has more than one svg:desc elements.")
					.printStackTrace();
		}
		return null;
	}
}