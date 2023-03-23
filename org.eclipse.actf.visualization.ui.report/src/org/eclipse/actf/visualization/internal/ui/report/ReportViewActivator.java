/*******************************************************************************
 * Copyright (c) 2009, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.ui.report;

import org.eclipse.actf.mediator.IACTFReportViewer;
import org.eclipse.actf.mediator.IMediatorEventListener;
import org.eclipse.actf.mediator.MediatorEvent;
import org.eclipse.actf.ui.util.PlatformUIUtil;
import org.eclipse.actf.visualization.eval.IEvaluationResult;
import org.eclipse.actf.visualization.ui.report.views.SummaryReportView;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;

public class ReportViewActivator implements IMediatorEventListener {

	private IEvaluationResult curResult = null;

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
		if (event.getReport() instanceof IEvaluationResult) {
			IEvaluationResult tmpResult = (IEvaluationResult) event.getReport();

			IWorkbenchPage page = PlatformUIUtil.getActivePage();
			if (curResult != tmpResult && page != null) {
				IViewPart view = page.findView(SummaryReportView.ID);
				if (view != null) {
					IViewPart[] views = page.getViewStack(view);
					if (views != null && !(views[0] instanceof IACTFReportViewer)) {
						page.activate(view);
						if (event.getView() != null) {
							page.activate(event.getView());
						}
					}
				}
			}
		}

	}
}
