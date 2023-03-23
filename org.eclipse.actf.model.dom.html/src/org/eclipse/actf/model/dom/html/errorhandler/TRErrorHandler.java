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

package org.eclipse.actf.model.dom.html.errorhandler;

import java.io.IOException;

import org.eclipse.actf.model.dom.html.IErrorHandler;
import org.eclipse.actf.model.dom.html.IParser;
import org.eclipse.actf.model.dom.html.IParserError;
import org.eclipse.actf.model.dom.html.ParseException;
import org.eclipse.actf.model.internal.dom.sgml.ISGMLParser;
import org.eclipse.actf.model.internal.dom.sgml.impl.ElementDefinition;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * If start tag of TR is missed and only its end tag exists, provide start tag.
 */
public class TRErrorHandler implements IErrorHandler {
	@SuppressWarnings("nls")
	public boolean handleError(int code, IParser parser, Node errorNode)
			throws ParseException, IOException, SAXException {
		Element context;
		String contextName;
		if (code == IParserError.ILLEGAL_CHILD && errorNode instanceof Element
				&& errorNode.getNodeName().equalsIgnoreCase("TD")) {
			context = parser.getContext();
			contextName = context.getNodeName();
			if (contextName.equalsIgnoreCase("TBODY")
					|| contextName.equalsIgnoreCase("TABLE")) {
				Element tr = parser.getDocument().createElement(
						parser.changeDefaultTagCase("TR"));
				ElementDefinition ed = ((ISGMLParser) parser).getDTD()
						.getElementDefinition(contextName);
				if (ed.contentMatch(((ISGMLParser) parser), context, tr)) {
					tr.insertBefore(errorNode, null);
					parser.setContext((Element) errorNode);
					return true;
				}
			}
		}
		return false;
	}
}
