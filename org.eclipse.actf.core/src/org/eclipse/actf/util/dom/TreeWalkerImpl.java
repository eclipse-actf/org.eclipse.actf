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

import java.util.HashMap;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;

/**
 * Implementation class of TreeWalker.
 * 
 */
public class TreeWalkerImpl implements TreeWalker {

	private Node walkerRoot;
	private Node current;
	private int whatToShow;
	private NodeFilter filter;
	private NodeFilter defaultFilter;
	private boolean entitiyReferenceExpansion;
	private boolean noFilter = true;
	private HashMap<Node, Node> parentMap = new HashMap<Node, Node>();

	/**
	 * @param root
	 * @param whatToShow
	 *            the attribute determines which types of node are presented via
	 *            the TreeWalker. The values are defined in the NodeFilter
	 *            interface.
	 * @param filter
	 *            the filter used to screen nodes
	 * @param entityReferenceExpansion
	 *            the flag to determine whether the children of entity reference
	 *            nodes are visible to TreeWalker.
	 * @throws DOMException
	 */
	public TreeWalkerImpl(Node root, int whatToShow, NodeFilter filter,
			boolean entityReferenceExpansion) throws DOMException {
		if (null == root) {
			throw new DOMException(DOMException.NOT_SUPPORTED_ERR,
					"Root can't be a null."); //$NON-NLS-1$
		}
		this.walkerRoot = root;
		this.current = root;
		this.whatToShow = whatToShow;
		this.filter = filter;
		this.noFilter = (null == filter);
		this.entitiyReferenceExpansion = entityReferenceExpansion;
		this.defaultFilter = new WhatToShowNodeFilter(whatToShow);
	}

	private short eval(Node target) {
		short flag = defaultFilter.acceptNode(target);

		// If the node is skipped by whatToShow flag, a NodeFiilter will not be
		// called.
		if (noFilter || flag == NodeFilter.FILTER_SKIP) {
			return flag;
		}
		return filter.acceptNode(target);
	}

	private Node getVisibleNextSibling(Node target, Node root) {
		if (target == root) {
			return null;
		}
		Node tmpN = target.getNextSibling();
		if (null == tmpN) {
			Node tmpP = getParentNode(target);
			if (eval(tmpP) == NodeFilter.FILTER_SKIP) {
				return getVisibleNextSibling(tmpP, root);
			}
			return null;
		}
		switch (eval(tmpN)) {
		case NodeFilter.FILTER_ACCEPT:
			return tmpN;
		case NodeFilter.FILTER_SKIP:
			Node tmpC = getVisibleFirstChild(tmpN);
			if (null != tmpC) {
				return tmpC;
			}
			// case NodeFilter.FILTER_REJECT:
		default:
			return getVisibleNextSibling(tmpN, root);
		}
	}

	private Node getVisiblePreviousSibling(Node target, Node root) {
		if (target == root) {
			return null;
		}
		Node tmpN = target.getPreviousSibling();
		if (null == tmpN) {
			Node tmpP = getParentNode(target);
			if (eval(tmpP) == NodeFilter.FILTER_SKIP) {
				return getVisiblePreviousSibling(tmpP, root);
			}
			return null;
		}
		switch (eval(tmpN)) {
		case NodeFilter.FILTER_ACCEPT:
			return tmpN;
		case NodeFilter.FILTER_SKIP:
			Node tmpC = getVisibleLastChild(tmpN);
			if (null != tmpC) {
				return tmpC;
			}
			// case NodeFilter.FILTER_REJECT:
		default:
			return getVisiblePreviousSibling(tmpN, root);
		}
	}

	private Node getVisibleFirstChild(Node target) {
		if (!entitiyReferenceExpansion
				&& Node.ENTITY_REFERENCE_NODE == target.getNodeType()) {
			return null;
		}
		Node tmpN = target.getFirstChild();
		if (null == tmpN) {
			return null;
		}

		parentMap.put(tmpN, target);
		Node tmpNext = tmpN.getNextSibling();
		while (null != tmpNext) {
			parentMap.put(tmpNext, target);
			tmpNext = tmpNext.getNextSibling();
		}

		switch (eval(tmpN)) {
		case NodeFilter.FILTER_ACCEPT:
			return tmpN;
		case NodeFilter.FILTER_SKIP:
			Node tmpN2 = getVisibleFirstChild(tmpN);
			if (null != tmpN2) {
				return tmpN2;
			}
			// case NodeFilter.FILTER_REJECT:
		default:
			return getVisibleNextSibling(tmpN, target);
		}
	}

