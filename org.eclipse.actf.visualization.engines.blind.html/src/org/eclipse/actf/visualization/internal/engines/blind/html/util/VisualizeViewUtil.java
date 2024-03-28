/*******************************************************************************
 * Copyright (c) 2004, 2024 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.engines.blind.html.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.actf.util.logging.DebugPrintUtil;
import org.eclipse.actf.util.xpath.XPathService;
import org.eclipse.actf.util.xpath.XPathServiceFactory;
import org.eclipse.actf.visualization.engines.blind.BlindVizResourceUtil;
import org.eclipse.actf.visualization.engines.blind.ParamBlind;
import org.eclipse.actf.visualization.engines.blind.html.IBlindProblem;
import org.eclipse.actf.visualization.engines.blind.html.VisualizeEngine;
import org.eclipse.actf.visualization.engines.voicebrowser.IPacket;
import org.eclipse.actf.visualization.engines.voicebrowser.IPacketCollection;
import org.eclipse.actf.visualization.eval.html.HtmlTagUtil;
import org.eclipse.actf.visualization.eval.problem.HighlightTargetId;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.internal.engines.blind.html.BlindProblem;
import org.eclipse.swt.graphics.RGB;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VisualizeViewUtil {

	// separated from VisualizeEngine

	private static final XPathService xpathService = XPathServiceFactory.newService();

	private static final String SCRIPT = "script"; //$NON-NLS-1$
	private static final String NAME = "name"; //$NON-NLS-1$
	private static final String TITLE = "title"; //$NON-NLS-1$
	private static final String HREF = "href"; //$NON-NLS-1$
	private static final String IMG = "img"; //$NON-NLS-1$
	private static final String NULL_STRING = ""; //$NON-NLS-1$
	private static final String BODY = "body"; //$NON-NLS-1$
	private static final String STYLE = "style"; //$NON-NLS-1$
	private static final String ID = "id"; //$NON-NLS-1$
	private static final String DIV = "div"; //$NON-NLS-1$
	private static final String INPUT = "input"; //$NON-NLS-1$
	private static final String IMAGE = "image"; //$NON-NLS-1$
	private static final String TYPE = "type"; //$NON-NLS-1$
	private static final String ON_CLICK = "onClick"; //$NON-NLS-1$
	private static final String ALT = "alt"; //$NON-NLS-1$
	private static final String SRC = "src"; //$NON-NLS-1$

	@SuppressWarnings("nls")
	public static File prepareActions(Document result, VisualizeMapDataImpl mapData, String baseUrl,
			boolean servletMode) {

		Map<Node, Node> linkMap = mapData.getIntraPageLinkMap();
		List<VisualizationNodeInfo> targetElementList = mapData.getNodeInfoList();

		NodeList bodyEl = result.getElementsByTagName(BODY);

		for (int i = 0; i < bodyEl.getLength(); i++) {
			Element tmpE = (Element) bodyEl.item(i);
			DocumentCleaner.removeOnMouse(tmpE);
			DocumentCleaner.removeOnLoad(tmpE);
		}

		// TODO move to appropriate place
		// to handle no body page
		if (bodyEl.getLength() == 0) {
			Node tmpN = result.getDocumentElement();
			if (tmpN != null) {
				tmpN.appendChild(result.createElement(BODY));
			} else {
				DebugPrintUtil.devOrDebugPrintln("VisualizeViewUtil: no doc element");
				// TODO test
				return null;
			}
		}

		// div for arrow
		Element div = result.createElement(DIV);
		div.setAttribute(STYLE, "position:absolute;pixelLeft= 10;pixelTop=10; color:red;font-size=12pt");
		div.setAttribute(ID, "test");

		// bodyEl.item(bodyEl.getLength() - 1).appendChild(div);
		Node tmpBody = bodyEl.item(0);
		tmpBody.insertBefore(div, tmpBody.getFirstChild());

		insertLinkIcon(result, linkMap, baseUrl);

		// div for control_pane
		insertControlPane(result);

		// remove links of "map"

		bodyEl = result.getElementsByTagName("map");
		if (bodyEl != null) {
			for (int i = 0; i < bodyEl.getLength(); i++) {
				Element x = (Element) bodyEl.item(i);
				x.setAttribute(ON_CLICK, "cancelMapLink(event)");
			}
		}

		return (createScriptFile(result, targetElementList, baseUrl, servletMode));

	}

	@SuppressWarnings("nls")
	private static void insertLinkIcon(Document doc, Map<Node, Node> linkMap, String baseUrl) {
		Iterator<Node> it = linkMap.keySet().iterator();
		Set<String> alreadySet = new HashSet<String>();
		int id = 0;
		while (it.hasNext()) {
			Element lel = (Element) it.next();
			Element ael = (Element) linkMap.get(lel);

			Element imgel1 = doc.createElement(IMG);
			String href = lel.getAttribute(HREF).substring(1);
			imgel1.setAttribute(ALT, "Intra-page link: " + href);
			imgel1.setAttribute(TITLE, "Intra-page link: " + href);
			imgel1.setAttribute(SRC, baseUrl + "img/jump.gif");
			imgel1.setAttribute(NAME, "jump" + id);

			if (lel.hasChildNodes()) {
				lel.insertBefore(imgel1, lel.getFirstChild());
			} else {
				lel.appendChild(imgel1);
			}

			if (!alreadySet.contains(href)) {

				Element imgel2 = doc.createElement(IMG);
				imgel2.setAttribute(ALT, "Intra-page link destination: " + href);
				imgel2.setAttribute(TITLE, "Intra-page link destination: " + href);
				imgel2.setAttribute(SRC, baseUrl + "img/dest.gif");
				imgel2.setAttribute(NAME, href);

				if (ael.hasChildNodes()) {
					ael.insertBefore(imgel2, ael.getFirstChild());
				} else {
					ael.appendChild(imgel2);
				}
				alreadySet.add(href);
			}
			id++;
		}
	}

	private static void addVisualizeImg(Document doc, Element target, String iconPath, String message) {
		Element imgEl = doc.createElement(IMG);
		imgEl.setAttribute(ALT, message);
		// imgel1.setAttribute(TITLE, name);
		imgEl.setAttribute(SRC, iconPath);

		if (target.getTagName().matches("input|textarea|select")) {
			target.getParentNode().insertBefore(imgEl, target);
		} else if (target.hasChildNodes()) {
			target.insertBefore(imgEl, target.getFirstChild());
		} else {
			target.appendChild(imgEl);
		}
	}

	private static void addLandmarkImg(Document doc, Element target, String baseUrl) {
		Element imgel1 = doc.createElement(IMG);
		String name = target.getTagName().toLowerCase();
		imgel1.setAttribute(ALT, "Landmark: " + name);
		// imgel1.setAttribute(TITLE, name);
		imgel1.setAttribute(SRC, baseUrl + "img/" + name + ".gif");

		if (target.hasChildNodes()) {
			target.insertBefore(imgel1, target.getFirstChild());
		} else {
			target.appendChild(imgel1);
		}
	}

	private static void addLandmarkImg(Document doc, Element target, String baseUrl, String landmarkName) {
		Element imgel1 = doc.createElement(IMG);
		imgel1.setAttribute(ALT, "Landmark: " + landmarkName);
		// imgel1.setAttribute(TITLE, name);
		imgel1.setAttribute(SRC, baseUrl + "img/" + landmarkName + ".gif");

		if (target.hasChildNodes()) {
			target.insertBefore(imgel1, target.getFirstChild());
		} else {
			target.appendChild(imgel1);
		}
	}

	public static void visualizeLandmark(Document doc, String baseUrl) {
		// String[] landmarks = { "nav", "article", "aside" };

		// NodeList tmpNl = doc.getElementsByTagName("main");
		NodeList tmpNl = xpathService.evalPathForNodeList("//main|//*[@role='main']", doc);
		for (int i = 0; i < tmpNl.getLength(); i++) {
			Element landE = (Element) tmpNl.item(i);
			addLandmarkImg(doc, landE, baseUrl, "main");

			try {
				Element parentE = (Element) landE.getParentNode();
				Element divE = doc.createElement("div");
				divE.setAttribute("class", "main");
				parentE.insertBefore(divE, landE);
				divE.appendChild(landE);
			} catch (Exception e) {
			}
		}

		tmpNl = xpathService.evalPathForNodeList("//article|//*[@role='article']", doc);
		for (int i = 0; i < tmpNl.getLength(); i++) {
			addLandmarkImg(doc, (Element) tmpNl.item(i), baseUrl, "article");
		}

		tmpNl = xpathService.evalPathForNodeList("//nav|//*[@role='navigation']", doc);
		for (int i = 0; i < tmpNl.getLength(); i++) {
			addLandmarkImg(doc, (Element) tmpNl.item(i), baseUrl, "nav");
		}

		tmpNl = doc.getElementsByTagName("aside");
		for (int i = 0; i < tmpNl.getLength(); i++) {
			addLandmarkImg(doc, (Element) tmpNl.item(i), baseUrl);
		}

		tmpNl = xpathService.evalPathForNodeList("//*[@role='complementary']", doc);
		for (int i = 0; i < tmpNl.getLength(); i++) {
			Element landE = (Element) tmpNl.item(i);
			if (!"aside".equalsIgnoreCase(landE.getNodeName())) {
				addLandmarkImg(doc, landE, baseUrl, "complementary");
			}
		}

		for (String tag : new String[] { "header", "footer" }) {
			tmpNl = doc.getElementsByTagName(tag);
			for (int i = 0; i < tmpNl.getLength(); i++) {
				Element landE = (Element) tmpNl.item(i);
				if (!HtmlTagUtil.hasAncestor(landE, "article") && !HtmlTagUtil.hasAncestor(landE, "section")) {
					landE.setAttribute("class", "header_footer");
					addLandmarkImg(doc, landE, baseUrl);
				}
			}
		}

		tmpNl = xpathService.evalPathForNodeList("//*[@role='banner']", doc);
		for (int i = 0; i < tmpNl.getLength(); i++) {
			Element landE = (Element) tmpNl.item(i);
			if (!"header".equalsIgnoreCase(landE.getNodeName())) {
				addLandmarkImg(doc, landE, baseUrl, "banner");
			}
		}

		tmpNl = xpathService.evalPathForNodeList("//*[@role='contentinfo']", doc);
		for (int i = 0; i < tmpNl.getLength(); i++) {
			Element landE = (Element) tmpNl.item(i);
			if (!"footer".equalsIgnoreCase(landE.getNodeName())) {
				addLandmarkImg(doc, landE, baseUrl, "contentinfo");
			}
		}

		tmpNl = xpathService.evalPathForNodeList("//*[@role='application']", doc);
		for (int i = 0; i < tmpNl.getLength(); i++) {
			addLandmarkImg(doc, (Element) tmpNl.item(i), baseUrl, "application");
		}

		tmpNl = xpathService.evalPathForNodeList("//*[@role='search']", doc);
		for (int i = 0; i < tmpNl.getLength(); i++) {
			addLandmarkImg(doc, (Element) tmpNl.item(i), baseUrl, "search");
		}

		NodeList nl = doc.getElementsByTagName("head");
		if (nl.getLength() > 0) {
			Element el = (Element) nl.item(0);

			Element cssE = doc.createElement("link");
			cssE.setAttribute("rel", "stylesheet");
			cssE.setAttribute("type", "text/css");
			cssE.setAttribute("href", "img/visualization.css");
			cssE.setAttribute("media", "all");
			el.appendChild(cssE);
		}

	}

	@SuppressWarnings("nls")
	// TODO
	private static void insertControlPane(Document result) {
		NodeList bodyEl = result.getElementsByTagName(BODY);
		Element div = result.createElement(DIV);
		div.setAttribute(STYLE,
				"position:absolute;font-size: medium; background-color: #FFFFFF; border-color: #333333 #000000 #000000; padding-top: 5px; padding-right: 5px; padding-bottom: 5px; padding-left: 5px; border-style: solid; border-top-width: 1px; border-right-width: 1px; border-bottom-width: 1px; border-left-width: 1px");
		div.setAttribute(ID, "control_pane");

		Element input1 = result.createElement(INPUT);
		input1.setAttribute(TYPE, IMAGE);
		input1.setAttribute(SRC, "img/stop.gif");
		input1.setAttribute(ALT, "Stop/Move Balloon");
		input1.setAttribute(ON_CLICK, "control_moving()");
		div.appendChild(input1);

		Element input2 = result.createElement(INPUT);
		input2.setAttribute(TYPE, IMAGE);
		input2.setAttribute(SRC, "img/clear.gif");
		input2.setAttribute(ALT, "Clear Line");
		input2.setAttribute(ON_CLICK, "clean_Line()");

		Element input3 = result.createElement(INPUT);
		input3.setAttribute(TYPE, IMAGE);
		input3.setAttribute(SRC, "img/refresh.gif");
		input3.setAttribute(ALT, "Refresh Line");
		input3.setAttribute(ON_CLICK, "refresh_Jump()");

		Element input4 = result.createElement(INPUT);
		input4.setAttribute(TYPE, IMAGE);
		input4.setAttribute(SRC, "img/draw.gif");
		input4.setAttribute(ALT, "Draw All Line");
		input4.setAttribute(ON_CLICK, "draw_all_Line()");

		div.appendChild(input2);
		div.appendChild(input3);
		div.appendChild(input4);
		// bodyEl.item(bodyEl.getLength() - 1).appendChild(div);
		Node tmpBody = bodyEl.item(0);
		tmpBody.insertBefore(div, tmpBody.getFirstChild());
	}

	@SuppressWarnings("nls")
	private static File createScriptFile(Document result, List<VisualizationNodeInfo> elementList, String baseUrl,
			boolean servletMode) {
		try {
			PrintWriter pw;

			File valiantFile = BlindVizResourceUtil.createTempFile("variant", ".js");
			pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(valiantFile), "UTF-8"));

			StringBuffer sb = new StringBuffer();
			sb.append("var id2time = new Array();");
			sb.append("var id2comment = new Array();");

			int size = elementList.size();
			for (int i = 0; i < size; i++) {
				VisualizationNodeInfo curInfo = elementList.get(i);

				//
				String comment = curInfo.getComment();
				StringBuffer comment_sb = new StringBuffer();
				// if (871 == curInfo.getId()) {
				for (int x = 0; x < comment.length(); x++) {
					if (comment.charAt(x) == '\"') {
						comment_sb.append("\\");
					}
					if (comment.charAt(x) == '\'') {
						comment_sb.append('\\');
					}

					comment_sb.append(comment.charAt(x));
				}

				sb.append("id2time['id");
				sb.append(curInfo.getId());
				sb.append("']=");
				sb.append(curInfo.getTime());
				sb.append(";");

				sb.append("id2comment['id");
				sb.append(curInfo.getId());
				sb.append("']='");
				sb.append(comment_sb.toString());
				sb.append("';");

			}

			String tmpS = sb.toString().replaceAll("\n", NULL_STRING).replaceAll("\r", NULL_STRING);
			pw.write(tmpS);

			sb = new StringBuffer();
			sb.append("var baloonSwitch = 1; ");
			sb.append("var baseUrl = '" + baseUrl + "'; ");
			sb.append("var acc_imageDir = 'img/'; ");
			if (servletMode) {
				sb.append("var servletMode = true; ");
			} else {
				sb.append("var servletMode = false; ");
			}

			// speed up -> sb.append("var isAlert = false; ");

			sb.append("var isAlert = true; ");

			pw.write(sb.toString());

			pw.flush();
			pw.close();

			NodeList nl = result.getElementsByTagName("head");
			if (nl.getLength() > 0) {
				Element el = (Element) nl.item(0);
				Element script = result.createElement(SCRIPT);
				script.setAttribute(SRC, baseUrl + valiantFile.getName());
				el.appendChild(script);

				Element script2 = result.createElement(SCRIPT);
				// script.setAttribute("src", "file:///C:/C/highlight.js");
				script2.setAttribute(SRC, baseUrl + "img/highlight_moz.js");
				el.appendChild(script2);
			}

			Element div = result.createElement(DIV);
			div.setAttribute(STYLE,
					"position:absolute;font-size: medium; background-color: #FFFFFF; border-color: #333333 #000000 #000000; padding-top: 5px; padding-right: 5px; padding-bottom: 5px; padding-left: 5px; border-style: solid; border-top-width: 1px; border-right-width: 1px; border-bottom-width: 1px; border-left-width: 1px");
			div.setAttribute(ID, "balloon");

			Element messageDiv = result.createElement(DIV);
			messageDiv.setAttribute(ID, "message");
			messageDiv.appendChild(result.createTextNode(NULL_STRING));
			div.appendChild(messageDiv);

			NodeList bodyNl = result.getElementsByTagName(BODY);
			if (bodyNl.getLength() > 0) {
				Element bodyEl = (Element) bodyNl.item(0);
				bodyEl.insertBefore(div, bodyEl.getFirstChild());
			}
			return valiantFile;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void visualizeARIA(Document doc, VisualizeMapDataImpl mapData, String baseUrl, ParamBlind param) {

		NodeList tmpNl = xpathService.evalPathForNodeList("//*[@role='alertdialog']", doc);
		for (int i = 0; i < tmpNl.getLength(); i++) {
			addVisualizeImg(doc, (Element) tmpNl.item(i), baseUrl + "img/alert.png", "role: alertdialog");
			VisualizationNodeInfo info = mapData.getNodeInfo(tmpNl.item(i));
			if (null != info) {
				info.appendComment("role=\"alertdialog\"");
			}
		}

		tmpNl = xpathService.evalPathForNodeList("//*[@role='alert']", doc);
		for (int i = 0; i < tmpNl.getLength(); i++) {
			addVisualizeImg(doc, (Element) tmpNl.item(i), baseUrl + "img/alert.png", "role: alert");
			VisualizationNodeInfo info = mapData.getNodeInfo(tmpNl.item(i));
			if (null != info) {
				info.appendComment("role=\"alert\"");
			}
		}

		tmpNl = xpathService.evalPathForNodeList("//*[@aria-live='assertive']", doc);
		for (int i = 0; i < tmpNl.getLength(); i++) {
			addVisualizeImg(doc, (Element) tmpNl.item(i), baseUrl + "img/alert.png", "aria-live: assertive");
			VisualizationNodeInfo info = mapData.getNodeInfo(tmpNl.item(i));
			if (null != info) {
				info.appendComment("aria-live=\"assertive\"");
			}
		}

		tmpNl = xpathService.evalPathForNodeList("//*[@aria-invalid='true']", doc);
		for (int i = 0; i < tmpNl.getLength(); i++) {
			addVisualizeImg(doc, (Element) tmpNl.item(i), baseUrl + "img/alert.png", "aria-invalid: true");
			VisualizationNodeInfo info = mapData.getNodeInfo(tmpNl.item(i));
			if (null != info) {
				info.appendComment("aria-invalid=\"true\"");
			}
		}

		tmpNl = xpathService.evalPathForNodeList("//*[@aria-required='true']", doc);
		for (int i = 0; i < tmpNl.getLength(); i++) {
			addVisualizeImg(doc, (Element) tmpNl.item(i), baseUrl + "img/required.png", "aria-required: true");
			VisualizationNodeInfo info = mapData.getNodeInfo(tmpNl.item(i));
			if (null != info) {
				info.appendComment("aria-reqiured=\"true\"");
			}
		}

		tmpNl = xpathService.evalPathForNodeList("//*[@role='img']", doc);
		for (int i = 0; i < tmpNl.getLength(); i++) {
			addVisualizeImg(doc, (Element) tmpNl.item(i), baseUrl + "img/img.png", "role: img");
			VisualizationNodeInfo info = mapData.getNodeInfo(tmpNl.item(i));
			if (null != info) {
				info.appendComment("role=\"img\"");
			}
		}

		int r = param.labelTagsColor.red + 80;
		int g = param.labelTagsColor.green + 80;
		int b = param.labelTagsColor.blue + 80;
		RGB rgb = new RGB(r > 255 ? 255 : r, g > 255 ? 255 : g, b > 255 ? 255 : b);
		String ariaLabelRGB = "rgb(" + rgb.red + "," + rgb.green + "," + rgb.blue + ")";
		rgb = param.captionColor;
		String captionRGB = "rgb(" + rgb.red + "," + rgb.green + "," + rgb.blue + ")";

		tmpNl = xpathService.evalPathForNodeList("//*[@aria-label]", doc);
		// org "//input[@aria-label]|//textarea[@aria-label]|//select[@aria-label]"
		// TBD //button[@aria-labelledby]|

		for (int i = 0; i < tmpNl.getLength(); i++) {
			Element ariaLabelElement = (Element) tmpNl.item(i);
			String tagName = ariaLabelElement.getTagName();
			if ("img".equals(tagName)) {
				// already visualized
				continue;
			}

			String target = ariaLabelElement.getAttribute("aria-label");
			VisualizationNodeInfo visInfo = mapData.getNodeInfo(ariaLabelElement);
			if (visInfo != null) {
				visInfo.appendComment("aria-label: " + target);
			}

			if (tagName.matches("input|textarea|select")) {
				Element tmpDiv = doc.createElement("div");
				tmpDiv.setAttribute("id", ariaLabelElement.getAttribute("id"));
				ariaLabelElement.removeAttribute("id");
				
				addVisualizeImg(doc, tmpDiv, baseUrl + "img/aria-label.png", "aria-label");
				Element tmpSpan = doc.createElement("span");
				tmpSpan.appendChild(doc.createTextNode(target));
				tmpSpan.setAttribute("style", "color: black; background-image: none; background-color:" + ariaLabelRGB);
				tmpDiv.appendChild(tmpSpan);

				ariaLabelElement.getParentNode().insertBefore(tmpDiv, ariaLabelElement);
				if (tagName.equals("input") && ariaLabelElement.getAttribute("type").matches("radio|checkbox")) {
					tmpDiv.insertBefore(ariaLabelElement, tmpDiv.getFirstChild());
				} else {
					tmpDiv.appendChild(ariaLabelElement);
				}
			}

			if (tagName.equals("table")) {
				Element tmpDiv = doc.createElement("div");
				addVisualizeImg(doc, tmpDiv, baseUrl + "img/aria-label.png", "aria-label");
				Element tmpSpan = doc.createElement("span");
				tmpSpan.appendChild(doc.createTextNode(target));
				tmpSpan.setAttribute("style", "color: black; background-image: none; background-color:" + captionRGB);
				tmpDiv.appendChild(tmpSpan);

				ariaLabelElement.getParentNode().insertBefore(tmpDiv, ariaLabelElement);
				tmpDiv.insertBefore(ariaLabelElement, tmpDiv.getFirstChild());
				tmpDiv.appendChild(ariaLabelElement);
			}

		}

	}

	public static void visualizeError(Document doc, List<IProblemItem> problems, VisualizeMapDataImpl mapData,
			String baseUrlS) {
		int size = problems.size();

		// TODO setNodeId might be duplicated
		for (int i = 0; i < size; i++) {
			BlindProblem prob = (BlindProblem) problems.get(i);
			Node node = prob.getTargetNodeInResultDoc();
			Integer idObj = mapData.getIdOfNode(node);

			int subType = prob.getSubType();
			switch (subType) {
			case IBlindProblem.WRONG_ALT_AREA:
			case IBlindProblem.NO_ALT_AREA:
			case IBlindProblem.TOO_LESS_STRUCTURE:
			case IBlindProblem.NO_SKIPTOMAIN_LINK:
				break;
			default:
				if (idObj != null) {
					int id = idObj.intValue();
					prob.setNodeId(id);
				}
			}

			VisualizationNodeInfo info = mapData.getNodeInfo(node);
			if (info != null) {
				if (IBlindProblem.WRONG_NBSP_ALT_IMG == prob.getSubType()) {
					info.appendComment(prob.getDescription().replaceAll("&", "&amp;"));
				} else {
					info.appendComment(prob.getDescription());
				}
			} else {
				if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
					try {
						String id = ((Element) node).getAttribute(ID);
						if (id.startsWith(ID)) {
							id = id.substring(2);
							// info =

						}
					} catch (Exception e) {
						//
					}
				}
			}

			switch (prob.getSubType()) {
			case IBlindProblem.NO_ALT_IMG:
			case IBlindProblem.WRONG_ALT_IMG:
			case IBlindProblem.WRONG_NBSP_ALT_IMG:
				Element el = (Element) node;
				Node replacement = mapData.getReplacement(el);
				if (replacement != null) {
					el = (Element) replacement;
				}
				Element img = createErrorImageElement(node, prob, idObj, baseUrlS);
				el.appendChild(img);
				break;
			case IBlindProblem.REDUNDANT_ALT:
			case IBlindProblem.ALERT_REDUNDANT_TEXT:
				int startId = -1;
				int endId = -1;
				try {
					List<Node> nl = prob.getNodeList();
					node = nl.get(1);
					Integer endNodeId = mapData.getIdOfNode(node);
					if (endNodeId != null) {
						endId = endNodeId.intValue();
					}
				} catch (NullPointerException npe) {
					npe.printStackTrace();
				}

				if (idObj != null) {
					startId = idObj.intValue();
				} else if (endId > -1) {
					startId = endId;
				}

				prob.setNodeId(startId);

				prob.addNodeIds(new HighlightTargetId(startId, startId));
				prob.addNodeIds(new HighlightTargetId(endId, endId));

				break;
			case IBlindProblem.NO_DEST_LINK:
			case IBlindProblem.NO_TEXT_INTRAPAGELINK:
			case IBlindProblem.NO_DEST_SKIP_LINK:
			case IBlindProblem.WRONG_SKIP_LINK_TEXT:
			case IBlindProblem.TOOFAR_SKIPTOMAIN_LINK:
			case IBlindProblem.NO_TEXT_WITH_TITLE_INTRAPAGELINK:
			case IBlindProblem.INVISIBLE_INTRAPAGE_LINK:
			case IBlindProblem.WRONG_SKIP_LINK_TITLE:
				Element element = (Element) node;
				Element image = createErrorImageElement(element, prob, idObj, baseUrlS);
				element.appendChild(image);
				break;
			case IBlindProblem.WRONG_TEXT:
				// Node node = prob.getNode();
				Element image2 = createErrorImageElement(node, prob, idObj, baseUrlS);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					node.appendChild(image2);
				} else {
					node.getParentNode().insertBefore(image2, node);
				}
				break;
			case IBlindProblem.NO_VALUE_INPUT_BUTTON:
			case IBlindProblem.SEPARATE_DBCS_INPUT_VALUE:
				Element image3 = createErrorImageElement(node, prob, idObj, baseUrlS);
				node.getParentNode().insertBefore(image3, node);
				break;

			default:
			}
		}
	}

	@SuppressWarnings("nls")
	private static Element createErrorImageElement(Node target, IProblemItem prob, Integer idObj, String baseUrlS) {
		Element img = target.getOwnerDocument().createElement(IMG);
		img.setAttribute(ALT, "error icon");
		if (IBlindProblem.WRONG_NBSP_ALT_IMG == prob.getSubType()) {
			img.setAttribute(TITLE, prob.getDescription().replaceAll("&", "&amp;"));
		} else {
			img.setAttribute(TITLE, prob.getDescription());
		}
		img.setAttribute("onmouseover", "updateBaloon('id" + idObj + "');");
		img.setAttribute(SRC, baseUrlS + "img/" + VisualizeEngine.ERROR_ICON_NAME);
		return (img);
	}

	@SuppressWarnings("nls")
	public static Document returnTextView(Document result, IPacketCollection allPc, String baseUrl) {
		NodeList bodyNl = result.getElementsByTagName(BODY);

		// TODO remove second Body, script, etc.
		if (bodyNl.getLength() > 0) {
			Element bodyEl = (Element) bodyNl.item(0);
			NodeList nl = bodyEl.getChildNodes();
			int size = nl.getLength();
			for (int i = size - 1; i >= 0; i--) {
				bodyEl.removeChild(nl.item(i));
			}
			String str;
			size = allPc.size();
			boolean brFlag = false;
			boolean insideLink = false;
			for (int i = 0; i < size; i++) {
				IPacket p = allPc.get(i);

				if (p.getContext().isLinkTag()) {
					insideLink = true;
				}
				str = p.getText();
				if (str != null && !str.equals(NULL_STRING)) {

					Element spanEl = result.createElement("span");
					spanEl.appendChild(result.createTextNode(str));
					bodyEl.appendChild(spanEl);

					if (insideLink)
						spanEl.setAttribute(STYLE, "text-decoration: underline;");
					brFlag = false;
				}
				if (p.getContext().isLineDelimiter()) {
					if (brFlag) {
						/*
						 * Element spanEl = result.createElement("span"); spanEl.appendChild(
						 * result.createTextNode(" SKIPPED ")); bodyEl.appendChild(spanEl);
						 */
					} else {
						Element br = result.createElement("br");
						bodyEl.appendChild(br);
						brFlag = true;
					}
				}
				if (!p.getContext().isLinkTag()) {
					insideLink = false;
				}
			}
		}

		NodeList nl = result.getElementsByTagName("head");
		if (nl.getLength() > 0) {

			Element el = (Element) nl.item(0);
			Element script = result.createElement(SCRIPT);
			script.setAttribute(SRC, baseUrl + "img/highlight-dummy.js");
			el.appendChild(script);

		}

		return result;
	}

}
