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

package org.eclipse.actf.visualization.internal.engines.blind.html.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.actf.visualization.engines.blind.html.IVisualizeMapData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class VisualizeMapDataImpl implements IVisualizeMapData {

	private Map<Node, Node> orig2resultMap = new HashMap<Node, Node>(1024); // orig
																			// node
																			// <>
																			// result
																			// node

	private Map<Node, Node> result2origMap = new HashMap<Node, Node>(1024); // result
																			// node
																			// <>
																			// orig
																			// node

	private Map<Node, Integer> orig2idMap = new HashMap<Node, Integer>(1024); // orig
																				// node
																				// <>
																				// result
																				// node's
																				// id

	private Map<Node, Integer> result2idMap = new HashMap<Node, Integer>(1024); // result
																				// node
																				// <>
																				// result
																				// node's
																				// id

	private Map<Integer, Integer> accId2id = new HashMap<Integer, Integer>(1024);

	private Map<Integer, Integer> id2accId = new HashMap<Integer, Integer>(1024);

	private Map<Node, VisualizationNodeInfo> node2infoMap = new HashMap<Node, VisualizationNodeInfo>(
			1024);

	private Map<Node, Node> removedNodeMap = new HashMap<Node, Node>(512);

	private Map<Node, Node> intraPageLinkMap = new HashMap<Node, Node>(256);

	private List<VisualizationNodeInfo> nodeInfoList = new ArrayList<VisualizationNodeInfo>(
			1024);

	/**
     *  
     */
	public VisualizeMapDataImpl() {

	}

	protected Map<Node, Node> getOrig2ResultMap() {
		return (orig2resultMap);
	}

	/**
	 * @return Returns the accId2idMap.
	 */
	protected Map<Integer, Integer> getAccId2IdMap() {
		return accId2id;
	}

	/**
	 * @return Returns the id2accIdMap.
	 */
	public Map<Integer, Integer> getId2AccIdMap() {
		return id2accId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.actf.visualization.engines.blind.html.IVisualizeMapData#
	 * getOrig2idMap()
	 */
	public Map<Node, Integer> getOrig2idMap() {
		return orig2idMap;
	}

	/**
	 * @return Returns the result2idMap.
	 */
	public Map<Node, Integer> getResult2idMap() {
		return result2idMap;
	}

	/**
	 * @return Returns the intraPageLinkMap.
	 */
	protected Map<Node, Node> getIntraPageLinkMap() {
		return intraPageLinkMap;
	}

	/**
	 * @return Returns the nodeInfoList.
	 */
	public List<VisualizationNodeInfo> getNodeInfoList() {
		return nodeInfoList;
	}

	/**
	 * @return Returns the node2infoMap.
	 */
	protected Map<Node, VisualizationNodeInfo> getNode2infoMap() {
		return node2infoMap;
	}

	//

	protected void addOrigResultMapping(Node orig, Node result) {
		orig2resultMap.put(orig, result);
		result2origMap.put(result, orig);
	}

	public void addOrigIdAccIdMapping(Integer orig, Integer acc) {
		accId2id.put(acc, orig);
		id2accId.put(orig, acc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.actf.visualization.engines.blind.html.IVisualizeMapData#
	 * addReplacedNodeMapping(org.w3c.dom.Node, org.w3c.dom.Node)
	 */
	public void addReplacedNodeMapping(Node result, Node replacement) {
		removedNodeMap.put(result, replacement);
	}

	protected void addNodeIdMapping(Node result, Integer id) {
		result2idMap.put(result, id);
		orig2idMap.put(getOrigNode(result), id);
	}

	protected void addNodeInfoMapping(Node result,
			VisualizationNodeInfo nodeInfo) {
		node2infoMap.put(result, nodeInfo);
	}

	protected void addNodeInfoIntoList(VisualizationNodeInfo info) {
		nodeInfoList.add(info);
	}

	protected void addIntraPageLinkMapping(Node source, Node dest) {
		intraPageLinkMap.put(source, dest);
	}

	//

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.actf.visualization.engines.blind.html.IVisualizeMapData#
	 * getOrigNode(org.w3c.dom.Node)
	 */
	public Node getOrigNode(Node result) {
		return result2origMap.get(result);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.actf.visualization.engines.blind.html.IVisualizeMapData#
	 * getResultNode(org.w3c.dom.Node)
	 */
	public Node getResultNode(Node orig) {
		return orig2resultMap.get(orig);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.actf.visualization.engines.blind.html.IVisualizeMapData#
	 * getReplacement(org.w3c.dom.Node)
	 */
	public Node getReplacement(Node result) {
		return removedNodeMap.get(result);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.actf.visualization.engines.blind.html.IVisualizeMapData#
	 * getIdOfNode(org.w3c.dom.Node)
	 */
	public Integer getIdOfNode(Node result) {
		return result2idMap.get(result);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.actf.visualization.engines.blind.html.IVisualizeMapData#
	 * getIdOfOrigNode(org.w3c.dom.Node)
	 */
	public Integer getIdOfOrigNode(Node orig) {
		return orig2idMap.get(orig);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.actf.visualization.engines.blind.html.IVisualizeMapData#
	 * getNodeInfo(org.w3c.dom.Node)
	 */
	public VisualizationNodeInfo getNodeInfo(Node result) {
		return node2infoMap.get(result);
	}

	public void makeIdMapping(String targetIdS) {
		Set<Node> nodeSet = getResult2idMap().keySet();
		for (Node node : nodeSet) {
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Integer targetId = getIdOfNode(node);
				Element tmpE = (Element) node;
				tmpE.setAttribute("id", "id" + (targetId.toString())); //$NON-NLS-1$ //$NON-NLS-2$

				if (tmpE.hasAttribute(targetIdS)) {
					try {
						Integer accId = new Integer(tmpE
								.getAttribute(targetIdS));
						addOrigIdAccIdMapping(targetId, accId);
					} catch (Exception e) {
					}
				}
				DocumentCleaner.removeOnMouse((Element) node);
			}
		}

	}

}
