/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.eval.problem;

import java.io.PrintWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

import org.eclipse.actf.util.FileUtils;
import org.eclipse.actf.visualization.eval.IEvaluationItem;
import org.eclipse.actf.visualization.eval.IGuidelineItem;
import org.eclipse.actf.visualization.eval.ITechniquesItem;
import org.eclipse.actf.visualization.eval.guideline.GuidelineHolder;
import org.eclipse.actf.visualization.eval.guideline.IGuidelineData;

public class ReportUtil implements IProblemItemVisitor {

	public static final int CSV = 0;
	public static final int TAB = 1;

	public static final String LINE_SEP = FileUtils.LINE_SEP;
	private static final String COMMA = ",";
	private static final String TAB_STRING = "\t";
	private static final String DOUBLEQUATE = "\"";

	private PrintWriter reportPW;

	private GuidelineHolder gh = GuidelineHolder.getInstance();

	private String[] metricsNames = gh.getLocalizedMetricsNames();

	private boolean[] enabledMetrics;

	private String[] guidelineNames = gh.getGuidelineNames();

	private boolean[] enabledGuidelines = new boolean[guidelineNames.length];

	private HashMap<IEvaluationItem, String> cacheMap = new HashMap<IEvaluationItem, String>();

	private int mode = CSV;

	public int getMode() {
		return mode;
	}

	private boolean isCSV = true;

	private String separator = COMMA;

	public ReportUtil() {
		enabledMetrics = gh.getEnabledMetrics();

		IGuidelineData tmpGD[] = gh.getGuidelineData();
		for (int i = 0; i < tmpGD.length; i++) {
			enabledGuidelines[i] = tmpGD[i].isEnabled();
		}
	}

	public void setMode(int mode) {
		switch (mode) {
		case CSV:
			isCSV = true;
			this.mode = mode;
			separator = COMMA;
			break;
		case TAB:
			isCSV = false;
			this.mode = mode;
			separator = TAB_STRING;
			break;
		}
	}

	public String getFirstLine() {
		StringBuffer tmpSB = new StringBuffer();

		tmpSB.append(prep(IProblemConst.TITLE_TYPE) + separator);
		for (int i = 0; i < metricsNames.length; i++) {
			if (enabledMetrics[i]) {
				tmpSB.append(prep(metricsNames[i]) + separator);
			}
		}
		for (int i = 0; i < guidelineNames.length; i++) {
			if (enabledGuidelines[i]) {
				tmpSB.append(prep(guidelineNames[i]) + separator);
			}
		}

		tmpSB.append(prep(IProblemConst.TITLE_GUIDELINE + "("
				+ IProblemConst.TITLE_HELP + ")")
				+ separator
				+ prep(IProblemConst.TITLE_TECHNIQUS)
				+ separator
				+ prep(IProblemConst.TITLE_TECHNIQUS + "("
						+ IProblemConst.TITLE_HELP + ")")
				+ separator
				+ prep(IProblemConst.TITLE_LINE)
				+ separator
				+ prep(IProblemConst.TITLE_DESCRIPTION));

		return (tmpSB.toString());
	}

	public void writeFirstLine() {
		if (reportPW != null) {
			reportPW.println(getFirstLine());
		}
	}

	public void setPrintWriter(PrintWriter reportPW) {
		this.reportPW = reportPW;
	}

	private String prep(String target) {
		if (isCSV) {
			return (DOUBLEQUATE
					+ target.replaceAll(DOUBLEQUATE, DOUBLEQUATE + DOUBLEQUATE) + DOUBLEQUATE);
		}else{
			return(target.replaceAll(TAB_STRING, "    "));
		}
	}

	public String toString(IProblemItem item) {
		if (item == null) {
			return "";
		}

		IEvaluationItem evalItem = item.getEvaluationItem();
		String csvStr = cacheMap.get(evalItem);
		if (csvStr == null) {
			StringBuffer tmpSB = new StringBuffer();
			tmpSB.append(prep(item.getSeverityStr()) + separator);
			int[] metricsValues = evalItem.getMetricsScores();
			for (int i = 0; i < metricsValues.length; i++) {
				if (enabledMetrics[i]) {
					tmpSB.append(prep(Integer.toString(-metricsValues[i]))
							+ separator);
				}
			}
			String[] guidelineValues = evalItem.getTableDataGuideline();
			for (int i = 0; i < guidelineValues.length; i++) {
				if (enabledGuidelines[i]) {
					tmpSB.append(prep(guidelineValues[i]) + separator);
				}
			}
			StringBuffer urlSB = new StringBuffer();
			StringBuffer techUrlSB = new StringBuffer();
			ITechniquesItem[][] techniques = evalItem.getTechniques();
			IGuidelineItem[] guidelines = evalItem.getGuidelines();
			TreeSet<ITechniquesItem> techSet = new TreeSet<ITechniquesItem>(
					new Comparator<ITechniquesItem>() {
						public int compare(ITechniquesItem o1,
								ITechniquesItem o2) {
							int flag = o1.getGuidelineName().compareTo(
									o2.getGuidelineName());
							if (flag == 0) {
								flag = o1.getId().compareTo(o2.getId());
							}
							return flag;
						}
					});

			for (int i = 0; i < guidelines.length; i++) {
				IGuidelineItem gItem = guidelines[i];
				if (gItem.isEnabled()) {
					urlSB.append(gItem.getUrl() + separator + " ");
					for (ITechniquesItem tech : techniques[i]) {
						techSet.add(tech);
					}
				}
			}
			for (ITechniquesItem i : techSet) {
				techUrlSB.append(i.getUrl() + separator + " ");
			}

			String tmpS = urlSB.toString();
			if (tmpS.length() > 2) {
				tmpS = tmpS.substring(0, tmpS.length() - 2);
			}
			tmpSB.append(prep(tmpS) + separator);

			tmpSB.append(prep(item.getEvaluationItem().getTableDataTechniques())
					+ separator);

			tmpS = techUrlSB.toString();
			if (tmpS.length() > 2) {
				tmpS = tmpS.substring(0, tmpS.length() - 2);
			}
			tmpSB.append(prep(tmpS) + separator);

			csvStr = tmpSB.toString();
			cacheMap.put(evalItem, csvStr);
		}
		return (csvStr + prep(item.getLineStrMulti()) + separator + prep(item
				.getDescription()));
	}

	public void visit(IProblemItem item) {
		if (item != null)
			reportPW.println(toString(item));
	}

}
