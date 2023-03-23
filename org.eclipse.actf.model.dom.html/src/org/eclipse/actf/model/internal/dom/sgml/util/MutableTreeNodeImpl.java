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
import java.util.*;

public class MutableTreeNodeImpl implements MutableTreeNode {
	private Node domNode;

	private IMutableTreeNodeFactory factory;

	public Node getDomNode() {
		return domNode;
	}

	private static IMutableTreeNodeFactory sfactory;

	/**
	 * @deprecated See {@link IMutableTreeNodeFactory}
	 */
	public static MutableTreeNodeImpl createNode(Node node) {
		if (sfactory == null) {
			sfactory = new MutableTreeNodeFactoryImpl();
		}
		return (MutableTreeNodeImpl) sfactory.createNode(node);
	}

	protected MutableTreeNodeImpl(Node node, IMutableTreeNodeFactory factory) {
		this.domNode = node;
		this.factory = factory;
	}

	public String toString() {
		return domNode.toString();
	}

	public TreeNode getChildAt(int childIndex) {
		Node ret;
		for (ret = domNode.getFirstChild(); childIndex > 0 && ret != null; ret = ret
				.getNextSibling(), childIndex--)
			;
		return ret != null ? factory.createNode(ret) : null;
	}

	public int getChildCount() {
		int ret = 0;
		for (Node child = domNode.getFirstChild(); child != null; child = child
				.getNextSibling())
			ret++;
		return ret;
	}

	public TreeNode getParent() {
		Node domParent = domNode.getParentNode();
		return domParent == null ? null : factory.createNode(domParent);
	}

	public int getIndex(TreeNode node) {
		Node targetChild = ((MutableTreeNodeImpl) node).domNode;
		int ret = 0;
		for (Node child = domNode.getFirstChild(); child != null; child = child
				.getNextSibling()) {
			if (child == targetChild) {
				return ret;
			} else {
				ret++;
			}
		}
		return -1;
	}

	public boolean getAllowsChildren() {
		return domNode instanceof Element || domNode instanceof Document;
	}

	public boolean isLeaf() {
		return !(domNode instanceof Element) && !domNode.hasChildNodes()
				&& !(domNode instanceof Document);
	}

	public Enumeration<MutableTreeNode> children() {
		return new Enumeration<MutableTreeNode>() {
			private Node nextChild = domNode.getFirstChild();

			public boolean hasMoreElements() {
				return nextChild != null;
			}

			public MutableTreeNode nextElement() {
				if (nextChild == null)
					throw new NoSuchElementException();
				MutableTreeNode ret = factory.createNode(nextChild);
				nextChild = nextChild.getNextSibling();
				return ret;
			}
		};
	}

	public void insert(MutableTreeNode child, int index) {
		Node after;
		for (after = domNode.getFirstChild(); after != null && index > 0; after = after
				.getNextSibling(), index--)
			;
		domNode.insertBefore(((MutableTreeNodeImpl) child).domNode, after);
	}

	public void remove(int index) {
		Node child;
		for (child = domNode.getFirstChild(); index > 0 && child != null; child = child
				.getNextSibling(), index--)
			;
		if (child != null)
			domNode.removeChild(child);
	}

	public void remove(MutableTreeNode node) {
		domNode.removeChild(((MutableTreeNodeImpl) node).domNode);
	}

	public void setUserObject(Object object) {
		if (object instanceof Node) {
			this.domNode = (Node) object;
		}
	}

	public void removeFromParent() {
		domNode.getParentNode().removeChild(domNode);
	}

	public void setParent(MutableTreeNode newParent) {
	}
}
