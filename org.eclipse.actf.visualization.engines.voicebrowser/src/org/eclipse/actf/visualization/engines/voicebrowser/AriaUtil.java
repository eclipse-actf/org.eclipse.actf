/*******************************************************************************
 * Copyright (c) 2024 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.engines.voicebrowser;

import java.util.Stack;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class AriaUtil {

	/**
	 * 
	 * @param target Element
	 * @return calculated alternative text or null (if not specified)
	 */
	public static String getAlternativeText(Element target, Document doc) {
		String result = null;

		switch (target.getTagName()) {
		case "img" -> {
			result = getAriaLabelledBy(target, doc);
			if (null != result)
				return result;

			if (target.hasAttribute("aria-label")) {
				result = target.getAttribute("aria-label");
				if (null != result && result.trim().length() == 0) {
					// if empty, override by alt
					if (target.hasAttribute("alt")) {
						result = target.getAttribute("alt");
					}
				}
			}
			if (null != result)
				return result;

			if (target.hasAttribute("alt")) {
				result = target.getAttribute("alt");
			}
		}
		}

		return result;
	}

	private static String getAriaLabelledBy(Element target, Document doc) {
		String result = null;
		if (target.hasAttribute("aria-labelledby")) {
			String targetS = target.getAttribute("aria-labelledby").trim();
			if (targetS.length() > 0) {
				StringBuffer tmpSB = new StringBuffer();
				String[] IDs = targetS.split(" ");
				for (String id : IDs) {
					Element tmpE = doc.getElementById(id);
					if (null != tmpE) {
						String tmpS = getTextAltDescendant(tmpE).trim();
						if (tmpS.length() > 0) {
							tmpSB.append(tmpS + " ");
						}
					}
					if (tmpSB.length() > 0) {
						result = tmpSB.substring(0, tmpSB.length() - 1);
					}
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * @param target Element
	 * @return calculated description or null (if not specified)
	 */
	public static String getAriaDescribedBy(Element target, Document doc) {
		String result = null;
		if (target.hasAttribute("aria-describedby")) {
			String targetS = target.getAttribute("aria-describedby").trim();
			if (targetS.length() > 0) {
				StringBuffer tmpSB = new StringBuffer();
				String[] IDs = targetS.split(" ");
				for (String id : IDs) {
					Element tmpE = doc.getElementById(id);
					if (null != tmpE) {
						String tmpS = getTextAltDescendant(tmpE).trim();
						if (tmpS.length() > 0) {
							tmpSB.append(tmpS + " ");
						}
					}
					if (tmpSB.length() > 0) {
						result = tmpSB.substring(0, tmpSB.length() - 1);
					}
				}
			}
		}
		return result;
	}

	/**
	 * Gather text and alternative text from descendant nodes and return it as
	 * String.
	 * 
	 * @param target target {@link Node}
	 * @return gathered text and alternative text
	 */
	private static String getTextAltDescendant(Node target) {
		Node curNode = target.getFirstChild();
		StringBuffer strBuf = new StringBuffer(512);
		Stack<Node> stack = new Stack<Node>();
		while (curNode != null) {
			if (curNode.getNodeType() == Node.TEXT_NODE) {
				strBuf.append(curNode.getNodeValue().trim() + " ");
			} else if (curNode.getNodeName().equalsIgnoreCase("img")) { //$NON-NLS-1$
				strBuf.append(((Element) curNode).getAttribute("alt").trim() + " "); //$NON-NLS-2$
			}

			if (curNode.hasChildNodes()) {
				stack.push(curNode);
				curNode = curNode.getFirstChild();
			} else if (curNode.getNextSibling() != null) {
				curNode = curNode.getNextSibling();
			} else {
				curNode = null;
				while ((curNode == null) && (stack.size() > 0)) {
					curNode = stack.pop();
					curNode = curNode.getNextSibling();
				}
			}
		}

		return strBuf.toString();
	}

}
