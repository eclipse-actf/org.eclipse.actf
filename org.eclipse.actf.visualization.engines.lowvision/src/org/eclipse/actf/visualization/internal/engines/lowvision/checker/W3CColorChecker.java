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

package org.eclipse.actf.visualization.internal.engines.lowvision.checker;

import org.eclipse.actf.visualization.internal.engines.lowvision.color.ColorIRGB;

public class W3CColorChecker extends ColorChecker {
	private static final double THRESHOLD_Y = 125.0;

	private static final int THRESHOLD_C = 500;

	private double diffY;

	private int diffC;

	private double sevY;

	private double sevC;
	
	private double sevL;

	private double severity;

	private double contrastRatio;
	
	public W3CColorChecker(ColorIRGB _c1, ColorIRGB _c2) {
		super(_c1, _c2);
		int r1 = color1.getR();
		int g1 = color1.getG();
		int b1 = color1.getB();
		int r2 = color2.getR();
		int g2 = color2.getG();
		int b2 = color2.getB();
		double y1 = calcY(r1, g1, b1);
		double y2 = calcY(r2, g2, b2);
		
		double l1 = calcL(r1, g1, b1);
		double l2 = calcL(r2, g2, b2);

		if(l1>l2){
			contrastRatio = (l1+0.05)/(l2+0.05);
		}else{
			contrastRatio = (l2+0.05)/(l1+0.05);			
		}
		
		diffY = Math.abs(y1 - y2);
		diffC = Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
		sevY = calcSevY(diffY);
		sevC = calcSevC(diffC);
		sevL = calcSevL(contrastRatio);
		//severity = Math.min(sevY, sevC);	WCAG 1.0, simulation
		severity = sevL;
				
	}

	public W3CColorChecker(int _i1, int _i2) {
		this(new ColorIRGB(_i1), new ColorIRGB(_i2));
	}

	public double calcSeverity() {
		return (this.severity);
	}

	public static double calcSeverity(ColorIRGB _c1, ColorIRGB _c2) {
		return ((new W3CColorChecker(_c1, _c2)).calcSeverity());
	}

	public double calcLuminanceSeverity() {
		return (this.sevY);
	}

	public static double calcLuminanceSeverity(ColorIRGB _c1, ColorIRGB _c2) {
		return ((new W3CColorChecker(_c1, _c2)).calcLuminanceSeverity());
	}

	public double calcChrominanceSeverity() {
		return (this.sevC);
	}

	public static double calcChrominanceSeverity(ColorIRGB _c1, ColorIRGB _c2) {
		return ((new W3CColorChecker(_c1, _c2)).calcChrominanceSeverity());
	}

	public int calcLuminanceDifference() {
		int iDiff = (int) (Math.round(this.diffY));
		if (iDiff < 0)
			iDiff = 0;
		else if (255 < iDiff)
			iDiff = 255;
		return (iDiff);
	}

	public static int calcLuminanceDifference(ColorIRGB _c1, ColorIRGB _c2) {
		return ((new W3CColorChecker(_c1, _c2)).calcLuminanceDifference());
	}

	public int calcChrominanceDifference() {
		return (this.diffC);
	}

	public static int calcChrominanceDifference(ColorIRGB _c1, ColorIRGB _c2) {
		return ((new W3CColorChecker(_c1, _c2)).calcChrominanceDifference());
	}

	private static double calcL(int _r, int _g, int _b){
		return(0.2126*calcRGB(_r)+0.7152*calcRGB(_g)+0.0722*calcRGB(_b));
	}
	
	private static double calcY(int _r, int _g, int _b) {
		return ( (_r * 299 + _g * 587 + _b * 114) / 1000.0);
	}
	
	private static double calcRGB(int rgb){
		double tmp = (double)rgb/255;
		if(tmp<=0.03928){
			return(tmp/12.92);
		}else{
			return(Math.pow((tmp+0.055)/1.055,2.4));
		}
	}
	

	private static double calcSevY(double _diffY) {
		double sevY = 0.0;
		if (_diffY < THRESHOLD_Y) {
			sevY = -_diffY / THRESHOLD_Y + 1.0;
		}
		if (sevY < 0.0)
			sevY = 0.0;
		else if (1.0 < sevY)
			sevY = 1.0;
		return (sevY);
	}

	private static double calcSevC(int _diffC) {
		double sevC = 0.0;
		if (_diffC < THRESHOLD_C) {
			sevC = -(double) _diffC / THRESHOLD_C + 1.0;
		}
		if (sevC < 0.0)
			sevC = 0.0;
		else if (1.0 < sevC)
			sevC = 1.0;
		return (sevC);
	}
	
	private static double calcSevL(double contrast){
		double sevL = 0.0;
		if(contrast>7){
			return sevL;
		}else if(contrast<3){
			return 1.0;
		}else{
			return(1.0-(contrast-3)/(7.0-3));			
		}
	}

	@Override
	public double calcContrast() {
		return contrastRatio;
	}
}
