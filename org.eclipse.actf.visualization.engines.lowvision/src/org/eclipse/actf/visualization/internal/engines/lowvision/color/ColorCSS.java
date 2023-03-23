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

import java.util.StringTokenizer;

import org.eclipse.actf.util.logging.DebugPrintUtil;

/*
 * colors in CSS
 * RGB(256)
 * 
 * valid syntax
 *  #RRGGBB
 *  #RGB
 *  rgb(R,G,B)
 *  rgb(R%,G%,B%)
 *  pre-defined colors(16) (see ColorUtil.java)
 *  
 *  //2020-01
 *  hsl(h,s%,l%) *  
 *  rgba/hsla -> try to calculate if a=1. if a!=1 then throws exception
 */
@SuppressWarnings("nls")
public class ColorCSS extends ColorIRGB {
	private static final String DELIM = "/";

	// default values
	static final int TRANSPARENT_R = 0xff; // "transparent"
	static final int TRANSPARENT_G = 0xff; // "transparent"
	static final int TRANSPARENT_B = 0xff; // "transparent"
	public static final int TRANSPARENT = ((TRANSPARENT_R & 0xff) << 16) | ((TRANSPARENT_G & 0xff) << 8)
			| (TRANSPARENT_B & 0xff);
	public static final int DEFAULT_BACKGROUND_COLOR_INT = TRANSPARENT;
	public static final int DEFAULT_COLOR_INT = 0;

	private static ColorIRGB parseHSL(String hsl) {
		// assume (h, s%, l%) format
		String target = hsl.replaceAll("[^0-9\\.,]", "");
		// System.out.println(hsl + ":\t" + target);
		StringTokenizer st = new StringTokenizer(target, ",", false);
		double h, s, l, max, min;
		double r, g, b;
		int ri, gi, bi;
		if (st.countTokens() == 3) {
			try {
				h = Float.parseFloat(st.nextToken());
				s = Float.parseFloat(st.nextToken());
				l = Float.parseFloat(st.nextToken());

				// assume valid data
				// TODO validation if needed

				if (l >= 50) {
					max = 2.55 * (l + (100 - l) * (s / 100));
					min = 2.55 * (l - (100 - l) * (s / 100));
				} else {
					max = 2.55 * (l + l * (s / 100));
					min = 2.55 * (l - l * (s / 100));
				}

				if (0 <= h && h < 60) {
					r = max;
					g = (h / 60) * (max - min) + min;
					b = min;
				} else if (60 <= h && h < 120) {
					r = ((120 - h) / 60) * (max - min) + min;
					g = max;
					b = min;
				} else if (120 <= h && h < 180) {
					r = min;
					g = max;
					b = ((h - 120) / 60) * (max - min) + min;
				} else if (180 <= h && h < 240) {
					r = min;
					g = ((240 - h) / 60) * (max - min) + min;
					b = max;
				} else if (240 <= h && h < 300) {
					r = ((h - 240) / 60) * (max - min) + min;
					g = min;
					b = max;
				} else if (300 <= h && h < 360) {
					r = max;
					g = min;
					b = ((360 - h) / 60) * (max - min) + min;
				} else {
					// error
					return null;
				}

				ri = (int) Math.round(r);
				gi = (int) Math.round(g);
				bi = (int) Math.round(b);

				ColorIRGB ci = new ColorIRGB(ri, gi, bi);
				return ci;
				// System.out.println(target + ":\t" + ri + ", " + gi + ", " +
				// bi);

			} catch (Exception e) {
				DebugPrintUtil.debugPrintStackTrace(e);
			}
		}
		return null;
	}

	private String originalString = "";
	private boolean hasAlpha = false;

	public ColorCSS(String _s) throws ColorException {
		this(_s, true);
	}

	public ColorCSS(String _s, boolean _check) throws ColorException {

		_s = _s.replaceAll("\\p{Space}", "").toLowerCase();

		if (!(_s.endsWith(DELIM))) {
			originalString = _s;
		} else {
			originalString = _s.substring(0, _s.length() - 1);
		}

		if (originalString.startsWith("rgba") || originalString.startsWith("hsla")) {
			hasAlpha = true;

			// if alpha = 1 then try to convert value into RGB
			if (originalString.endsWith(",1)")) {
				originalString = originalString.substring(0, 3)
						+ originalString.substring(4, originalString.length() - 3) + ")";
			} else {
				throw new ColorException(ColorException.ALPHA_EXISTS);
			}
		}

		// foreground color
		if (originalString.indexOf(DELIM) == -1) {
			if (originalString.startsWith("hsl")) {
				ColorIRGB ci = parseHSL(originalString);
				if (ci != null) {
					R = ci.getR();
					G = ci.getG();
					B = ci.getB();
				}
			} else {
				try {
					ColorIRGB ci = new ColorIRGB(originalString);
					R = ci.getR();
					G = ci.getG();
					B = ci.getB();
				} catch (ColorException e) {
					if (hasAlpha) {
						throw new ColorException(ColorException.ALPHA_EXISTS);
					}
				}
			}
		} else { // background-color -> need to consider ancestor

			StringTokenizer st = new StringTokenizer(originalString.toLowerCase(), DELIM);
			boolean success = false;
			while (st.hasMoreTokens()) {
				String tmpStr = st.nextToken();

				if (!(tmpStr.equals("transparent"))) {
					// ColorIRGB ci = interpret( tmpStr );
					ColorIRGB ci = new ColorIRGB(tmpStr);
					R = ci.getR();
					G = ci.getG();
					B = ci.getB();
					success = true;
					break;
				}
			}
			if (!success) {
				R = TRANSPARENT_R;
				G = TRANSPARENT_G;
				B = TRANSPARENT_B;
			}
		}

		if (_check) {
			rangeCheck();
		} else {
			rangeAdjust();
		}
	}

	public ColorCSS() throws ColorException {
		throw new ColorException("Constructor in wrong format.");
	}

	public ColorCSS(int _i) throws ColorException {
		throw new ColorException("Constructor in wrong format.");
	}

	public ColorCSS(int _i1, int _i2, int _i3) throws ColorException {
		throw new ColorException("Constructor in wrong format.");
	}

	public ColorCSS(int _i1, int _i2, int _i3, boolean _b) throws ColorException {
		throw new ColorException("Constructor in wrong format.");
	}

	private void rangeCheck() throws ColorException {
		if (R < 0 || R > 255) {
			throw new ColorException("R is out of range: " + R + ", inputString = " + originalString);
		}
		if (G < 0 || G > 255) {
			throw new ColorException("G is out of range: " + G + ", inputString = " + originalString);
		}
		if (B < 0 || B > 255) {
			throw new ColorException("B is out of range: " + B + ", inputString = " + originalString);
		}
	}

	private void rangeAdjust() {
		if (R < 0)
			R = 0;
		else if (R > 255)
			R = 255;
		if (G < 0)
			G = 0;
		else if (G > 255)
			G = 255;
		if (B < 0)
			B = 0;
		else if (B > 255)
			B = 255;
	}

	public String getOriginalString() {
		return (originalString);
	}
}
