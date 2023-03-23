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
 * Yxy x,y [0.0, 1.0] Y (yy) [0.0, 1.0] (in usual [0, 100])
 */
public class ColorYXY {
	private float YY;

	private float x;

	private float y;

	public ColorYXY() {
	}

	public ColorYXY(float _yy, float _x, float _y) throws ColorException {
		this(_yy, _x, _y, false);
	}

	public ColorYXY(float _yy, float _x, float _y, boolean _check)
			throws ColorException {
		setYY(_yy, _check);
		setX(_x, _check);
		setY(_y, _check);
	}

	public float getYY() {
		return (YY);
	}

	public float getX() {
		return (x);
	}

	public float getY() {
		return (y);
	}

	public void setYY(float _yy) throws ColorException {
		setYY(_yy, false);
	}

	public void setYY(float _yy, boolean _check) throws ColorException {
		if (_yy < 0.0f) {
			if (_check)
				throw new ColorException("Smaller than minimum."); //$NON-NLS-1$
			else
				YY = 0.0f;
		} else if (1.0f < _yy) {
			if (_check)
				throw new ColorException("Larger than maximum."); //$NON-NLS-1$
			else
				YY = 1.0f;
		} else
			YY = _yy;
	}

	public void setX(float _x) throws ColorException {
		setX(_x, false);
	}

	public void setX(float _x, boolean _check) throws ColorException {
		if (_x < 0.0f) {
			if (_check)
				throw new ColorException("Smaller than minimum."); //$NON-NLS-1$
			else
				x = 0.0f;
		} else if (1.0f < _x) {
			if (_check)
				throw new ColorException("Larger than maximum."); //$NON-NLS-1$
			else
				x = 1.0f;
		} else
			x = _x;
	}

	public void setY(float _y) throws ColorException {
		setY(_y, false);
	}

	public void setY(float _y, boolean _check) throws ColorException {
		if (_y < 0.0f) {
			if (_check)
				throw new ColorException("Smaller than minimum."); //$NON-NLS-1$
			else
				y = 0.0f;
		} else if (1.0f < _y) {
			if (_check)
				throw new ColorException("Larger than maximum."); //$NON-NLS-1$
			else
				y = 1.0f;
		} else
			y = _y;
	}

	public ColorXYZ toXYZ() throws ColorException {
		return (toXYZ(false));
	}

	public ColorXYZ toXYZ(boolean _check) throws ColorException {
		ColorXYZ xyz = new ColorXYZ();
		if (y == 0.0f) {
			xyz.setX(0.0f);
			xyz.setY(0.0f);
			xyz.setZ(0.0f);
		} else {
			xyz.setX(YY / y * x, _check);
			xyz.setY(YY, _check);
			xyz.setZ(YY / y * (1 - x - y), _check);
		}
		return (xyz);
	}

	public ColorSRGB toSRGB() throws ColorException {
		return (toSRGB(false));
	}

	public ColorSRGB toSRGB(boolean _check) throws ColorException {
		return (toXYZ(_check).toSRGB(_check));
	}

	public ColorIRGB toIRGB() throws ColorException {
		return (toIRGB(false));
	}

	public ColorIRGB toIRGB(boolean _check) throws ColorException {
		return (toXYZ(_check).toSRGB(_check).toIRGB(_check));
	}
}
