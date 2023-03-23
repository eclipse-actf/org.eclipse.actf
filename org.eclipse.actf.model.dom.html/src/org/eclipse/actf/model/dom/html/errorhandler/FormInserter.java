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
import java.util.Enumeration;
import java.util.Vector;

import org.eclipse.actf.model.dom.html.IErrorHandler;
import org.eclipse.actf.model.dom.html.IParser;
import org.eclipse.actf.model.dom.html.IParserError;
import org.eclipse.actf.model.dom.html.ParseException;
import org.eclipse.actf.model.internal.dom.sgml.impl.EndTag;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * ErrorHandler implementation for illegally closed form context.
 */
public class FormInserter implements IErrorHandler {
	private static final String FORM = "FORM"; //$NON-NLS-1$

	public boolean handleError(int code, IParser parser, Node errorNode)
			throws ParseException, IOException, SAXException {
		if (code != IParserError.FLOATING_ENDTAG)
			return false;
		if (errorNode instanceof EndTag
				&& errorNode.getNodeName().equalsIgnoreCase(FORM)) {
			Element context = parser.getContext();
			Vector<Node> nodes = new Vector<Node>();
			boolean includeFormCtrl = false;
			for (Node node = context.getLastChild(); node != null; node = node
					.getPreviousSibling()) {
				if (node instanceof Element) {
					Element element = (Element) node;
					if (hasElement(element, formName)) {
						break;
					} else if (hasFormCtrl(element)) {
						includeFormCtrl = true;
					}
				}
				nodes.insertElementAt(node, 0);
			}
			if (!includeFormCtrl)
				return false;
			Element form = parser.getDocument().createElement(
					parser.changeDefaultTagCase(FORM));
			for (Enumeration<Node> e = nodes.elements(); e.hasMoreElements();) {
				Node node = e.nextElement();
				context.removeChild(node);
				form.insertBefore(node, null);
			}
			context.insertBefore(form, null);
			return true;
		}
		return false;
	}

	private String formName[] = { FORM };

	@SuppressWarnings("nls")
	private String formCtrls[] = { "INPUT", "SELECT", "TEXTAREA", "LABEL",
			"BUTTON" };

	private boolean hasFormCtrl(Element top) {
		return hasElement(top, formCtrls);
	}

	private boolean hasElement(Element top, String elementNames[]) {
		Node tmp1, tmp2;
		tmp1 = top;
		while (tmp1 != null) {
			if (tmp1 instanceof Element) {
				for (int i = 0; i < elementNames.length; i++) {
					if (tmp1.getNodeName().equalsIgnoreCase(elementNames[i])) {
						return true;
					}
				}
			}
			if ((tmp2 = tmp1.getFirstChild()) == null) {
				if (tmp1 != top) {
					tmp2 = tmp1.getNextSibling();
				} else {
					return false;
				}
			}
			while (tmp2 == null) {
				tmp1 = tmp2 = tmp1.getParentNode();
				if (tmp1 != top) {
					tmp2 = tmp1.getNextSibling();
				} else {
					return false;
				}
			}
			tmp1 = tmp2;
		}
		return false;
	}
}
