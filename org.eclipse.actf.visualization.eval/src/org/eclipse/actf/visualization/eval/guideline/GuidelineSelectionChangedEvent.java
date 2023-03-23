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

import java.util.EventObject;

/**
 * The {@link GuidelineSelectionChangedEvent} is sent by {@link GuidelineHolder}
 * to {@link IGuidelineSlectionChangedListener}s when user changes target
 * guideline and/or metrics.
 */
public class GuidelineSelectionChangedEvent extends EventObject {

	private static final long serialVersionUID = 5238204220509765076L;
	private GuidelineHolder guidelineHolder;

	GuidelineSelectionChangedEvent(GuidelineHolder source) {
		super(source);
		guidelineHolder = source;
	}

	/**
	 * Get event source {@link GuidelineHolder}
	 * 
	 * @return event source {@link GuidelineHolder}
	 */
	public GuidelineHolder getGuidelineHolder() {
		return guidelineHolder;
	}

}
