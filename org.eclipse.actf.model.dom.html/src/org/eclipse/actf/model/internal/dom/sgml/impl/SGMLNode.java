/*******************************************************************************
 * Copyright (c) 1998, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Goh KONDOH - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.internal.dom.sgml.impl;

import org.eclipse.actf.model.internal.dom.sgml.ISGMLNode;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

/**
 * Base class of org.w3c.dom.Node implementation. This class implements most
 * methods except for a few which depends on concrete its classes.
 */
public abstract class SGMLNode implements ISGMLNode {
	private static final String CANNOT_HAVE_CHILDREN = " cannot have children."; //$NON-NLS-1$

	/**
	 * 
	 */
	private static final long serialVersionUID = -5762248895816715875L;

	SGMLNode previousSibling, nextSibling, parent;

	Document ownerDocument;

	SGMLNode(Document doc) {
		ownerDocument = doc;
	}

	public Node cloneNode(boolean deep) {
		try {
			SGMLNode ret = (SGMLNode) clone();
			ret.parent = ret.nextSibling = ret.previousSibling = null;
			return ret;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * default implementation. Element must override this method.
	 */
	public NamedNodeMap getAttributes() {
		return null;
	}

	private static NodeList nullNodeList = new NodeList() {
		public Node item(int index) {
			return null;
		}

		public int getLength() {
			return 0;
		}
	};

	public NodeList getChildNodes() {
		return nullNodeList;
	}

	public Node getFirstChild() {
		return null;
	}

	public Node getLastChild() {
		return null;
	}

	public Node getNextSibling() {
		return this.nextSibling;
	}

	public Document getOwnerDocument() {
		return ownerDocument;
	}

	public Node getParentNode() {
		return parent;
	}

	public Node getPreviousSibling() {
		return previousSibling;
	}

	public boolean hasChildNodes() {
		return false;
	}

	public Node appendChild(Node node) throws DOMException {
		throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR, this
				+ CANNOT_HAVE_CHILDREN) {

					/**
					 * 
					 */
					private static final long serialVersionUID = -1406388562000040267L;
		};
	}

	public Node insertBefore(Node newChild, Node refChild) throws DOMException {
		throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR, this
				+ CANNOT_HAVE_CHILDREN) {

					/**
					 * 
					 */
					private static final long serialVersionUID = 6548304333974360594L;
		};
	}

	public Node removeChild(Node oldChild) throws DOMException {
		throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR, this
				+ CANNOT_HAVE_CHILDREN) {

					/**
					 * 
					 */
					private static final long serialVersionUID = -8738956390455008177L;
		};
	}

	public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
		throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR, this
				+ CANNOT_HAVE_CHILDREN) {

					/**
					 * 
					 */
					private static final long serialVersionUID = -674952964898126813L;
		};
	}

	// DOM Level 2

	public void normalize() {
	}

	/**
	 * @return always false because this is not XML implementation but SGML.
	 */
	public boolean supports(String feature, String version) {
		return false;
	}

	/**
	 * @return always null
	 */
	public String getNamespaceURI() {
		return null;
	}

	/**
	 * @return always null
	 */
	public String getPrefix() {
		return null;
	}

	/**
	 * Does nothing.
	 */
	public void setPrefix(String prefix) throws DOMException {
	}

	/**
	 * @return always null
	 */
	public String getLocalName() {
		return null;
	}

	public boolean isSupported(String feature, String version) {
		return false;
	}

	public boolean hasAttributes() {
		return false;
	}

	/**
	 * DOM Level 3
	 */

	public short compareDocumentPosition(Node other) throws DOMException {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getBaseURI() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getFeature(String feature, String version) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTextContent() throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getUserData(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isDefaultNamespace(String namespaceURI) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isEqualNode(Node arg) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSameNode(Node other) {
		// TODO Auto-generated method stub
		return false;
	}

	public String lookupNamespaceURI(String prefix) {
		// TODO Auto-generated method stub
		return null;
	}

	public String lookupPrefix(String namespaceURI) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setTextContent(String textContent) throws DOMException {
		// TODO Auto-generated method stub

	}

	public Object setUserData(String key, Object data, UserDataHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

}
