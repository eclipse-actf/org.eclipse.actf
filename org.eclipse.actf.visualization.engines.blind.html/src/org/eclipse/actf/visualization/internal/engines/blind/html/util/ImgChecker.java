/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.engines.blind.html.util;

import java.util.Map;
import java.util.Vector;

import org.eclipse.actf.visualization.engines.blind.TextCheckResult;
import org.eclipse.actf.visualization.engines.blind.TextChecker;
import org.eclipse.actf.visualization.engines.blind.html.IBlindProblem;
import org.eclipse.actf.visualization.engines.blind.html.VisualizeEngine;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.internal.engines.blind.html.BlindProblem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ImgChecker {

	// separated from visualizeEngine
	// TODO refactoring
	// TODO UseMap (object)

	private static final String SRC = "src"; //$NON-NLS-1$

	private static final String ALT = "alt"; //$NON-NLS-1$

	private VisualizeMapDataImpl mapData;

	// private Map node2infoMap;

	// private Map idMap;

	private Map<String, Element> mapMap;

	private TextChecker textChecker;

	private Vector<IProblemItem> problemV;

	private String baseUrl;

	private boolean[] checkItems;

	/**
	 * @param result2documentMap
	 * @param node2infoMap
	 * @param idMap
	 * @param mapMap
	 * @param removedNodeMap
	 * @param textChecker
	 * @param problemV
	 * @param baseUrl
	 * @param checkItems
	 */
	public ImgChecker(VisualizeMapDataImpl mapData,
			Map<String, Element> mapMap, TextChecker textChecker,
			Vector<IProblemItem> problemV, String baseUrl, boolean[] checkItems) {

		this.mapData = mapData;
		this.mapMap = mapMap;
		this.textChecker = textChecker;
		this.problemV = problemV;
		this.baseUrl = baseUrl;
		this.checkItems = checkItems;
	}

	@SuppressWarnings("nls")
	public boolean checkAndReplaceImg(Element img, Document doc, boolean remove) {

		Element mapEl = null;
		NodeList areaNL = null;
		// NodeList aNL = null;

		String mapName = img.getAttribute("usemap");
		if (mapName.length() > 0) {

			mapEl = mapMap.get(mapName.substring(1));
			if (mapEl != null) {

				areaNL = mapEl.getElementsByTagName("area");
				// "a" -> CheckerEngine 57.2
			}
		}

		String imgText = checkAlt(img);

		if (remove) {
			Node parent = img.getParentNode();

			Element spanEl = doc.createElement("span");
			spanEl.setAttribute("width", img.getAttribute("width"));
			spanEl.setAttribute("height", img.getAttribute("height"));
			spanEl.setAttribute("id", img.getAttribute("id"));
			spanEl.setAttribute("style", img.getAttribute("style"));

			boolean isVisible = true;
			VisualizationNodeInfo info = mapData.getNodeInfo(img);
			if (info != null) {
				isVisible = !info.isInvisible();
			}
			img.removeAttribute("id");

			if (imgText.length() > 0 && isVisible) {
				spanEl.appendChild(doc.createTextNode(imgText));
			}
			parent.insertBefore(spanEl, img);

			// image map
			if (areaNL != null) {
				int size = areaNL.getLength();
				for (int i = 0; i < size; i++) {
					Element areaEl = doc.createElement("span");
					areaEl.setAttribute("style", img.getAttribute("style"));
					spanEl.appendChild(areaEl);

					Element areaE = (Element) areaNL.item(i);
					BlindProblem prob = null;
					Integer idObj = null;
					
					areaEl.setAttribute("id", areaE.getAttribute("id")+"-span");
					
					if (!areaE.hasAttribute(ALT)) {
						if (areaE.hasAttribute("href")) {
							prob = new BlindProblem(IBlindProblem.NO_ALT_AREA,
									"map name=\"" + mapEl.getAttribute("name")
											+ "\", href=\""
											+ areaE.getAttribute("href")
											+ "\".");
							prob.setTargetNode(mapData.getOrigNode(areaE));
						}
					} else {
						String alt = areaE.getAttribute(ALT);
						TextCheckResult result = textChecker.checkAlt(alt);

						// blank and inappropriate are moved to C_300.1

						if (result.equals(TextCheckResult.SPACE_SEPARATED)
								|| result
										.equals(TextCheckResult.SPACE_SEPARATED_JP)) {
							prob = new BlindProblem(
									IBlindProblem.SEPARATE_DBCS_ALT_AREA, alt);
						}
						if (prob != null) {
							prob.setTargetNode(mapData.getOrigNode(areaE));
						}
						areaEl.appendChild(doc
								.createTextNode("[" + alt + ".] "));
					}

					if (prob != null) {
						idObj = mapData.getIdOfNode(img);
						if (idObj != null) {
							prob.setNode(areaE, idObj.intValue());
						} else {
							prob.setNode(areaE);
						}
						problemV.add(prob);

						idObj = mapData.getIdOfNode(areaE);

						if (checkItems[prob.getSubType()]) {
							Element errorImg = doc.createElement("img");
							errorImg.setAttribute(ALT, "error icon");
							errorImg.setAttribute("title",
									prob.getDescription());
							if (idObj != null) {
								errorImg.setAttribute("onmouseover",
										"updateBaloon('id" + idObj + "');");
							}
							errorImg.setAttribute(SRC, baseUrl + "img/"
									+ VisualizeEngine.ERROR_ICON_NAME);
							areaEl.appendChild(errorImg);
						}
					}
				}
			}

			mapData.addReplacedNodeMapping(img, spanEl);
			parent.removeChild(img);
		}
		// TODO
		return true;
	}

	private String checkAlt(Element img) {
		// check_H67(img); // For new JIS

		boolean noAltError = false;
		String altS = ""; //$NON-NLS-1$

		BlindProblem prob = null;

		if (!img.hasAttribute(ALT)) {
			prob = new BlindProblem(IBlindProblem.NO_ALT_IMG,
					img.getAttribute(SRC));
			noAltError = true;
		} else {
			altS = img.getAttribute(ALT);
			if (altS.length() > 0) {
				TextCheckResult result = textChecker.checkAlt(altS,
						img.getAttribute(SRC));
				if (result.equals(TextCheckResult.NG_WORD)
						|| result.equals(TextCheckResult.IMG_EXT)) {
					// prob = new BlindProblem(IBlindProblem.WRONG_ALT_IMG,
					// altS);
					prob = new BlindProblem(IBlindProblem.ALERT_WRONG_ALT, altS);
				} else if (result.equals(TextCheckResult.SPACE_SEPARATED_JP)) {
					prob = new BlindProblem(
							IBlindProblem.SEPARATE_DBCS_ALT_IMG, altS);
				} else if (result.equals(TextCheckResult.SPACE_SEPARATED)) {
					prob = new BlindProblem(IBlindProblem.ALERT_SPELL_OUT, altS);
				} else if (result.equals(TextCheckResult.BLANK_NBSP)) {
					prob = new BlindProblem(IBlindProblem.WRONG_NBSP_ALT_IMG,
							altS);
				} else if (!result.equals(TextCheckResult.OK)) {
					// includes ASCII_ART, BLANK, SAME_AS_SRC etc.
					prob = new BlindProblem(IBlindProblem.ALERT_WRONG_ALT, altS);
					// prob = new
					// BlindProblem(IBlindProblem.WRONG_ALT_IMG,
					// altS);
				}
			}
		}
		if (prob != null) {
			prob.setTargetNode(mapData.getOrigNode(img));
			Integer idObj = mapData.getIdOfNode(img);
			if (idObj != null) {
				prob.setNode(img, idObj.intValue());
			} else {
				prob.setNode(img);
			}
			problemV.add(prob);
		}

		String imgText = null;
		VisualizationNodeInfo info = mapData.getNodeInfo(img);
		if (info != null && info.getPacket() != null) {
			imgText = info.getPacket().getText();
		} else {
			// alt="" or without alt
			if (noAltError) {
				imgText = ""; //$NON-NLS-1$
			} else {
				imgText = altS;
			}
		}
		return (imgText);
	}

	/**
	 * For new JIS
	 * 
	 * @param img
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean isIgnoredByAT(Element img) {
		String alt = img.getAttribute(ALT);
		// System.out.println(Integer.toHexString(alt.codePointAt(0)));
		return img.hasAttribute(ALT) && alt.matches("[ \\u00A0]*");
	}
}
