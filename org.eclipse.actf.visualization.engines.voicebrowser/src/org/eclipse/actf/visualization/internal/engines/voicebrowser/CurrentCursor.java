/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Masahide WASHIZAWA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.engines.voicebrowser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CurrentCursor {
	private Node topNode = null;

	private Node ownerDocumentNode = null;

	private PacketCollection pc;

	private int curPos = 0;

	/**
	 * Constructor for CurrentCursor.
	 */
	public CurrentCursor() {
		super();
	}

	/**
	 * Returns the curPos.
	 * 
	 * @return int
	 */
	public int getCurPos() {
		return curPos;
	}

	/**
	 * Returns the pc.
	 * 
	 * @return PacketCollection
	 */
	public PacketCollection getPc() {
		return pc;
	}

	/**
	 * Sets the curPos.
	 * 
	 * @param curPos
	 *            The curPos to set
	 */
	public void setCurPos(int curPos) {
		this.curPos = curPos;
	}

	/**
	 * Sets the pc.
	 * 
	 * @param pc
	 *            The pc to set
	 */
	public void setPc(PacketCollection pc) {
		this.pc = pc;
	}

	/**
	 * Method getNode.
	 * 
	 * @param pos
	 * @return Node
	 */
	public Node getNode(int pos) {
		if (pos < pc.size()) {
			return pc.get(pos).getNode();
		} else {
			return null;
		}
	}

	/**
	 * Returns the topNode.
	 * 
	 * @return Node
	 */
	public Node getTopNode() {
		return this.topNode;
	}

	/**
	 * Returns the ownerDocumentNode.
	 * 
	 * @return Node
	 */
	public Node getOwnerDocumentNode() {
		return ownerDocumentNode;
	}

	/**
	 * Sets the ownerDocumentNode.
	 * 
	 * @param ownerDocumentNode
	 *            The ownerDocumentNode to set
	 */
	public void setOwnerDocumentNode(Node ownerDocumentNode) {
		this.ownerDocumentNode = ownerDocumentNode;

		if (this.ownerDocumentNode != null) {
			NodeList children = ((Document) this.ownerDocumentNode)
					.getElementsByTagName("body"); //$NON-NLS-1$
			if (children.getLength() > 0) {
				this.topNode = children.item(0);
			}
		}
	}

}
