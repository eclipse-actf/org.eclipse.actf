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
package org.eclipse.actf.visualization.internal.engines.lowvision.image;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.io.PrintStream;
import java.io.PrintWriter;

import org.eclipse.actf.visualization.engines.lowvision.image.ImageException;

public class ImageUtil {
	public static Int2D bufferedImageToInt2D(BufferedImage _bi)
			throws ImageException {
		if (_bi == null) {
			throw new ImageException("Input BufferedImage is null."); //$NON-NLS-1$
		}
		int width = _bi.getWidth();
		int height = _bi.getHeight();
		Int2D i2d = new Int2D(width, height);
		WritableRaster srcRaster = _bi.copyData(null);

		DataBufferInt srcBufInt = (DataBufferInt) (srcRaster.getDataBuffer());
		int[] srcArray = srcBufInt.getData();
		int k = 0;
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				i2d.getData()[j][i] = srcArray[k];
				k++;
			}
		}
		return (i2d);
	}

	public static BufferedImage int2DToBufferedImage(IInt2D _i2d) {
		return (int2DArrayToBufferedImage(_i2d.getData(), _i2d.getWidth(), _i2d.getHeight()));
	}

	public static int[][] bufferedImageToInt2DArray(BufferedImage _bi,
			int _width, int _height) {
		int[][] pixel = new int[_height][_width];
		WritableRaster srcRaster = _bi.copyData(null);
		DataBufferInt srcBufInt = (DataBufferInt) (srcRaster.getDataBuffer());
		int[] srcArray = srcBufInt.getData();
		int k = 0;
		for (int j = 0; j < _height; j++) {
			for (int i = 0; i < _width; i++) {
				pixel[j][i] = srcArray[k];
				k++;
			}
		}
		return (pixel);
	}

	public static BufferedImage int2DArrayToBufferedImage(int[][] _pixel,
			int _width, int _height) {
		BufferedImage destBufferedImage = new BufferedImage(_width, _height,
				BufferedImage.TYPE_INT_RGB);
		WritableRaster destRaster = destBufferedImage.copyData(null);
		DataBufferInt destBufInt = (DataBufferInt) (destRaster.getDataBuffer());
		int[] destArray = destBufInt.getData();
		int k = 0;
		for (int j = 0; j < _height; j++) {
			for (int i = 0; i < _width; i++) {
				destArray[k] = _pixel[j][i];
				k++;
			}
		}
		destBufferedImage.setData(destRaster);
		return (destBufferedImage);
	}

	public static int[][] copyInt2DArray(int[][] _src, int _width, int _height) {
		int[][] destArray2D = new int[_height][_width];
		for (int j = 0; j < _height; j++) {
			for (int i = 0; i < _width; i++) {
				destArray2D[j][i] = _src[j][i];
			}
		}
		return (destArray2D);
	}

	public static void dumpInt2DArray(PrintStream _ps, int[][] _data,
			int _width, int _height) {
		PrintWriter pw = new PrintWriter(_ps, false);
		dumpInt2DArray(pw, _data, _width, _height);
	}

	@SuppressWarnings("nls")
	public static void dumpInt2DArray(PrintWriter _pw, int[][] _data,
			int _width, int _height) {
		_pw.println("-------");
		_pw.println("dumping int[][]");
		_pw.println("width = " + _width + ", height = " + _height);
		_pw.println("data:");
		for (int j = 0; j < _height; j++) {
			for (int i = 0; i < _width; i++) {
				_pw.print("" + _data[j][i]);
			}
			_pw.println("");
		}
		_pw.println("-------");
		_pw.flush();
	}
}
