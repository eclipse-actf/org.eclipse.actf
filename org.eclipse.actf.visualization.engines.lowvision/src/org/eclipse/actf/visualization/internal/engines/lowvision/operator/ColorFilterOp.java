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

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.util.HashMap;

import org.eclipse.actf.visualization.engines.lowvision.LowVisionException;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.IInt2D;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.Int2D;

/*
 * simulate cataract (RGB)
 */
public class ColorFilterOp implements ILowVisionOperator {
	private float redRatio = 1.0f; // [0.0, 1.0]
	private float greenRatio = 0.9f; // [0.0, 1.0]
	private float blueRatio = 0.6f; // [0.0, 1.0]

	public float getRedRatio() {
		return (redRatio);
	}

	public void setRedRatio(float _rf) throws LowVisionException {
		if (_rf < 0.0f || 1.0f < _rf)
			throw new LowVisionException("Ratio is out of range: " + _rf); //$NON-NLS-1$
		redRatio = _rf;
	}

	public float getGreenRatio() {
		return (greenRatio);
	}

	public void setGreenRatio(float _gf) throws LowVisionException {
		if (_gf < 0.0f || 1.0f < _gf)
			throw new LowVisionException("Ratio is out of range: " + _gf); //$NON-NLS-1$
		greenRatio = _gf;
	}

	public float getBlueRatio() {
		return (blueRatio);
	}

	public void setBlueRatio(float _bf) throws LowVisionException {
		if (_bf < 0.0f || 1.0f < _bf)
			throw new LowVisionException("Ratio is out of range: " + _bf); //$NON-NLS-1$
		blueRatio = _bf;
	}

	public void setRatio(float[] _rgb) throws LowVisionException {
		if (_rgb.length < 3)
			throw new LowVisionException("Array is to small."); //$NON-NLS-1$
		setRedRatio(_rgb[0]);
		setGreenRatio(_rgb[1]);
		setBlueRatio(_rgb[2]);
	}

	public BufferedImage filter(BufferedImage _src, BufferedImage _dest)
			throws LowVisionException {
		WritableRaster srcRaster = _src.copyData(null);
		DataBufferInt srcBufInt = (DataBufferInt) (srcRaster.getDataBuffer());
		int[] srcArray = srcBufInt.getData();
		int srcSize = srcArray.length;

		BufferedImage destImage = _dest;
		if (_dest == null) {
			destImage = new BufferedImage(_src.getWidth(), _src.getHeight(),
					BufferedImage.TYPE_INT_RGB);
		}
		WritableRaster destRaster = destImage.copyData(null);
		DataBufferInt destBufInt = (DataBufferInt) (destRaster.getDataBuffer());
		int[] destArray = destBufInt.getData();
		int destSize = destArray.length;
		if (srcSize != destSize) {
			throw new LowVisionException("Sizes of src and dest images differ."); //$NON-NLS-1$
		}

		HashMap<Integer, Integer> pixelMap = new HashMap<Integer, Integer>();
		for (int i = 0; i < srcSize; i++) {
			Integer srcPixel = new Integer(srcArray[i]);
			Integer destPixel = null;
			if ((destPixel = pixelMap.get(srcPixel)) == null) {
				destPixel = new Integer(convertColor(srcArray[i], redRatio,
						greenRatio, blueRatio));
				pixelMap.put(srcPixel, destPixel);
			}
			destArray[i] = destPixel.intValue();
		}

		destImage.setData(destRaster);
		return (destImage);
	}

	public IInt2D filter(Int2D _src, Int2D _dest) throws LowVisionException {
		Int2D destImage = _dest;
		if (_dest == null) {
			destImage = new Int2D(_src.getWidth(), _src.getHeight());
		}

		HashMap<Integer, Integer> pixelMap = new HashMap<Integer, Integer>();
		for (int j = 0; j < _src.getHeight(); j++) {
			for (int i = 0; i < _src.getWidth(); i++) {
				int srcColor = _src.getData()[j][i];
				Integer srcPixel = new Integer(srcColor);
				Integer destPixel = pixelMap.get(srcPixel);
				if (destPixel == null) {
					int destColor = convertColor(srcColor, redRatio,
							greenRatio, blueRatio);
					destImage.getData()[j][i] = destColor;
					pixelMap.put(srcPixel, new Integer(destColor));
				} else {
					destImage.getData()[j][i] = destPixel.intValue();
				}
			}
		}
		return (destImage);
	}

	@SuppressWarnings("nls")
	public static int convertColor(int _src, float _rRatio, float _gRatio,
			float _bRatio) throws LowVisionException {
		int pixel = _src;
		int b = pixel & 0xff;
		int newB = Math.round(b * _bRatio);
		if (newB < 0 || 255 < newB)
			throw new LowVisionException("New blue value is out of range: "
					+ newB);
		int g = pixel >> 8 & 0xff;
		int newG = Math.round(g * _gRatio);
		if (newG < 0 || 255 < newG)
			throw new LowVisionException("New green value is out of range: "
					+ newG);
		int r = pixel >> 16 & 0xff;
		int newR = Math.round(r * _rRatio);
		if (newR < 0 || 255 < newR)
			throw new LowVisionException("New red value is out of range: "
					+ newR);
		pixel = pixel & 0xff000000 | newR << 16 | newG << 8 | newB;
		return (pixel);
	}
}
