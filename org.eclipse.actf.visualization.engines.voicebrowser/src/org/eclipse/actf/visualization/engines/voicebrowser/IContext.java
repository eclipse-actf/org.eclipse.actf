/*******************************************************************************
 * Copyright (c) 2003, 2023 IBM Corporation and Others
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

/**
 * Context in the voice browser engine.
 */
public interface IContext {

	/**
	 * Check if this context is inside form.
	 * 
	 * @return true if current context is inside form
	 */
	public abstract boolean isInsideForm();

	/**
	 * Check if need to visit child.
	 * 
	 * @return true if need to visit child
	 */
	public abstract boolean isGoChild();

	/**
	 * Check if this context is a line delimiter.
	 * 
	 * @return true if this context is a line delimiter
	 */
	public abstract boolean isLineDelimiter();

	/**
	 * Check if this context is a link tag.
	 * 
	 * @return true if this context is a link tag
	 */
	public abstract boolean isLinkTag();

	/**
	 * Check if this context is inside anchor.
	 * 
	 * @return true if this context is inside anchor
	 */
	public abstract boolean isInsideAnchor();

	/**
	 * Returns the startSelect.
	 * 
	 * @return boolean
	 */
	public abstract boolean isStartSelect();

	/**
	 * Returns the stringOutput.
	 * 
	 * @return boolean
	 */
	public abstract boolean isStringOutput();

	/**
	 * Returns the href attribute value of the context if this context is inside
	 * anchor.
	 * 
	 * @return href attribute value; null if href attribute does not exist
	 */
	public abstract String getHref();

}