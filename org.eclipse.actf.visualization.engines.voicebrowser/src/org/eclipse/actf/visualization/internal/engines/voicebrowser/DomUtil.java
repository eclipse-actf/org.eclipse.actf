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

import org.eclipse.actf.visualization.engines.voicebrowser.IPacket;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@SuppressWarnings("nls")
public class DomUtil {

	protected static int tab, cnt;
	private static NodeList FormChildren = null;

	/**
	 * @see java.lang.Object#Object()
	 */
	public DomUtil() {
		tab = 0;
		cnt = 0;
		FormChildren = null;
	}

	/**
	 * Method recursiveWalk.
	 * 
	 * @param node
	 * @param num
	 */
	public static void recursiveWalk(Node node, int num) {
		getNodeInfo(node, num);
		tab++;
		cnt = 0;
		for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling(), cnt++) {
			recursiveWalk(child, cnt);
		}
		tab--;
	}

	/**
	 * Method getString.
	 * 
	 * @param node
	 * @return String
	 */
	public static String getString(Node node) {
		String str = null;

		if (node != null)
			str = "[" + node.getNodeName() + "] [" + node.getNodeValue() + "]";
		return str;
	}

	/**
	 * Method getNodeInfo.
	 * 
	 * @param node
	 * @param num
	 */
	private static void getNodeInfo(Node node, int num) {
		tabbing();
		String nodeName = node.getNodeName();
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			NamedNodeMap attList = ((Element) node).getAttributes();
			Attr att;
			if (attList.getLength() > 0) {
				String str = "<" + nodeName + ":" + node.getNodeValue();
				for (int i = 0; i < attList.getLength(); i++) {
					att = (Attr) attList.item(i);
					str += "," + att.getName() + "=" + att.getValue();
				}
				str += ">";
				System.out.println(str);
			} else {
				System.out.println("<" + nodeName + ":" + node.getNodeValue() + ">");
			}
		} else {
			System.out.println("<" + nodeName + ":" + node.getNodeValue() + ">");
		}
	}

	/**
	 * Method tabbing.
	 */
	private static void tabbing() {
		for (int i = 0; i < tab; i++) {
			System.out.print("\t");
		}
	}

	/**
	 * Method isNameAttr.
	 * 
	 * @param node
	 * @return boolean
	 */
	public static boolean isNameAttr(Node node) {
		if (node != null) {
			if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().toLowerCase().equals("a")) {
				NamedNodeMap attrs = node.getAttributes();
				Node altNode = attrs.getNamedItem("name");
				if (altNode != null)
					return true;
			}
		}
		return false;
	}

	/**
	 * Method dumpPC.
	 * 
	 * @param pc
	 */
	public static void dumpPC(PacketCollection pc) {
		if (pc == null)
			return;
		if (pc.size() > 0) {
			for (int i = 0; i < pc.size(); i++) {
				IPacket p = pc.get(i);
				String str = p.getText();
				System.out.println("[" + i + "] <" + str + ">");
			}
		}
	}

	/**
	 * Method setFormList.
	 * 
	 * @param node
	 */
	public static void setFormList(Node node) {
		if (node != null) {
			FormChildren = ((Element) node).getElementsByTagName("form");
		}
	}

	/**
	 * Method getFormNum.
	 * 
	 * @param element
	 * @return int
	 */
	public static int getFormNum(Element element) {
		if (FormChildren != null) {
			if (FormChildren.getLength() > 0) {
				for (int i = 0; i < FormChildren.getLength(); i++) {
					if (element == (Element) FormChildren.item(i))
						return i + 1;
				}
			}
		}
		return 0;
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
	
	
	
	public static boolean isDisabled(Element target){
		if(target.hasAttribute("disabled")){
			String disabled = target.getAttribute("disabled");
			if (!"false".equals(disabled)) {
				return true;
			}			
		}
		Node tmpN = DomUtil.getAncestor(target, "fieldset");
		if (null != tmpN && tmpN instanceof Element) {
			Element fieldset = (Element) tmpN;
			if (fieldset.hasAttribute("disabled")) {
				String disabled = ((Element) fieldset).getAttribute("disabled");
				if (!"false".equals(disabled)) {
					return true;
				}
			}
		}		
		return false;
	}

}
