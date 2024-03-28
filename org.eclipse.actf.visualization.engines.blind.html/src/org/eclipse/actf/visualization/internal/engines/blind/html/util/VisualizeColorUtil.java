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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.actf.util.logging.DebugPrintUtil;
import org.eclipse.actf.visualization.engines.blind.ParamBlind;
import org.eclipse.actf.visualization.eval.html.HtmlTagUtil;
import org.eclipse.swt.graphics.RGB;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@SuppressWarnings("nls")
public class VisualizeColorUtil {

	private static final double WORD_JP = (60.0 / (488.0 / 1.3));

	private Document result;

	private VisualizeMapDataImpl mapData;

	private List<VisualizationNodeInfo> nodeInfoList;

	private Map<Node, Node> linkMap;

	private ParamBlind param;

	private boolean isHTML5;

	/**
	 * @param result
	 * @param mapData
	 * @param nodeList
	 * @param linkMap
	 * @param param
	 */
	public VisualizeColorUtil(Document result, VisualizeMapDataImpl mapData, ParamBlind param, boolean isHTML5) {
		this.result = result;
		this.mapData = mapData;
		this.nodeInfoList = mapData.getNodeInfoList();
		this.linkMap = mapData.getIntraPageLinkMap();
		this.param = param;
		this.isHTML5 = isHTML5;
	}

	public void setColorAll() {
		int changed = 0;

		DocumentCleaner.removeBgcolor(result);

		initHeadings();

		for (int i = 0; i < 10; i++) { // appropriate? 10?
			changed = calcWords();
			// calcWordsRefresh();

			if (changed == 0)
				break;
		} // set color onto each element.

		calcTime();

		calcOrgTime();

		setColor();
	}

