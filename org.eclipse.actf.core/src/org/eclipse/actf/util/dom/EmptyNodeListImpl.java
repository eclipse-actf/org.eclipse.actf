/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *    Hisashi MIYASHITA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.util.dom;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Utility class working as an empty NodeList
 */
public class EmptyNodeListImpl implements NodeList {

	private static final NodeList INSTANCE = new EmptyNodeListImpl();

	/**
	 * Return an empty NodeList instance
	 * @return empty NodeList
	 */
	public static NodeList getInstance() {
		return INSTANCE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.NodeList#item(int)
	 */
	public Node item(int index) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.NodeList#getLength()
	 */
	public int getLength() {
		return 0;
	}

}
