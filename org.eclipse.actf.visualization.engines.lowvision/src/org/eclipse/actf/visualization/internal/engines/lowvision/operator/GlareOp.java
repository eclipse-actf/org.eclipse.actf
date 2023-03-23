/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and Others
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
import org.eclipse.actf.visualization.internal.engines.lowvision.color.ColorUtil;

public class GlareOp implements ILowVisionOperator {
	private float glareDegree = 0.0f;// [0,1] original-white

	public GlareOp() {
	}

	public GlareOp(float _deg) throws LowVisionException {
		if (!checkDegree(_deg)) {
			throw new LowVisionException("Glare Degree " + _deg //$NON-NLS-1$
					+ " is out of range."); //$NON-NLS-1$
		}
		glareDegree = _deg;
	}

	public float getGlareDegree() {
		return (glareDegree);
	}

	public void setGlareDegree(float _d) {
		glareDegree = _d;
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
				destPixel = new Integer(convertColor(srcArray[i], glareDegree));
				pixelMap.put(srcPixel, destPixel);
			}
			destArray[i] = destPixel.intValue();
		}

		destImage.setData(destRaster);
		return (destImage);
	}

	public static int convertColor(int _color, float _degree)
			throws LowVisionException {
		if (!checkDegree(_degree)) {
			throw new LowVisionException("Glare Degree " + _degree //$NON-NLS-1$
					+ " is out of range."); //$NON-NLS-1$
		}
		int[] rgb = ColorUtil.intToRGB(_color);

		int r = convertColorComponent(rgb[0], _degree);
		int g = convertColorComponent(rgb[1], _degree);
		int b = convertColorComponent(rgb[2], _degree);

		return (ColorUtil.RGBToInt(r, g, b));
	}

	private static int convertColorComponent(int _component, float _degree) {
		int answer = _component + Math.round(_degree * (255 - _component));
		if (answer < 0)
			return (0);
		if (answer > 255)
			return (255);
		return (answer);
	}

	private static boolean checkDegree(float _degree) {
		if (0.0f <= _degree && _degree <= 1.0f) {
			return (true);
		} else {
			return (false);
		}
	}
}
