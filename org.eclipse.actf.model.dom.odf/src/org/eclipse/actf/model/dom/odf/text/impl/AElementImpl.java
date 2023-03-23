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

import org.eclipse.actf.model.dom.odf.ODFException;
import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.base.impl.ODFElementImpl;
import org.eclipse.actf.model.dom.odf.content.IEditListener;
import org.eclipse.actf.model.dom.odf.content.IEditable;
import org.eclipse.actf.model.dom.odf.draw.DrawConstants;
import org.eclipse.actf.model.dom.odf.office.OfficeConstants;
import org.eclipse.actf.model.dom.odf.range.ITextElementContainer;
import org.eclipse.actf.model.dom.odf.range.impl.ITextElementContainerUtil;
import org.eclipse.actf.model.dom.odf.table.TableConstants;
import org.eclipse.actf.model.dom.odf.text.AElement;
import org.eclipse.actf.model.dom.odf.text.BookmarkElement;
import org.eclipse.actf.model.dom.odf.text.TextConstants;
import org.eclipse.actf.model.dom.odf.xlink.XLinkConstants;
import org.eclipse.actf.util.xpath.XPathService;
import org.eclipse.actf.util.xpath.XPathServiceFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@SuppressWarnings("nls")
class AElementImpl extends ODFElementImpl implements AElement {
	private static final long serialVersionUID = 884129871818293857L;

	private static final XPathService xpathService = XPathServiceFactory
			.newService();
	private static final Object EXP1 = xpathService
			.compile(".//*[namespace-uri()='"
					+ TextConstants.TEXT_NAMESPACE_URI + "' and local-name()='"
					+ TextConstants.ELEMENT_H + "']"
					+ "[parent::*[namespace-uri()!='"
					+ TextConstants.TEXT_NAMESPACE_URI + "' or local-name()!='"
					+ TextConstants.ELEMENT_LIST_ITEM + "'][namespace-uri()!='"
					+ TextConstants.TEXT_NAMESPACE_URI + "' or local-name()!='"
					+ TextConstants.ELEMENT_LIST_HEADER + "']]");

	protected AElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public String getAttrOfficeTitle() {
		if (hasAttributeNS(OfficeConstants.OFFICE_NAMESPACE_URI,
				OfficeConstants.ATTR_TITLE))
			return getAttributeNS(OfficeConstants.OFFICE_NAMESPACE_URI,
					OfficeConstants.ATTR_TITLE);
		return null;
	}

	public String getType() {
		if (hasAttributeNS(XLinkConstants.XLINK_NAMESPACE_URI,
				XLinkConstants.ATTR_TYPE))
			return getAttributeNS(XLinkConstants.XLINK_NAMESPACE_URI,
					XLinkConstants.ATTR_TYPE);
		return null;
	}

	public void setType(String typename) {
		setAttributeNS(XLinkConstants.XLINK_NAMESPACE_URI,
				XLinkConstants.ATTR_TYPE, typename);
	}

	public String getHref() {
		if (hasAttributeNS(XLinkConstants.XLINK_NAMESPACE_URI,
				XLinkConstants.ATTR_HREF))
			return getAttributeNS(XLinkConstants.XLINK_NAMESPACE_URI,
					XLinkConstants.ATTR_HREF);
		return null;
	}

	public void setHref(String url) {
		setAttributeNS(XLinkConstants.XLINK_NAMESPACE_URI,
				XLinkConstants.ATTR_HREF, url);
	}

	public Object getValue() {
		return this.getTextContent();
	}

	public void setValue(Object data) {
		if (data instanceof String) {
			this.setTextContent((String) data);
		} else {
			new ODFException("invalid object for text:a setValue function.")
					.printStackTrace();
		}
	}

	public void remove() {
		// TODO Auto-generated method stub
	}

	public void insert(IEditable data) {
		// TODO Auto-generated method stub
	}

	public void addEditListener(IEditListener observer, String topic) {
		// TODO Auto-generated method stub
	}

	public void removeEditListener(IEditListener observer, String topic) {
		// TODO Auto-generated method stub
	}

	public long getContentSize() {
		return ITextElementContainerUtil.getContentSize(this);
	}

	public Iterator<ITextElementContainer> getChildIterator() {
		return ITextElementContainerUtil.getChildIterator(this);
	}

