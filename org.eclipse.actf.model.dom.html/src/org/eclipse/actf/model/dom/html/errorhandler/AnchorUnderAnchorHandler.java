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
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLAnchorElement;
import org.xml.sax.SAXException;

/**
 * A sample implementation of ErrorHandler.
 */
public class AnchorUnderAnchorHandler implements IErrorHandler {
	private boolean isAnchor(Node node) {
		return node instanceof HTMLAnchorElement
				|| (node instanceof Element && node.getNodeName()
						.equalsIgnoreCase("A")); //$NON-NLS-1$
	}

	public boolean handleError(int code, IParser parser, Node errorNode)
			throws ParseException, IOException, SAXException {
		if (code == IParserError.ILLEGAL_CHILD && isAnchor(errorNode)) {
			for (Node node = parser.getContext(); node instanceof Element; node = node
					.getParentNode()) {
				if (isAnchor(node)) {
					Node parent = node.getParentNode();
					parent.insertBefore(errorNode, null);
					parser.setContext((Element) errorNode);
					return true;
				}
			}
		}
		return false;
	}

}
