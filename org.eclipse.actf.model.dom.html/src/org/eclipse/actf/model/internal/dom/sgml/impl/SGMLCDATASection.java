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

import org.eclipse.actf.model.internal.dom.sgml.IPrintXML;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;

@SuppressWarnings("nls")
public class SGMLCDATASection extends SGMLText implements CDATASection,
		IPrintXML {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8646896604778697913L;

	public SGMLCDATASection(String str, Document doc) {
		super(str, doc);
	}

	public short getNodeType() {
		return CDATA_SECTION_NODE;
	}

	public String getNodeName() {
		return "#cdata-section";
	}

	public String toString() {
		return text;
	}

	public void printAsXML(PrintWriter pw, int indentLevel, boolean indent) {
		if (indent)
			for (int i = 0; i < indentLevel; i++)
				pw.print(' ');
		pw.print("<![CDATA[" + text + "]]>");
		if (indent)
			pw.println();
	}

	public void printAsSGML(PrintWriter pw, int indentLevel, boolean indent) {
		pw.print(text);
	}
}
