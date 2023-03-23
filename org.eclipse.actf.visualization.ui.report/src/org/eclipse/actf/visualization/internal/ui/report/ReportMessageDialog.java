/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.ui.report;

import org.eclipse.actf.util.FileUtils;
import org.eclipse.actf.visualization.eval.guideline.GuidelineHolder;
import org.eclipse.actf.visualization.eval.guideline.IGuidelineData;
import org.eclipse.actf.visualization.eval.problem.IProblemConst;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class ReportMessageDialog {
	public static void openReportMEssageDialog(Shell shell, IProblemItem curItem) {
		if(shell==null||curItem==null)return;
		
		StringBuffer tmpSB = new StringBuffer();

		if(Platform.inDevelopmentMode()){
			tmpSB.append(curItem.getId()+FileUtils.LINE_SEP+FileUtils.LINE_SEP);
		}		
		
		IGuidelineData[] dataArray = GuidelineHolder.getInstance()
				.getGuidelineData();
		for (int i = 0; i < dataArray.length; i++) {
			if (dataArray[i].isMatched()) {
				tmpSB.append(dataArray[i].getGuidelineName() + ": "
						+ curItem.getTableDataGuideline()[i]
						+ FileUtils.LINE_SEP + FileUtils.LINE_SEP);
			}
		}
		tmpSB.append(IProblemConst.TITLE_TECHNIQUS + ": "
				+ curItem.getEvaluationItem().getTableDataTechniques()
				+ FileUtils.LINE_SEP + FileUtils.LINE_SEP);
		String lineS = curItem.getLineStrMulti();
		if (lineS != null && lineS.length() > 0) {
			tmpSB.append(IProblemConst.TITLE_LINE + ": " + lineS
					+ FileUtils.LINE_SEP + FileUtils.LINE_SEP);
		}
		tmpSB.append(IProblemConst.TITLE_DESCRIPTION + ": "
				+ FileUtils.LINE_SEP + curItem.getDescription());

		switch (curItem.getSeverity()) {
		case IProblemItem.SEV_ERROR:
			MessageDialog.openError(shell, IProblemConst.ESSENTIAL,
					tmpSB.toString());
			break;
		case IProblemItem.SEV_WARNING:
			MessageDialog.openWarning(shell, IProblemConst.WARNING,
					tmpSB.toString());
			break;
		case IProblemItem.SEV_USER:
			MessageDialog.openInformation(shell, IProblemConst.USER_CHECK,
					tmpSB.toString());
			break;
		case IProblemItem.SEV_INFO:
			MessageDialog.openInformation(shell, IProblemConst.INFO,
					tmpSB.toString());
			break;
		}
	}
}
