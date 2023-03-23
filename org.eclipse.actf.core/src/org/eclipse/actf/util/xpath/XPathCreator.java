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

package org.eclipse.actf.util.xpath;

import org.w3c.dom.Node;

/**
 * Utility class for creating XPath of the target Node.
 */
public class XPathCreator {

	private static final String TEXT_NODE_NAME = "#text"; //$NON-NLS-1$
	private static final String TEXT_NODE = "text()"; //$NON-NLS-1$
	private static final String RIGHT_BRACKET = "]"; //$NON-NLS-1$
	private static final String LEFT_BRACKET = "["; //$NON-NLS-1$
	private static final String SLASH = "/"; //$NON-NLS-1$

	/**
	 * Return XPath (child path sequence) of the target Node.
	 * 
	 * @param target
	 *            the target Node
	 * @return XPath (child path sequence)
	 */
	public static String childPathSequence(Node target) {
		StringBuffer tmpSB = new StringBuffer();

		Node owner = null;
		if (target != null) {
			owner = target.getOwnerDocument();
		}
		while (target != null && target != owner) {
			// short currentType = target.getNodeType();
			String currentName = target.getNodeName();
			int count = countSiblingByName(target, currentName);

			if (TEXT_NODE_NAME.equalsIgnoreCase(currentName)) {
				currentName = TEXT_NODE;
			}

			if (count > 0) {
				count = count + 1;
				tmpSB.insert(0, SLASH + currentName + LEFT_BRACKET + count
						+ RIGHT_BRACKET);
			} else {
				tmpSB.insert(0, SLASH + currentName);
			}
			target = target.getParentNode();
		}

		return (tmpSB.toString());
	}

	private static int countSiblingByName(Node target, String name) {
		int count = 0;
		if (target != null) {
			target = target.getPreviousSibling();
		}
		while (target != null) {
			if (target.getNodeName().equals(name)) {
				count++;
			}
			target = target.getPreviousSibling();
		}
		return count;
	}

}
