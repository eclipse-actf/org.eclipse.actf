/*******************************************************************************
 * Copyright (c) 2004, 2024 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.validation.html.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.actf.model.dom.dombyjs.IStyleSheet;
import org.eclipse.actf.model.dom.dombyjs.IStyleSheets;
import org.eclipse.actf.model.dom.html.DocumentTypeUtil;
import org.eclipse.actf.util.JapaneseEncodingDetector;
import org.eclipse.actf.util.xpath.XPathService;
import org.eclipse.actf.util.xpath.XPathServiceFactory;
import org.eclipse.actf.visualization.engines.blind.TextCheckResult;
import org.eclipse.actf.visualization.engines.blind.TextChecker;
import org.eclipse.actf.visualization.eval.html.HtmlEvalUtil;
import org.eclipse.actf.visualization.eval.html.HtmlTagUtil;
import org.eclipse.actf.visualization.eval.problem.HighlightTargetId;
import org.eclipse.actf.visualization.eval.problem.HighlightTargetNodeInfo;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.eval.problem.ProblemItemImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLImageElement;

import com.ibm.icu.text.MessageFormat;

@SuppressWarnings("unused")
public class CheckEngine extends HtmlTagUtil {

	// use org.w3c.dom.traversal, XPath

	private static final XPathService xpathService = XPathServiceFactory.newService();

	private static final String SPACE_STR = " "; //$NON-NLS-1$

	private static final String WINDOW_OPEN = "window.open"; //$NON-NLS-1$

	public static final int ITEM_COUNT = 1000;

	private static final int QUOTATION_SHORT_NUM = 10;

	private static final int TABLE_CELL_ABBR_CHARS = 30;

	private static final int TABLE_CELL_ABBR_WORDS = 10;

	private static final Pattern COMMENT = Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

	private static final Pattern FIXSIZE_PATTERN = Pattern.compile(
			".*\\{[^\\}]*font-size(\\p{Space})*:[^;v]*(mm|cm|in|pt|pc|px).*\\}.*",
			Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

	private static final Pattern FIXSIZE_PATTERN_ATTR = Pattern
			.compile("font-size(\\p{Space})*:[^;v]*(mm|cm|in|pt|pc|px)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

	private static final Pattern VIEWPORT_PATTERN = Pattern.compile(
			".*\\{[^\\}]*font-size(\\p{Space})*:[^;v]*(vh|vw|vi|vb|vmin|vmax).*\\}.*",
			Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

	private static final Pattern VIEWPORT_PATTERN_ATTR = Pattern
			.compile("font-size(\\p{Space})*:[^;v]*(vh|vw|vi|vb|vmin|vmax)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

	private static final Pattern BLINK_PATTERN = Pattern.compile(
			".*\\{[^\\}]*text-decoration(\\p{Space})*:[^;]*blink.*\\}.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

	private static final Pattern BLINK_PATTERN_ATTR = Pattern.compile("text-decoration(\\p{Space})*:[^;]*blink.*",
			Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

	// not strict check
	private static final String CSS_COLORS = "(aliceblue|antiquewhite|aqua|aquamarine|azure|beige|bisque|black|blanchedalmond|blue|blueviolet|brown|burlywood|cadetblue|chartreuse|chocolate|coral|cornflowerblue|cornsilk|crimson|cyan|darkblue|darkcyan|darkgoldenrod|darkgray|darkgreen|darkgrey|darkkhaki|darkmagenta|darkolivegreen|darkorange|darkorchid|darkred|darksalmon|darkseagreen|darkslateblue|darkslategray|darkslategrey|darkturquoise|darkviolet|deeppink|deepskyblue|dimgray|dimgrey|dodgerblue|firebrick|floralwhite|forestgreen|fuchsia|gainsboro|ghostwhite|gold|goldenrod|gray|green|greenyellow|grey|honeydew|hotpink|indianred|indigo|ivory|khaki|lavender|lavenderblush|lawngreen|lemonchiffon|lightblue|lightcoral|lightcyan|lightgoldenrodyellow|lightgray|lightgreen|lightgrey|lightpink|lightsalmon|lightseagreen|lightskyblue|lightslategray|lightslategrey|lightsteelblue|lightyellow|lime|limegreen|linen|magenta|maroon|mediumaquamarine|mediumblue|mediumorchid|mediumpurple|mediumseagreen|mediumslateblue|mediumspringgreen|mediumturquoise|mediumvioletred|midnightblue|mintcream|mistyrose|moccasin|navajowhite|navy|oldlace|olive|olivedrab|orange|orangered|orchid|palegoldenrod|palegreen|paleturquoise|palevioletred|papayawhip|peachpuff|peru|pink|plum|powderblue|purple|red|rosybrown|royalblue|saddlebrown|salmon|sandybrown|seagreen|seashell|sienna|silver|skyblue|slateblue|slategray|slategrey|snow|springgreen|steelblue|tan|teal|thistle|tomato|turquoise|violet|wheat|white|whitesmoke|yellow|yellowgreen|ActiveBorder|ActiveCaption|AppWorkspace|Background|ButtonFace|ButtonHighlight|ButtonShadow|ButtonText|CaptionText|GrayText|Highlight|HighlightText|InactiveBorder|InactiveCaption|InactiveCaptionText|InfoBackground|InfoText|Menu|MenuText|Scrollbar|ThreeDDarkShadow|ThreeDFace|ThreeDHighlight|ThreeDLightShadow|ThreeDShadow|Window|WindowFrame|WindowText|inherit|currentColor|rgb[a]?\\(.*\\)|hsl[a]?\\(.*\\)|#[a-f0-9]{3}|#[a-f0-9]{6})";

	// TODO media / import / comment <!-- -->
	private static final Pattern STYLEITEM = Pattern.compile(".*?\\{.*?\\}", Pattern.DOTALL);
	private static final Pattern COLOR = Pattern.compile("\\{(.*[^-]+)?color(\\p{Space})*:.*\\}",
			Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	private static final Pattern BGCOLOR = Pattern.compile("\\{.*background-color(\\p{Space})*:.*\\}",
			Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	private static final Pattern BGCOLOR2 = Pattern.compile("\\{.*background(\\p{Space})*:.*" + CSS_COLORS + ".*\\}",
			Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

	private static final Pattern COLOR_ATTR = Pattern.compile("(((.*[^\\-]+)color)|(\\p{Space})*color)(\\p{Space})*:.*",
			Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	private static final Pattern BGCOLOR_ATTR = Pattern.compile(".*background-color(\\p{Space})*:.*",
			Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	private static final Pattern BGCOLOR2_ATTR = Pattern.compile(".*background(\\p{Space})*:.*" + CSS_COLORS + ".*",
			Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

	@SuppressWarnings("nls")
	private static final String[] ASCII_ART_CHAR = { "\u2227", "\uff3f", "\uffe3", "\uff20", "\uff0f", "\uff3c",
			"\u03b3", "\u03a6", "\u2229", "\u222a", "\u03b9", "\uff2f", "\u2282", "\uff9f", "\u0414", "\u03c3",
			"\uff65", "\u2200", "\u2211", "i", "o", "0", "_", "\uff3f", "\uffe3", "\u00b4", "\uff40", "\u30fe" };

	private static Set<String> artCharSet;

	static {
		artCharSet = new HashSet<String>();
		for (int i = 0; i < ASCII_ART_CHAR.length; i++) {
			artCharSet.add(ASCII_ART_CHAR[i]);
		}
	}

	private static Method[] checkMethods;
	private static Method[] mobileCheckMethods;

	static {
		Method[] tmpM = CheckEngine.class.getDeclaredMethods();
		checkMethods = new Method[1000];// TODO
		mobileCheckMethods = new Method[1000];// TODO
		for (Method m : tmpM) {
			String name = m.getName();
			if (name.startsWith("item_")) { //$NON-NLS-1$
				try {
					int itemNum = Integer.parseInt(name.substring(5));
					checkMethods[itemNum] = m;
				} catch (Exception e) {
				}
			} else if (name.startsWith("mobile_")) { //$NON-NLS-1$
				try {
					int itemNum = Integer.parseInt(name.substring(7));
					mobileCheckMethods[itemNum] = m;
				} catch (Exception e) {
				}
			}
		}
	}

	private static final String[] AUDIO_FILE_EXTENSION = { "mp3", "mid", "mrm", "mrl", "vqf", "wav", "ogg", "spx", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			"oga" };

	private static final String[] MULTIMEDIA_FILE_EXTENSION = { "avi", "ram", "rm", "asf", "wm", "wmx", "wmv", "asx", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
			"mpeg", "mpg", "mp4", "ogv", "3gp" }; //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * debug flag for developers!
	 */
	private static final boolean DEBUG = false;

	private Set<String> blockEleSet = HtmlTagUtil.getBlockElementSet();

	private Document targetDoc;

	private Document resultDoc;

	private URL baseUrl;

	private Map<Node, Integer> document2IdMap;

	// private Vector html2ViewMapData;

	private boolean isDBCS;

	private Vector<IProblemItem> result;

	private boolean[] items;

	private int validate_str_len;

	private int valid_total_text_len;

	private boolean hasAwithHref = false;

	private Element[] aWithHref_elements;

	private String[] aWithHref_hrefs;

	private String[] aWithHref_strings;

	private HTMLImageElement[] img_elements;

	private Element[] table_elements;

	private Element[] body_elements;

	private Element[] frame_elements;

	private Element[] iframe_elements;

	private Element[] object_elements;

	private Element[] embed_elements;

	private Element[] parent_table_elements;

	private Element[] bottom_data_tables;

	private Element[] bottom_1row1col_tables;

	private Element[] bottom_notdata_tables;

	private Element[] headings;

	private Element[] custom_elements;

	private double invalidLinkRatio;

	private boolean isXHTML = false;
	private boolean isHTML5 = false;
	private boolean isXML = false;

	private List<Element> labelList;
	private List<Element> formList;
	private Vector<Node> formVwithText;
	private List<Element> layoutTableList;
	private List<Element> dataTableList;
	private TextChecker checker;

	private List<Element> elementsWithStyleList;
	private Map<Element, String> styleElementMap = new HashMap<Element, String>();

	private int liveObject = 0;
	private int liveEmbed = 0;

	// private int invisibleElementCount = 0;
	// private String[] invisibleLinkStrings = new String[0];

	private HtmlEvalUtil edu;

	private String docTypeS;

	private Map<String, String> styleSheetsMap = new HashMap<String, String>();

	private CssBeforeAfterChecker cssBeforeAfterChecker = new CssBeforeAfterChecker();

	/**
	 * 
	 */
	public CheckEngine(HtmlEvalUtil edu, boolean[] checkItems) {
		this.edu = edu;
		this.targetDoc = edu.getTarget();
		this.resultDoc = edu.getResult();

		prepStyleSheets(edu.getStyleSheets());

		Document tmpD = edu.getLiveDom();
		try {
			liveObject = tmpD.getElementsByTagName("object").getLength();
		} catch (Exception e) {
			liveObject = 0;
		}
		try {
			liveEmbed = tmpD.getElementsByTagName("embed").getLength();
		} catch (Exception e) {
			liveEmbed = 0;
		}

		baseUrl = edu.getBaseUrl();

		invalidLinkRatio = 0;

		this.items = checkItems;

		this.document2IdMap = edu.getDocument2IdMap();
		// this.html2ViewMapData = edu.getHtml2ViewMapData();

		this.isDBCS = edu.isDBCS();
		if (isDBCS) {
			validate_str_len = 20;
			valid_total_text_len = 50;
		} else {
			validate_str_len = 50;
			valid_total_text_len = 100;
		}
		result = new Vector<IProblemItem>();

		hasAwithHref = edu.isHasAwithHref();

		aWithHref_elements = edu.getAWithHref_elements();
		aWithHref_hrefs = edu.getAWithHref_hrefs();
		aWithHref_strings = edu.getAWithHref_strings();

		img_elements = edu.getImg_elements();

		table_elements = edu.getTable_elements();
		bottom_data_tables = edu.getBottom_data_tables();
		bottom_1row1col_tables = edu.getBottom_1row1col_tables();
		bottom_notdata_tables = edu.getBottom_notdata_tables();
		parent_table_elements = edu.getParent_table_elements();
		// for new JIS
		layoutTableList = new ArrayList<Element>();
		layoutTableList.addAll(Arrays.asList(parent_table_elements));
		layoutTableList.addAll(Arrays.asList(bottom_1row1col_tables));
		layoutTableList.addAll(Arrays.asList(bottom_notdata_tables));
		dataTableList = new ArrayList<Element>();
		dataTableList.addAll(Arrays.asList(bottom_data_tables));
		assert table_elements.length == layoutTableList.size() + dataTableList.size();

		body_elements = edu.getBody_elements();
		frame_elements = edu.getFrame_elements();
		iframe_elements = edu.getIframe_elements();
		object_elements = edu.getObject_elements();
		embed_elements = edu.getEmbed_elements();

		custom_elements = edu.getCustomElements();

		formList = edu.getElementsList(targetDoc, "form"); //$NON-NLS-1$

		elementsWithStyleList = edu.getElementsWithStyle();
		List<Element> styleElementList = edu.getStyleElements();
		for (Element e : styleElementList) {
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

		headings = edu.getHeadings();

		// use original document's doctype
		DocumentType docType = edu.getSrcDom().getDoctype();
		docTypeS = DocumentTypeUtil.getOriginalID(docType);
		isXHTML = DocumentTypeUtil.isOriginalXHTML(docType);
		isHTML5 = DocumentTypeUtil.isOriginalHTML5(docType);

		// check xml
		Document srcDoc = edu.getSrcDom();
		try {
			Node topN = srcDoc.getFirstChild();
			if (null != topN && topN.getNodeType() == 7 && topN.getNodeName().toLowerCase().startsWith("xml")) {
				isXML = true;
			}
		} catch (Exception e) {

		}
		// System.out.println(docTypeS + " : " + isXHTML + " : " + isHTML5);

		checker = TextChecker.getInstance();

		// System.out.println(GuidelineHolder.getInstance().toString());
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

	/**
	 * Checking main routine. This method invokes each item_*() methods by Java
	 * reflection, based on user's guidelines preference.
	 * 
	 * @return
	 */
	public Vector<IProblemItem> check() {
		// show list of techniques used in all problem items
		/*
		 * if (DEBUG) { if (false) { for (ITechniquesItem tech :
		 * GuidelineHolder.getInstance() .getTechniquesItemSet()) {
		 * System.out.println(tech.getId()); } }
		 * 
		 * Set<ITechniquesItem> techSet = GuidelineHolder.getInstance()
		 * .getTechniquesItemSet(); Map<String, Set<IProblemItem>> techProbMap =
		 * GuidelineHolder .getInstance().getTechProbMap();
		 * 
		 * for (ITechniquesItem tech : techSet) { for (IProblemItem pitem :
		 * techProbMap.get(tech.getId())) { System.out.println(tech.getId() + "\t" +
		 * pitem.getId() + "\t" + pitem.getSeverityStr() + "\t" +
		 * pitem.getDescription()); } }
		 * System.out.println(GuidelineHolder.getInstance()); }
		 */

		// not required to use for WCAG 2.0
		// checkDomDifference();

		validateHtml();

		Object[] tmpO = null;
		for (int i = 0; i < items.length; i++) {
			if (items[i] && null != checkMethods[i]) {
				try {
					checkMethods[i].invoke(this, tmpO);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		// always
		if (items.length < 90 || !items[89]) {
			item_89();
		}

		formCheck();
		styleCheck();
		mediaCheck();
		customElementCheck();
		always();

		for (int i = 0; i < mobileCheckMethods.length; i++) {
			if (null != mobileCheckMethods[i]) {
				try {
					mobileCheckMethods[i].invoke(this, tmpO);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		edu.getPageData().setInvalidLinkRatio(invalidLinkRatio);

		return (result);
	}

	private IProblemItem addCheckerProblem(String strId) {
		IProblemItem tmpCP = new ProblemItemImpl(strId);
		result.add(tmpCP);
		return (tmpCP);
	}

	private IProblemItem addCheckerProblem(String strId, String targetStr) {
		IProblemItem tmpCP = new ProblemItemImpl(strId);
		tmpCP.setTargetString(targetStr);
		result.add(tmpCP);
		return (tmpCP);
	}

	private IProblemItem addCheckerProblem(String strId, Element target) {
		return addCheckerProblem(strId, "", target); //$NON-NLS-1$
	}

	private IProblemItem addCheckerProblem(String strId, String targetStr, Element target) {
		IProblemItem tmpCP = new ProblemItemImpl(strId, target);
		tmpCP.setHighlightTargetNodeInfo(new HighlightTargetNodeInfo(target));
		tmpCP.setTargetString(targetStr);
		result.add(tmpCP);

		return (tmpCP);
	}

	private IProblemItem addCheckerProblem(String strId, Element startTarget, Element endTarget) {
		IProblemItem tmpCP = new ProblemItemImpl(strId, startTarget);
		tmpCP.setHighlightTargetNodeInfo(new HighlightTargetNodeInfo(startTarget, endTarget));
		result.add(tmpCP);
		return (tmpCP);
	}

	private IProblemItem addCheckerProblem(String strId, String targetStr, Vector<Node> targetV) {
		if (targetV.isEmpty()) {
			return null; // do nothing
		}

		IProblemItem tmpCP = new ProblemItemImpl(strId);
		tmpCP.setTargetString(targetStr);
		tmpCP.setHighlightTargetNodeInfo(new HighlightTargetNodeInfo(targetV));
		result.add(tmpCP);
		return tmpCP;
	}

	private IProblemItem addCheckerProblem(String strId, String targetStr, NodeList targetNL) {
		if (targetNL.getLength() == 0) {
			return null; // do nothing
		}

		Vector<Node> targetV = new Vector<Node>();
		for (int i = 0; i < targetNL.getLength(); i++) {
			targetV.add(targetNL.item(i));
		}
		return addCheckerProblem(strId, targetStr, targetV);
	}

	private IProblemItem addCheckerProblem(String strId, String targetStr, List<Element> targetList) {
		if (targetList.isEmpty()) {
			return null; // do nothing
		}

		Vector<Node> targetV = new Vector<Node>();
		for (int i = 0; i < targetList.size(); i++) {
			targetV.add(targetList.get(i));
		}
		return addCheckerProblem(strId, targetStr, targetV);
	}

	private void item_0() {
		for (Element applet : edu.getAppletElements()) {
			boolean bHasAlt = false;
			boolean bHasText = false;

			// justify if has alt attribute

			String strAlt = applet.getAttribute(ATTR_ALT);
			if (strAlt.length() > 0) {
				bHasAlt = true;
			}

			String desText = getTextAltDescendant(applet);
			bHasText = (desText.length() > 0);

			if (!bHasAlt)
				addCheckerProblem("C_0.0", applet); //$NON-NLS-1$
			if (!bHasText) {
				addCheckerProblem("C_0.1", applet); //$NON-NLS-1$

				// alternative link alert
				// check descendant text or image
				// addCheckerProblem("C_0.1", el);
			}
		}
	}

	private void item_1() {

		for (int i = 0; i < object_elements.length; i++) {
			Element el = object_elements[i];

			/*
			 * boolean bReported = false;
			 * 
			 * boolean bHasTextImg = hasTextDescendant(el); if (!bHasTextImg) { NodeList
			 * imgNl = el.getElementsByTagName("img"); //$NON-NLS-1$ if (imgNl.getLength() >
			 * 0) { // need alt description check bHasTextImg = true; } } if (!bHasTextImg)
			 * { // text or img check addCheckerProblem("C_1.1", el); //$NON-NLS-1$
			 * bReported = true; }
			 * 
			 * if (!bReported) { // alert // check links inside an OBJECT element, or a
			 * description link. // addCheckerProblem("C_1.2", el); }
			 */

			if (getTextAltDescendant(el).trim().length() == 0) {
				addCheckerProblem("C_1.1", el); //$NON-NLS-1$
			}
		}

	}

	private void item_2() {

		NodeList nl = targetDoc.getElementsByTagName("input"); //$NON-NLS-1$
		int length = nl.getLength();
		for (int i = 0; i < length; i++) {
			Element el = (Element) nl.item(i);
			String strType = el.getAttribute("type"); //$NON-NLS-1$
			if (strType != null && strType.equalsIgnoreCase("image")) { //$NON-NLS-1$
				String strUsemap;
				boolean bIsMap;
				try {
					bIsMap = ((HTMLImageElement) el).getIsMap();
					strUsemap = ((HTMLImageElement) el).getUseMap();
				} catch (RuntimeException e) {
					bIsMap = false;
					strUsemap = ""; //$NON-NLS-1$
				}
				if (bIsMap || !strUsemap.equals("")) { //$NON-NLS-1$
					// 1.1 server-side image map as submit button alert
					addCheckerProblem("C_2.0", el); //$NON-NLS-1$

					// // 9.1 alert: please use client-side image map
					// addCheckerProblem("C_2.2",el);
				}
			}
		}

		// nl = target.getElementsByTagName("img");
		// length = nl.getLength();
		for (int i = 0; i < img_elements.length; i++) {
			Element el = img_elements[i];
			String strUsemap = ((HTMLImageElement) el).getUseMap();
			if (((HTMLImageElement) el).getIsMap() && strUsemap.equals("")) { //$NON-NLS-1$
				// 1.2 server side image map
				addCheckerProblem("C_2.1", el); //$NON-NLS-1$

				// 9.1 alert: please use client-side image map
				addCheckerProblem("C_2.2", el); //$NON-NLS-1$
			}
		}
	}

	private void item_3() {
		for (Element el : img_elements) {
			if (el.hasAttribute("longdesc")) { //$NON-NLS-1$

				if (isHTML5) {
					addCheckerProblem("C_48.8", "longdesc", el);
				} else {
					String strLongDesc = el.getAttribute("longdesc"); //$NON-NLS-1$
					// original aDesigner routine
					if (strLongDesc.length() > 0) {
						boolean isDlink = false;
						// need to check distance
						int length = aWithHref_hrefs.length;
						for (int j = 0; j < length; j++) {
							// must use URL as in 58?
							if (strLongDesc.equalsIgnoreCase(aWithHref_hrefs[j])) {
								String strValue = aWithHref_strings[j];
								if (strValue.trim().equalsIgnoreCase("d")) { //$NON-NLS-1$
									isDlink = true;
									break;
								}
							}
						}
						if (!isDlink) { // d link check
							addCheckerProblem("C_3.0", el); //$NON-NLS-1$
						}
					}
					// For new JIS
					addCheckerProblem("C_3.1", el); //$NON-NLS-1$
				}
			}
		}
	}

	private void item_4() {
		// need to refine conditions
		Vector<Node> nodeV = new Vector<Node>();
		for (int i = 0; i < img_elements.length; i++) {
			Element el = img_elements[i];
			if (isNormalImage(el)) {
				if (el.hasAttribute(ATTR_ALT)) {
					String strAlt = el.getAttribute(ATTR_ALT);
					if (getWordCount(strAlt) >= 3 || strAlt.length() >= validate_str_len) {
						if (!strAlt.matches("\\p{ASCII}*") //$NON-NLS-1$
								|| strAlt.length() > 30) {
							nodeV.add(el);
						}
					}
				}
			}
		}
		addCheckerProblem("C_4.0", "", nodeV); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private void item_5() {
		int length = aWithHref_elements.length;
		for (int i = 0; i < length; i++) {
			Element el = aWithHref_elements[i];
			String strHref = aWithHref_hrefs[i];
			if (strHref != null && strHref.length() > 0) {
				String strExt = getFileExtension(strHref);
				if (isAudioFileExt(strExt)) {
					// 1.1 audio alert
					addCheckerProblem("C_5.0", el); //$NON-NLS-1$
					break;
				} else if (isMultimediaFileExt(strExt)) {
					// 1.1
					addCheckerProblem("C_5.1", el); //$NON-NLS-1$
					// 1.3
					addCheckerProblem("C_5.2", el); //$NON-NLS-1$
					// 1.4
					addCheckerProblem("C_5.3", el); //$NON-NLS-1$
					break;
				}
			}
		}
	}

	private void item_6() {
		if (body_elements.length > 0) {
			Element bodyEl = body_elements[0];
			Stack<Node> stack = new Stack<Node>();
			Node curNode = bodyEl;

			while (curNode != null) {
				boolean isArtStr = false;
				if (isLeafBlockEle(curNode) && isAsciiArtString(getTextAltDescendant(curNode))) {
					addCheckerProblem("C_6.0", (Element) curNode); //$NON-NLS-1$
					addCheckerProblem("C_6.1", (Element) curNode); //$NON-NLS-1$
					isArtStr = true;
				}

				if (!isArtStr && curNode.hasChildNodes()) {
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
		}
	}

	private void item_7() {
		int aLength = aWithHref_elements.length;
		NodeList nl = targetDoc.getElementsByTagName("area"); //$NON-NLS-1$
		int areaLen = nl.getLength();
		for (int i = 0; i < areaLen; i++) {
			boolean bHasLink = false;
			Element el = (Element) nl.item(i);
			String strHref = ""; //$NON-NLS-1$
			if (el.hasAttribute(HtmlTagUtil.ATTR_HREF)) {
				strHref = el.getAttribute(HtmlTagUtil.ATTR_HREF);
				if (strHref.length() > 0) {
					for (int j = 0; j < aLength; j++) {
						// TODO use URL
						if (strHref.equalsIgnoreCase(aWithHref_hrefs[j])) {
							bHasLink = true;
							break;
						}
					}
				}
			}
			if (!bHasLink) { // link check
				addCheckerProblem("C_7.0", " (href=\"" + strHref + "\")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		}
	}

	private void item_8() {
		// TODO add color info to Description
		NodeList nl = targetDoc.getElementsByTagName("font"); //$NON-NLS-1$
		Vector<Node> nodeV = new Vector<Node>();
		int length = nl.getLength();
		for (int i = 0; i < length; i++) {
			Element el = (Element) nl.item(i);
			if (el.hasAttribute("color") || el.hasAttribute("bgcolor")) { //$NON-NLS-1$ //$NON-NLS-2$
				String strColor, strBgColor;
				strColor = el.getAttribute("color"); //$NON-NLS-1$
				strBgColor = el.getAttribute("bgcolor"); //$NON-NLS-1$
				if (!strColor.equals("") || !strBgColor.equals("")) { //$NON-NLS-1$ //$NON-NLS-2$
					nodeV.add(el);
				}
			}
		}
		addCheckerProblem("C_8.0", "", nodeV); //$NON-NLS-1$ //$NON-NLS-2$

		// need to handle CSS
	}

	private void item_9() {
		// Use markup language alert
		boolean bHasNormalImage = false;
		for (int i = 0; i < img_elements.length; i++) {
			Element el = img_elements[i];
			if (isNormalImage(el)) {
				bHasNormalImage = true;
				break;
			}
		}
		if (bHasNormalImage)
			addCheckerProblem("C_9.0"); //$NON-NLS-1$
	}

	private void item_10() {
		// formal grammars alert
		addCheckerProblem("C_10.0"); //$NON-NLS-1$
	}

	private void item_12() {
		for (int i = 0; i < parent_table_elements.length; i++) {
			Element el = parent_table_elements[i];
			Stack<Node> stack = new Stack<Node>();
			Node curNode = el.getFirstChild();
			int tableCount = 0;
			int maxCount = 0;
			String strName = ""; //$NON-NLS-1$
			while (curNode != null) {
				strName = curNode.getNodeName();
				if (curNode.getNodeType() == Node.ELEMENT_NODE && strName.equalsIgnoreCase("table")) { //$NON-NLS-1$
					tableCount++;
					if (maxCount < tableCount)
						maxCount = tableCount;
				}

				if (curNode.hasChildNodes()) {
					stack.push(curNode);
					curNode = curNode.getFirstChild();
				} else if (curNode.getNextSibling() != null) {
					if ((curNode.getNodeType() == Node.ELEMENT_NODE) && curNode.getNodeName().equals("table")) { //$NON-NLS-1$
						tableCount--;
					}
					curNode = curNode.getNextSibling();
				} else {
					if ((curNode.getNodeType() == Node.ELEMENT_NODE) && curNode.getNodeName().equals("table")) { //$NON-NLS-1$
						tableCount--;
					}
					curNode = null;
					while ((curNode == null) && (stack.size() > 0)) {
						curNode = stack.pop();
						if ((curNode.getNodeType() == Node.ELEMENT_NODE) && curNode.getNodeName().equals("table")) { //$NON-NLS-1$
							tableCount--;
						}
						curNode = curNode.getNextSibling();
					}
				}
			}
			if (maxCount > 0) {
				String str = null;
				if (maxCount == 1) {
					str = Messages.CheckEngine_ChildTable;
				} else {
					str = MessageFormat.format(Messages.CheckEngine_TieredChildTable,
							new Object[] { String.valueOf(maxCount) });
				}
				addCheckerProblem("C_12.0", str, el); //$NON-NLS-1$
			} else
				; // Unreachable code!
		} // end of top-most for loop
		for (int i = 0; i < bottom_1row1col_tables.length; i++) {
			addCheckerProblem("C_12.1", //$NON-NLS-1$
					bottom_1row1col_tables[i]);
		}

		for (int i = 0; i < bottom_notdata_tables.length; i++) {
			addCheckerProblem("C_12.2", //$NON-NLS-1$
					bottom_notdata_tables[i]);
		}
	}

	private void item_13() {
		// font
		NodeList nl = targetDoc.getElementsByTagName("font"); //$NON-NLS-1$
		int length = nl.getLength();
		Vector<Node> nodeV = new Vector<Node>();
		for (int i = 0; i < length; i++) {
			Element el = (Element) nl.item(i);
			if (el.hasAttribute("size")) { //$NON-NLS-1$
				String strSize = el.getAttribute("size"); //$NON-NLS-1$
				if (strSize != null && strSize.length() > 0) {
					if (strSize.indexOf("+") == -1 //$NON-NLS-1$
							&& strSize.indexOf("-") == -1) { // absolute //$NON-NLS-1$
																// size
						nodeV.add(el);
					}
				}
			}
		}
		addCheckerProblem("C_13.0", "", nodeV); //$NON-NLS-1$ //$NON-NLS-2$

		// table
		checkAbsoluteSize("table"); //$NON-NLS-1$
		// tr
		checkAbsoluteSize("tr"); //$NON-NLS-1$
		// td
		checkAbsoluteSize("td"); //$NON-NLS-1$
		// col
		checkAbsoluteSize("col"); //$NON-NLS-1$
		// frames,style sheets?
	}

	private void item_14() {

		int curLevel = 0;
		int lastLevel = 0;
		for (int i = 0; i < headings.length; i++) {
			curLevel = edu.getHeadingLevel(headings[i].getNodeName());
			if (curLevel > 0) {
				if (lastLevel > 0) {
					if (curLevel - lastLevel > 1) {
						// heading level check

						String targetStr = MessageFormat.format(Messages.CheckEngine_Headings,
								new Object[] { curLevel, lastLevel });
						Vector<Node> tmpV = new Vector<Node>();
						tmpV.add(headings[i - 1]);
						tmpV.add(headings[i]);
						addCheckerProblem("C_14.0", targetStr, tmpV); //$NON-NLS-1$
					}
				}
				lastLevel = curLevel;
			}
		}

	}

	private void item_15() {
		Vector<Node> targetV = new Vector<Node>();

		for (int i = 0; i < headings.length; i++) {
			targetV.add(headings[i]);
		}

		addCheckerProblem("C_15.0", "", targetV); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private void item_16() {
		// DL, DT, DD, UL, OL, LI
		// checkListElement("dl");
		// checkListElement("dt");
		// checkListElement("dd");
		// checkListElement("ul");
		// checkListElement("ol");
		// checkListElement("li");
		NodeList nl = targetDoc.getElementsByTagName("ol"); //$NON-NLS-1$
		int length = nl.getLength();
		for (int i = 0; i < length; i++) {
			Element el = (Element) nl.item(i);
			NodeList liNl = el.getElementsByTagName("li"); //$NON-NLS-1$
			if (liNl.getLength() == 0) { // list element alert
				addCheckerProblem("C_16.1", el); //$NON-NLS-1$
			}
		}

		nl = targetDoc.getElementsByTagName("ul"); //$NON-NLS-1$
		length = nl.getLength();
		for (int i = 0; i < length; i++) {
			Element el = (Element) nl.item(i);
			NodeList liNl = el.getElementsByTagName("li"); //$NON-NLS-1$
			if (liNl.getLength() == 0) { // list element alert
				addCheckerProblem("C_16.1", el); //$NON-NLS-1$
			}
		}

		nl = targetDoc.getElementsByTagName("li"); //$NON-NLS-1$
		length = nl.getLength();
		Vector<Node> liVec = new Vector<Node>();
		for (int i = 0; i < length; i++) {
			boolean bHasUlOl = false;
			Node curNode = nl.item(i);
			if (liVec.contains(curNode))
				continue;
			String strName = ""; //$NON-NLS-1$
			while (curNode != null) {
				strName = curNode.getNodeName();
				if (strName.equalsIgnoreCase("ol") //$NON-NLS-1$
						|| strName.equalsIgnoreCase("ul")) { //$NON-NLS-1$
					bHasUlOl = true;
					break;
				} else if (strName.equalsIgnoreCase("body")) { //$NON-NLS-1$
					break;
				}
				curNode = curNode.getParentNode();
			}
			if (!bHasUlOl) {
				Node startNode = nl.item(i);
				Node endNode = nl.item(i);
				liVec.add(nl.item(i));
				Node preNode = nl.item(i).getPreviousSibling();
				while (preNode != null) {
					if (preNode.getNodeName().equalsIgnoreCase("li") //$NON-NLS-1$
							&& !liVec.contains(preNode)) {
						liVec.add(preNode);
						startNode = preNode;
					} else
						break;
					preNode = preNode.getPreviousSibling();
				}
				Node nextNode = nl.item(i).getNextSibling();
				while (nextNode != null) {
					if (nextNode.getNodeName().equalsIgnoreCase("li") //$NON-NLS-1$
							&& !liVec.contains(nextNode)) {
						liVec.add(nextNode);
						endNode = nextNode;
					} else
						break;
					nextNode = nextNode.getNextSibling();
				}
				// list element alert
				addCheckerProblem("C_16.2", (Element) startNode, (Element) endNode); //$NON-NLS-1$
			}
		}

		// dl,dt,dd
		// dir,menu > obsolete
	}

	private void item_17() {
		NodeList nl = targetDoc.getElementsByTagName("q"); //$NON-NLS-1$
		int length = nl.getLength();
		for (int i = 0; i < length; i++) {
			boolean bHasText = false;
			Node qNode = nl.item(i);
			bHasText = hasTextDescendant(qNode);
			if (!bHasText) { // quotation check
				Element el = (Element) qNode;
				addCheckerProblem("C_17.0", el); //$NON-NLS-1$
			}
		} // blockquote
		nl = targetDoc.getElementsByTagName("blockquote"); //$NON-NLS-1$
		length = nl.getLength();
		for (int i = 0; i < length; i++) {
			boolean bHasText = false;
			Node qNode = nl.item(i);
			bHasText = hasTextDescendant(qNode);
			if (!bHasText) { // quotation check
				Element el = (Element) qNode;
				addCheckerProblem("C_17.0", el); //$NON-NLS-1$
			}
		}
		addCheckerProblem("C_17.1", "", nl); //$NON-NLS-1$
	}

	private void item_18() {
		NodeList nl = targetDoc.getElementsByTagName("q"); //$NON-NLS-1$
		int length = nl.getLength();
		for (int i = 0; i < length; i++) {
			Node qNode = nl.item(i);
			String str = getTextDescendant(qNode);
			if (str.length() > QUOTATION_SHORT_NUM) { // longer quotation
				// check
				Element el = (Element) qNode;
				addCheckerProblem("C_18.0", el); //$NON-NLS-1$

				str = el.getAttribute("cite"); //$NON-NLS-1$
				if (str.equals("")) { //$NON-NLS-1$
					// cite attribute check
					addCheckerProblem("C_18.2", el); //$NON-NLS-1$
				}
			}
		}

		// blockquote
		nl = targetDoc.getElementsByTagName("blockquote"); //$NON-NLS-1$
		length = nl.getLength();
		for (int i = 0; i < length; i++) {
			Node qNode = nl.item(i);
			Element el = (Element) qNode;
			String str = getTextDescendant(qNode);
			if (str.length() <= QUOTATION_SHORT_NUM) {
				// short quotation check
				addCheckerProblem("C_18.1", el); //$NON-NLS-1$
			}

			str = el.getAttribute("cite"); //$NON-NLS-1$
			if (str.equals("")) { //$NON-NLS-1$
				// cite attribute check
				addCheckerProblem("C_18.2", el); //$NON-NLS-1$
			}
		}
	}

	private void item_21() {
		Element el = targetDoc.getDocumentElement();
		String attName = isXHTML ? "xml:lang" : "lang";
		String strLang = el.getAttribute(attName);

		if (isEmptyString(strLang) && isHTML5 && isXML) {
			attName = "xml:lang";
			strLang = el.getAttribute(attName);
		}

		if (isEmptyString(strLang)) {
			// no lang attribute
			addCheckerProblem("C_21.0", attName, el); //$NON-NLS-1$
		} else {
			// check conformance to "BCP 47"
			LanguageTag tag = new LanguageTag(strLang, false);

			if (!tag.isWellFormed()) {
				addCheckerProblem("C_21.1", attName, el); //$NON-NLS-1$
			} else {
				// valid language
				addCheckerProblem("C_21.2", attName + "=" + strLang, el); //$NON-NLS-1$
			}
		}
	}

	private void item_22() {
		if (table_elements.length > 0) {
			Vector<Node> tables = new Vector<Node>(Arrays.asList(table_elements));
			addCheckerProblem("C_22.0", null, tables); //$NON-NLS-1$
		}

	}

	private void item_23() { // C_25
		for (int i = 0; i < parent_table_elements.length; i++) {
			boolean bHasTh = false;
			boolean bHasSummary = false;
			boolean bHasCaption = false;
			Element tNode = parent_table_elements[i];

			bHasSummary = (tNode.getAttribute("summary") != null && tNode.getAttribute("summary").length() > 0);
			bHasCaption = tNode.getElementsByTagName("caption").getLength() > 0;

			Stack<Node> stack = new Stack<Node>();
			Node curNode = tNode.getFirstChild();
			while (curNode != null) {
				String strName = ""; //$NON-NLS-1$
				strName = curNode.getNodeName();
				if (strName.equalsIgnoreCase("th")) { //$NON-NLS-1$
					bHasTh = true;
					break;
				}

				if (curNode.hasChildNodes() && !strName.equalsIgnoreCase("table")) { //$NON-NLS-1$
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

			if (bHasTh || bHasCaption || bHasSummary) { // table aleret
				addCheckerProblem("C_23.0", tNode); //$NON-NLS-1$
			}

			// bottom table check(layout / not)
		}

		// C_23.1/2 check. For new JIS
		List<Element> tables = new ArrayList<Element>();
		Vector<Node> table_25_1 = new Vector<Node>();
		Vector<Node> table_25_2 = new Vector<Node>();
		Vector<Node> table_25_3 = new Vector<Node>();
		Vector<Node> table_25_4 = new Vector<Node>();

		for (Element table : dataTableList) {
			boolean added = false;
			if (getDirectDescendantElements(table, "th").size() > 0) {
				tables.add(table);
				added = true;
			}
			if (getDirectDescendantElements(table, "caption").size() > 0) {
				if (!added) {
					tables.add(table);
					added = true;
				}
				table_25_3.add(table);
				// addCheckerProblem("C_25.3", table); //$NON-NLS-1$
			} else {
				String name = getNameByAria(table);
				if (null == name || name.isBlank()) {
					table_25_1.add(table);
				}
			}
			if (isHTML5) {
				if (table.hasAttribute("summary")) {
					addCheckerProblem("C_48.8", "summary", table);
				}
			} else {
				if (table.hasAttribute("summary") && !isEmptyString(table.getAttribute("summary"))) {
					if (!added) {
						tables.add(table);
						added = true;
					}
					table_25_4.add(table);
					// addCheckerProblem("C_25.4", table); //$NON-NLS-1$
				} else {
					table_25_2.add(table);
				}
			}
		}

		addCheckerProblem("C_23.1", "", tables); //$NON-NLS-1$
		addCheckerProblem("C_25.1", null, table_25_1); //$NON-NLS-1$
		addCheckerProblem("C_25.2", null, table_25_2); //$NON-NLS-1$
		addCheckerProblem("C_25.3", null, table_25_3); //$NON-NLS-1$
		addCheckerProblem("C_25.4", null, table_25_4); //$NON-NLS-1$

		tables.clear();

		for (Element table : layoutTableList) {
			if (getDirectDescendantElements(table, "th").size() > 0) {
				tables.add(table);
			} else if (getDirectDescendantElements(table, "caption").size() > 0) {
				tables.add(table);
			} else {
				if (isHTML5 && table.hasAttribute("summary")) {
					addCheckerProblem("C_48.8", "summary", table);
				}
				if (table.hasAttribute("summary") && !isEmptyString(table.getAttribute("summary"))) {
					tables.add(table);
				}
			}
		}

		addCheckerProblem("C_23.2", "", tables); //$NON-NLS-1$
	}

	// private void item_25() {
	// // moved to item_23()!
	// }

	private void item_26() {
		NodeList nl = targetDoc.getElementsByTagName("th"); //$NON-NLS-1$
		int length = nl.getLength();
		for (int i = 0; i < length; i++) {
			Element el = (Element) nl.item(i);
			String str = getTextAltDescendant(el);
			if (isDBCS) {
				if (str.length() > TABLE_CELL_ABBR_CHARS) {
					addCheckerProblem("C_26.0", el); //$NON-NLS-1$
				}
			} else {
				if (getWordCount(str) > TABLE_CELL_ABBR_WORDS) {
					addCheckerProblem("C_26.0", el); //$NON-NLS-1$
				}
			}
		}
	}

	private void item_27() {
		for (int i = 0; i < frame_elements.length; i++) {
			Element el = frame_elements[i];
			String strSrc = el.getAttribute(ATTR_SRC);

			if (!isHtmlFile(strSrc)) { // no-html file check
				addCheckerProblem("C_27.0", "(src=" + strSrc + ")", el); //$NON-NLS-1$
			}
		}

		// nl = target.getElementsByTagName("iframe"); //$NON-NLS-1$
		// length = nl.getLength();
		// for (int i = 0; i < length; i++) {
		// boolean bHasHtml = true;
		// Element el = (Element)nl.item(i);
		// String strSrc = el.getAttribute("src"); //$NON-NLS-1$
		//
		// if (!isHtmlFile(strSrc)) { // no-html file check
		// addCheckerProblem("C_27.1", el); //$NON-NLS-1$
		// }
		// }
	}

	private void item_28() {
		// need to check script/noscript mapping
		int scrSize = edu.getScript_elements().length;
		if (scrSize > 0) {
			NodeList nl = targetDoc.getElementsByTagName("noscript"); //$NON-NLS-1$
			if (scrSize > nl.getLength()) { // noscript check
				addCheckerProblem("C_28.0"); //$NON-NLS-1$
			}
		}
	}

	private void item_29() {
		if (edu.isHasJavascript() || edu.getScript_elements().length > 0) {
			addCheckerProblem("C_29.0"); //$NON-NLS-1$
		}

		for (Element applet : edu.getAppletElements()) {
			addCheckerProblem("C_29.2", //$NON-NLS-1$
					applet);
		}

		for (int i = 0; i < aWithHref_hrefs.length; i++) {
			String str = aWithHref_hrefs[i];
			if (str.toLowerCase().startsWith("javascript:")) { //$NON-NLS-1$
				addCheckerProblem("C_29.1", //$NON-NLS-1$
						" (href=\"" + str + "\")", //$NON-NLS-2$ //$NON-NLS-1$
						aWithHref_elements[i]);
			}
		}
	}

	private void mediaCheck() {
		boolean bHasObject = false;
		Vector<Node> tmpV = new Vector<Node>();
		for (Element e : object_elements) {
			tmpV.add(e);
		}
		for (Element e : embed_elements) {
			tmpV.add(e);
		}
		tmpV.addAll(edu.getAppletElements());
		if (tmpV.size() > 0) {
			addCheckerProblem("C_30.0", "", tmpV); //$NON-NLS-1$
			addCheckerProblem("C_30.1", "", tmpV); //$NON-NLS-1$

			// TODO check if these two items should be shown
			addCheckerProblem("C_500.0", "", tmpV);
			addCheckerProblem("C_500.1", "", tmpV);

			addCheckerProblem("C_600.1", "", tmpV);
			addCheckerProblem("C_600.2", "", tmpV);
			addCheckerProblem("C_600.6", "", tmpV);
			addCheckerProblem("C_600.7", "", tmpV);

			addCheckerProblem("C_600.16", "", tmpV); //$NON-NLS-1$
		}
		if (edu.isHasJavascript()) {
			addCheckerProblem("C_30.1", "(JavaScript)"); //$NON-NLS-1$
		}

		String tmpS = "";
		if (object_elements.length < liveObject) {
			tmpS = tmpS + " object";
		}
		if (object_elements.length < liveObject) {
			tmpS = tmpS + " embed";
		}
		if (tmpS.length() > 0) {
			addCheckerProblem("C_30.0", "(" + Messages.Dynamic + tmpS + ")"); //$NON-NLS-1$
			addCheckerProblem("C_30.1", "(" + Messages.Dynamic + tmpS + ")"); //$NON-NLS-1$

			// TODO check if these two items should be shown
			addCheckerProblem("C_500.0", "(" + Messages.Dynamic + tmpS + ")");
			addCheckerProblem("C_500.1", "(" + Messages.Dynamic + tmpS + ")");

			addCheckerProblem("C_600.1", "(" + Messages.Dynamic + tmpS + ")");
			addCheckerProblem("C_600.2", "(" + Messages.Dynamic + tmpS + ")");
			addCheckerProblem("C_600.6", "(" + Messages.Dynamic + tmpS + ")");
			addCheckerProblem("C_600.7", "(" + Messages.Dynamic + tmpS + ")");

			addCheckerProblem("C_600.16", "(" + Messages.Dynamic + tmpS + ")"); //$NON-NLS-1$
		}

		addCheckerProblem("C_600.20", "MathML", targetDoc.getElementsByTagName("math"));
		addCheckerProblem("C_600.20", "SVG", targetDoc.getElementsByTagName("svg"));

		Vector<Node> autoError = new Vector<Node>();
		Vector<Node> autoUserCheck = new Vector<Node>();

		NodeList tmpNL = targetDoc.getElementsByTagName("video");
		if (tmpNL.getLength() > 0) {
			addCheckerProblem("C_500.0", "", tmpNL);
			addCheckerProblem("C_500.1", "", tmpNL);

			addCheckerProblem("C_600.1", "", tmpNL);
			addCheckerProblem("C_600.2", "", tmpNL);
			addCheckerProblem("C_600.7", "", tmpNL);

			for (int i = 0; i < tmpNL.getLength(); i++) {
				Element e = (Element) tmpNL.item(i);
				if (e.hasAttribute("autoplay")) {
					String autoplay = e.getAttribute("autoplay");
					if (!autoplay.equalsIgnoreCase("false")) {
						autoError.add(e);
					} else {
						autoUserCheck.add(e);
					}
				} else {
					autoUserCheck.add(e);
				}
			}
		}

		tmpNL = targetDoc.getElementsByTagName("audio");
		if (tmpNL.getLength() > 0) {
			addCheckerProblem("C_600.1", "", tmpNL);
			for (int i = 0; i < tmpNL.getLength(); i++) {
				Element e = (Element) tmpNL.item(i);
				if (e.hasAttribute("autoplay")) {
					String autoplay = e.getAttribute("autoplay");
					if (!autoplay.equalsIgnoreCase("false")) {
						autoError.add(e);
					} else {
						autoUserCheck.add(e);
					}
				} else {
					autoUserCheck.add(e);
				}
			}
		}

		addCheckerProblem("C_85.0", "", autoError);
		addCheckerProblem("C_600.6", "", autoUserCheck);

		tmpNL = targetDoc.getElementsByTagName("picture");
		for (int i = 0; i < tmpNL.getLength(); i++) {
			Element pictureE = (Element) tmpNL.item(i);
			if (pictureE.getElementsByTagName("img").getLength() == 0) {
				addCheckerProblem("C_600.21", pictureE);
			}
		}
	}

	private void item_31() {
		NodeList nl = targetDoc.getElementsByTagName("frameset"); //$NON-NLS-1$
		if (nl.getLength() > 0) {
			NodeList noNl = targetDoc.getElementsByTagName("noframes"); //$NON-NLS-1$
			if (noNl.getLength() == 0) { // noframes check
				Element el = (Element) nl.item(0);
				addCheckerProblem("C_31.0", el); //$NON-NLS-1$
			}
		}
	}

	private void item_32() {
		if (object_elements.length > 0) { // object alert
			result.add(new ProblemItemImpl("C_32.0")); //$NON-NLS-1$
		} else {
			if (edu.getAppletElements().size() > 0) { // object alert
				result.add(new ProblemItemImpl("C_32.0")); //$NON-NLS-1$
			}
		}
	}

	private void item_33() {
		NodeList nl = targetDoc.getElementsByTagName("blink"); //$NON-NLS-1$
		int length = nl.getLength();
		for (int i = 0; i < length; i++) {
			boolean bHasText = false;
			Element el = (Element) nl.item(i);
			bHasText = hasTextDescendant(el);
			if (bHasText) { // blink text check
				addCheckerProblem("C_33.0", el); //$NON-NLS-1$
			}
		}

		// TODO move to styleCheck
		for (Element e : elementsWithStyleList) {
			String style = e.getAttribute("style");
			if (BLINK_PATTERN_ATTR.matcher(style).matches()) {
				addCheckerProblem("C_33.1", "style=\"" + style + "\"", e); //$NON-NLS-1$
			}
		}

		Set<Element> styleElementList = styleElementMap.keySet();
		for (Element e : styleElementList) {
			String style = styleElementMap.get(e);
			if (style != null) {
				if (BLINK_PATTERN.matcher(style).matches()) {
					addCheckerProblem("C_33.2", "", e);
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
				if (BLINK_PATTERN.matcher(style).matches()) {
					addCheckerProblem("C_33.2", "(" + ss + ")");
				}
			}
		}

	}

	private void item_34() {
		NodeList nl = targetDoc.getElementsByTagName("marquee"); //$NON-NLS-1$
		int length = nl.getLength();
		for (int i = 0; i < length; i++) {
			Element el = (Element) nl.item(i);
			// marquee check
			addCheckerProblem("C_34.0", el); //$NON-NLS-1$
		}
	}

	private void item_35() {
		Vector<Node> gifImages = new Vector<Node>();

		for (int i = 0; i < img_elements.length; i++) {
			Element el = img_elements[i];
			if (isNormalImage(el) && el.hasAttribute(ATTR_SRC)) {
				String strSrc = el.getAttribute(ATTR_SRC);
				if (strSrc != null && strSrc.length() > 0) {
					String strExt = getFileExtension(strSrc);
					if (strExt.equalsIgnoreCase("gif")) { //$NON-NLS-1$
						// TODO gif image check
						gifImages.add(el);
					}
				}
			}
		}
		addCheckerProblem("C_35.0", "", gifImages);
		// " (src=\"" + strSrc + "\")",

	}

	private void item_36() {
		NodeList nl = targetDoc.getElementsByTagName("meta"); //$NON-NLS-1$
		int length = nl.getLength();
		for (int i = 0; i < length; i++) {
			Element el = (Element) nl.item(i);

			if (el.hasAttribute("http-equiv")) { //$NON-NLS-1$
				String strMeta = el.getAttribute("http-equiv"); //$NON-NLS-1$
				if (strMeta != null && strMeta.equalsIgnoreCase("refresh")) { //$NON-NLS-1$
					String strCon = el.getAttribute("content"); //$NON-NLS-1$
					if (strCon == null || strCon.toLowerCase().indexOf("url") < 0) { //$NON-NLS-1$
						// refresh
						// refresh itself check
						addCheckerProblem("C_36.0", el); //$NON-NLS-1$
					} else {
						// redirect
						Matcher m = Pattern.compile("(\\d+);.*").matcher(strCon);
						if (m.matches() && Integer.parseInt(m.group(1)) > 0)
							addCheckerProblem("C_36.1", el); //$NON-NLS-1$
					}
				}
			}
		}
	}

	private void item_38() {
		Element[] mouseButton = edu.getEventMouseButtonElements();
		Element[] mouseFocus = edu.getEventOnMouseElements();
		// Element[] onKey = edu.getEventOnKeyElements();

		// TODO update message

		HashSet<Element> tmpSet = new HashSet<Element>();
		Vector<Node> nodeV = new Vector<Node>();
		for (int i = 0; i < mouseButton.length; i++) {
			Element el = mouseButton[i];
			tmpSet.add(el);
			if (el.hasAttribute(ATTR_ONKEYDOWN) || el.hasAttribute(ATTR_ONKEYPRESS) || el.hasAttribute(ATTR_ONKEYUP)) {
				// info (confirm)
			} else {
				nodeV.add(el);
			}
		}
		addCheckerProblem("C_38.0", "", nodeV); //$NON-NLS-1$ //$NON-NLS-2$

		nodeV.clear();
		for (int i = 0; i < mouseFocus.length; i++) {
			Element el = mouseFocus[i];
			if (tmpSet.add(el)) {
				if (el.hasAttribute(ATTR_ONFOCUS) || el.hasAttribute(ATTR_ONBLUR) || el.hasAttribute(ATTR_ONSELECT)) {
					// info (confirm)
				} else {
					nodeV.add(el);
				}
			}
		}
		addCheckerProblem("C_38.0", "", nodeV); //$NON-NLS-1$ //$NON-NLS-2$

	}

	private void item_39() {
		boolean bHasTabIndex = false;
		if (body_elements.length > 0) {
			Element bodyEl = body_elements[0];
			Stack<Node> stack = new Stack<Node>();
			Node curNode = bodyEl;
			while (curNode != null) {
				if (curNode.getNodeType() == Node.ELEMENT_NODE) {
					Element el = (Element) curNode;
					if (el.hasAttribute("tabindex")) { //$NON-NLS-1$
						String str = el.getAttribute("tabindex"); //$NON-NLS-1$
						if (!str.equals("")) { //$NON-NLS-1$
							bHasTabIndex = true;
							break;
						}
					}
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
		}
		if (!bHasTabIndex) {
			// tabindex check
			addCheckerProblem("C_39.0"); //$NON-NLS-1$
		}
	}

	private void item_40() {
		boolean bHasAccess = false;
		for (int i = 0; i < aWithHref_elements.length; i++) {
			Element el = aWithHref_elements[i];
			if (el.hasAttribute("accesskey")) { //$NON-NLS-1$
				String str = el.getAttribute("accesskey"); //$NON-NLS-1$
				if (str.length() > 0) {
					bHasAccess = true;
					break;
				}
			}
		}
		if (!bHasAccess) {
			// alert to add keyboard shortcut to frequently used links
			addCheckerProblem("C_40.0"); //$NON-NLS-1$
		}
	}

	private void item_41() {
		boolean bHasAccess = false;
		int length = formList.size();
		if (length == 0)
			return;
		for (Element formEl : formList) {
			Stack<Node> stack = new Stack<Node>();
			Node curNode = formEl.getFirstChild();
			while (curNode != null) {
				if (curNode.getNodeType() == Node.ELEMENT_NODE) {
					Element el = (Element) curNode;
					if (el.hasAttribute("accesskey")) { //$NON-NLS-1$
						String str = el.getAttribute("accesskey"); //$NON-NLS-1$
						if (!str.equals("")) { //$NON-NLS-1$
							bHasAccess = true;
							break;
						}
					}
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

			if (bHasAccess)
				break;
		}
		if (!bHasAccess) {
			// alert to furnish keyboard shortcut for form elements
			addCheckerProblem("C_41.0"); //$NON-NLS-1$
		}

	}

	private void item_42() {
		Vector<Node> anchors = new Vector<Node>();

		for (int i = 0; i < aWithHref_elements.length; i++) {
			Element el = aWithHref_elements[i];
			if (el.hasAttribute("target")) { //$NON-NLS-1$
				String strTarget = el.getAttribute("target"); //$NON-NLS-1$
				if (!strTarget.equals("") //$NON-NLS-1$
						&& !strTarget.equalsIgnoreCase("_top") //$NON-NLS-1$
						&& !strTarget.equalsIgnoreCase("_self") //$NON-NLS-1$
						&& !strTarget.equalsIgnoreCase("top") //$NON-NLS-1$
						&& !strTarget.equalsIgnoreCase("self")) { //$NON-NLS-1$
					// && (strTarget.equalsIgnoreCase("_blank")
					// || strTarget.equalsIgnoreCase("blank")
					// || strTarget.equalsIgnoreCase("_new")
					// || strTarget.equalsIgnoreCase("new")
					// || strTarget.equalsIgnoreCase("_top")
					// || strTarget.equalsIgnoreCase("top"))) {
					// popup new window alert
					anchors.add(el);
				}
			}
		}
		addCheckerProblem("C_42.0", null, anchors); //$NON-NLS-1$
	}

	private void item_43() {

		boolean bHasProblem = false;
		Element[] tmpE = edu.getScript_elements();
		int length = tmpE.length;
		for (int i = 0; i < length; i++) {
			Element el = tmpE[i];
			Node curChild = el.getFirstChild();
			while (curChild != null) {
				if (curChild.getNodeType() == Node.CDATA_SECTION_NODE) {
					String strTxt = curChild.getNodeValue();
					if (strTxt != null && strTxt.toLowerCase().indexOf(WINDOW_OPEN) >= 0) {
						bHasProblem = true;
						break;
					}
				}
				curChild = curChild.getNextSibling();
			}
			if (bHasProblem) {
				result.add(new ProblemItemImpl("C_43.0")); //$NON-NLS-1$
				break;
			}
		}

		HashSet<Element> tmpSet = new HashSet<Element>();
		tmpE = edu.getEventFocusElements();
		for (int i = 0; i < tmpE.length; i++) {
			if (hasOpenWndEvent(tmpE[i], HtmlEvalUtil.EVENT_FOCUS) && tmpSet.add(tmpE[i])) {
				result.add(new ProblemItemImpl("C_43.0", tmpE[i])); //$NON-NLS-1$
			}
		}
		tmpE = edu.getEventLoadElements();
		for (int i = 0; i < tmpE.length; i++) {
			if (hasOpenWndEvent(tmpE[i], HtmlEvalUtil.EVENT_LOAD) && tmpSet.add(tmpE[i])) {
				result.add(new ProblemItemImpl("C_43.0", tmpE[i])); //$NON-NLS-1$
			}
		}
		tmpE = edu.getEventMouseButtonElements();
		for (int i = 0; i < tmpE.length; i++) {
			if (hasOpenWndEvent(tmpE[i], HtmlEvalUtil.EVENT_MOUSE_BUTTON) && tmpSet.add(tmpE[i])) {
				result.add(new ProblemItemImpl("C_43.0", tmpE[i])); //$NON-NLS-1$
			}
		}
		tmpE = edu.getEventOnMouseElements();
		for (int i = 0; i < tmpE.length; i++) {
			if (hasOpenWndEvent(tmpE[i], HtmlEvalUtil.EVENT_MOUSE_FOCUS) && tmpSet.add(tmpE[i])) {
				result.add(new ProblemItemImpl("C_43.0", tmpE[i])); //$NON-NLS-1$
			}
		}
		tmpE = edu.getEventOnKeyElements();
		for (int i = 0; i < tmpE.length; i++) {
			if (hasOpenWndEvent(tmpE[i], HtmlEvalUtil.EVENT_ON_KEY) && tmpSet.add(tmpE[i])) {
				result.add(new ProblemItemImpl("C_43.0", tmpE[i])); //$NON-NLS-1$
			}
		}
		tmpE = edu.getEventWindowElements();
		for (int i = 0; i < tmpE.length; i++) {
			if (hasOpenWndEvent(tmpE[i], HtmlEvalUtil.EVENT_WINDOW) && tmpSet.add(tmpE[i])) {
				result.add(new ProblemItemImpl("C_43.0", tmpE[i])); //$NON-NLS-1$
			}
		}
	}

	private void item_45() {
		// input: text, text area, radio
		NodeList nl = targetDoc.getElementsByTagName("input"); //$NON-NLS-1$
		int length = nl.getLength();
		for (int i = 0; i < length; i++) {
			boolean bHasDefault = false;
			Element el = (Element) nl.item(i);
			if (!el.hasAttribute("type")) //$NON-NLS-1$
				continue;
			String strType = el.getAttribute("type").toLowerCase(); //$NON-NLS-1$
			if (strType.equalsIgnoreCase("text") //$NON-NLS-1$
					|| strType.equalsIgnoreCase("textbox") //$NON-NLS-1$
					|| strType.equalsIgnoreCase("textarea") //$NON-NLS-1$
					|| strType.equals("")) { //$NON-NLS-1$
				String strValue = el.getAttribute("value"); //$NON-NLS-1$
				if (strValue != null && strValue.length() > 0)
					bHasDefault = true;
				else {
					bHasDefault = hasTextDescendant(el);
				}

				if (!bHasDefault) { // default value check
					addCheckerProblem("C_45.0", el); //$NON-NLS-1$
				}
			} else if (strType.equalsIgnoreCase("radio")) { //$NON-NLS-1$
				String strChecked = el.getAttribute("checked"); //$NON-NLS-1$
				if (strChecked != null && !strChecked.equals("")) //$NON-NLS-1$
					bHasDefault = true;
				else {
					if (!el.hasAttribute("name")) //$NON-NLS-1$
						continue;
					String strName1 = el.getAttribute("name"); //$NON-NLS-1$

					for (int j = 0; j < length; j++) {
						if (i == j)
							continue;
						Element el2 = (Element) nl.item(j);
						if (!el2.hasAttribute("type")) //$NON-NLS-1$
							continue;
						String strType2 = el2.getAttribute("type").toLowerCase(); //$NON-NLS-1$

						if (strType2.equalsIgnoreCase("radio")) { //$NON-NLS-1$
							String strName2 = el2.getAttribute("name"); //$NON-NLS-1$
							if (strName2 != null && strName1.equalsIgnoreCase(strName2)) { // this
								// radio
								// group
								// has
								// been
								// checked
								if (j < i) {
									bHasDefault = true;
									break;
								}
								String strChecked2 = el2.getAttribute("checked"); //$NON-NLS-1$
								if (strChecked2 != null && !strChecked2.equals("")) { //$NON-NLS-1$
									bHasDefault = true;
									break;
								}
							}
						}
					}
				}

				if (!bHasDefault) { // default value check
					addCheckerProblem("C_45.1", el); //$NON-NLS-1$
				}
			} else {
				continue;
			}

		}

		// select
		nl = targetDoc.getElementsByTagName("select"); //$NON-NLS-1$
		length = nl.getLength();
		for (int i = 0; i < length; i++) {
			boolean bHasDefault = false;
			Element el = (Element) nl.item(i);
			NodeList opNl = el.getElementsByTagName("option"); //$NON-NLS-1$
			int opLength = opNl.getLength();
			for (int j = 0; j < opLength; j++) {
				String strSelected = ((Element) opNl.item(j)).getAttribute("selected"); //$NON-NLS-1$
				if (strSelected != null) {
					bHasDefault = true;
					break;
				}
			}
			if (!bHasDefault) { // default value check
				addCheckerProblem("C_45.1", el); //$NON-NLS-1$
			}
		} // html:text
		nl = targetDoc.getElementsByTagName("html:text"); //$NON-NLS-1$
		length = nl.getLength();
		for (int i = 0; i < length; i++) {
			boolean bHasDefault = false;
			Element el = (Element) nl.item(i);
			String strValue = el.getAttribute("value"); //$NON-NLS-1$
			if (strValue != null && strValue.length() > 0) {
				bHasDefault = true;
			} else {
				bHasDefault = hasTextDescendant(el);
			}
			if (!bHasDefault) { // default value check
				addCheckerProblem("C_45.0", el); //$NON-NLS-1$
			}
		} // textarea
		nl = targetDoc.getElementsByTagName("textarea"); //$NON-NLS-1$
		length = nl.getLength();
		for (int i = 0; i < length; i++) {
			boolean bHasDefault = false;
			Element el = (Element) nl.item(i);
			String strValue = el.getAttribute("value"); //$NON-NLS-1$
			if (strValue != null && strValue.length() > 0) {
				bHasDefault = true;
			} else {
				bHasDefault = hasTextDescendant(el);
			}
			if (!bHasDefault) { // default value check
				addCheckerProblem("C_45.0", el); //$NON-NLS-1$
			}
		} // html:radio
		nl = targetDoc.getElementsByTagName("html:radio"); //$NON-NLS-1$
		length = nl.getLength();
		for (int i = 0; i < length; i++) {
			boolean bHasDefault = false;
			Element el = (Element) nl.item(i);
			String strName1 = el.getAttribute("name"); //$NON-NLS-1$
			if (strName1 == null)
				continue;
			for (int j = i + 1; i < length; j++) {
				Element el2 = (Element) nl.item(j);
				String strName2 = el2.getAttribute("name"); //$NON-NLS-1$
				if (strName2 != null && strName1.equalsIgnoreCase(strName2)) {
					String strChecked2 = el2.getAttribute("checked"); //$NON-NLS-1$
					if (strChecked2 != null) {
						bHasDefault = true;
						break;
					}
				}
			}
			if (!bHasDefault) { // default value check
				addCheckerProblem("C_45.1", el); //$NON-NLS-1$
			}
		}
	}

	private void item_46() {
		Vector<Element> eleVec = new Vector<Element>();
		for (int i = 0; i < aWithHref_elements.length; i++) {
			boolean bAdjacentLink = false;
			Element el = aWithHref_elements[i];
			if (eleVec.contains(el))
				continue;
			Element endEl = null;
			Node nextNode = el.getNextSibling();
			String url1 = aWithHref_hrefs[i];
			String url2;
			try {
				url1 = new URL(baseUrl, aWithHref_hrefs[i]).toString();
			} catch (MalformedURLException e) {
			}
			while (nextNode != null) {
				if (nextNode.getNodeType() == Node.ELEMENT_NODE) {
					String strName = nextNode.getNodeName();
					if (strName != null) {
						if (strName.equalsIgnoreCase("br") //$NON-NLS-1$
								|| strName.equalsIgnoreCase("p")) { //$NON-NLS-1$
							nextNode = nextNode.getNextSibling();
							continue;
						} else if (strName.equalsIgnoreCase("a")) { //$NON-NLS-1$
							try {
								url2 = new URL(baseUrl, ((Element) nextNode).getAttribute(HtmlTagUtil.ATTR_HREF))
										.toString();
							} catch (MalformedURLException e) {
								url2 = ((Element) nextNode).getAttribute(HtmlTagUtil.ATTR_HREF);
							}
							if (!url1.equals(url2)) {
								endEl = (Element) nextNode;
								if (!eleVec.contains(el))
									eleVec.add(el);
								if (!eleVec.contains(endEl))
									eleVec.add(endEl);
								bAdjacentLink = true;
							} else
								break;
						} else {
							break;
						}
					}
					// break;
				} else if (nextNode.getNodeType() == Node.TEXT_NODE) {
					String strText = nextNode.getNodeValue();
					if (strText != null && !strText.trim().equals("")) //$NON-NLS-1$
						break;
				}
				nextNode = nextNode.getNextSibling();
			}
			if (bAdjacentLink) { // adjacent link check
				addCheckerProblem("C_46.0", el, endEl); //$NON-NLS-1$
			}
		}
	}

	@SuppressWarnings("nls")
	private void item_48() {
		checkObsoluteEle("C_48.0", "menuitem"); //$NON-NLS-1$
		// Applet
		checkObsoluteEle("C_48.1", "applet"); //$NON-NLS-1$
		// BASEFONT, CENTER, FONT, STRIKE, U
		checkObsoluteEle("C_48.2", "basefont"); //$NON-NLS-1$
		checkObsoluteEle("C_48.2", "center"); //$NON-NLS-1$
		checkObsoluteEle("C_48.2", "font"); //$NON-NLS-1$
		// ?
		checkObsoluteEle("C_48.2", "strike"); //$NON-NLS-1$
		checkObsoluteEle("C_48.2", "u"); //$NON-NLS-1$
		// DIR
		checkObsoluteEle("C_48.3", "dir"); //$NON-NLS-1$
		// ISINDEX
		checkObsoluteEle("C_48.4", "isindex"); //$NON-NLS-1$
		// LISTING, PLAINTEXT, XMP
		checkObsoluteEle("C_48.5", "listing"); //$NON-NLS-1$
		checkObsoluteEle("C_48.5", "plaintext"); //$NON-NLS-1$
		checkObsoluteEle("C_48.5", "xmp"); //$NON-NLS-1$
		if (!isHTML5) {
			checkObsoluteEle("C_48.2", "s"); //$NON-NLS-1$
			// em/strong
			checkObsoluteEle("C_48.6", "b"); //$NON-NLS-1$
			checkObsoluteEle("C_48.6", "i"); //$NON-NLS-1$
		}

		if (isHTML5) {
			checkObsoluteEle("C_48.2", "big");
			checkObsoluteEle("C_48.2", "tt");

			checkObsoluteEle("C_48.0", "frame");
			checkObsoluteEle("C_48.0", "frameset");
			checkObsoluteEle("C_48.0", "noframes");
			if (isXML) {
				checkObsoluteEle("C_48.0", "noscript");
			}
			checkObsoluteEle("C_48.7", "acronym");
		} else {
			checkObsoluteEle("C_48.3", "menu"); //$NON-NLS-1$
		}
	}

	private void item_49() {
		// alert about customize experience
		addCheckerProblem("C_49.0"); //$NON-NLS-1$
	}

	private void item_50() {
		// alert about accessible version
		addCheckerProblem("C_50.0"); //$NON-NLS-1$
	}

	/**
	 * Checks for title attributes of frame/iframe elements.
	 */
	private void item_51() {
		for (int i = 0; i < frame_elements.length; i++) {
			Element el = frame_elements[i];
			if (!hasTitle(el)) {
				addCheckerProblem("C_51.0", el); //$NON-NLS-1$
			} else if (hasBlankTitle(el)) {
				addCheckerProblem("C_51.4", el); //$NON-NLS-1$
			} else {
				addCheckerProblem("C_51.2", el.getAttribute(ATTR_TITLE), el); //$NON-NLS-1$
			}
		}

		for (int i = 0; i < iframe_elements.length; i++) {
			Element el = iframe_elements[i];
			if (!hasTitle(el)) {
				addCheckerProblem("C_51.1", //$NON-NLS-1$
						": src=" + el.getAttribute(ATTR_SRC), //$NON-NLS-1$
						el);
			} else if (hasBlankTitle(el)) {
				addCheckerProblem("C_51.5", //$NON-NLS-1$
						": src=" + el.getAttribute(ATTR_SRC), //$NON-NLS-1$
						el);
			} else {
				addCheckerProblem("C_51.3", el.getAttribute(ATTR_TITLE), el); //$NON-NLS-1$
			}
		}
	}

	private void item_52() {
		// add longdesc/title into description
		for (int i = 0; i < frame_elements.length; i++) {
			Element el = frame_elements[i];
			String strTitle = el.getAttribute(ATTR_TITLE);
			if (!strTitle.equals("")) { //$NON-NLS-1$
				String strLongdesc = el.getAttribute("longdesc"); //$NON-NLS-1$
				if (strLongdesc == null || strLongdesc.equals("")) { // alert //$NON-NLS-1$
					// about
					// frame
					// description
					// //$NON-NLS-1$
					addCheckerProblem("C_52.0", el); //$NON-NLS-1$
				}
			}
		}
		// www.cnn.com --target has <iframe but source does not
		for (int i = 0; i < iframe_elements.length; i++) {
			Element el = iframe_elements[i];
			String strTitle = el.getAttribute(ATTR_TITLE);
			if (!strTitle.equals("")) { //$NON-NLS-1$
				String strLongdesc = el.getAttribute("longdesc"); //$NON-NLS-1$
				if (strLongdesc == null || strLongdesc.equals("")) { // alert //$NON-NLS-1$
					// about
					// frame
					// description
					// //$NON-NLS-1$
					addCheckerProblem("C_52.1", el); //$NON-NLS-1$
				}
			}
		}
	}

	private void item_53() {
		NodeList nl = targetDoc.getElementsByTagName("select"); //$NON-NLS-1$
		int length = nl.getLength();
		for (int i = 0; i < length; i++) {
			// alert to group long lists of selections
			Element select = (Element) nl.item(i);

			NodeList optGpNl = select.getElementsByTagName("optgroup"); //$NON-NLS-1$
			if (optGpNl.getLength() == 0) {
				addCheckerProblem("C_53.2", select); //$NON-NLS-1$
				NodeList optList = select.getElementsByTagName("option"); //$NON-NLS-1$
				if (optList.getLength() > 10) {
					addCheckerProblem("C_53.1", select); //$NON-NLS-1$
				}
			}
		}
	}

	/**
	 * Fieldset and legend checking
	 */
	private void item_54() {

		// move into initialization
		// if (formList == null)
		// formList = edu.getElementsList(target, "form"); //$NON-NLS-1$

		Vector<Node> noFieldsetForms = new Vector<Node>();
		Vector<Node> fieldsetForms = new Vector<Node>();
		List<Element> groupListAll = new ArrayList<Element>();
		List<Element> radiogroupListAll = new ArrayList<Element>();

		for (Element form : formList) {
			if (getFormControlNum(form) <= 1)
				continue;
			List<Element> fieldsets = edu.getElementsList(form, "fieldset"); //$NON-NLS-1$
			List<Element> groupList = edu.getElementsListByXPath("descendant::*[@role=\"group\"]", form);
			List<Element> radiogroupList = edu.getElementsListByXPath("descendant::*[@role=\"radiogroup\"]", form);
			if (fieldsets.size() == 0) {
				if (groupList.size() + radiogroupList.size() == 0) {
					noFieldsetForms.add(form);
				}
			} else {
				for (Element fieldset : fieldsets) {
					fieldsetForms.add(fieldset);
					List<Element> legends = edu.getElementsList(fieldset, "legend"); //$NON-NLS-1$
					if (legends.size() == 0) {
						addCheckerProblem("C_54.1", fieldset); //$NON-NLS-1$
					} else {
						for (Element e : legends)
							addCheckerProblem("C_54.4", e); //$NON-NLS-1$
					}
				}
			}
			// C_54.3 check
			FieldsetManager map = new FieldsetManager(getRadioAndCheck(form));

			for (Vector<Node> error : map.getMultipleList()) {
				String target = "";
				try {
					target = ((Element) error.get(0)).getAttribute("name");
				} catch (Exception e) {
				}
				addCheckerProblem("C_54.3", target, error); //$NON-NLS-1$
			}
			for (Vector<Node> missing : map.getMissingList()) {
				String target = "";
				try {
					target = ((Element) missing.get(0)).getAttribute("name");
				} catch (Exception e) {
				}
				if (missing.size() > 1) {
					// warning
					addCheckerProblem("C_54.5", target, missing); //$NON-NLS-1$
				} else {
					// user
					addCheckerProblem("C_54.6", target, missing); //$NON-NLS-1$
				}
			}

		}
		addCheckerProblem("C_54.0", null, noFieldsetForms); //$NON-NLS-1$

		// group/radiogroup are covered by 703.8,9
		addCheckerProblem("C_54.2", null, fieldsetForms); //$NON-NLS-1$

	}

	@SuppressWarnings("nls")
	private void item_57() {
		// need more detailed check
		Element el = null;
		int errorCount = 0;
		int exceptCount = 0;

		int length = aWithHref_elements.length;
		Vector<Node> item57V = new Vector<Node>();
		Vector<Node> linkTitle = new Vector<Node>();
		HashMap<String, Vector<Node>> emptyMap = new HashMap<>();

		for (int i = 0; i < length; i++) {
			el = aWithHref_elements[i];

			String strTxt = aWithHref_strings[i];
			if (strTxt.trim().length() == 0) {
				// consider aria-label, aria-labelledby (TBD handling order)
				String tmpS = getNameByAria(el);
				if (null != tmpS) {
					strTxt = tmpS.trim();
				}
			}

			if (el.hasAttribute(ATTR_TITLE)) {
				if (hasBlankTitle(el))
					addCheckerProblem("C_57.3", el); //$NON-NLS-1$
				else
					linkTitle.add(el);
			}

			if (getWordCount(strTxt) < 3 && strTxt.length() < validate_str_len) {
				String strTitle = el.getAttribute(ATTR_TITLE);
				if (strTitle.equals("")) { //$NON-NLS-1$
					if (strTxt.trim().length() > 0) {
						item57V.add(el);
					} else {
						// can't use link

						if (!aWithHref_hrefs[i].startsWith("#")) { //$NON-NLS-1$

							String noScriptText = getNoScriptText(el);

							if ((!el.hasChildNodes() && el.getElementsByTagName("img").getLength() == 0)) { //$NON-NLS-1$
								//ToDo check conditions (duplication)
								exceptCount++;
								if (emptyMap.containsKey(aWithHref_hrefs[i])) {
									emptyMap.get(aWithHref_hrefs[i]).add(el);
								} else {
									Vector<Node> tmpEmptyV = new Vector<>();
									tmpEmptyV.add(el);
									emptyMap.put(aWithHref_hrefs[i], tmpEmptyV);
								}

							} else if (noScriptText.length() > 0) {
								// script + noscript(Text)
								exceptCount++;
								// alert
							} else {
								errorCount++;
								String current = aWithHref_hrefs[i];
								boolean sequenceOk = false;
								if (i - 1 > 0) {
									if (current.equals(aWithHref_hrefs[i - 1])
											&& aWithHref_strings[i - 1].length() > 0) {
										sequenceOk = true;
									}
								}
								if (!sequenceOk && i + 1 < aWithHref_hrefs.length) {
									if (current.equals(aWithHref_hrefs[i + 1])
											&& aWithHref_strings[i + 1].length() > 0) {
										sequenceOk = true;
									}
								}

								if (sequenceOk) {
									// USER
									IProblemItem tmpCP = addCheckerProblem("C_57.5", //$NON-NLS-1$
											" (href=\"" + aWithHref_hrefs[i] //$NON-NLS-1$
													+ "\")", //$NON-NLS-1$
											el);
									edu.appendErrorIcon(tmpCP, el);
								} else {
									// WARN
									IProblemItem tmpCP = addCheckerProblem("C_57.2", //$NON-NLS-1$
											" (href=\"" + aWithHref_hrefs[i] //$NON-NLS-1$
													+ "\")", //$NON-NLS-1$
											el);
									edu.appendErrorIcon(tmpCP, el);
								}
							}
						} else {
							// need to count # (intra page link)
							errorCount++;
						}
					}
				} else {
					if (getWordCount(strTitle) < 3 && strTitle.length() < validate_str_len) {
						// link title check
						// show always?
						addCheckerProblem("C_57.1", //$NON-NLS-1$
								" (link text=\"" //$NON-NLS-1$
										+ strTxt + "\", title=\"" //$NON-NLS-1$
										+ strTitle + "\", href=\"" //$NON-NLS-1$
										+ aWithHref_hrefs[i] + "\")", //$NON-NLS-1$
								el);
					}
				}
			}
		}
		addCheckerProblem("C_57.0", "", item57V); //$NON-NLS-1$ //$NON-NLS-2$
		/*
		 * if (linkTitle.size() > 0) addCheckerProblem("C_57.4", "", linkTitle);
		 * //$NON-NLS-1$ //$NON-NLS-2$
		 */
		for (Node link : linkTitle) {
			addCheckerProblem("C_57.4", ((Element) link).getAttribute(ATTR_TITLE), (Element) link);
		}

		for(String hrefS : emptyMap.keySet()) {
			addCheckerProblem("C_57.6", hrefS, emptyMap.get(hrefS));
		}
			
		// need URL check

		length = length - exceptCount;

		if (length > 0) {
			invalidLinkRatio = (double) errorCount / (double) length;
			// System.out.println(invalidLinkRatio);
		}

	}

	private void item_58() {

		int[] countStrLen = { 0, 0, 0, 0, 0 };
		for (int i = 0; i < aWithHref_elements.length; i++) {
			// strArray[i] = getTextAltDescendant(nl.item(i));
			int length = aWithHref_strings[i].length();

			if (length == 0) {
				continue;
			}

			if (length > 9) {
				if (length < 20) {
					countStrLen[3]++;
				} else {
					countStrLen[4]++;
				}
			} else if (length < 7) {
				if (length < 4)
					countStrLen[0]++;
				else
					countStrLen[1]++;
			} else {
				countStrLen[2]++;
			}
		}

		// 0 (exclusion)
		// length 1-3
		// length 4-6
		// length 7-9
		// length 10-20
		// length 20-

		int[][] strHash = new int[5][];
		Node[][] nodeArray = new Node[5][];
		String[][] strArray = new String[5][];
		String[][] urlArray = new String[5][];
		boolean[][] bDuplicate = new boolean[5][];
		for (int i = 0; i < 5; i++) {
			strHash[i] = new int[countStrLen[i]];
			bDuplicate[i] = new boolean[countStrLen[i]];
			nodeArray[i] = new Node[countStrLen[i]];
			strArray[i] = new String[countStrLen[i]];
			urlArray[i] = new String[countStrLen[i]];
			// System.out.print(countStrLen[i] + " ");
			countStrLen[i] = 0;
		}
		// System.out.println();

		int nlLength = aWithHref_elements.length;
		for (int i = 0; i < nlLength; i++) {
			int length = aWithHref_strings[i].length();
			if (length == 0)
				continue;
			if (length > 9) {
				if (length < 20) {
					strHash[3][countStrLen[3]] = aWithHref_strings[i].hashCode();
					nodeArray[3][countStrLen[3]] = aWithHref_elements[i];
					strArray[3][countStrLen[3]] = aWithHref_strings[i];
					urlArray[3][countStrLen[3]] = aWithHref_hrefs[i];
					countStrLen[3]++;
				} else {
					strHash[4][countStrLen[4]] = aWithHref_strings[i].hashCode();
					nodeArray[4][countStrLen[4]] = aWithHref_elements[i];
					strArray[4][countStrLen[4]] = aWithHref_strings[i];
					urlArray[4][countStrLen[4]] = aWithHref_hrefs[i];
					countStrLen[4]++;
				}
			} else if (length < 7) {
				if (length < 4) {
					strHash[0][countStrLen[0]] = aWithHref_strings[i].hashCode();
					nodeArray[0][countStrLen[0]] = aWithHref_elements[i];
					strArray[0][countStrLen[0]] = aWithHref_strings[i];
					urlArray[0][countStrLen[0]] = aWithHref_hrefs[i];
					countStrLen[0]++;
				} else {
					strHash[1][countStrLen[1]] = aWithHref_strings[i].hashCode();
					nodeArray[1][countStrLen[1]] = aWithHref_elements[i];
					strArray[1][countStrLen[1]] = aWithHref_strings[i];
					urlArray[1][countStrLen[1]] = aWithHref_hrefs[i];
					countStrLen[1]++;
				}
			} else {
				strHash[2][countStrLen[2]] = aWithHref_strings[i].hashCode();
				nodeArray[2][countStrLen[2]] = aWithHref_elements[i];
				strArray[2][countStrLen[2]] = aWithHref_strings[i];
				urlArray[2][countStrLen[2]] = aWithHref_hrefs[i];
				countStrLen[2]++;
			}
		}

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < strHash[i].length - 1; j++) {
				if (bDuplicate[i][j])
					continue;
				Vector<Node> idVec = new Vector<Node>();

				for (int k = j + 1; k < strHash[i].length; k++) {
					if (bDuplicate[i][k])
						continue;
					if (strHash[i][j] == strHash[i][k]) {

						String url1 = urlArray[i][j];
						try {
							url1 = new URL(baseUrl, url1).toString();
						} catch (MalformedURLException e) {
							// e.printStackTrace();
						}

						String url2 = urlArray[i][k];
						try {
							url2 = new URL(baseUrl, url2).toString();
						} catch (MalformedURLException e1) {
							// e1.printStackTrace();
						}

						// System.out.println(url1 + " " + url2);
						if (!url1.equalsIgnoreCase(url2)) {
							if (!bDuplicate[i][j]) {
								idVec.add(nodeArray[i][j]);
								bDuplicate[i][j] = true;
							}
							idVec.add(nodeArray[i][k]);
							bDuplicate[i][k] = true;
						}
					}

				}
				addCheckerProblem("C_58.0", " (link text=\"" + strArray[i][j] + "\")", idVec); //$NON-NLS-3$
			}
		}

	}

	private void item_59() {

		// 59.0 was identical with 68

		boolean bHasKeyWdDesc = false;
		NodeList nl = targetDoc.getElementsByTagName("meta"); //$NON-NLS-1$
		int length = nl.getLength();
		for (int i = 0; i < length; i++) {
			Element el = (Element) nl.item(i);
			String strName = el.getAttribute("name"); //$NON-NLS-1$
			if (strName != null && strName.equalsIgnoreCase("keywords")) //$NON-NLS-1$
				bHasKeyWdDesc = true;
			else if (strName != null && strName.equalsIgnoreCase("description")) //$NON-NLS-1$
				bHasKeyWdDesc = true;
		}
		if (!bHasKeyWdDesc) {
			// Use the keywords
			addCheckerProblem("C_59.1"); //$NON-NLS-1$
		}

		nl = targetDoc.getElementsByTagName("address"); //$NON-NLS-1$
		if (nl.getLength() == 0) {
			// Use the ADDRESS element
			addCheckerProblem("C_59.2"); //$NON-NLS-1$
		}

		// Use the Resource Description Framework (RDF) in the header of your
		// document
		addCheckerProblem("C_59.3"); //$NON-NLS-1$
	}

	private void item_60() {
		boolean bHasTitle = false;
		Element firstTitle = null;
		NodeList nl = targetDoc.getElementsByTagName("head"); //$NON-NLS-1$
		if (nl.getLength() > 0) {
			NodeList hdNl = ((Element) nl.item(0)).getElementsByTagName(ATTR_TITLE);
			int length = hdNl.getLength();
			for (int i = 0; i < length; i++) {
				Element titleEl = (Element) hdNl.item(i);
				if (firstTitle == null)
					firstTitle = titleEl;
				bHasTitle = hasTextDescendant(titleEl);
			}
		}
		if (!bHasTitle) { // document title check
			result.add(new ProblemItemImpl("C_60.0")); //$NON-NLS-1$
		} else {
			List<String> ngPatterns = new ArrayList<String>();
			ngPatterns.add("\u7121\u984c.*");
			ngPatterns.add(".*untitled.*");
			ngPatterns.add(".*no title.*");
			ngPatterns.add(".*\\.html?");
			ngPatterns.add("[\\p{Punct}\\d]+");
			String title = getTextDescendant(firstTitle).toLowerCase().trim();

			for (String pattern : ngPatterns) {
				if (title.matches(pattern)) {
					addCheckerProblem("C_60.1", title, firstTitle);
					break;
				}
			}

		}
	}

	private void item_66() {
		// need to check descendant text (or href txt) contains "search","go",
		// etc.
		// exist text box / text area

		for (Element formE : formList) {
			// alert if there are different types of searches
			addCheckerProblem("C_66.0", formE); //$NON-NLS-1$
		}
	}

	private void item_68() {
		boolean bHasRel = false;
		NodeList nl = targetDoc.getElementsByTagName("link"); //$NON-NLS-1$
		int length = nl.getLength();
		for (int i = 0; i < length; i++) {
			Element el = (Element) nl.item(i);
			String strRel = el.getAttribute("rel"); //$NON-NLS-1$
			if (strRel != null && !strRel.equals("") //$NON-NLS-1$
					&& !strRel.equalsIgnoreCase("stylesheet")) { //$NON-NLS-1$
				bHasRel = true;
				break;
			} else {
				strRel = el.getAttribute("rev"); //$NON-NLS-1$
				if (strRel != null && !strRel.equals("")) { //$NON-NLS-1$
					bHasRel = true;
					break;
				}
			}
		}
		if (!bHasRel) { // alert about identifying document location
			addCheckerProblem("C_68.0"); //$NON-NLS-1$
		}

		// //alert to use the Resource Description Framework (RDF) in the header
		// result.add(new CheckerProblem("68.1"));
	}

	private void item_69() {
		if (body_elements.length > 0) {
			Element bodyEl = body_elements[0];
			Stack<Node> stack = new Stack<Node>();
			Node curNode = bodyEl;

			while (curNode != null) {
				// TODO have means to skip over?
				boolean isArtStr = false;
				if (isLeafBlockEle(curNode) && isAsciiArtString(getTextAltDescendant(curNode))) {
					addCheckerProblem("C_69.0", (Element) curNode); //$NON-NLS-1$
					isArtStr = true;
				}

				if (!isArtStr && curNode.hasChildNodes()) {
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
		}
	}

	private void item_73() {
		Vector<Node> nodeV = new Vector<Node>();
		for (int i = 0; i < object_elements.length; i++) {
			Element el = object_elements[i];
			String str = el.getAttribute("data"); //$NON-NLS-1$
			str = getFileExtension(str);
			if (str.equalsIgnoreCase("pdf") //$NON-NLS-1$
					|| str.equalsIgnoreCase("ppt") //$NON-NLS-1$
					|| isAudioFileExt(str) || isMultimediaFileExt(str)) {
				nodeV.add(el);
			}
		}
		for (int i = 0; i < aWithHref_hrefs.length; i++) {
			Element el = aWithHref_elements[i];
			String str = aWithHref_hrefs[i];
			str = getFileExtension(str);
			if (str.equalsIgnoreCase("pdf") //$NON-NLS-1$
					|| str.equalsIgnoreCase("ppt") //$NON-NLS-1$
					|| isAudioFileExt(str) || isMultimediaFileExt(str)) {
				nodeV.add(el);
			}
		}

		for (Element applet : edu.getAppletElements()) {
			NodeList parNl = applet.getElementsByTagName("param"); //$NON-NLS-1$
			int parLength = parNl.getLength();
			for (int j = 0; j < parLength; j++) {
				String str = ((Element) parNl.item(j)).getAttribute("value"); //$NON-NLS-1$
				str = getFileExtension(str);
				if (str.equalsIgnoreCase("pdf") //$NON-NLS-1$
						|| str.equalsIgnoreCase("ppt") //$NON-NLS-1$
						|| isAudioFileExt(str) || isMultimediaFileExt(str)) {
					nodeV.add(applet);
				}
			}
		}
		addCheckerProblem("C_73.0", "", nodeV); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private void item_74() {
		boolean hasHeader = false;
		NodeList nl = targetDoc.getElementsByTagName("meta"); //$NON-NLS-1$
		int length = nl.getLength();
		for (int i = 0; i < length; i++) {
			Element el = (Element) nl.item(i);
			String strMeta = el.getAttribute("http-equiv"); //$NON-NLS-1$
			if (strMeta.equalsIgnoreCase("refresh") //$NON-NLS-1$
					|| strMeta.equalsIgnoreCase("location")) { //$NON-NLS-1$
				addCheckerProblem("C_74.1", el); //$NON-NLS-1$
				hasHeader = true;
			}
		}
		// timeout process alert
		if (!hasHeader) {
			addCheckerProblem("C_74.0"); //$NON-NLS-1$
		}

		if (formList.size() > 0) {
			addCheckerProblem("C_74.2"); //$NON-NLS-1$
		}
	}

	private void item_75() {
		for (int i = 0; i < bottom_data_tables.length; i++) {
			Element tNode = bottom_data_tables[i];
			NodeList nl = tNode.getElementsByTagName("th"); //$NON-NLS-1$
			if (nl.getLength() == 0) {
				// table header check
				addCheckerProblem("C_75.0", tNode); //$NON-NLS-1$
			}
		}
	}

	private void item_76() {
		Vector<Node> rowColTables = new Vector<Node>();

		for (Element el : dataTableList) {
			// duplicated with C_331
			// int thNum = 0;
			// boolean hasScopeAxis = false;
			// for (Element tr : edu.getElementsList(el, "tr")) { //$NON-NLS-1$
			// List<Element> cells = edu.getElementsList(tr, "th");
			// //$NON-NLS-1$
			// if (cells.size() > 0)
			// thNum++;
			// cells.addAll(edu.getElementsList(tr, "td"));
			// for (Element cell : cells) {
			// if (getAttribute(cell, "scope") != null
			// || getAttribute(cell, "axis") != null) {
			// hasScopeAxis = true;
			// break;
			// }
			// }
			// if (hasScopeAxis)
			// break;
			// }
			// if (!hasScopeAxis && thNum > 1) {
			// // TODO check table header structure
			// addCheckerProblem("C_76.0", el); //$NON-NLS-1$
			// }
			//

			if (hasRowColSpan(el)) {
				// the table has rowspan and/or colspan
				rowColTables.add(el);
			}
		}
		addCheckerProblem("C_76.1", "", rowColTables);
	}

	/**
	 * Returns true if the specified table has any td or th elements which has
	 * rowspan and/or colspan attribute. for new JIS.
	 * 
	 * @param table a leaf table element
	 * @return a boolean value that indicates if the specified table has any td or
	 *         th elements which has rowspan and/or colspan attribute.
	 */
	private boolean hasRowColSpan(Element table) {
		boolean bHasRowColSpan = false;

		for (Element cell : edu.getElementsList(table, "th", "td")) {
			int row = 0;
			int col = 0;
			try {
				row = Integer.parseInt(getAttribute(cell, "rowspan"));
			} catch (Exception e) {
			}
			try {
				col = Integer.parseInt(getAttribute(cell, "colspan"));
			} catch (Exception e) {
			}

			if ((row > 1) || (col > 1)) {
				bHasRowColSpan = true;
				break;
			}
		}

		return bHasRowColSpan;
	}

	private void item_77() {
		Element el = null;
		boolean bHasCSS = false;
		NodeList nl = targetDoc.getElementsByTagName("link"); //$NON-NLS-1$
		int length = nl.getLength();
		for (int i = 0; i < length; i++) {
			el = (Element) nl.item(i);
			String strRel = el.getAttribute("rel"); //$NON-NLS-1$
			if (strRel != null && strRel.equalsIgnoreCase("stylesheet")) { //$NON-NLS-1$
				bHasCSS = true;
				break;
			}
		}
		if (bHasCSS) { // alert about style sheet
			addCheckerProblem("C_77.0", //$NON-NLS-1$
					// AdditionalDescription.getString("CheckEngine._(link_tag_for_include_stylesheet)_492"),
					// //$NON-NLS-1$
					el);
			// multi?
		}

		nl = targetDoc.getElementsByTagName("style"); //$NON-NLS-1$
		addCheckerProblem("C_77.1", "", nl); //$NON-NLS-1$ //$NON-NLS-2$

		Vector<Node> nodeV = new Vector<Node>();

		if (body_elements.length > 0) {
			Element bodyEl = body_elements[0];
			Stack<Node> stack = new Stack<Node>();
			Node curNode = bodyEl;
			while (curNode != null) {
				if (curNode.getNodeType() == Node.ELEMENT_NODE) {
					el = (Element) curNode;
					String strAlt = el.getAttribute("style"); //$NON-NLS-1$
					if (!strAlt.equals("")) { //$NON-NLS-1$
						// alert about style
						nodeV.add(el);
					}
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
		}
		addCheckerProblem("C_77.2", "", nodeV); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@SuppressWarnings("nls")
	private void item_78() {
		Vector<Node> textInputs = new Vector<Node>();
		NodeList nl = targetDoc.getElementsByTagName("input");
		int length = nl.getLength();
		for (int i = 0; i < length; i++) {
			Element el = (Element) nl.item(i);
			if (!el.hasAttribute("type"))
				continue;
			String strType = el.getAttribute("type").toLowerCase();
			if (strType.equalsIgnoreCase("text") || strType.equalsIgnoreCase("textbox") || strType.equals("")) {
				textInputs.add(el);
			}
		}

		nl = targetDoc.getElementsByTagName("html:text");
		length = nl.getLength();
		for (int i = 0; i < length; i++) {
			textInputs.add((Element) nl.item(i));
		}
		addCheckerProblem("C_78.2", null, textInputs);
	}

	/**
	 * Checks labels and titles for form controls
	 */
	private void item_79() {
		if (labelList == null)
			labelList = edu.getElementsList(targetDoc, "label"); //$NON-NLS-1$
		Vector<Node> noLabelTitleControls = new Vector<Node>();
		Vector<Node> noLabelTitleControlsNew = new Vector<Node>();
		Vector<Node> noLabelEmptyTitleControls = new Vector<Node>();
		Vector<Node> noLabelEmptyTitleControlsNew = new Vector<Node>();
		Vector<Node> labeledControls = new Vector<Node>();

		for (Element body : body_elements) { // $NON-NLS-1$
			for (Element el : getFormControl(body)) {

				Element label = null;

				if ((label = getLabel(el)) != null) {
					item_79_label(el, label);
				} else if ((label = hasImplicitLabel(el)) != null) {
					// TODO check label place
					Vector<Node> target = new Vector<Node>();
					target.add(el);
					target.add(label);
					addCheckerProblem("C_79.5", "", target);
				} else {
					// aria-label / aria-labelledby
					String name = getNameByAria(el);
					if (name == null || name.isBlank()) {
						// check title for each input controls that has no label
						if (isLabelable(getFormControlType(el))) {
							TitleCheckResult res = item_79_title(el);
							if (res == TitleCheckResult.NO_TITLE) {
								if (isInH44(el)) {
									noLabelTitleControls.add(el);
								} else {
									noLabelTitleControlsNew.add(el);
								}
							} else if (res == TitleCheckResult.EMPTY_TITLE) {
								if (isInH44(label)) {
									noLabelEmptyTitleControls.add(el);
								} else {
									noLabelEmptyTitleControlsNew.add(el);
								}
							} else if (res == TitleCheckResult.OK) {
								addCheckerProblem("C_79.4", el.getAttribute(ATTR_TITLE), el);
							}
						}
					}
				}
			}
		}

		addCheckerProblem("C_79.6", "", noLabelTitleControls);
		addCheckerProblem("C_79.8", "", noLabelTitleControlsNew);
		addCheckerProblem("C_79.0", "", noLabelEmptyTitleControls);
		addCheckerProblem("C_79.9", "", noLabelEmptyTitleControlsNew);
	}

	private boolean isInH44(Element el) {
		if ("input".equals(el.getNodeName())) {
			String strType = el.getAttribute("type").toLowerCase();
			if (strType.equals("") // default is text? //$NON-NLS-1$
					|| strType.equals("text") //$NON-NLS-1$
					|| strType.equals("textarea") //$NON-NLS-1$
					|| strType.equals("radio") //$NON-NLS-1$
					|| strType.equals("checkbox") //$NON-NLS-1$
					|| strType.equals("file") //$NON-NLS-1$ // For new JIS
					|| strType.equals("password")) { //$NON-NLS-1$
				return true;
			}
			return false;
		}
		return true;
	}

	private void item_79_label(Element ctrl, Element label) {
		String elType = getFormControlType(ctrl);
		// TODO highlight the label as well

		Vector<Node> target = new Vector<Node>();
		target.add(ctrl);
		target.add(label);

		if (!hasProperLabel(ctrl)) {
			// in case of TYPE was removed by IE
			addCheckerProblem("C_79.1", //$NON-NLS-1$
					" (input type: " + (elType.equals("") ? "text" : elType) + ")", //$NON-NLS-1$ //$NON-NLS-2$
					target);
		} else {
			// H44 OK
			addCheckerProblem("C_79.5", "", target); //$NON-NLS-1$
		}
	}

	/**
	 * If a control passes check, it returns true.
	 * 
	 * @param ctrl
	 * @return
	 */
	private TitleCheckResult item_79_title(Element ctrl) {
		if (hasTitle(ctrl)) {
			if (hasBlankTitle(ctrl)) {
				return TitleCheckResult.EMPTY_TITLE;
			}
			return TitleCheckResult.OK;
		} else {
			if (check_G167(ctrl)) {
				return TitleCheckResult.G167;
			} else {
				return TitleCheckResult.NO_TITLE;
			}
		}
	}

	/**
	 * Looks for an adjacent button to a text. If such text field is found, this
	 * method returns true, otherwise false.
	 * 
	 * @param ctrl a form control element
	 */
	private boolean check_G167(Element ctrl) {
		if (HtmlTagUtil.isTextControl(ctrl)) {
			// looks for next control
			Element next = findNextElementNode(ctrl);
			if (next == null || !isButtonControl(next)) {
				return false;
			} else {
				Vector<Node> ctrls = new Vector<Node>();
				ctrls.add(ctrl);
				ctrls.add(next);
				addCheckerProblem("C_79.7", null, ctrls);
				return true;
			}
		} else if (HtmlTagUtil.isButtonControl(ctrl)) {
			// does not occur
			return false;
		} else
			return false;
	}

	/**
	 * Finds most close element node that comes after the given element.
	 * 
	 * @param el
	 * @return the next element node if any. returns null if no such element is
	 *         found.
	 */
	private Element findNextElementNode(Element el) {
		Node current = el;
		while ((current = current.getNextSibling()) != null) {
			short t = current.getNodeType();
			if (current.getNodeType() == Node.TEXT_NODE) {
				if (!isBlankString(current.getNodeValue()))
					return null;
			} else if (current.getNodeType() == Node.ELEMENT_NODE)
				break;
			else {
				// skips other node types (CDATA?)
				// CDATA section treated as comment node
				;
			}
		}
		return (Element) current;
	}

	/**
	 * Checks whether any label attribute exists in controls temporally implemented
	 * as separated method. For new JIS
	 */
	/*
	 * private void item_79_label() { NodeList nl =
	 * target.getElementsByTagName("form"); //$NON-NLS-1$ int length =
	 * nl.getLength(); NodeList labelNl = target.getElementsByTagName("label");
	 * //$NON-NLS-1$ // checks for each <form> element for (int i = 0; i < length;
	 * i++) { Element fEl = (Element) nl.item(i); Vector<Element> fcVector =
	 * getFormControl(fEl); int labelLen = labelNl.getLength(); // checks for each
	 * input controls for (int j = 0; j < fcVector.size(); j++) { Element el =
	 * fcVector.get(j); boolean bTitle = this.hasTitle(el); if (!bTitle) { // no
	 * title for a form control System.out.println("C_79.6");
	 * addCheckerProblem("C_79.6", el); //$NON-NLS-1$ } } } }
	 */

	private void item_80() {
		// NodeList bodyNl = target.getElementsByTagName("body");
		if (body_elements.length > 0) {
			Element bodyEl = body_elements[0];
			Stack<Node> stack = new Stack<Node>();
			Node curNode = bodyEl;
			while (curNode != null) {
				if (curNode.getNodeType() == Node.ELEMENT_NODE) {
					Element el = (Element) curNode;
					String strAlt = el.getAttribute(ATTR_ALT);
					if (strAlt != null && strAlt.length() > 150) {
						// alt text length check
						addCheckerProblem("C_80.0", el); //$NON-NLS-1$
					}
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
		}
	}

	private void item_85() {
		boolean bHasBgsound = false;
		NodeList nl = targetDoc.getElementsByTagName("head"); //$NON-NLS-1$
		Element targetE = null;
		for (int i = 0; i < nl.getLength(); i++) {
			NodeList bgNl = ((Element) nl.item(i)).getElementsByTagName("bgsound"); //$NON-NLS-1$
			if (bgNl.getLength() > 0) {
				bHasBgsound = true;
				targetE = (Element) bgNl.item(0);
				break;
			}
		}
		if (bHasBgsound) {
			addCheckerProblem("C_85.0", targetE); //$NON-NLS-1$
		}
	}

	private void item_86() {
		boolean bHasMulti = false;
		for (int i = 0; i < object_elements.length; i++) {
			Element el = object_elements[i];
			String str = el.getAttribute("data"); //$NON-NLS-1$
			str = getFileExtension(str);
			if (isAudioFileExt(str) || isMultimediaFileExt(str)) {
				bHasMulti = true;
				break;
			}
		}

		if (!bHasMulti) {
			for (Element applet : edu.getAppletElements()) {
				NodeList parNl = applet.getElementsByTagName("param"); //$NON-NLS-1$
				int parLength = parNl.getLength();
				for (int j = 0; j < parLength; j++) {
					String str = ((Element) parNl.item(j)).getAttribute("value"); //$NON-NLS-1$
					str = getFileExtension(str);
					if (isAudioFileExt(str) || isMultimediaFileExt(str)) {
						bHasMulti = true;
						break;
					}
				}
				if (bHasMulti) {
					break;
				}
			}
		}

		if (!bHasMulti) {
			for (int i = 0; i < aWithHref_hrefs.length; i++) {
				// Element el = aWithHref_elements[i];
				String str = aWithHref_hrefs[i];
				str = getFileExtension(str);
				if (isAudioFileExt(str) || isMultimediaFileExt(str)) {
					bHasMulti = true;
					break;
				}
			}
		}

		if (bHasMulti) {
			addCheckerProblem("C_86.0"); //$NON-NLS-1$
		}

	}

	@SuppressWarnings("nls")
	private void item_88() {
		String charset = "";
		NodeList nl = targetDoc.getElementsByTagName("meta");
		int length = nl.getLength();
		for (int i = 0; i < length; i++) {
			Element el = (Element) nl.item(i);
			if (el.hasAttribute("http-equiv") && el.hasAttribute("content")) { //$NON-NLS-1$
				String strMeta = el.getAttribute("http-equiv"); //$NON-NLS-1$
				String strCon = el.getAttribute("content");
				if (strMeta != null && strMeta.equalsIgnoreCase("Content-Type") && strCon != null) {
					int index = strCon.toLowerCase().indexOf("text/html");
					if (index >= 0) {
						strCon = strCon.substring(index + 9);
						index = strCon.indexOf(";");
						if (index >= 0) {
							strCon = strCon.substring(index + 1);
							index = strCon.toLowerCase().indexOf("charset");
							if (index >= 0) {
								strCon = strCon.substring(index + 7);
								index = strCon.indexOf("=");
								if (index >= 0) {
									charset = strCon.substring(index + 1);
								}
							}
						}
					}
				}
			} else if (isHTML5 && el.hasAttribute("charset")) {
				charset = el.getAttribute("charset");
			}
		}

		if (charset.length() == 0) {
			if (isHTML5) {
				// check existence of BOM in BlindVisualizerHTMl.visualize().
				result.add(new ProblemItemImpl("C_88.1")); //$NON-NLS-1$
			} else {
				result.add(new ProblemItemImpl("C_88.0")); //$NON-NLS-1$
			}
		} else {
			// check coding (EUC-JP, Shift_JIS, UTF-8...)
			// addCheckerProblem("C_88.1", charset); //$NON-NLS-1$
		}
	}

	@SuppressWarnings("nls")
	private void item_89() {

		if (body_elements.length == 1 && targetDoc.getElementsByTagName("frameset").getLength() == 0) {
			Node curNode = body_elements[0].getFirstChild();
			StringBuffer strBuf = new StringBuffer(512);
			Stack<Node> stack = new Stack<Node>();
			while (curNode != null && strBuf.length() < valid_total_text_len) {
				if (curNode.getNodeType() == Node.TEXT_NODE) {
					// &#nbsp; (160)
					strBuf.append(curNode.getNodeValue().replaceAll(String.valueOf((char) 160), "").trim());
				} else if (curNode.getNodeType() == Node.ELEMENT_NODE) {
					Element tmpE = (Element) curNode;

					// need to check element name

					if (tmpE.hasAttribute(ATTR_ALT)) {
						strBuf.append(tmpE.getAttribute(ATTR_ALT).replaceAll(String.valueOf((char) 160), "").trim()); //$NON-NLS-1$
					}
					if (tmpE.hasAttribute(ATTR_TITLE)) {
						strBuf.append(tmpE.getAttribute(ATTR_TITLE).replaceAll(String.valueOf((char) 160), "").trim());
					}
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
			if (strBuf.length() == 0) {
				addCheckerProblem("C_89.0");
			} else if (strBuf.length() < valid_total_text_len) {
				if (img_elements.length > 0) {
					addCheckerProblem("C_89.1");
				} else {
					addCheckerProblem("C_89.2");
				}
			}
		}

	}

	private void item_90() {
		// TODO move to styleCheck

		Set<Element> styleElementList = styleElementMap.keySet();
		for (Element e : styleElementList) {
			String style = styleElementMap.get(e);
			if (style != null) {
				result.addAll(cssBeforeAfterChecker.check(style, e));
			}
		}

		Set<String> keys = styleSheetsMap.keySet();
		for (String ss : keys) {
			if (ss != null && ss.length() > 0) {
				// avoid duplication
				String style = styleSheetsMap.get(ss);
				if (style != null) {
					result.addAll(cssBeforeAfterChecker.check(style, ss));
				}
			}
		}
	}

	/**
	 * ALT text check for image buttons, area elements.
	 */
	// For new JIS
	private void item_300() {
		for (Element button : edu.getImageButtons()) {
			String alt = getAttribute(button, "alt");
			if (alt == null)
				; // B_1 error
			else {
				TextCheckResult result = checker.checkAlt(alt);
				if (!TextCheckResult.OK.equals(result)) {
					if (TextCheckResult.SPACE_SEPARATED.equals(result)
							|| TextCheckResult.SPACE_SEPARATED_JP.equals(result))
						addCheckerProblem("C_300.3", alt, button);
					else if (result.equals(TextCheckResult.NULL) || result.equals(TextCheckResult.BLANK))
						addCheckerProblem("C_300.4", alt, button);
					else
						addCheckerProblem("C_300.0", alt, button);
				}
			}
		}
		for (Element button : edu.getTextButtons()) {
			String value = getAttribute(button, "value");
			if (value == null)
				; // ??? error
			else {
				TextCheckResult result = checker.checkAlt(value);
				if (!TextCheckResult.OK.equals(result)) {
					// addCheckerProblem("C_300.0", alt, button);
				}
			}
		}
		for (Element area : edu.getAreaElements()) {
			String alt = getAttribute(area, "alt");
			if (alt == null) {
				; // B_2 error
			} else {
				Set<String> ngWord = new TreeSet<String>();
				ngWord.add("area");
				TextCheckResult result = checker.checkAlt(alt, ngWord);
				if (result.equals(TextCheckResult.OK)) {
					;
				} else if (result.equals(TextCheckResult.SPACE_SEPARATED)
						|| result.equals(TextCheckResult.SPACE_SEPARATED_JP)) {
					;
				} else if (!result.equals(TextCheckResult.BLANK) || area.hasAttribute("href")) {

					Element map = (Element) area.getParentNode();
					for (Element image : HtmlTagUtil.getImgElementsFromMap(targetDoc, map)) {
						int id = document2IdMap.get(image);

						IProblemItem tmpP = addCheckerProblem("C_300.1", alt, area);
						tmpP.setHighlightTargetIds(new HighlightTargetId(id, id));
						edu.appendErrorIcon(tmpP, area);
					}
				}
			}
		}
		for (Element applet : edu.getAppletElements()) {
			String alt = getAttribute(applet, "alt");
			if (alt == null)
				; // B_2 error
			else {
				Set<String> ngWord = new TreeSet<String>();
				ngWord.add("applet");
				ngWord.add("\u30a2\u30d7\u30ec\u30c3\u30c8"); // "applet" in
				// Japanese
				ngWord.add("\u30d7\u30ed\u30b0\u30e9\u30e0"); // "program" in
				// Japanese
				TextCheckResult result = checker.checkAlt(alt, ngWord);
				if (!TextCheckResult.OK.equals(result)) {
					addCheckerProblem("C_300.2", alt, applet);
				}
			}
		}

		NodeList tmpNL = targetDoc.getElementsByTagName("canvas");
		// use Techniques in the future
		addCheckerProblem("C_300.5", "", tmpNL);

	}

	// moved into 600.18
	// // For new JIS
	// @SuppressWarnings("nls")
	// private void item_320() {
	// addCheckerProblem("C_320.0");
	// }

	// For new JIS
	private void item_331() {
		Vector<Node> withoutScope = new Vector<Node>();
		Vector<Node> invalidScope = new Vector<Node>();

		for (Element table : dataTableList) {
			int thCount = edu.getElementsList(table, "th").size();//$NON-NLS-1$

			boolean isHeaderRow = false;
			boolean isHeaderColumn = true;

			boolean isRowAndCol = false;
			// th th th
			// th td td
			// th td td

			boolean isSimpleTable2 = false;
			// simple table with row/col headers (top left td should be empty)
			// td th th
			// th td td
			// th td td

			int firstRowLength = 0;
			int trCount = 0;
			int maxColCount = 0;
			Element topLeftElement = null;

			for (Element tr : edu.getElementsList(table, "tr")) { //$NON-NLS-1$
				List<Element> thList = edu.getElementsList(tr, "th"); //$NON-NLS-1$
				List<Element> cellList = edu.getElementsList(tr, "th", "td"); //$NON-NLS-1$
				int thSize = thList.size();

				int colCount = 0;
				for (Element el : cellList) {
					int col = 1;
					if (el.hasAttribute("colspan")) {//$NON-NLS-1$
						try {
							col = Integer.parseInt(el.getAttribute("colspan"));//$NON-NLS-1$
						} catch (Exception e) {
						}
					}
					colCount += col;
				}
				if (colCount > maxColCount) {
					maxColCount = colCount;
				}

				Node firstCell = tr.getFirstChild();
				String firstCellName = "";//$NON-NLS-1$
				if (null != firstCell) {
					firstCellName = firstCell.getNodeName().toLowerCase();
				}
				boolean isTH = firstCellName.equals("th");//$NON-NLS-1$

				if (trCount == 0) {
					firstRowLength = tr.getChildNodes().getLength();
					if (thSize == firstRowLength) {
						isHeaderRow = true;
					} else if ((thSize + 1 == firstRowLength) && firstCellName.equals("td")) {//$NON-NLS-1$
						isSimpleTable2 = true;
					}
					if (firstCell instanceof Element) {
						topLeftElement = (Element) firstCell;
					}

				}

				boolean isTHwoRowspan = isTH;
				if (isTHwoRowspan) {
					try {
						isTHwoRowspan = (Integer.parseInt(((Element) firstCell).getAttribute("rowspan")) < 2);//$NON-NLS-1$
					} catch (Exception e) {
					}
				}

				if (isHeaderColumn) {
					isHeaderColumn = isTHwoRowspan;
				}
				if (isSimpleTable2 && trCount != 0) {
					isSimpleTable2 = isTHwoRowspan;
				}
				trCount++;
			}

			// count check
			isHeaderRow = isHeaderRow && firstRowLength == maxColCount;
			isSimpleTable2 = isSimpleTable2 && firstRowLength == maxColCount
					&& (firstRowLength + trCount - 2) == thCount;

			// check 1st Row and 1st Column
			if (isHeaderRow && isHeaderColumn && (trCount + firstRowLength - 1) == thCount) {
				isRowAndCol = true;
			}

			// is Simple?
			boolean isSimpleTable = (isHeaderRow && firstRowLength == thCount)
					|| (isHeaderColumn && trCount == thCount);
			// th th th
			// td td td
			// --- or ---
			// th td td
			// th td td
			// th td td

			// all data tables are leaf tables, so getElements() suffice.
			for (Element th : edu.getElementsList(table, "th")) {
				if (!th.hasAttribute("scope")) {
					if (!isSimpleTable && !isSimpleTable2 && !isRowAndCol) {
						withoutScope.add(th);
					}
				} else if (!th.getAttribute("scope").matches("row(group)?|col(group)?")) {
					invalidScope.add(th);
				}
			}

			if (isRowAndCol && !topLeftElement.hasAttribute("scope")) {
				withoutScope.add(topLeftElement);
			}

			if (isSimpleTable2) {
				String tdString = getTextAltDescendant(topLeftElement).strip();
				if (tdString.length() > 0) {
					addCheckerProblem("C_331.2", topLeftElement);
				}
			}

			if (withoutScope.size() > 0) {
				boolean hasHeaders = false;
				try {
					NodeList tdWithHeaders = xpathService.evalPathForNodeList("descendant::td[@headers]", table);
					hasHeaders = tdWithHeaders.getLength() == table.getElementsByTagName("td").getLength();
				} catch (Exception e) {
				}
				if (!hasHeaders) {
					addCheckerProblem("C_331.0", "", withoutScope);
				}
			}
			withoutScope = new Vector<Node>();
		}
		addCheckerProblem("C_331.1", "", invalidScope);
	}

	// For new JIS
	private void item_332() {
		for (Element table : dataTableList) {
			List<Element> cells = edu.getElementsList(targetDoc, "th", "td");
			for (Element cell : cells) {
				if (cell.hasAttribute("headers")) {
					for (String id : cell.getAttribute("headers").split("[ \t]+")) {
						Element referred = targetDoc.getElementById(id);
						if (referred == null) {
							addCheckerProblem("C_332.1", "(id=" + id + ")", cell);
						} else if (!referred.getTagName().toLowerCase().matches("td|th")) {
							addCheckerProblem("C_332.2", referred.getTagName().toLowerCase(), cell);
						}
					}
				}
			}
		}
	}

	// For new JIS
	@SuppressWarnings("nls")
	private void item_380() {
		Vector<Node> noSubmitForms = new Vector<Node>();
		for (Element form : formList) {
			boolean hasSubmit = false;
			NodeList inputs = form.getElementsByTagName("input");
			for (int j = 0; j < inputs.getLength(); j++) {
				Element input = (Element) inputs.item(j);
				String typeS = input.getAttribute("type");
				if ("submit".equals(typeS) || "image".equals(typeS)) {
					hasSubmit = true;
					break;
				}
			}
			if (!hasSubmit) {
				NodeList buttons = form.getElementsByTagName("button");
				for (int j = 0; j < buttons.getLength(); j++) {
					Element button = (Element) buttons.item(j);
					String typeS = button.getAttribute("type");
					if ("submit".equals(typeS)) {
						hasSubmit = true;
						break;
					}
				}
			}
			if (!hasSubmit && isHTML5 && form.hasAttribute("id")) {
				String id = form.getAttribute("id");
				inputs = targetDoc.getElementsByTagName("input");
				for (int i = 0; i < inputs.getLength(); i++) {
					Element e = (Element) inputs.item(i);
					String formAttr = e.getAttribute("form");
					if (id.equals(formAttr)) {
						String typeS = e.getAttribute("type");
						if ("submit".equals(typeS) || "image".equals(typeS)) {
							hasSubmit = true;
							break;
						}
					}
				}
				if (!hasSubmit) {
					NodeList buttons = targetDoc.getElementsByTagName("button");
					for (int i = 0; i < buttons.getLength(); i++) {
						Element e = (Element) buttons.item(i);
						String formAttr = e.getAttribute("form");
						if (id.equals(formAttr)) {
							String typeS = e.getAttribute("type");
							if ("submit".equals(typeS)) {
								hasSubmit = true;
								break;
							}
						}
					}
				}
			}
			if (!hasSubmit) {
				noSubmitForms.add(form);
			}
		}
		addCheckerProblem("C_380.0", null, noSubmitForms);
	}

	@SuppressWarnings("nls")
	private void formCheck() {
		for (Element form : formList) {
			if (form.getElementsByTagName("select").getLength() > 0)
				addCheckerProblem("C_381.0", form);
		}

		NodeList nl = targetDoc.getElementsByTagName("form");
		addCheckerProblem("C_382.0", "", nl);
		addCheckerProblem("C_383.0", "", nl);
		addCheckerProblem("C_387.0", "", nl);
		addCheckerProblem("C_388.0", "", nl);

	}

	// For new JIS
	@SuppressWarnings("nls")
	private void item_384() {
		if (formVwithText == null)
			formVwithText = getFormsWithTextinput();
		addCheckerProblem("C_384.0", "", formVwithText); //$NON-NLS-1$
	}

	// For new JIS
	@SuppressWarnings("nls")
	private void item_385() {
		if (formVwithText == null)
			formVwithText = getFormsWithTextinput();
		addCheckerProblem("C_385.0", "", formVwithText);
	}

	// For new JIS
	@SuppressWarnings("nls")
	private void item_386() {
		if (formVwithText == null)
			formVwithText = getFormsWithTextinput();
		addCheckerProblem("C_386.0", "", formVwithText);
	}

	// For new JIS
	@SuppressWarnings("nls")
	private void item_389() {
		if (formVwithText == null)
			formVwithText = getFormsWithTextinput();
		addCheckerProblem("C_389.0", "", formVwithText);
	}

	// For new JIS
	@SuppressWarnings("nls")
	private void item_422() {
		List<Element> accesskeys = edu.getAccessKeyElements();
		Map<String, List<Element>> map = new HashMap<String, List<Element>>();
		for (Element element : accesskeys) {
			String key = element.getAttribute("accesskey");
			if (!map.containsKey(key))
				map.put(key, new ArrayList<Element>());
			map.get(key).add(element);
		}
		for (String key : map.keySet()) {
			if (map.get(key).size() > 1) {
				addCheckerProblem("C_422.0", key, new Vector<Node>(map.get(key)));
			}
		}
	}

	private void item_423() {
		List<Element> idElementList = edu.getElementsWithId();
		Map<String, List<Element>> map = new HashMap<String, List<Element>>();
		for (Element element : idElementList) {
			String key = element.getAttribute("id");
			if (!map.containsKey(key)) {
				map.put(key, new ArrayList<Element>());
			}
			map.get(key).add(element);
		}
		for (String key : map.keySet()) {
			if (map.get(key).size() > 1) {
				if (key.isEmpty()) {
					addCheckerProblem("C_423.0", "id=\"\"", new Vector<Node>(map.get(key)));
				} else {
					addCheckerProblem("C_423.0", key, new Vector<Node>(map.get(key)));
				}
			}
		}
	}

	/*
	 * ARIA related items (700)
	 */
	private void item_700() {
		List<Element> ariaLabelledByElementList = edu.getElementsWithAriaLabelledBy();
		Vector<Node> withoutTargetV = new Vector<Node>();
		// ToDo divide by tag/role
		for (Element element : ariaLabelledByElementList) {
			Vector<Node> otherV = new Vector<Node>();
			String target = element.getAttribute("aria-labelledby").trim();
			if (target.length() == 0) {
				withoutTargetV.add(element);
			} else {
				StringBuffer tmpSB = new StringBuffer();
				String[] IDs = target.split(" ");
				otherV.add(element);
				for (String id : IDs) {
					Element labelElement = targetDoc.getElementById(id);
					if (null == labelElement) {
						tmpSB.append(id + ", ");
					} else {
						otherV.add(labelElement);
					}
				}
				if (tmpSB.length() > 0) {
					addCheckerProblem("C_700.1", tmpSB.substring(0, tmpSB.length() - 2), element);
				} else {
					addCheckerProblem("C_700.2", target, otherV);
				}
			}
		}
		addCheckerProblem("C_700.0", "", withoutTargetV);
	}

	private void item_701() {
		addCheckerProblem("C_701.0", "", edu.getElementsWithAriaLabel());
	}

	private void item_702() {
		List<Element> ariaDescribedByElementList = edu.getElementsWithAriaDescribedby();
		Vector<Node> withoutTargetV = new Vector<Node>();
		// ToDo divide by tag/role
		for (Element element : ariaDescribedByElementList) {
			Vector<Node> otherV = new Vector<Node>();
			String target = element.getAttribute("aria-describedby").trim();
			if (target.length() == 0) {
				withoutTargetV.add(element);
			} else {
				StringBuffer tmpSB = new StringBuffer();
				String[] IDs = target.split(" ");
				otherV.add(element);
				for (String id : IDs) {
					Element describedbyElement = targetDoc.getElementById(id);
					if (null == describedbyElement) {
						tmpSB.append(id + ", ");
					} else {
						otherV.add(describedbyElement);
					}
				}
				if (tmpSB.length() > 0) {
					addCheckerProblem("C_702.1", tmpSB.substring(0, tmpSB.length() - 2), element);
				} else {
					addCheckerProblem("C_702.2", target, otherV);
				}
			}
		}
		addCheckerProblem("C_702.0", "", withoutTargetV);
	}

	private void item_703() {
		List<Element> roleElementList = edu.getElementsWithAriaRole();
		addCheckerProblem("C_703.0", "", roleElementList);
		Vector<Node> semanticV = new Vector<>();
		Vector<Node> bannerV = new Vector<>();
		Vector<Node> applicationV = new Vector<>();
		Vector<Node> regionV = new Vector<>();
		Vector<Node> regionWoNameV = new Vector<>();
		Vector<Node> headingV = new Vector<>();
		Vector<Node> headingWoLevelV = new Vector<>();
		Vector<Node> groupV = new Vector<>();
		Vector<Node> groupWoNameV = new Vector<>();
		Vector<Node> radiogroupV = new Vector<>();
		Vector<Node> radiogroupWoNameV = new Vector<>();
		Vector<Node> alertV = new Vector<>();
		Vector<Node> alertdialogV = new Vector<>();
		Vector<Node> imgV = new Vector<>();

		for (Element target : roleElementList) {
			String roleS = target.getAttribute("role");

			// landmark, region, heading, group, alert
			switch (roleS) {
			case "complementary", "contentinfo", "form", "main", "navigation", "search" -> { // ARIA11,H101
				semanticV.add(target);
				// ARIA13(aria-labelledby) is covered by 700.2
			}
			case "banner" -> { // ARIA11
				bannerV.add(target);
			}
			case "application" -> { // ARIA11
				applicationV.add(target);
			}
			case "region" -> { // ARIA20,ARIA13,H101
				String name = getNameByAria(target);
				if (name == null || name.isBlank()) {
					regionWoNameV.add(target);
				} else {
					regionV.add(target);
				}
			}
			case "heading" -> {// ARIA12
				boolean hasLevel = false;
				try {
					int level = Integer.parseInt(target.getAttribute("aria-level"));
					if (level > 0) {
						hasLevel = true;
					}
				} catch (Exception e) {
				}

				if (hasLevel) {
					headingV.add(target);
				} else {
					headingWoLevelV.add(target);
				}
			}
			case "group" -> {// ARIA17
				String name = getNameByAria(target);
				if (name == null || name.isBlank()) {
					groupWoNameV.add(target);
				} else {
					groupV.add(target);
				}
			}
			case "radiogroup" -> {// ARIA17
				String name = getNameByAria(target);
				if (name == null || name.isBlank()) {
					radiogroupWoNameV.add(target);
				} else {
					radiogroupV.add(target);
				}
			}
			case "alertdialog" -> {// ARIA18
				alertdialogV.add(target);
			}
			case "alert" -> {// ARIA19
				alertV.add(target);
			}
			case "img" -> {// ARIA24
				// CSS (before/after) check is in C90.1,2
				imgV.add(target);
			}
			// TBD document structure role, etc.
			}

		}
		addCheckerProblem("C_703.1", "", semanticV);
		addCheckerProblem("C_703.2", "", bannerV);
		addCheckerProblem("C_703.3", "", applicationV);
		addCheckerProblem("C_703.4", "", regionV);
		addCheckerProblem("C_703.5", "", regionWoNameV);
		addCheckerProblem("C_703.6", "", headingV);
		addCheckerProblem("C_703.7", "", headingWoLevelV);
		addCheckerProblem("C_703.8", "group", groupV);
		addCheckerProblem("C_703.8", "radiogroup", radiogroupV);
		addCheckerProblem("C_703.9", "group", groupWoNameV);
		addCheckerProblem("C_703.9", "radiogroup", radiogroupWoNameV);

		addCheckerProblem("C_703.10", "role=\"alert\"", alertV);
		addCheckerProblem("C_703.10", "aria-live=\"assertive\"", edu.getElementsWithAriaLiveAssertive());
		addCheckerProblem("C_703.11", "role=\"alertdialog\"", alertdialogV);

		addCheckerProblem("C_703.12", "", imgV);
	}

	private void item_704() {
		addCheckerProblem("C_704.0", "", edu.getElementsWithARIAStateProperties());
	}

	private String getNameByAria(Element target) {
		String result = null;
		if (target.hasAttribute("aria-labelledby")) {
			String targetS = target.getAttribute("aria-labelledby").trim();
			if (targetS.length() > 0) {
				StringBuffer tmpSB = new StringBuffer();
				String[] IDs = targetS.split(" ");
				Document doc = target.getOwnerDocument();
				if (null != doc) {
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
		}
		if (null == result && target.hasAttribute("aria-label")) {
			result = target.getAttribute("aria-label");
		}
		return result;
	}

	/**
	 * Displays AA items
	 */
	private void always() {

		// language check
		addCheckerProblem("C_19.0"); //$NON-NLS-1$

		// Use the ABBR and ACRONYM elements alert
		// check sequence of capital
		addCheckerProblem("C_20.0"); //$NON-NLS-1$

		// alert to use latest technology
		addCheckerProblem("C_47.0"); //$NON-NLS-1$

		// alert to group related elements
		addCheckerProblem("C_55.0"); //$NON-NLS-1$

		if (hasAwithHref) {
			// link phrase alert
			addCheckerProblem("C_56.1"); //$NON-NLS-1$

			// TBD estimation
			// alert to identify logical groups of links
			addCheckerProblem("C_64.0"); //$NON-NLS-1$

			addCheckerProblem("C_65.0"); //$NON-NLS-1$

			addCheckerProblem("C_81.0"); //$NON-NLS-1$
		}

		// alert if there is a site map or...
		addCheckerProblem("C_61.0"); //$NON-NLS-1$

		// alert if there is a clear navigation structure
		addCheckerProblem("C_62.0"); //$NON-NLS-1$

		// TBD estimation
		// alert if there are navigation bars for navigation structure
		addCheckerProblem("C_63.0"); //$NON-NLS-1$

		// alert if there is distinguishing information
		addCheckerProblem("C_67.0"); //$NON-NLS-1$

		// alert about language
		addCheckerProblem("C_70.0"); //$NON-NLS-1$

		// alert
		addCheckerProblem("C_71.0"); //$NON-NLS-1$

		// alert about consistant style
		addCheckerProblem("C_72.0"); //$NON-NLS-1$

		addCheckerProblem("C_82.0"); //$NON-NLS-1$
		addCheckerProblem("C_83.0"); //$NON-NLS-1$
		addCheckerProblem("C_84.0"); //$NON-NLS-1$

		addCheckerProblem("C_87.0"); //$NON-NLS-1$

		// moved into mediaCheck
		// if (SHOW_ALWAYS || object_elements.length > 0
		// || embed_elements.length > 0
		// || edu.getAppletElements().size() > 0) {
		// addCheckerProblem("C_500.0");
		// addCheckerProblem("C_500.1");
		// }

		addCheckerProblem("C_321.0");
		addCheckerProblem("C_322.0");

		addCheckerProblem("C_421.0");
		addCheckerProblem("C_421.1", isXHTML ? "XHTML" : "HTML");

		addCheckerProblem("C_500.11");
		addCheckerProblem("C_500.12");
		addCheckerProblem("C_500.2");
		addCheckerProblem("C_500.3");

		// moved into 15 & 388
		// // TODO same as above
		// if (SHOW_ALWAYS || headings.length > 0
		// || target.getElementsByTagName("label").getLength() > 0)
		// addCheckerProblem("C_500.4");

		addCheckerProblem("C_500.5");
		addCheckerProblem("C_500.6");
		addCheckerProblem("C_500.7");
		addCheckerProblem("C_500.8");
		addCheckerProblem("C_500.9");
		addCheckerProblem("C_500.10");

		// addCheckerProblem("C_500.13");
		// addCheckerProblem("C_500.14");
		// addCheckerProblem("C_500.15");
		// addCheckerProblem("C_500.16");

		addCheckerProblem("C_500.22");

		addCheckerProblem("C_600.0");

		// moved into mediaCheck
		// addCheckerProblem("C_600.1");
		// addCheckerProblem("C_600.2");
		// addCheckerProblem("C_600.6");
		// addCheckerProblem("C_600.7");

		addCheckerProblem("C_600.3");
		addCheckerProblem("C_600.4");
		addCheckerProblem("C_600.5");

		addCheckerProblem("C_600.8");
		addCheckerProblem("C_600.9");
		addCheckerProblem("C_600.10");
		addCheckerProblem("C_600.11");
		addCheckerProblem("C_600.12");
		addCheckerProblem("C_600.13");
		addCheckerProblem("C_600.14");
		addCheckerProblem("C_600.15");

		// move into mediaCheck
		// addCheckerProblem("C_600.16");

		addCheckerProblem("C_600.17");
		addCheckerProblem("C_600.18");
		addCheckerProblem("C_600.19");
	}

	private class StyleSelectorSets {
		Set<String> colorSelector = new HashSet<>();
		Set<String> bgcolorSelector = new HashSet<>();
		Set<String> allcolorSelector = new HashSet<>();
		Set<String> fixSelector = new HashSet<>();
		Set<String> viewportSelector = new HashSet<>();

		public StyleSelectorSets() {
		}
	}

	private StyleSelectorSets findStyles(String style) {
		StyleSelectorSets results = new StyleSelectorSets();
		Matcher matcher = STYLEITEM.matcher(style);
		while (matcher.find()) {
			String group = matcher.group();
			boolean color = COLOR.matcher(group).find();
			boolean bgColor = BGCOLOR.matcher(group).find() || BGCOLOR2.matcher(group).find();
			if (color || bgColor) {
				String selector = getSelector(group);
				if (color && !bgColor) {
					results.colorSelector.add(selector);
				} else if (!color && bgColor) {
					results.bgcolorSelector.add(selector);
				} else {
					results.allcolorSelector.add(selector);
				}
			}
			if (FIXSIZE_PATTERN.matcher(group).find()) {
				results.fixSelector.add(getSelector(group));
			}
			if (VIEWPORT_PATTERN.matcher(group).find()) {
				results.viewportSelector.add(getSelector(group));
			}
		}
		return results;
	}

	private void styleCheck() {
		Set<Element> styleElementList = styleElementMap.keySet();
		for (Element e : styleElementList) {
			String style = styleElementMap.get(e);

			if (style != null) {
				StyleSelectorSets sss = findStyles(style);
				if (sss.colorSelector.size() > 0) {
					addCheckerProblem("C_500.17", "(" + Messages.StyleElement + ", " + Messages.Selector + "="
							+ getSelectors(sss.colorSelector) + ")", e);
				}
				if (sss.bgcolorSelector.size() > 0) {
					addCheckerProblem("C_500.18", "(" + Messages.StyleElement + ", " + Messages.Selector + "="
							+ getSelectors(sss.bgcolorSelector) + ")", e);
				}
				if (sss.allcolorSelector.size() > 0) {
					addCheckerProblem("C_8.0", "(" + Messages.StyleElement + ", " + Messages.Selector + "="
							+ getSelectors(sss.allcolorSelector) + ")", e);
				}
				if (sss.fixSelector.size() > 0) {
					addCheckerProblem("C_500.19", "(" + Messages.StyleElement + ", " + Messages.Selector + "="
							+ getSelectors(sss.fixSelector) + ")", e);
				}
				if (sss.viewportSelector.size() > 0) {
					addCheckerProblem("C_500.21", "(" + Messages.StyleElement + ", " + Messages.Selector + "="
							+ getSelectors(sss.viewportSelector) + ")", e);
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
				StyleSelectorSets sss = findStyles(style);
				if (sss.colorSelector.size() > 0) {
					addCheckerProblem("C_500.17",
							"(" + ss + ", " + Messages.Selector + "=" + getSelectors(sss.colorSelector) + ")");
				}
				if (sss.bgcolorSelector.size() > 0) {
					addCheckerProblem("C_500.18",
							"(" + ss + ", " + Messages.Selector + "=" + getSelectors(sss.bgcolorSelector) + ")");
				}
				if (sss.allcolorSelector.size() > 0) {
					addCheckerProblem("C_8.0",
							"(" + ss + ", " + Messages.Selector + "=" + getSelectors(sss.allcolorSelector) + ")");
				}
				if (sss.fixSelector.size() > 0) {
					addCheckerProblem("C_500.19",
							"(" + ss + ", " + Messages.Selector + "=" + getSelectors(sss.fixSelector) + ")");
				}
				if (sss.viewportSelector.size() > 0) {
					addCheckerProblem("C_500.21",
							"(" + ss + ", " + Messages.Selector + "=" + getSelectors(sss.viewportSelector) + ")");
				}
			}
		}

		for (Element e : elementsWithStyleList) {
			String style = e.getAttribute("style");

			boolean color = COLOR_ATTR.matcher(style).matches(); // need to use matches()
			boolean bgColor = BGCOLOR_ATTR.matcher(style).find() || BGCOLOR2_ATTR.matcher(style).find();
			if (color || bgColor) {
				if (color && !bgColor) {
					addCheckerProblem("C_500.17", "(" + Messages.StyleAttribute + ", " + style + ")", e);
				} else if (!color && bgColor) {
					addCheckerProblem("C_500.18", "(" + Messages.StyleAttribute + ", " + style + ")", e);
				} else {
					addCheckerProblem("C_8.0", "(" + Messages.StyleAttribute + ", " + style + ")", e);
				}
			}

			if (FIXSIZE_PATTERN_ATTR.matcher(style).find()) {
				addCheckerProblem("C_500.20", "(" + Messages.StyleAttribute + ", " + style + ")", e); // error
			}
			if (VIEWPORT_PATTERN_ATTR.matcher(style).find()) {
				addCheckerProblem("C_500.21", "(" + Messages.StyleAttribute + ", " + style + ")", e); // error
			}
		}

		// Special check for body element
		// http://www.w3.org/TR/html4/sgml/loosedtd.html#bodycolors

		if (body_elements.length > 0) {
			Element bodyEl = body_elements[0];
			boolean color = bodyEl.getAttribute("text").length() > 0;
			boolean bgColor = bodyEl.getAttribute("bgcolor").length() > 0;

			boolean linkColor = (bodyEl.getAttribute("link").length() > 0 || bodyEl.getAttribute("vlink").length() > 0
					|| bodyEl.getAttribute("alink").length() > 0);

			if (color && !bgColor) {
				addCheckerProblem("C_500.17", "", bodyEl);
			} else if (!color && bgColor) {
				addCheckerProblem("C_500.18", "", bodyEl);
			}

			if (linkColor) {
				addCheckerProblem("C_500.18", "(link)", bodyEl);
			}

		}
	}

	private void customElementCheck() {
		if (custom_elements.length > 0) {
			Vector<Node> tmpV = new Vector<Node>();
			HashSet<String> tmpSet = new HashSet<String>();
			StringBuffer tmpSB = new StringBuffer();
			for (int i = 0; i < custom_elements.length; i++) {
				tmpV.add(custom_elements[i]);
				tmpSet.add(custom_elements[i].getTagName());
			}
			for (String tmpS : tmpSet) {
				tmpSB.append(tmpS + ", ");
			}
			addCheckerProblem("C_600.22", tmpSB.substring(0, tmpSB.length() - 2), tmpV);
		}
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

	// Mobile Web Evaluation (from here)
	private void mobile_1() {
		// TODO implement evaluation
		// addCheckerProblem("M_1"); //$NON-NLS-1$
	}

	// Mobile Web Evaluation (end here)

	private void validateHtml() {
		if (body_elements.length > 1) {
			addCheckerProblem("C_1000.0"); //$NON-NLS-1$
		} else if (body_elements.length == 0 && !docTypeS.toLowerCase().contains("frameset")) {
			addCheckerProblem("C_1000.4"); //$NON-NLS-1$
		}
	}

	// @SuppressWarnings("nls")
	// private void checkDomDifference() {
	//
	// if (edu.getInvisibleElementCount() > 0) {
	// addCheckerProblem("C_201.0");
	// }
	// String[] invisibleLinkStrings = edu.getInvisibleLinkStrings();
	// for (int i = 0; i < invisibleLinkStrings.length; i++) {
	// if (invisibleLinkStrings[i].trim().length() > 0) {
	// addCheckerProblem("C_201.1", " (href="
	// + invisibleLinkStrings[i] + ")");
	// }
	// }
	//
	// Set<String> notExistSet = edu.getNotExistHrefSet();
	// for (String href : notExistSet) {
	// addCheckerProblem("C_200.0", " (href=" + href + ")");
	// }
	// if (notExistSet.size() > 10) {
	// addCheckerProblem("C_200.1");
	// }
	//
	// }

	private void checkAbsoluteSize(String strName) {
		NodeList nl = targetDoc.getElementsByTagName(strName);
		int length = nl.getLength();
		Vector<Node> nodeV = new Vector<Node>();
		for (int i = 0; i < length; i++) {
			Element el = (Element) nl.item(i);
			String strWidth = el.getAttribute("width"); //$NON-NLS-1$
			String strHeight = el.getAttribute("height"); //$NON-NLS-1$
			if ((strWidth != null && !strWidth.equals("") //$NON-NLS-1$
					&& strWidth.indexOf("%") == -1) //$NON-NLS-1$
					|| (strHeight != null && !strHeight.equals("") //$NON-NLS-1$
							&& strHeight.indexOf("%") == -1)) { //$NON-NLS-1$
				// absolute width or height
				nodeV.add(el);
			}
		}
		addCheckerProblem("C_13.0", "", nodeV);//$NON-NLS-1$ //$NON-NLS-2$
	}

	private boolean hasOpenWndEvent(Element element, String[] targetAttrs) {

		// need to check open(...) (can use without 'window.')

		for (int i = 0; i < targetAttrs.length; i++) {
			String str = element.getAttribute(targetAttrs[i]);
			if (str.toLowerCase().indexOf(WINDOW_OPEN) >= 0) {
				return true;
			}
		}
		return false;
	}

	private void checkObsoluteEle(String problemId, String strEle) {
		addCheckerProblem(problemId, strEle, targetDoc.getElementsByTagName(strEle));
	}

	private String getFormControlType(Element el) {
		String strName = el.getNodeName().toLowerCase();
		if (strName.equals("input")) { //$NON-NLS-1$
			return el.getAttribute("type").toLowerCase(); //$NON-NLS-1$
		} else {
			return strName;
		}
	}

	/**
	 * Returns radio buttons and check boxes contained in the specified form
	 * element.
	 * 
	 * @param form
	 * @return A <code>List</code> of <code>Element</code>s.
	 */
	private List<Element> getRadioAndCheck(Element form) {
		List<Element> returns = new ArrayList<Element>();
		List<Element> inputs = edu.getElementsList(form, "input");
		for (Element e : inputs) {
			if (e.getAttribute("type").toLowerCase().matches("radio|checkbox"))
				returns.add(e);
		}
		return returns;
	}

	/**
	 * Obtains input controls in the given form element.
	 * 
	 * @param formEl
	 * @return a Vector of controls
	 */
	private List<Element> getFormControl(Element formEl) {
		List<Element> fcVector = new ArrayList<Element>();
		NodeList nl = formEl.getElementsByTagName("input"); //$NON-NLS-1$
		int length = nl.getLength();
		for (int i = 0; i < length; i++) {
			Element el = (Element) nl.item(i);
			String strType = el.getAttribute("type").toLowerCase(); //$NON-NLS-1$
			if (isHTML5) {
				if (!strType.equals("hidden")) {
					fcVector.add(el);
				}
				// TODO update by using techniques (only hidden state is not
				// labelable in HTML5)
			} else if (strType.equals("") // default is text? //$NON-NLS-1$
					|| strType.equals("text") //$NON-NLS-1$
					|| strType.equals("textarea") //$NON-NLS-1$
					|| strType.equals("radio") //$NON-NLS-1$
					|| strType.equals("checkbox") //$NON-NLS-1$
					|| strType.equals("file") //$NON-NLS-1$ // For new JIS
					|| strType.equals("password")) { //$NON-NLS-1$

				fcVector.add(el);
			}

		}

		// TODO update by using techniques
		// keygen, meter, output, progress

		nl = formEl.getElementsByTagName("textarea"); //$NON-NLS-1$
		length = nl.getLength();
		for (int i = 0; i < length; i++) {
			Element el = (Element) nl.item(i);
			fcVector.add(el);
		}

		nl = formEl.getElementsByTagName("select"); //$NON-NLS-1$
		length = nl.getLength();
		for (int i = 0; i < length; i++) {
			Element el = (Element) nl.item(i);
			fcVector.add(el);
		}

		nl = formEl.getElementsByTagName("html:text"); //$NON-NLS-1$
		length = nl.getLength();
		for (int i = 0; i < length; i++) {
			Element el = (Element) nl.item(i);
			fcVector.add(el);
		}

		nl = formEl.getElementsByTagName("html:radio"); //$NON-NLS-1$
		length = nl.getLength();
		for (int i = 0; i < length; i++) {
			Element el = (Element) nl.item(i);
			fcVector.add(el);
		}

		return fcVector;

	}

	private int getFormControlNum(Element formEl) {
		int iNum = 0;
		NodeList nl = formEl.getElementsByTagName("input"); //$NON-NLS-1$
		iNum += nl.getLength();
		nl = formEl.getElementsByTagName("select"); //$NON-NLS-1$
		iNum += nl.getLength();
		nl = formEl.getElementsByTagName("textarea"); //$NON-NLS-1$
		iNum += nl.getLength();
		nl = formEl.getElementsByTagName("html:text"); //$NON-NLS-1$
		iNum += nl.getLength();
		nl = formEl.getElementsByTagName("html:radio"); //$NON-NLS-1$
		iNum += nl.getLength();
		return iNum;
	}

	/**
	 * Returns true if the specified form element has any text input controls. For
	 * new JIS.
	 * 
	 * @param form
	 * @return
	 */
	private boolean hasTextFormControl(Element form) {
		int iNum = 0;
		for (Element e : edu.getElementsList(form, "input")) { //$NON-NLS-1$
			if (e.getAttribute("type").toLowerCase().matches("|text(area)?|password"))
				iNum++;
		}
		iNum += form.getElementsByTagName("textarea").getLength(); //$NON-NLS-1$
		iNum += form.getElementsByTagName("html:text").getLength(); //$NON-NLS-1$
		return (iNum > 0);
	}

	private Vector<Node> getFormsWithTextinput() {
		Vector<Node> forms = new Vector<Node>();
		for (Element form : formList) {
			if (hasTextFormControl(form))
				forms.add(form);
		}
		return forms;
	}

	/**
	 * Returns true if the given control type is usually used with label elements.
	 * 
	 * @return
	 */
	private boolean isLabelable(String type) {
		return !type.matches("|submit|reset|hidden|image|button");
		// TODO update by using techniques (only hidden state is not labelable
		// in HTML5)
	}

	/**
	 * For new JIS
	 * 
	 * @param el
	 * @param labels
	 * @return
	 */
	private Element getLabel(Element el) {
		String strid = el.getAttribute("id"); //$NON-NLS-1$

		if (strid.equals("")) //$NON-NLS-1$
			return null; // no id

		for (Element e : labelList) {
			String strFor = e.getAttribute("for"); //$NON-NLS-1$
			if (strFor != null && strFor.equalsIgnoreCase(strid)) {
				return e; // label found
			}
		}
		return null; // no label found
	}

	/**
	 * Detects use of implicit labels. If an implicit label is used, the label
	 * element is returned. Otherwise, null is returned. For new JIS.
	 * 
	 * @param el
	 * @return label element when an implicit label is used.
	 */
	private Element hasImplicitLabel(Element el) {
		// TODO check ancestor, check only one or not
		Node n = el.getParentNode();
		if (!(n instanceof Element))
			return null;
		Element e = (Element) n;
		if (e.getTagName().toLowerCase().equals("label"))
			return e;
		else
			return null;
	}

	/**
	 * Stub function, to be refined. For new JIS
	 * 
	 * @param title
	 * @return
	 */
	private boolean isInappropriateTitle(String title) {
		return title.matches("(?i).*untitled.*");
	}

	/**
	 * Checks if the specified form control has a properly located label.
	 * 
	 * @param el
	 * @return
	 */
	private boolean hasProperLabel(Element el) {

		// need to consider position
		boolean bRadioCheckbox = getFormControlType(el).matches("radio|checkbox"); //$NON-NLS-1$

		Node node = el;
		if (bRadioCheckbox) {
			while (node.getNextSibling() == null) {
				if (node.getNodeName().equalsIgnoreCase("body")) { //$NON-NLS-1$
					return false;
				}
				node = node.getParentNode();
			}
			if (node.getNextSibling().getNodeType() != Node.ELEMENT_NODE)
				return false;
			Element nextEl = (Element) node.getNextSibling();
			Element labelEl;
			if (nextEl.getNodeName().equalsIgnoreCase("label")) { //$NON-NLS-1$
				labelEl = nextEl;
			} else {
				NodeList nl = nextEl.getElementsByTagName("label"); //$NON-NLS-1$
				if (nl.getLength() == 0) {
					return false;
				} else {
					labelEl = (Element) nl.item(0);
				}
			}

			String strId = el.getAttribute("id"); //$NON-NLS-1$
			String strFor = labelEl.getAttribute("for"); //$NON-NLS-1$
			if (!strId.equals(strFor)) {
				return false;
			} else {
				if (getTextAltDescendant(nextEl).trim().indexOf(getTextAltDescendant(labelEl).trim()) == 0) {
					return true;
				} else {
					return false;
				}
			}

		} else {
			while (node.getPreviousSibling() == null) {
				if (node.getNodeName().equalsIgnoreCase("body")) { //$NON-NLS-1$
					return false;
				}
				node = node.getParentNode();
			}
			while (null != node && !node.getNodeName().equalsIgnoreCase("label")) { //$NON-NLS-1$
				if (node.getNodeName().equalsIgnoreCase("body")) { //$NON-NLS-1$
					return false;
				}
				node = node.getPreviousSibling();
			}
			if (node == null || node.getNodeType() != Node.ELEMENT_NODE)
				return false;
			Element preEl = (Element) node;
			Element labelEl;
			if (preEl.getNodeName().equalsIgnoreCase("label")) { //$NON-NLS-1$
				labelEl = preEl;
			} else {
				NodeList nl = preEl.getElementsByTagName("label"); //$NON-NLS-1$
				if (nl.getLength() == 0) {
					return false;
				} else {
					labelEl = (Element) nl.item(nl.getLength() - 1);
				}
			}
			String strId = el.getAttribute("id"); //$NON-NLS-1$
			String strFor = labelEl.getAttribute("for"); //$NON-NLS-1$
			if (!strId.equals(strFor)) {
				return false;
			} else {
				String strWhole = getTextAltDescendant(preEl).trim();
				String strLabel = getTextAltDescendant(labelEl).trim();
				if (strWhole.indexOf(strLabel) == strWhole.length() - strLabel.length()) {
					return true;
				} else {
					return false;
				}
			}

		}
	}

	private boolean isAsciiArtString(String str) {

		if (str == null) {
			return false;
		}

		// int realTotal = str.length();
		String[] tmpS = str.split("\\p{Space}"); //$NON-NLS-1$
		int num = 0;
		int total = 0;

		for (int j = 0; j < tmpS.length; j++) {
			String target = tmpS[j];
			int strLength = target.length();
			total += strLength;

			target = target.replaceAll("\\p{Punct}", ""); //$NON-NLS-1$ //$NON-NLS-2$
			num += strLength - target.length();

			strLength = target.length();
			for (int i = 0; i < strLength; i++) {
				if (artCharSet.contains(target.substring(i, i + 1))) {
					num++;
				}
			}

		}
		if (total == 0)
			total = 1;
		if (num > 30 && (double) num / total > 0.8)
			return true;
		else if (num > 30 && checker.isAsciiArtString(str)) // another AA
															// checking routine
			return true;
		else
			return false;
	}

	private boolean isNormalImage(Element imgEl) {
		String strWidth = imgEl.getAttribute("width"); //$NON-NLS-1$
		String strHeight = imgEl.getAttribute("height"); //$NON-NLS-1$
		int iWidth = 0, iHeight = 0;
		try {
			if (strWidth != null)
				iWidth = Integer.valueOf(strWidth).intValue();
			if (strHeight != null)
				iHeight = Integer.valueOf(strHeight).intValue();

		} catch (NumberFormatException e) {
			iWidth = 100;
			iHeight = 100;
		}
		// ignore small image according to bobby
		if (iWidth < 50 || iHeight < 50) {
			int iBig, iSmall;
			if (iWidth > iHeight) {
				iBig = iWidth;
				iSmall = iHeight;
			} else {
				iBig = iHeight;
				iSmall = iWidth;
			}
			if (iBig < 50) {
				return false;
			} else if ((double) iSmall / iBig < 0.2) {
				return false;
			}
		}
		return true;
	}

	// TODO check
	private boolean isHtmlFile(String strFile) {
		int iPos = strFile.lastIndexOf("."); //$NON-NLS-1$
		if (iPos > 0) {
			String strExt = strFile.substring(iPos + 1).toLowerCase();
			if (strExt.equals("html") //$NON-NLS-1$
					|| strExt.equals("htm") //$NON-NLS-1$
					|| strExt.equals("jsp") //$NON-NLS-1$
					|| strExt.equals("php") //$NON-NLS-1$
					|| strExt.equals("shtml")) //$NON-NLS-1$
				return true;
			else if (strFile.toLowerCase().indexOf(".cgi") > 0) //$NON-NLS-1$
				return true;
		}
		return false;
	}

	private boolean isAudioFileExt(String strFileExt) {
		for (int i = 0; i < AUDIO_FILE_EXTENSION.length; i++) {
			if (strFileExt.equalsIgnoreCase(AUDIO_FILE_EXTENSION[i])) {
				return true;
			}
		}
		return false;
	}

	private boolean isMultimediaFileExt(String strFileExt) {
		for (int i = 0; i < MULTIMEDIA_FILE_EXTENSION.length; i++) {
			if (strFileExt.equalsIgnoreCase(MULTIMEDIA_FILE_EXTENSION[i])) {
				return true;
			}
		}
		return false;
	}

	private boolean isLeafBlockEle(Node node) {
		String str = node.getNodeName().toLowerCase();
		if (!blockEleSet.contains(str)) {
			return false;
		}
		if (!node.hasChildNodes()) {
			return true;
		}
		Node curNode = node.getFirstChild();
		Stack<Node> stack = new Stack<Node>();
		while (curNode != null) {
			str = curNode.getNodeName().toLowerCase();
			if (blockEleSet.contains(str)) {
				return false;
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
		return true;
	}

	private int getWordCount(String str) {
		StringTokenizer st = new StringTokenizer(str,
				" \t\n\r\f,.[]()<>!?:/\"\u3001\u3002\u300c\u300d\u30fb\u3008\u3009\u3000"); //$NON-NLS-1$
		return st.countTokens();

	}

	private String getFileExtension(String strName) {
		int iPos = strName.lastIndexOf("."); //$NON-NLS-1$
		if (iPos > 0) {
			return strName.substring(iPos + 1);
		}
		return ""; //$NON-NLS-1$
	}

	private boolean isElementVisible(Element el) {
		// TODO function stub // For new JIS
		return true;
	}

	// For new JIS
	private boolean isButton(Element el) {
		return (el.getTagName().toLowerCase().equals("input")
				&& (el.getAttribute("type").toLowerCase().equals("submit") || el.getAttribute("type").equals("image")))
				|| (el.getTagName().toLowerCase().equals("button")
						&& el.getAttribute("type").toLowerCase().equals("submit"));
	}

	private List<Element> getDirectDescendantElements(Element element, String tagName) {
		return getDirectDescendantElements(element, tagName, element.getTagName().toLowerCase());
	}

	/**
	 * Utility function that returns a List array instead of a NodeList array.
	 * 
	 * @param el
	 * @param tagName
	 * @return list of elements with given tag name that are direct descendants of
	 *         the node.
	 */
	private List<Element> getDirectDescendantElements(Element element, String tagName, String excluded) {
		List<Element> nodes = new ArrayList<Element>();
		NodeList children = element.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node n = children.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) n;
				if (e.getTagName().toLowerCase().equals(tagName))
					nodes.add(e);
				else if (!e.getTagName().toLowerCase().equals(excluded))
					nodes.addAll(getDirectDescendantElements(e, tagName, excluded));
			}
		}
		return nodes;
	}

	/**
	 * Returns string for the attribute value if it is specified in the HTML source,
	 * otherwise this method returns null. Note that hasAttribute() badly works when
	 * the default value of the attribute is specified in the HTML spec. Using this
	 * method and comparing the result to null works well.
	 * 
	 * @param e
	 * @param attr
	 * @return attribute value string or null.
	 */
	// TODO consider <tag attr>, not <tag attr="value">.
	private String getAttribute(Element e, String attr) {
		Attr att = e.getAttributeNode(attr);
		if (att == null)
			return null;
		return att.getValue();
	}

	/**
	 * For new JIS. used in item_23(). Returns the most nearest ascendant table
	 * element. If such table is not found (case of invalid use of caption
	 * elements), this method returns null.
	 */
	private Element nearestTable(Element el) {
		Node e = el;
		while ((e instanceof Element) && !((Element) e).getTagName().toLowerCase().equals("table")) {
			e = ((Element) e).getParentNode();
		}
		return (e instanceof Element) ? (Element) e : null;
	}

	/**
	 * For new JIS. Currently it is not used. It will be used from item_3().
	 * 
	 * @param alt
	 * @param longdescUri
	 * @return
	 */
	private boolean longdescLongerThanALT(String alt, String longdescUri) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * For new JIS. Currently it is not used. It will be used from item_3().
	 * 
	 * @param url
	 * @return
	 */
	private boolean isExistingUrl(String url) {
		url = url.trim();
		if (url.length() == 0)
			return false;
		else
			return true; // check routine comes here
	}

	// For new JIS
	/**
	 * @param table
	 */
	private boolean hasDuplicatedCaptionAndSummary(Element table) {
		String strSum = table.getAttribute("summary").trim(); //$NON-NLS-1$
		NodeList capNl = table.getElementsByTagName("caption"); //$NON-NLS-1$
		for (int i = 0; i < capNl.getLength(); i++) {
			if (strSum.equals(HtmlTagUtil.getTextDescendant(capNl.item(i)).trim()))
				return true;
		}
		return false;
	}

	/**
	 * For new JIS
	 * 
	 * @param table
	 */
	private boolean hasSummary(Element table) {
		String strSum = table.getAttribute("summary"); //$NON-NLS-1$
		return strSum.length() > 0;
	}

	private boolean hasCaption(Element table) {
		return table.getElementsByTagName("caption").getLength() > 0; //$NON-NLS-1$
	}

	/**
	 * For new JIS
	 * 
	 * @param el
	 * @param labels
	 * @return
	 */
	private boolean hasTitle(Element el) {
		return el.hasAttribute("title"); //$NON-NLS-1$
	}

	private boolean hasBlankTitle(Element el) {
		if (!el.hasAttribute("title"))
			return true;
		String title = el.getAttribute("title");
		return title.matches("^[\\s\u3000]*$");
	}

	// For new JIS
	private boolean isEmptyString(String s) {
		return (s == null || s.length() == 0);
	}
}
