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

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.ODFNamedNodeMap;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;


class ODFNamedNodeMapImpl implements ODFNamedNodeMap {
	protected ODFDocumentImpl odfDoc = null;

	private NamedNodeMap iAttrs;

	public ODFNamedNodeMapImpl(ODFDocument odfDoc, NamedNodeMap iAttrs) {
		this.odfDoc = (ODFDocumentImpl) odfDoc;
		this.iAttrs = iAttrs;
	}

	public Node item(int index) {
		Node iNode = iAttrs.item(index);
		if (iNode == null)
			return null;
		return odfDoc.getODFNode(iNode);
	}

	public int getLength() {
		return iAttrs.getLength();
	}

	public Node getNamedItem(String name) {
		Node iNode = iAttrs.getNamedItem(name);
		if (iNode == null)
			return null;
		return odfDoc.getODFNode(iNode);
	}

	public Node setNamedItem(Node node) throws DOMException {
		Node iNode = iAttrs.setNamedItem(node);
		if (iNode == null)
			return null;
		return odfDoc.getODFNode(iNode);
	}

	public Node removeNamedItem(String name) throws DOMException {
		Node iNode = iAttrs.removeNamedItem(name);
		if (iNode == null)
			return null;
		return odfDoc.getODFNode(iNode);
	}

	public Node getNamedItemNS(String namespaceURI, String localName) {
		Node iNode = iAttrs.getNamedItemNS(namespaceURI, localName);
		if (iNode == null)
			return null;
		return odfDoc.getODFNode(iNode);
	}

	public Node setNamedItemNS(Node node) throws DOMException {
		Node iNode = iAttrs.setNamedItemNS(node);
		if (iNode == null)
			return null;
		return odfDoc.getODFNode(iNode);
	}

	public Node removeNamedItemNS(String namespaceURI, String localName)
			throws DOMException {
		Node iNode = iAttrs.removeNamedItemNS(namespaceURI, localName);
		if (iNode == null)
			return null;
		return odfDoc.getODFNode(iNode);
	}
}
