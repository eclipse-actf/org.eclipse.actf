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
package org.eclipse.actf.visualization.internal.engines.lowvision.image;

import java.awt.image.BufferedImage;

import org.eclipse.actf.visualization.engines.lowvision.LowVisionIOException;
import org.eclipse.actf.visualization.engines.lowvision.image.ImageException;

/**
 * Interface to store image data as 2D integer array. <br>
 * data[y][x] = BufferdImage.TYPE_INT_RGB (null, R, G, B)<br>
 * data[0][0] is top left
 */
public interface IInt2D {

	/**
	 * Get width of image
	 * 
	 * @return width
	 */
	public abstract int getWidth();

	/**
	 * Get height of image
	 * 
	 * @return height
	 */
	public abstract int getHeight();

	/**
	 * Convert image data into {@link BufferedImage}
	 * 
	 * @return image as {@link BufferedImage}
	 */
	public abstract BufferedImage toBufferedImage();

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

	/**
	 * Fill image data with specified color.<br>
	 * (BufferdImage.TYPE_INT_RGB (null, R, G, B))
	 * 
	 * @param _color
	 * @see BufferedImage
	 */
	public abstract void fill(int _color);

	/**
	 * Cut margin pixels and return as new {@link IInt2D}
	 * 
	 * @param _m
	 *            target margin
	 * @return new {@link IInt2D}
	 * @throws ImageException
	 */
	public abstract IInt2D cutMargin(int _m) throws ImageException;

	/**
	 * Get image data as 2D integer array. Color data is stored as integer
	 * (BufferdImage.TYPE_INT_RGB (null, R, G, B))
	 * 
	 * @return image data
	 */
	public int[][] getData();

	/**
	 * Copy image data and return as new {@link IInt2D}
	 * @return deep copied {@link IInt2D}
	 */
	public IInt2D deepCopy();

}