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

package org.eclipse.actf.visualization.eval.guideline;

import java.util.EventListener;

/**
 * This listener interface may be implemented in order to receive a
 * {@link GuidelineSelectionChangedEvent} notification when user changes a
 * selection of target guidelines and/or metrics.
 * 
 * @see GuidelineHolder#addGuidelineSelectionChangedListener(IGuidelineSlectionChangedListener)
 * @see GuidelineHolder#removeGuidelineSelectionChangedListener(IGuidelineSlectionChangedListener)
 */
public interface IGuidelineSlectionChangedListener extends EventListener {
	/**
	 * This method is called when user changes a selection of target guidelines
	 * and/or metrics.
	 * 
	 * @param e
	 *            the {@link GuidelineSelectionChangedEvent}
	 */
	public void selectionChanged(GuidelineSelectionChangedEvent e);
}
