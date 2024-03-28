/*******************************************************************************
 * Copyright (c) 2010, 2024 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.validation.html.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.eclipse.actf.visualization.eval.html.HtmlTagUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

class FieldsetManager {
	private Map<String, List<Element>> ctrlMap = new HashMap<String, List<Element>>();

	private Map<String, Set<Node>> groupingMap = new HashMap<String, Set<Node>>();

	private List<Vector<Node>> multipleList = new ArrayList<Vector<Node>>();
	private List<Vector<Node>> missingList = new ArrayList<Vector<Node>>();

	public FieldsetManager(List<Element> radioAndCheckList) {
		for (Element ctrl : radioAndCheckList) {
			addEntry(ctrl.getAttribute("name"), ctrl);
		}

		for (String key : ctrlMap.keySet()) {

			Set<Node> tmpSet = groupingMap.get(key);
			int size = tmpSet.size();

			if (size > 1) {
				multipleList.add(new Vector<Node>(ctrlMap.get(key)));
			} else if (size == 1 && !tmpSet.contains(null)) {
			} else {
				missingList.add(new Vector<Node>(ctrlMap.get(key)));
			}
		}

	}

	/**
	 * Adds a form control with the specified name attribute.
	 * 
	 * @param name
	 * @param ctrl
	 */
	public void addEntry(String name, Element ctrl) {
		List<Element> list;
		Set<Node> groupingSet;
		if (ctrlMap.containsKey(name)) {
			list = ctrlMap.get(name);
			groupingSet = groupingMap.get(name);
		} else {
			list = new ArrayList<Element>();
			ctrlMap.put(name, list);
			groupingSet = new HashSet<Node>();
			groupingMap.put(name, groupingSet);
		}
		list.add(ctrl);

		groupingSet.add(getFirstAncestorGrouping(ctrl));

	}

	private Node getFirstAncestorGrouping(Node target) {
		Node tmpNode = target;
		while (tmpNode != null) {
			if (tmpNode.getNodeName().equals("fieldset")) {
				return tmpNode;
			} else if (tmpNode instanceof Element) {
				String role = ((Element) tmpNode).getAttribute("role");
				if ("group".equals(role) || "radiogroup".equals(role)) {
					return tmpNode;
				}
			}
			tmpNode = tmpNode.getParentNode();
		}
		return null;
	}

	/**
	 * @return the multipleList
	 */
	public List<Vector<Node>> getMultipleList() {
		return multipleList;
	}

	/**
	 * @return the missingList
	 */
	public List<Vector<Node>> getMissingList() {
		return missingList;
	}

}
