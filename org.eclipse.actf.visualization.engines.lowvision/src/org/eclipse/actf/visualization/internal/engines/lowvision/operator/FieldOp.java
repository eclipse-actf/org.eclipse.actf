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
package org.eclipse.actf.visualization.internal.engines.lowvision.operator;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;

import org.eclipse.actf.visualization.engines.lowvision.LowVisionException;

/*
 * field of view
 * 
 */
public class FieldOp implements ILowVisionOperator {
	public static final short TYPE_CIRCLE_INT = 1; // number of pixels

	public static final short TYPE_CIRCLE_FLOAT = 2; // ratio of circle to
	// image

	public static final float CIRCLE_WIDTH = 20.0f; // blur width

	BufferedImage filterImage;

	short type = 0;

	int diameterInt;

	float diameterFloat;

	public FieldOp(short _type) {
		type = _type;
	}

	public FieldOp(short _type, int _param1) throws LowVisionException {
		this(_type);
		if (type == TYPE_CIRCLE_INT) {
			diameterInt = _param1;
		} else {
			throw new LowVisionException(
					"The type is unknown, or the type and the parameter(s) do not match: type = " //$NON-NLS-1$
							+ _type + ", param = " + _param1); //$NON-NLS-1$
		}
	}

	public FieldOp(short _type, float _param1) throws LowVisionException {
		this(_type);
		if (type == TYPE_CIRCLE_FLOAT) {
			diameterFloat = _param1;
		} else {
			throw new LowVisionException(
					"The type is unknown, or the type and the parameter(s) do not match: type = " //$NON-NLS-1$
							+ _type + ", param = " + _param1); //$NON-NLS-1$
		}
	}

	public BufferedImage filter(BufferedImage _src, BufferedImage _dest)
			throws LowVisionException {
		int width = _src.getWidth();
		int height = _src.getHeight();
		createFilterImage(type, width, height);

		BufferedImage destImage = _dest;
		if (_dest == null)
			destImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = destImage.createGraphics();
		g2d.drawImage(_src, null, 0, 0);
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
		g2d.setComposite(ac);
		g2d.drawImage(filterImage, null, 0, 0);

		return (destImage);
	}

	private void createFilterImage(short _type, int _width, int _height)
			throws LowVisionException {
		filterImage = new BufferedImage(_width, _height,
				BufferedImage.TYPE_INT_ARGB);

		WritableRaster filterRaster = filterImage.copyData(null);
		DataBufferInt filterBufInt = (DataBufferInt) (filterRaster
				.getDataBuffer());
		int[] filterArray = filterBufInt.getData();
		int[][] alphaField = null;

		if (_type == TYPE_CIRCLE_INT) {
			alphaField = getCircleAlphaField(_width, _height,
					(float) diameterInt / (float) _width);
		} else if (_type == TYPE_CIRCLE_FLOAT) {
			alphaField = getCircleAlphaField(_width, _height, diameterFloat);
		} else {
			throw new LowVisionException("Unknown field type: " + _type); //$NON-NLS-1$
		}

		int k = 0;
		for (int j = 0; j < _height; j++) {
			for (int i = 0; i < _width; i++) {
				filterArray[k] = alphaField[j][i] << 24 & 0xff000000;
				k++;
			}
		}

		filterImage.setData(filterRaster);
	}

	// 0: transparent
	// 255: block all
	private int[][] getCircleAlphaField(int _width, int _height, float _diameter) {
		int[][] field = new int[_height][_width];
		float r = _width * _diameter / 2.0f;
		float r2 = 0.0f; // r for blur
		if (r > CIRCLE_WIDTH)
			r2 = r - CIRCLE_WIDTH;
		float xc = _width / 2.0f; // center of image(x)
		// float yc = (float) _height / 2.0f; // center of image(y)

		for (int j = 0; j < _height; j++) {
			for (int i = 0; i < _width; i++) {
				float x0 = i - xc;
				float y0 = j - xc;
				float r0 = (float) Math.sqrt(x0 * x0 + y0 * y0);
				int a = Math.round((r0 - r2) / (r - r2) * 255.0f);
				if (a < 0)
					a = 0;
				else if (a > 255)
					a = 255;

				field[j][i] = a;
			}
		}

		return (field);
	}
}
