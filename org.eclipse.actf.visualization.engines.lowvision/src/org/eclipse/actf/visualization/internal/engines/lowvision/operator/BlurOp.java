/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Junji MAEDA - initial API and implementation
 *    Daisuke SATO - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.engines.lowvision.operator;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import org.eclipse.actf.visualization.engines.lowvision.LowVisionType;

public class BlurOp implements ILowVisionOperator {
	private ConvolveOp cop = null;

	public BlurOp(LowVisionType _lvType) {
		double pixel = _lvType.getEyesightPixel();

		int cPixel = (int) (Math.ceil(pixel));
		int fPixel = (int) (Math.floor(pixel));

		// ---square---

		int arrayWidth = 2 * (cPixel) + 1;
		double[][] blurArray = new double[arrayWidth][arrayWidth];
		if (cPixel == fPixel) {
			double area = arrayWidth * arrayWidth;
			for (int j = 0; j < arrayWidth; j++) {
				for (int i = 0; i < arrayWidth; i++) {
					blurArray[j][i] = 1.0 / area;
				}
			}
		} else if (cPixel == fPixel + 1) {
			double dicimal = pixel - fPixel;
			double area = (2.0 * pixel + 1.0) * (2.0 * pixel + 1.0);

			double centerValue = 1.0 / area;
			for (int j = 1; j < arrayWidth - 1; j++) {
				for (int i = 1; i < arrayWidth - 1; i++) {
					blurArray[j][i] = centerValue;
				}
			}

			double edgeValue = dicimal / area;
			for (int j = 1; j < arrayWidth - 1; j++) {
				blurArray[j][0] = edgeValue;
				blurArray[j][arrayWidth - 1] = edgeValue;
				blurArray[0][j] = edgeValue;
				blurArray[arrayWidth - 1][j] = edgeValue;
			}

			double cornerValue = dicimal * dicimal / area;
			blurArray[0][0] = cornerValue;
			blurArray[0][arrayWidth - 1] = cornerValue;
			blurArray[arrayWidth - 1][0] = cornerValue;
			blurArray[arrayWidth - 1][arrayWidth - 1] = cornerValue;

			// ---square---

			// use octagon
			double temp = blurArray[0][0] * 4;
			double cornerHalf = 0;// blurArray[0][0] / 2.0;
			blurArray[0][0] = cornerHalf;
			blurArray[0][arrayWidth - 1] = cornerHalf;
			blurArray[arrayWidth - 1][0] = cornerHalf;
			blurArray[arrayWidth - 1][arrayWidth - 1] = cornerHalf;
			blurArray[arrayWidth / 2][arrayWidth / 2] += temp;
			// (cornerHalf * 4.0);
		} else {
			// assert
		}

		float[] element = new float[arrayWidth * arrayWidth];
		int k = 0;
		for (int j = 0; j < arrayWidth; j++) {
			for (int i = 0; i < arrayWidth; i++) {
				element[k] = (float) (blurArray[j][i]);
				k++;
			}
		}
		Kernel kernel = new Kernel(arrayWidth, arrayWidth, element);
		cop = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);

	}

	public BufferedImage filter(BufferedImage _src, BufferedImage _dest) {
		return newFilter(_src, _dest); // by daisuke
		// return (cop.filter(_src, _dest)); // by maeda
	}

	private BufferedImage newFilter(BufferedImage _src, BufferedImage _dest) {
		if (_dest == null) {
			_dest = new BufferedImage(_src.getWidth(), _src.getHeight(), _src
					.getType());
		}

		Kernel karnel = cop.getKernel();
		int w = karnel.getWidth();
		int h = karnel.getHeight();
		float[] d = new float[w * h];
		karnel.getKernelData(d);

		int width = _src.getWidth();
		int height = _src.getHeight();

		double N = 1.005;

		double[] pow = new double[256];
		for (int i = 0; i < pow.length; i++) {
			pow[i] = Math.pow(N, i);
		}
		double logN = Math.log(N);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				double dr = 0, dg = 0, db = 0;

				for (int xx = 0; xx < w; xx++) {
					for (int yy = 0; yy < h; yy++) {
						int xxx = x + xx - w / 2;
						int yyy = y + yy - h / 2;
						xxx = xxx > 0 ? xxx : 0;
						xxx = xxx >= width ? width - 1 : xxx;
						yyy = yyy > 0 ? yyy : 0;
						yyy = yyy >= height ? height - 1 : yyy;

						int rgb = _src.getRGB(xxx, yyy);
						int ir = (rgb & 0xFF0000) >> 16;
						int ig = (rgb & 0x00FF00) >> 8;
						int ib = (rgb & 0x0000FF);

						dr += pow[ir] * d[xx * w + yy];
						dg += pow[ig] * d[xx * w + yy];
						db += pow[ib] * d[xx * w + yy];
					}
				}
				int ir = (int) (Math.log(dr) / logN);
				int ig = (int) (Math.log(dg) / logN);
				int ib = (int) (Math.log(db) / logN);

				ir = ir > 255 ? 255 : ir;
				ig = ig > 255 ? 255 : ig;
				ib = ib > 255 ? 255 : ib;

				_dest.setRGB(x, y, (ir << 16) + (ig << 8) + ib);
			}
		}

		return _dest;
	}

	public WritableRaster filter(Raster _src, WritableRaster _dest) {
		return (cop.filter(_src, _dest));
	}
}
