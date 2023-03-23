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

package org.eclipse.actf.visualization.engines.lowvision;

import org.eclipse.actf.visualization.engines.lowvision.image.PageImageFactory;
import org.eclipse.actf.visualization.internal.engines.lowvision.operator.CVDOp;
import org.eclipse.actf.visualization.internal.engines.lowvision.operator.ColorFilterOp;
import org.eclipse.actf.visualization.internal.engines.lowvision.operator.GlareOp;

/**
 * This class defines low vision simulation type
 * 
 * @see PageImageFactory
 */
public class LowVisionType {

	/**
	 * Color vision deficiency type: Protan
	 */
	public static final int CVD_PROTAN = 1;
	/**
	 * Color vision deficiency type: Deutan
	 */
	public static final int CVD_DEUTAN = 2;
	/**
	 * Color vision deficiency type: Tritan
	 */
	public static final int CVD_TRITAN = 3;

	private static final double EYESIGHT_DEGREE_MARGIN = 0.1;

	private final int DISPLAY_RESOLUTION = 1024; // Y

	private final double DISPLAY_HEIGHT = 20.8; // 14.1 inches

	// public static final double DISPLAY_HEIGHT = 26.5; // 19 inches
	private final double EYE_DISPLAY_DISTANCE = 30.0;

	private boolean eyesight = false;

	private float eyesightDegree;

	// radius of blur array (exclude center, 3x3 -> 1)
	private int eyesightRadius;

	// undistinguishable pixel size for the target person
	private double eyesightPixel;

	// undistinguishable length(mm) for thetarget person
	private double eyesightLength;

	// color vision deficiency
	private boolean CVD = false;

	private int CVDType;

	// color filter
	private boolean colorFilter = false;

	private float colorFilterR; // [0.0, 1.0]

	private float colorFilterG; // [0.0, 1.0]

	private float colorFilterB; // [0.0, 1.0]

	// glare
	private boolean glare = false;

	private float glareDegree = 0.0f;

	private int displayResolution = DISPLAY_RESOLUTION;

	private double displayHeight = DISPLAY_HEIGHT;

	private double eyeDisplayDistance = EYE_DISPLAY_DISTANCE;

	/**
	 * Constructor of LowVisionType
	 */
	public LowVisionType() {
	}

	/**
	 * Check if an eyesight type simulation is enabled
	 * 
	 * @return true if the simulation is enabled
	 */
	public boolean doEyesight() {
		return (eyesight);
	}

	/**
	 * Turn on/off an eyesight type simulation
	 * 
	 * @param enable
	 *            set true to turn on
	 */
	public void setEyesight(boolean enable) {
		eyesight = enable;
	}

	/**
	 * Get target eyesight degree of eyesight type simulation
	 * 
	 * @return eyesight degree
	 */
	public float getEyesightDegree() {
		return (eyesightDegree);
	}

	/**
	 * Set target eyesight degree of eyesight type simulation
	 * 
	 * @param _deg
	 *            target eyesight degree
	 * @throws LowVisionException
	 *             if target degree is not positive value
	 */
	public void setEyesightDegree(float _deg) throws LowVisionException {
		if (_deg <= 0.0) {
			throw new LowVisionException("Eyesight degree must be positive."); //$NON-NLS-1$
		}
		eyesightDegree = _deg;
		eyesightPixel = calcUndistinguishablePixel(eyesightDegree);
		eyesightRadius = calcRadius(eyesightPixel);
		eyesightLength = calcUndistinguishableLength(eyesightDegree);
	}

	/**
	 * Get indistinguishable pixel size for target eyesight degree
	 * 
	 * @return indistinguishable pixel size
	 */
	public double getEyesightPixel() {
		return (eyesightPixel);
	}

	/**
	 * Get indistinguishable radius for target eyesight degree
	 * 
	 * @return indistinguishable radius
	 */
	public int getEyesightRadius() {
		return (eyesightRadius);
	}

	/**
	 * Get indistinguishable length for target eyesight degree
	 * 
	 * @return indistinguishable length
	 */
	public double getEyesightLength() {
		return (eyesightLength);
	}

