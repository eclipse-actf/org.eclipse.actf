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

package org.eclipse.actf.visualization.internal.engines.lowvision.color;

/*
 * L*a*b* color space
 */
public class ColorLAB {
	private static final double THIRD = 1.0 / 3.0;

	private float L;

	private float a;

	private float b;

	// ColorXYZ [0.0, 1.0]
	public ColorLAB(ColorXYZ _xyz) {
		float x = _xyz.getX();
		float y = _xyz.getY();
		float z = _xyz.getZ();

		// L*
		if (y > 0.008856f) {
			L = (float) (116.0 * Math.pow((double) y, THIRD) - 16.0);
		} else {
			L = 903.3f * y;
		}

		// a*,b*
		double xThird = Math.pow((double) x, THIRD);
		double yThird = Math.pow((double) y, THIRD);
		double zThird = Math.pow((double) z, THIRD);
		a = (float) (500.0 * (xThird - yThird));
		b = (float) (200.0 * (yThird - zThird));
	}

	public ColorLAB(float _l, float _a, float _b) throws ColorException {
		setL(_l);
		setA(_a);
		setB(_b);
	}

	public float getL() {
		return (L);
	}

	public void setL(float _l) throws ColorException {
		if (_l < 0.0f || 100.0f < _l) {
			throw new ColorException("L is out of range: " + _l); //$NON-NLS-1$
		}
		L = _l;
	}

	public float getA() {
		return (a);
	}

	public void setA(float _a) {
		a = _a;
	}

	public float getB() {
		return (b);
	}

	public void setB(float _b) {
		b = _b;
	}

	public ColorXYZ toXYZ() throws ColorException {
		float y = 0.0f;
		double y3 = 0.0;
		if (L > 8.0f) {
			y3 = (double) ((L + 16.0f) / 116.0f);
			y = (float) (Math.pow(y3, 3.0));
		} else {
			y = L / 903.3f;
			y3 = Math.pow((double) y, THIRD);
		}
		float x = (float) (Math.pow(((double) a / 500.0 + y3), 3.0));
		float z = (float) (Math.pow((y3 - (double) b / 200.0), 3.0));

		return (new ColorXYZ(x, y, z, false));
	}

	public float chroma() {
		return ((float) (Math.sqrt(a * a + b * b)));
	}

	public double hueAngle() {
		return (Math.atan2(b, a));
	}

	// color-difference
	public static float deltaE(ColorLAB _c1, ColorLAB _c2) {
		double dL = _c1.L - _c2.L;
		double dA = _c1.a - _c2.a;
		double dB = _c1.b - _c2.b;
		return ((float) (Math.sqrt(dL * dL + dA * dA + dB * dB)));
	}

	//brightness difference (ABS)
	public static float deltaL(ColorLAB _c1, ColorLAB _c2) {
		return (Math.abs(_c1.L - _c2.L));
	}

	//hue difference
	public static float deltaH(ColorLAB _c1, ColorLAB _c2) {
		float dC = _c1.chroma() - _c2.chroma();
		float dA = _c1.a - _c2.a;
		float dB = _c1.b - _c2.b;

		return ((float) (Math.sqrt(dA * dA + dB * dB - dC * dC)));
	}

	public static double deltaHueAngle(ColorLAB _c1, ColorLAB _c2) {
		double ha1 = _c1.hueAngle();
		double ha2 = _c2.hueAngle();

		/*
		 *  in usual case, achromatic color -> return NaN
		 *  this method returns 0 (means not need to consider the difference)
		 */
		if (Double.isNaN(ha1)) {
			// return( Double.NaN );
			return (0.0);
		}
		if (Double.isNaN(ha2)) {
			// return( Double.NaN );
			return (0.0);
		}
		double diff = Math.abs(ha1 - ha2);
		if (diff <= Math.PI) {
			return (diff);
		} else if (diff <= 2 * Math.PI) {
			return (2 * Math.PI - diff);
		} else { 
			return (diff - 2 * Math.PI);
		}
	}

	public static double deltaHueAngleInDegree(ColorLAB _c1, ColorLAB _c2) {
		return (deltaHueAngle(_c1, _c2) * 180.0 / Math.PI);
	}
}
