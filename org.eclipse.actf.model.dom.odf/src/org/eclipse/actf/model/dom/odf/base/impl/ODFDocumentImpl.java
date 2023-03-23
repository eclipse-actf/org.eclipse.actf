/*******************************************************************************
 * Copyright (c) 2007, 2016 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tatsuya ISHIHARA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.model.dom.odf.base.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.ODFNode;
import org.eclipse.actf.model.dom.odf.chart.ChartConstants;
import org.eclipse.actf.model.dom.odf.chart.impl.ChartNodeFactory;
import org.eclipse.actf.model.dom.odf.dr3d.Dr3dConstants;
import org.eclipse.actf.model.dom.odf.dr3d.impl.Dr3dNodeFactory;
import org.eclipse.actf.model.dom.odf.draw.DrawConstants;
import org.eclipse.actf.model.dom.odf.draw.impl.DrawNodeFactory;
import org.eclipse.actf.model.dom.odf.form.FormConstants;
import org.eclipse.actf.model.dom.odf.form.impl.FormNodeFactory;
import org.eclipse.actf.model.dom.odf.number.NumberConstants;
import org.eclipse.actf.model.dom.odf.number.impl.NumberNodeFactory;
import org.eclipse.actf.model.dom.odf.office.DocumentContentElement;
import org.eclipse.actf.model.dom.odf.office.DocumentStylesElement;
import org.eclipse.actf.model.dom.odf.office.OfficeConstants;
import org.eclipse.actf.model.dom.odf.office.impl.OfficeNodeFactory;
import org.eclipse.actf.model.dom.odf.presentation.PresentationConstants;
import org.eclipse.actf.model.dom.odf.presentation.impl.PresentationNodeFactory;
import org.eclipse.actf.model.dom.odf.style.StyleConstants;
import org.eclipse.actf.model.dom.odf.style.impl.StyleNodeFactory;
import org.eclipse.actf.model.dom.odf.svg.SVGConstants;
import org.eclipse.actf.model.dom.odf.svg.impl.SVGNodeFactory;
import org.eclipse.actf.model.dom.odf.table.TableConstants;
import org.eclipse.actf.model.dom.odf.table.impl.TableNodeFactory;
import org.eclipse.actf.model.dom.odf.text.TextConstants;
import org.eclipse.actf.model.dom.odf.text.impl.TextNodeFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

public class ODFDocumentImpl extends ODFNodeImpl implements ODFDocument {
	private static final Map<String, Class<? extends AbstractODFNodeFactory>> factoryMap = new HashMap<String, Class<? extends AbstractODFNodeFactory>>();

	static {
		factoryMap.put(ChartConstants.CHART_NAMESPACE_URI, ChartNodeFactory.class);
		factoryMap.put(Dr3dConstants.DR3D_NAMESPACE_URI, Dr3dNodeFactory.class);
		factoryMap.put(DrawConstants.DRAW_NAMESPACE_URI, DrawNodeFactory.class);
		factoryMap.put(FormConstants.FORM_NAMESPACE_URI, FormNodeFactory.class);
		factoryMap.put(NumberConstants.NUMBER_NAMESPACE_URI, NumberNodeFactory.class);
		factoryMap.put(OfficeConstants.OFFICE_NAMESPACE_URI, OfficeNodeFactory.class);
		factoryMap.put(PresentationConstants.PRESENTATION_NAMESPACE_URI, PresentationNodeFactory.class);
		factoryMap.put(StyleConstants.STYLE_NAMESPACE_URI, StyleNodeFactory.class);
		factoryMap.put(TableConstants.TABLE_NAMESPACE_URI, TableNodeFactory.class);
		factoryMap.put(TextConstants.TEXT_NAMESPACE_URI, TextNodeFactory.class);
		factoryMap.put(SVGConstants.SVG_NAMESPACE_URI, SVGNodeFactory.class);
	}

	private Map<Node, ODFNode> internalNodeMap = new HashMap<Node, ODFNode>();

	private String sUrl = null;

	private ODFDocument styleDoc = null;

	public ODFDocumentImpl(Document doc) {
		super(doc);
		internalNodeMap.put(doc, this);
	}

	public Node adoptNode(Node source) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	public Attr createAttribute(String name) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	public CDATASection createCDATASection(String data) throws DOMException {
		if (iNode instanceof Document) {
			return ((Document) iNode).createCDATASection(data);
		}
		return null;
	}

	public Comment createComment(String data) {
		if (iNode instanceof Document) {
			return ((Document) iNode).createComment(data);
		}
		return null;
	}

	public DocumentFragment createDocumentFragment() {
		// TODO Auto-generated method stub
		return null;
	}

	public Element createElement(String tagName) throws DOMException {
		if (iNode instanceof Document) {
			return (Element) getODFNode(((Document) iNode).createElement(tagName));
		}
		return null;
	}

	public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
		if (iNode instanceof Document) {
			return (Element) getODFNode(((Document) iNode).createElementNS(namespaceURI, qualifiedName));
		}
		return null;
	}

	public EntityReference createEntityReference(String name) throws DOMException {
		if (iNode instanceof Document) {
			return ((Document) iNode).createEntityReference(name);
		}
		return null;
	}

	public ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
		if (iNode instanceof Document) {
			return ((Document) iNode).createProcessingInstruction(target, data);
		}
		return null;
	}

	public Text createTextNode(String data) {
		if (iNode instanceof Document) {
			Text iText = ((Document) iNode).createTextNode(data);
			return (Text) getODFNode(iText);
		}
		return null;
	}

	public DocumentType getDoctype() {
		if (iNode instanceof Document) {
			return ((Document) iNode).getDoctype();
		}
		return null;
	}

	public Element getDocumentElement() {
		if (iNode instanceof Document) {
			Element iElem = ((Document) iNode).getDocumentElement();
			if (iElem == null)
				return iElem;
			return (Element) getODFNode(iElem);
		}
		return null;
	}

	public String getDocumentURI() {
		if (iNode instanceof Document) {
			return ((Document) iNode).getDocumentURI();
		}
		return null;
	}

	public DOMConfiguration getDomConfig() {
		if (iNode instanceof Document) {
			return ((Document) iNode).getDomConfig();
		}
		return null;
	}

	public Element getElementById(String elementId) {
		if (iNode instanceof Document) {
			Element iElem = ((Document) iNode).getElementById(elementId);
			if (iElem == null)
				return null;
			return (Element) getODFNode(iElem);
		}
		return null;
	}

	public NodeList getElementsByTagName(String tagname) {
		if (iNode instanceof Document) {
			NodeList iNodeList = ((Document) iNode).getElementsByTagName(tagname);
			if (iNodeList == null)
				return null;
			return new ODFNodeListImpl(this, iNodeList);
		}
		return null;
	}

	public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
		if (iNode instanceof Document) {
			NodeList iNodeList = ((Document) iNode).getElementsByTagNameNS(namespaceURI, localName);
			if (iNodeList == null)
				return null;
			return new ODFNodeListImpl(this, iNodeList);
		}
		return null;
	}

	public DOMImplementation getImplementation() {
		if (iNode instanceof Document) {
			return ((Document) iNode).getImplementation();
		}
		return null;
	}

	public String getInputEncoding() {
		if (iNode instanceof Document) {
			return ((Document) iNode).getInputEncoding();
		}
		return null;
	}

	public boolean getStrictErrorChecking() {
		if (iNode instanceof Document) {
			return ((Document) iNode).getStrictErrorChecking();
		}
		return false;
	}

	public String getXmlEncoding() {
		if (iNode instanceof Document) {
			return ((Document) iNode).getXmlEncoding();
		}
		return null;
	}

	public boolean getXmlStandalone() {
		if (iNode instanceof Document) {
			return ((Document) iNode).getXmlStandalone();
		}
		return false;
	}

	public String getXmlVersion() {
		if (iNode instanceof Document) {
			return ((Document) iNode).getXmlVersion();
		}
		return null;
	}

	public Node importNode(Node importedNode, boolean deep) throws DOMException {
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
		if (iNode instanceof Document) {
			((Document) iNode).setDocumentURI(documentURI);
		}
	}

	public void setStrictErrorChecking(boolean strictErrorChecking) {
		if (iNode instanceof Document) {
			((Document) iNode).setStrictErrorChecking(strictErrorChecking);
		}
	}

	public void setXmlStandalone(boolean xmlStandalone) throws DOMException {
		if (iNode instanceof Document) {
			((Document) iNode).setXmlStandalone(xmlStandalone);
		}
	}

	public void setXmlVersion(String xmlVersion) throws DOMException {
		if (iNode instanceof Document) {
			((Document) iNode).setXmlVersion(xmlVersion);
		}
	}

	// /////////////////////////////////
	// ODFDocument implementation
	// /////////////////////////////////

	private Method findCreateElementMethod(Class<? extends AbstractODFNodeFactory> cs) {
		Method[] methods = cs.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			if ("createElement".equals(methods[i].getName())) { //$NON-NLS-1$
				Class<?>[] parms = methods[i].getParameterTypes();
				if (parms.length == 3 && parms[0].equals(ODFDocument.class) && parms[1].equals(String.class)
						&& parms[2].equals(Element.class)) {
					return methods[i];
				}
			}
		}
		return null;
	}

	public ODFNode getODFNode(Node node) {
		ODFNode result = internalNodeMap.get(node);
		if (result != null)
			return result;

		Class<? extends AbstractODFNodeFactory> factory = factoryMap.get(node.getNamespaceURI());
		if (factory != null) {
			if (node instanceof Element) {
				Method createElemMethod = findCreateElementMethod(factory);
				try {
					result = (ODFNode) createElemMethod.invoke(factory,
							new Object[] { this, node.getLocalName(), (Element) node });
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		if (result == null) {
			if (node instanceof Element) {
				result = new ODFElementImpl(this, (Element) node);
			} else if (node instanceof Attr) {
				result = new ODFAttrImpl(this, (Attr) node);
			} else if (node instanceof Text) {
				result = new ODFTextImpl(this, (Text) node);
			} else if (node instanceof Comment) {
				result = new ODFCommentImpl(this, (Comment) node);
			} else if (node instanceof CharacterData) {
				result = new ODFCharacterDataImpl(this, (CharacterData) node);
			} else if (node instanceof ProcessingInstruction) {
				result = new ODFProcessingInstructionImpl(this, (ProcessingInstruction) node);
			} else {
				result = new ODFNodeImpl(this, node);
			}
		}
		internalNodeMap.put(node, result);
		return result;
	}

	public String getURL() {
		return sUrl;
	}

	public void setURL(String sUrl) {
		this.sUrl = sUrl;
	}

	public void setODFVersion(double version) {
		Element root = this.getDocumentElement();
		root.setAttributeNS(OfficeConstants.OFFICE_NAMESPACE_URI, OfficeConstants.ATTR_VERSION,
				new Double(version).toString());
	}

	public double getODFVersion() {
		Element root = this.getDocumentElement();
		if (root instanceof DocumentContentElement) {
			return ((DocumentContentElement) root).getAttrOfficeVersion();
		} else if (root instanceof DocumentStylesElement) {
			return ((DocumentStylesElement) root).getAttrOfficeVersion();
		}
		return -1.0;
	}

	public void setStyleDocument(ODFDocument styleDoc) {
		this.styleDoc = styleDoc;
	}

	public ODFDocument getStyleDocument() {
		return this.styleDoc;
	}
}