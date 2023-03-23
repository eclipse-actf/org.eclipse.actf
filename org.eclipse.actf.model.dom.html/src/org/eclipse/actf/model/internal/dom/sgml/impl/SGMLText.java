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
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Text;

@SuppressWarnings("nls")
public class SGMLText extends SGMLCharacterData implements Text, IPrintXML {
	/**
	 * 
	 */
	private static final long serialVersionUID = -579697061500329505L;

	public SGMLText(String str, Document doc) {
		super(str, doc);
	}

	public short getNodeType() {
		return TEXT_NODE;
	}

	public String getNodeName() {
		return "#text";
	}

	public Text splitText(int offset) throws DOMException {
		SGMLText sibling;
		try {
			sibling = new SGMLText(text.substring(offset), ownerDocument);
		} catch (StringIndexOutOfBoundsException e) {
			throw new DOMException(DOMException.INDEX_SIZE_ERR, e.getMessage()) {

				/**
				 * 
				 */
				private static final long serialVersionUID = -1750553827974411478L;
			};
		}
		sibling.previousSibling = this;
		sibling.parent = parent;
		nextSibling = sibling;
		text = text.substring(offset);
		return this;
	}

	/**
	 * Gets original string whose converted character entities are reverted. If
	 * an original string is "a &amp;gt; b", then returned value is also "a
	 * &amp;gt; b" (eg. return value of {@link #getNodeValue()} is "a &gt; b").
	 * 
	 * @return original string
	 */
	public String toString() {
		SGMLDocument doc = (SGMLDocument) getOwnerDocument();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			Character C = new Character(ch);
			String entityOrigin = doc.getEntityOrigin(C);
			if (entityOrigin != null) {
				sb.append('&' + entityOrigin + ';');
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	public String toXMLString() {
		SGMLDocument doc = (SGMLDocument) getOwnerDocument();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			Character C = new Character(ch);
			String entityOrigin = doc.getEntityOrigin(C);
			if (entityOrigin != null) {
				sb.append("&#" + (int) ch + ';');
			} else if (ch == '&') {
				sb.append("&amp;");
			} else if (ch == '<') {
				sb.append("&lt;");
			} else if (ch == '>') {
				sb.append("&gt;");
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	public void printAsXML(PrintWriter pw, int indentLevel, boolean indent) {
		pw.print(toXMLString());
	}

	public void printAsSGML(PrintWriter pw, int indentLevel, boolean indent) {
		pw.print(toString());
	}

	private boolean isWhitespace = false;

	public boolean getIsWhitespaceInElementContent() {
		return this.isWhitespace;
	}

	void setIsWhitespaceInElementContent(boolean b) {
		this.isWhitespace = b;
	}

	/**
	 * DOM Level 3
	 */

	public String getWholeText() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isElementContentWhitespace() {
		// TODO Auto-generated method stub
		return false;
	}

	public Text replaceWholeText(String content) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}
}
