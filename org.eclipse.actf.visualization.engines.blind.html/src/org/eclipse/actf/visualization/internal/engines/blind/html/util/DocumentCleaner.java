/*******************************************************************************
 * Copyright (c) 2004, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.engines.blind.html.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;

@SuppressWarnings("nls")
public class DocumentCleaner {

	// separated from VisualizeEngine

	public static Set<String> removeCSS(Document result, String baseUrl) {
		HashSet<String> cssSet = new HashSet<String>();

		URL baseURL = null;
		try {
			baseURL = new URL(baseUrl);
		} catch (Exception e1) {
			// e1.printStackTrace();
			// System.out.println("DocCleaner: create base URL: " + baseUrl);
		}

		// Remove CSS
		NodeList linkEl = result.getElementsByTagName("link");
		if (linkEl != null) {
			int xy = linkEl.getLength();
			for (int i = xy - 1; i >= 0; i--) {
				Element x = (Element) linkEl.item(i);
				if ("text/css".equals(x.getAttribute("type")) || "stylesheet".equals(x.getAttribute("rel"))) {
					String hrefS = getHref(x, baseURL);
					if (!hrefS.equals(""))
						cssSet.add(hrefS);
					x.getParentNode().removeChild(x);
				}
			}
		}

		NodeList styleEl = result.getElementsByTagName("style");
		if (styleEl != null) {
			int xy = styleEl.getLength();
			for (int i = xy - 1; i >= 0; i--) {
				Element x = (Element) styleEl.item(i);
				if (x.getAttribute("type").equals("text/css")) {
					NodeList tmpNL = x.getChildNodes();
					for (int j = 0; j < tmpNL.getLength(); j++) {
						Node tmpN = tmpNL.item(j);
						getImportUrl(tmpN.toString(), baseURL, cssSet);
					}
				}
				x.getParentNode().removeChild(x);
			}
		}
		NodeList bodyNl = result.getElementsByTagName("body");
		for (int i = 0; i < bodyNl.getLength(); i++) {
			Element bodyEl = (Element) bodyNl.item(i);
			bodyEl.removeAttribute("style");
		}
		return (cssSet);
	}

	private static String createFullPath(URL baseURL, String spec) {
		String result = spec;
		if (baseURL != null) {
			try {
				URL targetUrl = new URL(baseURL, spec);
				// System.out.println(targetUrl.toString());
				result = targetUrl.toString();
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
		return (result);
	}

	private static String getHref(Element target, URL baseURL) {
		String result = "";
		if (target.hasAttribute("href")) {
			result = target.getAttribute("href");
			result = createFullPath(baseURL, result);
		}
		return (result);
	}

	private static void getImportUrl(String target, URL baseURL, Set<String> cssSet) {
		BufferedReader tmpBR = new BufferedReader(new StringReader(target));
		String tmpS;
		int index;
		try {
			while ((tmpS = tmpBR.readLine()) != null) {
				if ((index = tmpS.indexOf("@import")) > -1) {
					tmpS = tmpS.substring(index + 7);
					// TODO
					if ((index = tmpS.indexOf("url(\"")) > -1) {
						tmpS = tmpS.substring(index + 5);
						if ((index = tmpS.indexOf("\");")) > -1) {
							tmpS = tmpS.substring(0, index);
							tmpS = createFullPath(baseURL, tmpS);
							cssSet.add(tmpS);
						}
					}
				}
			}
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

	public static void removeBase(Document result) {
		NodeList objectNl = result.getElementsByTagName("base");
		int size = objectNl.getLength();
		for (int i = size - 1; i >= 0; i--) {
			Element objectEl = (Element) objectNl.item(i);
			objectEl.getParentNode().removeChild(objectEl);
		}
	}

	public static void removeApplet(Document result) {
		NodeList appletNl = result.getElementsByTagName("applet");
		int size = appletNl.getLength();
		for (int i = size - 1; i >= 0; i--) {
			Element appletEl = (Element) appletNl.item(i);
			String id = appletEl.getAttribute("id");
			Element divEl = result.createElement("div");
			divEl.setAttribute("id", id);
			// remove param
			Node curChild = appletEl.getLastChild();
			while (curChild != null) {
				Node next = curChild.getPreviousSibling();
				if (!curChild.getNodeName().equals("param")) {
					if (divEl.hasChildNodes()) {
						divEl.insertBefore(curChild, divEl.getFirstChild());
					} else {
						divEl.appendChild(curChild);
					}
				}
				curChild = next;
			}
			appletEl.getParentNode().insertBefore(divEl, appletEl);
			appletEl.getParentNode().removeChild(appletEl);
		}
	}

	public static void removeObject(Document result) {
		NodeList objectNl = result.getElementsByTagName("object");
		int size = objectNl.getLength();
		for (int i = size - 1; i >= 0; i--) {
			Element objectEl = (Element) objectNl.item(i);
			String id = objectEl.getAttribute("id");
			Element divEl = result.createElement("div");
			divEl.setAttribute("id", id);
			// remove param

			Node curChild = objectEl.getLastChild();
			while (curChild != null) {
				Node next = curChild.getPreviousSibling();
				if (!curChild.getNodeName().equals("param")) {
					if (divEl.hasChildNodes()) {
						divEl.insertBefore(curChild, divEl.getFirstChild());
					} else {
						divEl.appendChild(curChild);
					}
				}
				curChild = next;
			}

			objectEl.getParentNode().insertBefore(divEl, objectEl);
			objectEl.getParentNode().removeChild(objectEl);
		}
	}

	public static void removeEmbed(Document doc) { // remove onload and
		// onmouseover
		NodeList nl = doc.getElementsByTagName("embed");
		int size = nl.getLength();
		for (int i = size - 1; i >= 0; i--) {
			Element el = (Element) nl.item(i);
			String id = el.getAttribute("id");
			Element newDiv = doc.createElement("div");
			newDiv.setAttribute("id", id);

			Node parent = el.getParentNode();

			NodeList noembedL = el.getElementsByTagName("noembed");
			Node childNoembed = null;
			boolean hasNoembed = false;
			for (int j = 0; j < noembedL.getLength(); j++) {
				// System.out.println(j + "/" + noembedL.getLength() + " " +
				// hasNoembed);
				childNoembed = noembedL.item(j);
				if (el == childNoembed.getParentNode()) {
					hasNoembed = true;
					break;
				}
			}

			Node startNode;

			if (hasNoembed) {
				NodeList tmpNL = childNoembed.getChildNodes();
				for (int k = tmpNL.getLength() - 1; k >= 0; k--) {
					newDiv.insertBefore(tmpNL.item(k), newDiv.getFirstChild());
				}
				startNode = childNoembed.getNextSibling();
			} else {
				newDiv.appendChild(doc.createTextNode("(Embeded element)"));
				startNode = el.getFirstChild();
			}

			parent.insertBefore(newDiv, el);
			while (startNode != null) {
				Node next = startNode.getNextSibling();
				parent.insertBefore(startNode, el);
				startNode = next;
			}
			parent.removeChild(el);
		}
	}

	public static void removeOnMouse(Element element) {
		String str = element.getAttribute("onmouseover");
		if ((str != null) && (str.length() > 0)) {
			element.removeAttribute("onmouseover");
		}
		str = element.getAttribute("onmouseout");
		if ((str != null) && (str.length() > 0)) {
			element.removeAttribute("onmouseout");
		}
		str = element.getAttribute("onmousemove");
		if ((str != null) && (str.length() > 0)) {
			element.removeAttribute("onmousemove");
		}
		str = element.getAttribute("onclick");
		if ((str != null) && (str.length() > 0)) {
			element.removeAttribute("onclick");
		}
		str = element.getAttribute("onresize");
		if ((str != null) && (str.length() > 0)) {
			element.removeAttribute("onresize");
		}

	}

	public static void removeOnLoad(Element element) {
		String str = element.getAttribute("onload");
		if ((str != null) && (str.length() > 0)) {
			element.removeAttribute("onload");
		}
	}

	public static void removeDisplayNone(Document doc) {
		NodeList tmpNL = doc.getElementsByTagName("div");
		for (int i = tmpNL.getLength() - 1; i >= 0; i--) {
			Element tmpE = (Element) tmpNL.item(i);
			// System.out.println(tmpE.getAttribute("style"));
			if (tmpE.getAttribute("style").indexOf("DISPLAY: none") > -1) {
				Node parent = tmpE.getParentNode();
				if (parent != null) {
					// System.out.println("remove display: none");
					parent.removeChild(tmpE);
				}
			}
		}
	}

	public static void removeBgcolor(Document doc) {
		NodeList bodyNl = doc.getElementsByTagName("body");

		if (bodyNl.getLength() > 0) {

			Element bodyEl = (Element) bodyNl.item(0);
			bodyEl.removeAttribute("bgcolor");
		}
	}

	public static void removeMeta(Document doc) {
		NodeList nl = doc.getElementsByTagName("meta");
		int size = nl.getLength();
		for (int i = 0; i < size; i++) {
			Element el = (Element) nl.item(i); // item(i)?
			String http_equiv = el.getAttribute("http-equiv");
			if ((http_equiv != null)
					&& (http_equiv.equalsIgnoreCase("refresh") || http_equiv.equalsIgnoreCase("Content-Type"))) {
				el.removeAttribute("http-equiv");
			}
		}
	}

	public static void removeJavaScript(List<VisualizationNodeInfo> nodeList, Document doc) {
		/*
		 * remove onload and onmouseover
		 */
		NodeList nl = doc.getElementsByTagName("script");
		int size = nl.getLength();
		for (int i = size - 1; i >= 0; i--) {
			try {
				Element el = (Element) nl.item(i);
				// if (el.getParentNode().getNodeName().equals("head")) {
				el.getParentNode().removeChild(el);
			} catch (Exception e) {
				//
			}
		}

		// Remove rel as="script"
		NodeList linkEl = doc.getElementsByTagName("link");
		if (linkEl != null) {
			int xy = linkEl.getLength();
			for (int i = xy - 1; i >= 0; i--) {
				Element x = (Element) linkEl.item(i);
				if ("script".equals(x.getAttribute("as"))) {
					x.getParentNode().removeChild(x);
				}
			}
		}

		nl = doc.getElementsByTagName("body");
		size = nl.getLength();
		for (int i = 0; i < size; i++) {
			try {
				((Element) nl.item(i)).removeAttribute("onload");
				// Added on 2004/03/03
				((Element) nl.item(i)).removeAttribute("onunload");
			} catch (Exception e) {
				//
			}
		}
		size = nodeList.size();
		for (int i = 0; i < size; i++) {
			Node node = (nodeList.get(i)).getNode();
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				try {
					Element el = (Element) node;
					String onload = el.getAttribute("onload");
					if (onload.length() > 0) {
						el.removeAttribute("onload");
					}
					String onmouseover = el.getAttribute("onmouseover");
					if (onmouseover.length() > 0) {
						el.removeAttribute("onmouseover");
					}
				} catch (Exception e) {
					//
				}
			}

		}
	}

	public static void removePI(Document doc) {
		NodeList nl = doc.getChildNodes();
		for (int i = nl.getLength() - 1; i > -1; i--) {
			Node n = nl.item(i);
			if (n instanceof ProcessingInstruction) {
				doc.removeChild(n);
			}
		}
	}

}
