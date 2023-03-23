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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.actf.visualization.util.html2view.Html2ViewMapData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Utility class to get highlight target information.
 */
public class HighlightTargetNodeInfo {
	private static final int SINGLE = 0;

	private static final int RANGE = 1;

	private static final int MULTI = 2;

	private int mode;

	private Node targetNode;

	private Node startTarget;

	private Node endTarget;

	private Node[] targets;

	/**
	 * Constructor of the class
	 * 
	 * @param target
	 *            target {@link Node} to highlight corresponding positions
	 */
	public HighlightTargetNodeInfo(Node target) {
		mode = SINGLE;
		targetNode = target;
	}

	/**
	 * Constructor of the class
	 * 
	 * @param startTarget
	 *            start {@link Node} to highlight corresponding positions
	 * @param endTarget
	 *            end {@link Node} to highlight corresponding positions
	 */
	public HighlightTargetNodeInfo(Node startTarget, Node endTarget) {
		mode = RANGE;
		this.startTarget = startTarget;
		this.endTarget = endTarget;
	}

	/**
	 * Constructor of the class
	 * 
	 * @param targets
	 *            list of target {@link Node} to highlight corresponding
	 *            positions
	 */
	public HighlightTargetNodeInfo(List<Node> targets) {
		mode = MULTI;
		this.targets = new Node[0];
		if (targets != null) {
			this.targets = new Node[targets.size()];
			try {
				targets.toArray(this.targets);
			} catch (Exception e) {
				this.targets = new Node[0];
			}

		}
	}

	/**
	 * Get corresponding {@link Node} IDs relate to the target {@link Node}
	 * 
	 * @param node2Id
	 *            map between {@link Node} and ID
	 * @return target ID information as array of {@link HighlightTargetId}
	 */
	public HighlightTargetId[] getHighlightTargetIds(Map<Node, Integer> node2Id) {
		HighlightTargetId[] result = new HighlightTargetId[0];
		switch (mode) {
		case SINGLE:
			if (node2Id != null && node2Id.containsKey(targetNode)) {
				try {
					int nodeId = node2Id.get(targetNode).intValue();
					result = new HighlightTargetId[1];
					result[0] = new HighlightTargetId(nodeId, nodeId);
				} catch (Exception e) {
				}
			}
			break;
		case RANGE:
			// -1 check?
			if (node2Id != null
					&& (node2Id.containsKey(startTarget) || node2Id
							.containsKey(endTarget))) {
				int state = 0;
				int start = 0;
				int end = 0;
				try {
					start = node2Id.get(startTarget).intValue();
					state += 1;
				} catch (Exception e) {
				}
				try {
					end = node2Id.get(endTarget).intValue();
					state += 2;
				} catch (Exception e) {
				}
				switch (state) {
				case 0:
					result = new HighlightTargetId[0];
					break;
				case 1:
					result = new HighlightTargetId[1];
					result[0] = new HighlightTargetId(start, start);
					break;
				case 2:
					result = new HighlightTargetId[1];
					result[0] = new HighlightTargetId(end, end);
					break;
				case 3:
					result = new HighlightTargetId[1];
					result[0] = new HighlightTargetId(start, end);
					break;
				default:
				}
			}
			break;
		case MULTI:
			Vector<HighlightTargetId> tmpV = new Vector<HighlightTargetId>();
			for (int i = 0; i < targets.length; i++) {
				if (node2Id != null && node2Id.containsKey(targets[i])) {
					try {
						int nodeId = node2Id.get(targets[i]).intValue();
						tmpV.add(new HighlightTargetId(nodeId, nodeId));
					} catch (Exception e) {
					}
				}
			}

			result = new HighlightTargetId[tmpV.size()];
			tmpV.toArray(result);
			break;
		default:
		}
		return result;
	}

	/**
	 * Get corresponding HTML source positions relate to the target {@link Node}
	 * 
	 * @param html2ViewMapDataV
	 *            Vector of HTML source position information
	 * @return corresponding HTML source positions as array of
	 *         {@link HighlightTargetSourceInfo}
	 */
	public HighlightTargetSourceInfo[] getHighlightTargetSourceInfo(
			Vector<Html2ViewMapData> html2ViewMapDataV) {
		HighlightTargetSourceInfo[] result = new HighlightTargetSourceInfo[0];
		switch (mode) {
		case SINGLE:
			Html2ViewMapData h2vmd = getViewMapData(targetNode,
					html2ViewMapDataV);
			if (h2vmd != null) {
				result = new HighlightTargetSourceInfo[] { new HighlightTargetSourceInfo(
						h2vmd, h2vmd) };
			}
			break;
		case RANGE:
			Html2ViewMapData startData = getViewMapData(startTarget,
					html2ViewMapDataV);
			Html2ViewMapData endData = getViewMapData(endTarget,
					html2ViewMapDataV);
			if (startData != null && endData != null) {
				result = new HighlightTargetSourceInfo[] { new HighlightTargetSourceInfo(
						startData, endData) };
			} else {
				if (startData == null) {
					result = new HighlightTargetSourceInfo[] { new HighlightTargetSourceInfo(
							endData, endData) };
				} else {
					result = new HighlightTargetSourceInfo[] { new HighlightTargetSourceInfo(
							startData, startData) };
				}
			}
			break;
		case MULTI:
			ArrayList<HighlightTargetSourceInfo> tmpArray = new ArrayList<HighlightTargetSourceInfo>();
			for (int i = 0; i < targets.length; i++) {
				Html2ViewMapData data = getViewMapData(targets[i],
						html2ViewMapDataV);
				if (data != null) {
					tmpArray.add(new HighlightTargetSourceInfo(data, data));
				}
			}
			result = new HighlightTargetSourceInfo[tmpArray.size()];
			tmpArray.toArray(result);
			break;
		default:
		}
		return result;
	}

	private Html2ViewMapData getViewMapData(Node targetN,
			Vector<Html2ViewMapData> html2ViewMapDataV) {
		if (targetN != null && html2ViewMapDataV != null) {
			try {
				String targetId = ((Element) targetN)
						.getAttribute(Html2ViewMapData.ACTF_ID);
				if (!targetId.equals("")) { //$NON-NLS-1$
					int target = Integer.parseInt(targetId);
					if (target > -1 && target < html2ViewMapDataV.size()) {
						Html2ViewMapData h2vmd = html2ViewMapDataV.get(target);
						return h2vmd;
					}
				} else {
					// System.out.println("sethighlight: empty:
					// "+targetN.toString());
				}
			} catch (Exception e) {
				// System.out.println("getViewMapData: "
				// +targetN.getNodeName());
				// e.printStackTrace();
			}
		}
		return null;
	}

}
