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

package org.eclipse.actf.visualization.ui.report.views;

import org.eclipse.actf.mediator.IACTFReportViewer;
import org.eclipse.actf.mediator.MediatorEvent;
import org.eclipse.actf.visualization.eval.EvaluationResultImpl;
import org.eclipse.actf.visualization.eval.IEvaluationResult;
import org.eclipse.actf.visualization.internal.ui.report.DetailProblemReportArea;
import org.eclipse.actf.visualization.ui.IVisualizationView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 *
 */
public class DetailedReportView extends ViewPart implements
		IACTFReportViewer {

	private IEvaluationResult curResult = null;

	private static final IEvaluationResult dummyResult = new EvaluationResultImpl();

	private DetailProblemReportArea problemArea;

	/**
	 * 
	 */
	public DetailedReportView() {
		super();
	}

	public void createPartControl(Composite parent) {
		problemArea = new DetailProblemReportArea(parent, SWT.NONE);
		getSite().setSelectionProvider(
				problemArea.getProblemTree().getResultTableViewer()
						.getTableViewer());
	}

	public void setFocus() {
	}

	public void modelserviceChanged(MediatorEvent event) {
		problemArea.setModelService(event.getModelServiceHolder()
				.getModelService());
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
			IVisualizationView view = null;
			if (event.getView() instanceof IVisualizationView) {
				view = (IVisualizationView) event.getView();
			}

			problemArea.setResult(view, tmpResult);
			curResult = tmpResult;
		}

	}

}
