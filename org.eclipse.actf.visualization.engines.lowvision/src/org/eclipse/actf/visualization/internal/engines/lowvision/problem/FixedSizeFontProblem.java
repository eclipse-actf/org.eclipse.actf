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

import org.eclipse.actf.util.logging.DebugPrintUtil;
import org.eclipse.actf.visualization.engines.lowvision.LowVisionType;
import org.eclipse.actf.visualization.internal.engines.lowvision.Messages;
import org.eclipse.actf.visualization.internal.engines.lowvision.PageElement;

public class FixedSizeFontProblem extends LowVisionProblem {

	// severity for fixed size font (0-1)
	public static final double SEVERITY_FIXED_SMALL_FONT = 1.0;
	public static final double SEVERITY_FIXED_SIZE_FONT = 0.75;

	private static String getDescriptionFromType(int type) {
		switch (type) {
		case LOWVISION_FIXED_SIZE_FONT_PROBLEM:
			return Messages.FixedSizeFontProblem_Fixed_size_font_is_used__1;
		case LOWVISION_FIXED_SIZE_FONT_WARNING:
			return Messages.FixedSizeFontWarning;
		case LOWVISION_FIXED_SMALL_FONT_PROBLEM:
			return Messages.FixedSmallFontProblem_This_text_is_too_small_and_its_font_size_is_fixed__1;
		case LOWVISION_FIXED_SMALL_FONT_WARNIG:
			return Messages.FixedSmallFontWarning;
		default:
			DebugPrintUtil.devOrDebugPrintln("undefined type");
			return "";
		}
	}

	private static double getProbabilityFromType(int type) {
		switch (type) {
		case LOWVISION_FIXED_SIZE_FONT_PROBLEM:
			return SEVERITY_FIXED_SIZE_FONT;
		case LOWVISION_FIXED_SIZE_FONT_WARNING:
			return 0;
		case LOWVISION_FIXED_SMALL_FONT_PROBLEM:
			return SEVERITY_FIXED_SMALL_FONT;
		case LOWVISION_FIXED_SMALL_FONT_WARNIG:
			return SEVERITY_FIXED_SMALL_FONT / 2;
		default:
			DebugPrintUtil.devOrDebugPrintln("undefined type");
			return 0;
		}
	}

	private String attrName = "";

	public FixedSizeFontProblem(int _problemType, PageElement _pe, LowVisionType _lvType)
			throws LowVisionProblemException {
		super(_problemType, _lvType, getDescriptionFromType(_problemType), _pe, getProbabilityFromType(_problemType));
		setRecommendations();
	}

	protected void setRecommendations() throws LowVisionProblemException {
		setRecommendations(this.getLowVisionProblemType());
	}

	private void setRecommendations(int type) throws LowVisionProblemException {
		switch (type) {
		case LOWVISION_FIXED_SIZE_FONT_PROBLEM:
		case LOWVISION_FIXED_SIZE_FONT_WARNING:
			recommendations = new LowVisionRecommendation[1];
			recommendations[0] = new ChangableFontRecommendation(this);
			break;
		case LOWVISION_FIXED_SMALL_FONT_PROBLEM:
		case LOWVISION_FIXED_SMALL_FONT_WARNIG:
			recommendations = new LowVisionRecommendation[2];
			recommendations[0] = new EnlargeTextRecommendation(this);
			recommendations[1] = new ChangableFontRecommendation(this);
			break;
		default:
			DebugPrintUtil.devOrDebugPrintln("undefined type");
			recommendations = new LowVisionRecommendation[0];
		}
	}

	public void changeType(int type) throws LowVisionProblemException {
		switch (type) {
		case LOWVISION_FIXED_SIZE_FONT_PROBLEM:
		case LOWVISION_FIXED_SIZE_FONT_WARNING:
		case LOWVISION_FIXED_SMALL_FONT_PROBLEM:
		case LOWVISION_FIXED_SMALL_FONT_WARNIG:
			this.problemType = type;
			this.description = getDescriptionFromType(type);
			this.probability = getProbabilityFromType(type);
			setRecommendations(type);
			break;
		default:
			DebugPrintUtil.devOrDebugPrintln("changeType: undefined type");
		}

	}

	public void setAttrName(String _attrName) {
		if (_attrName == null || _attrName.isEmpty()) {
			attrName = "";
		} else {
			this.description = getDescriptionFromType(this.getLowVisionProblemType()) + " (" + _attrName + ")";
			attrName = _attrName;
		}
	}

	public String getAttrName() {
		return attrName;
	}

}
