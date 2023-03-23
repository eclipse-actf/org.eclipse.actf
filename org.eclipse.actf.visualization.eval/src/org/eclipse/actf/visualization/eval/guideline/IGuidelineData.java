/*******************************************************************************
 * Copyright (c) 2005, 2010 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.eval.guideline;

import org.eclipse.actf.visualization.eval.IGuidelineItem;
import org.eclipse.actf.visualization.eval.ITechniquesItem;

/**
 * Interface for guideline data
 * 
 * @see GuidelineHolder
 */
public interface IGuidelineData {

	/**
	 * Get guideline item information
	 * 
	 * @param id
	 *            target ID of guideline item
	 * @return guideline item ({@link IGuidelineItem}), or null if not
	 *         available
	 */
	public abstract IGuidelineItem getGuidelineItem(String id);

	/**
	 * Get Techniques item information
	 * 
	 * @param id
	 *            target ID of Techniques item
	 * @return guideline item ({@link ITechniquesItem}), or null if not
	 *         available
	 */
	public abstract ITechniquesItem getTequniquesItem(String id);
	
	/**
	 * Get guideline name
	 * 
	 * @return guideline name
	 */
	public abstract String getGuidelineName();

	/**
	 * Get levels of guideline (e.g., P1, P2, etc.)
	 * 
	 * @return levels of guideline
	 */
	public abstract String[] getLevels();

	/**
	 * Check if guideline has levels
	 * 
	 * @return true if guideline has levels
	 */
	public abstract boolean hasLevel();

	/**
	 * Get corresponding content MIME types
	 * 
	 * @return corresponding content MIME types
	 */
	public abstract String[] getMIMEtypes();

	/**
	 * Check if this guideline is enabled by user
	 * 
	 * @return true if this guideline is enabled
	 */
	public abstract boolean isEnabled();

	/**
	 * Check if current active content's MIME type is supported by this
	 * guideline
	 * 
	 * @return true if supported
	 */
	public abstract boolean isMatched();

	/**
	 * Get guideline information for specified level
	 * 
	 * @param levelStr
	 *            target level
	 * @return guideline information for specified level, or null if not
	 *         available
	 */
	public abstract IGuidelineData getSubLevelData(String levelStr);

	/**
	 * Get level of this guideline item
	 * 
	 * @return level as String
	 */
	public abstract String getLevelStr();

	/**
	 * Check if target MIME type is supported by this guideline item
	 * 
	 * @param mimetype
	 *            target MIME type
	 * @return true if supported
	 */
	public abstract boolean isTargetMIMEtype(String mimetype);

	/**
	 * Get category of this guideline item
	 * 
	 * @return category
	 */
	public abstract String getCategory();

	/**
	 * Get description about this guideline item
	 * 
	 * @return description
	 */
	public abstract String getDescription();

}