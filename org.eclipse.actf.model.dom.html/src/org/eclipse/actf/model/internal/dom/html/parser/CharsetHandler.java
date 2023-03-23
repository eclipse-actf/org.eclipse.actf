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

package org.eclipse.actf.model.internal.dom.html.parser;

import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.actf.model.dom.html.IHTMLParser;
import org.eclipse.actf.model.dom.html.IParserError;
import org.eclipse.actf.model.dom.html.dochandler.DefaultDocumentHandler;
import org.eclipse.actf.model.internal.dom.html.util.RereadableInputStream;
import org.xml.sax.AttributeList;
import org.xml.sax.SAXException;


/**
 * Watch a element that specify character encoding of the document. If
 * <code> &lt;META http-equiv="Content-Type" content="text/html; charset=xxx"&gt;
 * appears in a document, this method changes character encoding.
 */
public class CharsetHandler extends DefaultDocumentHandler {
	private RereadableInputStream ris;

	public CharsetHandler(HTMLParser parser, RereadableInputStream ris) {
		super(parser);
		this.ris = ris;
	}

	@SuppressWarnings("nls")
	public void startElement(String name, AttributeList atts)
			throws SAXException {
		if (name.equalsIgnoreCase("META")) {
			String httpEquiv = null;
			String content = null;
			for (int i = 0; i < atts.getLength(); i++) {
				name = atts.getName(i);
				if (name.equalsIgnoreCase("http-equiv")) {
					httpEquiv = atts.getValue(i);
				} else if (name.equalsIgnoreCase("content")) {
					content = atts.getValue(i);
				}
			}
			if (httpEquiv == null || content == null) {
				super.startElement(name, atts);
				return;
			}
			if (httpEquiv.equalsIgnoreCase("Content-Type")) {
				int charsetIndex = content.indexOf("charset=");
				if (charsetIndex == -1) {
					charsetIndex = content.indexOf("CHARSET=");
				}
				if (charsetIndex != -1) {
					String encoding = content.substring(charsetIndex
							+ "charset=".length());
					try {
						InputStreamReader newReader = new InputStreamReader(
								this.ris, encoding);
						if (((IHTMLParser) parser).getEncoding() == null) {
							((HTMLParser) parser).setEncoding(encoding);
						}
						encoding = newReader.getEncoding();
						String prevEncoding = ((HTMLParser) parser)
								.getISReader().getEncoding();
						if (prevEncoding.equals(encoding)) {
							parser.setDocumentHandler(another);
						} else {
							this.ris.reset();
							((HTMLParser) parser).getISReader().close();
							parser.setDocumentHandler(another);
							throw new EncodingException(newReader);
						}
					} catch (IOException e) {
						parser.error(IParserError.MISC_ERR, e.getMessage());
						parser.setDocumentHandler(another);
						super.startElement(name, atts);
						return;
					}
				}
			}
		} else if (name.equalsIgnoreCase("BODY")) {
			parser.setDocumentHandler(another);
		}
		super.startElement(name, atts);
	}
}