	private Node getVisibleLastChild(Node target) {
		if (!entitiyReferenceExpansion
				&& Node.ENTITY_REFERENCE_NODE == target.getNodeType()) {
			return null;
		}
		Node tmpN = target.getLastChild();
		if (null == tmpN) {
			return null;
		}

		switch (eval(tmpN)) {
		case NodeFilter.FILTER_ACCEPT:
			return tmpN;
		case NodeFilter.FILTER_SKIP:
			Node tmpN2 = getVisibleLastChild(tmpN);
			if (null != tmpN2) {
				return tmpN2;
			}
			// case NodeFilter.FILTER_REJECT:
		default:
			return getVisiblePreviousSibling(tmpN, target);
		}
	}

	private Node getVisibleParent(Node target) {
		if (target == walkerRoot) {
			return null;
		}
		Node tmpN = getParentNode(target);
		if (null == tmpN) {
			return null;
		}
		switch (eval(tmpN)) {
		case NodeFilter.FILTER_ACCEPT:
			return tmpN;
			// case NodeFilter.FILTER_SKIP:
			// case NodeFilter.FILTER_REJECT:
		default:
			return getVisibleParent(tmpN);
		}
	}

	// to avoid to fall into an infinite loop caused by broken tree structure of
	// IE DOM
	protected Node getParentNode(Node target) {
		Node tmpN = parentMap.get(target);
		if (null != tmpN) {
			return tmpN;
		}
		return target.getParentNode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.traversal.TreeWalker#firstChild()
	 */
	public Node firstChild() {
		Node result = getVisibleFirstChild(current);
		if (null != result) {
			current = result;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.traversal.TreeWalker#getCurrentNode()
	 */
	public Node getCurrentNode() {
		return current;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.traversal.TreeWalker#getExpandEntityReferences()
	 */
	public boolean getExpandEntityReferences() {
		return entitiyReferenceExpansion;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.traversal.TreeWalker#getFilter()
	 */
	public NodeFilter getFilter() {
		return filter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.traversal.TreeWalker#getRoot()
	 */
	public Node getRoot() {
		return walkerRoot;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.traversal.TreeWalker#getWhatToShow()
	 */
	public int getWhatToShow() {
		return whatToShow;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.traversal.TreeWalker#lastChild()
	 */
	public Node lastChild() {
		Node result = getVisibleLastChild(current);
		if (null != result) {
			current = result;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.traversal.TreeWalker#nextNode()
	 */
	public Node nextNode() {
		// search child
		Node tmpN = getVisibleFirstChild(current);
		if (null != tmpN) {
			current = tmpN;
			return tmpN;
		}

		// search sibling
		tmpN = getVisibleNextSibling(current, walkerRoot);
		if (null != tmpN) {
			current = tmpN;
			return tmpN;
		}

		// search parent's sibling
		Node tmpP = getVisibleParent(current);
		while (null != tmpP) {
			tmpN = getVisibleNextSibling(tmpP, walkerRoot);
			if (null != tmpN) {
				current = tmpN;
				return tmpN;
			}
			tmpP = getVisibleParent(tmpP);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.traversal.TreeWalker#nextSibling()
	 */
	public Node nextSibling() {
		Node result = getVisibleNextSibling(current, walkerRoot);
		if (null != result) {
			current = result;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.traversal.TreeWalker#parentNode()
	 */
	public Node parentNode() {
		Node result = getVisibleParent(current);
		if (null != result) {
			current = result;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.traversal.TreeWalker#previousNode()
	 */
	public Node previousNode() {
		// search previous sibling
		Node tmpN = getVisiblePreviousSibling(current, walkerRoot);
		// no sibling, search parent
		if (null == tmpN) {
			tmpN = getVisibleParent(current);
			if (null != tmpN) {
				current = tmpN;
				return tmpN;
			}
			return null;
		}

		// search last child of previous sibling
		Node tmpC = getVisibleLastChild(tmpN);
		while (null != tmpC) {
			tmpN = tmpC;
			tmpC = getVisibleLastChild(tmpN);
		}
		current = tmpN;
		return tmpN;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.traversal.TreeWalker#previousSibling()
	 */
	public Node previousSibling() {
		Node result = getVisiblePreviousSibling(current, walkerRoot);
		if (null != result) {
			current = result;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.traversal.TreeWalker#setCurrentNode(org.w3c.dom.Node)
	 */
	public void setCurrentNode(Node arg0) throws DOMException {
		if (arg0 == null) {
			throw new DOMException(DOMException.NOT_SUPPORTED_ERR,
					"Current node can't be null."); //$NON-NLS-1$
		}
		current = arg0;
	}

}