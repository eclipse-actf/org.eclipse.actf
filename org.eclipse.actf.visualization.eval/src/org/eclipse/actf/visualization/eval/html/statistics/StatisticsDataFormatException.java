/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.eval.html.statistics;

/**
 * Exception relates to HTML statistics data format error.
 */
public class StatisticsDataFormatException extends Exception {
	private static final long serialVersionUID = -2899274818180627014L;

	/**
	 * Constructor of Exception
	 */
	public StatisticsDataFormatException() {
		super();
	}

	/**
	 * Constructor of Exception
	 * 
	 * @param msg
	 *            exception message
	 */
	public StatisticsDataFormatException(String message) {
		super(message);
	}

}