	private double calcUndistinguishablePixel(double _degree) {
		double degree = _degree + EYESIGHT_DEGREE_MARGIN;
		double thetaD = 1.0 / degree; // distinguishable degree (arcmin)
		double thetaR = thetaD * Math.PI / 10800.0; // distinguishable
		// degree(radian)
		return (2.0 * Math.tan(thetaR / 2.0) * displayResolution
				* eyeDisplayDistance / displayHeight);
	}

	private int calcRadius(double _pixel) {
		return ((int) (Math.ceil(_pixel)));
	}

	private double calcUndistinguishableLength(double _degree) {
		double degree = _degree + EYESIGHT_DEGREE_MARGIN;
		double thetaD = 1.0 / degree;
		double thetaR = thetaD * Math.PI / 10800.0;
		return (20.0 * Math.tan(thetaR / 2.0) * eyeDisplayDistance);
		/*
		 * 20.0 = 10.0*2.0 2.0 equals in calcPixel() 10.0-> cm to mm
		 */
	}

	/**
	 * Check if a color vision deficiency type simulation is enabled
	 * 
	 * @return true if the simulation is enabled
	 */
	public boolean doCVD() {
		return (CVD);
	}

	/**
	 * Turn on/off a color vision deficiency type simulation
	 * 
	 * @param enable
	 *            set true to turn on
	 */
	public void setCVD(boolean enable) {
		CVD = enable;
	}

	/**
	 * Get target color vision deficiency type
	 * 
	 * @return color vision deficiency type
	 */
	public int getCVDType() {
		return (CVDType);
	}

	/**
	 * Set target color vision deficiency type
	 * 
	 * @param cvd_type
	 *            target color vision deficiency type
	 * @throws LowVisionException
	 *             if target type is not CVD_DEUTAN,CVD_PROTAN, or CVD_TRITAN
	 */
	public void setCVDType(int cvd_type) throws LowVisionException {
		if (cvd_type != 1 && cvd_type != 2 && cvd_type != 3) {
			throw new LowVisionException("CVD type must be 1,2, or 3"); //$NON-NLS-1$
		}
		CVDType = cvd_type;
	}

	/**
	 * Check if a color filter type simulation is enabled
	 * 
	 * @return true if the simulation is enabled
	 */
	public boolean doColorFilter() {
		return (colorFilter);
	}

	/**
	 * Turn on/off a color filter type simulation
	 * 
	 * @param enable
	 *            set true to turn on
	 */
	public void setColorFilter(boolean enable) {
		colorFilter = enable;
	}

	/**
	 * Get color filter value array (Red, Green and Blue). Each filter value is
	 * within range of [0.0, 1.0].
	 * 
	 * @return color filter value array
	 */
	public float[] getColorFilterRGB() {
		float[] rgb = { colorFilterR, colorFilterG, colorFilterB };
		return (rgb);
	}

	@SuppressWarnings("nls")
	private void setColorFilterRGB(float _r, float _g, float _b)
			throws LowVisionException {
		if (_r < 0.0 || 1.0 < _r)
			throw new LowVisionException("Value of R(" + _r
					+ ") is out of range.");
		colorFilterR = _r;
		if (_g < 0.0 || 1.0 < _g)
			throw new LowVisionException("Value of G(" + _g
					+ ") is out of range.");
		colorFilterG = _g;
		if (_b < 0.0 || 1.0 < _b)
			throw new LowVisionException("Value of B(" + _b
					+ ") is out of range.");
		colorFilterB = _b;
	}

	/**
	 * Set degree of color filter
	 * 
	 * @param _degree
	 *            degree of color filter
	 * @throws LowVisionException
	 *             if resulting filter value exceeds the range of [0.0, 1.0].
	 */
	public void setColorFilterDegree(float _degree) throws LowVisionException {
		// TODO check color filter values
		float bDegree = _degree;
		// float bDegree = 1.0f-(1.0f-_degree)*0.9f;
		float rgDegree = 1 - (1 - bDegree) / 2.0f;
		setColorFilterRGB(rgDegree, rgDegree, bDegree);
	}

