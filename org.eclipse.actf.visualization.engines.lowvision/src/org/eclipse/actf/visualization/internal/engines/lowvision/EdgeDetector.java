/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Junji MAEDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.engines.lowvision;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import org.eclipse.actf.visualization.engines.lowvision.LowVisionException;

public class EdgeDetector {
	public static final short METHOD_LAPLACIAN4_MINUS = 0;

	public static final short METHOD_LAPLACIAN4_PLUS = 1;

	public static final short METHOD_LAPLACIAN8_MINUS = 2;

	public static final short METHOD_LAPLACIAN8_PLUS = 3;

	public static final short METHOD_DEFAULT = METHOD_LAPLACIAN4_MINUS;

	private static final float[] LAPLACIAN4_3X3_MINUS = { 0.0f, 1.0f, 0.0f,
			1.0f, -4.0f, 1.0f, 0.0f, 1.0f, 0.0f };

	private static final float[] LAPLACIAN4_3X3_PLUS = { 0.0f, -1.0f, 0.0f,
			-1.0f, 4.0f, -1.0f, 0.0f, -1.0f, 0.0f };

	private static final float[] LAPLACIAN8_3X3_MINUS = { 1.0f, 1.0f, 1.0f,
			1.0f, -8.0f, 1.0f, 1.0f, 1.0f, 1.0f };

	private static final float[] LAPLACIAN8_3X3_PLUS = { -1.0f, -1.0f, -1.0f,
			-1.0f, 8.0f, -1.0f, -1.0f, -1.0f, -1.0f };

	private BufferedImage srcBufferedImage = null;

	private BufferedImage destBufferedImage = null;

//	private int width = 0;
//
//	private int height = 0;

	public EdgeDetector(BufferedImage _src) throws LowVisionException {
		this(_src, METHOD_DEFAULT);
	}

	public EdgeDetector(BufferedImage _src, short _method)
			throws LowVisionException {
		srcBufferedImage = _src;
//		width = srcBufferedImage.getWidth();
//		height = srcBufferedImage.getHeight();

		if (_method == METHOD_LAPLACIAN4_MINUS) {
			destBufferedImage = convolve(srcBufferedImage, 3, 3,
					LAPLACIAN4_3X3_MINUS);
		} else if (_method == METHOD_LAPLACIAN4_PLUS) {
			destBufferedImage = convolve(srcBufferedImage, 3, 3,
					LAPLACIAN4_3X3_PLUS);
		} else if (_method == METHOD_LAPLACIAN8_MINUS) {
			destBufferedImage = convolve(srcBufferedImage, 3, 3,
					LAPLACIAN8_3X3_MINUS);
		} else if (_method == METHOD_LAPLACIAN8_PLUS) {
			destBufferedImage = convolve(srcBufferedImage, 3, 3,
					LAPLACIAN8_3X3_PLUS);
		} else {
			throw new LowVisionException("Unknown method: " + _method); //$NON-NLS-1$
		}
	}

	private BufferedImage convolve(BufferedImage _srcImage, int _kernelWidth,
			int _kernelHeight, float[] _kernelData) {
		BufferedImage tmpImage = new BufferedImage(_srcImage.getWidth(),
				_srcImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		Kernel ker = new Kernel(_kernelWidth, _kernelHeight, _kernelData);
		ConvolveOp cop = new ConvolveOp(ker, ConvolveOp.EDGE_NO_OP, null);
		return (cop.filter(_srcImage, tmpImage));
	}

	public BufferedImage writeToImage() {
		// TBD
		return (destBufferedImage);
	}
}
