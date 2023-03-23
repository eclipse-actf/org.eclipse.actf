/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tatsuya ISHIHARA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.model.dom.odf.base.impl;

import org.eclipse.actf.model.dom.odf.base.ODFNodeList;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


class ODFNodeListImpl implements ODFNodeList {
	private ODFDocumentImpl owner;

	private NodeList iNodeList;

	public ODFNodeListImpl(ODFDocumentImpl owner, NodeList iNodeList) {
		this.owner = owner;
		this.iNodeList = iNodeList;
	}

	public Node item(int index) {
		Node iNode = iNodeList.item(index);
		if (iNode == null)
			return null;
		return owner.getODFNode(iNode);
	}

	public int getLength() {
		return iNodeList.getLength();
	}
}
