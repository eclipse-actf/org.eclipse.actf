/*******************************************************************************
 * Copyright (c) 2003, 2020 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Junji MAEDA - initial API and implementation
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.engines.lowvision.color;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// sRGB [0,255]
@SuppressWarnings("nls")
public class ColorIRGB {
	int R;

	int G;

	int B;

	public ColorIRGB() {
	}

	// convert from BufferedImage.TYPE_INT_RGB
	public ColorIRGB(int _i) {
		R = _i >> 16 & 0xff;
		G = _i >> 8 & 0xff;
		B = _i & 0xff;
	}

	public ColorIRGB(int _r, int _g, int _b) throws ColorException {
		setR(_r);
		setG(_g);
		setB(_b);
	}

	public ColorIRGB(int _r, int _g, int _b, boolean _check) throws ColorException {
		setR(_r, _check);
		setG(_g, _check);
		setB(_b, _check);
	}

	// color definition in CSS2
	// pre-defined 16 colors (see ColorUtil.java)
	// #RRGGBB
	// #RGB
	// rgb(R,G,B)
	// rgb(R%,G%,B%)
	public ColorIRGB(String _str) throws ColorException {
		Pattern patRemoveSpace = Pattern.compile("^\\s*(\\S.+\\S)\\s*$");
		Matcher matRemoveSpace = patRemoveSpace.matcher(_str.toLowerCase());
		if (!(matRemoveSpace.find())) {
			throw new ColorException("There no color spacifications. Input string is \"" + _str + "\"");
		}
		String spec = matRemoveSpace.group(1);

		String tmpS = ColorUtil.predefinedColor2Pound(spec);
		if (tmpS != null) {
			spec = tmpS;
		}

		if (spec.startsWith("#")) {
			spec = spec.substring(1); // remove "#"
			if (spec.length() == 6) {
				String rStr = spec.substring(0, 2);
				String gStr = spec.substring(2, 4);
				String bStr = spec.substring(4);
				try {
					R = Integer.parseInt(rStr, 16);
					G = Integer.parseInt(gStr, 16);
					B = Integer.parseInt(bStr, 16);
				} catch (NumberFormatException e) {
					e.printStackTrace();
					throw new ColorException("Bad color specification(1): #" + spec);
				}
			} else if (spec.length() == 3) {
				String rStr = spec.substring(0, 1);
				String gStr = spec.substring(1, 2);
				String bStr = spec.substring(2);
				try {
					R = Integer.parseInt(rStr + rStr, 16);
					G = Integer.parseInt(gStr + gStr, 16);
					B = Integer.parseInt(bStr + bStr, 16);
				} catch (NumberFormatException e) {
					e.printStackTrace();
					throw new ColorException("Bad color specification(2): #" + spec);
				}
			} else {
				throw new ColorException("Bad color specification(3): #" + spec);
			}
		} else if (spec.startsWith("rgb(") && spec.endsWith(")")) {
			spec = spec.substring(4); // remove "rgb("
			spec = spec.substring(0, spec.length() - 1); // remove ")"
			if (spec.indexOf("%") == -1) {
				Pattern patRGB = Pattern
						.compile("^\\s*([\\+\\-]?\\d+)\\s*\\,\\s*([\\+\\-]?\\d+)\\s*\\,\\s*([\\+\\-]?\\d+)\\s*$");
				Matcher matRGB = patRGB.matcher(spec);
				if (!(matRGB.find())) {
					throw new ColorException("Bad color specification(4): rgb(" + spec + ")");
				}
				try {
					setR(Integer.parseInt(matRGB.group(1)), false);
					setG(Integer.parseInt(matRGB.group(2)), false);
					setB(Integer.parseInt(matRGB.group(3)), false);
				} catch (NumberFormatException e) {
					e.printStackTrace();
					throw new ColorException("Bad color specification(5): rgb(" + spec + ")");
				}
			} else { // float value
				Pattern patRGB = Pattern.compile(
						"^\\s*([\\+\\-]?\\d+(\\.\\d+)*)\\%\\s*\\,\\s*([\\+\\-]?\\d+(\\.\\d+)*)\\%\\s*\\,\\s*([\\+\\-]?\\d+(\\.\\d+)*)\\%\\s*$");
				Matcher matRGB = patRGB.matcher(spec);
				if (!(matRGB.find())) {
					throw new ColorException("Bad color specification(6): rgb(" + spec + ")");
				}
				try {
					setR((int) ((Float.parseFloat(matRGB.group(1))) / 100.0f * 255.0f), false);
					setG((int) ((Float.parseFloat(matRGB.group(3))) / 100.0f * 255.0f), false);
					setB((int) ((Float.parseFloat(matRGB.group(5))) / 100.0f * 255.0f), false);
				} catch (Exception e) {
					e.printStackTrace();
					throw new ColorException("Bad color specification(7): rgb(" + spec + ")");
				}
			}
		} else if (spec.equals("transparent")) {
			R = ColorCSS.TRANSPARENT_R;
			G = ColorCSS.TRANSPARENT_G;
			B = ColorCSS.TRANSPARENT_B;
		} else {
			throw new ColorException("Unknown color specification: " + spec);
		}
	}

	public int getR() {
		return (R);
	}

	public int getG() {
		return (G);
	}

	public int getB() {
		return (B);
	}

	public void setR(int _r) throws ColorException {
		setR(_r, false);
	}

	public void setR(int _r, boolean _check) throws ColorException {
		if (_r < 0) {
			if (_check)
				throw new ColorException("Smaller than minimum.");
			else
				R = 0;
		} else if (255 < _r) {
			if (_check)
				throw new ColorException("Larger than maximum.");
			else
				R = 255;
		} else
			R = _r;
	}

	public void setG(int _g) throws ColorException {
		setG(_g, false);
	}

	public void setG(int _g, boolean _check) throws ColorException {
		if (_g < 0) {
			if (_check)
				throw new ColorException("Smaller than minimum.");
			else
				G = 0;
		} else if (255 < _g) {
			if (_check)
				throw new ColorException("Larger than maximum.");
			else
				G = 255;
		} else
			G = _g;
	}

	public void setB(int _b) throws ColorException {
		setB(_b, false);
	}

	public void setB(int _b, boolean _check) throws ColorException {
		if (_b < 0) {
			if (_check)
				throw new ColorException("Smaller than minimum.");
			else
				B = 0;
		} else if (255 < _b) {
			if (_check)
				throw new ColorException("Larger than maximum.");
			else
				B = 255;
		} else
			B = _b;
	}

	public boolean equals(ColorIRGB _c) throws ColorException {
		if (_c == null) {
			// return( false );
			throw new ColorException("The parameter is null.");
		}
		if ((_c.getR() == R) && (_c.getG() == G) && (_c.getB() == B)) {
			return (true);
		} else {
			return (false);
		}
	}

	public boolean equals(int _i) {
		if (this.toInt() == _i) {
			return (true);
		} else {
			return (false);
		}
	}

	public ColorSRGB toSRGB() throws ColorException {
		return (toSRGB(false));
	}

	public ColorSRGB toSRGB(boolean _check) throws ColorException {
		ColorSRGB s = new ColorSRGB(R / 255.0f, G / 255.0f, B / 255.0f, _check);
		// s.setR( (float)R/255.0f, _check );
		// s.setG( (float)G/255.0f, _check );
		// s.setB( (float)B/255.0f, _check );
		return (s);
	}

	public ColorXYZ toXYZ() throws ColorException {
		return (toXYZ(false));
	}

	public ColorXYZ toXYZ(boolean _check) throws ColorException {
		return (toSRGB(_check).toXYZ(_check));
	}

	public ColorYXY toYXY() throws ColorException {
		return (toYXY(false));
	}

	public ColorYXY toYXY(boolean _check) throws ColorException {
		return (toSRGB(_check).toXYZ(_check).toYXY(_check));
	}

	// return as BufferedImage.TYPE_INT_RGB
	public int toInt() {
		return ((R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff));
	}

	public void dump(PrintStream _ps) {
		PrintWriter pw = new PrintWriter(_ps, true);
		dump(pw);
	}

	public void dump(PrintWriter _pw) {
		_pw.println("-------");
		_pw.println("dumping color values");
		_pw.println("R= " + R + ", G= " + G + ", B= " + B);
		_pw.println("-------");
	}
}
