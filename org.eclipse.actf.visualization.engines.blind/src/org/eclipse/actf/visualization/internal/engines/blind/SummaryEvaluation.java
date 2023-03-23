/*******************************************************************************
 * Copyright (c) 2005, 2016 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.engines.blind;

import org.eclipse.actf.util.FileUtils;
import org.eclipse.actf.visualization.engines.blind.eval.PageEvaluation;
import org.eclipse.actf.visualization.eval.guideline.GuidelineHolder;
import org.eclipse.actf.visualization.eval.html.statistics.PageData;

import com.ibm.icu.text.MessageFormat;

/**
 * 
 */
public class SummaryEvaluation {

	private static final String SPACE = " "; //$NON-NLS-1$

	private PageEvaluation pe;

	private PageData pageData;

	private int noImageAltCount;

	private int wrongImageAltCount;

	private int redundantImageAltCount = 0;

	private GuidelineHolder guidelineHolder = GuidelineHolder.getInstance();

	private boolean hasError;

	public SummaryEvaluation(PageEvaluation pe, PageData pd, boolean hasError) {
		this.pe = pe;
		this.pageData = pd;
		this.hasError = hasError;
	}

	/**
	 * @return
	 */
	public String getOverview() {
		StringBuffer tmpSB = new StringBuffer(512);
		StringBuffer noGoodMetrics = new StringBuffer();

		boolean hasComp = false;
		boolean hasOperable = false;
		boolean hasOther = false;

		String[] metrics = pe.getMetrics();
		String[] lMetrics = pe.getLocalizedMetrics();
		int[] scores = pe.getScores();

		int compTotal = 0;
		int compCount = 0;
		int operable = 100;
		int other = 100;

		// boolean[] enabledMetrics = guidelineHolder.getMatchedMetrics();

		for (int i = 0; i < metrics.length; i++) {
			int score = scores[i];
			if ((metrics[i].equalsIgnoreCase("perceivable") //$NON-NLS-1$
					|| metrics[i].equalsIgnoreCase("understandable") || //$NON-NLS-1$
					metrics[i].equalsIgnoreCase("robust"))//$NON-NLS-1$
					&& guidelineHolder.isMatchedMetric(metrics[i])) {
				compTotal += score;
				compCount++;
				hasComp = true;
				if (score != 100) {
					noGoodMetrics.append(lMetrics[i] + ","); //$NON-NLS-1$
				}
			} else if (metrics[i].equalsIgnoreCase("operable") //$NON-NLS-1$
					&& guidelineHolder.isMatchedMetric(metrics[i])) {
				operable = score;
				compTotal += score;
				compCount++;
				hasOperable = true;
			} else {
				hasOther = true;
				if (other > score) {
					other = score;
				}
				if (score != 100) {
					noGoodMetrics.append(lMetrics[i] + ","); //$NON-NLS-1$
				}
			}
		}

		noImageAltCount = pageData.getMissingAltNum();
		wrongImageAltCount = pageData.getWrongAltNum();
		// alertImageAltCount = pageData.get;
		// redundantImageAltCount = pageData.get;//TODO
		int totalAltError = noImageAltCount + wrongImageAltCount;// +
		// redundantImageAltCount;
		// +alertImageAltCount

		StringBuffer aboutComp = new StringBuffer();
		StringBuffer aboutNav = new StringBuffer();

		boolean isGood = false;

		if (compCount == 0) {
			return ("");
		}
		if (compTotal / compCount >= 75) {
			if (hasError) { // hasComplianceError
				aboutComp.append(Messages.Eval_compliant_with_some_other_errors + FileUtils.LINE_SEP);

				if (totalAltError > 0) {
					aboutComp.append(Messages.Eval_confirm_alt_attributes_first + FileUtils.LINE_SEP);
					aboutComp.append(getImageAltStatistics());
				} else {
					aboutComp.append(Messages.Eval_confirm_errors_detailed_report);
				}
			} else {
				if (hasOther && other != 100) {
					aboutComp.append(Messages.Eval_some_errors_on_metrics + FileUtils.LINE_SEP + MessageFormat.format(
							Messages.Eval_some_errors_on_metrics1,
							(Object[]) (new String[] { noGoodMetrics.substring(0, noGoodMetrics.length() - 1) })));
				} else {
					if (compTotal / compCount == 100) {
						isGood = true;
						aboutComp.append(
								Messages.Eval_completely_compliant + FileUtils.LINE_SEP + Messages.Eval_user_check2);
					} else {
						isGood = true;
						aboutComp.append(Messages.Eval_completely_compliant_with_user_check_items + FileUtils.LINE_SEP
								+ Messages.Eval_user_check1);
					}
				}
			}
		} else {
			if (compTotal / compCount >= 50) {
				aboutComp.append(Messages.Eval_some_accessibility_issues + FileUtils.LINE_SEP);
			} else {
				aboutComp.append(Messages.Eval_many_accessibility_issues + FileUtils.LINE_SEP);
			}

			if (totalAltError > 0) {
				aboutComp.append(Messages.Eval_confirm_alt_attributes_first + FileUtils.LINE_SEP);
				aboutComp.append(getImageAltStatistics());
			} else {
				aboutComp.append(Messages.Eval_confirm_errors_detailed_report);
			}
		}

		//
		if (hasOperable && guidelineHolder.isEnabledMetric("operable")) { //$NON-NLS-1$
			if (operable >= 75) {
				if (pageData.getMaxTime() > 240) {
					aboutNav.append(MessageFormat
							.format(Messages.Eval_navigability_long_time_error_msg, (Object[]) (new String[] {
									FileUtils.LINE_SEP, FileUtils.LINE_SEP, FileUtils.LINE_SEP, FileUtils.LINE_SEP }))
							+ FileUtils.LINE_SEP);

				} else {
					aboutNav.append(MessageFormat.format(Messages.Eval_navigability_good_msg,
							(Object[]) (new String[] { FileUtils.LINE_SEP, FileUtils.LINE_SEP })) + FileUtils.LINE_SEP);
				}
			} else {
				isGood = false;
				aboutNav.append(MessageFormat
						.format(Messages.Eval_navigability_low_score_error_msg, (Object[]) (new String[] {
								FileUtils.LINE_SEP, FileUtils.LINE_SEP, FileUtils.LINE_SEP, FileUtils.LINE_SEP }))
						+ FileUtils.LINE_SEP);
			}
		}

		if ((hasComp || hasOperable) && isGood) {
			tmpSB.append(Messages.Eval_excellent + FileUtils.LINE_SEP + FileUtils.LINE_SEP);
		}
		tmpSB.append(aboutComp + FileUtils.LINE_SEP + FileUtils.LINE_SEP);
		tmpSB.append(aboutNav);

		return (tmpSB.toString());
	}

