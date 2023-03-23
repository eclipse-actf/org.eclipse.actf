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

package org.eclipse.actf.visualization.lowvision.eval;

import java.util.List;

import org.eclipse.actf.ui.util.HighlightStringListener;
import org.eclipse.actf.util.FileUtils;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.eval.problem.IProblemItemImage;
import org.eclipse.actf.visualization.lowvision.ui.internal.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class SummaryEvaluationLV {

	public static HighlightStringListener getHighLightStringListener() {
		HighlightStringListener hlsl = new HighlightStringListener();
		Color blue = Display.getDefault().getSystemColor(SWT.COLOR_BLUE);
		Color red = Display.getDefault().getSystemColor(SWT.COLOR_RED);

		if (Display.getDefault().getHighContrast()) {
			blue = null;
			red = null;
		}

		hlsl.addTarget(Messages.EvalLV_no_color_difficult_distinguish, blue,
				SWT.BOLD);
		hlsl.addTarget(Messages.EvalLV_font_might_enough_to_read, blue,
				SWT.BOLD);

		hlsl.addTarget(Messages.EvalLV_color_difficult_distinguish, red,
				SWT.BOLD);
		hlsl.addTarget(Messages.EvalLV_color_might_difficult_distinguish, red,
				SWT.BOLD);
		hlsl.addTarget(Messages.EvalLV_font_too_small_to_read, red, SWT.BOLD);
		hlsl.addTarget(Messages.EvalLV_font_might_too_small_to_read, red,
				SWT.BOLD);
		hlsl.addTarget(Messages.EvalLV_page_have_fixed_font, red, SWT.BOLD);

		hlsl.addTarget(Messages.EvalLV_0, null, SWT.BOLD);
		// TODO

		return (hlsl);
	}

	private IProblemItemImage[] _problems = new IProblemItemImage[0];

	public SummaryEvaluationLV(List<IProblemItem> problemList) {
		try {
			_problems = new IProblemItemImage[problemList.size()];
			problemList.toArray(_problems);
		} catch (Exception e) {
			_problems = new IProblemItemImage[0];
		}
	}

	public static String notSupported() {
		return (Messages.EvalLV_0);
	}

	public String getOverview() {
		StringBuffer tmpSB = new StringBuffer(512);

		int problemCount[] = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		int severeCount[] = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

		for (int i = 0; i < _problems.length; i++) {
			IProblemItemImage curProblem = _problems[i];
			problemCount[curProblem.getSubType()]++;
			if (curProblem.getSeverityLV() > 49) {
				severeCount[curProblem.getSubType()]++;
			}
		}

		// for(int i=0;i<problemCount.length;i++){
		// System.out.print(problemCount[i]+"/"+severeCount[i]+", ");
		// }

		if (problemCount[1] > 0 || problemCount[3] > 0) {
			if (severeCount[1] > 0 || severeCount[3] > 0) {
				tmpSB.append(Messages.EvalLV_color_difficult_distinguish
						+ FileUtils.LINE_SEP);
			} else {
				tmpSB.append(Messages.EvalLV_color_might_difficult_distinguish
						+ FileUtils.LINE_SEP);
			}
			tmpSB.append(Messages.EvalLV_click_detailed_report
					+ FileUtils.LINE_SEP);

			tmpSB.append(FileUtils.LINE_SEP);
			if (problemCount[1] > 0) {
				tmpSB.append(Messages.EvalLV_text_color_combination
						+ " " + problemCount[1] + FileUtils.LINE_SEP); //$NON-NLS-1$ 
			}
			if (problemCount[3] > 0) {
				tmpSB.append(Messages.EvalLV_img_color_combination
						+ " " + problemCount[3] + FileUtils.LINE_SEP); //$NON-NLS-1$ 
			}
		} else {
			tmpSB.append(Messages.EvalLV_no_color_difficult_distinguish
					+ FileUtils.LINE_SEP);
		}

		tmpSB.append(FileUtils.LINE_SEP);

		if (problemCount[5] > 0 || problemCount[6] > 0) {
			if (severeCount[5] > 0 || severeCount[6] > 0) {
				tmpSB.append(Messages.EvalLV_font_too_small_to_read
						+ FileUtils.LINE_SEP);
			} else {
				tmpSB.append(Messages.EvalLV_font_might_too_small_to_read
						+ FileUtils.LINE_SEP);
			}
			tmpSB.append(Messages.EvalLV_click_detailed_report
					+ FileUtils.LINE_SEP);

			tmpSB.append(FileUtils.LINE_SEP);
			if (problemCount[5] > 0) {
				tmpSB.append(Messages.EvalLV_font_too_small + " " //$NON-NLS-1$
						+ problemCount[5]);
			}
			if (problemCount[6] > 0) {
				tmpSB.append(Messages.EvalLV_font_too_small_fixed
						+ " " + problemCount[6]); //$NON-NLS-1$ 
			}
		} else if (problemCount[4] > 0) {
			tmpSB.append(Messages.EvalLV_page_have_fixed_font
					+ FileUtils.LINE_SEP);
			tmpSB.append(FileUtils.LINE_SEP);
			tmpSB.append(Messages.EvalLV_font_fixed + " " + problemCount[4]); //$NON-NLS-1$
		} else {
			tmpSB.append(Messages.EvalLV_font_might_enough_to_read
					+ FileUtils.LINE_SEP);
		}

		return (tmpSB.toString());
	}

}
