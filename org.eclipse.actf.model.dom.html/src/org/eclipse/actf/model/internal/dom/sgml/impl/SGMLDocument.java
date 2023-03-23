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

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.actf.model.dom.html.DocumentTypeUtil;
import org.eclipse.actf.model.internal.dom.sgml.ISGMLDocument;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

/**
 * Root node is a instance of this class.
 */
@SuppressWarnings("nls")
public class SGMLDocument extends SGMLParentNode implements ISGMLDocument {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9052104600001817404L;

	/**
	 * @serial
	 */
	public Element documentElement = null;

	/**
	 * @serial
	 */
	public DocumentType doctype = null;

	private transient DOMImplementation domImpl;

	/**
	 * @see SGMLDOMImpl#createDocument(java.lang.String,java.lang.String,org.w3c.dom.DocumentType)
	 */
	public SGMLDocument() {
		this(SGMLDOMImpl.getDOMImplementation());
	}

	protected SGMLDocument(DOMImplementation imple) {
		super(null);
		this.domImpl = imple;
	}

	void check(Node node) throws DOMException {
		if (node.getOwnerDocument() != this && !(node instanceof SGMLDocType)) {
			throw new DOMException(DOMException.WRONG_DOCUMENT_ERR,
					node + " created from " + node.getOwnerDocument() + " this.") {

				/**
				 * 
				 */
				private static final long serialVersionUID = -6949628537817157229L;
			};
		}
		switch (node.getNodeType()) {
		case ELEMENT_NODE:
			if (documentElement == null) {
				documentElement = (Element) node;
			} else {
				throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR, " document cannot have roots.") {

					/**
					 * 
					 */
					private static final long serialVersionUID = -8867384684522317782L;
				};
			}
			break;
		case PROCESSING_INSTRUCTION_NODE:
		case COMMENT_NODE:
			break;
		case DOCUMENT_TYPE_NODE:
			doctype = (DocumentType) node;
			((SGMLNode) node).ownerDocument = this;
			break;
		default:
			throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR, node + " is not allowed as a child of " + this) {

				/**
				 * 
				 */
				private static final long serialVersionUID = 7083250644035192730L;
			};
		}
	}

	public Node cloneNode(boolean deep) {
		SGMLDocument ret = (SGMLDocument) super.cloneNode(true);
		setOwnerDocument(ret, ret);
		for (Node child = ret.firstChild; child != null; child = child.getNextSibling()) {
			if (child instanceof Element) {
				ret.documentElement = (Element) child;
			} else if (child instanceof DocumentType) {
				ret.doctype = (DocumentType) child;
			}
		}
		processNodeDeepForOptimization(ret.documentElement);
		return ret;
	}

	private void processNodeDeepForOptimization(Element element) {
		Node f = element.getFirstChild();
		while (f != null) {
			if (f instanceof Element) {
				processNodeDeepForOptimization((Element) f);
			}
			f = f.getNextSibling();
		}
		if (element instanceof SGMLElement) {
			((SGMLElement) element).processNodeForOptimization(element);
		}
	}

	private void setOwnerDocument(SGMLNode ret, Document doc) {
		ret.ownerDocument = doc;

		for (SGMLNode child = (SGMLNode) ret.getFirstChild(); child != null; child = (SGMLNode) child
				.getNextSibling()) {
			setOwnerDocument(child, doc);
		}
	}

	public Attr createAttribute(String name) {
		return new SGMLAttribute(name, name, this);
	}

	public CDATASection createCDATASection(String c) {
		return new SGMLCDATASection(c, this);
	}

	public Comment createComment(String data) {
		return new SGMLComment(data, this);
	}

	public DocumentFragment createDocumentFragment() {
		return new SGMLDocumentFragment(this);
	}

	public Element createElement(String tagName) {
		return new SGMLElement(tagName, this);
	}

	public EntityReference createEntityReference(String a) throws DOMException {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "cannot create Entity Ref.") {

			/**
			 * 
			 */
			private static final long serialVersionUID = -4581301359508117945L;
		};
	}

	public ProcessingInstruction createProcessingInstruction(String target, String data) {
		return new SGMLPI(target, data, this);
	}

	public Text createTextNode(String data) {
		return new SGMLText(data, this);
	}

	public DocumentType getDoctype() {
		return doctype;
	}

	public Element getDocumentElement() {
		return documentElement;
	}

	// implementation of Document interface;
	public Node getDocumentType() {
		return doctype;
	}

	public NodeList getElementsByTagName(String tagname) {
		// replaced for performance reason @2009/06/25 by dsato@jp.ibm.com
		return documentElement.getElementsByTagName(tagname);
	}

	// very slow
	// public NodeList getElementsByTagName(String tagname) {
	// final boolean all = tagname.equals("*");
	// final String targetName = tagname;
	// return new NodeList() {
	// public int getLength() {
	// int ret = 0;
	// Node tmp1, tmp2;
	// tmp1 = documentElement;
	// outer: while (tmp1 != null) {
	// if (tmp1 instanceof Element
	// && (all || targetName.equalsIgnoreCase(tmp1
	// .getNodeName()))) {
	// ret++;
	// }
	// if ((tmp2 = tmp1.getFirstChild()) == null) {
	// if (tmp1 == documentElement) {
	// break outer;
	// } else {
	// tmp2 = tmp1.getNextSibling();
	// }
	// }
	// while (tmp2 == null && tmp1 != null) {
	// tmp1 = tmp2 = tmp1.getParentNode();
	// if (tmp1 != documentElement) {
	// tmp2 = tmp1.getNextSibling();
	// } else {
	// break outer;
	// }
	// }
	// tmp1 = tmp2;
	// }
	// return ret;
	// }
	//
	// public Node item(int index) {
	// Node tmp1, tmp2;
	// tmp1 = documentElement;
	// outer: while (tmp1 != null) {
	// if (tmp1 instanceof Element
	// && (all || targetName.equalsIgnoreCase(tmp1
	// .getNodeName()))) {
	// if (index == 0) {
	// return tmp1;
	// } else {
	// index--;
	// }
	// }
	// if ((tmp2 = tmp1.getFirstChild()) == null) {
	// if (tmp1 == documentElement) {
	// break outer;
	// } else {
	// tmp2 = tmp1.getNextSibling();
	// }
	// }
	// while (tmp2 == null && tmp1 != null) {
	// tmp1 = tmp2 = tmp1.getParentNode();
	// if (tmp1 != documentElement) {
	// tmp2 = tmp1.getNextSibling();
	// } else {
	// break outer;
	// }
	// }
	// tmp1 = tmp2;
	// }
	// return null;
	// }
	// };
	// }

	public DOMImplementation getImplementation() {
		return this.domImpl;
	}

	public Node getNextSibling() {
		return null;
	}

	public String getNodeName() {
		return "#document";
	}

	// implementation of Node interface
	public short getNodeType() {
		return DOCUMENT_NODE;
	}

	public String getNodeValue() {
		return null;
	}

	public Document getOwnerDocument() {
		return null;
	}

	public Node getParentNode() {
		return null;
	}

	/**
	 * A Document object is the root node. So this have no siblings.
	 */
	public Node getPreviousSibling() {
		return null;
	}

	/**
	 * This method was created in VisualAge.
	 * 
	 * @return org.w3c.dom.Node
	 * @param oldChild
	 *            org.w3c.dom.Node
	 * @exception org.w3c.dom.DOMException
	 *                The exception description.
	 */
	public Node removeChild(Node oldChild) throws DOMException {
		if (oldChild == documentElement) {
			documentElement = null;
		} else if (oldChild == doctype) {
			doctype = null;
		}
		return super.removeChild(oldChild);
	}

	public void setNodeValue(String nodeValue) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "#document is always null") {

			/**
			 * 
			 */
			private static final long serialVersionUID = 6689389325290139309L;
		};
	}

	public void printAsXML(String publicID, URL location, PrintWriter pw, boolean indent) throws IOException {
		printAsXML(publicID, location, pw, indent, null);
	}

	/**
	 * Print Document as XML
	 * 
	 * @param public
	 *            ID
	 * @param DTD's
	 *            location
	 * @param pw
	 *            stream to write this document.
	 * @param indent
	 *            indent if true. Otherwise, not indent.
	 * @param enc
	 */
	public void printAsXML(String publicID, URL location, PrintWriter pw, boolean indent, String enc)
			throws IOException {
		if (enc == null) {
			pw.println("<?xml version=\"1.0\"?>");
		} else {
			pw.println("<?xml version=\"1.0\" encoding=\"" + enc + "\"?>");
		}
		if (publicID != null) {
			pw.print("<!DOCTYPE " + documentElement.getTagName() + " PUBLIC \"" + publicID + '"');
			if (location != null) {
				pw.println(" \"" + location + "\">");
			} else {
				pw.println('>');
			}
		}
		((SGMLElement) documentElement).printAsXML(pw, 0, indent);
		pw.println();
		pw.flush();
		// charEntities4Xml = null;
	}

	public void printAsXML(PrintWriter pw, boolean indent, String enc) throws IOException {
		printAsXML(null, null, pw, indent, enc);
	}

	/**
	 * Print Document as SGML. starttag's string in this document are from the
	 * original document (see {@link SGMLElement#toString()}. Even if attributes
	 * in elements are modified, starttag's string does not change.
	 * 
	 * @param DTD's
	 *            location
	 * @param pw
	 *            stream to write this document.
	 */
	public void printAsSGML(PrintWriter pw, boolean indent) throws IOException {
		if (doctype != null) {
			String orgDoctype = DocumentTypeUtil.getOriginalID(doctype);
			if (orgDoctype.isEmpty()) {
				pw.println("<!DOCTYPE html>");
			} else if (orgDoctype.equalsIgnoreCase("about:legacy-compat")) {
				pw.println("<!DOCTYPE html SYSTEM \"about:legacy-compat\">");
			} else {
				pw.println(doctype.toString());
			}
		}
		if (documentElement != null)
			((SGMLElement) documentElement).printAsSGML(pw, 0, indent);
		pw.println();
		pw.flush();
	}

	/**
	 * @serial
	 */
	// private Hashtable charEntities4Xml;
	//
	// String getEntityOrigin4Xml(String entity) {
	// return (String) charEntities4Xml.get(entity);
	// }

	private transient SGMLDocTypeDef dtd;

	public SGMLDocTypeDef getDTD() {
		return dtd;
	}

	public void setDTD(SGMLDocTypeDef dtd) {
		this.dtd = dtd;
	}

	String getEntityOrigin(Character Ch) {
		return charNumEntities.get(Ch);
	}

	/**
	 * @serial
	 */
	private Hashtable<Character, String> charNumEntities = new Hashtable<Character, String>();

	void putCharNumEntity(Character C, String ent) {
		charNumEntities.put(C, ent);
	}

	String getCharNumEntity(Character C) {
		return charNumEntities.get(C);
	}

	// DOM Level 2

	public Node importNode(Node importedNode, boolean deep) throws DOMException {
		Node ret;
		switch (importedNode.getNodeType()) {
		case Node.ELEMENT_NODE:
			ret = createElement(importedNode.getNodeName());
			NamedNodeMap attributes = importedNode.getAttributes();
			for (int i = 0; i < attributes.getLength(); i++) {
				Attr importedAttr = (Attr) attributes.item(i);
				if (importedAttr.getSpecified()) {
					Attr attr = createAttribute(importedAttr.getNodeName());
					attr.setValue(importedAttr.getValue());
					((Element) ret).setAttributeNode(attr);
				}
			}
			break;
		case Node.ATTRIBUTE_NODE:
			ret = createAttribute(importedNode.getNodeName());
			((Attr) ret).setValue(importedNode.getNodeValue());
			break;
		case Node.TEXT_NODE:
			ret = createTextNode(importedNode.getNodeValue());
			break;
		case Node.CDATA_SECTION_NODE:
			ret = createCDATASection(importedNode.getNodeValue());
			break;
		case Node.COMMENT_NODE:
			ret = createComment(importedNode.getNodeValue());
			break;
		case Node.PROCESSING_INSTRUCTION_NODE:
			ProcessingInstruction pi = (ProcessingInstruction) importedNode;
			ret = createProcessingInstruction(pi.getTarget(), pi.getData());
			break;
		case Node.ENTITY_REFERENCE_NODE:
			throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "ENTITY_REFERENCE: " + importedNode) {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1578579363722608207L;
			};
		case Node.ENTITY_NODE:
			throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "ENTITY: " + importedNode) {

				/**
				 * 
				 */
				private static final long serialVersionUID = -450181572985174561L;
			};
		case Node.DOCUMENT_NODE:
			throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "DOCUMENT: " + importedNode) {

				/**
				 * 
				 */
				private static final long serialVersionUID = 8785157203267073381L;
			};
		case Node.DOCUMENT_TYPE_NODE:
			throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "DOCUMENT_TYPE: " + importedNode) {

				/**
				 * 
				 */
				private static final long serialVersionUID = 5629745929750604049L;
			};
		case Node.DOCUMENT_FRAGMENT_NODE:
			ret = createDocumentFragment();
			break;
		case Node.NOTATION_NODE:
			throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "NOTATION: " + importedNode) {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1787799543281735366L;
			};
		default:
			throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Unknown node type: " + importedNode.getNodeType()) {

				/**
				 * 
				 */
				private static final long serialVersionUID = -9119985548894040858L;
			};
		}
		if (deep) {
			for (Node importedChild = importedNode.getFirstChild(); importedChild != null; importedChild = importedChild
					.getNextSibling()) {
				ret.appendChild(importNode(importedChild, true));
			}
		}
		return ret;
	}

	/**
	 * @param namespaceURI
	 *            always ignored.
	 * @return same as <code>createElement(qualifiedName)</code>
	 */
	public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
		return createElement(qualifiedName);
	}

	/**
	 * @param namespaceURI
	 *            always ignored.
	 * @return same as <code>createAttribute(qualifiedName)</code>
	 */
	public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
		return createAttribute(qualifiedName);
	}

	/**
	 * @param namespaceURI
	 *            always ignored.
	 * @return same as <code>getElementsByTagName(localName)</code>
	 */
	public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
		return getElementsByTagName(localName);
	}

	/**
	 * Returns the <code>Element</code> whose <code>ID</code> is given by
	 * <code>elementId</code>. If more than one element has this <code>ID</code>
	 * The first element in the depth-first and pre-order traversal is returned.
	 */
	public Element getElementById(String elementID) {
		// replaced for performance reason @2009/06/25 by dsato@jp.ibm.com
		if (documentElement instanceof SGMLElement) {
			HashMap<String, List<WeakReference<Element>>> map = SGMLParentNode.getIdMap(this);
			List<WeakReference<Element>> list = map.get(elementID);
			if (list == null) {
				return null;
			}
			return list.get(0).get();
		}
		return null;
		// very slow
		// return getElementById(documentElement, elementID);
	}
	/*
	 * private Element getElementById(Element el, String elementID) { if
	 * (el.getAttribute("id").equals(elementID)) { return el; } else { for (Node
	 * child = el.getFirstChild(); child != null; child = child
	 * .getNextSibling()) { if (child instanceof Element) { Element ret =
	 * getElementById((Element) child, elementID); if (ret != null) { return
	 * ret; } } } return null; } }
	 */

	/**
	 * DOM Level 3
	 */

	public Node adoptNode(Node source) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDocumentURI() {
		// TODO Auto-generated method stub
		return null;
	}

	public DOMConfiguration getDomConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getInputEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean getStrictErrorChecking() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getXmlEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean getXmlStandalone() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getXmlVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	public void normalizeDocument() {
		// TODO Auto-generated method stub

	}

	public Node renameNode(Node n, String namespaceURI, String qualifiedName) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDocumentURI(String documentURI) {
		// TODO Auto-generated method stub

	}

	public void setStrictErrorChecking(boolean strictErrorChecking) {
		// TODO Auto-generated method stub

	}

	public void setXmlStandalone(boolean xmlStandalone) throws DOMException {
		// TODO Auto-generated method stub

	}

	public void setXmlVersion(String xmlVersion) throws DOMException {
		// TODO Auto-generated method stub

	}
}