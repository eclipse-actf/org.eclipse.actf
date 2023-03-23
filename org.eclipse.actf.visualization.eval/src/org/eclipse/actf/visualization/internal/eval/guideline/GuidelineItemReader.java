/*******************************************************************************
 * Copyright (c) 2003, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.eval.guideline;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Stack;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.actf.visualization.eval.IGuidelineItem;
import org.eclipse.actf.visualization.eval.ITechniquesItem;
import org.eclipse.actf.visualization.internal.eval.GuidelineItemImpl;
import org.eclipse.actf.visualization.internal.eval.TechniquesItemImpl;
import org.eclipse.ui.PlatformUI;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

@SuppressWarnings("nls")
public class GuidelineItemReader extends DefaultHandler {
	private static final String GUIDELINE = "guideline";

	private static final String MIMETYPES = "mimetypes";

	private static final String MIMETYPE = "mimetype";

	private static final String ITEMS = "items";

	private static final String GITEM = "gItem";

	private static final String TECHNIQUES = "techniques";

	private static final String TECHNIQUE = "technique";

	private static final String LEVELS = "levels";

	private static final String LEVEL = "level";

	private static final String NAME = "name";

	private static final String ID = "id";

	private static final String HELP_URL = "helpUrl";

	private static final String ORDER = "order";

	private static final String DESCRIPTION = "description";

	private static final String CATEGORY = "category";

	// TODO id,level-> attribute url -> lang

	private static final short IN_GUIDELINE = 0;

	private static final short IN_LEVELS = 1;

	private static final short IN_ITEMS = 2;

	private static final short IN_ITEM = 3;

	private static final short IN_MIMETYPES = 4;

	private static final short IN_LEVEL = 5;

	private static final short IN_TECHNIQUES = 6;

	private static final short IN_TECHNIQUE = 7;

	public static GuidelineData getGuidelineData(InputStream is) {
		GuidelineItemReader glir = new GuidelineItemReader();
		try {
			SAXParserFactory spfactory = SAXParserFactory.newInstance();
			SAXParser parser = spfactory.newSAXParser();
			parser.parse(is, glir);
		} catch (Exception e) {
		}
		if (glir.isSucceed() && glir.guidelineName.length() > 0) {
			return (new GuidelineData(glir.guidelineName, glir.order,
					glir.category, glir.description, glir.levels,
					glir.categories, glir.descriptions, glir.mimetypes,
					glir.itemMap, glir.techMap));
		} else {
			// TODO dialog
			return (null);
		}
	}

	private Vector<String> levelsV = new Vector<String>();

	private Vector<String> descriptionsV = new Vector<String>();

	private Vector<String> categoriesV = new Vector<String>();

	private Vector<String> mimeV = new Vector<String>();

	private GuidelineItemImpl curItem = new GuidelineItemImpl("");

	private TechniquesItemImpl curTech = new TechniquesItemImpl();

	private Stack<Short> statusStack = new Stack<Short>();

	private String curValue;

	private HashMap<String, IGuidelineItem> itemMap;

	private HashMap<String, ITechniquesItem> techMap;

	private String guidelineName;

	private int order;

	// for sub-levels
	private String levels[] = new String[0];

	private String descriptions[] = new String[0];

	private String categories[] = new String[0];

	// for root-level guideline
	private String description = "";

	private String category = "";

	private String mimetypes[] = new String[0];

	private short status = IN_GUIDELINE;

	private boolean succeed = false;

	/**
     * 
     */
	public GuidelineItemReader() {
		itemMap = new HashMap<String, IGuidelineItem>();
		techMap = new HashMap<String, ITechniquesItem>();
	}

	/**
     * 
     */
	public void characters(char[] ch, int offset, int length) {

		if (length > 0) {
			curValue += (new String(ch, offset, length));
		}
	}

	/**
     * 
     */
	public void endDocument() {
		levels = new String[levelsV.size()];
		levelsV.toArray(levels);

		categories = new String[categoriesV.size()];
		categoriesV.toArray(categories);

		descriptions = new String[descriptionsV.size()];
		descriptionsV.toArray(descriptions);

		mimetypes = new String[mimeV.size()];
		mimeV.toArray(mimetypes);

		// TODO validation
		succeed = true;
	}

	private String getLocalGuidelineURL(String url) {
		if (url.startsWith("${")) {
			int bundleNameEnd = url.indexOf("}");
			if (bundleNameEnd == -1)
				return null;
			String bundleName = url.substring(2, bundleNameEnd);
			String href = "/" + bundleName + "/"
					+ url.substring(bundleNameEnd + 2);
			return PlatformUI.getWorkbench().getHelpSystem()
					.resolve(href, true).toString();
		}
		return null;
	}

	/**
     * 
     */
	public void endElement(String uri, String localName, String qName) {

		// TODO
		if (qName.equalsIgnoreCase(GITEM)) {
			itemMap.put(curItem.getId(), curItem);
			if (!statusStack.isEmpty()) {
				status = (statusStack.pop()).shortValue();
			}
		} else if (qName.equalsIgnoreCase(TECHNIQUE)) {
			techMap.put(curTech.getId(), curTech);
			if (!statusStack.isEmpty()) {
				status = (statusStack.pop()).shortValue();
			}
		} else if (qName.equalsIgnoreCase(HELP_URL)) {
			String localUrl = getLocalGuidelineURL(curValue);
			switch (status) {
			case IN_ITEM:
				if (localUrl != null)
					curItem.setUrl(localUrl);
				else
					curItem.setUrl(curValue);
				break;
			case IN_TECHNIQUE:
				if (localUrl != null)
					curTech.setUrl(localUrl);
				else
					curTech.setUrl(curValue);
				break;
			default:
			}
			;
		} else if (qName.equalsIgnoreCase(DESCRIPTION)) {
			switch (status) {
			case IN_LEVEL:
				descriptionsV.add(curValue);
				break;
			case IN_GUIDELINE:
				description = curValue;
				break;
			default:
			}
			;

		} else if (qName.equalsIgnoreCase(CATEGORY)) {
			switch (status) {
			case IN_LEVEL:
				categoriesV.add(curValue);
				break;
			case IN_GUIDELINE:
				category = curValue;
				break;
			default:
			}
			;
		} else if (qName.equalsIgnoreCase(GUIDELINE)) {

		} else if (qName.equalsIgnoreCase(MIMETYPES)
				|| qName.equalsIgnoreCase(ITEMS)
				|| qName.equalsIgnoreCase(TECHNIQUES)
				|| qName.equalsIgnoreCase(LEVELS)) {
			if (!statusStack.isEmpty()) {
				status = (statusStack.pop()).shortValue();
			}
		} else if (qName.equalsIgnoreCase(MIMETYPE)) {
			switch (status) {
			case IN_MIMETYPES:
				mimeV.add(curValue);
				break;
			default:
			}
			;
		} else if (qName.equalsIgnoreCase(LEVEL)) {
			if (!statusStack.isEmpty()) {
				status = (statusStack.pop()).shortValue();
			}
			if (levelsV.size() > descriptionsV.size()) {
				descriptionsV.add("");
			}
			if (levelsV.size() > categoriesV.size()) {
				categoriesV.add("");
			}

		} else {
			System.out.println("unknown element(end): " + qName);
		}
	}

	/**
     * 
     */
	public void startDocument() {
	}

	/**
     * 
     */
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) {

		curValue = "";
		if (qName.equalsIgnoreCase(GITEM)) {
			statusStack.push(Short.valueOf(status));
			status = IN_ITEM;
			curItem = new GuidelineItemImpl(guidelineName);
			curItem.setLevel(getAttribute(attributes, LEVEL));
			curItem.setId(getAttribute(attributes, ID));
		} else if (qName.equalsIgnoreCase(TECHNIQUE)) {
			statusStack.push(Short.valueOf(status));
			status = IN_TECHNIQUE;
			curTech = new TechniquesItemImpl();
			curTech.setId(getAttribute(attributes, ID));
			curTech.setGuidelineName(guidelineName);
		} else if (qName.equalsIgnoreCase(HELP_URL)) {

		} else if (qName.equalsIgnoreCase(GUIDELINE)) {
			guidelineName = getAttribute(attributes, NAME);
			getGuidelineOrder(attributes);
		} else if (qName.equalsIgnoreCase(MIMETYPES)) {
			statusStack.push(Short.valueOf(status));
			status = IN_MIMETYPES;
		} else if (qName.equalsIgnoreCase(LEVELS)) {
			statusStack.push(Short.valueOf(status));
			status = IN_LEVELS;
		} else if (qName.equalsIgnoreCase(LEVEL)) {
			statusStack.push(Short.valueOf(status));
			status = IN_LEVEL;
			levelsV.add(getAttribute(attributes, ID));
		} else if (qName.equalsIgnoreCase(ITEMS)) {
			statusStack.push(Short.valueOf(status));
			status = IN_ITEMS;
		} else if (qName.equalsIgnoreCase(TECHNIQUES)) {
			statusStack.push(Short.valueOf(status));
			status = IN_TECHNIQUES;
		} else {

		}

	}

	public boolean isSucceed() {
		return succeed;
	}

	private String getAttribute(Attributes attr, String target) {
		String result = attr.getValue(target);
		if (result == null) {
			result = "";
		}
		return result;
	}

	private void getGuidelineOrder(Attributes attr) {
		String idS = attr.getValue(ORDER);
		order = Integer.MAX_VALUE;

		if (guidelineName != null) {
			try {
				order = Integer.parseInt(idS);
			} catch (Exception e) {
			}
		}
	}

}
