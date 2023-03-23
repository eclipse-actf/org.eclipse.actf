/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.model.dom.html;

import org.eclipse.actf.model.internal.dom.html.parser.HTMLParser;

/**
 * Factory class for {@link IHTMLParser}
 */
public class HTMLParserFactory {

	/**
	 * @return new instance of {@link IHTMLParser}
	 */
	public static IHTMLParser createHTMLParser() {
		IHTMLParser parser = new HTMLParser();
		parser.setTagCase(IParser.LOWER_CASE);
		parser.setAttrNameCase(IParser.LOWER_CASE);
		parser.setDefaultTagCase(IParser.LOWER_CASE);
		parser.keepUnknownElements(true);
		parser.elementHandle(false); // to get line number

		return parser;
	}

}
