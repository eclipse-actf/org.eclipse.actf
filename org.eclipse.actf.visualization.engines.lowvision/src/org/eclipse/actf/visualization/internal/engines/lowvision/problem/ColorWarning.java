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

public class ColorWarning extends LowVisionProblem {
	public static final int FONT = 0;
	public static final int BACKGROUND = 1;
	public static final int BOTH = 2;
	public static final int OPACITY = 3;

	private static String getDescriptionFromType(int type) {
		switch (type) {
		case FONT:
			return Messages.FontAlphaWarning;
		case BACKGROUND:
			return Messages.BackgroundAlphaWarning;
		case BOTH:
			return Messages.BothAlphaWarning;
		case OPACITY:
			return Messages.BothAlphaWarning + " (opacity)";
		}
		return "";
	}

	int warningType = BOTH;

	public ColorWarning(int _warningType, PageComponent _pc, LowVisionType _lvType) throws LowVisionProblemException {
		super(LOWVISION_COLOR_WITH_ALPHA_WARNING, _lvType, getDescriptionFromType(_warningType), _pc, 0);
		warningType = _warningType;
		setRecommendations();
	}

	public ColorWarning(int _warningType, PageElement _pe, LowVisionType _lvType) throws LowVisionProblemException {
		super(LOWVISION_COLOR_WITH_ALPHA_WARNING, _lvType, getDescriptionFromType(_warningType), _pe, 0);
		warningType = _warningType;
		setRecommendations();
	}

	protected void setRecommendations() throws LowVisionProblemException {
		recommendations = new LowVisionRecommendation[0];
	}

	public int getWarningType() {
		if(warningType == OPACITY){
			//use message/errorId for BothAlphaWarning
			return BOTH;
		}
		return warningType;
	}
}
