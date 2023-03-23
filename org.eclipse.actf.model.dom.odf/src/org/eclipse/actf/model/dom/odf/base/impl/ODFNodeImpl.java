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
import org.eclipse.actf.model.dom.odf.base.ODFNode;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;


class ODFNodeImpl implements ODFNode {
	protected ODFDocumentImpl odfDoc = null;

	protected Node iNode = null;

	// constructure for ODFDocumentImpl
	protected ODFNodeImpl(Node iNode) {
		this.odfDoc = (ODFDocumentImpl) this;
		this.iNode = iNode;
	}

	protected ODFNodeImpl(ODFDocument odfDoc, Node iNode) {
		this.odfDoc = (ODFDocumentImpl) odfDoc;
		this.iNode = iNode;
	}

	protected Node getInternalNode() {
		return iNode;
	}

	public Node appendChild(Node newChild) throws DOMException {
		if (newChild instanceof ODFNodeImpl) {
			Node iNewChild = ((ODFNodeImpl) newChild).getInternalNode();
			Node iResultNode = iNode.appendChild(iNewChild);
			return new ODFNodeImpl(odfDoc, iResultNode);
		}
		return null;
	}

	public Node cloneNode(boolean deep) {
		if (odfDoc == null)
			return null;

		Node iCloneNode = iNode.cloneNode(deep);
		if (iCloneNode == null)
			return null;

		return odfDoc.getODFNode(iCloneNode);
	}

	public short compareDocumentPosition(Node other) throws DOMException {
		if (other instanceof ODFNodeImpl) {
			Node iOther = ((ODFNodeImpl) other).getInternalNode();
			return iNode.compareDocumentPosition(iOther);
		}
		return 0;
	}

	public NamedNodeMap getAttributes() {
		if (odfDoc == null)
			return null;

		NamedNodeMap iAttrs = iNode.getAttributes();
		if (iAttrs == null)
			return null;
		return new ODFNamedNodeMapImpl(odfDoc, iAttrs);
	}

	public String getBaseURI() {
		return iNode.getBaseURI();
	}

	public NodeList getChildNodes() {
		return new ODFNodeListImpl(odfDoc, iNode.getChildNodes());
	}

	public Object getFeature(String feature, String version) {
		throw new UnsupportedOperationException();
	}

	public Node getFirstChild() {
		if (odfDoc == null)
			return null;

		Node iFirstChild = iNode.getFirstChild();
		if (iFirstChild == null)
			return null;

		return odfDoc.getODFNode(iFirstChild);
	}

	public Node getLastChild() {
		if (odfDoc == null)
			return null;

		Node iLastChild = iNode.getLastChild();
		if (iLastChild == null)
			return null;

		return odfDoc.getODFNode(iLastChild);
	}

	public String getLocalName() {
		return iNode.getLocalName();
	}

	public String getNamespaceURI() {
		return iNode.getNamespaceURI();
	}

	public Node getNextSibling() {
		if (odfDoc == null)
			return null;

		Node iNextSibling = iNode.getNextSibling();
		if (iNextSibling == null)
			return null;

		return odfDoc.getODFNode(iNextSibling);
	}

	public String getNodeName() {
		return iNode.getNodeName();
	}

	public short getNodeType() {
		return iNode.getNodeType();
	}

	public String getNodeValue() throws DOMException {
		return iNode.getNodeValue();
	}

	public Document getOwnerDocument() {
		return odfDoc;
	}

	public Node getParentNode() {
		Node iParent = iNode.getParentNode();
		if (iParent == null)
			return null;
		return odfDoc.getODFNode(iParent);
	}

	public String getPrefix() {
		return iNode.getPrefix();
	}

	public Node getPreviousSibling() {
		if (odfDoc == null)
			return null;

		Node iPreviousSibling = iNode.getPreviousSibling();
		if (iPreviousSibling == null)
			return null;

		return odfDoc.getODFNode(iPreviousSibling);
	}

	public String getTextContent() throws DOMException {
		return iNode.getTextContent();
	}

	public Object getUserData(String key) {
		return iNode.getUserData(key);
	}

	public boolean hasAttributes() {
		return iNode.hasAttributes();
	}

	public boolean hasChildNodes() {
		return iNode.hasChildNodes();
	}

	public Node insertBefore(Node newChild, Node refChild) throws DOMException {
		if (odfDoc == null)
			return null;

		if ((newChild instanceof ODFNodeImpl)
				&& (refChild instanceof ODFNodeImpl)) {
			Node iNewChild = ((ODFNodeImpl) newChild).getInternalNode();
			Node iRefChild = ((ODFNodeImpl) refChild).getInternalNode();
			Node iInsertedChild = iNode.insertBefore(iNewChild, iRefChild);
			if (iInsertedChild == null)
				return null;
			return odfDoc.getODFNode(iInsertedChild);
		}
		return null;
	}

	public boolean isDefaultNamespace(String namespaceURI) {
		return iNode.isDefaultNamespace(namespaceURI);
	}

	public boolean isEqualNode(Node arg) {
		if (arg instanceof ODFNodeImpl) {
			Node iarg = ((ODFNodeImpl) arg).getInternalNode();
			return iNode.isEqualNode(iarg);
		}
		return false;
	}

	public boolean isSameNode(Node other) {
		if (other instanceof ODFNodeImpl) {
			Node iother = ((ODFNodeImpl) other).getInternalNode();
			return iNode.isSameNode(iother);
		}
		return false;
	}

	public boolean isSupported(String feature, String version) {
		return iNode.isSupported(feature, version);
	}

	public String lookupNamespaceURI(String prefix) {
		return iNode.lookupNamespaceURI(prefix);
	}

	public String lookupPrefix(String namespaceURI) {
		return iNode.lookupPrefix(namespaceURI);
	}

	public void normalize() {
		// TODO Auto-generated method stub

	}

	public Node removeChild(Node oldChild) throws DOMException {
		if (odfDoc == null)
			return null;

		if (oldChild instanceof ODFNodeImpl) {
			Node iOldChild = ((ODFNodeImpl) oldChild).getInternalNode();
			Node iRemovedChild = iNode.removeChild(iOldChild);
			return odfDoc.getODFNode(iRemovedChild);
		}
		return null;
	}

	public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
		if (odfDoc == null)
			return null;

		if ((newChild instanceof ODFNodeImpl)
				&& (oldChild instanceof ODFNodeImpl)) {
			Node iNewChild = ((ODFNodeImpl) newChild).getInternalNode();
			Node iOldChild = ((ODFNodeImpl) oldChild).getInternalNode();
			Node iReplacedChild = iNode.replaceChild(iNewChild, iOldChild);
			return odfDoc.getODFNode(iReplacedChild);
		}
		return null;
	}

	public void setNodeValue(String nodeValue) throws DOMException {
		iNode.setNodeValue(nodeValue);
	}

	public void setPrefix(String prefix) throws DOMException {
		iNode.setPrefix(prefix);
	}

	public void setTextContent(String textContent) throws DOMException {
		iNode.setTextContent(textContent);
	}

	public Object setUserData(String key, Object data, UserDataHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

}
