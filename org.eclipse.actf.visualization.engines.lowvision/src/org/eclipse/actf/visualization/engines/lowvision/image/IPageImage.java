/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Junji MAEDA - initial API and implementation
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.engines.lowvision.image;

import java.awt.image.BufferedImage;
import java.util.List;

import org.eclipse.actf.model.ui.ImagePositionInfo;
import org.eclipse.actf.visualization.engines.lowvision.LowVisionIOException;
import org.eclipse.actf.visualization.engines.lowvision.LowVisionType;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.LowVisionProblemException;

/**
 * Interface to store image data used in low vision simulation engine
 */
public interface IPageImage {

	/**
	 * Get width of image
	 * 
	 * @return width
	 */
	public int getWidth();

	/**
	 * Get height of image
	 * 
	 * @return image
	 */
	public int getHeight();

	/**
	 * Extract character position in the image
	 * 
	 * @throws ImageException
	 */
	public void extractCharacters() throws ImageException;

	/**
	 * Get interior image position (e.g., <img> position in HTML page image)
	 * 
	 * @return array of {@link ImagePositionInfo}
	 */
	public ImagePositionInfo[] getInteriorImagePosition();

	/**
	 * Set interior image position (e.g., <img> position in HTML page image)
	 * 
	 * @param infoArray
	 *            array of {@link ImagePositionInfo}
	 */
	public void setInteriorImagePosition(ImagePositionInfo[] infoArray);

	/**
	 * Check existence of interior image position information
	 * 
	 * @return true if interior image position is specified
	 */
	public boolean hasInteriorImageArraySet();

	/**
	 * Check accessibility issues inside the image.
	 * 
	 * @param type
	 *            target low vision type
	 * @param urlS
	 *            target's URL
	 * @param frameId
	 *            target's frame ID
	 * @return list of accessibility issues ({@link IProblemItem})
	 * @throws ImageException
	 * @throws LowVisionProblemException
	 */
	public List<IProblemItem> checkCharacters(LowVisionType type, String urlS,
			int frameId) throws ImageException, LowVisionProblemException;

	/**
	 * Convert image data into {@link BufferedImage}
	 * 
	 * @return image as new {@link BufferedImage}
	 */
	public BufferedImage getBufferedImage();

	/**
	 * Write image to File as Bitmap
	 * 
	 * @param _fileName
	 *            target file path
	 * @throws LowVisionIOException
	 */
	public abstract void writeToBMPFile(String _fileName)
			throws LowVisionIOException;

	/**
	 * Write image to File as Bitmap
	 * 
	 * @param _fileName
	 *            target file path
	 * @param _bitCount
	 *            depth of color (16 or 24)
	 * @throws LowVisionIOException
	 */
	public abstract void writeToBMPFile(String _fileName, int _bitCount)
			throws LowVisionIOException;

}