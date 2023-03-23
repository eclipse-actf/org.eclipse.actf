/*******************************************************************************
 * Copyright (c) 2004, 2020 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.eval.problem;

/**
 * Interface for low vision problem types
 */
public interface ILowvisionProblemSubtype {
	public static final int LOWVISION_COLOR_PROBLEM = 1;
	public static final int LOWVISION_BLUR_PROBLEM = 2;
	public static final int LOWVISION_IMAGE_COLOR_PROBLEM = 3;
	public static final int LOWVISION_FIXED_SIZE_FONT_PROBLEM = 4;
	public static final int LOWVISION_SMALL_FONT_PROBLEM = 5;
	public static final int LOWVISION_FIXED_SMALL_FONT_PROBLEM = 6;
	public static final int LOWVISION_PROHIBITED_BOTH_COLORS_PROBLEM = 7;
	public static final int LOWVISION_PROHIBITED_FOREGROUND_COLOR_PROBLEM = 8;
	public static final int LOWVISION_PROHIBITED_BACKGROUND_COLOR_PROBLEM = 9;
	public static final int LOWVISION_BACKGROUND_IMAGE_WARNING = 10;
	public static final int LOWVISION_FIXED_SIZE_FONT_WARNING = 11;	//for pt
	public static final int LOWVISION_FIXED_SMALL_FONT_WARNIG =12;	//for pt
	public static final int LOWVISION_COLOR_WITH_ALPHA_WARNING = 13;
}
