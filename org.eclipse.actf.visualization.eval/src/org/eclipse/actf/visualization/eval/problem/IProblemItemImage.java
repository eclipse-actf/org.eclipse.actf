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
package org.eclipse.actf.visualization.eval.problem;

import org.eclipse.actf.visualization.ui.IPositionSize;
import org.eclipse.swt.graphics.Image;

/**
 * Interface to store image related information in addition to
 * {@link IProblemItem}
 */
public interface IProblemItemImage extends IProblemItem, IPositionSize,
		ILowvisionProblemSubtype {

	public static final int ICON_COLOR = 1;

	public static final int ICON_BLUR = 3;

	/**
	 * @return background
	 */
	public abstract String getBackground();

	/**
	 * @return foreground
	 */
	public abstract String getForeground();

	/**
	 * @return frame ID
	 */
	public abstract int getFrameId();

	/**
	 * @return frame offset
	 */
	public abstract int getFrameOffset();

	/**
	 * @return frame URL
	 */
	public abstract String getFrameUrl();

	/**
	 * @return ID of problem icon
	 */
	public abstract int getIconId();

	/**
	 * @return problem icon as {@link Image}
	 */
	public abstract Image getIconImage();

	/**
	 * @return tooltip text for problem icon
	 */
	public abstract String getIconTooltip();

	/**
	 * @return severity value (from 0 to 100)
	 */
	public abstract int getSeverityLV();


	/**
	 * Set area of the problem
	 * 
	 * @param area
	 *            target area size
	 */
	public void setArea(int area);

	/**
	 * Set background
	 * 
	 * @param backgroundS
	 *            target background
	 */
	public void setBackground(String backgroundS);

	/**
	 * Set foreground
	 * 
	 * @param foregroundS
	 *            target foreground
	 */
	public void setForeground(String foregroundS);

	/**
	 * Set frame ID
	 * 
	 * @param frameId
	 *            target frame ID
	 */
	public void setFrameId(int frameId);

	/**
	 * Set frame offset
	 * 
	 * @param frameOffset
	 *            target frame offset
	 */
	public void setFrameOffset(int frameOffset);

	/**
	 * Set target frane URL
	 * 
	 * @param frameUrlS
	 *            target frame URL
	 */
	public void setFrameUrl(String frameUrlS);

	/**
	 * Set severity value (from 0 to 100)
	 * 
	 * @param severityLV
	 */
	public void setSeverityLV(int severityLV);

}