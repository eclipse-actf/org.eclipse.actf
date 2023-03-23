/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and Others
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

import org.w3c.dom.Node;

/**
 * The interface to hold audio packet information.
 */
public interface IPacket {

	/**
	 * Returns the text of this audio packet.
	 * 
	 * @return the text of this audio packet
	 */
	public abstract String getText();

	/**
	 * Returns the current context of this audio packet
	 * 
	 * @return the current context
	 * @see IContext
	 */
	public abstract IContext getContext();

	/**
	 * Returns the corresponding node of this audio packet
	 * 
	 * @return corresponding node
	 */
	public abstract Node getNode();

}