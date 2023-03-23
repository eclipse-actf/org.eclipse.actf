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

import org.eclipse.actf.visualization.eval.guideline.GuidelineHolder;
import org.eclipse.actf.visualization.eval.problem.IProblemItemImage;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * A label provide implementation for image related accessibility issues
 * 
 * @see IProblemItemImage
 */
public class ResultTableLabelProviderLV extends LabelProvider implements
		ITableLabelProvider {

	private GuidelineHolder guidelineHolder = GuidelineHolder.getInstance();

	private int guidelineFinPos;

	/**
	 * Constructor of the class
	 */
	public ResultTableLabelProviderLV() {
		super();
		guidelineFinPos = 1 + guidelineHolder.getGuidelineData().length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang
	 * .Object, int)
	 */
	public Image getColumnImage(Object arg0, int arg1) {
		IProblemItemImage tmpItem = (IProblemItemImage) arg0;
		if (arg1 == 0) {
			return (tmpItem.getIconImage());
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
		IProblemItemImage tmpItem = (IProblemItemImage) arg0;

		if (arg1 == 0) {
			return (tmpItem.getIconTooltip());
		} else if (arg1 < guidelineFinPos) {
			try {
				return (tmpItem.getEvaluationItem().getTableDataGuideline()[arg1 - 1]);
			} catch (Exception e) {
				return ("");
			}
		} else {
			switch (arg1 - guidelineFinPos) {
			case 0:
				if(tmpItem.getSeverityLV()>0){
					return (Integer.toString(tmpItem.getSeverityLV()));
				}
				return("-");
			case 1:
				return (tmpItem.getForeground());
			case 2:
				return (tmpItem.getBackground());
			case 3:
				return (Integer.toString(tmpItem.getX()));
			case 4:
				return (Integer.toString(tmpItem.getY()));
			case 5:
				return (Integer.toString(tmpItem.getArea()));
			case 6:
				return (tmpItem.getEvaluationItem().getTableDataTechniques());// techniques
			case 7:
				return (tmpItem.getDescription());
			default:
				return (""); //$NON-NLS-1$

			}
		}
	}

}
