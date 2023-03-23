/*******************************************************************************
 * Copyright (c) 2004, 2015 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.eval.html;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.eclipse.actf.util.xpath.XPathServiceFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Utility class for HTML evaluation.
 */
@SuppressWarnings("nls")
public class HtmlTagUtil implements IHtmlEventHandlerAttributes {

	public static final String ATTR_HREF = "href";

	public static final String ATTR_ALT = "alt";

	public static final String ATTR_SRC = "src";

	public static final String ATTR_TITLE = "title";

	public static final String FLASH_OBJECT = "clsid:d27cdb6e-ae6d-11cf-96b8-444553540000";

	public static final String FLASH_CODEBASE = "http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab";

	public static final String FLASH_TYPE = "application/x-shockwave-flash";

	public static final String FLASH_PLUGINSPAGE = "http://www.macromedia.com/go/getflashplayer";

	private static final String[] BLOCK_ELEMENT = { "address", "blockquote",
			"center", "dir", "div", "dl", "fieldset", "form", "h1", "h2", "h3",
			"h4", "h5", "h6", "hr", "isindex", "menu", "noframes", "noscript",
			"ol", "p", "pre", "table", "ul", "dd",
			// inline? "dt",
			"frameset", "li", "tbody", "td", "tfoot", "th", "thead", "tr" };

	/**
	 * Get tag names of block element in HTML
	 * 
	 * @return set of block element tag names
	 */
	public static Set<String> getBlockElementSet() {
		Set<String> blockEleSet = new HashSet<String>();
		for (int i = 0; i < BLOCK_ELEMENT.length; i++) {
			blockEleSet.add(BLOCK_ELEMENT[i]);
		}
		return (blockEleSet);
	}

	/**
	 * Check if target {@link Node} has ancestor whose name is specified target
	 * name.
	 * 
	 * @param target
	 *            target {@link Node}
	 * @param ancestorName
	 *            target ancestor tag name
	 * @return true if target {@link Node} has target ancestor
	 */
	public static boolean hasAncestor(Node target, String ancestorName) {
		boolean result = false;
		Node tmpNode = target;
		while (tmpNode != null) {
			if (tmpNode.getNodeName().equals(ancestorName)) {
				result = true;
				break;
			}
			tmpNode = tmpNode.getParentNode();
		}
		return (result);
	}

	/**
	 * Get ancestor node whose name is specified target name
	 * 
	 * @param target
	 *            target {@link Node}
	 * @param ancestorName
	 *            target ancestor tag name
	 * @return target ancestor {@link Node} or null
	 */
	public static Node getAncestor(Node target, String ancestorName) {
		Node tmpNode = target;
		while (tmpNode != null) {
			if (tmpNode.getNodeName().equals(ancestorName)) {
				return tmpNode;
			}
			tmpNode = tmpNode.getParentNode();
		}
		return null;
	}

	/**
	 * Get <i>noscript</i> text of the {@link Node}
	 * 
	 * @param target
	 *            target {@link Node}
	 * @return <i>noscript</i> text
	 */
	public static String getNoScriptText(Node target) {
		StringBuffer tmpSB = new StringBuffer();
		if (target.getNodeType() == Node.ELEMENT_NODE) {
			Element tmpE = (Element) target;
			// TBD check neighbor?
			if (tmpE.getElementsByTagName("script").getLength() > 0) {
				NodeList tmpNL = tmpE.getElementsByTagName("noscript");
				for (int i = 0; i < tmpNL.getLength(); i++) {
					tmpSB.append(getTextAltDescendant(tmpNL.item(i)));
				}
			}
		}
		return tmpSB.toString();
	}

	/**
	 * Gather text and alternative text from descendant nodes and return it as
	 * String.
	 * 
	 * @param target
	 *            target {@link Node}
	 * @return gathered text and alternative text
	 */
	public static String getTextAltDescendant(Node target) {
		Node curNode = target.getFirstChild();
		StringBuffer strBuf = new StringBuffer(512);
		Stack<Node> stack = new Stack<Node>();
		while (curNode != null) {
			if (curNode.getNodeType() == Node.TEXT_NODE) {
				strBuf.append(curNode.getNodeValue().trim() + " ");
			} else if (curNode.getNodeName().equalsIgnoreCase("img")) { //$NON-NLS-1$
				strBuf.append(((Element) curNode).getAttribute(ATTR_ALT).trim()
						+ " "); //$NON-NLS-1$
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

	/**
	 * Gather text from descendant nodes and return it as String.
	 * 
	 * @param target
	 *            target {@link Node}
	 * @return gathered text
	 */
	public static String getTextDescendant(Node target) {
		Node curNode = target.getFirstChild();
		StringBuffer strBuf = new StringBuffer(512);
		Stack<Node> stack = new Stack<Node>();
		while (curNode != null) {
			if (curNode.getNodeType() == Node.TEXT_NODE) {
				strBuf.append(curNode.getNodeValue());
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

	/**
	 * Check if target {@link Node} has text descendant
	 * 
	 * @param target
	 *            target {@link Node}
	 * @return true if target {@link Node} has text descendant
	 */
	public static boolean hasTextDescendant(Node target) {
		Node curNode = target.getFirstChild();
		Stack<Node> stack = new Stack<Node>();
		while (curNode != null) {
			if (curNode.getNodeType() == Node.TEXT_NODE) {
				return (true);
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
		return (false);
	}

	public static List<Element> getImgElementsFromMap(Document target,
			Element map) {
		List<Element> result = new ArrayList<Element>();
		String mapName = map.getAttribute("name");
		NodeList images = XPathServiceFactory.newService().evalPathForNodeList(
				"//img[@usemap='#" + mapName + "']", target);
		for (int i = 0; i < images.getLength(); i++) {
			Element image = (Element) images.item(i);
			result.add(image);
		}
		return result;
	}

	public static boolean isTextControl(Element ctrl) {
		String tagName = ctrl.getTagName().toLowerCase();
		return (tagName.equals("textarea") || (tagName.equals("input") && ctrl
				.getAttribute("type").toLowerCase().matches("|text|password")));
	}

	public static boolean isButtonControl(Element ctrl) {
		String tagName = ctrl.getTagName().toLowerCase();
		return ((tagName.equals("button") && ctrl.getAttribute("type")
				.toLowerCase().matches("submit|reset|button")) || (tagName
				.equals("input") && ctrl.getAttribute("type").toLowerCase()
				.matches("submit|reset|button|image")));
	}

	public static boolean isBlankString(String str) {
		return str.matches("[\\p{Space}\u3000\u00A0]*");
	}
}
