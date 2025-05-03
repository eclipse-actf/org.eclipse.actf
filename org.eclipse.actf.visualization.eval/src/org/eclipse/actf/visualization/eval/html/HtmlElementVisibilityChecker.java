/*******************************************************************************
 * Copyright (c) 2025 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.eval.html;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.actf.model.ui.editor.browser.ICurrentStyles;
import org.eclipse.actf.model.ui.editor.browser.IWebBrowserStyleInfo;
import org.eclipse.actf.util.logging.DebugPrintUtil;
import org.eclipse.actf.util.xpath.XPathCreator;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class HtmlElementVisibilityChecker {
	private Map<String, ICurrentStyles> map;
	private TreeSet<String> displayNoneSet = new TreeSet<>();
	private HashSet<String> visibilityHiddenSet = new HashSet<>();

	public HtmlElementVisibilityChecker(IWebBrowserStyleInfo styleInfo) {
		map = styleInfo.getCurrentStyles();
		Set<String> keys = new TreeSet<String>(map.keySet());
		for (String key : keys) {
			ICurrentStyles styles = map.get(key);

			//DebugPrintUtil.devOrDebugPrintln(key + " : " + styles.getDisplay() + " / " + styles.getVisibility());

			if ("none".equals(styles.getDisplay())) {
				displayNoneSet.add(key);
			}
			if ("hidden".equals(styles.getVisibility())) {
				visibilityHiddenSet.add(key);
			}
		}
		keys.removeAll(displayNoneSet);
		HashSet<String> displayNoneDescendantSet = new HashSet<String>();
		for (String key : keys) {
			for (String xpath : displayNoneSet) {
				if (key.startsWith(xpath)) {
					displayNoneDescendantSet.add(key);
					break;
				}
			}
		}


		displayNoneSet.addAll(displayNoneDescendantSet);

		DebugPrintUtil.devOrDebugPrintln("----------");
		for (String xpath : displayNoneSet) {
			DebugPrintUtil.devOrDebugPrintln(xpath);
		}
		DebugPrintUtil.devOrDebugPrintln("----------");
	}

	public boolean isDisplayNoneOrVisibilityHidden(String xpath) {
		if (displayNoneSet.contains(xpath)) {
			return true;
		}
		if (visibilityHiddenSet.contains(xpath)) {
			return true;
		}
		/*
		 * while (xpath.contains("/")) { xpath = xpath.substring(0,
		 * xpath.lastIndexOf("/")); if(displayNoneSet.contains(xpath)) { return true; }
		 * }
		 */
		return false;
	}

	public boolean isDisplayNoneOrVisibilityHidden(Element el) {
		String xpath = XPathCreator.childPathSequence(el);
		return (isDisplayNoneOrVisibilityHidden(xpath));
	}

	public boolean isDisplayNoneOrVisibilityHidden(Node node) {
		if (node instanceof Element) {
			return isDisplayNoneOrVisibilityHidden((Element) node);
		}
		try {
			return isDisplayNoneOrVisibilityHidden((Element) node.getParentNode());
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isDisplayNone(String xpath) {
		if (displayNoneSet.contains(xpath)) {
			return true;
		}
		/*
		 * while (xpath.contains("/")) { xpath = xpath.substring(0,
		 * xpath.lastIndexOf("/")); if(displayNoneSet.contains(xpath)) { return true; }
		 * }
		 */
		return (false);	
	}
	
	public boolean isDisplayNone(Element el) {
		String xpath = XPathCreator.childPathSequence(el);
		return (isDisplayNone(xpath));
	}	

	public boolean isDisplayNone(Node node) {
		if (node instanceof Element) {
			return isDisplayNone((Element) node);
		}
		try {
			return isDisplayNone((Element) node.getParentNode());
		} catch (Exception e) {
			return false;
		}
	}
}
