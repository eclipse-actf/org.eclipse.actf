/*******************************************************************************
 * Copyright (c) 2004, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.engines.lowvision.checker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.actf.model.dom.dombyjs.IStyleSheet;
import org.eclipse.actf.model.dom.dombyjs.IStyleSheets;
import org.eclipse.actf.util.JapaneseEncodingDetector;
import org.eclipse.actf.util.xpath.XPathService;
import org.eclipse.actf.util.xpath.XPathServiceFactory;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.internal.engines.lowvision.Messages;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.ProblemItemLV;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FixedFontChecker {

	private static final Pattern COMMENT = Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	private static final Pattern STYLEITEM = Pattern.compile(".*?\\{.*?\\}", Pattern.DOTALL);

	private static final Pattern FIXSIZE_PATTERN = Pattern.compile(
			".*\\{[^\\}]*font-size(\\p{Space})*:[^;v]*(mm|cm|in|pt|pc|px).*\\}.*",
			Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

	private static final Pattern FIXSIZE_PATTERN_ATTR = Pattern
			.compile("font-size(\\p{Space})*:[^;v]*(mm|cm|in|pt|pc|px)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

	private static final XPathService xpathService = XPathServiceFactory.newService();

	private Map<String, String> styleSheetsMap = new HashMap<String, String>();
	private Map<Element, String> styleElementMap = new HashMap<Element, String>();
	private List<Element> elementsWithStyleList;

	public FixedFontChecker(Document target, IStyleSheets styleSheets) {
		elementsWithStyleList = getElementsListByXPath(target, "//*[@style]");
		prepStyleElements(target.getElementsByTagName("style"));
		prepStyleSheets(styleSheets);
	}

	public List<IProblemItem> check() {
		List<IProblemItem> problemList = new ArrayList<IProblemItem>();

		Set<Element> styleElementList = styleElementMap.keySet();
		for (Element e : styleElementList) {
			String style = styleElementMap.get(e);
			if (style != null) {
				Set<String> fixSelector = new HashSet<String>();

				Matcher matcher = STYLEITEM.matcher(style);
				while (matcher.find()) {
					String group = matcher.group();
					boolean fix = FIXSIZE_PATTERN.matcher(group).find();
					if (fix) {
						fixSelector.add(getSelector(group));
					}
				}
				if (fixSelector.size() > 0) {
					problemList.add(generateCheckerProblem("L_11.1", "(" + Messages.StyleElement + ", "
							+ Messages.Selector + "=" + getSelectors(fixSelector) + ")", e));
				}
			}
		}

		Set<String> keys = styleSheetsMap.keySet();
		for (String ss : keys) {
			if (ss != null && ss.length() > 0) {
				// avoid duplication
				String style = styleSheetsMap.get(ss);
				if (style == null) {
					continue;
				}
				Set<String> fixSelector = new HashSet<String>();

				Matcher matcher = STYLEITEM.matcher(style);
				while (matcher.find()) {
					String group = matcher.group();
					boolean fix = FIXSIZE_PATTERN.matcher(group).find();
					if (fix) {
						fixSelector.add(getSelector(group));
					}
				}
				if (fixSelector.size() > 0) {
					problemList.add(generateCheckerProblem("L_11.1",
							"(" + ss + ", " + Messages.Selector + "=" + getSelectors(fixSelector) + ")"));
				}
			}
		}

		for (Element e : elementsWithStyleList) {
			String style = e.getAttribute("style");

			boolean fix = FIXSIZE_PATTERN_ATTR.matcher(style).find();
			if (fix) {
				problemList
						.add(generateCheckerProblem("L_11.2", "(" + Messages.StyleAttribute + ", " + style + ")", e)); // error
			}
		}

		return problemList;
	}

	private IProblemItem generateCheckerProblem(String strId, String targetStr, Element targetE) {
		IProblemItem tmpCP = generateCheckerProblem(strId, targetStr);
		tmpCP.setTargetNode(targetE);
		return (tmpCP);
	}

	private IProblemItem generateCheckerProblem(String strId, String targetStr) {
		ProblemItemLV tmpCP = new ProblemItemLV(strId);
		tmpCP.setCanHighlight(false);
		tmpCP.setTargetString(targetStr);
		tmpCP.setSubType(ProblemItemLV.LOWVISION_FIXED_SIZE_FONT_PROBLEM);
		tmpCP.setSeverityLV(25);
		return (tmpCP);
	}

	private String getSelector(String targetString) {
		Pattern SELECTOR = Pattern.compile("^.*\\{", Pattern.MULTILINE);
		Matcher matcher = SELECTOR.matcher(targetString);
		if (matcher.find()) {
			String tmpS = matcher.group();
			return (tmpS.substring(0, tmpS.lastIndexOf("{")).trim());
		}
		return "";
	}

	private String getSelectors(Set<String> target) {
		StringBuffer tmpSB = new StringBuffer();
		for (String tmpS : target) {
			tmpSB.append("\"" + tmpS + "\", ");
		}
		if (tmpSB.length() > 2) {
			tmpSB.setLength(tmpSB.length() - 2);
		}
		return (tmpSB.toString());
	}

	private List<Element> getElementsListByXPath(Document target, String xpath) {
		NodeList tmpNL = xpathService.evalPathForNodeList(xpath, target);
		int length = tmpNL.getLength();
		List<Element> elements = new ArrayList<Element>();
		for (int i = 0; i < length; i++) {
			elements.add((Element) tmpNL.item(i));
		}
		return elements;
	}

	private void prepStyleElements(NodeList styleNL) {
		for (int i = 0; i < styleNL.getLength(); i++) {
			if (styleNL.item(i) instanceof Element) {
				Element e = (Element) styleNL.item(i);
				if (e.hasChildNodes()) {
					Node n = e.getFirstChild();
					String style = n.getNodeValue();

					String[] results = COMMENT.split(style);
					String resultS = "";
					for (String tmpS : results) {
						resultS = resultS + tmpS;
					}
					styleElementMap.put(e, resultS);
				}
			}
		}
	}

	private void prepStyleSheets(IStyleSheets styleSheets) {
		if (styleSheets != null) {
			for (int i = 0; i < styleSheets.getLength(); i++) {
				IStyleSheet ss = styleSheets.item(i);

				try {
					styleSheetsMap.put(ss.getHref(), ss.getCssText());
					prepStyleSheets(ss.getImports());
				} catch (Exception e) {
					String ssHref = ss.getHref();
					// System.out.println(ssHref);

					if (ssHref.startsWith("file://")) {
						try {
							URL ssUrl = new URL(ssHref);
							JapaneseEncodingDetector JED;
							String encoding = "MS932";
							FileInputStream fis = null;
							File target = new File(ssUrl.toURI());
							if (target.isFile() && target.canRead()) {
								try {
									fis = new FileInputStream(target);
									JED = new JapaneseEncodingDetector(fis);
									encoding = JED.detect();
								} catch (Exception e1) {
								} finally {
									if (fis != null) {
										try {
											fis.close();
										} catch (IOException e1) {
										}
									}
								}
							}
							Path file = Paths.get(ssUrl.toURI());
							String tmpS = Files.readString(file, Charset.forName(encoding));
							String[] results = COMMENT.split(tmpS);
							String resultS = "";
							for (String tmpS2 : results) {
								resultS = resultS + tmpS2;
							}
							styleSheetsMap.put(ssHref, resultS);
						} catch (Exception e2) {
						}
					}
				}
			}
		}
	}

}
