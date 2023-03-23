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

import java.io.Serializable;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

@SuppressWarnings("nls")
class SGMLAttribute implements Attr, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1931516269113572264L;

	// Node interface;
	public String getNodeName() {
		return name;
	}

	public String getNodeValue() {
		return value;
	}

	public void setNodeValue(String nodeValue) throws DOMException {
		this.value = nodeValue;
		this.isSpecified = true;
	}

	public short getNodeType() {
		return ATTRIBUTE_NODE;
	}

	public Node getParentNode() {
		return null;
	}

	public NodeList getChildNodes() {
		return new NodeList() {
			public Node item(int index) {
				return null;
			}

			public int getLength() {
				return 0;
			}
		};
	}

	public Node getFirstChild() {
		return null;
	}

	public Node getLastChild() {
		return null;
	}

	public Node getPreviousSibling() {
		return null;
	}

	public Node getNextSibling() {
		return null;
	}

	public NamedNodeMap getAttributes() {
		return null;
	}

	public Document getOwnerDocument() {
		return ownerDocument;
	}

	public Node insertBefore(Node newChild, Node refChild) throws DOMException {
		throw new DOMException(DOMException.NO_DATA_ALLOWED_ERR,
				"attributes can't have siblings.") {
			private static final long serialVersionUID = 6136151862849266055L;
		};
	}

	public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
		throw new DOMException(DOMException.NO_DATA_ALLOWED_ERR,
				"attributes can't have siblings.") {
			private static final long serialVersionUID = 3768806169937347988L;
		};
	}

	public Node removeChild(Node newChild) throws DOMException {
		throw new DOMException(DOMException.NO_DATA_ALLOWED_ERR,
				"attributes can't have siblings.") {
			private static final long serialVersionUID = 8504678875617246364L;
		};
	}

	public Node appendChild(Node newChild) throws DOMException {
		throw new DOMException(DOMException.NO_DATA_ALLOWED_ERR,
				"attributes can't have siblings.") {
			private static final long serialVersionUID = -3997286265892179681L;
		};
	}

	public boolean hasChildNodes() {
		return false;
	}

	public Node cloneNode(boolean deep) {
		return new SGMLAttribute(this);
	}

	// Attr interface
	public String getName() {
		return name;
	}

	/**
	 * Not supported.
	 */
	public boolean getSpecified() {
		return isSpecified;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
		this.isSpecified = true;
	}

	String name, value;

	boolean isSpecified = false;

	private Document ownerDocument;

	SGMLAttribute(String name, String value, Document doc) {
		this.name = name;
		this.value = value;
		this.ownerDocument = doc;
	}

	// copy constructor
	SGMLAttribute(SGMLAttribute another) {
		this.name = another.name;
		this.value = another.value;
		this.isSpecified = another.isSpecified;
		this.ownerDocument = another.ownerDocument;
	}

	public String toString() {
		SGMLDocument doc = (SGMLDocument) getOwnerDocument();
		StringBuffer sb = new StringBuffer();
		char quote = '"';
		for (int i = 0; i < value.length(); i++) {
			char ch = value.charAt(i);
			String entityOrigin = doc.getEntityOrigin(new Character(ch));
			if (entityOrigin != null) {
				sb.append('&' + entityOrigin + ';');
			} else {
				if (ch == '"')
					quote = '\'';
				sb.append(ch);
			}
		}
		return ' ' + name + '=' + quote + sb + quote;
	}

	String toXMLString() {
		SGMLDocument doc = (SGMLDocument) getOwnerDocument();
		StringBuffer sb = new StringBuffer();
		char quote = '"';
		for (int i = 0; i < value.length(); i++) {
			char ch = value.charAt(i);
			String entityOrigin = doc.getEntityOrigin(new Character(ch));
			if (entityOrigin != null) {
				sb.append("&#" + (int) ch + ';');
			} else if (ch == '&') {
				sb.append("&amp;");
			} else if (ch == '<') {
				sb.append("&lt;");
			} else if (ch == '>') {
				sb.append("&gt;");
			} else {
				if (ch == '"')
					quote = '\'';
				sb.append(ch);
			}
		}
		return ' ' + name + '=' + quote + sb + quote;
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
	public void setPrefix(String prefix) {
	}

	/**
	 * @return always same as <code>getName()</code>.
	 */
	public String getLocalName() {
		return getName();
	}

	/**
	 * @return always null
	 */
	public String getNamespaceURI() {
		return null;
	}

	/**
	 * Does nothing.
	 */
	public void normalize() {
	}

	private Element ownerElement = null;

	public Element getOwnerElement() {
		return ownerElement;
	}

	void setOwnerElement(Element owner) {
		this.ownerElement = owner;
	}

	/**
	 * @return always false because this is not XML implementation but SGML.
	 */
	public boolean supports(String feature, String version) {
		return false;
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

	public TypeInfo getSchemaTypeInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isId() {
		// TODO Auto-generated method stub
		return false;
	}

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
