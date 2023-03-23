/*******************************************************************************
 * Copyright (c) 2005, 2020 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.engines.lowvision;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.actf.visualization.eval.problem.ILowvisionProblemSubtype;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.ColorProblem;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.ColorWarning;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.ILowVisionProblem;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.LowVisionProblem;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.LowVisionProblemGroup;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.ProblemItemLV;

public class LowVisionProblemConverter {

	public static List<IProblemItem> convert(LowVisionProblemGroup[] target, String urlS, int frameId) {

		ArrayList<IProblemItem> result = new ArrayList<IProblemItem>();

		for (int i = 0; i < target.length; i++) {
			int type = target[i].getLowVisionProblemType();
			ProblemItemLV tmp;
			ColorProblem cp;
			switch (type) {
			case ILowVisionProblem.LOWVISION_COLOR_PROBLEM:
				cp = (ColorProblem) target[i].getRepresentative();
				tmp = new ProblemItemLV("L_" + target[i].getLowVisionProblemType() + "." + cp.getLevel()); //$NON-NLS-1$
				tmp.setTargetNode(cp.getElement());
				tmp.setTargetString(cp.getAdditionalDescription());
				break;
			case ILowVisionProblem.LOWVISION_COLOR_WITH_ALPHA_WARNING:
				ColorWarning cw = (ColorWarning) target[i].getRepresentative();
				tmp = new ProblemItemLV("L_" + target[i].getLowVisionProblemType() + "." + cw.getWarningType()); //$NON-NLS-1$
				break;
			default:
				tmp = new ProblemItemLV("L_" + target[i].getLowVisionProblemType()); //$NON-NLS-1$
			}
			tmp.setSubType(type);
			try {
				switch (type) {
				case ILowvisionProblemSubtype.LOWVISION_BACKGROUND_IMAGE_WARNING:
					cp = (ColorProblem) target[i].getRepresentative();
					tmp.setTargetNode(cp.getElement());
					tmp.setTargetString(cp.getAdditionalDescription());
					break;
				case ILowvisionProblemSubtype.LOWVISION_COLOR_PROBLEM:
					break;
				default:
					tmp.setDescription(target[i].getDescription());
					if (target[i].getRepresentative() != null)
						tmp.setTargetNode(target[i].getRepresentative().getElement());
				}
			} catch (Exception e) {
				tmp.setDescription("unknown"); //$NON-NLS-1$
			}
			tmp.setCanHighlight(true);

			tmp.setFrameId(frameId);
			tmp.setFrameUrl(urlS);

			tmp.setSeverityLV(target[i].getIntProbability());// TODO
			tmp.setForeground(getLVProblemColorString(target[i], true));
			tmp.setBackground(getLVProblemColorString(target[i], false));
			tmp.setX(target[i].getX());
			tmp.setY(target[i].getY());
			tmp.setWidth(target[i].getWidth());
			tmp.setHeight(target[i].getHeight());
			tmp.setArea(target[i].getWidth() * target[i].getHeight());

			// TODO recommendation
			result.add(tmp);
		}

		return (result);
	}

	private static String getLVProblemColorString(LowVisionProblemGroup problem, boolean isFore) {
		int probType;
		int origAll;
		int origR;
		int origG;
		int origB;

		probType = problem.getLowVisionProblemType();

		if (probType == LowVisionProblem.LOWVISION_COLOR_PROBLEM
				|| probType == LowVisionProblem.LOWVISION_BACKGROUND_IMAGE_WARNING) {
			ColorProblem cp = (ColorProblem) (problem.getRepresentative());
			if (isFore) {
				origAll = cp.getForegroundColor();
			} else {
				origAll = cp.getBackgroundColor();
			}
			origR = origAll >> 16 & 0xff;
			origG = origAll >> 8 & 0xff;
			origB = origAll & 0xff;
			return origR + "," + origG + "," + origB; //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			return " "; //$NON-NLS-1$
		}
	}

}
