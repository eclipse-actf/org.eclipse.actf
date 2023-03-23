/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.ui.report.table;

import org.eclipse.actf.visualization.eval.EvaluationUtil;
import org.eclipse.actf.visualization.eval.IEvaluationItem;
import org.eclipse.actf.visualization.eval.guideline.GuidelineHolder;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.internal.ui.report.ReportPlugin;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * A label provider implementation for general accessibility issues
 * 
 * @see IProblemItem
 */
public class ResultTableLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	private GuidelineHolder guidelineHolder = GuidelineHolder.getInstance();

	private int metricsFinPos = guidelineHolder.getMetricsNames().length + 1;
	private int guidelineFinPos = metricsFinPos
			+ guidelineHolder.getGuidelineData().length;

	private static Image HIGHLIGHT_IMAGE = ReportPlugin
			.imageDescriptorFromPlugin(EvaluationUtil.PLUGIN_ID,
					"icons/star.gif") //$NON-NLS-1$
			.createImage();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang
	 * .Object, int)
	 */
	public Image getColumnImage(Object arg0, int arg1) {
		IProblemItem tmpItem = (IProblemItem) arg0;
		if (arg1 == 0) {
			if (tmpItem.isCanHighlight()) {
				return (HIGHLIGHT_IMAGE);
			}
		} else if (arg1 < metricsFinPos) {
			return (tmpItem.getEvaluationItem().getMetricsIcons()[arg1 - 1]);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang
	 * .Object, int)
	 */
	public String getColumnText(Object arg0, int arg1) {
		IProblemItem tmpItem = (IProblemItem) arg0;
		IEvaluationItem ei = tmpItem.getEvaluationItem();

		if (arg1 == 0) {

		} else if (arg1 < metricsFinPos) {
			return (ei.getTableDataMetrics()[arg1 - 1]);
		} else if (arg1 < guidelineFinPos) {
			return (ei.getTableDataGuideline()[arg1 - metricsFinPos]);
		} else {
			switch (arg1 - guidelineFinPos) {
			case 0:
				return ei.getTableDataTechniques(); // techniques
			case 1:
				return (tmpItem.getLineStrMulti());
			case 2:
				return (tmpItem.getDescription());
			default:
			}
		}

		return ""; //$NON-NLS-1$
	}
}
