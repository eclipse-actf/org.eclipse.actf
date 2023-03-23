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
package org.eclipse.actf.visualization.internal.ui.report.action;

import java.util.List;

import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.eval.problem.IProblemItemImage;
import org.eclipse.actf.visualization.eval.problem.ReportUtil;
import org.eclipse.actf.visualization.internal.ui.report.Messages;
import org.eclipse.actf.visualization.internal.ui.report.table.ResultTableViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

public class CopyAction extends Action {

	private TableViewer tableViewer;

	public CopyAction(ResultTableViewer resultTableViewer) {
		super(Messages.Copy);

		tableViewer = resultTableViewer.getTableViewer();
		tableViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent arg0) {
						@SuppressWarnings("rawtypes")
						List tmpList = ((IStructuredSelection) arg0
								.getSelection()).toList();
						if (tmpList == null || tmpList.size() == 0) {
							setIProblemItem(null);
						} else {
							try {
								setIProblemItem((IProblemItem) tmpList.get(0));
							} catch (Exception e) {
								setIProblemItem(null);
							}
						}
					}
				});

		this.setEnabled(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {

		@SuppressWarnings("unchecked")
		List<IProblemItem> targetList = ((IStructuredSelection) tableViewer
				.getSelection()).toList();

		ReportUtil reportUtil = new ReportUtil();
		reportUtil.setMode(ReportUtil.TAB);
		StringBuffer tmpSB = new StringBuffer(reportUtil.getFirstLine()
				+ ReportUtil.LINE_SEP);

		for (IProblemItem tmpItem : targetList) {
			tmpSB.append(reportUtil.toString(tmpItem) + ReportUtil.LINE_SEP);
		}

		Clipboard clipboard = new Clipboard(tableViewer.getControl()
				.getDisplay());
		clipboard.setContents(new Object[] { tmpSB.toString() },
				new Transfer[] { TextTransfer.getInstance() });

	}

	public void setIProblemItem(IProblemItem target) {
		if (target == null) {
			this.setEnabled(false);
		} else {
			if (target instanceof IProblemItemImage) {
				// TODO CSV export function for lv
				this.setEnabled(false);
			} else {
				this.setEnabled(true);
			}
		}
	}
}
