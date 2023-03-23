/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.util.html2view;

/**
 * Utility class to store line number and column information of element
 */
public class Html2ViewMapData {

	/**
	 * ACTF_ID
	 */
	public static final String ACTF_ID = "eclipse-actf-id"; //$NON-NLS-1$

	int start[] = { -1, -1 };
	int end[] = { -1, -1 };

	/**
	 * Constructor of the class
	 * 
	 * @param start
	 *            start position information [line, column] of corresponding
	 *            element
	 * @param end
	 *            end position information [line, column] of corresponding
	 *            element
	 */
	public Html2ViewMapData(int[] start, int[] end) {
		setStart(start);
		setEnd(end);
	}

	public String toString() {
		return ("start:" + start[0] + "," + start[1] + " end:" + end[0] + "," + end[1]); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	/**
	 * Get end position of the element
	 * 
	 * @return end position [line, column]
	 */
	public int[] getEnd() {
		return end;
	}

	/**
	 * Get start position of the element
	 * 
	 * @return start position [line, column]
	 */
	public int[] getStart() {
		return start;
	}

	/**
	 * Get start line number of the element
	 * 
	 * @return start line number
	 */
	public int getStartLine() {
		return start[0];
	}

	/**
	 * Get end line number of the element
	 * 
	 * @return end line number
	 */
	public int getEndLine() {
		return end[0];
	}

	/**
	 * Get start column of the element
	 * 
	 * @return column
	 */
	public int getStartColumn() {
		return start[1];
	}

	/**
	 * Get end column of the element
	 * 
	 * @return column
	 */
	public int getEndColumn() {
		return end[1];
	}

	private void setEnd(int[] is) {
		if (is != null && is.length == 2) {
			end = is;
		}
	}

	private void setStart(int[] is) {
		if (is != null && is.length == 2) {
			start = is;
		}
	}

}
