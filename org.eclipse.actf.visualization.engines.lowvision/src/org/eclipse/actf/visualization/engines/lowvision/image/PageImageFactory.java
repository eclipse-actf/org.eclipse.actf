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
package org.eclipse.actf.visualization.engines.lowvision.image;

import java.io.InputStream;

import org.eclipse.actf.visualization.engines.lowvision.LowVisionType;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.IInt2D;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.Int2D;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.PageImage;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.SimulatedPageImage;
import org.eclipse.actf.visualization.internal.engines.lowvision.io.BMPReader;

/**
 * Factory for {@link IPageImage} instance.
 */
public class PageImageFactory {

	/**
	 * Create empty {@link IPageImage} instance
	 * 
	 * @return empty {@link IPageImage} instance
	 */
	public static IPageImage createPageImage() {
		return new PageImage();
	}

	/**
	 * Simulate low vision user's view by using {@link LowVisionType}
	 * configuration and return result as new {@link IPageImage} instance
	 * 
	 * @param target
	 *            target {@link IPageImage}
	 * @param type
	 *            target {@link LowVisionType} for simulation
	 * @return simulation result as new {@link IPageImage} instance
	 * @throws ImageException
	 * @see LowVisionType
	 */
	public static IPageImage createSimulationPageImage(IPageImage target,
			LowVisionType type) throws ImageException {
		return new SimulatedPageImage(target, type);
	}

	/**
	 * Read image data (bitmap) from target file and create new
	 * {@link IPageImage} instance
	 * 
	 * @param bmpFileName
	 *            target bitmap file name
	 * @return new {@link IPageImage} instance, or null if not available
	 */
	static public IPageImage createPageImage(String bmpFileName) {
		return createPageImage(bmpFileName, false);
	}

	/**
	 * Read image data (bitmap) from target file and create new
	 * {@link IPageImage} instance. If removeScrollBar flag is true, cut scroll
	 * bar area from image
	 * 
	 * @param bmpFileName
	 *            target bitmap file name
	 * @param removeScrollBar
	 *            if true, cut scroll bar area from image
	 * @return new {@link IPageImage} instance, or null if not available
	 */
	static public IPageImage createPageImage(String bmpFileName,
			boolean removeScrollBar) {

		IInt2D int2dWhole = new Int2D(0, 0);
		try {
			int2dWhole = (BMPReader.readInt2D(bmpFileName));
			PageImage result = new PageImage(int2dWhole, removeScrollBar);
			int2dWhole = null;
			return (result);
		} catch (Exception e) {
			// TODO
		}

		return (null);
	}

	/**
	 * Read image data (bitmap) from {@link InputStream} and create new
	 * {@link IPageImage} instance
	 * 
	 * @param is
	 *            target {@link InputStream} of image data
	 * @return new {@link IPageImage} instance, or null if not available
	 */
	static public IPageImage createPageImage(InputStream is) {
		return createPageImage(is, false);
	}

	/**
	 * Read image data (bitmap) from {@link InputStream} and create new
	 * {@link IPageImage} instance. If removeScrollBar flag is true, cut scroll
	 * bar area from image
	 * 
	 * @param is
	 *            target {@link InputStream} of image data
	 * @param removeScrollBar
	 *            if true, cut scroll bar area from image
	 * @return new {@link IPageImage} instance, or null if not available
	 */
	static public IPageImage createPageImage(InputStream is,
			boolean removeScrollBar) {

		IInt2D int2dWhole = new Int2D(0, 0);
		try {
			int2dWhole = BMPReader.readInt2D(is);
			PageImage result = new PageImage(int2dWhole, removeScrollBar);
			int2dWhole = null;
			return (result);
		} catch (Exception e) {
			// TODO
		}

		return (null);
	}

	/**
	 * Join multiple {@link IPageImage} into new {@link IPageImage} instance
	 * 
	 * @param targets
	 *            array of target {@link IPageImage}
	 * @return new {@link IPageImage} instance
	 */
	public static synchronized IPageImage joinPageImages(IPageImage[] targets) {
		Int2D int2dWhole = new Int2D(0, 0);
		int iWholeWidth = 0;
		int iWholeHeight = 0;

		if (targets != null) {
			for (int i = 0; i < targets.length; i++) {
				if (iWholeWidth < targets[i].getWidth())
					iWholeWidth = targets[i].getWidth();
				iWholeHeight += targets[i].getHeight();
			}

			int2dWhole = new Int2D(iWholeWidth, iWholeHeight);

			int iDrawY = 0;
			for (int i = 0; i < targets.length; i++) {
				//TODO
				IInt2D tmpInt2d = ((PageImage)targets[i]).getInt2D();
				for (int k = 0; k < tmpInt2d.getHeight(); k++) {
					System.arraycopy(tmpInt2d.getData()[k], 0, int2dWhole
							.getData()[iDrawY + k], 0, tmpInt2d.getWidth());
				}
				iDrawY += tmpInt2d.getHeight();
			}

		}
		return (new PageImage(int2dWhole));
	}

}
