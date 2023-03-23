/*******************************************************************************
 * Copyright (c) 2005, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Hironobu TAKAGI - initial API and implementation
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.engines.blind.html;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.eclipse.actf.visualization.engines.blind.html.IBlindProblem;
import org.eclipse.actf.visualization.engines.blind.html.util.Id2LineViaActfId;
import org.eclipse.actf.visualization.eval.problem.HighlightTargetId;
import org.eclipse.actf.visualization.eval.problem.HighlightTargetSourceInfo;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.eval.problem.ProblemItemImpl;
import org.eclipse.actf.visualization.util.html2view.Html2ViewMapData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Implementation of {@link IProblemItem} for detected problems through blind
 * visualization
 */
public class BlindProblem extends ProblemItemImpl implements IBlindProblem {

	private static final String ID = "id"; //$NON-NLS-1$

	private List<Node> nodeList = null;

	private int nodeId = -1;

	private boolean isMulti = false;

	private ArrayList<HighlightTargetId> idsList = new ArrayList<HighlightTargetId>();

	/**
	 * Constructor for BlindProblem.
	 * 
	 * @param _subtype
	 *            subType of problem
	 */
	public BlindProblem(int _subtype) {
		this(_subtype, ""); //$NON-NLS-1$
	}

	/**
	 * Constructor for BlindProblem.
	 * 
	 * @param _subtype
	 *            subType of problem
	 * @param targetString
	 *            target String to be embedded to error description
	 */
	public BlindProblem(int _subtype, String targetString) {
		super("B_" + Integer.toString(_subtype)); //$NON-NLS-1$

		subType = _subtype;

		nodeList = new Vector<Node>();
		setTargetString(targetString);

		switch (_subtype) {
		case WRONG_ALT_IMG:
		case WRONG_ALT_INPUT:
		case WRONG_ALT_AREA:
		case WRONG_TEXT:
		case ALERT_WRONG_ALT:
		case SEPARATE_DBCS_ALT_IMG:
		case SEPARATE_DBCS_ALT_INPUT:
		case SEPARATE_DBCS_ALT_AREA:
			// case WRONG_SKIP_LINK_TEXT:
			// case WRONG_SKIP_LINK_TITLE:
			// case WRONG_TITLE_IFRAME:
		case SEPARATE_DBCS_INPUT_VALUE:
		case REDUNDANT_ALT:
		case ALERT_REDUNDANT_TEXT:
		case NO_DEST_LINK:
		case NO_DEST_SKIP_LINK:
		case ALERT_NO_DEST_INTRA_LINK:
		case ALERT_SPELL_OUT:
			this.targetStringForHPB = targetString;
			break;
		default:
		}
	}

