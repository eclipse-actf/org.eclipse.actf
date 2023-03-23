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
import org.eclipse.actf.visualization.eval.problem.IProblemItemImage;
import org.eclipse.jface.viewers.Viewer;

/**
 * Viewer sorter implementation for image related accessibility issues
 * 
 * @see IResultTableSorter
 * @see IProblemItemImage
 */
public class ResultTableSorterLV extends ResultTableSorterBase {

	private GuidelineHolder guidelineHolder = GuidelineHolder.getInstance();

	private boolean inverse = false;

	private int curColumn = 0;

	private int guidelineFinPos;

	/**
	 * Constructor of the class
	 */
	public ResultTableSorterLV() {
		super();
		guidelineFinPos = 1 + guidelineHolder.getGuidelineData().length;
	}

	private int compareInt(int type1, int type2) {
		return (type1 - type2);
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
				IProblemItemImage tmp1 = (IProblemItemImage) arg1;
				IProblemItemImage tmp2 = (IProblemItemImage) arg2;

				if (curColumn == 0) {
					result = compareInt(tmp1.getIconId(), tmp2.getIconId());
				} else if (curColumn < guidelineFinPos) {
					// TODO sync with label

					result = compareGuideline(tmp1, tmp2, curColumn - 1);

				} else {
					switch (curColumn - guidelineFinPos) {
					case 0:
						result = compareInt(tmp1.getSeverityLV(),
								tmp2.getSeverityLV());
						break;
					case 1:
						result = compareString(tmp1.getForeground(),
								tmp2.getForeground());
						break;
					case 2:
						result = compareString(tmp1.getBackground(),
								tmp2.getBackground());
						break;
					case 3:
						result = compareInt(tmp1.getX(), tmp2.getX());
						break;
					case 4:
						result = compareInt(tmp1.getY(), tmp2.getY());
						break;
					case 5:
						result = compareInt(tmp1.getArea(), tmp2.getArea());
						break;
					case 6:
						result = compareEvalItem(tmp1.getEvaluationItem(),
								tmp2.getEvaluationItem());
						break;
					case 7:
						result = compareString(tmp1.getDescription(),
								tmp2.getDescription());
						break;
					}
				}

				if (result == 0) {
					result = tmp1.getSerialNumber() - tmp2.getSerialNumber();
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
