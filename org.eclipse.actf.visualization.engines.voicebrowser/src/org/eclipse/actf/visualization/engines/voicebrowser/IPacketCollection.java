/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Masahide WASHIZAWA - initial API and implementation
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.engines.voicebrowser;

import java.util.List;

import org.w3c.dom.Node;

/**
 * The interface to hold a group of audio packets.
 * 
 * @see IPacket
 * @see IVoiceBrowserController
 */
public interface IPacketCollection extends List<IPacket> {

	/**
	 * Check if the target packet is line delimiter.
	 * 
	 * @param i
	 *            index of target packet
	 * @return true if target is line delimiter
	 */
	public abstract boolean isLineDelimiter(int i);

	/**
	 * Check if the target packet is link tag.
	 * 
	 * @param i
	 *            index of target packet
	 * @return true if target is link tag
	 */
	public abstract boolean isLinkTag(int i);

	/**
	 * Check if the target packet is inside form.
	 * 
	 * @param i
	 *            index of target packet
	 * @return true if target is inside form
	 */
	public abstract boolean isInsideForm(int i);

	/**
	 * Check if the target packet is inside anchor.
	 * 
	 * @param i
	 *            index of target packet
	 * @return true if target is inside anchor
	 */
	public abstract boolean isInsideAnchor(int i);

	/**
	 * Get first Node from collection
	 * 
	 * @return first Node
	 */
	public abstract Node getFirstNode();

	/**
	 * Get last Node from collection
	 * 
	 * @return last Node
	 */
	public abstract Node getLastNode();

	/**
	 * Get index of top Node.
	 * 
	 * @return index of top Node
	 */
	public abstract int getTopNodePosition();

	/**
	 * Get index of bottom Node.
	 * 
	 * @return index of bottom node
	 */
	public abstract int getBottomNodePosition();

	/**
	 * Get index of target Node.
	 * 
	 * @param node
	 *            target node
	 * @return index of the target node
	 */
	public abstract int getNodePosition(Node node);

}