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

/**
 * The class to store IDs to highlight visualization result
 */
public class HighlightTargetId {
	int startId;
	int endId;

	/**
	 * Constructor of the class
	 * 
	 * @param startId
	 *            ID of start position
	 * @param endId
	 *            ID of end position
	 */
	public HighlightTargetId(int startId, int endId) {
		if (endId < startId) {
			endId = startId;
		}
		this.startId = startId;
		this.endId = endId;
	}

	/**
	 * @return end position ID
	 */
	public int getEndId() {
		return endId;
	}

	/**
	 * @return start position ID
	 */
	public int getStartId() {
		return startId;
	}

}
