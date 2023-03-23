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

package org.eclipse.actf.visualization.ui.report.views;

import org.eclipse.actf.mediator.IACTFReportViewer;
import org.eclipse.actf.mediator.MediatorEvent;
import org.eclipse.actf.visualization.eval.EvaluationResultImpl;
import org.eclipse.actf.visualization.eval.IEvaluationResult;
import org.eclipse.actf.visualization.internal.ui.report.SummaryProblemReportArea;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class SummaryReportView extends ViewPart implements IACTFReportViewer {

	public static final String ID = SummaryReportView.class.getName();
	
	private IEvaluationResult curResult = null;

	private static final IEvaluationResult dummyResult = new EvaluationResultImpl();

	private SummaryProblemReportArea problemArea;

	public SummaryReportView() {
		super();
	}

	public void createPartControl(Composite parent) {
		problemArea = new SummaryProblemReportArea(parent, SWT.NONE);
	}

	public void setFocus() {

	}

	public void modelserviceChanged(MediatorEvent event) {
		updateEvaluationResult(event);
	}

	public void modelserviceInputChanged(MediatorEvent event) {
		updateEvaluationResult(event);
	}

	public void reportChanged(MediatorEvent event) {
		updateEvaluationResult(event);
	}

	public void reportGeneratorChanged(MediatorEvent event) {
		updateEvaluationResult(event);
	}

	private void updateEvaluationResult(MediatorEvent event) {
		IEvaluationResult tmpResult = dummyResult;
		if (event.getReport() instanceof IEvaluationResult) {
			tmpResult = (IEvaluationResult) event.getReport();
		}

		if (curResult != tmpResult) {
			problemArea.setEvaluationResult(tmpResult);
			curResult = tmpResult;
		}

	}

}
