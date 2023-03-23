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
 * NTSC YIQ [0.0, 1.0] (float)
 */
public class ColorYIQ {
	private float Y;

	private float I;

	private float Q;

	public ColorYIQ(ColorSRGB _srgb) throws ColorException {
		this(_srgb, false);
	}

	public ColorYIQ(ColorSRGB _srgb, boolean _check) throws ColorException {
		float r = _srgb.getR();
		float g = _srgb.getG();
		float b = _srgb.getB();
		Y = 0.299f * r + 0.587f * g + 0.114f * b;
		I = 0.596f * r - 0.274f * g - 0.322f * b;
		Q = 0.211f * r - 0.522f * g + 0.311f * b;

		if (_check) {
			rangeCheck();
		} else {
			rangeAdjust();
		}
	}

	private void rangeCheck() throws ColorException {
		if (Y < 0.0f || 1.0f < Y) {
			throw new ColorException("Y is out of range: " + Y); //$NON-NLS-1$
		}
		if (I < 0.0f || 1.0f < I) {
			throw new ColorException("I is out of range: " + I); //$NON-NLS-1$
		}
		if (Q < 0.0f || 1.0f < Q) {
			throw new ColorException("Q is out of range: " + Q); //$NON-NLS-1$
		}
	}

	private void rangeAdjust() {
		if (Y < 0.0f)
			Y = 0.0f;
		else if (Y > 1.0f)
			Y = 1.0f;
		if (I < 0.0f)
			I = 0.0f;
		else if (I > 1.0f)
			I = 1.0f;
		if (Q < 0.0f)
			Q = 0.0f;
		else if (Q > 1.0f)
			Q = 1.0f;
	}

	public float getY() {
		return (Y);
	}

	public float getI() {
		return (I);
	}

	public float getQ() {
		return (Q);
	}

	public void setY(float _y) {
		Y = _y;
	}

	public void seti(float _i) {
		I = _i;
	}

	public void setQ(float _q) {
		Q = _q;
	}

	public ColorSRGB toSRGB() throws ColorException {
		float r = 1.0f * Y + 0.955653f * I + 0.622895f * Q;
		float g = 1.0f * Y - 0.27215f * I - 0.64834f * Q;
		float b = 1.0f * Y - 1.10516f * I + 1.704625f * Q;
		return (new ColorSRGB(r, g, b, false));
	}
}
