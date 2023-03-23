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

import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.eclipse.actf.model.internal.dom.sgml.IPrintXML;
import org.eclipse.actf.model.internal.dom.sgml.ISGMLElement;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;

/**
 * Element class of SGML.
 * 
 */
@SuppressWarnings("nls")
public class SGMLElement extends SGMLParentNode implements ISGMLElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8088815760723728378L;

	/**
	 * 
	 */
	private String tagName;

	/**
	 * 
	 */
	private SGMLAttribute attributes[] = new SGMLAttribute[8];

	/**
	 * 
	 */
	private int attrNum = 0;

	/**
	 * Constructs an element.
	 * 
	 * @param name
	 *            element's name
	 * @param doc
	 *            owner document of this element.
	 */
	public SGMLElement(String name, Document doc) {
		super(doc);
		this.tagName = name;
	}

	/**
	 * Checks if an specified node is appropriate as a child.
	 * 
	 * @param node
	 *            child node to add
	 * @exception DOMException
	 *                If <code> node </code> is not appropriate as a child.
	 */
	void check(Node node) throws DOMException {
		if (node == null) {
			throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR,
					"trying to insert null") {

				/**
						 * 
						 */
				private static final long serialVersionUID = -3195872655825042328L;
			};
		} else if (node.getOwnerDocument() != ownerDocument) {
			throw new DOMException(DOMException.WRONG_DOCUMENT_ERR, node
					+ " created from " + node.getOwnerDocument() + ". " + this
					+ " created from " + ownerDocument) {

				/**
						 * 
						 */
				private static final long serialVersionUID = -3952917378943559036L;
			};
		}
		switch (node.getNodeType()) {
		case ELEMENT_NODE:
		case TEXT_NODE:
		case COMMENT_NODE:
		case PROCESSING_INSTRUCTION_NODE:
		case CDATA_SECTION_NODE:
		case ENTITY_REFERENCE_NODE:
			break;
		default:
			throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR, node
					+ " is not allowed as a child of " + this) {

				/**
						 * 
						 */
				private static final long serialVersionUID = 2793178675397177186L;
			};
		}
	}

	public String getAttribute(String name) {
		for (int i = attrNum - 1; i >= 0; i--) {
			SGMLAttribute attr = attributes[i];
			if (attr.getNodeName().equalsIgnoreCase(name)) {
				return attr.getNodeValue();
			}
		}
		if(getDefaultValue(name) != null){
			return(getDefaultValue(name));
		}
		
		return "";
	}

	public Attr getAttributeNode(String name) {
		for (int i = attrNum - 1; i >= 0; i--) {
			SGMLAttribute ret = attributes[i];
			if (ret.getNodeName().equalsIgnoreCase(name)) {
				return ret;
			}
		}
		return null;
	}

	private void expandAttrBuf() {
		SGMLAttribute newBuf[] = new SGMLAttribute[attrNum * 2];
		System.arraycopy(attributes, 0, newBuf, 0, attrNum);
		attributes = newBuf;
	}

	/**
	 * returned instance always ignores <code>namespaceURI</code>
	 */
	public NamedNodeMap getAttributes() {
		return new NamedNodeMap() {
			public String toString() {
				StringBuffer ret = new StringBuffer('[');
				for (int i = 0; i < attrNum - 1; i++) {
					ret.append(attributes[i] + ", ");
				}
				if (attrNum > 0) {
					ret.append(attributes[attrNum - 1]);
				}
				ret.append(']');
				return ret.toString();
			}

			public Node getNamedItem(String name) {
				return getAttributeNode(name);
			}

			public Node getNamedItemNS(String namespaceURI, String localName) {
				return getNamedItem(localName);
			}

			public Node setNamedItem(Node arg) throws DOMException {
				if (arg instanceof SGMLAttribute) {
					for (int i = attrNum - 1; i >= 0; i--) {
						SGMLAttribute ret = attributes[i];
						if (ret.getNodeName().equalsIgnoreCase(
								arg.getNodeName())) {
							attributes[i] = (SGMLAttribute) arg;
							attributes[i].setOwnerElement(SGMLElement.this);
							return ret;
						}
					}
					if (attrNum == attributes.length) {
						expandAttrBuf();
					}
					attributes[attrNum++] = (SGMLAttribute) arg;
					((SGMLAttribute) arg).setOwnerElement(SGMLElement.this);
					return null;
				} else {
					throw new DOMException(
							DOMException.NO_MODIFICATION_ALLOWED_ERR,
							"only Attr instance can be set: " + arg) {

						/**
								 * 
								 */
						private static final long serialVersionUID = -8500293705674064623L;
					};
				}
			}

			public Node setNamedItemNS(Node arg) throws DOMException {
				return setNamedItem(arg);
			}

			public Node removeNamedItem(String name) throws DOMException {
				for (int i = attrNum - 1; i >= 0; i--) {
					SGMLAttribute attr = attributes[i];
					if (attr.getNodeName().equalsIgnoreCase(name)) {
						attributes[i] = null;
						attr.setOwnerElement(null);
						for (i++; i < attrNum; i++) {
							attributes[i - 1] = attributes[i];
						}
						attrNum--;
						return attr;
					}
				}
				return null;
			}

			public Node removeNamedItemNS(String namespaceURI, String localName)
					throws DOMException {
				return removeNamedItem(localName);
			}

			public Node item(int index) {
				if (0 <= index && index < attrNum) {
					return attributes[index];
				}
				return null;
			}

			public int getLength() {
				return attrNum;
			}
		};
	}

	public NodeList getElementsByTagName(String name) {
		class MyNodeList implements NodeList {
			private String name;
			private ArrayList<WeakReference<Node>> list;
			private int s;
			private int e;
			private long lastupdated;

			MyNodeList(String name) {
				this.name = name;
				list = getNodeList(ownerDocument, name);
				init();
			}

			private void init() {
				s = 0;
				Node start = findPreviousNodeByTagName(SGMLElement.this, name);
				if (start != null) {
					for (; s < list.size(); s++) {
						if (list.get(s).get() == start) {
							s++;
							break;
						}
					}
				}
				if(s<list.size() && list.get(s).get() == SGMLElement.this){
					s++;
				}
				e = list.size();
				Node next = SGMLElement.this.getNextSibling();
				Node p = SGMLElement.this.getParentNode();
				while (p != null && next == null) {
					next = p.getNextSibling();
					if (next == null) {
						p = p.getParentNode();
					}
				}
				if (next != null) {
					Node end = findPreviousNodeByTagName(next, name);
					if (end != null) {
						for (e--; e >= 0; e--) {
							if (list.get(e).get() == end) {
								e++;
								break;
							}
						}
					} else {
						e = 0;
					}
				}
				lastupdated = getNodeListUpdatedAt(ownerDocument, name);
			}

			public int getLength() {
				if (lastupdated != getNodeListUpdatedAt(ownerDocument, name)) {
					init();
				}
				return e - s;
			}

			public Node item(int index) {
				if (getLength() <= index) {
					return null;
				}
				return list.get(s + index).get();
			}

		}

		return new MyNodeList(name);
	}

	/*
	 * replaced for performance reason @2009/06/25 by dsato@jp.ibm.com public
	 * NodeList getElementsByTagName(String name) { final boolean all =
	 * name.equals("*"); final String targetName = name; return new NodeList() {
	 * public int getLength() { int ret = 0; Node tmp1, tmp2; tmp1 =
	 * SGMLElement.this.firstChild; outer: while (tmp1 != null) { if (tmp1
	 * instanceof Element && (all || targetName.equalsIgnoreCase(tmp1
	 * .getNodeName())) && tmp1 != SGMLElement.this) { ret++; } if ((tmp2 =
	 * tmp1.getFirstChild()) == null) { if (tmp1 == SGMLElement.this) { break
	 * outer; } else { tmp2 = tmp1.getNextSibling(); } } while (tmp2 == null &&
	 * tmp1 != null) { tmp1 = tmp2 = tmp1.getParentNode(); if (tmp1 !=
	 * SGMLElement.this) { tmp2 = tmp1.getNextSibling(); } else { break outer; }
	 * } tmp1 = tmp2; } return ret; }
	 * 
	 * public Node item(int index) { Node tmp1, tmp2; tmp1 =
	 * SGMLElement.this.firstChild; outer: while (tmp1 != null) { if (tmp1
	 * instanceof Element && (all || targetName.equalsIgnoreCase(tmp1
	 * .getNodeName())) && tmp1 != SGMLElement.this) { if (index == 0) { return
	 * tmp1; } else { index--; } } if ((tmp2 = tmp1.getFirstChild()) == null) {
	 * if (tmp1 == SGMLElement.this) { break outer; } else { tmp2 =
	 * tmp1.getNextSibling(); } } while (tmp2 == null && tmp1 != null) { tmp1 =
	 * tmp2 = tmp1.getParentNode(); if (tmp1 != SGMLElement.this) { tmp2 =
	 * tmp1.getNextSibling(); } else { break outer; } } tmp1 = tmp2; } return
	 * null; } };
	 * 
	 * }
	 */

	public String getNodeName() {
		return tagName;
	}

	public short getNodeType() {
		return ELEMENT_NODE;
	}

	public String getNodeValue() {
		return null;
	}

	public String getTagName() {
		return tagName;
	}

	public void removeAttribute(String name) throws DOMException {
		for (int i = attrNum - 1; i >= 0; i--) {
			SGMLAttribute attr = attributes[i];
			if (attr.getNodeName().equalsIgnoreCase(name)) {
				String defaultValue = getDefaultValue(name);
				if (defaultValue != null) {
					attr.setNodeValue(defaultValue);
				} else {
					attributes[i] = null;
					attr.setOwnerElement(null);
					for (i++; i < attrNum; i++) {
						attributes[i - 1] = attributes[i];
					}
					attrNum--;
				}

				processIdForOptimization(this);
				return;
			}
		}
	}

	private String getDefaultValue(String attrName) {
		ElementDefinition edef;
		SGMLDocTypeDef dtd = ((SGMLDocument) ownerDocument).getDTD();
		if (dtd == null)
			return null;
		edef = dtd.getElementDefinition(tagName);
		if (edef == null)
			return null;
		AttributeDefinition adef = edef.getAttributeDef(attrName);
		return adef != null ? adef.getDefaultValue() : null;
	}

	public Attr removeAttributeNode(Attr oldAttr) {
		for (int i = attrNum - 1; i >= 0; i--) {
			if (attributes[i] == oldAttr) {
				String defaultValue = getDefaultValue(oldAttr.getNodeName());
				if (defaultValue != null) {
					SGMLAttribute attr = (SGMLAttribute) ownerDocument
							.createAttribute(oldAttr.getNodeName());
					attr.setValue(defaultValue);
					attributes[i] = attr;
					attr.setOwnerElement(this);
				} else {
					attributes[i] = null;
					for (i++; i < attrNum; i++) {
						attributes[i - 1] = attributes[i];
					}
					attrNum--;
				}
				break;
			}
		}
		((SGMLAttribute) oldAttr).setOwnerElement(this);
		processIdForOptimization(this);
		return oldAttr;
	}

	public void setAttribute(String name, String value) throws DOMException {
		SGMLAttribute attr;
		for (int i = attrNum - 1; i >= 0; i--) {
			attr = attributes[i];
			if (attr.getName().equalsIgnoreCase(name)) {
				attr.setValue(value);
				processIdForOptimization(this);
				return;
			}
		}
		attr = (SGMLAttribute) ownerDocument.createAttribute(name);
		attr.setValue(value);
		attr.setOwnerElement(this);
		if (attributes.length == attrNum) {
			expandAttrBuf();
		}
		attributes[attrNum++] = attr;
		processIdForOptimization(this);
	}

	public Attr setAttributeNode(Attr newAttr) throws DOMException {
		if (!(newAttr instanceof SGMLAttribute)) {
			throw new DOMException(DOMException.WRONG_DOCUMENT_ERR, newAttr
					+ "is not SGMLAttribute") {

				/**
						 * 
						 */
				private static final long serialVersionUID = -2574702797917329018L;
			};
		}
		for (int i = attrNum - 1; i >= 0; i--) {
			SGMLAttribute attr = attributes[i];
			if (attr.getName().equalsIgnoreCase(newAttr.getName())) {
				attr.setOwnerElement(null);
				attributes[i] = (SGMLAttribute) newAttr;
				attributes[i].setOwnerElement(this);
				processIdForOptimization(this);
				return attr;
			}
		}
		if (attributes.length == attrNum) {
			expandAttrBuf();
		}
		attributes[attrNum] = (SGMLAttribute) newAttr;
		attributes[attrNum++].setOwnerElement(this);
		processIdForOptimization(this);
		return null;
	}

	public void setNodeValue(String nodeValue) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"can't set value in " + this) {

			/**
					 * 
					 */
			private static final long serialVersionUID = 159616090913212538L;
		};
	}

	/**
	 * Get start tag string. If attribute value includes a character that can be
	 * reverted, replaces it to original character entity.
	 * 
	 * @return string of start tag.
	 */
	public String toString() {
		StringBuffer ret = new StringBuffer();
		ret.append('<');
		ret.append(tagName);
		for (int i = 0; i < attrNum; i++)
			ret.append(attributes[i].toString());
		ret.append('>');
		return new String(ret);
	}

	public void printAsXML(PrintWriter pw, int indentLevel, boolean indent) {
		if (indent
				&& (previousSibling == null || previousSibling.getNodeType() != Node.TEXT_NODE
						&& previousSibling.getNodeType() != Node.CDATA_SECTION_NODE)) {
			for (int i = 0; i < indentLevel; i++)
				pw.print(' ');
		}
		pw.print('<' + getTagName());
		for (int i = 0; i < attrNum; i++) {
			pw.print(attributes[i].toXMLString());
		}
		if (hasChildNodes()) {
			pw.print('>');
			for (Node child = getFirstChild(); child != null; child = child
					.getNextSibling()) {
				if (indent && child.getNodeType() != Node.TEXT_NODE
						&& child.getNodeType() != Node.CDATA_SECTION_NODE) {
					pw.println();
				}
				((IPrintXML) child).printAsXML(pw, indentLevel + 1, indent);
			}
			if (indent
					&& (getLastChild() == null || getLastChild().getNodeType() != Node.TEXT_NODE
							&& getLastChild().getNodeType() != Node.CDATA_SECTION_NODE)) {
				pw.println();
				for (int i = indentLevel; i > 0; i--)
					pw.print(' ');
			}
			pw.print("</" + getTagName() + '>');
		} else {
			pw.print("/>");
		}
	}

	public void printAsSGML(PrintWriter pw, int indentLevel, boolean indent) {
		if (indent
				&& (previousSibling == null || previousSibling.getNodeType() != Node.TEXT_NODE
						&& previousSibling.getNodeType() != Node.CDATA_SECTION_NODE)) {
			for (int i = indentLevel; i > 0; i--)
				pw.print(' ');
		}
		pw.print(toString());
		boolean parent;
		if (parent = hasChildNodes()) {
			for (Node child = getFirstChild(); child != null; child = child
					.getNextSibling()) {
				if (indent && child.getNodeType() != Node.TEXT_NODE
						&& child.getNodeType() != Node.CDATA_SECTION_NODE) {
					pw.println();
				}
				((IPrintXML) child).printAsSGML(pw, indentLevel + 1, indent);
			}
		}
		SGMLDocTypeDef dtd = ((SGMLDocument) getOwnerDocument()).getDTD();
		ElementDefinition ed = null;
		if (dtd != null) {
			ed = dtd.getElementDefinition(tagName);
		}
		if (parent || ed == null || ed.getContentModel() != SGMLParser.empty) {
			if (indent
					&& (getLastChild() == null || getLastChild().getNodeType() != Node.TEXT_NODE
							&& getLastChild().getNodeType() != Node.CDATA_SECTION_NODE)) {
				pw.println();
				for (int i = indentLevel; i > 0; i--)
					pw.print(' ');
			}
			pw.print("</" + getTagName() + '>');
		}
	}

	public Node cloneNode(boolean deep) {
		SGMLElement ret = (SGMLElement) super.cloneNode(deep);
		SGMLAttribute cloneAttributes[] = new SGMLAttribute[attributes.length];
		for (int i = attrNum - 1; i >= 0; i--) {
			cloneAttributes[i] = (SGMLAttribute) attributes[i].cloneNode(false);
			cloneAttributes[i].setOwnerElement(ret);
		}
		ret.attributes = cloneAttributes;
		return ret;
	}

	/**
	 * @param namespaceURI
	 *            always ignored
	 * @return same as <code>getAttribute(localName)</code>
	 */
	public String getAttributeNS(String namespaceURI, String localName) {
		return getAttribute(localName);
	}

	/**
	 * Sets an attribute. In this implementation, this method is same as
	 * <code>setAttribute(qualifiedName, value)</code>
	 * 
	 * @param namespaceURI
	 *            always ignored
	 */
	public void setAttributeNS(String namespaceURI, String qualifiedName,
			String value) throws DOMException {
		setAttribute(qualifiedName, value);
	}

	/**
	 * Removes an attribute. In this implementation, this method is same as
	 * <code>removeAttribute(localName)</code>
	 * 
	 * @param namespaceURI
	 *            always ignored
	 */
	public void removeAttributeNS(String namespaceURI, String localName)
			throws DOMException {
		removeAttribute(localName);
	}

	/**
	 * @param namespaceURI
	 *            always ignored
	 * @return same as <code>getAttributeNode(localName)</code>
	 */
	public Attr getAttributeNodeNS(String namespaceURI, String localName) {
		return getAttributeNode(localName);
	}

	/**
	 * @return same as <code>setAttributeNode(newAttr)</code>
	 */
	public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
		return setAttributeNode(newAttr);
	}

	/**
	 * @param namespaceURI
	 *            always ignored
	 * @return same as <code>getElementsByTagName(localName)</code>
	 */
	public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
		return getElementsByTagName(localName);
	}

	public boolean hasAttribute(String name) {
		for (int i = attrNum - 1; i >= 0; i--) {
			SGMLAttribute attr = attributes[i];
			if (attr.getNodeName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return getDefaultValue(name) != null;
	}

	/**
	 * @param namespaceURI
	 *            ignored
	 * @return same as <code>hasAttributeNS(localName)</code>
	 */
	public boolean hasAttributeNS(String namespaceURI, String localName) {
		return hasAttribute(localName);
	}

	public boolean hasAttributes() {
		return attrNum > 0;
	}

	/**
	 * DOM Level 3
	 */

	public TypeInfo getSchemaTypeInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setIdAttribute(String name, boolean isId) throws DOMException {
		// TODO Auto-generated method stub

		processIdForOptimization(this);
	}

	public void setIdAttributeNS(String namespaceURI, String localName,
			boolean isId) throws DOMException {
		// TODO Auto-generated method stub

		processIdForOptimization(this);
	}

	public void setIdAttributeNode(Attr idAttr, boolean isId)
			throws DOMException {
		// TODO Auto-generated method stub

		processIdForOptimization(this);
	}
}