	/**
	 * Check if a glare type simulation is enabled
	 * 
	 * @return true if the simulation is enabled
	 */
	public boolean doGlare() {
		return (glare);
	}

	/**
	 * Turn on/off a glare type simulation
	 * 
	 * @param enable
	 *            set true to turn on
	 */
	public void setGlare(boolean enable) {
		glare = enable;
	}

	/**
	 * Get target degree of glare
	 * 
	 * @return degree of glare
	 */
	public float getGlareDegree() {
		return (glareDegree);
	}

	/**
	 * Set target degree of glare
	 * 
	 * @param _deg
	 *            target degree of glare
	 */
	public void setGlareDegree(float _deg) {
		glareDegree = _deg;
	}

	/**
	 * Get number of enabled simulation types
	 * 
	 * @return number of enabled simulation types
	 */
	public int countTypes() {
		int num = 0;

		if (eyesight)
			num++;
		if (CVD)
			num++;
		if (colorFilter)
			num++;
		if (glare)
			num++;

		return (num);
	}

	/**
	 * Check if a blur type simulation is enabled
	 * 
	 * @return true if the simulation is enabled
	 */
	public boolean doBlur() {
		return (eyesight);
	}

	/**
	 * Check if at least one of color related simulation (color vision
	 * deficiency, color filter, or glare) is enabled
	 * 
	 * @return true if color related simulation is enabled
	 */
	public boolean doChangeColors() {
		return (CVD || colorFilter || glare);
	}

	/**
	 * Convert target color based on current low vision simulation type
	 * configuration
	 * 
	 * @param _src
	 *            target color
	 * @return resulting color
	 * @throws LowVisionException
	 */
	public int convertColor(int _src) throws LowVisionException {
		int dest = _src;
		// need to use same order as in the LowVisionFilter
		if (colorFilter) {
			dest = ColorFilterOp.convertColor(dest, colorFilterR, colorFilterG,
					colorFilterB);
		}
		if (glare) {
			dest = GlareOp.convertColor(dest, glareDegree);
		}
		if (CVD) {
			dest = CVDOp.convertColor(dest, CVDType);
		}
		return (dest);
	}

	@SuppressWarnings("nls")
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (eyesight) {
			sb.append("Eyesight:on Degree=");
			sb.append("" + eyesightDegree);
		} else {
			sb.append("Eyesight:off,");
		}
		if (CVD) {
			sb.append("  CVD:on Type=" + CVDType + ",");
		} else {
			sb.append("  CVD: off,");
		}
		if (colorFilter) {
			sb.append("  ColorFilter:on Degree=" + colorFilterB);
		} else {
			sb.append("  ColorFilter:off");
		}
		return (sb.toString());
	}

	/**
	 * Get height of target display. Default value is 20.8cm (14.1 inches).
	 * 
	 * @return height of target display
	 */
	public double getDisplayHeight() {
		return displayHeight;
	}

	/**
	 * Get vertical resolution of target display. Default value is 1024.
	 * 
	 * @return vertical resolution of target display
	 */
	public int getDisplayResolution() {
		return displayResolution;
	}

	/**
	 * Get distance between user's eye and display. Default value is 30cm.
	 * 
	 * @return distance between user's eye and display
	 */
	public double getEyeDisplayDistance() {
		return eyeDisplayDistance;
	}

	/**
	 * Set display height in target environment
	 * 
	 * @param height
	 *            target display height
	 */
	public void setDisplayHeight(double height) {
		this.displayHeight = height;
	}

	/**
	 * Set resolution of target display
	 * 
	 * @param resolution
	 *            resolution of target display
	 */
	public void setDisplayResolution(int resolution) {
		this.displayResolution = resolution;
	}

	/**
	 * Set distance between user's eye and display
	 * 
	 * @param distance
	 *            distance between user's eye and display
	 */
	public void setEyeDisplayDistance(double distance) {
		this.eyeDisplayDistance = distance;
	}
}
