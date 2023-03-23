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

import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;

/*
 * XYZ [0.0, 1.0] (div by 100.0 to convert from CIE)
 * 
 */
public class ColorXYZ {
	ICC_ColorSpace cs = (ICC_ColorSpace) (ColorSpace
			.getInstance(ColorSpace.CS_sRGB));

	private float[] xyz = new float[3];

	public ColorXYZ() {
	}

	public ColorXYZ(float[] _xyz) throws ColorException {
		this(_xyz, false);
	}

	public ColorXYZ(float[] _xyz, boolean _check) throws ColorException {
		this(_xyz[0], _xyz[1], _xyz[2], _check);
	}

	public ColorXYZ(float _x, float _y, float _z) throws ColorException {
		this(_x, _y, _z, false);
	}

	public ColorXYZ(float _x, float _y, float _z, boolean _check)
			throws ColorException {
		setX(_x, _check);
		setY(_y, _check);
		setZ(_z, _check);
	}

	public float getX() {
		return (xyz[0]);
	}

	public float getY() {
		return (xyz[1]);
	}

	public float getZ() {
		return (xyz[2]);
	}

	public void setX(float _x) throws ColorException {
		setX(_x, false);
	}

	public void setX(float _x, boolean _check) throws ColorException {
		if (_x < 0.0f) {
			if (_check)
				throw new ColorException("Smaller than minimum."); //$NON-NLS-1$
			else
				xyz[0] = 0.0f;
		} else if (1.0f < _x) {
			if (_check)
				throw new ColorException("Larger than maximum."); //$NON-NLS-1$
			else
				xyz[0] = 1.0f;
		} else
			xyz[0] = _x;
	}

	public void setY(float _y) throws ColorException {
		setY(_y, false);
	}

	public void setY(float _y, boolean _check) throws ColorException {
		if (_y < 0.0f) {
			if (_check)
				throw new ColorException("Smaller than minimum."); //$NON-NLS-1$
			else
				xyz[1] = 0.0f;
		} else if (1.0f < _y) {
			if (_check)
				throw new ColorException("Larger than maximum."); //$NON-NLS-1$
			else
				xyz[1] = 1.0f;
		} else
			xyz[1] = _y;
	}

	public void setZ(float _z) throws ColorException {
		setZ(_z, false);
	}

	public void setZ(float _z, boolean _check) throws ColorException {
		if (_z < 0.0f) {
			if (_check)
				throw new ColorException("Smaller than minimum."); //$NON-NLS-1$
			else
				xyz[2] = 0.0f;
		} else if (1.0f < _z) {
			if (_check)
				throw new ColorException("Larger than maximum."); //$NON-NLS-1$
			else
				xyz[2] = 1.0f;
		} else
			xyz[2] = _z;
	}

	public ColorSRGB toSRGB() throws ColorException {
		return (toSRGB(false));
	}

	public ColorSRGB toSRGB(boolean _check) throws ColorException {
		float[] frgb = cs.fromCIEXYZ(xyz);
		ColorSRGB s = new ColorSRGB(frgb[0], frgb[1], frgb[2], _check);
		return (s);
	}

	public ColorYXY toYXY() throws ColorException {
		return (toYXY(false));
	}

	public ColorYXY toYXY(boolean _check) throws ColorException {
		float sum = xyz[0] + xyz[1] + xyz[2];
		ColorYXY yxy = new ColorYXY();
		if (sum == 0.0f) {
			yxy.setYY(0.0f);
			yxy.setX(0.0f);
			yxy.setY(0.0f);
		} else {
			yxy.setYY(xyz[1], _check);
			yxy.setX(xyz[0] / sum, _check);
			yxy.setY(xyz[1] / sum, _check);
		}
		return (yxy);
	}

	public ColorIRGB toIRGB() throws ColorException {
		return (toIRGB(false));
	}

	public ColorIRGB toIRGB(boolean _check) throws ColorException {
		return (toSRGB(_check).toIRGB(_check));
	}

	public ColorLAB toLAB() {
		return (new ColorLAB(this));
	}
}
