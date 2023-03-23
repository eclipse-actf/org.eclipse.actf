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

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import org.eclipse.actf.visualization.eval.IGuidelineItem;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.internal.ui.report.Messages;
import org.eclipse.actf.visualization.internal.ui.report.table.ResultTableViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;

public class GuidelineSubMenu extends MenuManager {

//	private ResultTableViewer _resultTableViewer;

	private TableViewer _tableViewer;

	private Action _dummy;

	public GuidelineSubMenu(ResultTableViewer resultTableViewer) {
		super(Messages.ProblemTable_View_Guideline_16);

//		this._resultTableViewer = resultTableViewer;
		this._tableViewer = resultTableViewer.getTableViewer();

		this._tableViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					@SuppressWarnings("unchecked")
					public void selectionChanged(SelectionChangedEvent arg0) {
						List<IProblemItem> tmpList = ((IStructuredSelection) arg0
								.getSelection()).toList();
						setGuidelineItem(tmpList);
					}
				});

		this._dummy = new Action(Messages.GuidelineSubMenu_0) {
		};
		this._dummy.setEnabled(false);
		add(this._dummy);
	}

	public void setGuidelineItem(List<IProblemItem> target) {
		TreeSet<IGuidelineItem> tmpSet = new TreeSet<IGuidelineItem>(
				new Comparator<IGuidelineItem>() {
					public int compare(IGuidelineItem o1, IGuidelineItem o2) {
						return (o1.toString().compareTo(o2.toString()));// TODO
					}
				});

		for (IProblemItem tmpItem : target) {
			tmpSet.addAll(Arrays.asList(tmpItem.getEvaluationItem()
					.getGuidelines()));
		}

		this.removeAll();

		for (IGuidelineItem tmpItem : tmpSet) {
			if (tmpItem.getUrl() != null && tmpItem.getUrl().length() != 0) {
				// Lowvision-> show all
				if (// _resultTableViewer.isShowAllGuidelineItems()
					// ||
				tmpItem.isEnabled()) {
					add(new ShowGuidelineAction(tmpItem));
				}
			}
		}

		if (this.getItems().length == 0) {
			this.add(this._dummy);
		}
	}

}
