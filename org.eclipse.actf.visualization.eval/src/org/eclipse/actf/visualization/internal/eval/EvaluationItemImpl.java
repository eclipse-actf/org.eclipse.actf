/*******************************************************************************
 * Copyright (c) 2005, 2013 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.eval;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.actf.util.FileUtils;
import org.eclipse.actf.visualization.eval.IEvaluationItem;
import org.eclipse.actf.visualization.eval.IGuidelineItem;
import org.eclipse.actf.visualization.eval.ITechniquesItem;
import org.eclipse.actf.visualization.eval.guideline.GuidelineSelectionChangedEvent;
import org.eclipse.actf.visualization.eval.problem.IProblemConst;
import org.eclipse.actf.visualization.internal.eval.guideline.GuidelineItemDescription;
import org.eclipse.actf.visualization.internal.eval.guideline.MetricsItem;
import org.eclipse.swt.graphics.Image;

import com.ibm.icu.text.MessageFormat;

@SuppressWarnings("nls")
public class EvaluationItemImpl implements IEvaluationItem {

	private static final String LISTENABILITY = "listenability";
	private static final String NAVIGABILITY = "navigability";
	private static final String COMPLIANCE = "compliance";
	private static final String PERCEIVABLE = "perceivable";
	private static final String OPERABLE = "operable";
	private static final String UNDERSTANDABLE = "understandable";
	private static final String ROBUST = "robust";

	private static final Image ERROR_C_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/ErrC.png").createImage();
	private static final Image ERROR_N_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/ErrN.png").createImage();
	private static final Image ERROR_L_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/ErrL.png").createImage();
	private static final Image ERROR_O_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/ErrO.png").createImage();
	private static final Image ERROR_P_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/ErrP.png").createImage();
	private static final Image ERROR_R_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/ErrR.png").createImage();
	private static final Image ERROR_U_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/ErrU.png").createImage();
	private static final Image ERROR_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/Err.png").createImage();

	private static final Image WARN_C_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/WarnC.png").createImage();
	private static final Image WARN_N_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/WarnN.png").createImage();
	private static final Image WARN_L_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/WarnL.png").createImage();
	private static final Image WARN_O_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/WarnO.png").createImage();
	private static final Image WARN_P_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/WarnP.png").createImage();
	private static final Image WARN_R_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/WarnR.png").createImage();
	private static final Image WARN_U_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/WarnU.png").createImage();
	private static final Image WARN_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/Warn.png").createImage();

	private static final Image USER_C_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/ConfC.png").createImage();
	private static final Image USER_N_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/ConfN.png").createImage();
	private static final Image USER_L_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/ConfL.png").createImage();
	private static final Image USER_O_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/ConfO.png").createImage();
	private static final Image USER_P_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/ConfP.png").createImage();
	private static final Image USER_R_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/ConfR.png").createImage();
	private static final Image USER_U_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/ConfU.png").createImage();
	private static final Image USER_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/Conf.png").createImage();

	private static final Image INFO_C_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/InfoC.png").createImage();
	private static final Image INFO_N_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/InfoN.png").createImage();
	private static final Image INFO_L_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/InfoL.png").createImage();
	private static final Image INFO_O_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/InfoO.png").createImage();
	private static final Image INFO_P_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/InfoP.png").createImage();
	private static final Image INFO_R_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/InfoR.png").createImage();
	private static final Image INFO_U_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/InfoU.png").createImage();
	private static final Image INFO_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/Info.png").createImage();

	private String id = "";

	private IGuidelineItem[] guidelines = new GuidelineItemImpl[0];

	private ITechniquesItem[][] techniques = new ITechniquesItem[0][];

	private MetricsItem[] metrics = new MetricsItem[0];

	private String description;

	private String[] tableDataMetrics = new String[0];

	private String[] tableDataGuideline = new String[0];

	private String tableDataTechniques = "";

	private Image[] metricsIcons = new Image[0];

	private int[] metricsScores = new int[0];

	private short severity = SEV_INFO;

	private String severityStr = SEV_INFO_STR;

	private Pattern pattern = Pattern.compile("\\d+");

	/**
	 * @param id
	 */
	public EvaluationItemImpl(String id, String severity) {
		this.id = id;
		setSeverity(severity);
		description = GuidelineItemDescription.getDescription(id);
	}

	public String createDescription(String targetString) {
		return MessageFormat.format(description, new Object[] { targetString });
	}

	public String createDescription() {
		return (description);
	}

	public IGuidelineItem[] getGuidelines() {
		return guidelines;
	}

	public void setGuidelines(IGuidelineItem[] guidelines) {
		this.guidelines = guidelines;
	}

	private TreeSet<String> getTechniquesSortTree(){
		return new TreeSet<String>(new Comparator<String>() {
			public int compare(String arg1, String arg2) {
				int result;
				String str1, str2;
				int num1, num2;

				Matcher matcher1 = pattern.matcher(arg1);
				Matcher matcher2 = pattern.matcher(arg2);

				if (matcher1.find()) {
					str1 = arg1.substring(0, matcher1.start());
					num1 = Integer.parseInt(matcher1.group());
				} else {
					str1 = arg1;
					num1 = Integer.MIN_VALUE;
				}

				if (matcher2.find()) {
					str2 = arg2.substring(0, matcher2.start());
					num2 = Integer.parseInt(matcher2.group());
				} else {
					str2 = arg2;
					num2 = Integer.MIN_VALUE;
				}

				result = str1.compareTo(str2);
				if (result != 0)
					return result;
				//TODO Java 1.7 or later
				//result = Integer.compare(num1, num2);
				result = Integer.valueOf(num1).compareTo(num2);
				if (result != 0)
					return result;
				return arg1.compareTo(arg2);
			}
		});
	}
	
	public void setTechniques(ITechniquesItem[][] techniques) {
		this.techniques = techniques;

		// init
		TreeSet<String> tmpTree = getTechniquesSortTree();

		for (int i = 0; i < techniques.length; i++) {
			ITechniquesItem[] ti = techniques[i];
			for (ITechniquesItem tech : ti) {
				tmpTree.add(tech.getId());
			}
		}
		StringBuffer tmpSB = new StringBuffer();
		Iterator<String> tmpI = tmpTree.iterator();
		if (tmpI.hasNext()) {
			tmpSB.append(tmpI.next());
		}
		while (tmpI.hasNext()) {
			tmpSB.append(", " + tmpI.next());
		}
		tableDataTechniques = tmpSB.toString();
	}

	public String getId() {
		return id;
	}

	public int getSeverity() {
		return severity;
	}

	public String getSeverityStr() {
		return severityStr;
	}

	private void setSeverity(String _severityStr) {
		severity = SEV_INFO;
		severityStr = IProblemConst.INFO;
		if (_severityStr != null) {
			_severityStr = _severityStr.trim();
			if (SEV_ERROR_STR.equalsIgnoreCase(_severityStr)) {
				severity = SEV_ERROR;
				severityStr = IProblemConst.ESSENTIAL;
			} else if (SEV_WARNING_STR.equalsIgnoreCase(_severityStr)) {
				severity = SEV_WARNING;
				severityStr = IProblemConst.WARNING;
			} else if (SEV_USER_STR.equalsIgnoreCase(_severityStr)) {
				severity = SEV_USER;
				severityStr = IProblemConst.USER_CHECK;
			}
			// else{
			// severity = SEV_INFO;
			// }
		}
	}

	public MetricsItem[] getMetricsItems() {
		return metrics;
	}

	public void setMetrics(MetricsItem[] metrics) {
		this.metrics = metrics;
	}

	public void initMetrics(String[] metricsNames) {
		tableDataMetrics = new String[metricsNames.length];
		metricsScores = new int[metricsNames.length];
		metricsIcons = new Image[metricsNames.length];

		for (int i = 0; i < metricsNames.length; i++) {
			String curName = metricsNames[i];
			tableDataMetrics[i] = "";
			metricsScores[i] = 0;
			metricsIcons[i] = null;
			for (int j = 0; j < metrics.length; j++) {
				MetricsItem tmpItem = metrics[j];
				if (curName.equalsIgnoreCase(tmpItem.getMetricsName())) {
					metricsScores[i] = tmpItem.getScore();
					if (tmpItem.getScore() != 0) {
						tableDataMetrics[i] = Integer.toString(-tmpItem
								.getScore()) + " ";
					}

					switch (this.severity) {
					case SEV_ERROR:
						if (curName.equalsIgnoreCase(PERCEIVABLE)) {
							metricsIcons[i] = ERROR_P_IMAGE;
						} else if (curName.equalsIgnoreCase(OPERABLE)) {
							metricsIcons[i] = ERROR_O_IMAGE;
						} else if (curName.equalsIgnoreCase(UNDERSTANDABLE)) {
							metricsIcons[i] = ERROR_U_IMAGE;
						} else if (curName.equalsIgnoreCase(ROBUST)) {
							metricsIcons[i] = ERROR_R_IMAGE;
						} else if (curName.equalsIgnoreCase(COMPLIANCE)) {
							metricsIcons[i] = ERROR_C_IMAGE;
						} else if (curName.equalsIgnoreCase(NAVIGABILITY)) {
							metricsIcons[i] = ERROR_N_IMAGE;
						} else if (curName.equalsIgnoreCase(LISTENABILITY)) {
							metricsIcons[i] = ERROR_L_IMAGE;
						} else {
							metricsIcons[i] = ERROR_IMAGE;
						}
						tableDataMetrics[i] = tableDataMetrics[i] + "("
								+ IProblemConst.ESSENTIAL + ")";
						break;
					case SEV_WARNING:
						if (curName.equalsIgnoreCase(PERCEIVABLE)) {
							metricsIcons[i] = WARN_P_IMAGE;
						} else if (curName.equalsIgnoreCase(OPERABLE)) {
							metricsIcons[i] = WARN_O_IMAGE;
						} else if (curName.equalsIgnoreCase(UNDERSTANDABLE)) {
							metricsIcons[i] = WARN_U_IMAGE;
						} else if (curName.equalsIgnoreCase(ROBUST)) {
							metricsIcons[i] = WARN_R_IMAGE;
						} else if (curName.equalsIgnoreCase(COMPLIANCE)) {
							metricsIcons[i] = WARN_C_IMAGE;
						} else if (curName.equalsIgnoreCase(NAVIGABILITY)) {
							metricsIcons[i] = WARN_N_IMAGE;
						} else if (curName.equalsIgnoreCase(LISTENABILITY)) {
							metricsIcons[i] = WARN_L_IMAGE;
						} else {
							metricsIcons[i] = WARN_IMAGE;
						}
						tableDataMetrics[i] = tableDataMetrics[i] + "("
								+ IProblemConst.WARNING + ")";
						break;
					case SEV_USER:
						if (curName.equalsIgnoreCase(PERCEIVABLE)) {
							metricsIcons[i] = USER_P_IMAGE;
						} else if (curName.equalsIgnoreCase(OPERABLE)) {
							metricsIcons[i] = USER_O_IMAGE;
						} else if (curName.equalsIgnoreCase(UNDERSTANDABLE)) {
							metricsIcons[i] = USER_U_IMAGE;
						} else if (curName.equalsIgnoreCase(ROBUST)) {
							metricsIcons[i] = USER_R_IMAGE;
						} else if (curName.equalsIgnoreCase(COMPLIANCE)) {
							metricsIcons[i] = USER_C_IMAGE;
						} else if (curName.equalsIgnoreCase(NAVIGABILITY)) {
							metricsIcons[i] = USER_N_IMAGE;
						} else if (curName.equalsIgnoreCase(LISTENABILITY)) {
							metricsIcons[i] = USER_L_IMAGE;
						} else {
							metricsIcons[i] = USER_IMAGE;
						}
						tableDataMetrics[i] = tableDataMetrics[i] + "("
								+ IProblemConst.USER_CHECK + ")";
						break;
					case SEV_INFO:
						if (curName.equalsIgnoreCase(PERCEIVABLE)) {
							metricsIcons[i] = INFO_P_IMAGE;
						} else if (curName.equalsIgnoreCase(OPERABLE)) {
							metricsIcons[i] = INFO_O_IMAGE;
						} else if (curName.equalsIgnoreCase(UNDERSTANDABLE)) {
							metricsIcons[i] = INFO_U_IMAGE;
						} else if (curName.equalsIgnoreCase(ROBUST)) {
							metricsIcons[i] = INFO_R_IMAGE;
						} else if (curName.equalsIgnoreCase(COMPLIANCE)) {
							metricsIcons[i] = INFO_C_IMAGE;
						} else if (curName.equalsIgnoreCase(NAVIGABILITY)) {

							metricsIcons[i] = INFO_N_IMAGE;
						} else if (curName.equalsIgnoreCase(LISTENABILITY)) {
							metricsIcons[i] = INFO_L_IMAGE;
						} else {
							metricsIcons[i] = INFO_IMAGE;
						}
						tableDataMetrics[i] = tableDataMetrics[i] + "("
								+ IProblemConst.INFO + ")";
						break;
					}

				}
			}
		}
	}

	public String toString() {
		StringBuffer tmpSB = new StringBuffer();
		tmpSB.append(id + " " + severity + " : " + FileUtils.LINE_SEP);
		for (int i = 0; i < guidelines.length; i++) {
			tmpSB.append("  " + guidelines[i] + FileUtils.LINE_SEP);
		}

		for (int i = 0; i < tableDataGuideline.length; i++) {
			tmpSB.append("  " + tableDataGuideline[i] + FileUtils.LINE_SEP);
		}

		for (int i = 0; i < metrics.length; i++) {
			tmpSB.append("  " + metrics[i] + FileUtils.LINE_SEP);
		}

		for (int i = 0; i < tableDataMetrics.length; i++) {
			tmpSB.append("  " + tableDataMetrics[i] + FileUtils.LINE_SEP);
		}

		return (tmpSB.toString());
	}

	private void updateTableDataGuidelines(String[] guidelineNames) {
		tableDataGuideline = new String[guidelineNames.length];
		for (int i = 0; i < guidelineNames.length; i++) {
			StringBuffer tmpSB = new StringBuffer();
			boolean notFirst = false;
			for (int j = 0; j < guidelines.length; j++) {
				IGuidelineItem tmpItem = guidelines[j];
				if (tmpItem.isEnabled()) {
					if (guidelineNames[i].equalsIgnoreCase(tmpItem
							.getGuidelineName())) {
						if (notFirst) {
							tmpSB.append(", ");
						} else {
							notFirst = true;
						}
						if (tmpItem.getLevel().length() > 0) {
							tmpSB.append(tmpItem.getLevel() + ": "
									+ tmpItem.getId());
						} else {
							tmpSB.append(tmpItem.getId());
						}
					}
				}
			}
			tableDataGuideline[i] = tmpSB.toString();
		}
	}

	private void updateTableDataTechniques() {
		TreeSet<String> tmpTree = getTechniquesSortTree();
		for (int i = 0; i < guidelines.length; i++) {
			if (guidelines[i].isEnabled()) {
				ITechniquesItem[] ti = techniques[i];
				for (ITechniquesItem tech : ti) {
					tmpTree.add(tech.getId());
				}
			}
		}
		StringBuffer tmpSB = new StringBuffer();
		Iterator<String> tmpI = tmpTree.iterator();
		if (tmpI.hasNext()) {
			tmpSB.append(tmpI.next());
		}
		while (tmpI.hasNext()) {
			tmpSB.append(", " + tmpI.next());
		}
		tableDataTechniques = tmpSB.toString();
	}

	public int[] getMetricsScores() {
		return metricsScores;
	}

	public String[] getTableDataGuideline() {
		return tableDataGuideline;
	}

	public String[] getTableDataMetrics() {
		return tableDataMetrics;
	}

	public Image[] getMetricsIcons() {
		return metricsIcons;
	}

	public ITechniquesItem[][] getTechniques() {
		return techniques;
	}

	public String getTableDataTechniques() {
		return tableDataTechniques;
	}

	public void selectionChanged(GuidelineSelectionChangedEvent e) {
		updateTableDataTechniques();
		updateTableDataGuidelines(e.getGuidelineHolder().getGuidelineNames());
	}
}
