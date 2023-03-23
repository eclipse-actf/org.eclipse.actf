/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Norimasa HAYASHIDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.ui.report;

import org.eclipse.actf.visualization.eval.IEvaluationResult;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

public class SummaryProblemReportArea extends SashForm {

	private StyledText _summaryReportText;

	private ReportDisplay _reportDisplay;

	private LineStyleListener currentStyle;

	public SummaryProblemReportArea(Composite parent, int style) {
		super(parent, style);
		init();
	}

	public void init() {
		setOrientation(SWT.HORIZONTAL);

		GridData gridData = new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL);
		gridData.horizontalSpan = 1;
		setLayoutData(gridData);

		this._summaryReportText = new StyledText(this, SWT.BORDER
				| SWT.V_SCROLL);
		this._summaryReportText.setEditable(false);
		this._summaryReportText.setWordWrap(true);

		this._reportDisplay = new ReportDisplay(this);
		setWeights(new int[] { 2, 1 });

	}

	public void setEvaluationResult(IEvaluationResult result) {
		if (currentStyle != null) {
			_summaryReportText.removeLineStyleListener(currentStyle);
		}
		currentStyle = result.getLineStyleListener();
		this._summaryReportText.addLineStyleListener(currentStyle);
		this._summaryReportText.setText(result.getSummaryReportText());
		this._reportDisplay.displayReportFile(result.getSummaryReportUrl());

	}

}
