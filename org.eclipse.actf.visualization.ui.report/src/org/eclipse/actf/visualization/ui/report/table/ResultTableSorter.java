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
package org.eclipse.actf.visualization.ui.report.table;

import org.eclipse.actf.visualization.eval.guideline.GuidelineHolder;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.jface.viewers.Viewer;

/**
 * Viewer sorter implementation for general accessibility issues
 * 
 * @see IResultTableSorter
 * @see IProblemItem
 */
public class ResultTableSorter extends ResultTableSorterBase {

	private GuidelineHolder guidelineHolder = GuidelineHolder.getInstance();

	private boolean inverse = false;

	private int curColumn = -1;

	private int metricsFinPos;

	private int guidelineFinPos;

	/**
	 * Constructor of the class
	 */
	public ResultTableSorter() {
		super();
		metricsFinPos = guidelineHolder.getMetricsNames().length + 1;
		guidelineFinPos = metricsFinPos
				+ guidelineHolder.getGuidelineData().length;
	}

	// for only aDesigner client
	protected int compareTotalScore(IProblemItem item1, IProblemItem item2) {
		int[] score1 = item1.getMetricsScores();
		int[] score2 = item2.getMetricsScores();

		int result = 0;
		boolean[] isEnabled = guidelineHolder.getMatchedMetrics();
		for (int i = 0; i < score1.length; i++) {
			if (isEnabled[i]) {
				result += score2[i] - score1[i];
			}
		}
		return (result);
	}

	// for only aDesigner client
	protected int compareHighlight(IProblemItem tmp1, IProblemItem tmp2) {
		boolean b1 = tmp1.isCanHighlight();
		boolean b2 = tmp2.isCanHighlight();
		if (b1 == b2) {
			return (compareTotalScore(tmp1, tmp2));
		} else if (b1) {
			return (-1);
		} else {
			return (1);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.viewers
	 * .Viewer, java.lang.Object, java.lang.Object)
	 */
	public int compare(Viewer arg0, Object arg1, Object arg2) {
		int result = 0;
		if (arg1 != null && arg2 != null) {
			try {
				IProblemItem tmp1 = (IProblemItem) arg1;
				IProblemItem tmp2 = (IProblemItem) arg2;

				// ICheckItem item1 = tmp1.getCheckItem();
				// ICheckItem item2 = tmp2.getCheckItem();

				if (curColumn == -1) {
					result = compareTotalScore(tmp1, tmp2);
				} else if (curColumn == 0) {
					result = compareHighlight(tmp1, tmp2);
				} else if (curColumn < metricsFinPos) {
					result = compareScore(tmp1, tmp2, curColumn - 1);
				} else if (curColumn < guidelineFinPos) {
					result = compareGuideline(tmp1, tmp2, curColumn
							- metricsFinPos);
				} else if (curColumn == guidelineFinPos) {
					result = compareEvalItem(tmp1.getEvaluationItem(),
							tmp2.getEvaluationItem());
				} else if (curColumn == guidelineFinPos + 1) {
					result = compareLine(tmp1, tmp2);
				} else {
					result = compareString(tmp1.getDescription(),
							tmp2.getDescription());
				}

				if (result == 0) {
					result = compareSeverity(tmp1, tmp2);
					if (result == 0) {
						result = tmp1.getSerialNumber()
								- tmp2.getSerialNumber();
					}
				}

				if (inverse) {
					return (-result);
				} else {
					return (result);
				}
			} catch (Exception e) {
			}
		}
		return super.compare(arg0, arg1, arg2);
	}

	public void setCurColumn(int curColumn) {
		if (this.curColumn == curColumn) {
			inverse = !inverse;
		} else {
			inverse = false;
			this.curColumn = curColumn;
		}
	}

	public void reset() {
		curColumn = -1;
		inverse = false;
	}

}
