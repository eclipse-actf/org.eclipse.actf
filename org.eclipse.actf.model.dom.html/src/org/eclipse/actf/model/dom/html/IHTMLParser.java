/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Goh KONDOH - initial API and implementation
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.model.dom.html;

import java.io.IOException;
import java.io.InputStream;

import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public interface IHTMLParser extends IParser {

	/**
	 * Parses an HTML document and return its top element.
	 * 
	 * @param is
	 *            target {@link InputStream} to parse with default encoding. 
	 *            The InputStream will be closed after parsing.
	 * @return Top element.
	 * @exception PaserException
	 *                If unrecoverable syntax or token error occurred, thrown
	 * @exception IOException
	 */
	public Node parse(InputStream is) throws ParseException, IOException,
			SAXException;

	/**
	 * Parses a HTML document and return its top element.
	 * 
	 * @param is
	 *            target {@link InputStream} to parse.
	 *            The InputStream will be closed after parsing.
	 * @param charEncoding
	 *            encoding used for parse
	 * @return Top element.
	 * @exception PaserException
	 *                If unrecoverable syntax or token error occurred, thrown
	 * @exception IOException
	 */
	public Node parse(InputStream is, String charEncoding) throws SAXException,
			ParseException, IOException;

	/**
	 * Parses a HTML document and return its top element. This method is almost
	 * same as {@link #parse(InputStream) parse(InputStream)}. If it meets
	 * <code> &lt;META http-equiv="Content-Type" 
	 * content="text/html; charset=xxx"&gt;</code>
	 * tag in a document, it tries to change encoding to <code>xxx</code>.
	 * 
	 * @param is
	 *            target {@link InputStream} to parse.
	 *            The InputStream will be closed after parsing.
	 * @return Top element.
	 * @exception PaserException
	 *                If unrecoverable syntax or token error occurred, thrown
	 * @exception IOException
	 */
	public Node parseSwitchEnc(InputStream is) throws ParseException,
			IOException, SAXException;

	/**
	 * Parses a HTML document and return its top element. This method is the
	 * same as {@link #parse(InputStream,String) parse(InputStream,String)} If
	 * it meets <code> &lt;META http-equiv="Content-Type"
	 * content="text/html; charset=xxx"&gt;</code>
	 * tag in a document, it tries to change encoding to <code>xxx</code>.
	 * 
	 * @param is
	 *            target {@link InputStream} to parse.
	 *            The InputStream will be closed after parsing.
	 * @param defaultEncoding
	 *            default encoding before switching encoding.
	 * @return Top element.
	 * @exception PaserException
	 *                If unrecoverable syntax or token error occurred, thrown
	 * @exception IOException
	 */
	public Node parseSwitchEnc(InputStream is, String defaultEncoding)
			throws SAXException, ParseException, IOException;

	/**
	 * Character Encoding parsed a document with.
	 * 
	 * @return If null parsed a document with default encoding.
	 */
	public String getEncoding();

}
