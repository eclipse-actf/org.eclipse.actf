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
package org.eclipse.actf.visualization.internal.ui.report.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.actf.visualization.eval.problem.HighlightTargetSourceInfo;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.internal.ui.report.Messages;
import org.eclipse.actf.visualization.internal.ui.report.srcviewer.SrcViewerForPT;
import org.eclipse.actf.visualization.internal.ui.report.table.ResultTableViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

public class SrcHighlightAction extends Action {

	private ResultTableViewer _resultTableViewer;

	private TableViewer _tableView;

	private SrcViewerForPT _srcViewer;

	public SrcHighlightAction(ResultTableViewer resultTableViewer) {
		super(Messages.ProblemTable_5);
		this._resultTableViewer = resultTableViewer;
		this._tableView = resultTableViewer.getTableViewer();
		this._srcViewer = SrcViewerForPT.getInstance();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {

		if (_srcViewer != null) {

			_srcViewer.openSrcViewer();

			@SuppressWarnings("unchecked")
			List<IProblemItem> targetList = ((IStructuredSelection) _tableView
					.getSelection()).toList();
			ArrayList<HighlightTargetSourceInfo> srcLineArray = new ArrayList<HighlightTargetSourceInfo>();

			for (IProblemItem tmpItem : targetList) {
				srcLineArray.addAll(Arrays.asList(tmpItem
						.getHighlightTargetSoruceInfo()));
			}

			HighlightTargetSourceInfo[] target = new HighlightTargetSourceInfo[srcLineArray
					.size()];
			srcLineArray.toArray(target);
			_srcViewer.highlightSrcViewer(target,
					_resultTableViewer.getCurrentSoruceFile());

		}
	}

}
