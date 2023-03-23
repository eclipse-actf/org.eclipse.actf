/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer;

import java.util.List;

import org.eclipse.actf.visualization.eval.problem.HighlightTargetId;

/**
 * Interface to highlight corresponding position of selected item in
 * visualization result. If visualization view implements this interface, they
 * can be synchronized with element information view.
 */
public interface IHighlightElementListener {
	/**
	 * Clear highlight
	 */
	void clearHighlight();

	/**
	 * Highlight corresponding positions.
	 * 
	 * @param targetIdList
	 *            target positions as list of {@link HighlightTargetId}
	 */
	void highlight(List<HighlightTargetId> targetIdList);
}
