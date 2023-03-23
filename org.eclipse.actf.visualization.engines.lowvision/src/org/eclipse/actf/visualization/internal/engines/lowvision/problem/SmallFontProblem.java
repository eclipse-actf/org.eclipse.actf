/*******************************************************************************
 * Copyright (c) 2004, 2020 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Junji MAEDA - initial API and implementation
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.engines.lowvision.problem;

import org.eclipse.actf.visualization.engines.lowvision.LowVisionType;
import org.eclipse.actf.visualization.internal.engines.lowvision.Messages;
import org.eclipse.actf.visualization.internal.engines.lowvision.PageElement;

/*
 * detected by using HTML DOM
 */
public class SmallFontProblem extends LowVisionProblem {

	public static final double SEVERITY_SMALL_FONT = 0.25;

	public SmallFontProblem(PageElement _pe, LowVisionType _lvType) throws LowVisionProblemException {
		super(LOWVISION_SMALL_FONT_PROBLEM, _lvType, Messages.SmallFontProblem_This_text_is_too_small__1, _pe,
				SEVERITY_SMALL_FONT);
		// fontSize = _pe.getFontSize();
		setRecommendations();
	}

	protected void setRecommendations() throws LowVisionProblemException {
		recommendations = new LowVisionRecommendation[1];
		recommendations[0] = new EnlargeTextRecommendation(this);
	}
}
