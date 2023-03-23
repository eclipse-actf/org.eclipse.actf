/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.ui;

import org.eclipse.actf.mediator.IACTFReportGenerator;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * Interface to implement visualization views.
 * 
 * @see IACTFReportGenerator
 */
@SuppressWarnings("nls")
public interface IVisualizationView extends IACTFReportGenerator {
	/**
	 * View ID of blind visualization view
	 */
	public static final String ID_BLINDVIEW = "org.eclipse.actf.visualization.blind.ui.views.BlindView";
	/**
	 * View ID of low vision visualization view
	 */
	public static final String ID_LOWVISIONVIEW = "org.eclipse.actf.visualization.lowvision.ui.views.LowVisionView";
	/**
	 * View ID of presentation visualization view
	 */
	public static final String ID_PRESENTATIONVIEW = "org.eclipse.actf.visualization.presentation.ui.views.RoomView";
	/**
	 * View ID of detailed report view
	 */
	public static final String DETAILED_REPROT_VIEW_ID = "org.eclipse.actf.visualization.ui.report.views.DetailedReportView";
	/**
	 * View ID of summary report view
	 */
	public static final String SUMMARY_REPORT_VIEW_ID = "org.eclipse.actf.visualization.ui.report.views.SummaryReportView";
	/**
	 * ID of default mode
	 */
	public static final int MODE_DEFAULT = 0;
	/**
	 * ID of low vision mode
	 */
	public static final int MODE_LOWVISION = 1;

	/**
	 * Get label provider to show evaluation result
	 * 
	 * @return {@link IBaseLabelProvider}
	 */
	IBaseLabelProvider getLabelProvider();

	/**
	 * Get sorter to show evaluation result
	 * 
	 * @return {@link ViewerSorter}
	 */
	ViewerSorter getTableSorter();

	/**
	 * Set status message to status line.
	 * {@link VisualizationStatusLineContributionItem} can be used for
	 * implementation.
	 * 
	 * @param statusMessage
	 *            status message
	 */
	public void setStatusMessage(String statusMessage);

	/**
	 * Set information message to status line. *
	 * {@link VisualizationStatusLineContributionItem} can be used for
	 * implementation.
	 * 
	 * @param infoMessage
	 *            information message
	 */
	public void setInfoMessage(String infoMessage);

	/**
	 * Set visualization mode
	 * 
	 * @param mode
	 *            visualization mode
	 */
	public void setVisualizeMode(int mode);

	/**
	 * Get visualization mode to show evaluation result
	 * 
	 * @return visualization mode
	 */
	int getResultTableMode();

	/**
	 * Invoke visualization
	 */
	void doVisualize();

}
