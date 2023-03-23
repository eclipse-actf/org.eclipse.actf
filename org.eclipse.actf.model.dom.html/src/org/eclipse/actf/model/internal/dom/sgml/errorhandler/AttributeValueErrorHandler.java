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

import org.eclipse.actf.model.dom.html.IParserError;
import org.eclipse.actf.model.internal.dom.sgml.ISGMLConstants;
import org.eclipse.actf.model.internal.dom.sgml.ISGMLParser;


public class AttributeValueErrorHandler implements ITokenErrorHandler, ISGMLConstants {
	public boolean handleError(int code, ISGMLParser parser, String errorStr)
			throws IOException {
		if (code != IParserError.ATTR_VALUE)
			return false;
		int gtIndex = errorStr.indexOf('>');
		int ltIndex;
		char closingChar;
		int errorStrLen = errorStr.length();
		if (errorStrLen == 0)
			return false;
		if ((closingChar = errorStr.charAt(0)) != '\'' && closingChar != '"') {
			return false;
		}
		if (gtIndex > 1 && (ltIndex = errorStr.indexOf('<')) > gtIndex) {
			// case of attr="value_without_endquote> ... <nextTag
			int nextTagBegin = ltIndex + 1;
			if (errorStr.charAt(nextTagBegin) == '/')
				nextTagBegin++;
			int nextTagEnd = nextTagBegin;
			char c0;
			while (Character.isLowerCase(c0 = errorStr.charAt(nextTagEnd))
					|| Character.isUpperCase(c0)) {
				nextTagEnd++;
			}
			if (nextTagBegin == nextTagEnd)
				return false;
			String nextTag = errorStr.substring(nextTagBegin, nextTagEnd);
			if (parser.getDTD().getElementDefinition(nextTag) != null) {
				parser.error(IParserError.STARTTAG_SYNTAX_ERR,
						" attribute value missed closing quotation."); //$NON-NLS-1$
				parser.insert(errorStr.substring(0, gtIndex) + closingChar
						+ errorStr.substring(gtIndex));
				return true;
			}
		} else if (errorStr.charAt(errorStrLen - 1) == closingChar
				&& errorStr.length() > 1
				&& errorStr.charAt(errorStrLen - 2) == '=') {
			// case of attr="value_without_endquote
			// nextAttrName="nextAttrValue...
			for (int i = 0; i < errorStrLen; i++) {
				if (Character.isWhitespace(errorStr.charAt(i))) {
					int errorPoint = i;
					while (Character.isWhitespace(errorStr.charAt(++i)))
						;
					char c0;
					// int nextAttrNameStart = i;
					c0 = errorStr.charAt(i);
					if (!Character.isLowerCase(c0)
							&& !Character.isUpperCase(c0)) {
						return false;
					}
					do {
						i++;
						c0 = errorStr.charAt(i);
					} while (Character.isLowerCase(c0)
							|| Character.isUpperCase(c0));
					while (Character.isWhitespace(c0 = errorStr.charAt(i)))
						i++;
					if (c0 == '=') {
						parser.insert(errorStr.substring(0, errorPoint)
								+ closingChar + errorStr.substring(errorPoint));
						parser.error(IParserError.STARTTAG_SYNTAX_ERR,
								" attribute value missed closing quotation."); //$NON-NLS-1$
						return true;
					}
				}
			}
		}
		return false;
	}
}
