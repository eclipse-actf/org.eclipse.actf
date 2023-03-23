/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.util.dom;

import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;

/**
 * Implementation class of NodeFilter. This filter uses whatToShow attributes to
 * define the filter behavior.
 */
public class WhatToShowNodeFilter implements NodeFilter {

	private int filter;

	/**
	 * Constructor of WhatToShowNodeFilter
	 * 
	 * @param whatToShow
	 *            the attribute determines which types of node are presented.
	 *            The values are defined in the NodeFilter interface.
	 */
	public WhatToShowNodeFilter(int whatToShow) {
		this.filter = whatToShow;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.traversal.NodeFilter#acceptNode(org.w3c.dom.Node)
	 */
	public short acceptNode(Node arg0) {
		if (null == arg0) {
			return FILTER_REJECT;
		}

		if ((filter & (1 << (arg0.getNodeType() - 1))) != 0) {
			return FILTER_ACCEPT;
		}
		return FILTER_SKIP;
	}
}
