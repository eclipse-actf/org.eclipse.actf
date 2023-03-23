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
import org.eclipse.actf.model.dom.html.IHTMLParser;
import org.eclipse.actf.model.dom.html.IParser;
import org.eclipse.actf.model.dom.html.IParserError;
import org.eclipse.actf.model.dom.html.ParseException;
import org.eclipse.actf.model.dom.html.NodeUtil;
import org.eclipse.actf.model.internal.dom.html.parser.HTMLParser;
import org.eclipse.actf.model.internal.dom.sgml.impl.EndTag;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * ErrorHandler implementation for illegally closed form context. Usually to add
 * an error handler, you uses {@link IParser#addErrorHandler(IErrorHandler)}.
 * However, this error handler conflicts with {@link HTMLErrorHandler} installed
 * to parsers by default. Thus, instead of that method use {@link 
 * #addTo(IHTMLParser)}. And to remove this, use {@link #remove()}. Note: This
 * error handler is <em>experimental</em>.
 * 
 */
public class FormExpander implements IErrorHandler {
	private HTMLErrorHandler htmlErrorHandler;

	private HTMLParser parser;

	/**
	 * Add this error handler to <code>parser</code>
	 */
	public void addTo(IHTMLParser parser) {
		IErrorHandler errorHandlers[] = parser.getErrorHandlers();
		for (int i = 0; i < errorHandlers.length; i++) {
			if (errorHandlers[i] instanceof HTMLErrorHandler) {
				htmlErrorHandler = (HTMLErrorHandler) errorHandlers[i];
				htmlErrorHandler.setKeepForm(false);
				break;
			}
		}
		parser.addErrorHandler(this);
	}

	/**
	 * Remove this error handler from the parser.
	 */
	public void remove() {
		if (htmlErrorHandler != null) {
			htmlErrorHandler.setKeepForm(true);
			parser.removeErrorHandler(this);
		}
	}

	/**
	 * Find the last <code>FORM</code> element and relink it to the lowest
	 * position whereas it covers its start and end tag.
	 */
	@SuppressWarnings("nls")
	public boolean handleError(int code, IParser parser, Node errorNode)
			throws ParseException, IOException, SAXException {
		String nodeName = errorNode.getNodeName();
		if (code == IParserError.FLOATING_ENDTAG && errorNode instanceof EndTag
				&& nodeName.equalsIgnoreCase("FORM")) {
			// Element context = parser.getContext();
			NodeList forms = parser.getDocument().getElementsByTagName("FORM");
			Element targetForm = (Element) forms.item(forms.getLength() - 1);
			if (targetForm == null || parser.hasEndTag(targetForm))
				return false;
			// Checks if target form is under the context.
			Element targetFirstNode = targetForm;
			Element contexts[] = parser.getContextElements();
			outer: while (targetFirstNode.getParentNode() instanceof Element) {
				Element parent = (Element) targetFirstNode.getParentNode();
				if (targetFirstNode.getElementsByTagName("FORM").getLength() > 1)
					break;
				for (int c = contexts.length - 1; c >= 0; c--) {
					if (contexts[c] == parent) {
						for (Node child = targetFirstNode.getNextSibling(); child != null; child = child
								.getNextSibling()) {
							if (child instanceof Element
									&& ((Element) child).getElementsByTagName(
											"FORM").getLength() > 0) {
								break outer;
							}
						}
						NodeUtil.remove(targetForm);
						NodeUtil.add(parent, targetForm, targetFirstNode,
								parent.getLastChild());
						if (c != contexts.length - 1)
							parser.setContext(parent);
						((EndTag) errorNode).setElement(targetForm);
						parser.setHasEndTag(targetForm);
						parser.error(code, "Expanded " + targetForm);
						return true;
					}
				}
				targetFirstNode = parent;
			}
		}
		return false;
	}
}
