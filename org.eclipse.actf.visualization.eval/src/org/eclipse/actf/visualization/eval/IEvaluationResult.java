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

package org.eclipse.actf.visualization.eval;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.eclipse.actf.mediator.IACTFReport;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.eval.problem.IProblemItemVisitor;
import org.eclipse.swt.custom.LineStyleListener;

/**
 * Interface for evaluation result information
 * 
 * @see IACTFReport
 */
public interface IEvaluationResult extends IACTFReport {

	/**
	 * Set detected problem item list
	 * 
	 * @param problemList
	 *            target problem item list
	 */
	public void setProblemList(List<IProblemItem> problemList);

	/**
	 * Add problem items into current list
	 * 
	 * @param c
	 *            target problem item collection to add
	 */
	public void addProblemItems(Collection<IProblemItem> c);

	/**
	 * Add problem items into current list
	 * 
	 * @param items
	 *            target problem item array to add
	 */
	public void addProblemItems(IProblemItem[] items);

	/**
	 * Get current problem item list
	 * 
	 * @return problem item list
	 */
	public List<IProblemItem> getProblemList();

	/**
	 * Set summary text of evaluation
	 * 
	 * @param summaryReportText
	 *            summary of evaluation
	 */
	public void setSummaryReportText(String summaryReportText);

	/**
	 * Get summary text of evaluation
	 * 
	 * @return summary of evaluation
	 */
	public String getSummaryReportText();

	/**
	 * Get URL of summary report file
	 * 
	 * @return URL of summary report
	 */
	public String getSummaryReportUrl();

	/**
	 * Set URL of summary report file
	 * 
	 * @param reportUrl
	 *            URL of summary report
	 */
	public void setSummaryReportUrl(String reportUrl);

	/**
	 * Accept {@link IProblemItemVisitor}. This method can be used to export
	 * problem list, filter some problem items, etc.
	 * 
	 * @param visitor
	 *            target {@link IProblemItemVisitor} to accept
	 */
	public void accept(IProblemItemVisitor visitor);

	/**
	 * Get URL of evaluation target
	 * 
	 * @return URL of evaluation target
	 */
	public String getTargetUrl();

	/**
	 * Set URL of evaluation target
	 * 
	 * @param targetUrl
	 *            URL of evaluation target
	 */
	public void setTargetUrl(String targetUrl);

	/**
	 * Get evaluation target (source) as File
	 * 
	 * @return target source {@link File}
	 */
	public File getSourceFile();

	/**
	 * Set evaluation target (source) as File
	 * 
	 * @param sourceFile
	 *            target source {@link File}
	 */
	public void setSourceFile(File sourceFile);

	/**
	 * Check if non-selected guideline item will be shown in the result view
	 * 
	 * @return true if need to show all guideline item
	 */
	public boolean isShowAllGuidelineItems();

	/**
	 * Set flag to show non-selected guideline item in the result view
	 * 
	 * @param b
	 *            true to show all guideline item in the result view
	 */
	public void setShowAllGuidelineItems(boolean b);

	/**
	 * Set {@link LineStyleListener} to be used to determine style of evaluation
	 * summary.
	 * 
	 * @param lsl
	 *            target {@link LineStyleListener}
	 */
	void setLineStyleListener(LineStyleListener lsl);

	/**
	 * Get {@link LineStyleListener} to be used to determine style of evaluation
	 * summary.
	 * 
	 * @return {@link LineStyleListener}
	 */
	LineStyleListener getLineStyleListener();

	/**
	 * Add File to evaluation result for future use
	 * 
	 * @param target
	 *            target File
	 * @return true, if the target File is successfully added
	 */
	public boolean addAssociateFile(File target);

	/**
	 * Remove File from evaluation result
	 * 
	 * @param target
	 *            target File
	 * @return true, if the target File is successfully removed
	 */
	public boolean removeAssociatedFile(File target);

	/**
	 * Get associated {@link File}
	 * 
	 * @return array of associated {@link File}
	 */
	public File[] getAssociateFiles();

}
