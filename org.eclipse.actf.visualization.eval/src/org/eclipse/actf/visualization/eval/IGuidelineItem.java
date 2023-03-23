/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.eval;

/**
 * Interface to hold guideline item information
 */
public interface IGuidelineItem {

	/**
	 * @return name of guideline
	 */
	public abstract String getGuidelineName();

	/**
	 * @return level of guideline item (e.g., P1, P2, etc.)
	 */
	public abstract String getLevel();

	/**
	 * @return ID of guideline item (e.g., 1.1, 2.2, etc.)
	 */
	public abstract String getId();

	/**
	 * @return URL of public introduction page of guideline item
	 */
	public abstract String getUrl();

	/**
	 * @return true if this item is enabled
	 */
	public abstract boolean isEnabled();
	

	/**
	 * Set this item is enabled or not
	 * 
	 * @param isEnabled
	 * 
	 */
	public abstract void setEnabled(boolean isEnabled);

	
	/**
	 * Set level of guideline item (e.g., P1, P2, etc.)
	 * 
	 * @param level
	 *            target level
	 * 
	 * @deprecated
	 */
	public abstract void setLevel(String level);

	/**
	 * Set ID of guideline item (e.g., 1.1, 2.2, etc.)
	 * 
	 * @param id
	 *            target id
	 * 
	 * @deprecated
	 */
	public abstract void setId(String id);

	/**
	 * Set URL of public introduction page of guideline item
	 * 
	 * @param url
	 *            target URL
	 * @deprecated
	 */
	public abstract void setUrl(String url);

}
