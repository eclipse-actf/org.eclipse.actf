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
import org.w3c.dom.Comment;
import org.w3c.dom.Document;

@SuppressWarnings("nls")
public class SGMLComment extends SGMLCharacterData implements Comment,
		IPrintXML {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3866401135787687787L;

	public short getNodeType() {
		return COMMENT_NODE;
	}

	public String getNodeName() {
		return "#comment";
	}

	public SGMLComment(String data, Document doc) {
		super(data, doc);
	}

	public String toString() {
		return "<!--" + text + "-->";
	}

	public void printAsXML(PrintWriter pw, int indentLevel, boolean indent) {
		StringBuffer sb = new StringBuffer();
		int len = text.length();
		for (int i = 0; i < len - 2; i++) {
			if (text.charAt(i) == '-' && text.charAt(i + 1) == '-') {
				i++;
			} else {
				sb.append(text.charAt(i));
			}
		}
		if (len - 2 >= 0) {
			if (text.charAt(len - 1) != '-') {
				sb.append(text.charAt(len - 2));
				sb.append(text.charAt(len - 1));
			} else if (text.charAt(len - 2) != '-') {
				sb.append(text.charAt(len - 2));
			}
		} else if (len - 1 >= 0 && text.charAt(len - 1) != '-') {
			sb.append(text.charAt(len - 1));
		}
		pw.print("<!--" + sb + "-->");
	}

	public void printAsSGML(PrintWriter pw, int indentLevel, boolean indent) {
		pw.print("<!--" + text + "-->");
	}
}
