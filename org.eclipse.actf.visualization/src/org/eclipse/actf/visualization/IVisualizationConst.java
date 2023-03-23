/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization;

/**
 * Constants for Visualization components
 */
@SuppressWarnings("nls")
public interface IVisualizationConst {
	/**
	 * prefix for result file
	 */
	String PREFIX_RESULT = "result";
	/**
	 * suffix for html file
	 */
	String SUFFIX_HTML = ".html";
	/**
	 * suffix for bitmap file
	 */
	String SUFFIX_BMP = ".bmp";
	/**
	 * prefix for screenshot file
	 */
	String PREFIX_SCREENSHOT = "screenshot";
	/**
	 * prefix for report file
	 */
	String PREFIX_REPORT = "report";
	/**
	 * prefix for visualization result file
	 */
	String PREFIX_VISUALIZATION = "visualization";

	/**
	 * Image file name for evaluation result (very good)
	 */
	public static final String RATING_V_GOOD = "VeryGood.png";
	/**
	 * Image file name for evaluation result (good)
	 */
	public static final String RATING_GOOD = "Good.png";
	/**
	 * Image file name for evaluation result (poor)
	 */
	public static final String RATING_POOR = "Poor.png";
	/**
	 * Image file name for evaluation result (bad)
	 */
	public static final String RATING_BAD = "Bad.png";
}
