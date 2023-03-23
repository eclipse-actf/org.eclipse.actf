/*******************************************************************************
 * Copyright (c) 2003, 2020 IBM Corporation and Others
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
import org.eclipse.actf.visualization.internal.engines.lowvision.image.PageComponent;

public class BlurProblem extends LowVisionProblem {
	public BlurProblem(PageComponent _pc, LowVisionType _lvType, double _proba)
			throws LowVisionProblemException {
		super(
				LOWVISION_BLUR_PROBLEM,
				_lvType,
				Messages.BlurProblem_It_is_difficult_for_weak_sighted_to_read_these_characters__1,
				_pc, _proba);
		setRecommendations();
	}

	public BlurProblem(PageElement _pe, LowVisionType _lvType, double _proba)
			throws LowVisionProblemException {
		super(
				LOWVISION_BLUR_PROBLEM,
				_lvType,
				Messages.BlurProblem_It_is_difficult_for_weak_sighted_to_read_these_characters__1,
				_pe, _proba);
		setRecommendations();
	}

	protected void setRecommendations() throws LowVisionProblemException {
		recommendations = new LowVisionRecommendation[2];
		recommendations[0] = new EnlargeTextRecommendation(this);
		recommendations[1] = new EnlargeLineRecommendation(this);
	}
}
