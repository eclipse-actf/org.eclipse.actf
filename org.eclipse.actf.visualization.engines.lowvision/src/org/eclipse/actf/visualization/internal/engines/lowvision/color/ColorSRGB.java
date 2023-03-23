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

//sRGB [0.0 , 1.0]
public class ColorSRGB {
	private ICC_ColorSpace cs;
	private float[] rgb = new float[3];

	public ColorSRGB( float _r, float _g, float _b ) throws ColorException{
		cs =(ICC_ColorSpace) (ColorSpace.getInstance(ColorSpace.CS_sRGB));		
		setR(_r);
		setG(_g);
		setB(_b);
	}
	public ColorSRGB( float _r, float _g, float _b, boolean _check ) throws ColorException{
		cs =(ICC_ColorSpace) (ColorSpace.getInstance(ColorSpace.CS_sRGB));		
		setR(_r,_check);
		setG(_g,_check);
		setB(_b,_check);
	}

	public float getR() {
		return (rgb[0]);
	}
	public float getG() {
		return (rgb[1]);
	}
	public float getB() {
		return (rgb[2]);
	}

	public void setR(float _r) throws ColorException {
		setR(_r, false);
	}
	public void setR(float _r, boolean _check) throws ColorException {
		if (_r < 0.0f) {
			if (_check)
				throw new ColorException("Smaller than minimum."); //$NON-NLS-1$
			else
				rgb[0] = 0.0f;
		} else if (1.0f < _r) {
			if (_check)
				throw new ColorException("Larger than maximum."); //$NON-NLS-1$
			else
				rgb[0] = 1.0f;
		} else
			rgb[0] = _r;
	}
	public void setG(float _g) throws ColorException {
		setG(_g, false);
	}
	public void setG(float _g, boolean _check) throws ColorException {
		if (_g < 0.0f) {
			if (_check)
				throw new ColorException("Smaller than minimum."); //$NON-NLS-1$
			else
				rgb[1] = 0.0f;
		} else if (1.0f < _g) {
			if (_check)
				throw new ColorException("Larger than maximum."); //$NON-NLS-1$
			else
				rgb[1] = 1.0f;
		} else
			rgb[1] = _g;
	}
	public void setB(float _b) throws ColorException {
		setB(_b, false);
	}
	public void setB(float _b, boolean _check) throws ColorException {
		if (_b < 0.0f) {
			if (_check)
				throw new ColorException("Smaller than minimum."); //$NON-NLS-1$
			else
				rgb[2] = 0.0f;
		} else if (1.0f < _b) {
			if (_check)
				throw new ColorException("Larger than maximum."); //$NON-NLS-1$
			else
				rgb[2] = 1.0f;
		} else
			rgb[2] = _b;
	}

	public ColorIRGB toIRGB() throws ColorException {
		return (toIRGB(false));
	}
	public ColorIRGB toIRGB(boolean _check) throws ColorException {
		int ir = Math.round(rgb[0] * 255.0f);
		int ig = Math.round(rgb[1] * 255.0f);
		int ib = Math.round(rgb[2] * 255.0f);

		ColorIRGB i = new ColorIRGB();
		i.setR(ir, _check);
		i.setG(ig, _check);
		i.setB(ib, _check);
		return (i);
	}

	public ColorXYZ toXYZ() throws ColorException {
		return (toXYZ(false));
	}
	public ColorXYZ toXYZ(boolean _check) throws ColorException {
		float[] fxyz = null;

		boolean success = false;
		while( !success ){
			try{
				fxyz = cs.toCIEXYZ(rgb);
				success = true;
			}catch( Exception e ){
				// e.printStackTrace();
				try{
					Thread.sleep( 20L );
				}catch( InterruptedException ie ){
					;
				}
			}
		}

		ColorXYZ xyz = new ColorXYZ();
		xyz.setX(fxyz[0], _check);
		xyz.setY(fxyz[1], _check);
		xyz.setZ(fxyz[2], _check);
		return (xyz);
	}

	public ColorYXY toYXY() throws ColorException {
		return (toYXY(false));
	}
	public ColorYXY toYXY(boolean _check) throws ColorException {
		return (toXYZ(_check).toYXY(_check));
	}

	public ColorYIQ toYIQ() throws ColorException{
		return( new ColorYIQ(this) );
	}
	public ColorYIQ toYIQ(boolean _check) throws ColorException{
		return( new ColorYIQ(this,_check));
	}
}
