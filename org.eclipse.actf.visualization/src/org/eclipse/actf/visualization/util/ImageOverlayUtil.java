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

package org.eclipse.actf.visualization.util;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.widgets.Display;

/**
 * Utility class to create overlay image
 */
public class ImageOverlayUtil {

	/**
	 * 24 bit direct palette used for overlay. Pixels will divide into 3
	 * portions, red in the lowest 8 bits, green in the central 8 bits and blue
	 * in the highest 8 bits. For example, pixel value 0xFF is red, 0xFF00 is
	 * green, 0xFF0000 is blue.
	 */
	public static final PaletteData PALETTE = new PaletteData(0xFF, 0xFF00,
			0xFF0000);

	/**
	 * Overlay image onto base image.
	 * 
	 * @param base
	 *            the target image to add overlay
	 * @param overlay
	 *            the overlay image. alpha value of the Image should be set in
	 *            advance.
	 * @return whether overlay succeeded or not
	 */
	public static boolean overlay(Image base, Image overlay) {
		try {
			GC gc = new GC(base);
			if (null == overlay || overlay.isDisposed()) {
				return false;
			}
			gc.drawImage(overlay, 0, 0);
			gc.dispose();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Overlay pixels onto base image. An alpha value of 255 is full weight, and
	 * 0 is no weight (transparent).
	 * 
	 * @param base
	 *            the target image to add overlay
	 * @param pixels
	 *            the pixel data array of the overlay image in [y][x] format
	 * @param alpha
	 *            the global alpha value to be used for every pixel
	 * @return whether overlay succeeded or not
	 * 
	 * @see PALETTE to understand how to specify pixel value
	 */
	public static boolean overlay(Image base, int[][] pixels, int alpha) {
		try {
			int width = pixels[0].length;
			int height = pixels.length;
			ImageData overlayData = new ImageData(width, height, 24, PALETTE);
			overlayData.alpha = alpha;
			for (int y = 0; y < height; y++) {
				overlayData.setPixels(0, y, width, pixels[y], 0);
			}
			Image overlayImage = new Image(Display.getDefault(), overlayData);
			boolean result = overlay(base, overlayImage);
			overlayImage.dispose();
			return result;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Overlay pixels onto base image. An alpha value of 255 is full weight, and
	 * 0 is no weight (transparent).
	 * 
	 * @param base
	 *            the target image to add overlay
	 * @param width
	 *            the width of the overlay image
	 * @param pixels
	 *            the pixel data array of the overlay image
	 * @param alpha
	 *            the global alpha value to be used for every pixel
	 * @return whether overlay succeeded or not
	 * @see PALETTE to understand how to specify pixel value
	 */
	public static boolean overlay(Image base, int width, int[] pixels, int alpha) {
		try {
			int height = pixels.length / width;
			ImageData overlayData = new ImageData(width, height, 24, PALETTE);
			overlayData.alpha = alpha;
			for (int y = 0; y < height; y++) {
				overlayData.setPixels(0, y, width, pixels, width * y);
			}
			Image overlayImage = new Image(Display.getDefault(), overlayData);
			boolean result = overlay(base, overlayImage);
			overlayImage.dispose();
			return result;
		} catch (Exception e) {
			return false;
		}
	}
}
