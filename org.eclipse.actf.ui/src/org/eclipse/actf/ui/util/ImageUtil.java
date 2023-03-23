/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.ui.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;

/**
 * Utility class to save {@link Image} into file.
 */
public class ImageUtil {

	/**
	 * Save target {@link Image} to file.
	 * 
	 * @param image
	 *            target Image to save
	 * @param savePath
	 *            target path in String format
	 * @param format
	 *            this parameter can have one of the format type defined at
	 *            {@link SWT}
	 * 
	 * @return true if succeeded
	 * @see ImageLoader#save(java.io.OutputStream, int)
	 */
	public static synchronized boolean saveImageToFile(Image image,
			String savePath, int format) {
		ImageLoader loader = new ImageLoader();
		if (image != null && image.getImageData() != null) {
			loader.data = new ImageData[] { image.getImageData() };
			try {
				loader.save(savePath, format);
			} catch (Exception e) {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}
}
