/*******************************************************************************
 * Copyright (c) 2003, 2016 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Masahide WASHIZAWA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.engines.voicebrowser;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@SuppressWarnings("nls")
public class JWATCore {
	private static MessageCollection mc = null;

	private static String uriPrefix = null;

	/**
	 * Method setJwatMode.
	 * 
	 * @param mode
	 * @param xmlpath
	 * @return MessageCollection
	 */
	public MessageCollection setJwatMode(int mode, String xmlpath) {
		try {
			if (mc != null && mc.size() > 0)
				mc.clear();
			mc = OutLoud.createMessageCollection(mode, xmlpath);
			return mc;
		} catch (Exception e) {
			e.printStackTrace(System.err);
			return null;
		}
	}

	/**
	 * Method getPacketCollection.
	 * 
	 * @param node
	 * @return PacketCollection
	 */
	public PacketCollection getPacketCollection(Node node) {
		// put parent nodes on the stack;
		try {
			// traverse
			PacketCollection result = getPacketCollectionTraversely(node,
					getCurrentParentsStack(node));
			return result;
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			return null;
		}
	}

	/**
	 * Method getCurrentParentsStack.
	 * 
	 * @param node
	 * @return Stack
	 */
	private static Stack<Node> getCurrentParentsStack(Node node) {
		try {
			Stack<Node> parentsStack = new Stack<Node>();
			Stack<Node> tempStack = new Stack<Node>();
			Node tempNode = node;
			while (tempNode != null) {
				tempStack.push(tempNode);
				tempNode = tempNode.getParentNode();
			}

			while (tempStack.size() > 1) {
				// Node curNode = (Node) tempStack.peek();
				parentsStack.push(tempStack.pop());
			}
			return parentsStack;
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			return null;
		}
	}

	/**
	 * Method getPacketCollectionTraversely.
	 * 
	 * @param node
	 * @param elementStack
	 * @return PacketCollection
	 */
	private PacketCollection getPacketCollectionTraversely(Node node,
			Stack<Node> elementStack) {

		Context curContext = new Context();
		PacketCollection result = new PacketCollection();
		Node curReadNode = node;
		while (curReadNode != null) {

			PacketCollection tmp = getPacketCollectionOneNode(curReadNode,
					curContext);

			if (tmp != null) {
				result.addAll(tmp);
			}
			curReadNode = goNext(curReadNode, curContext, elementStack, result);
		}
		return result;
	}

	/**
	 * Method goNext.
	 * 
	 * @param curReadNode
	 * @param elementStack
	 * @param result
	 * @return Node
	 */
	private Node goNext(Node curReadNode, Context curContext,
			Stack<Node> elementStack, PacketCollection result) {

		PacketCollection tmp = null;

		if (curReadNode == null)
			return null;

		if ((curContext.isGoChild()) && curReadNode.hasChildNodes()) {
			// Has Child
			elementStack.push(curReadNode);
			return curReadNode.getFirstChild();

		} else if (curReadNode.getNextSibling() != null) {
			// Has Brother
			if ((result != null)
					&& (curReadNode.getNodeType() == Node.ELEMENT_NODE)) {
				IElementRenderer renderer = getElementRenderer((Element) curReadNode);
				if (renderer != null) {
					tmp = renderer.getPacketCollectionOut(
							(Element) curReadNode, curContext, uriPrefix, mc);

					if (tmp != null) {
						result.addAll(tmp);
					}
				}
			}
			return curReadNode.getNextSibling();
		} else {
			// Check Parent
			curReadNode = null;
			while (curReadNode == null) {
				if (elementStack.size() == 0) {
					break;
				}
				curReadNode = elementStack.pop();

				if ((result != null)
						&& (curReadNode.getNodeType() == Node.ELEMENT_NODE)) {
					IElementRenderer renderer = getElementRenderer((Element) curReadNode);
					if (renderer != null) {
						tmp = renderer.getPacketCollectionOut(
								(Element) curReadNode, curContext, uriPrefix,
								mc);

						if (tmp != null) {
							result.addAll(tmp);
						}
					}
				}
				curReadNode = curReadNode.getNextSibling();
			}
			return curReadNode;
		}
	}

	/**
	 * Method getPacketCollectionOneNode.
	 * 
	 * @param node
	 * @param curContext
	 * @return PacketCollection
	 */
	private static PacketCollection getPacketCollectionOneNode(Node node,
			Context curContext) {

		curContext.setLinkTag(false);
		curContext.setStartSelect(false);
		curContext.setStringOutput(true);
		switch (node.getNodeType()) {
		case Node.ELEMENT_NODE:
			// String n = node.getNodeName();
			PacketCollection result = getPacketCollectionOneElement(
					(Element) node, curContext);
			return result;

		case Node.TEXT_NODE:
			try {
				String curNodeValue = node.getNodeValue();
				curNodeValue = TextUtil.trim(curNodeValue);
				curNodeValue = TextUtil.replace(new StringBuffer(curNodeValue))
						.toString();
				curNodeValue = TextUtil.substitute(curNodeValue, "&nbsp;", " ");
				if (curNodeValue.indexOf("<!") >= 0
						|| curNodeValue.indexOf("<%") >= 0
						|| curNodeValue.length() == 0) {
					return null;
				} else {
					curContext.setLineDelimiter(false);
					return new PacketCollection(new Packet(node, curNodeValue,
							curContext, true));
				}
			} catch (NullPointerException npe) {
				return null;
			}
		default:
			break;
		}
		return null;
	}

	// renderers for every Tag
	private static Map<String, IElementRenderer> elementMap = new HashMap<String, IElementRenderer>();

	static final int IMG_ELEMENT = 0;

	static final int A_ELEMENT = 1;

	private static IElementRenderer defaultRenderer = new StaticDefaultRenderer();
	static {
		elementMap.put("img", new StaticIMGRenderer());
		elementMap.put("a", new StaticARenderer());
		elementMap.put("td", new StaticTDRenderer());
		elementMap.put("form", new StaticFORMRenderer());
		elementMap.put("select", new StaticSELECTRenderer());
		elementMap.put("input", new StaticINPUTRenderer());
		elementMap.put("button", new StaticBUTTONRenderer());
		elementMap.put("br", new StaticBRRenderer());
		elementMap.put("table", new StaticTABLERenderer());
		elementMap.put("area", new StaticAREARenderer());
		elementMap.put("map", new StaticMAPRenderer());
		elementMap.put("head", new StaticNoRenderer());
		elementMap.put("script", new StaticNoRenderer());
		elementMap.put("noscript", new StaticNoRenderer());
		elementMap.put("tr", new StaticTRRenderer());
		elementMap.put("tbody", new StaticTRRenderer());
		elementMap.put("p", new StaticPRenderer());
		elementMap.put("th", new StaticTDRenderer());
		elementMap.put("div", new StaticTDRenderer());
		elementMap.put("li", new StaticTDRenderer());
		elementMap.put("h1", new StaticHRenderer());
		elementMap.put("h2", new StaticHRenderer());
		elementMap.put("h3", new StaticHRenderer());
		elementMap.put("h4", new StaticHRenderer());
		elementMap.put("h5", new StaticHRenderer());
		elementMap.put("h6", new StaticHRenderer());
		elementMap.put("abbr", new StaticABBRRenderer());
		elementMap.put("acronym", new StaticABBRRenderer());
		elementMap.put("option", new StaticOPTIONRenderer());
		elementMap.put("textarea", new StaticTEXTAREARenderer());
	}

	/**
	 * Method getPacketCollectionOneElement.
	 * 
	 * @param element
	 * @param curContext
	 * @return PacketCollection
	 */
	private static PacketCollection getPacketCollectionOneElement(
			Element element, Context curContext) {
		PacketCollection result = null;
		try {
			result = getElementRenderer(element).getPacketCollectionIn(element,
					curContext, uriPrefix, mc);
			return result;
		} catch (NullPointerException npe) {
			return null;
		}
	}

	/**
	 * Method getElementRenderer.
	 * 
	 * @param element
	 * @return AbstractElementRenderer
	 */
	private static IElementRenderer getElementRenderer(Element element) {
		String nodeName = element.getNodeName().toLowerCase();
		IElementRenderer renderer = null;
		renderer = elementMap.get(nodeName);
		if (renderer == null)
			renderer = defaultRenderer;
		return renderer;
	}

	/**
	 * Method getNodePosition.
	 * 
	 * @param node
	 * @param pc
	 * @return int
	 */
	public int getNodePosition(Node node, PacketCollection pc) {
		if (pc == null)
			return 0;

		try {
			Context curContext = new Context();
			PacketCollection p = new PacketCollection();
			Node curReadNode = node;
			Stack<Node> parentsStack = getCurrentParentsStack(node);
			while (curReadNode != null) {
				PacketCollection tmp = getPacketCollectionOneNode(curReadNode,
						curContext);
				if (tmp != null) {
					p.addAll(tmp);
					break;
				}
				curReadNode = goNext(curReadNode, curContext, parentsStack, p);
			}

			int size = pc.size();
			Node n = p.get(0).getNode();
			for (int i = 0; i < size; i++) {
				if (n == pc.get(i).getNode()) {
					return i;
				}
			}
			return 0;
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			return 0;
		} catch (ClassCastException cce) {
			cce.printStackTrace();
			return 0;
		}
	}

	/**
	 * Method getAnchorPosition.
	 * 
	 * @param anchorName
	 * @param pc
	 * @return int
	 */
	public int getAnchorPosition(String anchorName, Node topNode,
			PacketCollection pc) {
		if (pc == null)
			return 0;

		try {
			int pos = 0;
			Node node = null;
			NodeList aTags = ((Element) topNode).getElementsByTagName("a");
			for (int i = 0; i < aTags.getLength(); i++) {
				node = aTags.item(i);
				NamedNodeMap attrs = node.getAttributes();
				Node typeNode = attrs.getNamedItem("name");
				if (typeNode != null) {
					String name = typeNode.getNodeValue();
					if (anchorName.equals(name)) {
						break;
					}
				}
			}

			if (node != null)
				pos = getNodePosition(node, pc);

			return pos;
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			return 0;
		}
	}

	/**
	 * Method getHrefString.
	 * 
	 * @param node
	 * @return String
	 */
	public String getHrefString(Node node) {
		String href = null;
		if (node.getNodeType() == Node.ELEMENT_NODE
				&& (node.getNodeName().toLowerCase().equals("a") || node
						.getNodeName().toLowerCase().equals("area"))) {
			NamedNodeMap attrs = node.getAttributes();
			Node typeNode = attrs.getNamedItem("href");
			if (typeNode != null) {
				href = typeNode.getNodeValue();
			}
		}
		return href;
	}

	/**
	 * Method getJumpTargetNode.
	 * 
	 * @param node
	 * @return Node
	 */
	public Node getTargetNode(Node node, Node topNode) {
		String href = getHrefString(node);
		if (href != null && href.length() > 0) {
			if (href.charAt(0) == '#') {
				String targetStr = href.substring(1, href.length());
				if (node != null) {
					NodeList aTags = ((Element) topNode)
							.getElementsByTagName("a");
					for (int i = 0; i < aTags.getLength(); i++) {
						Node n = aTags.item(i);
						NamedNodeMap attrs = n.getAttributes();
						Node typeNode = attrs.getNamedItem("name");
						if (typeNode != null) {
							String name = typeNode.getNodeValue();
							if (targetStr.equals(name))
								return n;
						}
					}
				}
				return node;
			}
			return null;
		} else {
			return null;
		}
	}

	/**
	 * Sets the uriPrefix.
	 * 
	 * @param uriPrefix
	 *            The uriPrefix to set
	 */
	public static void setUriPrefix(String uriPrefix) {
		JWATCore.uriPrefix = uriPrefix;
	}

}
