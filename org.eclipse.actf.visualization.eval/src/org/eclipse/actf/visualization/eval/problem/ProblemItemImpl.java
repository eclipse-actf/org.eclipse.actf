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

package org.eclipse.actf.visualization.eval.problem;

import org.eclipse.actf.util.logging.DebugPrintUtil;
import org.eclipse.actf.visualization.eval.IEvaluationItem;
import org.eclipse.actf.visualization.eval.guideline.GuidelineHolder;
import org.eclipse.actf.visualization.internal.eval.EvaluationItemImpl;
import org.eclipse.actf.visualization.util.html2view.Html2ViewMapData;
import org.eclipse.swt.graphics.Image;
import org.w3c.dom.Node;

/**
 * Default implementation of {@link IProblemItem}
 */
public class ProblemItemImpl implements IProblemItem {

	private static final String NULL_STRING = ""; //$NON-NLS-1$

	private static GuidelineHolder GUIDELINE_HOLDER = GuidelineHolder
			.getInstance();

	private boolean canHighlight = false;

	private IEvaluationItem checkItem;

	private String description = NULL_STRING;

	private int serialNumber = -1;

	private int line = -1;

	private HighlightTargetNodeInfo highlightTargetNodeInfo;

	private HighlightTargetId[] targetIds = new HighlightTargetId[0];

	private HighlightTargetSourceInfo[] targetSources = new HighlightTargetSourceInfo[0];

	private Node targetNode = null;

	protected String targetStringForHPB = NULL_STRING;

	protected String targetString = NULL_STRING;

	protected int subType;

	// TODO add Icon(for Result doc) info

	/**
	 * Create new ProblemItemImpl for the evaluation item
	 * 
	 * @param id
	 *            evaluation item ID
	 * @see GuidelineHolder#getEvaluationItem(String)
	 */
	@SuppressWarnings("nls")
	public ProblemItemImpl(String id) {
		checkItem = GUIDELINE_HOLDER.getEvaluationItem(id);

		if (checkItem == null) {
			checkItem = new EvaluationItemImpl("unknown",
					EvaluationItemImpl.SEV_INFO_STR);
			DebugPrintUtil.devOrDebugPrintln("Problem Item: unknown id \"" + id
					+ "\"");

		} else {
			description = checkItem.createDescription();
		}

	}

	/**
	 * Create new ProblemItemImpl for the evaluation item and set target Node
	 * 
	 * @param id
	 *            evaluation item ID
	 * @param targetNode
	 *            target Node
	 * @see GuidelineHolder#getEvaluationItem(String)
	 */
	public ProblemItemImpl(String id, Node targetNode) {
		this(id);
		setTargetNode(targetNode);
	}

	public IEvaluationItem getEvaluationItem() {
		return checkItem;
	}

	public String getId() {
		return checkItem.getId();
	}

	public String[] getTableDataGuideline() {
		return checkItem.getTableDataGuideline();
	}

	public int[] getMetricsScores() {
		return checkItem.getMetricsScores();
	}

	public Image[] getMetricsIcons() {
		return checkItem.getMetricsIcons();
	}

	public int getSeverity() {
		return checkItem.getSeverity();
	}

	public String getSeverityStr() {
		return checkItem.getSeverityStr();
	}

	public String getDescription() {
		return description;
	}

	public int getSerialNumber() {
		return serialNumber;
	}

	public Node getTargetNode() {
		return targetNode;
	}

	public String getTargetStringForExport() {
		return targetStringForHPB;
	}

	public boolean isCanHighlight() {
		return canHighlight;
	}

	public void setCanHighlight(boolean canHighlight) {
		this.canHighlight = canHighlight;
	}

	public void setEvaluationItem(IEvaluationItem checkItem) {
		this.checkItem = checkItem;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}

	public void setHighlightTargetIds(HighlightTargetId[] targetIds) {
		if (targetIds != null) {
			this.targetIds = targetIds;
			if (targetIds.length > 0) {
				canHighlight = true;
			}
		}
	}

	public HighlightTargetId[] getHighlightTargetIds() {
		return (targetIds);
	}

	public void setTargetNode(Node targetNode) {
		this.targetNode = targetNode;
	}

	public void setTargetStringForExport(String targetString) {
		this.targetStringForHPB = targetString;
	}

	public String getTargetString() {
		return targetString;
	}

	public void setTargetString(String targetString) {
		this.targetString = targetString;
		if (targetString != null) {
			this.description = checkItem.createDescription(targetString);
		}
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public String getLineStr() {
		int curLine = getLine();
		if (curLine > -1) {
			return (Integer.toString(curLine));
		} else {
			return NULL_STRING;
		}
	}

	public String getLineStrMulti() {
		StringBuffer tmpSB = new StringBuffer();
		// TODO check multiple same line number
		if (targetSources.length > 0) {
			for (int i = 0; i < targetSources.length; i++) {
				int tmp = targetSources[i].getStartLine();
				tmpSB.append(tmp);
				tmpSB.append(", "); //$NON-NLS-1$
			}
			String result = tmpSB.substring(0, tmpSB.length() - 2);
			return result;
		}
		if (line > -1) {
			return (Integer.toString(line));
		} else {
			return NULL_STRING;
		}
	}

	public void setHighlightTargetSourceInfo(
			HighlightTargetSourceInfo[] targetSourceInfo) {
		if (targetSourceInfo != null) {
			targetSources = targetSourceInfo;
			int tmpLine = Integer.MAX_VALUE;
			for (int i = 0; i < targetSources.length; i++) {
				if (tmpLine > targetSources[i].getStartLine()) {
					tmpLine = targetSources[i].getStartLine();
				}
				// TODO
				if (tmpLine != Integer.MAX_VALUE) {
					line = tmpLine;
				}
			}
		}
	}

	public HighlightTargetSourceInfo[] getHighlightTargetSoruceInfo() {
		if (targetSources.length == 0 && line > -1) {
			Html2ViewMapData dummy = new Html2ViewMapData(
					new int[] { line, -1 }, new int[] { line, -1 });
			return (new HighlightTargetSourceInfo[] { new HighlightTargetSourceInfo(
					dummy, dummy) });
		}
		return (targetSources);
	}

	public void accept(IProblemItemVisitor visitor) {
		visitor.visit(this);
	}

	public HighlightTargetNodeInfo getHighlightTargetNodeInfo() {
		return highlightTargetNodeInfo;
	}

	public void setHighlightTargetNodeInfo(
			HighlightTargetNodeInfo targetNodeInfo) {
		this.highlightTargetNodeInfo = targetNodeInfo;
	}

	public void setHighlightTargetIds(HighlightTargetId targetId) {
		if (targetId != null) {
			this.setHighlightTargetIds(new HighlightTargetId[] { targetId });
		}
	}

	public void setHighlightTargetSourceInfo(
			HighlightTargetSourceInfo targetSourceInfo) {
		if (targetSourceInfo != null) {
			this
					.setHighlightTargetSourceInfo(new HighlightTargetSourceInfo[] { targetSourceInfo });
		}
	}

	public int getSubType() {
		return subType;
	}

	public void setSubType(int subType) {
		this.subType = subType;
	}

}
