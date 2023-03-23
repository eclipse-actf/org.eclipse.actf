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

import java.io.Serializable;

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.office.DocumentStylesElement;
import org.eclipse.actf.util.xpath.XPathServiceFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;

public class ODFElementImpl extends ODFNodeImpl implements ODFElement,
		Serializable {
	private static final long serialVersionUID = -4579652376938577296L;

	public ODFElementImpl(ODFDocument odfDoc, Element iElement) {
		super(odfDoc, iElement);
	}

	@SuppressWarnings("nls")
	public ODFElement findElementByAttrValue(String elemNamespaceURI,
			String elemLocalName, String attrNamespaceURI,
			String attrLocalName, String attrValue) {
		Element root = getOwnerDocument().getDocumentElement();
		NodeList nl = XPathServiceFactory.newService().evalPathForNodeList(
				".//*[namespace-uri()='" + elemNamespaceURI
						+ "' and local-name()='" + elemLocalName + "']"
						+ "[attribute::*[namespace-uri()='" + attrNamespaceURI
						+ "' and local-name()='" + attrLocalName + "']='"
						+ attrValue + "']", root);
		if ((nl != null) && (nl.getLength() != 0)) {
			return (ODFElement) nl.item(0);
		}
		return null;
	}

	public ODFElement findElementByAttrValueFromStyleDoc(
			String elemNamespaceURI, String elemLocalName,
			String attrNamespaceURI, String attrLocalName, String attrValue) {
		ODFDocument styleDoc = ((ODFDocument) getOwnerDocument())
				.getStyleDocument();
		if (styleDoc == null)
			return null;
		Element styleRoot = styleDoc.getDocumentElement();
		if ((styleRoot != null) && (styleRoot instanceof DocumentStylesElement)) {
			return ((ODFElementImpl) styleRoot).findElementByAttrValue(
					elemNamespaceURI, elemLocalName, attrNamespaceURI,
					attrLocalName, attrValue);
		}
		return null;
	}

	public String getAttribute(String name) {
		if (iNode instanceof Element) {
			return ((Element) iNode).getAttribute(name);
		}
		return null;
	}

	public String getAttributeNS(String namespaceURI, String localName)
			throws DOMException {
		if (iNode instanceof Element) {
			return ((Element) iNode).getAttributeNS(namespaceURI, localName);
		}
		return null;
	}

	public Attr getAttributeNode(String name) {
		if (iNode instanceof Element) {
			Attr iAttr = ((Element) iNode).getAttributeNode(name);
			if (iAttr == null)
				return null;

			return (Attr) odfDoc.getODFNode(iAttr);
		}
		return null;
	}

	public Attr getAttributeNodeNS(String namespaceURI, String localName)
			throws DOMException {
		if (iNode instanceof Element) {
			Attr iAttr = ((Element) iNode).getAttributeNodeNS(namespaceURI,
					localName);
			if (iAttr == null)
				return null;

			return (Attr) odfDoc.getODFNode(iAttr);
		}
		return null;
	}

	public NodeList getElementsByTagName(String name) {
		if (iNode instanceof Element) {
			NodeList iNodeList = ((Element) iNode).getElementsByTagName(name);
			return new ODFNodeListImpl(odfDoc, iNodeList);
		}
		return null;
	}

	public NodeList getElementsByTagNameNS(String namespaceURI, String localName)
			throws DOMException {
		if (iNode instanceof Element) {
			NodeList iNodeList = ((Element) iNode).getElementsByTagNameNS(
					namespaceURI, localName);
			return new ODFNodeListImpl(odfDoc, iNodeList);
		}
		return null;
	}

	public TypeInfo getSchemaTypeInfo() {
		if (iNode instanceof Element) {
			return ((Element) iNode).getSchemaTypeInfo();
		}
		return null;
	}

	public String getTagName() {
		if (iNode instanceof Element) {
			return ((Element) iNode).getTagName();
		}
		return null;
	}

	public boolean hasAttribute(String name) {
		if (iNode instanceof Element) {
			return ((Element) iNode).hasAttribute(name);
		}
		return false;
	}

	public boolean hasAttributeNS(String namespaceURI, String localName)
			throws DOMException {
		if (iNode instanceof Element) {
			return ((Element) iNode).hasAttributeNS(namespaceURI, localName);
		}
		return false;
	}

	public void removeAttribute(String name) throws DOMException {
		if (iNode instanceof Element) {
			((Element) iNode).removeAttribute(name);
		}
	}

	public void removeAttributeNS(String namespaceURI, String localName)
			throws DOMException {
		if (iNode instanceof Element) {
			((Element) iNode).removeAttributeNS(namespaceURI, localName);
		}
	}

	public Attr removeAttributeNode(Attr oldAttr) throws DOMException {
		if (iNode instanceof Element) {
			Attr iRemovedAttr = ((Element) iNode).removeAttributeNode(oldAttr);
			odfDoc.getODFNode(iRemovedAttr);
		}
		return null;
	}

	public void setAttribute(String name, String value) throws DOMException {
		((Element) iNode).setAttribute(name, value);
		Attr iNewAttr = ((Element) iNode).getAttributeNode(name);

		if (iNewAttr == null)
			return;
		odfDoc.getODFNode(iNewAttr);
		return;
	}

	public void setAttributeNS(String namespaceURI, String qualifiedName,
			String value) throws DOMException {
		((Element) iNode).setAttributeNS(namespaceURI, qualifiedName, value);
		Attr iNewAttr = ((Element) iNode).getAttributeNodeNS(namespaceURI,
				qualifiedName);

		if (iNewAttr == null)
			return;
		odfDoc.getODFNode(iNewAttr);
		return;
	}

	public Attr setAttributeNode(Attr newAttr) throws DOMException {
		if ((iNode instanceof Element) && (newAttr instanceof ODFNodeImpl)) {
			Node iNewAttr = ((ODFNodeImpl) newAttr).getInternalNode();
			if (iNewAttr instanceof Attr) {
				try {
					((Element) iNode).setAttributeNode((Attr) iNewAttr);
				} catch (Exception e) {
					e.printStackTrace();
				}

				// if (iNewAttr == null)
				// return null;
				return (Attr) odfDoc.getODFNode(iNewAttr);
			}
		}
		return null;
	}

	public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
		return setAttributeNode(newAttr);
	}

	public void setIdAttribute(String name, boolean isId) throws DOMException {
		((Element) iNode).setIdAttribute(name, isId);
	}

	public void setIdAttributeNS(String namespaceURI, String localName,
			boolean isId) throws DOMException {
		((Element) iNode).setIdAttributeNS(namespaceURI, localName, isId);
	}

	public void setIdAttributeNode(Attr idAttr, boolean isId)
			throws DOMException {
		Node internalIdAttr = ((ODFNodeImpl) idAttr).getInternalNode();
		if ((iNode instanceof Element) && (internalIdAttr instanceof Attr)) {
			((Element) iNode).setIdAttributeNode((Attr) internalIdAttr, isId);
		}
	}
}