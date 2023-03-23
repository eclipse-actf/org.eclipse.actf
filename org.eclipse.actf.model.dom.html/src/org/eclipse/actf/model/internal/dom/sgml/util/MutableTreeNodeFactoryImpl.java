/*******************************************************************************
 * Copyright (c) 1998, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Goh KONDOH - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.internal.dom.sgml.util;

import javax.swing.tree.*;

import org.w3c.dom.*;

import java.util.Hashtable;

public class MutableTreeNodeFactoryImpl implements IMutableTreeNodeFactory {
	/**
	 * Key: DOM Node, Value: MutableTreeNodeImpl
	 */
	private Hashtable<Node, MutableTreeNodeImpl> table = new Hashtable<Node, MutableTreeNodeImpl>();

	public MutableTreeNode createNode(Node node) {
		MutableTreeNodeImpl ret = table.get(node);
		if (ret == null) {
			ret = new MutableTreeNodeImpl(node, this);
			table.put(node, ret);
		}
		return ret;
	}

	public MutableTreeNode getNode(Node node) {
		return table.get(node);
	}
}
