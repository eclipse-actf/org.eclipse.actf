/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Shin SAITO - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.ui.report.table;

import org.eclipse.actf.visualization.eval.guideline.GuidelineHolder;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 *
 */
public class MetricsLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	private GuidelineHolder guidelineHolder = GuidelineHolder.getInstance();

	private int numMetrics;

	/**
     * 
     */
	public MetricsLabelProvider() {
		super();
		numMetrics = guidelineHolder.getMetricsNames().length;
	}

	public Image getColumnImage(Object arg0, int arg1) {
		IProblemItem tmpItem = (IProblemItem) arg0;
		if (arg1 >= 0 && arg1 < numMetrics) {
			return (tmpItem.getEvaluationItem().getMetricsIcons()[arg1]);
		}
		return null;
	}

	public String getColumnText(Object arg0, int arg1) {
		IProblemItem tmpItem = (IProblemItem) arg0;
		if (arg1 >= 0 && arg1 < numMetrics) {
			return (tmpItem.getEvaluationItem().getTableDataMetrics()[arg1]);
		}
		return ""; //$NON-NLS-1$
	}
}
