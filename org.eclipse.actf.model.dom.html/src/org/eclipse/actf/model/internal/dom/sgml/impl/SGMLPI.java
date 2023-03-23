/*******************************************************************************
 * Copyright (c) 1998, 2016 IBM Corporation and Others
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

import org.eclipse.actf.model.internal.dom.sgml.IPrintXML;
import org.eclipse.actf.model.internal.dom.sgml.ISGMLDocument;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;

/**
 * Originally <code>org.w3c.dom.ProcessingInstruction</code> is defined for XML.
 * Structure of XML's ProcessingInstruction is &lt;?<code>target data
 * </code> ?&gt;. But structure
 * of SGML's PI is &lt;?<code>data</code> &gt; So, this class has no
 * <code>target</code>. Everything is <code>data</code>
 */
@SuppressWarnings("nls")
public class SGMLPI extends SGMLNode implements ProcessingInstruction,
		IPrintXML {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2540695089393435586L;

	void check(Node node) {
		throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR,
				"ProcessingInstruction cannot have any children.") {
			private static final long serialVersionUID = 7449313326260141011L;
		};
	}

	private static NodeList nullNodeList = new NodeList() {
		public Node item(int index) {
			return null;
		}

		public int getLength() {
			return 0;
		}
	};

	public String getNodeName() {
		return data;
	}

	public String getNodeValue() {
		return data;
	}

	public void setNodeValue(String nodeValue) {
		this.data = nodeValue;
	}

	public short getNodeType() {
		return PROCESSING_INSTRUCTION_NODE;
	}

	public Node getParentNode() {
		return parent;
	}

	public NodeList getChildNodes() {
		return nullNodeList;
	}

	public Node getFirstChild() {
		return null;
	}

	public Node getLastChild() {
		return null;
	}

	/**
	 * @serial
	 */
	@SuppressWarnings("unused")
	private String target;

	private String data;

	/**
	 * Constructs ProcessingInstruction instance whose target value is null.
	 */
	public SGMLPI(String target, String data, ISGMLDocument doc) {
		super(doc);
		this.target = target;
		this.data = data;
	}

	public boolean hasChildNodes() {
		return false;
	}

	/**
	 * SGML's PI has no taget. So returns data instead of target.
	 * 
	 * @return always same as {@link #getData() getData()}
	 */
	public String getTarget() {
		return null;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	/**
	 * For debug.
	 */
	public String toString() {
		return "<?" + data + '>';
	}

	public void printAsXML(PrintWriter pw, int indentLevel, boolean indent) {
		pw.print("<?" + data + "?>");
	}

	public void printAsSGML(PrintWriter pw, int indentLevel, boolean indent) {
		pw.print("<?" + data + '>');
	}
}
