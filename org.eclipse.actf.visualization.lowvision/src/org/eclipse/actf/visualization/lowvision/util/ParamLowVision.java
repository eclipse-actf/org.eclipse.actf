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

package org.eclipse.actf.visualization.lowvision.util;

import org.eclipse.actf.visualization.engines.lowvision.LowVisionType;
import org.eclipse.actf.visualization.lowvision.ui.internal.Messages;

public class ParamLowVision {
	// TODO use preference store
	private static ParamLowVision INSTANCE;

	// the current setting
	private boolean oEyeSight;

	private int iCurEyeSightValue; // the actual value:devided it by 100

	private boolean oColorVision;

	private int iCurColorVisionValue; // 1,2,3

	private boolean oColorFilter;

	private int iCurColorFilterValue; // the actual value:devided it by 100

	private LowVisionType lowVisionType;

	private static final double DEFAULT_EYESIGHT_DEGREE = 0.5;

	private static final int DEFAULT_CVD_TYPE = 2;

	private static final double DEFAULT_COLOR_FILTER = 0.8;

	public static ParamLowVision getDefaultInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ParamLowVision();
		}
		return INSTANCE;
	}

	public ParamLowVision() {

		oEyeSight = true;
		iCurEyeSightValue = (int) (Math.rint(DEFAULT_EYESIGHT_DEGREE * 100.0));
		oColorVision = true;
		iCurColorVisionValue = DEFAULT_CVD_TYPE;
		oColorFilter = true;
		iCurColorFilterValue = (int) (Math.rint(DEFAULT_COLOR_FILTER * 100.0));
		lowVisionType = new LowVisionType();
	}

	public LowVisionType getLowVisionType() {
		// LowVisionType lowVisionType = new LowVisionType();
		try {
			lowVisionType.setEyesight(oEyeSight);
			if (oEyeSight) {
				float fEyeSight = iCurEyeSightValue / 100.0f;
				/*
				 * float fEye = 0.1f; if (fEyeSight < 0.075f) fEye = 0.05f; else
				 * if (fEyeSight > 0.15f) fEye = 0.2f;
				 */
				// System.out.println(fEyeSight);
				lowVisionType.setEyesightDegree(fEyeSight);
			}

			lowVisionType.setCVD(oColorVision);
			if (oColorVision) {
				lowVisionType.setCVDType(iCurColorVisionValue);
			}

			lowVisionType.setColorFilter(oColorFilter);
			if (oColorFilter) {
				lowVisionType
						.setColorFilterDegree(iCurColorFilterValue / 100.0f);
			}
			// debug
			// DebugUtil.outMsg( this, "eyesight = " +
			// lowVisionType.getEyesight() + "\nCVD = " + lowVisionType.getCVD()
			// + "\nfileter = " + lowVisionType.getColorFilter());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (lowVisionType);
	}

	public Object clone() {
		ParamLowVision tmpParam = new ParamLowVision();
		tmpParam.setColorFilterValue(iCurColorFilterValue);
		tmpParam.setColorVisionValue(iCurColorVisionValue);
		tmpParam.setEyeSightValue(iCurEyeSightValue);
		tmpParam.setColorFilter(oColorFilter);
		tmpParam.setColorVision(oColorVision);
		tmpParam.setEyeSight(oEyeSight);

		return (tmpParam);
	}

	/**
	 * @return
	 */
	public int getColorFilterValue() {
		return iCurColorFilterValue;
	}

	/**
	 * @return
	 */
	public int getColorVisionValue() {
		return iCurColorVisionValue;
	}

	/**
	 * @return
	 */
	public int getEyeSightValue() {
		return iCurEyeSightValue;
	}

	/**
	 * @return
	 */
	public boolean useColorFilter() {
		return oColorFilter;
	}

	/**
	 * @return
	 */
	public boolean useColorVision() {
		return oColorVision;
	}

	/**
	 * @return
	 */
	public boolean useEyeSight() {
		return oEyeSight;
	}

	/**
	 * @param i
	 */
	public void setColorFilterValue(int i) {
		iCurColorFilterValue = i;
	}

	/**
	 * @param i
	 */
	public void setColorVisionValue(int i) {
		iCurColorVisionValue = i;
	}

	/**
	 * @param i
	 */
	public void setEyeSightValue(int i) {
		iCurEyeSightValue = i;
	}

	/**
	 * @param b
	 */
	public void setColorFilter(boolean b) {
		oColorFilter = b;
	}

	/**
	 * @param b
	 */
	public void setColorVision(boolean b) {
		oColorVision = b;
	}

	/**
	 * @param b
	 */
	public void setEyeSight(boolean b) {
		oEyeSight = b;
	}

	@SuppressWarnings("nls")
	public String toString() {
		StringBuffer result = new StringBuffer(" "); //$NON-NLS-1$

		if (useEyeSight()) {
			result.append(Messages.ParamLowVision_1 + " "
					+ (getEyeSightValue() / 100.0f) + "  ");
		}

		if (useColorVision()) {
			result.append(Messages.ParamLowVision_4 + " "
					+ getColorVisionValue() + "  ");
		}

		if (useColorFilter()) {
			result.append(Messages.ParamLowVision_7 + " "
					+ (getColorFilterValue() / 100.0f) + "  ");
		}

		return result.toString();
	}

}