	/**
	 * @return target Node in visualization result {@link Document}
	 */
	public Node getTargetNodeInResultDoc() {
		if (nodeList.size() > 0) {
			return nodeList.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Set target {@link Node}
	 * 
	 * @param node
	 *            target Node
	 */
	public void setNode(Node node) {
		nodeList.add(0, node);
	}

	/**
	 * Add additional target {@link Node}
	 * 
	 * @param node
	 *            additional target Node
	 */
	public void addNode(Node node) {
		nodeList.add(node);
	}

	/**
	 * Sets target {@link Node} and ID of Node
	 * 
	 * @param node
	 *            target Node
	 * @param id
	 *            target ID
	 */
	public void setNode(Node node, int id) {
		nodeList.add(0, node);
		this.nodeId = id;
	}

	public String toString() {
		return "node=" + nodeId + ":" + getDescription(); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * @return List of target {@link Node}
	 */
	public List<Node> getNodeList() {
		return nodeList;
	}

	/**
	 * Sets the ID of target {@link Node}.
	 * 
	 * @param nodeId
	 *            target Node ID
	 * @return true if former target Node ID was not set yet
	 */
	public boolean setNodeId(int nodeId) {
		if (this.nodeId == -1) {
			this.nodeId = nodeId;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Add {@link HighlightTargetId}
	 * 
	 * @param target
	 */
	public void addNodeIds(HighlightTargetId target) {
		isMulti = true;
		idsList.add(target);
	}

	public void prepareHighlight() {

		if (isMulti) {
			HighlightTargetId[] targets = new HighlightTargetId[idsList.size()];
			idsList.toArray(targets);
			setHighlightTargetIds(targets);
		} else {
			// setHighlightTargetIds(nodeId, nodeId);
			if (nodeId > -1) {
				setHighlightTargetIds(new HighlightTargetId(nodeId, nodeId));
			}
		}
	}

	private int getElementId(Element target) {
		int result = -1;
		String tmpId = target.getAttribute(ID);
		if (tmpId.length() > 0 && tmpId.startsWith(ID)) {
			tmpId = tmpId.substring(tmpId.indexOf(ID) + 2);
			try {
				result = Integer.parseInt(tmpId);
			} catch (Exception e) {
			}
		}
		return result;
	}

	/**
	 * @param id2line
	 */
	public void setLineNumber(Id2LineViaActfId id2line) {

		switch (subType) {
		case WRONG_TEXT:
			try {
				// text_node -> span_for_visualize -> real_parent
				Node tmpN = getTargetNodeInResultDoc().getParentNode()
						.getParentNode();
				if (tmpN != null && tmpN.getNodeType() == Node.ELEMENT_NODE) {
					int id = getElementId((Element) tmpN);
					if (id > -1) {
						// setLine(id2line.getLine(id));
						Html2ViewMapData tmpData = id2line.getViewMapData(id);
						if (tmpData != null) {
							setHighlightTargetSourceInfo(new HighlightTargetSourceInfo(
									tmpData, tmpData));
						}
					}
				}
			} catch (Exception e1) {
				// e1.printStackTrace();
			}
			break;
		case NO_ALT_AREA:
		case WRONG_ALT_AREA:
		case SEPARATE_DBCS_ALT_AREA:
			// int tmpNodeId = nodeId;
			Node tmpN = getTargetNodeInResultDoc();
			if (tmpN.getNodeType() == Node.ELEMENT_NODE) {
				int id = getElementId((Element) tmpN);
				if (id > -1) {
					Html2ViewMapData tmpData = id2line.getViewMapData(id);
					setHighlightTargetSourceInfo(new HighlightTargetSourceInfo(
							tmpData, tmpData));
					// setLine(id2line.getLine(id));
				}
			}
			break;
		default:
			// TODO divide more by using case
			if (isMulti) {

				ArrayList<HighlightTargetSourceInfo> tmpArray = new ArrayList<HighlightTargetSourceInfo>();

				HighlightTargetId[] targets = new HighlightTargetId[idsList
						.size()];
				idsList.toArray(targets);
				for (int i = 0; i < targets.length; i++) {
					Html2ViewMapData startData = id2line
							.getViewMapData(targets[i].getStartId());
					Html2ViewMapData endData = id2line
							.getViewMapData(targets[i].getEndId());

					if (startData == null) {
						startData = endData;
					}
					if (endData == null) {
						endData = startData;
					}

					if (startData != null) {
						tmpArray.add(new HighlightTargetSourceInfo(startData,
								endData));
					}
				}

				HighlightTargetSourceInfo[] sourceInfo = new HighlightTargetSourceInfo[tmpArray
						.size()];
				tmpArray.toArray(sourceInfo);
				setHighlightTargetSourceInfo(sourceInfo);

			} else {

				Html2ViewMapData tmpData = id2line.getViewMapData(nodeId);
				if (tmpData != null) {
					setHighlightTargetSourceInfo(new HighlightTargetSourceInfo(
							tmpData, tmpData));
				}
				// check
				// setLine(id2line.getLine(nodeId));

			}
		}
	}
}
