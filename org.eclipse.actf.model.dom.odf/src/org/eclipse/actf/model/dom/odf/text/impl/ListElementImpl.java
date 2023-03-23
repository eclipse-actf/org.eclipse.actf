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

import java.util.Iterator;

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.base.impl.ODFStylableElementImpl;
import org.eclipse.actf.model.dom.odf.range.ITextElementContainer;
import org.eclipse.actf.model.dom.odf.style.StyleConstants;
import org.eclipse.actf.model.dom.odf.style.StyleElement;
import org.eclipse.actf.model.dom.odf.text.ListElement;
import org.eclipse.actf.model.dom.odf.text.ListItemElement;
import org.eclipse.actf.model.dom.odf.text.ListLevelStyleBulletElement;
import org.eclipse.actf.model.dom.odf.text.ListLevelStyleNumberElement;
import org.eclipse.actf.model.dom.odf.text.ListStyleElement;
import org.eclipse.actf.model.dom.odf.text.TextConstants;
import org.eclipse.actf.util.xpath.XPathService;
import org.eclipse.actf.util.xpath.XPathServiceFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class ListElementImpl extends ODFStylableElementImpl implements ListElement {
	private static final long serialVersionUID = -1816073361921569606L;

	private static final XPathService xpathService = XPathServiceFactory
			.newService();

	@SuppressWarnings("nls")
	private static final Object EXP1 = xpathService
			.compile("./*[namespace-uri()='" + TextConstants.TEXT_NAMESPACE_URI
					+ "' and local-name()='list-item']");

	protected ListElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public long getSize() {
		return xpathService.evalForNodeList(EXP1, this).getLength();
	}

	@SuppressWarnings("nls")
	public ListItemElement getItem(long idx) {
		NodeList nodeList = xpathService.evalPathForNodeList(
				"./*[namespace-uri()='" + TextConstants.TEXT_NAMESPACE_URI
						+ "' and local-name()='list-item'][" + (idx + 1) + "]",
				this);
		if ((nodeList != null) && (nodeList.getLength() == 1)
				&& (nodeList.item(0) instanceof ListItemElement))
			return (ListItemElement) nodeList.item(0);
		return null;
	}

	public StyleElement getStyle() {
		ListElement topLevelList = getTopLevelListElement();
		String styleName = topLevelList
				.getAttributeNS(TextConstants.TEXT_NAMESPACE_URI,
						TextConstants.ATTR_STYLE_NAME);
		ODFElement elem = findElementByAttrValue(
				TextConstants.TEXT_NAMESPACE_URI,
				TextConstants.ELEMENT_LIST_STYLE,
				StyleConstants.STYLE_NAMESPACE_URI, StyleConstants.ATTR_NAME,
				styleName);
		if ((elem != null) && (elem instanceof ListStyleElement))
			return (StyleElement) elem;
		return null;
	}

	public void appendItem(ListItemElement item) {
		// TODO Auto-generated method stub

	}

	public void insertBefore(ListItemElement item, long idx) {
		// TODO Auto-generated method stub

	}

	public long getContentSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Iterator<ITextElementContainer> getChildIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	public long getListLevel() {
		long level = 1;
		ListElement topListElem = this;
		while (true) {
			Node parent = topListElem.getParentNode();
			Node gparent = null;
			if (parent != null)
				gparent = parent.getParentNode();
			if ((parent != null) && (parent instanceof ListItemElement)
					&& (gparent != null) && (gparent instanceof ListElement)) {
				topListElem = (ListElement) gparent;
				level++;
			} else {
				break;
			}
		}
		return level;
	}

	public ListElement getTopLevelListElement() {
		ListElement topListElem = this;
		while (true) {
			Node parent = topListElem.getParentNode();
			Node gparent = null;
			if (parent != null)
				gparent = parent.getParentNode();
			if ((parent != null) && (parent instanceof ListItemElement)
					&& (gparent != null) && (gparent instanceof ListElement)) {
				topListElem = (ListElement) gparent;
			} else {
				break;
			}
		}
		return topListElem;
	}

	public StyleElement getListLevelStyleElement() {
		long level = getListLevel();
		StyleElement style = getStyle();
		if (style == null)
			return null;
		NodeList children = style.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child instanceof StyleElement) {
				if ((child instanceof ListLevelStyleBulletElement)
						&& (((ListLevelStyleBulletElement) child)
								.getAttrLevel() == level)) {
					return (StyleElement) child;
				} else if ((child instanceof ListLevelStyleNumberElement)
						&& (((ListLevelStyleNumberElement) child)
								.getAttrLevel() == level)) {
					return (StyleElement) child;
				}
			}
		}
		return null;
	}
}