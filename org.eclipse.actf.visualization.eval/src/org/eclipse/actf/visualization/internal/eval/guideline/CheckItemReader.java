/*******************************************************************************
 * Copyright (c) 2005, 2023 IBM Corporation and Others
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
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.actf.util.logging.DebugPrintUtil;
import org.eclipse.actf.visualization.eval.IEvaluationItem;
import org.eclipse.actf.visualization.eval.IGuidelineItem;
import org.eclipse.actf.visualization.eval.ITechniquesItem;
import org.eclipse.actf.visualization.eval.guideline.GuidelineHolder;
import org.eclipse.actf.visualization.eval.guideline.IGuidelineData;
import org.eclipse.actf.visualization.internal.eval.EvaluationItemImpl;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

@SuppressWarnings("nls")
public class CheckItemReader extends DefaultHandler {
	private static final String CHECKER_CONFIG = "checker-config";

	private static final String CHECK_ITEM = "checkitem";

	private static final String GUIDELINE = "guideline";

	private static final String GITEM = "gItem";

	private static final String MITEM = "mItem";

	private static final String DESC = "desc";

	private static final String ID = "id";

	private static final String NAME = "name";

	private static final String TYPE = "type";

	private static final String METRICS = "metrics";

	private static final String SCORE = "score";

	private static final String DESCRIPTION = "description";

	private static final String TECHNIQUS = "techniques";

	// private static final String LANG = "lang";

	private static final short IN_DEFAULT = 0;

	private static final short IN_GUIDELINE = 1;

	private static final short IN_METRICS = 2;

	private static final short IN_DESCRIPTION = 3;

	public static CheckItemReader parse(InputStream is, GuidelineHolder gh) {
		CheckItemReader cir = new CheckItemReader(gh);
		try {
			SAXParserFactory spfactory = SAXParserFactory.newInstance();
			SAXParser parser = spfactory.newSAXParser();
			parser.parse(is, cir);
		} catch (Exception e) {
		}

		return (cir);
	}

	private HashMap<String, IEvaluationItem> checkItemMap = new HashMap<String, IEvaluationItem>();

	private Stack<Short> statusStack = new Stack<Short>();

	@SuppressWarnings("unused")
	private String curValue = "";

	private EvaluationItemImpl curItem = new EvaluationItemImpl("", "");

	private short status = -1;

	private boolean succeed = false;

	private GuidelineHolder guidelineHolder;

	private Vector<IGuidelineItem> guidelineV = new Vector<IGuidelineItem>();

	private Vector<ITechniquesItem[]> techniquesV = new Vector<ITechniquesItem[]>();

	private IGuidelineData[] guidelines = new IGuidelineData[0];

	private Set<String> metricsSet = new TreeSet<String>(
			new MetricsNameComparator());

	// comparator
	// &
	// metrics.xml

	private Vector<MetricsItem> metricsV = new Vector<MetricsItem>();

	/**
	 * 
	 */
	public CheckItemReader(GuidelineHolder guidelineHolder) {
		this.guidelineHolder = guidelineHolder;
		guidelines = guidelineHolder.getGuidelineData();
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
		// need validation
		succeed = true;
	}

	/**
	 * 
	 */
	public void endElement(String uri, String localName, String qName) {

		if (qName.equals(GITEM)) {
		} else if (qName.equals(MITEM)) {
		} else if (qName.equals(DESC)) {
		} else if (qName.equals(CHECK_ITEM)) {
			checkItemMap.put(curItem.getId(), curItem);
			if (!statusStack.isEmpty()) {
				status = (statusStack.pop()).shortValue();
			}
		} else if (qName.equals(GUIDELINE)) {

			curItem.setGuidelines(guidelineV
					.toArray(new IGuidelineItem[guidelineV.size()]));

			curItem.setTechniques(techniquesV.toArray(new ITechniquesItem[techniquesV.size()][]));

			if (!statusStack.isEmpty()) {
				status = (statusStack.pop()).shortValue();
			}
		} else if (qName.equals(METRICS)) {

			MetricsItem[] mis = new MetricsItem[metricsV.size()];
			metricsV.toArray(mis);
			curItem.setMetrics(mis);

			if (!statusStack.isEmpty()) {
				status = (statusStack.pop()).shortValue();
			}
		} else if (qName.equals(DESCRIPTION)) {
			// TODO
			if (!statusStack.isEmpty()) {
				status = (statusStack.pop()).shortValue();
			}
		} else if (qName.equals(CHECKER_CONFIG)) {

		} else {

			// in metrics
			DebugPrintUtil.devOrDebugPrintln("unknown element(end): " + qName);
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
		if (qName.equals(GITEM)) {
			IGuidelineItem gi = guidelineHolder.getGuidelineItem(
					getValue(NAME, attributes, true),
					getValue(ID, attributes, true));
			// System.out.println(getValue(NAME,attributes)+"
			// "+getValue(ID,attributes)+" : "+gi);
			if (gi != null) {
				guidelineV.add(gi);
				String techs = attributes.getValue(TECHNIQUS);
				if (techs != null) {
					IGuidelineData targetData = null;
					for (IGuidelineData gd : guidelines) {
						if (gd.getGuidelineName().equals(gi.getGuidelineName())) {
							targetData = gd;
							break;
						}
					}
					String[] techsArray = techs.split(",");
					Vector<ITechniquesItem> tempV = new Vector<ITechniquesItem>();
					for (String s : techsArray) {
						ITechniquesItem ti = targetData.getTequniquesItem(s
								.trim());
						if (ti != null) {
							tempV.add(ti);
						}
					}
					techniquesV.add(tempV.toArray(new ITechniquesItem[tempV.size()]));
				} else {
					techniquesV.add(new ITechniquesItem[0]);
				}
			}
		} else if (qName.equals(MITEM)) {
			String metrics = getValue(NAME, attributes, true);
			String scoreS = getValue(SCORE, attributes, true);

			if (metrics.length() > 0) {
				metricsSet.add(metrics);
				metricsV.add(new MetricsItem(metrics, scoreS));
			}
		} else if (qName.equals(CHECK_ITEM)) {

			curItem = new EvaluationItemImpl(getValue(ID, attributes),
					getValue(TYPE, attributes));

			statusStack.push(Short.valueOf(status));
			status = IN_DEFAULT;
		} else if (qName.equals(GUIDELINE)) {

			guidelineV = new Vector<IGuidelineItem>();
			techniquesV = new Vector<ITechniquesItem[]>();

			statusStack.push(Short.valueOf(status));
			status = IN_GUIDELINE;
		} else if (qName.equals(METRICS)) {

			metricsV = new Vector<MetricsItem>();

			statusStack.push(Short.valueOf(status));
			status = IN_METRICS;
		} else if (qName.equals(DESCRIPTION)) {
			statusStack.push(Short.valueOf(status));
			status = IN_DESCRIPTION;
		} else if (qName.equals(CHECKER_CONFIG)) {
		} else {
		}
	}

	private String getValue(String target, Attributes attrs) {
		return (getValue(target, attrs, false));
	}

	private String getValue(String target, Attributes attrs, boolean trim) {
		String result = attrs.getValue(target);
		if (result == null) {
			result = "";
		}
		if (trim) {
			return (result.trim());
		}
		return (result);
	}

	public boolean isSucceed() {
		return succeed;
	}

	public HashMap<String, IEvaluationItem> getCheckItemMap() {
		return checkItemMap;
	}

	public Set<String> getMetricsSet() {
		return metricsSet;
	}
}
