/*******************************************************************************
 * Copyright (c) 2010, 2023 IBM Corporation and Others
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
	private Map<String, Set<Node>> fieldSetMap = new HashMap<String, Set<Node>>();

	/**
	 * Adds a form control with the specified name attribute.
	 * 
	 * @param name
	 * @param ctrl
	 */
	public void addEntry(String name, Element ctrl) {
		List<Element> list;
		Set<Node> set;
		if (ctrlMap.containsKey(name)) {
			list = ctrlMap.get(name);
			set = fieldSetMap.get(name);
		} else {
			list = new ArrayList<Element>();
			ctrlMap.put(name, list);
			set = new HashSet<Node>();
			fieldSetMap.put(name, set);
		}
		list.add(ctrl);

		Node fieldset = HtmlTagUtil.getAncestor(ctrl, "fieldset");
		set.add(fieldset);
	}

	public List<Vector<Node>> getErrorList() {
		List<Vector<Node>> returns = new ArrayList<Vector<Node>>();
		for (String key : fieldSetMap.keySet()) {
			//System.out.println(key + " : " + fieldSetMap.get(key).size());
			int size = fieldSetMap.get(key).size();
			if (size > 1) {
				returns.add(new Vector<Node>(ctrlMap.get(key)));
			} else if (size == 1 && null == fieldSetMap.get(key).toArray()[0]) {
				returns.add(new Vector<Node>(ctrlMap.get(key)));
			}
		}
		return returns;
	}
}
