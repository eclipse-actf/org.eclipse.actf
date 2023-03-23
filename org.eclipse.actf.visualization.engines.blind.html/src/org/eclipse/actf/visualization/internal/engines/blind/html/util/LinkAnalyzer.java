/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.engines.blind.html.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

import org.eclipse.actf.visualization.engines.blind.ParamBlind;
import org.eclipse.actf.visualization.engines.blind.html.IBlindProblem;
import org.eclipse.actf.visualization.engines.voicebrowser.IPacket;
import org.eclipse.actf.visualization.engines.voicebrowser.IPacketCollection;
import org.eclipse.actf.visualization.eval.html.HtmlTagUtil;
import org.eclipse.actf.visualization.eval.html.statistics.PageData;
import org.eclipse.actf.visualization.eval.problem.HighlightTargetId;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.internal.engines.blind.html.BlindProblem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LinkAnalyzer {

	private static final String NULL_STRING = ""; //$NON-NLS-1$

	private static final String SKIP_TO_MAIN_LINK_DEFINITION = ".*([sS]kip|[jJ]ump to|[lL]ink to) .+|.*(\u672c\u6587|\u30e1\u30a4\u30f3|\u3092\u8aad\u3080|(\u3078|\u306b)\u30b8\u30e3\u30f3\u30d7|(\u3078|\u306b)\u79fb\u52d5|\u3078\u306e\u30ea\u30f3\u30af).*"; //$NON-NLS-1$

	// TODO
	private static final String WRONG_SKIPLINK_DEFINITION = ".+ ([sS]kip to|[jJ]ump to) .+"; //$NON-NLS-1$

	private static final int MAX_SKIPLINK_REACHING_TIME = 20;

	private static final int MIN_MAX_TIME_TO_ELIMINATE_SKIP_LINK = 90;

	private List<Element> intraPageLinkList = new ArrayList<Element>();

	private Map<String, Element> anchorMap = new HashMap<String, Element>();

	private Map<Element, String> skipLinkMap = new HashMap<Element, String>();

	private Document doc;

	private IPacketCollection allPc;

	private VisualizeMapDataImpl mapData;

	private Set<String> invisibleIdSet;

	private List<IProblemItem> problems;

	private int intralinkErrorCount = 0;

	private int skiplinkErrorCount = 0;

	private TextCounter textCounter;

	private PageData pageData;

	/**
	 * 
	 */
	public LinkAnalyzer(Document result, IPacketCollection allPc,
			VisualizeMapDataImpl mapData, List<IProblemItem> problems,
			Set<String> invisibleIdSet, ParamBlind paramBlind, PageData pageData) {
		this.doc = result;
		this.allPc = allPc;
		this.mapData = mapData;
		this.invisibleIdSet = invisibleIdSet;
		this.problems = problems;
		this.pageData = pageData;

		textCounter = new TextCounter(paramBlind.iLanguage);
		listIntraPageLinks();
		analyzeIntraPageLinkMapping();
	}

	@SuppressWarnings("nls")
	private void listIntraPageLinks() {
		// list up links and anchors
		NodeList aNl = doc.getElementsByTagName("a");
		int aNlSize = aNl.getLength();
		for (int i = 0; i < aNlSize; i++) {
			Element curEl = (Element) aNl.item(i);
			String href = curEl.getAttribute("href");
			String name = curEl.getAttribute("name");
			if ((href != null) && (href.length() > 0)) {
				if (href.charAt(0) == '#') {

					VisualizationNodeInfo info = mapData.getNodeInfo(curEl);
					StringBuffer sb = new StringBuffer();

					// 07/28/2004 fix IndexOutOfBoundsException
					int size = allPc.size();
					if (info != null) {
						for (int j = info.getPacketId(); j < size; j++) {
							IPacket p = allPc.get(j);
							String str = p.getText();
							if (str != null) {
								sb.append(str);
							}
							if (!p.getContext().isInsideAnchor()) {
								break;
							}
						}
					}
					int words = textCounter.getWordCount(sb.toString());

					String linkTitleOrg = NULL_STRING;
					String linkTitle = NULL_STRING;

					BlindProblem prob = null;

					if (curEl.hasAttribute("title")) {
						linkTitle = curEl.getAttribute("title");
						linkTitleOrg = linkTitle;

						if (words == 0) {
							words += textCounter.getWordCount(linkTitleOrg);
							if (words > 0) {
								prob = new BlindProblem(
										IBlindProblem.NO_TEXT_WITH_TITLE_INTRAPAGELINK,
										linkTitle);

								prob.setTargetNode(mapData.getOrigNode(curEl));
								Integer idObj = mapData.getIdOfNode(curEl);
								if (idObj != null) {
									prob.setNode(curEl, idObj.intValue());
								} else {
									prob.setNode(curEl);
								}
								// skiplinkErrorCount++;
								problems.add(prob);
							}
						}

						linkTitle = linkTitle.trim();
						linkTitle = linkTitle.replaceAll("\\[|\\]|\\.|\\!|\\>",
								NULL_STRING);
					}

					String linkText = sb.toString();
					linkText = linkText.trim();
					linkText = linkText.replaceAll("\\[|\\]|\\.|\\!|\\>", NULL_STRING);

					prob = null;

					if (words > 0) {

						if (linkText.matches(SKIP_TO_MAIN_LINK_DEFINITION)) {
							skipLinkMap.put(curEl, sb.toString());
						} else if (linkTitle
								.matches(SKIP_TO_MAIN_LINK_DEFINITION)) {
							skipLinkMap.put(curEl, linkTitleOrg);
						} else {
							if (linkTitle.matches(WRONG_SKIPLINK_DEFINITION)) {
								prob = new BlindProblem(
										IBlindProblem.WRONG_SKIP_LINK_TITLE,
										linkText);
							}

							if (linkText.matches(WRONG_SKIPLINK_DEFINITION)) {
								prob = new BlindProblem(
										IBlindProblem.WRONG_SKIP_LINK_TEXT,
										linkText);
							}
						}
						intraPageLinkList.add(curEl);
					} else {
						String noScriptText = HtmlTagUtil
								.getNoScriptText(curEl);
						if (noScriptText.length() > 0) {
							// TODO new alert
							// TODO append text -> result?
						} else {
							prob = new BlindProblem(
									IBlindProblem.NO_TEXT_INTRAPAGELINK, href);
							if (!(curEl.hasAttribute("onclick") || curEl
									.hasAttribute("onmouseover"))) {

								intralinkErrorCount++;
							}
						}
					}

					// add problem
					if (prob != null) {
						prob.setTargetNode(mapData.getOrigNode(curEl));
						Integer idObj = mapData.getIdOfNode(curEl);
						if (idObj != null) {
							prob.setNode(curEl, idObj.intValue());
						} else {
							prob.setNode(curEl);
						}
						// skiplinkErrorCount++;
						problems.add(prob);
					}
				}
			}

			if ((name != null) && (name.length() > 0)) {
				anchorMap.put(name, curEl);
			}
		}

		// TODO consider intrapage link map by using "area"
	}

	@SuppressWarnings("nls")
	private void analyzeIntraPageLinkMapping() {
		Iterator<Element> it = intraPageLinkList.iterator();
		while (it.hasNext()) {
			Element lel = it.next();

			String href = lel.getAttribute("href").substring(1);
			// lel.getAttribute("href").substring(1).toLowerCase();

			Element tmpTarget = lel;
			boolean isVisible = true;
			while (isVisible && tmpTarget != null) {
				String idS = tmpTarget.getAttribute("id");
				if (invisibleIdSet.contains(idS)) {
					// System.out.println(idS);
					BlindProblem prob = new BlindProblem(
							IBlindProblem.INVISIBLE_INTRAPAGE_LINK, "\""
									+ HtmlTagUtil.getTextAltDescendant(lel)
									+ "\"(href=#" + href + ", id=" + idS + ") ");
					prob.setTargetNode(mapData.getOrigNode(lel));
					Integer idObj = mapData.getIdOfNode(lel);
					if (idObj != null) {
						prob.setNode(lel, idObj.intValue());
					} else {
						prob.setNode(lel);
					}

					intralinkErrorCount++;
					problems.add(prob);

					isVisible = false;
				}

				Node tmpN = tmpTarget.getParentNode();
				if (tmpN != null && tmpN.getNodeType() == Node.ELEMENT_NODE) {
					tmpTarget = (Element) tmpN;
				} else {
					tmpTarget = null;
				}
			}
			if (!isVisible) {
				continue;
			}

			if (href.length() == 0) {
				NodeList tmpNL = doc.getElementsByTagName("body");
				if (tmpNL != null && tmpNL.getLength() > 0) {
					Node tmpBody = tmpNL.item(0);
					mapData.addIntraPageLinkMapping(lel, tmpBody);
				}
				continue;
			}

			Element ael = anchorMap.get(href);
			if (ael != null) {
				mapData.addIntraPageLinkMapping(lel, ael);
			} else {
				Element idEl = doc.getElementById(href);
				if (idEl != null) {
					mapData.addIntraPageLinkMapping(lel, idEl);
				} else {
					BlindProblem prob = null;

					boolean toTop = false;
					String linkText = HtmlTagUtil.getTextAltDescendant(lel);
					if (linkText
							.matches(".*(\u5148\u982d|\u30c8\u30c3\u30d7|\u4e0a|top|start).*")) {
						toTop = true;
					}

					if (skipLinkMap.containsKey(lel)) {
						if (href.matches(".*top.*") || toTop) {// TBD accuracy
							prob = new BlindProblem(
									IBlindProblem.ALERT_NO_DEST_INTRA_LINK,
									href);
						} else {
							prob = new BlindProblem(
									IBlindProblem.NO_DEST_SKIP_LINK, href);
							// TODO onclick?
							intralinkErrorCount++;
							skiplinkErrorCount++;
						}

						skipLinkMap.remove(lel);
					} else {
						if (href.matches(".*top.*") || toTop) {// TBD accuracy
							prob = new BlindProblem(
									IBlindProblem.ALERT_NO_DEST_INTRA_LINK,
									href);
						} else {
							prob = new BlindProblem(IBlindProblem.NO_DEST_LINK,
									href);
							if (!(lel.hasAttribute("onClick") || lel
									.hasAttribute("onmouseover"))) {
								intralinkErrorCount++;
							}
						}
					}

					prob.setTargetNode(mapData.getOrigNode(lel));
					Integer idObj = mapData.getIdOfNode(lel);
					if (idObj != null) {
						prob.setNode(lel, idObj.intValue());
					} else {
						prob.setNode(lel);
					}
					problems.add(prob);
				}
			}
		}
	}

	/**
	 * 
	 */
	public Vector<IProblemItem> skipLinkCheck(int maxTime, int maxTimeLeaf) {
		// TODO consider leaf time

		Vector<IProblemItem> problemV = new Vector<IProblemItem>();

		VisualizationNodeInfo skipLinkNodeInfo = null;
		int minSkipLinkTime = Integer.MAX_VALUE;

		int headingCount = 0;
		int skipLinkCount = skipLinkMap.size();
		int forwardIntraLinkCount = 0;

		@SuppressWarnings("unused")//TODO
		int intraDestCount, headingDestCount = 0;

		HashSet<Integer> forwardSkipDestIdSet = new HashSet<Integer>();
		HashSet<Node> skipDestIdSet = new HashSet<Node>();
		// TODO href="#" -> body
		HashSet<Integer> headingDestIdSet = new HashSet<Integer>();
		// TODO href="#" -> body

		Vector<HighlightTargetId> overTimeElementV = new Vector<HighlightTargetId>();
		Set<Node> overTimeElementChildSet = new HashSet<Node>();

		List<VisualizationNodeInfo> nodeList = mapData.getNodeInfoList();
		Iterator<VisualizationNodeInfo> it = nodeList.iterator();
		while (it.hasNext()) {
			VisualizationNodeInfo curInfo = it.next();
			Node curNode = curInfo.getNode();
			if (curNode != null) {
				if (skipLinkMap.containsKey(curNode)) {
					if (curInfo.getTime() < minSkipLinkTime) {
						// skip link detected
						skipLinkNodeInfo = curInfo;
						minSkipLinkTime = curInfo.getTime();
					}
				}

				if (curInfo.isHeading()) {
					// TODO check (do not include elements under the headings)
					headingCount++;
					headingDestIdSet.add(new Integer(curInfo.getId()));
				}

				Node tmpNode = curNode;
				if (curInfo.isBlockElement() && !curInfo.isSequence()) {
					if (curInfo.getTime() > 120
							&& !overTimeElementChildSet.contains(curNode)) {
						overTimeElementV.add(new HighlightTargetId(curInfo
								.getId(), curInfo.getId()));

						Stack<Node> stack = new Stack<Node>();
						tmpNode = tmpNode.getFirstChild();
						while (tmpNode != null) {
							if (tmpNode.getNodeType() == Node.ELEMENT_NODE) {
								overTimeElementChildSet.add(tmpNode);
							}

							if (tmpNode.hasChildNodes()) {
								stack.push(tmpNode);
								tmpNode = tmpNode.getFirstChild();
							} else if (tmpNode.getNextSibling() != null) {
								tmpNode = tmpNode.getNextSibling();
							} else {
								tmpNode = null;
								while ((tmpNode == null) && (stack.size() > 0)) {
									tmpNode = stack.pop();
									tmpNode = tmpNode.getNextSibling();
								}
							}
						}

					}
				}

			}
		}

		Map<Node, Node> linkMap = mapData.getIntraPageLinkMap();
		for (Iterator<Node> linkIt = linkMap.keySet().iterator(); linkIt
				.hasNext();) {
			Node source = linkIt.next();
			Node dest = linkMap.get(source);
			// System.out.println("ok: "+source+" : "+dest);

			skipDestIdSet.add(dest);

			Map<Node, Integer> idMap = mapData.getResult2idMap();
			if (idMap.containsKey(source) && idMap.containsKey(dest)) {
				// System.out.println("id found");
				int sourceId = mapData.getIdOfNode(source).intValue();
				Integer destIdInteger = mapData.getIdOfNode(dest);
				int destId = destIdInteger.intValue();
				if (sourceId < destId) {
					VisualizationNodeInfo info = mapData.getNodeInfo(source);
					if (info != null) {
						int timeFromTop = info.getTime();
						if (timeFromTop < 60) {
							forwardSkipDestIdSet.add(destIdInteger);
						}
					} else {
						// can't calc time
						forwardSkipDestIdSet.add(destIdInteger);
					}
				}
				;
			}
		}

		forwardIntraLinkCount = forwardSkipDestIdSet.size();
		intraDestCount = skipDestIdSet.size();
		headingDestCount = headingDestIdSet.size();

		// TODO use block element time (isBlock && !isSequence)
		// TODO alert only single heading

		int overTimeCount = overTimeElementV.size();
		HighlightTargetId[] overId = new HighlightTargetId[overTimeCount];
		overTimeElementV.toArray(overId);

		pageData.setSkipMainNum(skipLinkCount);
		pageData.setForwardIntraPageLinkNum(forwardIntraLinkCount);
		pageData.setBrokenSkipMainNum(skiplinkErrorCount);
		pageData.setBrokenIntraPageLinkNum(intralinkErrorCount);

		// TODO
		// number/ratio of overTimeElement
		// number of forwardIntraLink/link target
		// efficiency of forwardIntraLink
		// time difference (original/with intra)?

		if (skipLinkNodeInfo == null) {
			if (headingCount > 0 || forwardIntraLinkCount > 0) {
				if (maxTime >= MIN_MAX_TIME_TO_ELIMINATE_SKIP_LINK) {
					problemV.add(new BlindProblem(
							IBlindProblem.NO_SKIPTOMAIN_WITH_STRUCTURE));
				}
			} else {
				if (maxTime < MIN_MAX_TIME_TO_ELIMINATE_SKIP_LINK) {
					// if max time is less than 90 sec, skip link can be
					// eliminated.
					problemV.add(new BlindProblem(
							IBlindProblem.ALERT_NO_SKIPTOMAIN_NO_STRUCTURE, NULL_STRING
									+ maxTime));
					// return true;
				} else {
					problemV.add(new BlindProblem(
							IBlindProblem.NO_SKIPTOMAIN_LINK));
				}

			}
		} else if (minSkipLinkTime >= MAX_SKIPLINK_REACHING_TIME) {
			// TODO remove this problem if the page has skip link at top of the
			// page
			BlindProblem prob = new BlindProblem(
					IBlindProblem.TOOFAR_SKIPTOMAIN_LINK, minSkipLinkTime + " "); //$NON-NLS-1$
			Integer idObj = mapData.getIdOfNode(skipLinkNodeInfo.getNode());
			if (idObj != null) {
				prob.setNode(skipLinkNodeInfo.getNode(), idObj.intValue());
			} else {
				prob.setNode(skipLinkNodeInfo.getNode());
			}
			prob.setTargetNode(mapData.getOrigNode(skipLinkNodeInfo.getNode()));
			problemV.add(prob);
			// return false;
		}

		if (overTimeCount > 0) {
			BlindProblem tmpBP = null;
			if (headingCount > 0) {
				if (forwardIntraLinkCount > 0) {
					tmpBP = new BlindProblem(
							IBlindProblem.LESS_STRUCTURE_WITH_BOTH);
				} else {
					tmpBP = new BlindProblem(
							IBlindProblem.LESS_STRUCTURE_WITH_HEADING);
				}
			} else {
				if (forwardIntraLinkCount > 0) {
					tmpBP = new BlindProblem(
							IBlindProblem.LESS_STRUCTURE_WITH_SKIPLINK);
				} else {
					tmpBP = new BlindProblem(IBlindProblem.TOO_LESS_STRUCTURE);
				}
			}
			for (int i = 0; i < overTimeCount; i++) {
				tmpBP.addNodeIds(overId[i]);
			}

			problemV.add(tmpBP);

		}

		return (problemV);
	}

}
