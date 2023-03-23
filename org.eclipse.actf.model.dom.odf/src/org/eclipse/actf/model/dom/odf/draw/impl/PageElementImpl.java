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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.actf.model.dom.odf.ODFException;
import org.eclipse.actf.model.dom.odf.base.ContentBaseElement;
import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.base.impl.ODFElementImpl;
import org.eclipse.actf.model.dom.odf.dr3d.Dr3dConstants;
import org.eclipse.actf.model.dom.odf.draw.DrawConstants;
import org.eclipse.actf.model.dom.odf.draw.PageElement;
import org.eclipse.actf.model.dom.odf.office.BodyElement;
import org.eclipse.actf.model.dom.odf.office.DocumentContentElement;
import org.eclipse.actf.model.dom.odf.presentation.NotesElement;
import org.eclipse.actf.model.dom.odf.presentation.PresentationConstants;
import org.eclipse.actf.util.xpath.XPathService;
import org.eclipse.actf.util.xpath.XPathServiceFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class PageElementImpl extends ODFElementImpl implements PageElement {
	private static final long serialVersionUID = 8198841362223795490L;

	private static final XPathService xpathService = XPathServiceFactory
			.newService();

	@SuppressWarnings("nls")
	private static final Object EXP1 = xpathService
			.compile("./*[(namespace-uri()='"
					+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
					+ DrawConstants.ELEMENT_RECT + "') or"
					+ "(namespace-uri()='" + DrawConstants.DRAW_NAMESPACE_URI
					+ "' and local-name()='" + DrawConstants.ELEMENT_LINE
					+ "') or" + "(namespace-uri()='"
					+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
					+ DrawConstants.ELEMENT_POLYLINE + "') or"
					+ "(namespace-uri()='" + DrawConstants.DRAW_NAMESPACE_URI
					+ "' and local-name()='" + DrawConstants.ELEMENT_POLYGON
					+ "') or" + "(namespace-uri()='"
					+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
					+ DrawConstants.ELEMENT_REGULAR_POLYGON + "') or"
					+ "(namespace-uri()='" + DrawConstants.DRAW_NAMESPACE_URI
					+ "' and local-name()='" + DrawConstants.ELEMENT_PATH
					+ "') or" + "(namespace-uri()='"
					+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
					+ DrawConstants.ELEMENT_CIRCLE + "') or"
					+ "(namespace-uri()='" + DrawConstants.DRAW_NAMESPACE_URI
					+ "' and local-name()='" + DrawConstants.ELEMENT_ELLIPSE
					+ "') or" + "(namespace-uri()='"
					+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
					+ DrawConstants.ELEMENT_G + "') or" + "(namespace-uri()='"
					+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
					+ DrawConstants.ELEMENT_PAGE_THUMBNAIL + "') or"
					+ "(namespace-uri()='" + DrawConstants.DRAW_NAMESPACE_URI
					+ "' and local-name()='" + DrawConstants.ELEMENT_MEASURE
					+ "') or" + "(namespace-uri()='"
					+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
					+ DrawConstants.ELEMENT_CAPTION + "') or"
					+ "(namespace-uri()='" + DrawConstants.DRAW_NAMESPACE_URI
					+ "' and local-name()='" + DrawConstants.ELEMENT_CONNECTOR
					+ "') or" + "(namespace-uri()='"
					+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
					+ DrawConstants.ELEMENT_CUSTOM_SHAPE + "') or"
					+ "(namespace-uri()='" + Dr3dConstants.DR3D_NAMESPACE_URI
					+ "' and local-name()='" + Dr3dConstants.ELEMENT_SCENE
					+ "')]");

	@SuppressWarnings("nls")
	private static final Object EXP2 = xpathService
			.compile("./*[namespace-uri()='" + DrawConstants.DRAW_NAMESPACE_URI
					+ "' and local-name()='" + DrawConstants.ELEMENT_PAGE
					+ "']");

	@SuppressWarnings("nls")
	private static final Object EXP3 = xpathService
			.compile("./*[namespace-uri()='"
					+ PresentationConstants.PRESENTATION_NAMESPACE_URI
					+ "' and local-name()='"
					+ PresentationConstants.ELEMENT_NOTES + "']");

	private ContentBaseElement contentElement = null;

	protected PageElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public ODFElement createObject(long x, long y, long width, long height) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getPageIndex() {
		if (odfDoc == null)
			return -1;

		Element root = odfDoc.getDocumentElement();
		if (root instanceof DocumentContentElement) {
			BodyElement body = ((DocumentContentElement) root).getBodyElement();
			contentElement = body.getContent();
		}

		if (contentElement == null)
			return -1;

		NodeList list = XPathServiceFactory.newService().evalForNodeList(EXP2,
				contentElement);
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).equals(this)) {
				return i;
			}
		}

		return -1;
	}

	public String getAttrDrawName() {
		if (hasAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ATTR_NAME))
			return getAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ATTR_NAME);
		return null;
	}

	public String getAttrDrawStyleName() {
		if (hasAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ATTR_STYLE_NAME))
			return getAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ATTR_STYLE_NAME);
		return null;
	}

	public String getAttrDrawMasterPageName() {
		if (hasAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ATTR_MASTER_PAGE_NAME))
			return getAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ATTR_MASTER_PAGE_NAME);
		return null;
	}

	public String getAttrDrawNavOrder() {
		if (hasAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ATTR_NAV_ORDER))
			return getAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ATTR_NAV_ORDER);
		return null;
	}

	private class NavOrderComparator implements Comparator<ODFElement> {
		/*
		 * private boolean isTitleShape(ODFElement shape) { try { if(
		 * "title"==shape.getAttributeNS(PresentationConstants.PRESENTATION_NAMESPACE_URI,
		 * PresentationConstants.ATTR_CLASS) ) { return true; } }
		 * catch(Exception e) {} return false; }
		 */

		private int getNavOrder(ODFElement shape, String navOrders) {
			String id = shape.getAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ATTR_ID);
			if (id != null) {
				int index = navOrders.indexOf(id);
				if (index >= 0) {
					return index;
				}
			}
			return navOrders.length();
		}

		private int getZOrder(ODFElement shape) {
			Node parent = shape.getParentNode();
			if (!(parent instanceof PageElement))
				return -1;

			int index = 0;
			Node preSib = shape.getPreviousSibling();
			while (preSib != null) {
				if (preSib instanceof ODFElement)
					index++;
				preSib = preSib.getPreviousSibling();
			}
			return index;
		}

		public int compare(ODFElement a, ODFElement b) {
			/*
			 * if( isTitleShape(a) ) { return -1; }
			 */
			String navOrders = getAttrDrawNavOrder();
			if (navOrders != null) {
				int oDelta = getNavOrder(a, navOrders)
						- getNavOrder(b, navOrders);
				if (oDelta != 0) {
					return oDelta;
				}
			}
			int oDelta = getZOrder(a) - getZOrder(b);
			if (oDelta != 0) {
				return oDelta;
			}

			return 0;
		}
	}

	public List<ODFElement> getChildNodesInNavOrder() {
		List<ODFElement> children = new ArrayList<ODFElement>();
		NodeList nl = getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i) instanceof ODFElement) {
				children.add((ODFElement) nl.item(i));
			}
		}
		String navOrder = getAttrDrawNavOrder();
		if (navOrder == null)
			return children;

		Collections.sort(children, new NavOrderComparator());
		return children;
	}

	/*
	 * get drawing elements listed below "draw:rect" "draw:line" "draw:polyline"
	 * "draw:polygon" "draw:regular-polygon" "draw:path" "draw:circle"
	 * "draw:ellipse" "draw:g" "draw:page-thumbnail" "draw:measure"
	 * "draw:caption" "draw:connector" "draw:custom-shape" "dr3d:scene"
	 */
	public NodeList getDrawingObjectElements() {
		return xpathService.evalForNodeList(EXP1, this);
	}

	public NotesElement getPresentationNotesElement() {
		NodeList nl = xpathService.evalForNodeList(EXP3, this);
		if ((nl != null) && (nl.getLength() == 1))
			return (NotesElement) nl.item(0);
		else if ((nl != null) && (nl.getLength() > 1)) {
			new ODFException(
					"draw:page has more than one presentation:notes element") //$NON-NLS-1$
					.printStackTrace();
		}
		return null;
	}
}