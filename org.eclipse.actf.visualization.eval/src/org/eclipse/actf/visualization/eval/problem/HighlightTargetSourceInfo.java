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

package org.eclipse.actf.visualization.eval.problem;

import org.eclipse.actf.visualization.util.html2view.Html2ViewMapData;

/**
 * The class to store information to highlight HTML source
 * 
 * @see Html2ViewMapData
 */
public class HighlightTargetSourceInfo {
	private Html2ViewMapData start;

	private Html2ViewMapData end;

	/**
	 * Constructor of the class
	 * 
	 * @param start
	 *            target start position information
	 * @param end
	 *            target end position information
	 */
	public HighlightTargetSourceInfo(Html2ViewMapData start,
			Html2ViewMapData end) {
		this.start = start;
		this.end = end;
	}

	/**
	 * @return start position (line) of target tag
	 */
	public int getStartLine() {
		return (start.getStartLine() + 1);
	}

	/**
	 * @return start position (column) of target tag
	 */
	public int getStartColumn() {
		return start.getStartColumn();
	}

	/**
	 * @return end pisition (line) of target tag
	 */
	public int getEndLine() {
		return (end.getEndLine() + 1);
	}

	/**
	 * @return end position (column) of target tag
	 */
	public int getEndColumn() {
		return end.getEndColumn();
	}
}