	private void setColor() {

		String strRGB = "#000000";
		int r = param.labelTagsColor.red + 80;
		int g = param.labelTagsColor.green + 80;
		int b = param.labelTagsColor.blue + 80;
		RGB ariaLabelColor = new RGB(r > 255 ? 255 : r, g > 255 ? 255 : g, b > 255 ? 255 : b);
		RGB descriptionColor = new RGB(r > 255 ? 255 : r, g > 255 ? 255 : g, b > 255 ? 255 : b);

		if (param.bVisualizeTable) {
			NodeList nl = result.getElementsByTagName("head");
			try {
				Element headEl = (Element) nl.item(0);

				Element styleEl = result.createElement("style");
				styleEl.setAttribute("type", "text/css");
				strRGB = getRGBString(param.tableBorderColor, "#000000");
				Comment comment = result
						.createComment("td {border-width: 1px; border-style: dashed; border-color: " + strRGB + "}");
				styleEl.appendChild(comment);
				headEl.appendChild(styleEl);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Iterator<VisualizationNodeInfo> it = nodeInfoList.iterator();
		while (it.hasNext()) {
			VisualizationNodeInfo info = it.next();
			Node node = info.getNode();
			Element el = null;
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				el = (Element) node;
			} else if (node.getNodeType() == Node.TEXT_NODE) {
				/*
				 * System.out.println( "VisualizeEngine: 709: parent of span: " +
				 * node.getParentNode().getNodeName());
				 */
				Node parent = node.getParentNode();
				if (parent.getNodeName().equals("textarea")) {
					continue;
				}
				el = result.createElement("span");
				parent.insertBefore(el, node);
				if (info.isInvisible()) {
					// System.out.println("invisible:"+node);
					parent.removeChild(node);
				} else {
					el.appendChild(node);
					VisualizationNodeInfo tmpInfo = mapData.getNodeInfo(parent);
					if (null != tmpInfo) {
						if (tmpInfo.isARIAlabel() || tmpInfo.isARIAdescription()) {
							try {
								el.setAttribute("style", ((Element) parent).getAttribute("style"));
							} catch (Exception e) {
							}
						}
					}
				}
				Integer idObj = mapData.getIdOfNode(node);
				if (idObj != null) {
					el.setAttribute("id", "id" + idObj.toString());
				}

			} else {
				DebugPrintUtil.devOrDebugPrintln("VisualizeColorUtil: unknown node in the nodeList"); //$NON-NLS-1$
				continue;
			}

			String headingRGB = getRGBString(param.headingTagsColor, "#33CCFF");
			String thRGB = getRGBString(param.tableHeaderColor, "#99FF00");
			String labelRGB = getRGBString(param.labelTagsColor, "#FFFF00");
			String inputRGB = getRGBString(param.inputTagsColor, "#FF9900");
			String captionRGB = getRGBString(param.captionColor, "#FFFF80");
			String ariaLabelRGB = getRGBString(ariaLabelColor, "#FFFF50");
			String descriptionRGB = getRGBString(descriptionColor, "#FFFF50");

			if (param.bColorizeTags && (info.isColorVisualizationTarget())) {
				if (info.isHeading()) {
					strRGB = headingRGB;
				} else if (info.isTableHeader()) {
					strRGB = thRGB;
				} else if (info.isIdRequiredInput()) {
					strRGB = inputRGB;
				} else if (info.isLabel()) {
					strRGB = labelRGB;
				} else if (info.isARIAlabel()) {
					strRGB = ariaLabelRGB;
				} else if (info.isARIAdescription()) {
					strRGB = descriptionRGB;
				} else if (info.isCaption()) {
					strRGB = captionRGB;
				}

				// TBD caption

				el.setAttribute("style", "color: black; background-image: none; background-color:" + strRGB);// +
				// "}");
				// } else if (info.getWords() > 0) {
			} else {
				int time = info.getTime();
				if (time == 0) {
					switch (param.iLanguage) {
					case 1: // japanese
						time = calcTimeJp(info.getTotalWords(), info.getTotalLines());
						break;
					default: // english
						time = calcTime(info.getTotalWords(), info.getTotalLines());
						break;
					}
					info.setTime(time);
				}

				if (param.bVisualizeTime == true) {
					if ("mark".equals(el.getTagName()) || HtmlTagUtil.hasAncestor(el, "mark")) {
						el.setAttribute("style", "color: black; background-image: none;");
					} else if (!el.hasAttribute("style")) {
						el.setAttribute("style", "color: black; background-image: none; background-color: #"
								+ calcColor(time, param.maxTimeColor, param.iMaxTime));
					}

				} else {
					el.setAttribute("style", "color: black; background-image: none; background-color: transparent");
				}
			} /*
				 * else { }
				 */
			/*
			 * el.setAttribute( "comment", info.getPacketId() + "," + info.getId() + "," +
			 * info.getTotalWords() + "," + info.getWords() + "," + info.getTotalLines() +
			 * "," + info.getLines());
			 */
		}
	}

	private String calcColor(int time, RGB rgb, int maxTime) {

		double timeD = time;
		double maxTimeD = maxTime;

		if (time >= maxTime) {
			java.awt.Color color = new java.awt.Color(rgb.red, rgb.green, rgb.blue);
			return Integer.toHexString(color.getRGB()).substring(2);
		} else {

			int colorValueR = (int) (255.0 - (timeD / maxTimeD) * (255.0 - rgb.red));
			int colorValueG = (int) (255.0 - (timeD / maxTimeD) * (255.0 - rgb.green));
			int colorValueB = (int) (255.0 - (timeD / maxTimeD) * (255.0 - rgb.blue));

			java.awt.Color color = new java.awt.Color(colorValueR, colorValueG, colorValueB);
			return Integer.toHexString(color.getRGB()).substring(2);
		}
	}

	private String getRGBString(RGB target, String defaultValue) {
		if (target != null) {
			return ("rgb(" + target.red + "," + target.green + "," + target.blue + ")");
		}
		return (defaultValue);
	}

	private void initHeadings() {
		// TODO consider combination of skip nav and Headings

		int headingCount = 0;
		int landmarkCount = 0;

		int curTotalWords = 0;
		int curTotalLines = 0;
		int size = nodeInfoList.size();

		for (int i = 0; i < size; i++) {
			VisualizationNodeInfo curInfo = nodeInfoList.get(i);

			if (isHTML5 && curInfo.isLandmark()) {
				landmarkCount++;

				int words = wordcountForLandmark(landmarkCount);
				if (calcTime(curTotalWords, curTotalLines) >= calcTime(words, 0)) {
					curTotalWords = words;
					curTotalLines = 0;
				}

				// TODO enable after NVDA support navigation key for "main"
				// if (curInfo.getNode().getNodeName().equalsIgnoreCase("main"))
				// {
				// // some screen readers has navigation key (JAWS: "q") for
				// "main"
				// curTotalWords = 0;
				// curTotalLines = 0;
				// }

				curInfo.setTotalWords(curTotalWords);
				curInfo.setTotalLines(curTotalLines);

				curTotalWords += curInfo.getWords();
				curTotalLines += curInfo.getLines();

			} else if (curInfo.isHeading()) {
				Node tmpN = curInfo.getNode();
				if (tmpN.getNodeName().matches("h[1-6]")) {
					headingCount++;
				} else if (tmpN instanceof Element) {
					Element tmpE = (Element) tmpN;
					if (tmpE.hasAttribute("role") && "heading".equalsIgnoreCase(tmpE.getAttribute("role"))) {
						headingCount++;
					}
				}

				int tmpTotalWords = wordcountForHeading(headingCount);

				// System.out.println(headingCount+": "+curTotalWords+"
				// "+tmpTotalWords+" "+curInfo.getTotalWords()+"
				// "+curInfo.getWords());
				if (calcTime(curTotalWords, curTotalLines) >= calcTime(tmpTotalWords, 0)) {
					curTotalWords = tmpTotalWords;
					curTotalLines = 0;
				}

				curInfo.setTotalWords(curTotalWords);
				curInfo.setTotalLines(curTotalLines);

				curTotalWords += curInfo.getWords();
				curTotalLines += curInfo.getLines();

			} else {

				if (calcTime(curInfo.getTotalWords(), curInfo.getTotalLines()) > calcTime(curTotalWords,
						curTotalLines)) {
					curInfo.setTotalWords(curTotalWords);
					curInfo.setTotalLines(curTotalLines);

					curTotalWords += curInfo.getWords();
					curTotalLines += curInfo.getLines();

				} else {

					curTotalWords = curInfo.getTotalWords() + curInfo.getWords();
					curTotalLines = curInfo.getTotalLines() + curInfo.getLines();

				}
			}
		}
	}

	private void calcTime() {
		int size = nodeInfoList.size();
		for (int i = 0; i < size; i++) {
			VisualizationNodeInfo curInfo = nodeInfoList.get(i);

			int time = calcTime(curInfo.getTotalWords(), curInfo.getTotalLines());

			curInfo.setTime(time);
			if (curInfo.getNode().getNodeName().matches("h[1-6]")) {
				replaceParentInfoTime(curInfo.getNode(), time);
			}
		}
	}

	private void calcOrgTime() {
		int size = nodeInfoList.size();
		for (int i = 0; i < size; i++) {
			VisualizationNodeInfo curInfo = nodeInfoList.get(i);

			int time = calcTime(curInfo.getOrgTotalWords(), curInfo.getOrgTotalLines());

			curInfo.setOrgTime(time);
		}
	}

	private void replaceParentInfoTime(Node target, int time) {
		if (target != null) {
			Node parent = target.getParentNode();
			while (parent != null) {
				if (parent.getFirstChild() == target) {
					VisualizationNodeInfo nodeInfo = mapData.getNodeInfo(parent);
					if (nodeInfo != null && nodeInfo.getTime() > time) {
						nodeInfo.setTime(time);
					}
					target = parent;
					parent = target.getParentNode();
				} else {
					break;
				}
			}
		}
	}

	private void replaceParentInfoWord(Node target, int word, int line, int newTime) {
		if (target != null) {
			Node parent = target.getParentNode();
			while (parent != null) {
				if (parent.getFirstChild() == target) {
					VisualizationNodeInfo nodeInfo = mapData.getNodeInfo(parent);
					if (nodeInfo != null && calcTime(nodeInfo.getTotalWords(), nodeInfo.getTotalLines()) > newTime) {
						nodeInfo.setTotalWords(word);
						nodeInfo.setTotalLines(line);
					}

					target = parent;
					parent = target.getParentNode();
				} else {
					break;
				}
			}
		}
	}

	private int calcWords() {
		int countChanged = 0;
		Set<Node> linkSet = linkMap.keySet();
		Iterator<Node> it = linkSet.iterator();
		while (it.hasNext()) {
			Node fromNode = it.next();
			Node toNode = linkMap.get(fromNode);

			Integer fromIdInt = mapData.getIdOfNode(fromNode);
			Integer toIdInt = mapData.getIdOfNode(toNode);
			if (fromIdInt == null || toIdInt == null) {
				// toIdInt=null -> Alert is moved to other checker
				continue;
			}

			// TODO might be able to use mapData.getNodeInfo(node)
			int fromId = fromIdInt.intValue();
			int toId = toIdInt.intValue();

			VisualizationNodeInfo fromInfo = nodeInfoList.get(fromId);
			if (fromInfo.getNode() != fromNode) {
				DebugPrintUtil.devOrDebugPrintln("from node does not exists: " + fromId + " " + fromNode);
				continue;
			}
			VisualizationNodeInfo toInfo = nodeInfoList.get(toId);
			if (toInfo.getNode() != toNode) {
				DebugPrintUtil.devOrDebugPrintln("to node does not exists: " + toId + " " + toNode);
				continue;
			}

			VisualizationNodeInfo curInfo = toInfo;
			int curId = toId;
			int curTotalWords = fromInfo.getTotalWords() + getWordcountFor2sec();
			int curTotalLines = fromInfo.getTotalLines();
			int newTime = calcTime(curTotalWords, curTotalLines);

			while (calcTime(curInfo.getTotalWords(), curInfo.getTotalLines()) > newTime) {
				countChanged++;
				curInfo.setTotalWords(curTotalWords);
				curInfo.setTotalLines(curTotalLines);

				replaceParentInfoWord(curInfo.getNode(), curTotalWords, curTotalLines, newTime);

				// elements after intra page link
				curId++;
				if (curId >= nodeInfoList.size()) {
					break;
				}

				curTotalWords = curTotalWords + curInfo.getWords();
				curTotalLines = curTotalLines + curInfo.getLines();
				curInfo = nodeInfoList.get(curId);
				newTime = calcTime(curTotalWords, curTotalLines);
			}
		}
		return countChanged;
	}

	private int calcTime(int words, int lines) {
		switch (param.iLanguage) {
		case ParamBlind.EN:
			return calcTimeEn(words, lines);
		case ParamBlind.JP:
			return calcTimeJp(words, lines);
		default:
			return calcTimeEn(words, lines);
		}
	}

	// seconds
	private int calcTimeEn(int words, int lines) {
		return (int) ((words / (3.0)) + (lines * (0.7)));// 3.0 = 60/180
	}

	// seconds
	private int calcTimeJp(int words, int lines) {
		return (int) ((words * WORD_JP) + (lines * (0.6)));
	}

	private int wordcountForLandmark(int landmarkNumber) {
		switch (param.iLanguage) {
		case ParamBlind.EN:
			return 3 * landmarkNumber + 9;
		case ParamBlind.JP:
			return (int) Math.round(6.5 * landmarkNumber) + 18;
		default:
			return 3 * landmarkNumber + 9;
		}
	}

	private int wordcountForHeading(int headingNumber) {
		// use 1 sec for each landmark (text is shorter than headings)
		switch (param.iLanguage) {
		case ParamBlind.EN:
			return 6 * headingNumber + 9;
		case ParamBlind.JP:
			return 13 * headingNumber + 18;
		default:
			return 6 * headingNumber + 9;
		}

	}

	private int getWordcountFor2sec() {
		switch (param.iLanguage) {
		case ParamBlind.EN:
			return 6;
		case ParamBlind.JP:
			return 13;
		default:
			return 6;
		}
	}

}