	private String getImageAltStatistics() {
		StringBuffer tmpSB = new StringBuffer(FileUtils.LINE_SEP);

		if (noImageAltCount > 0) {
			tmpSB.append(" -" + Messages.Eval_no_img_alt_error_msg //$NON-NLS-1$
					+ FileUtils.LINE_SEP);
		}
		if (wrongImageAltCount > 0) {
			tmpSB.append(" -" + Messages.Eval_wrong_img_alt_error_msg //$NON-NLS-1$
					+ FileUtils.LINE_SEP);
		}
		if (redundantImageAltCount > 0) {
			tmpSB.append(" -" + Messages.Eval_redundant_img_alt_error_msg //$NON-NLS-1$
					+ FileUtils.LINE_SEP);
		}
		tmpSB.append(FileUtils.LINE_SEP);

		if (noImageAltCount > 0) {
			tmpSB.append(SPACE + Messages.Eval_no_img_alt + SPACE + noImageAltCount + FileUtils.LINE_SEP);
		}
		if (wrongImageAltCount > 0) {
			tmpSB.append(SPACE + Messages.Eval_wrong_img_alt + SPACE + wrongImageAltCount + FileUtils.LINE_SEP);
		}
		if (redundantImageAltCount > 0) {
			tmpSB.append(SPACE + Messages.Eval_redundant_img_alt + SPACE + redundantImageAltCount + FileUtils.LINE_SEP);
		}

		return (tmpSB.toString());
	}
}
