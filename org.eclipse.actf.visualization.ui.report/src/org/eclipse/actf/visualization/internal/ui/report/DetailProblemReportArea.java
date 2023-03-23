/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.ui.report;

import org.eclipse.actf.model.ui.IModelService;
import org.eclipse.actf.visualization.eval.IEvaluationResult;
import org.eclipse.actf.visualization.internal.ui.report.table.ResultTableViewer;
import org.eclipse.actf.visualization.ui.IVisualizationView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

public class DetailProblemReportArea extends SashForm {

	private ProblemTree problemTree;
    private ResultTableViewer resultTableViewer;

	public DetailProblemReportArea(Composite parent, int style) {
		super(parent, style);
		init();
	}

	private void init(){		
		setOrientation(SWT.HORIZONTAL);
		
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		gridData.horizontalSpan = 1;
		setLayoutData(gridData);

		this.problemTree = new ProblemTree(this);
		resultTableViewer = new ResultTableViewer(this);
        
		this.problemTree.setResultTableViewer(resultTableViewer);
		
		setWeights(new int[] { 2, 9 });
		
	}
	
    public void setResult(IVisualizationView vizView, IEvaluationResult result) {
        problemTree.setResult(result);
        resultTableViewer.setResult(vizView, result);
    }

    public ProblemTree getProblemTree() {
        return problemTree;
    }

	public void setModelService(IModelService modelService) {
        resultTableViewer.setModelService(modelService);		
	}

}
