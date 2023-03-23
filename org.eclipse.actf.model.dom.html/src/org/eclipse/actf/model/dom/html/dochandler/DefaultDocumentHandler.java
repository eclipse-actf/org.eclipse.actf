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

package org.eclipse.actf.model.dom.html.dochandler;

import org.eclipse.actf.model.internal.dom.sgml.ISGMLParser;
import org.eclipse.actf.model.internal.dom.sgml.impl.SGMLParser;
import org.xml.sax.AttributeList;
import org.xml.sax.DocumentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Default DocumentHandler class. This class makes linked list of document
 * handlers.
 */
public class DefaultDocumentHandler implements DocumentHandler {
	/**
	 * Next DocumentHandler messages are forwarded to.
	 */
	protected DocumentHandler another;

	/**
	 * Parser instance which this instance is added to and holding a next
	 * handler.
	 */
	protected ISGMLParser parser;

	/**
	 * Constructs DefaultDocumentHandler that points next DocumentHandler hold
	 * by the parser.
	 * 
	 * @param parser
	 *            parser instance that holds a next handler.
	 */
	public DefaultDocumentHandler(SGMLParser parser) {
		this.another = parser.getDocumentHandler();
		this.parser = parser;
	}

	/**
	 * Just forwards this method invokation with <code>locator</code> to the
	 * next handler.
	 */
	public void setDocumentLocator(Locator locator) {
		if (another != null)
			another.setDocumentLocator(locator);
	}

	/**
	 * Just forwards this method invokation with <code>locator</code> to the
	 * next handler.
	 */
	public void startDocument() throws SAXException {
		if (another != null)
			another.startDocument();
	}

	/**
	 * Just forwards this method invokation with <code>locator</code> to the
	 * next handler.
	 */
	public void endDocument() throws SAXException {
		if (another != null)
			another.endDocument();
	}

	/**
	 * Just forwards this method invokation with <code>locator</code> to the
	 * next handler.
	 */
	public void startElement(String name, AttributeList atts)
			throws SAXException {
		if (another != null)
			another.startElement(name, atts);
	}

	/**
	 * Just forwards this method invokation with <code>locator</code> to the
	 * next handler.
	 */
	public void endElement(String name) throws SAXException {
		if (another != null)
			another.endElement(name);
	}

	/**
	 * Just forwards this method invokation with <code>locator</code> to the
	 * next handler.
	 */
	public void characters(char ch[], int start, int length)
			throws SAXException {
		if (another != null)
			another.characters(ch, start, length);
	}

	/**
	 * Just forwards this method invokation with <code>locator</code> to the
	 * next handler.
	 */
	public void ignorableWhitespace(char ch[], int start, int length)
			throws SAXException {
		if (another != null)
			another.ignorableWhitespace(ch, start, length);
	}

	/**
	 * Just forwards this method invokation with <code>locator</code> to the
	 * next handler.
	 */
	public void processingInstruction(String target, String data)
			throws SAXException {
		if (another != null)
			another.processingInstruction(target, data);
	}
}
