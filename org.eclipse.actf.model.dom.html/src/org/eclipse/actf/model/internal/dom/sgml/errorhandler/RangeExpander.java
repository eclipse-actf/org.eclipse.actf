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

package org.eclipse.actf.model.internal.dom.sgml.errorhandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;

import org.eclipse.actf.model.dom.html.IErrorHandler;
import org.eclipse.actf.model.dom.html.IErrorLogListener;
import org.eclipse.actf.model.dom.html.IParser;
import org.eclipse.actf.model.dom.html.IParserError;
import org.eclipse.actf.model.dom.html.ParseException;
import org.eclipse.actf.model.internal.dom.html.parser.HTMLParser;
import org.eclipse.actf.model.internal.dom.sgml.ISGMLDocument;
import org.eclipse.actf.model.internal.dom.sgml.impl.EndTag;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * A sample implementation class of {@link IErrorHandler}.
 */
public class RangeExpander implements IErrorHandler {
	public boolean handleError(int code, IParser parser, Node errorNode)
			throws ParseException, IOException, SAXException {
		if (!(code == IParserError.SUDDEN_ENDTAG && errorNode instanceof EndTag)) {
			return false;
		}
		Vector<Element> elementsToBeInserted = new Vector<Element>();
		for (Node node = parser.getContext(); node instanceof Element; node = node
				.getParentNode()) {
			String tagName = node.getNodeName();
			if (tagName.equalsIgnoreCase(errorNode.getNodeName())) {
				parser.setContext((Element) node.getParentNode());
				for (Enumeration<Element> e = elementsToBeInserted.elements(); e
						.hasMoreElements();) {
					Element el = e.nextElement();
					parser.getContext().appendChild(el);
					parser.setContext(el);
				}
				return true;
			} else {
				elementsToBeInserted.addElement(parser.getDocument()
						.createElement(tagName));
			}
		}
		return false;
	}

	/**
	 * Just for tests.
	 */
	public static void main(String args[]) {
		try {
			HTMLParser parser = new HTMLParser();
			parser.addErrorLogListener(new IErrorLogListener() {
				public void errorLog(int code, String msg) {
					System.err.println(msg);
				}
			});
			parser.setErrorHandler(new RangeExpander());
			parser.parse(new java.io.FileInputStream(args[0]));
			((ISGMLDocument) parser.getDocument()).printAsSGML(new PrintWriter(
					System.out), false);
		} catch (Exception e) {
		}
	}
}
