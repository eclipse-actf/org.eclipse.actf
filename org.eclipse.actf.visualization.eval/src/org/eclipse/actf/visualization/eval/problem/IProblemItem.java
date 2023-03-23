/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.eval.problem;

import org.eclipse.actf.visualization.eval.IEvaluationItem;
import org.eclipse.swt.graphics.Image;
import org.w3c.dom.Node;

/**
 * Interface for problem information
 */
public interface IProblemItem {

	/**
	 * Severity: error
	 */
	public static final int SEV_ERROR = IEvaluationItem.SEV_ERROR;

	/**
	 * Severity: warning
	 */
	public static final int SEV_WARNING = IEvaluationItem.SEV_WARNING;

	/**
	 * Severity: user check
	 */
	public static final int SEV_USER = IEvaluationItem.SEV_USER;

	/**
	 * Severity: informational
	 */
	public static final int SEV_INFO = IEvaluationItem.SEV_INFO;

	/**
	 * @return related evaluation item information
	 */
	public abstract IEvaluationItem getEvaluationItem();

	/**
	 * @return description about the problem
	 */
	public abstract String getDescription();

	/**
	 * @return serial number
	 */
	public abstract int getSerialNumber();

	/**
	 * @return target {@link Node} of the problem
	 */
	public abstract Node getTargetNode();

	/**
	 * @return target String will be embedded into description
	 */
	public abstract String getTargetString();

	/**
	 * @return target String will be used to export this problem item
	 */
	public abstract String getTargetStringForExport();

	/**
	 * @return true if this error position can highlight in visualization result
	 *         view
	 */
	public abstract boolean isCanHighlight();

	/**
	 * Set if his error position can highlight in visualization result view
	 * 
	 * @param canHighlight
	 *            true if this error position can highlight in visualization
	 *            result view
	 */
	public abstract void setCanHighlight(boolean canHighlight);

	/**
	 * Set {@link IEvaluationItem} relates to this problem
	 * 
	 * @param evalItem
	 */
	public abstract void setEvaluationItem(IEvaluationItem evalItem);

	/**
	 * Set description about this problem
	 * 
	 * @param description
	 */
	public abstract void setDescription(String description);

	/**
	 * Set serial number for this problem
	 * 
	 * @param serialNumber
	 *            serial number
	 */
	public abstract void setSerialNumber(int serialNumber);

	/**
	 * Set {@link HighlightTargetNodeInfo}.
	 * 
	 * @param targetNodeInfo
	 *            target {@link HighlightTargetNodeInfo}
	 */
	void setHighlightTargetNodeInfo(HighlightTargetNodeInfo targetNodeInfo);

	/**
	 * @return corresponding {@link HighlightTargetNodeInfo} of this problem
	 */
	HighlightTargetNodeInfo getHighlightTargetNodeInfo();

	/**
	 * Set {@link HighlightTargetId} corresponds to this problem
	 * 
	 * @param targetId
	 *            target {@link HighlightTargetId}
	 */
	void setHighlightTargetIds(HighlightTargetId targetId);

	/**
	 * Set array of {@link HighlightTargetId} corresponds to this problem
	 * 
	 * @param targetIds
	 *            array of target {@link HighlightTargetId}
	 */
	void setHighlightTargetIds(HighlightTargetId[] targetIds);

	/**
	 * @return array of {@link HighlightTargetId} corresponds to this problem
	 */
	HighlightTargetId[] getHighlightTargetIds();

	/**
	 * Set target {@link Node} of this problem
	 * 
	 * @param targetNode
	 *            target Node
	 */
	public abstract void setTargetNode(Node targetNode);

	/**
	 * Set target String will be embedded into description
	 * 
	 * @param targetString
	 *            target String
	 */
	public abstract void setTargetString(String targetString);

	/**
	 * Set target String will be used to export this problem item
	 * 
	 * @param targetString
	 *            target String
	 */
	public abstract void setTargetStringForExport(String targetString);

	/**
	 * @return line number of the problem position
	 */
	public abstract int getLine();

	/**
	 * @return line number of the problem position as String
	 */
	public abstract String getLineStr();

	/**
	 * Set line number of the problem position
	 * 
	 * @param line
	 *            line number
	 */
	public abstract void setLine(int line);

	/**
	 * @return related line numbers of the problem as comma separated String
	 */
	public abstract String getLineStrMulti();

	/**
	 * Set {@link HighlightTargetSourceInfo} corresponds to this problem
	 * 
	 * @param targetSourceInfo
	 *            target {@link HighlightTargetSourceInfo}
	 */
	void setHighlightTargetSourceInfo(HighlightTargetSourceInfo targetSourceInfo);

	/**
	 * Set array of {@link HighlightTargetSourceInfo} corresponds to this
	 * problem
	 * 
	 * @param targetSourceInfo
	 *            array of target {@link HighlightTargetSourceInfo}
	 */
	void setHighlightTargetSourceInfo(
			HighlightTargetSourceInfo[] targetSourceInfo);

	/**
	 * @return corresponding {@link HighlightTargetSourceInfo} of this problem
	 */
	HighlightTargetSourceInfo[] getHighlightTargetSoruceInfo();

	/**
	 * Accept {@link IProblemItemVisitor}. This method can be used to export
	 * problem list, filter some problem items, etc.
	 * 
	 * @param visitor
	 *            target {@link IProblemItemVisitor} to accept
	 */
	public abstract void accept(IProblemItemVisitor visitor);

	/**
	 * @return evaluation item ID
	 */
	public String getId();

	/**
	 * @return get severity of this problem
	 */
	public int getSeverity();

	/**
	 * @return get severity as String
	 */
	public String getSeverityStr();

	/**
	 * @return get evaluation metrics scores relate to this problem
	 */
	public int[] getMetricsScores();

	/**
	 * @return get icons for evaluation metrics
	 */
	public Image[] getMetricsIcons();

	/**
	 * @return get related guideline information as String array
	 */
	public String[] getTableDataGuideline();

	/**
	 * @return sub type of the problem
	 */
	public abstract int getSubType();

	/**
	 * Set problem sub type
	 * 
	 * @param subType
	 *            problem sub type
	 */
	public void setSubType(int subType);

}
