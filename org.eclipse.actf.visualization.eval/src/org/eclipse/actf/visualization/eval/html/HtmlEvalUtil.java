/*******************************************************************************
 * Copyright (c) 2005, 2024 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.eval.html;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.actf.model.dom.dombyjs.IStyleSheet;
import org.eclipse.actf.model.dom.dombyjs.IStyleSheets;
import org.eclipse.actf.util.xpath.XPathService;
import org.eclipse.actf.util.xpath.XPathServiceFactory;
import org.eclipse.actf.visualization.eval.html.statistics.FlashData;
import org.eclipse.actf.visualization.eval.html.statistics.HeadingsData;
import org.eclipse.actf.visualization.eval.html.statistics.ImageStatData;
import org.eclipse.actf.visualization.eval.html.statistics.PageData;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLImageElement;

/**
 * Utility class for HTML evaluation
 */
public class HtmlEvalUtil extends HtmlTagUtil {

	private static final boolean PERFORMANCE_DEBUG = false;

	private static final int LONG_TEXT_NUM = 250; // TODO check

	private static final String[] HEADING_LEVEL = { "h1", "h2", "h3", "h4", "h5", "h6" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

	public static final String[] EVENT_MOUSE_BUTTON = { ATTR_ONCLICK, ATTR_ONDBLCLICK, ATTR_ONMOUSEUP,
			ATTR_ONMOUSEDOWN };

	public static final String[] EVENT_MOUSE_FOCUS = { ATTR_ONMOUSEOVER, ATTR_ONMOUSEOUT, ATTR_ONMOUSEMOVE };

	public static final String[] EVENT_ON_KEY = { ATTR_ONKEYDOWN, ATTR_ONKEYPRESS, ATTR_ONKEYUP };

	public static final String[] EVENT_LOAD = { ATTR_ONLOAD, ATTR_ONUNLOAD, ATTR_ONABORT, ATTR_ONERROR };

	public static final String[] EVENT_WINDOW = { ATTR_ONRESIZE, ATTR_ONMOVE, ATTR_ONDRAGDROP };

	public static final String[] EVENT_FOCUS = { ATTR_ONFOCUS, ATTR_ONBLUR, ATTR_ONSELECT };

	private static final XPathService xpathService = XPathServiceFactory.newService();

	private static final Object EXP1 = xpathService.compile(".//a[@href]"); //$NON-NLS-1$

	private static final Object EXP2 = xpathService.compile("//h1|//h2|//h3|//h4|//h5|//h6"); //$NON-NLS-1$

	private static final String[] ARIA_STATE_PROPERTIES = { "aria-activedescendant", "aria-atomic", "aria-autocomplete",
			"aria-busy", "aria-checked", "aria-colcount", "aria-colindex", "aria-colspan", "aria-controls",
			"aria-current", "aria-describedby", "aria-details", "aria-disabled", "aria-dropeffect", "aria-errormessage",
			"aria-expanded", "aria-flowto", "aria-grabbed", "aria-haspopup", "aria-hidden", "aria-invalid",
			"aria-keyshortcuts", "aria-label", "aria-labelledby", "aria-level", "aria-live", "aria-modal",
			"aria-multiline", "aria-multiselectable", "aria-orientation", "aria-owns", "aria-placeholder",
			"aria-posinset", "aria-pressed", "aria-readonly", "aria-relevant", "aria-required", "aria-roledescription",
			"aria-rowcount", "aria-rowindex", "aria-rowspan", "aria-selected", "aria-setsize", "aria-sort",
			"aria-valuemax", "aria-valuemin", "aria-valuenow", "aria-valuetext" };

	private Document target;

	private Document resultDoc;

	private Document srcDom;

	private Document liveDom;

	private IStyleSheets styleSheets = null;

	private File targetFile;

	private File srcFile;

	private File liveFile;

	private URL baseUrl;

	private Map<Node, Integer> document2IdMap;

	private boolean isDBCS;

	private boolean isLiveDom;

	private boolean hasAwithHref = false;

	private boolean hasJavascript = false;

	private Element[] aWithHref_elements;

	private String[] aWithHref_hrefs;

	private String[] aWithHref_strings;

	private HTMLImageElement[] img_elements;

	private Element[] table_elements;

	private Element[] body_elements;

	private Element[] frame_elements;

	private Element[] iframe_elements;

	private Element[] object_elements;

	private Element[] parent_table_elements;

	private Element[] bottom_data_tables;

	private Element[] bottom_1row1col_tables;

	private Element[] bottom_notdata_tables;

	private Element[] headings;

	private Element[] embed_elements;

	private Element[] script_elements;

	private Element[] javascript_elements;

	private Element[] eventMouseButtonElements; // on Click/Dblclick, onMouse

	// up/down

	private Element[] eventMouseFocusElements; // onMouse over/out/move

	private Element[] eventOnKeyElements; // onKey Down/up/press

	private Element[] eventLoadElements; // on load/unload/abort/error

	private Element[] eventWindowElements; // on Resize/Mode/DragDrop

	private Element[] eventFocusElements; // on Focus/Blur/Select

	private Element[] javascriptHref_elements;

	private Element[] customElements;

	private String[] javascriptHref_hrefs;

	private String[] javascriptHref_strings;

	private String curUrl;

	private double invalidLinkRatio;

	private PageData pageData;

	// private int invisibleElementCount = 0;
	//
	// private String[] invisibleLinkStrings = new String[0];
	//
	// private HashSet<String> notExistHrefSet = new HashSet<String>();

	// for new JIS
	private List<Element> imageButtonList;
	private List<Element> textButtonList;
	private List<Element> areaList;
	private List<Element> appletList;
	private List<Element> accessKeyList;
	private List<Element> styleList;
	private List<Element> styleElementList;
	private List<Element> idElementList;

	// for ARIA
	private List<Element> ariaLabelledbyElementList, ariaLabelElementList, ariaDescribedbyElementList,
			ariaRoleElementList, ariaAssertiveElementList, ariaStatePropertiesElementList;

	/**
	 * Constructor of the class.
	 * 
	 * @param target         target {@link Document}
	 * @param resultDoc      visualization result {@link Document}
	 * @param url            target URL
	 * @param document2IdMap map between {@link Node} and ACTF_ID
	 * @param srcDom         the original source {@link Document}
	 * @param liveDom        the live {@link Document} obtained from browser
	 * @param pageData       the detailed page information as {@link PageData}
	 * @param isDBCS         true if target page uses DBCS
	 * @param isLive         true if target is live DOM
	 */
	public HtmlEvalUtil(Document target, Document resultDoc, String url, Map<Node, Integer> document2IdMap,
			Document srcDom, Document liveDom, PageData pageData, boolean isDBCS, boolean isLive) {
		this(target, resultDoc, url, document2IdMap, srcDom, liveDom, pageData, 0, null, isDBCS, isLive);
	}

	/**
	 * Constructor of the class.
	 * 
	 * @param target                target {@link Document}
	 * @param resultDoc             visualization result {@link Document}
	 * @param url                   target URL
	 * @param document2IdMap        map between {@link Node} and ACTF_ID
	 * @param srcDom                the original source {@link Document}
	 * @param liveDom               the live {@link Document} obtained from browser
	 * @param pageData              the detailed page information as
	 *                              {@link PageData}
	 * @param invisibleElementCount number of invisible Element inside the page
	 * @param invisibleLinkStrings  array of link target urls of invisible anchor
	 *                              Element
	 * @param isDBCS                true if target page uses DBCS
	 * @param isLive                true if target is live DOM
	 */
	@SuppressWarnings("nls")
	private HtmlEvalUtil(Document target, Document resultDoc, String url, Map<Node, Integer> document2IdMap,
			Document srcDom, Document liveDom, PageData pageData, int invisibleElementCount,
			String[] invisibleLinkStrings, boolean isDBCS, boolean isLive) {
		this.target = target;
		this.resultDoc = resultDoc;

		this.srcDom = srcDom;
		this.liveDom = liveDom;
		this.isLiveDom = isLive;

		this.pageData = pageData;

		this.curUrl = url;
		baseUrl = null;
		try {
			baseUrl = new URL(url); // ToDo handle base
		} catch (MalformedURLException e) {
			// e.printStackTrace();
		}

		this.invalidLinkRatio = 0;
		// this.invisibleElementCount = invisibleElementCount;
		// if (invisibleLinkStrings != null) {
		// this.invisibleLinkStrings = invisibleLinkStrings;
		// }

		this.document2IdMap = document2IdMap;
		// this.html2ViewMapData = html2ViewMapData;

		if (PERFORMANCE_DEBUG)
			System.out.println("document2IdMap\t" + (new Date()).getTime());

		this.isDBCS = isDBCS;

		// prepare freq use elements
		// System.out.println(df.format(new Date(System.currentTimeMillis()))
		// + ": checker engine init");

		NodeList tmpNL = xpathService.evalForNodeList(EXP1, target);
		int length = tmpNL.getLength();

		if (length > 0) {
			hasAwithHref = true;
		}

		aWithHref_elements = new Element[length];
		aWithHref_hrefs = new String[length];
		aWithHref_strings = new String[length];

		for (int i = 0; i < length; i++) {
			Element tmpE = (Element) tmpNL.item(i);
			aWithHref_elements[i] = tmpE;
			aWithHref_hrefs[i] = tmpE.getAttribute(ATTR_HREF);
			aWithHref_strings[i] = getTextAltDescendant(tmpE);
			// System.out.println(aWithHref_hrefs[i]);
		}

		// System.out.println(df.format(new Date(System.currentTimeMillis()))
		// + ": href fin");

		tmpNL = target.getElementsByTagName("img"); //$NON-NLS-1$
		length = tmpNL.getLength();
		img_elements = new HTMLImageElement[length];
		Vector<ImageStatData> tmpV = new Vector<ImageStatData>();
		HashMap<HTMLImageElement, ImageStatData> tmpMap = new HashMap<HTMLImageElement, ImageStatData>();
		HashMap<Element, ImageStatData> linkImgMap = new HashMap<Element, ImageStatData>();
		for (int i = 0; i < length; i++) {
			img_elements[i] = (HTMLImageElement) tmpNL.item(i);
			ImageStatData isd = new ImageStatData(img_elements[i], baseUrl);
			tmpV.add(isd);
			tmpMap.put(img_elements[i], isd);
			if (isd.getAncestorLink() != null) {
				linkImgMap.put(isd.getAncestorLink(), isd);
			}
		}
		pageData.setImageData(tmpV);
		pageData.setImageDataMap(tmpMap);
		pageData.setLinkImageDataMap(linkImgMap);

		if (PERFORMANCE_DEBUG)
			System.out.println("process images\t" + (new Date()).getTime());

		// TODO use XPath
		tmpNL = target.getElementsByTagName("table"); //$NON-NLS-1$
		length = tmpNL.getLength();
		table_elements = new Element[length];
		Vector<Element> bottomV = new Vector<Element>();
		Vector<Element> parentV = new Vector<Element>();
		Vector<Element> b1row1colV = new Vector<Element>();
		Vector<Element> bNotDataV = new Vector<Element>();
		for (int i = 0; i < length; i++) {
			table_elements[i] = (Element) tmpNL.item(i);
			if (table_elements[i].getElementsByTagName("table").getLength() //$NON-NLS-1$
					== 0) {
				if (is1Row1ColTable(table_elements[i])) {
					b1row1colV.add(table_elements[i]);
				} else if (isDataTable(table_elements[i])) {
					bottomV.add(table_elements[i]);
				} else {
					bNotDataV.add(table_elements[i]);
				}
			} else {
				parentV.add(table_elements[i]);
			}
		}
		bottom_data_tables = new Element[bottomV.size()];
		bottom_1row1col_tables = new Element[b1row1colV.size()];
		bottom_notdata_tables = new Element[bNotDataV.size()];
		parent_table_elements = new Element[parentV.size()];
		bottomV.toArray(bottom_data_tables);
		b1row1colV.toArray(bottom_1row1col_tables);
		bNotDataV.toArray(bottom_notdata_tables);
		parentV.toArray(parent_table_elements);

		if (PERFORMANCE_DEBUG)
			System.out.println("process tables\t" + (new Date()).getTime());

		body_elements = getElementsArray(target, "body");
		frame_elements = getElementsArray(target, "frame");
		iframe_elements = getElementsArray(target, "iframe");

		if (PERFORMANCE_DEBUG)
			System.out.println("process frames\t" + (new Date()).getTime());

		HashSet<Element> embedInObjectSet = new HashSet<Element>();

		// TODO ieDOM
		tmpNL = target.getElementsByTagName("object"); //$NON-NLS-1$
		length = tmpNL.getLength();
		object_elements = new Element[length];
		for (int i = 0; i < length; i++) {
			object_elements[i] = (Element) tmpNL.item(i);
			if (FLASH_OBJECT.equalsIgnoreCase(object_elements[i].getAttribute("classid"))) {
				// TODO check codebase
				// TODO get width hight align ... loop quality...
				NodeList paramNL = object_elements[i].getElementsByTagName("param");
				String src = "";
				for (int j = 0; j < paramNL.getLength(); j++) {
					try {
						Element tmpE = (Element) paramNL.item(j);
						String name = tmpE.getAttribute("name");
						String value = tmpE.getAttribute("value");
						if (name.equalsIgnoreCase("movie")) {
							src = value;
						}
					} catch (Exception e) {

					}
				}
				if (src != null && src.length() > 0) {
					FlashData flashD = new FlashData(object_elements[i], src, true);
					pageData.addFlashData(flashD);

					NodeList embedNL = object_elements[i].getElementsByTagName("embed");
					for (int j = 0; j < embedNL.getLength(); j++) {
						Element tmpE = (Element) embedNL.item(j);
						if (FLASH_TYPE.equalsIgnoreCase(tmpE.getAttribute("type"))) {
							// TODO check PLUGINSPAGE
							// TODO get width hight align ... loop quality...

							String src2 = tmpE.getAttribute("src");
							if (src2 != null && src2.length() > 0) {
								if (src.equalsIgnoreCase(src2)) {
									embedInObjectSet.add(tmpE);
									flashD.setWithEmbed(true);
								} else {
									pageData.addFlashData(new FlashData(tmpE, src, false));
								}
							}
						}
					}

				}

			}
		}

		embed_elements = getElementsArray(target, "embed");
		for (int i = 0; i < embed_elements.length; i++) {
			Element tmpE = embed_elements[i];
			if (!embedInObjectSet.contains(tmpE) && FLASH_TYPE.equals(tmpE.getAttribute("type"))) {
				// TODO check PLUGINSPAGE
				// TODO get width hight align ... loop quality...
				String src = tmpE.getAttribute("src");
				if (src != null && src.length() > 0) {
					pageData.addFlashData(new FlashData(tmpE, src, false));
				}
			}
		}

		if (PERFORMANCE_DEBUG)
			System.out.println("process object\t" + (new Date()).getTime());

		NodeList headingsNL = xpathService.evalForNodeList(EXP2, target);
		length = headingsNL.getLength();
		Vector<HeadingsData> tmpV2 = new Vector<HeadingsData>();
		headings = new Element[headingsNL.getLength()];
		for (int i = 0; i < length; i++) {
			Element tmpE = (Element) headingsNL.item(i);
			headings[i] = tmpE;
			tmpV2.add(new HeadingsData(tmpE, getTextAltDescendant(tmpE)));
		}
		pageData.setHeadingsData(tmpV2);

		customElements = getElementsArrayByXPath(target, "//*[contains(local-name(),\"-\")]");

		if (PERFORMANCE_DEBUG)
			System.out.println("process headins\t" + (new Date()).getTime());

		collectScriptElements();
		if (PERFORMANCE_DEBUG)
			System.out.println("collectScriptElements\t" + (new Date()).getTime());
		/*
		 * calcDomDifference(); if (PERFORMANCE_DEBUG)
		 * System.out.println("calcDomDifference\t" + (new Date()).getTime());
		 */
	}

	/**
	 * Utility function to evaluate XPath and returns a List instance instead of a
	 * NodeList instance.
	 * 
	 * @param xpath   XPath expression
	 * @param context context node for evaluation
	 * @return {@link List} of elements (XPath evaluation results)
	 */
	public static List<Element> getElementsListByXPath(String xpath, Node context) {
		NodeList tmpNL = xpathService.evalPathForNodeList(xpath, context);
		int length = tmpNL.getLength();
		List<Element> elements = new ArrayList<>();
		for (int i = 0; i < length; i++) {
//			System.out.println((Element) tmpNL.item(i));
			elements.add((Element) tmpNL.item(i));
		}
		return elements;
	}

	private Element[] getElementsArray(Document target, String tagName) {
		NodeList tmpNL = target.getElementsByTagName(tagName);
		int length = tmpNL.getLength();
		Element[] result = new Element[length];
		for (int i = 0; i < length; i++) {
			result[i] = (Element) tmpNL.item(i);
		}
		return (result);
	}

	/**
	 * Utility function similar to getElementsByTagName() that returns a List
	 * instance instead of a NodeList instance.
	 * 
	 * @param el       an Element or Document instance
	 * @param tagName  the name of element which you want look for
	 * @param tagNames optional list of element names which you want look for
	 * @return {@link List} of elements with given tag name that are descendants of
	 *         the node.
	 */
	// for new JIS
	public List<Element> getElementsList(Node node, String tagName, String... tagNames) {
		List<Element> nodes = new ArrayList<Element>();
		NodeList nl = null;
		if (node instanceof Document)
			nl = ((Document) node).getElementsByTagName(tagName);
		else if (node instanceof Element)
			nl = ((Element) node).getElementsByTagName(tagName);
		for (int i = 0; i < nl.getLength(); i++) {
			nodes.add((Element) nl.item(i));
		}

		// variable argument
		if (tagNames.length > 0) {
			for (int i = 0; i < tagNames.length; i++) {
				nodes.addAll(getElementsList(node, tagNames[i]));
			}
		}
		return nodes;
	}

	private Element[] getElementsArrayByXPath(Document target, String xpath) {
		NodeList tmpNL = xpathService.evalPathForNodeList(xpath, target);
		int length = tmpNL.getLength();
		Element[] result = new Element[length];
		for (int i = 0; i < length; i++) {
			Element tmpE = (Element) tmpNL.item(i);
			result[i] = tmpE;
		}
		return result;
	}

	@SuppressWarnings("nls")
	private void collectScriptElements() {
		script_elements = getElementsArray(target, "script");

		javascript_elements = getElementsArrayByXPath(target, "//script[@type=\"text/javascript\"]");

		// allEventElements = getElementsArrayByXPath(
		// target,
		// "//*[@onclick or @ondblclick or @onmouseup or @onmousedown or
		// @onmouseover or @onmouseout or @onmousemove or
		// @onkeydown or @onkeyup or @onkeypress or @onload or @onunload or
		// @onabort or @onerror or @onresize or @onmove
		// or @ondragdrop or @onfocus or @onblur or @onselect]");

		eventMouseButtonElements = getElementsArrayByXPath(target,
				"//*[@onclick or @ondblclick or @onmouseup or @onmousedown]");
		eventMouseFocusElements = getElementsArrayByXPath(target, "//*[@onmouseover or @onmouseout or @onmousemove]");
		eventOnKeyElements = getElementsArrayByXPath(target, "//*[@onkeydown or @onkeyup or @onkeypress]");
		eventLoadElements = getElementsArrayByXPath(target, "//*[@onload or @onunload or @onabort or @onerror]");
		eventWindowElements = getElementsArrayByXPath(target, "//*[@onresize or @onmove or @ondragdrop]");
		eventFocusElements = getElementsArrayByXPath(target, "//*[@onfocus or @onblur or @onselect]");

		Vector<Element> tmpV1 = new Vector<Element>();
		Vector<String> tmpV2 = new Vector<String>();
		Vector<String> tmpV3 = new Vector<String>();
		for (int i = 0; i < aWithHref_hrefs.length; i++) {
			if (aWithHref_hrefs[i].startsWith("javascript:")) {
				tmpV1.add(aWithHref_elements[i]);
				tmpV2.add(aWithHref_hrefs[i]);
				tmpV3.add(aWithHref_strings[i]);
			}
		}

		int size = tmpV1.size();
		javascriptHref_elements = new Element[size];
		javascriptHref_hrefs = new String[size];
		javascriptHref_strings = new String[size];
		tmpV1.toArray(javascriptHref_elements);
		tmpV2.toArray(javascriptHref_hrefs);
		tmpV3.toArray(javascriptHref_strings);

		int javascriptNum = javascript_elements.length + eventFocusElements.length + eventLoadElements.length
				+ eventMouseButtonElements.length + eventMouseFocusElements.length + eventOnKeyElements.length
				+ eventWindowElements.length + javascriptHref_hrefs.length;

		hasJavascript = (javascriptNum > 0);
		pageData.setHasJavascript(hasJavascript);
	}

	// private void calcDomDifference() {
	//
	// if (EvaluationUtil.isOriginalDOM()) {
	// // target = orig DOM
	// if (isLiveDom || null == liveDom) {
	// // parse error
	// return;
	// }
	//
	// TreeSet<String> existSet = new TreeSet<String>(
	// Arrays.asList(aWithHref_hrefs));
	// // trim()?
	//
	// for (String href : aWithHref_hrefs) {
	// if (!href.startsWith("http://") && !href.startsWith("https://")) {
	// try {
	// existSet.add(new URL(baseUrl, href).toString());
	// // System.out.println(href +" : "+new
	// // URL(baseUrl,href));
	// } catch (MalformedURLException e) {
	// }
	// }
	// }
	//
	// /*
	// * NodeList ieNL = xpathService.evalForNodeList(EXP1, liveDom); int
	// * size = ieNL.getLength();
	// */
	//
	// NodeList ieNL = liveDom.getElementsByTagName("a");
	// int size = ieNL.getLength();
	//
	// for (int i = 0; i < size; i++) {
	// Element tmpE = (Element) ieNL.item(i);
	// if (!tmpE.hasAttribute(ATTR_HREF)) {
	// continue;
	// }
	// String tmpS = tmpE.getAttribute(ATTR_HREF);
	// if (!existSet.contains(tmpS)) {
	// // System.out.println("ie:"+tmpS);
	// notExistHrefSet.add(tmpS);
	// }
	// }
	// } else {
	// // target = IE DOM
	// NodeList orgNL = xpathService.evalForNodeList(EXP1, srcDom);
	// int size = orgNL.getLength();
	// TreeSet<String> existSet = new TreeSet<String>();
	// for (int i = 0; i < size; i++) {
	// existSet.add(((Element) orgNL.item(i)).getAttribute(ATTR_HREF));
	// // System.out.println("Src:"+((Element)
	// // orgNL.item(i)).getAttribute(ATTR_HREF));
	// }
	//
	// size = aWithHref_hrefs.length;
	// for (int i = 0; i < size; i++) {
	// if (!existSet.contains(aWithHref_hrefs[i])) {
	// notExistHrefSet.add(aWithHref_hrefs[i]);
	// }
	// // System.out.println("IE:"+aWithHref_hrefs[i]);
	// }
	//
	// }
	//
	// }

	private boolean is1Row1ColTable(Element el) {
		NodeList cellNl = el.getElementsByTagName("tr"); //$NON-NLS-1$
		if (cellNl.getLength() <= 1) {
			return true;
		} else {
			boolean bMultiCol = false;
			int length = cellNl.getLength();
			for (int i = 0; i < length; i++) {
				NodeList thNl = ((Element) cellNl.item(i)).getElementsByTagName("th"); //$NON-NLS-1$
				NodeList tdNl = ((Element) cellNl.item(i)).getElementsByTagName("td"); //$NON-NLS-1$
				if ((thNl.getLength() + tdNl.getLength()) > 1) {
					bMultiCol = true;
					break;
				}
			}
			if (!bMultiCol) {
				return true;
			}
		}
		return false;
	}

	/**
	 * If this method returns <code>true</code>, this table is a data table.
	 * Otherwise, this table is a layout table.
	 * 
	 * @param table
	 * @return boolean indicating if this table is a data table.
	 */
	private boolean isDataTable(Element el) {
		if (hasFormControl(el)) {
			return false;
		}

		NodeList cellNl = el.getElementsByTagName("td"); //$NON-NLS-1$
		if (cellNl.getLength() == 0) {
			return false;
		} else {
			int length = cellNl.getLength();
			for (int j = 0; j < length; j++) {
				if (!isDataCell((Element) cellNl.item(j))) {
					return false;
				}
			}

			cellNl = el.getElementsByTagName("th"); //$NON-NLS-1$
			length = cellNl.getLength();
			for (int j = 0; j < length; j++) {
				if (!isDataCell((Element) cellNl.item(j))) {
					return false;
				}
			} // image?
		}

		return true;
	}

	private boolean hasFormControl(Element formEl) {
		NodeList nl = formEl.getElementsByTagName("form"); //$NON-NLS-1$
		if (nl.getLength() > 0)
			return true;
		nl = formEl.getElementsByTagName("input"); //$NON-NLS-1$
		if (nl.getLength() > 0)
			return true;
		nl = formEl.getElementsByTagName("select"); //$NON-NLS-1$
		if (nl.getLength() > 0)
			return true;
		nl = formEl.getElementsByTagName("textarea"); //$NON-NLS-1$
		if (nl.getLength() > 0)
			return true;
		nl = formEl.getElementsByTagName("html:text"); //$NON-NLS-1$
		if (nl.getLength() > 0)
			return true;
		nl = formEl.getElementsByTagName("html:radio"); //$NON-NLS-1$
		if (nl.getLength() > 0)
			return true;
		return false;
	}

	private boolean isDataCell(Element el) {
		if (getTextAltDescendant(el).length() > LONG_TEXT_NUM) {
			return false;
		}
		// NodeList aNl = el.getElementsByTagName("a"); //$NON-NLS-1$
		// NodeList liNl = el.getElementsByTagName("li"); //$NON-NLS-1$
		// NodeList imgNl = el.getElementsByTagName("img"); //$NON-NLS-1$
		// if ((aNl.getLength() + liNl.getLength() + imgNl.getLength()) > 3) {
		// return false;
		// }
		NodeList imgNl = el.getElementsByTagName("img"); //$NON-NLS-1$
		if (imgNl.getLength() > 10) {
			return false;
		}

		return true;
	}

	/**
	 * Get heading level as int
	 * 
	 * @param strNodeName target Heading tag name (H1, H2,..., H6)
	 * @return heading level as int (1, 2,..., 6)
	 */
	public int getHeadingLevel(String strNodeName) {
		for (int i = 0; i < HEADING_LEVEL.length; i++) {
			if (strNodeName.equalsIgnoreCase(HEADING_LEVEL[i])) {
				return Integer.valueOf(strNodeName.substring(1)).intValue();
			}
		}
		return 0;
	}

	/**
	 * Get all elements that has accessKey.
	 * 
	 * @return
	 */
	public List<Element> getAccessKeyElements() {
		if (accessKeyList == null) {
			accessKeyList = getElementsListByXPath("//*[@accesskey]", target);
		}
		return accessKeyList;
	}

	/**
	 * Get all elements that has style attribute.
	 * 
	 * @return
	 */
	public List<Element> getElementsWithStyle() {
		if (styleList == null) {
			styleList = getElementsListByXPath("//*[@style]", target);
		}
		return styleList;
	}

	/**
	 * Get all elements that has id attribute.
	 * 
	 * @return
	 */
	public List<Element> getElementsWithId() {
		if (idElementList == null) {
			// limit to elements under body
			idElementList = getElementsListByXPath("//body/*[@id]", target);
		}
		return idElementList;
	}

	/**
	 * Get all elements that have aria-labelledby attribute.
	 * 
	 * @return
	 */
	public List<Element> getElementsWithAriaLabelledBy() {
		if (ariaLabelledbyElementList == null) {
			ariaLabelledbyElementList = getElementsListByXPath("//*[@aria-labelledby]", target);
		}
		return ariaLabelledbyElementList;
	}

	/**
	 * Get all elements that have aria-label attribute.
	 * 
	 * @return
	 */
	public List<Element> getElementsWithAriaLabel() {
		if (ariaLabelElementList == null) {
			ariaLabelElementList = getElementsListByXPath("//*[@aria-label]", target);
		}
		return ariaLabelElementList;
	}

	/**
	 * Get all elements that have aria-describedby attribute.
	 * 
	 * @return
	 */
	public List<Element> getElementsWithAriaDescribedby() {
		if (ariaDescribedbyElementList == null) {
			ariaDescribedbyElementList = getElementsListByXPath("//*[@aria-describedby]", target);
		}
		return ariaDescribedbyElementList;
	}

	/**
	 * Get all elements that have role (ARIA role) attribute.
	 * 
	 * @return
	 */
	public List<Element> getElementsWithAriaRole() {
		if (ariaRoleElementList == null) {
			ariaRoleElementList = getElementsListByXPath("//*[@role]", target);
		}
		return ariaRoleElementList;
	}

	/**
	 * Get all elements that have (aria-live="assertive") attribute.
	 * 
	 * @return
	 */
	public List<Element> getElementsWithAriaLiveAssertive() {
		if (ariaAssertiveElementList == null) {
			ariaAssertiveElementList = getElementsListByXPath("//*[@aria-live='assertive']", target);
		}
		return ariaAssertiveElementList;
	}

	/**
	 * Get all elements that have ARIA state and properties attribute.
	 * 
	 * @return
	 */
	public List<Element> getElementsWithARIAStateProperties() {
		if (ariaStatePropertiesElementList == null) {
			HashSet<Element> tmpSet = new HashSet<>();
			for (String targetS : ARIA_STATE_PROPERTIES) {
				tmpSet.addAll(getElementsListByXPath("//*[@" + targetS + "]", target));
			}
			ariaStatePropertiesElementList = new ArrayList<>(tmpSet);
		}

		// ariaStatePropertiesElementList = getElementsListByXPath("//*[@aria-*]",
		// target);

		return ariaStatePropertiesElementList;
	}

	/**
	 * Get all style elements.
	 * 
	 * @return
	 */
	public List<Element> getStyleElements() {
		if (styleElementList == null) {
			styleElementList = new ArrayList<Element>();
			for (Element applet : getElementsList(target, "style"))
				styleElementList.add(applet);
		}
		return styleElementList;
	}

	/**
	 * Get all applet elements.
	 * 
	 * @return
	 */
	public List<Element> getAppletElements() {
		if (appletList == null) {
			appletList = getElementsList(target, "applet");
		}
		return appletList;
	}

	/**
	 * Get all area elements.
	 * 
	 * @return
	 */
	public List<Element> getAreaElements() {
		if (areaList == null) {
			areaList = new ArrayList<Element>();
			for (Element area : getElementsList(target, "area"))
				areaList.add(area);
		}
		return areaList;
	}

	/**
	 * Get array of anchor {@link Element} who has href attribute
	 * 
	 * @return array of anchor {@link Element} who has href attribute
	 */
	public Element[] getAWithHref_elements() {
		return aWithHref_elements;
	}

	/**
	 * Get target hrefs of anchor Elements
	 * 
	 * @return target hrefs
	 */
	public String[] getAWithHref_hrefs() {
		return aWithHref_hrefs;
	}

	/**
	 * Get text descendant of anchor {@link Element} who has href attribute
	 * 
	 * @return text descendant of anchor {@link Element} who has href attribute
	 * 
	 * @see HtmlTagUtil#getTextAltDescendant(Node)
	 */
	public String[] getAWithHref_strings() {
		return aWithHref_strings;
	}

	/**
	 * Get base URL of the target page
	 * 
	 * @return base URL
	 */
	public URL getBaseUrl() {
		return baseUrl;
	}

	/**
	 * Get body {@link Element}
	 * 
	 * @return body {@link Element}
	 */
	public Element[] getBody_elements() {
		return body_elements;
	}

	/**
	 * Get tables who has 1 row and 1 column
	 * 
	 * @return table {@link Element} who has 1 row and 1 column
	 */
	public Element[] getBottom_1row1col_tables() {
		return bottom_1row1col_tables;
	}

	/**
	 * Get bottom data tables in the nested table
	 * 
	 * @return bottom data tables in the nested table
	 */
	public Element[] getBottom_data_tables() {
		return bottom_data_tables;
	}

	/**
	 * Get bottom tables (not data table) in the nested table
	 * 
	 * @return bottom tables in the nested table
	 */
	public Element[] getBottom_notdata_tables() {
		return bottom_notdata_tables;
	}

	/**
	 * Get URL of the page
	 * 
	 * @return URL of the page
	 */
	public String getUrl() {
		return curUrl;
	}

	/**
	 * Get map between {@link Node} and ACTF_ID
	 * 
	 * @return map between {@link Node} and ACTF_ID
	 */
	public Map<Node, Integer> getDocument2IdMap() {
		return document2IdMap;
	}

	/**
	 * Get frame {@link Element} in the page
	 * 
	 * @return frame elements
	 */
	public Element[] getFrame_elements() {
		return frame_elements;
	}

	/**
	 * Check if the page has
	 * 
	 * <pre>
	 * &lt;a href=&quot;&quot;&gt;
	 * </pre>
	 * 
	 * @return true if the page has anchor with href attribute
	 */
	public boolean isHasAwithHref() {
		return hasAwithHref;
	}

	/**
	 * Check if the page uses JavaScript
	 * 
	 * @return true if the page uses JavaScript
	 */
	public boolean isHasJavascript() {
		return hasJavascript;
	}

	/**
	 * Get all heading {@link Element} in the page
	 * 
	 * @return all heading elements
	 */
	public Element[] getHeadings() {
		return headings;
	}

	/**
	 * Get live DOM
	 * 
	 * @return live DOM
	 */
	public Document getLiveDom() {
		return liveDom;
	}

	/**
	 * Get all iframe {@link Element} in the page
	 * 
	 * @return all iframe elements
	 */
	public Element[] getIframe_elements() {
		return iframe_elements;
	}

	/**
	 * Get all img {@link Element} in the page
	 * 
	 * @return all img elements
	 */
	public HTMLImageElement[] getImg_elements() {
		return img_elements;
	}

	/**
	 * Get all image button (input elements whose type is "image").
	 * 
	 * @return
	 */
	// for new JIS
	public List<Element> getImageButtons() {
		if (imageButtonList != null)
			return imageButtonList;

		imageButtonList = new ArrayList<Element>();
		for (Element input : getElementsList(target, "input")) {
			if (input.getAttribute("type").equals("image"))
				imageButtonList.add(input);
		}
		return imageButtonList;
	}

	/**
	 * Get all text-based button.
	 * 
	 * @return
	 */
	// TODO treat button elements...
	//
	public List<Element> getTextButtons() {
		if (textButtonList == null) {
			textButtonList = new ArrayList<Element>();
			for (Element input : getElementsList(target, "input")) {
				if (input.getAttribute("type").matches("button|submit|reset"))
					textButtonList.add(input);
			}
		}
		return textButtonList;
	}

	/**
	 * Get invalid link ratio of the page. (target URL number under invisible
	 * link/all target URL number)
	 * 
	 * @return invalid link ratio
	 */
	public double getInvalidLinkRatio() {
		return invalidLinkRatio;
	}

	/**
	 * Get number of invisible {@link Element}
	 * 
	 * @return number of invisible elements
	 * @deprecated
	 */
	public int getInvisibleElementCount() {
		// return invisibleElementCount;
		return 0;
	}

	/**
	 * Get array of link target urls of invisible anchor {@link Element}
	 * 
	 * @return array of link target urls of invisible anchor elements
	 * @deprecated
	 */
	public String[] getInvisibleLinkStrings() {
		// return invisibleLinkStrings;
		return new String[0];
	}

	/**
	 * Check if the target page uses DBCS
	 * 
	 * @return true if the target page uses DBCS
	 */
	public boolean isDBCS() {
		return isDBCS;
	}

	/**
	 * Check if the target DOM is live DOM
	 * 
	 * @return true if the target DOM is live DOM
	 */
	public boolean isLiveDom() {
		return isLiveDom;
	}

	/**
	 * Get Set of target URL that are not included in source DOM but exist in live
	 * DOM. (might be inaccessible without JavaScript)
	 * 
	 * @return
	 * @deprecated
	 */
	public HashSet<String> getNotExistHrefSet() {
		// return notExistHrefSet;
		return new HashSet<String>();
	}

	/**
	 * Get all object {@link Element} in the page
	 * 
	 * @return object elements
	 */
	public Element[] getObject_elements() {
		return object_elements;
	}

	/**
	 * Get source DOM
	 * 
	 * @return source DOM
	 */
	public Document getSrcDom() {
		return srcDom;
	}

	/**
	 * Get target page information as {@link PageData}
	 * 
	 * @return target page information
	 */
	public PageData getPageData() {
		return pageData;
	}

	/**
	 * Get array of parent table elements of nested tables.
	 * 
	 * @return array of parent table elements of nested tables
	 */
	public Element[] getParent_table_elements() {
		return parent_table_elements;
	}

	/**
	 * Get visualization result {@link Document}
	 * 
	 * @return result {@link Document}
	 */
	public Document getResult() {
		return resultDoc;
	}

	/**
	 * Get all table {@link Element} in the page
	 * 
	 * @return all table elements
	 */
	public Element[] getTable_elements() {
		return table_elements;
	}

	/**
	 * Get target {@link Document}
	 * 
	 * @return target {@link Document}
	 */
	public Document getTarget() {
		return target;
	}

	/**
	 * Get all embed {@link Element} in the page
	 * 
	 * @return all embed elements
	 */
	public Element[] getEmbed_elements() {
		return embed_elements;
	}

	/**
	 * Get all
	 * 
	 * <pre>
	 * &lt;a href=&quot;javascript:...&quot;
	 * </pre>
	 * 
	 * elements
	 * 
	 * @return all anchor elements for JavaScript
	 */
	public Element[] getJavascriptHref_elements() {
		return javascriptHref_elements;
	}

	/**
	 * Get all href Strings of
	 * 
	 * <pre>
	 * &lt;a href=&quot;javascript:...&quot;
	 * </pre>
	 * 
	 * elements
	 * 
	 * @return all target arguments of anchor elements for JavaScript
	 */
	public String[] getJavascriptHref_hrefs() {
		return javascriptHref_hrefs;
	}

	/**
	 * Get text descendant of anchor {@link Element} for JavaScript
	 * 
	 * @return text descendant of anchor {@link Element} for JavaScript
	 * 
	 * @see HtmlTagUtil#getTextAltDescendant(Node)
	 */
	public String[] getJavascriptHref_strings() {
		return javascriptHref_strings;
	}

	/**
	 * Get array of {@link Element} that has event handler (onload, onunload,
	 * onabort or onerror)
	 * 
	 * @return array of elements that have event handler for onload, onunload,
	 *         onabort or onerror
	 */
	public Element[] getEventLoadElements() {
		return eventLoadElements;
	}

	/**
	 * Get array of {@link Element} that has mouse event handler (onclick,
	 * ondblclick, onmouseup or onmousedown)
	 * 
	 * @return array of elements that have mouse event handler
	 */
	public Element[] getEventMouseButtonElements() {
		return eventMouseButtonElements;
	}

	/**
	 * Get array of {@link Element} that has onmouse event handler (onmouseover,
	 * onmouseout or onmousemove)
	 * 
	 * @return array of elements that have onmouse event handler
	 */
	public Element[] getEventOnMouseElements() {
		return eventMouseFocusElements;
	}

	/**
	 * Get array of {@link Element} that has onkey event handler (onkeydown, onkeyup
	 * or onkeypress)
	 * 
	 * @return array of elements that have onkey event handler
	 */
	public Element[] getEventOnKeyElements() {
		return eventOnKeyElements;
	}

	/**
	 * Get all script {@link Element}
	 * 
	 * @return all script elements
	 */
	public Element[] getScript_elements() {
		return script_elements;
	}

	/**
	 * Get array of {@link Element} that has window event handler (onresize, onmove
	 * or ondragdrop)
	 * 
	 * @return array of elements that have window event handler
	 */
	public Element[] getEventWindowElements() {
		return eventWindowElements;
	}

	/**
	 * Get array of {@link Element} that has focus event handler (onfocus, onblur or
	 * onselect)
	 * 
	 * @return array of elements that have focus event handler
	 */
	public Element[] getEventFocusElements() {
		return eventFocusElements;
	}

	/**
	 * Get array of Custom Elements
	 * 
	 * @return array of custom elements
	 */
	public Element[] getCustomElements() {
		return customElements;
	}

	/**
	 * Get evaluation target HTML file
	 * 
	 * @return evaluation target HTML file
	 */
	public File getTargetFile() {
		return targetFile;
	}

	/**
	 * Set evaluation target HTML file
	 * 
	 */
	public void setTargetFile(File targetFile) {
		this.targetFile = targetFile;
	}

	/**
	 * Get source HTML file
	 * 
	 * @return source HTML file
	 */
	public File getSrcFile() {
		return srcFile;
	}

	/**
	 * Set source HTML file
	 * 
	 */
	public void setSrcFile(File srcFile) {
		this.srcFile = srcFile;
	}

	/**
	 * Get HTML file that represents live DOM
	 * 
	 * @return live DOM as HTML file
	 */
	public File getLiveFile() {
		return liveFile;
	}

	/**
	 * Set HTML file that represents live DOM
	 * 
	 */
	public void setLiveFile(File liveFile) {
		this.liveFile = liveFile;
	}

	/**
	 * Get StyleSheets information from live DOM
	 * 
	 * @return {@link IStyleSheet} objects in the document.
	 */
	public IStyleSheets getStyleSheets() {
		return styleSheets;
	}

	/**
	 * Set StyleSheets information from live DOM
	 * 
	 */
	public void setStyleSheets(IStyleSheets styleSheets) {
		this.styleSheets = styleSheets;
	}

	/**
	 * Append an error icon to an element in the blind view. If it is already
	 * appended or it failed to append, returns false.
	 * 
	 * @param pitem
	 * @param original
	 * @return
	 */
	public boolean appendErrorIcon(IProblemItem pitem, Element original) {
		try {
			String id = document2IdMap.get(original).toString();
			Element tmpE = resultDoc.getElementById("id" + id);
			if (tmpE != null && "area".equalsIgnoreCase(tmpE.getTagName())) {
				tmpE = resultDoc.getElementById("id" + id + "-span");
			}
			if (tmpE != null && tmpE.getElementsByTagName("img").getLength() == 0) {
				Element errorImg = resultDoc.createElement("img");
				errorImg.setAttribute(ATTR_ALT, "error icon");
				errorImg.setAttribute(ATTR_SRC, "img/exclawhite21.gif");
				errorImg.setAttribute(ATTR_TITLE, pitem.getDescription());

				String comment = pitem.getDescription();
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

				String tmpS = comment_sb.toString().replaceAll("\n", "").replaceAll("\r", "");

				errorImg.setAttribute("onmouseover", "updateBaloon2(\"id" + id + "\",\"" + tmpS + "\");");
				tmpE.appendChild(errorImg);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
}