	/*
	 * Internal link literal is not defined in ODF spec. This function might not
	 * be compatible with OpenOffice.
	 */
	public ODFElement getHrefElement() {
		String type = getType();
		if (XLinkConstants.LINK_TYPE_SIMPLE.equals(type)) {
			String href = getHref();
			if ((href.startsWith("#")) && (href.endsWith("|outline"))) { // link
				// for
				// header
				String outlineContents = href.substring(1, href.length() - 8);
				// calculate list level of heading element
				// if level==1, it is not included within list structure
				int level = 0;
				while (true) {
					int indexEndPos = outlineContents.indexOf('.');
					if (indexEndPos == -1)
						break;
					try {
						String prefixnum = outlineContents.substring(0,
								indexEndPos);
						new Integer(prefixnum).intValue();
						outlineContents = outlineContents.substring(2).trim();
					} catch (Exception e) {
						break;
					}
					level++;
				}

				// search heading element from list structure, if list level is
				// more than 1
				if (level > 1) {
					NodeList nl = xpathService.evalPathForNodeList(
							".//*[namespace-uri()='"
									+ TextConstants.TEXT_NAMESPACE_URI
									+ "' and local-name()='"
									+ TextConstants.ELEMENT_H
									+ "'][attribute::*[namespace-uri()='"
									+ TextConstants.TEXT_NAMESPACE_URI
									+ "' and local-name()='"
									+ TextConstants.ATTR_OUTLINE_LEVEL + "']='"
									+ level + "']", getOwnerDocument()
									.getDocumentElement());
					if (nl != null) {
						for (int i = 0; i < nl.getLength(); i++) {
							ODFElement elem = (ODFElement) nl.item(i);
							String content = elem.getTextContent();
							if ((content != null)
									&& (outlineContents.equals(content.trim()))) {
								return elem;
							}
						}
					}
				}

				// search heading element from heading elements which are not
				// included in list structure
				NodeList hElemList = xpathService.evalForNodeList(EXP1,
						getOwnerDocument().getDocumentElement());
				int indexEndPos = href.indexOf('.');
				if (indexEndPos != -1) {
					int index = -1;
					outlineContents = href.substring(indexEndPos + 1,
							href.length() - 8).trim();
					try {
						index = new Integer(href.substring(1, indexEndPos))
								.intValue() - 1;
					} catch (Exception e) {
					}
					if (index != -1) {
						if ((hElemList != null)
								&& (hElemList.getLength() > index)) {
							ODFElement elem = (ODFElement) hElemList
									.item(index);
							String content = elem.getTextContent();
							if ((content != null)
									&& (outlineContents.equals(content.trim()))) {
								return elem;
							}
						}
					}
				}

				// if href URI does not start with number, or link target cannot
				// be found,
				// search heading element by its text content
				outlineContents = href.substring(1, href.length() - 8);
				for (int i = 0; i < hElemList.getLength(); i++) {
					ODFElement elem = (ODFElement) hElemList.item(i);
					String content = elem.getTextContent();
					if ((content != null)
							&& (outlineContents.equals(content.trim()))) {
						return elem;
					}
				}
			} else if ((href.startsWith("#")) && (href.endsWith("|graphic"))) { // link
				// for
				// graphic
				// object
				String drawName = href.substring(1, href.length() - 8);
				return findElementByAttrValue(DrawConstants.DRAW_NAMESPACE_URI,
						DrawConstants.ELEMENT_FRAME,
						DrawConstants.DRAW_NAMESPACE_URI,
						DrawConstants.ATTR_NAME, drawName);
			} else if ((href.startsWith("#")) && (href.endsWith("|table"))) { // link
				// for
				// table
				String tableName = href.substring(1, href.length() - 6);
				return findElementByAttrValue(
						TableConstants.TABLE_NAMESPACE_URI,
						TableConstants.ELEMENT_TABLE,
						TableConstants.TABLE_NAMESPACE_URI,
						TableConstants.ATTR_NAME, tableName);
			} else if ((href.startsWith("#")) && (href.endsWith("|frame"))) { // link
				// for
				// frame
				String frameName = href.substring(1, href.length() - 6);
				return findElementByAttrValue(DrawConstants.DRAW_NAMESPACE_URI,
						DrawConstants.ELEMENT_FRAME,
						DrawConstants.DRAW_NAMESPACE_URI,
						DrawConstants.ATTR_NAME, frameName);
			} else if ((href.startsWith("#")) && (href.endsWith("|ole"))) { // link
				// for
				// OLE
				// object
				String oleName = href.substring(1, href.length() - 4);
				return findElementByAttrValue(DrawConstants.DRAW_NAMESPACE_URI,
						DrawConstants.ELEMENT_FRAME,
						DrawConstants.DRAW_NAMESPACE_URI,
						DrawConstants.ATTR_NAME, oleName);
			} else if ((href.startsWith("#")) && (href.endsWith("|region"))) { // link
				// for
				// section,
				// or
				// table
				// of
				// contents
				String regionName = href.substring(1, href.length() - 7);
				ODFElement elem = findElementByAttrValue(
						TextConstants.TEXT_NAMESPACE_URI,
						TextConstants.ELEMENT_SECTION,
						TextConstants.TEXT_NAMESPACE_URI,
						TextConstants.ATTR_NAME, regionName);
				if (elem != null)
					return elem;
				elem = findElementByAttrValue(TextConstants.TEXT_NAMESPACE_URI,
						TextConstants.ELEMENT_TABLE_OF_CONTENT,
						TextConstants.TEXT_NAMESPACE_URI,
						TextConstants.ATTR_NAME, regionName);
				if (elem != null)
					return elem;
				elem = findElementByAttrValue(TextConstants.TEXT_NAMESPACE_URI,
						TextConstants.ELEMENT_INDEX_TITLE,
						TextConstants.TEXT_NAMESPACE_URI,
						TextConstants.ATTR_NAME, regionName);
				if (elem != null)
					return elem;
			} else if (href.startsWith("#")) { // link for bookmark
				String bookmarkName = href.substring(1);
				ODFElement elem = findElementByAttrValue(
						TextConstants.TEXT_NAMESPACE_URI,
						TextConstants.ELEMENT_BOOKMARK,
						TextConstants.TEXT_NAMESPACE_URI,
						TextConstants.ATTR_NAME, bookmarkName);
				if (elem instanceof BookmarkElement)
					return elem;
			}
		}
		return null;
	}
}