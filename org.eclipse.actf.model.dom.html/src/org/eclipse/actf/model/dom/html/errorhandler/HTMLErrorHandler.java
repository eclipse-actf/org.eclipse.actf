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
import org.eclipse.actf.model.internal.dom.sgml.ISGMLParser;
import org.eclipse.actf.model.internal.dom.sgml.impl.ElementDefinition;
import org.eclipse.actf.model.internal.dom.sgml.impl.EndTag;
import org.eclipse.actf.model.internal.dom.sgml.impl.SGMLText;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class HTMLErrorHandler implements IErrorHandler {
	/**
	 * Searches proper a parent node of node. Behavior is described as follows.
	 * <ol>
	 * <li> If <code>node</code> is LINK, STYLE BASE, ISINDEX or META element,
	 * add it to last HEAD element.
	 * <li> If context and <code>node</code> are equal nodes, Changes Context
	 * to its parent and append <code>node</code> to context. <!--
	 * <li> If context is HTML element,
	 * <ol>
	 * <li>If <code>node<code> is BODY
	 * element, append <code>node</code> to context and change it to
	 * <code>node</code> (This strange case occurs when frameset dtd is used).
	 * <li> If context has only a HEAD element, makes BODY element as
	 * a next sibling of the HEAD and appends <code>node</code> and changes 
	 * context to it.
	 * </ol>
	 * -->
	 * </ol>
	 * @param code error type
	 * @param parser caller of this handler. This parser's state is easily changed by the methods below.
	errorNode a node that causes the error.
	 * @param errorNode illegal child node.
	 * @return true if found.  Otherwise false.
	 * @see org.eclipse.actf.model.dom.html.IParser#getContext()
	 * @see org.eclipse.actf.model.dom.html.IParser#setContext(org.w3c.dom.Element)
	 */
	@SuppressWarnings("nls")
	public boolean handleError(int code, IParser parser, Node errorNode)
			throws ParseException, IOException, SAXException {
		if (code == IParserError.ILLEGAL_ATTRIBUTE) {
			return false;
		}
		String nodeName = errorNode.getNodeName();
		Element context = parser.getContext();
		Node contextParent = context.getParentNode();
		String contextName = context.getNodeName();
		NodeList bodies;
		if (errorNode instanceof Element) {
			ElementDefinition ed = ((ISGMLParser) parser).getDTD()
					.getElementDefinition(nodeName);
			if (ed == null) {
				return false;
			}
			if (nodeName.equalsIgnoreCase("LINK")
					|| nodeName.equalsIgnoreCase("STYLE")
					|| nodeName.equalsIgnoreCase("META")
					|| nodeName.equalsIgnoreCase("BASE")
					|| nodeName.equalsIgnoreCase("ISINDEX")) {
				Element html = parser.getDocument().getDocumentElement();
				for (Node child = html.getLastChild(); child != null; child = child
						.getPreviousSibling()) {

					// System.out.println("ErrorHandler: "+child.getNodeName());

					if (child instanceof Element
							&& child.getNodeName().equalsIgnoreCase("HEAD")) {
						child.insertBefore(errorNode, null);
						parser.error(code, errorNode + " must be under "
								+ child);
						return true;
					}
				}
			} else if (nodeName.equalsIgnoreCase("BODY")) {
				Element top = parser.getDocument().getDocumentElement();
				for (Node child = top.getFirstChild(); child != null; child = child
						.getNextSibling()) {
					if (child instanceof Element
							&& (child.getNodeName().equalsIgnoreCase("BODY") || child
									.getNodeName().equalsIgnoreCase("FRAMESET"))) {
						return false;
					}
				}
				top.insertBefore(errorNode, null);
				parser.setContext((Element) errorNode);
				parser.error(code, errorNode + " must be under " + top);
				return true;
			} else if (nodeName.equalsIgnoreCase("HEAD")) {
				Document doc = parser.getDocument();
				if (context.getElementsByTagName("HEAD").getLength() == 0
						&& ((bodies = doc.getElementsByTagName("BODY"))
								.getLength() > 0 || (bodies = doc
								.getElementsByTagName("FRAMESET")).getLength() > 0)) {
					Element body = (Element) bodies.item(0);
					body.getParentNode().insertBefore(errorNode, body);
					parser.setContext((Element) errorNode);
					parser.error(code, errorNode + " must be before " + body);
					return true;
				} else {
					if (doc.getElementsByTagName("BODY").getLength() > 0
							|| doc.getElementsByTagName("FRAMESET").getLength() > 0) {
						parser.error(code,
								"HTMLErrorHandler makes parser ignore "
										+ errorNode);
						return true;
					}
				}
			} else if (contextName.equalsIgnoreCase("HTML")) {
				for (Node child = context.getLastChild(); child != null; child = child
						.getPreviousSibling()) {
					if (child instanceof Element
							&& child.getNodeName().equalsIgnoreCase("BODY")) {
						parser.error(code,
								"BODY context is already closed.  Reopen it.");
						parser.reopenContext(1);
						parser.getContext().insertBefore(errorNode, null);
						parser.setContext((Element) errorNode);
						return true;
					} else if (child instanceof Comment
							|| child instanceof ProcessingInstruction
							|| child instanceof Text
							&& whitespaceText((Text) child)) {
						continue;
					} else {
						break;
					}
				}
			} else if (nodeName.equalsIgnoreCase(contextName)
					&& ed.endTagOmittable() && contextParent instanceof Element) {
				// parser.setContext((Element)contextParent);
				contextParent.insertBefore(errorNode, null);
				parser.setContext((Element) errorNode);
				return true;
			}
		} else if (code == IParserError.SUDDEN_ENDTAG) {
			try {
				@SuppressWarnings("unchecked")
				Vector<EndTag> missedEndtags = (Vector<EndTag>) parser
						.getExtraErrInfo();
				for (Enumeration<EndTag> e = missedEndtags.elements(); e
						.hasMoreElements();) {
					EndTag missedEtag = e.nextElement();
					String missedEtagName = missedEtag.getNodeName();
					if (missedEtagName.equalsIgnoreCase("TABLE")) {
						// ignore the endtag
						return true;
					} else if (keepForm
							&& missedEtagName.equalsIgnoreCase("FORM")
							&& (nodeName.equalsIgnoreCase("TD") || nodeName
									.equalsIgnoreCase("TR"))) {
						// TD is not able to close FORM
						return true;
					}
				}
			} catch (ClassCastException e) {
			}
		}
		return false;
	}

	private boolean keepForm = true;

	void setKeepForm(boolean keep) {
		this.keepForm = keep;
	}

	private boolean whitespaceText(Text text) {
		if (text instanceof SGMLText) {
			return ((SGMLText) text).getIsWhitespaceInElementContent();
		}
		char str[] = text.getData().toCharArray();
		for (int i = 0; i < str.length; i++) {
			if (!Character.isWhitespace(str[i]))
				return false;
		}
		return true;
	}
}
