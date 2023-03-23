/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and Others
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
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.eclipse.actf.visualization.eval.IEvaluationItem;
import org.eclipse.actf.visualization.eval.IGuidelineItem;
import org.eclipse.actf.visualization.eval.ITechniquesItem;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.internal.ui.report.Messages;
import org.eclipse.actf.visualization.internal.ui.report.table.ResultTableViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;

public class TechniquesSubMenu extends MenuManager {

//	private ResultTableViewer _resultTableViewer;

	private TableViewer _tableViewer;

	private Action _dummy;

	public TechniquesSubMenu(ResultTableViewer resultTableViewer) {
		super(Messages.ViewTechniques);

//		this._resultTableViewer = resultTableViewer;
		this._tableViewer = resultTableViewer.getTableViewer();

		this._tableViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					@SuppressWarnings("unchecked")
					public void selectionChanged(SelectionChangedEvent arg0) {
						List<IProblemItem> tmpList = ((IStructuredSelection) arg0
								.getSelection()).toList();
						setTechniques(tmpList);
					}
				});

		this._dummy = new Action(Messages.NoTechniques) {
		};
		this._dummy.setEnabled(false);
		add(this._dummy);
	}

	public void setTechniques(List<IProblemItem> target) {
		this.removeAll();

		TreeSet<IEvaluationItem> tmpSet = new TreeSet<IEvaluationItem>(
				new Comparator<IEvaluationItem>() {
					public int compare(IEvaluationItem o1, IEvaluationItem o2) {
						return o1.getId().compareTo(o2.getId());
					}
				});

		for (Iterator<IProblemItem> i = target.iterator(); i.hasNext();) {
			IProblemItem tmpItem = i.next();
			tmpSet.add(tmpItem.getEvaluationItem());
		}

		TreeSet<ITechniquesItem> techSet = new TreeSet<ITechniquesItem>(
				new Comparator<ITechniquesItem>() {
					public int compare(ITechniquesItem o1, ITechniquesItem o2) {
						int flag = o1.getGuidelineName().compareTo(
								o2.getGuidelineName());
						if (flag == 0) {
							flag = o1.getId().compareTo(o2.getId());
						}
						return flag;
					}
				});

		for (Iterator<IEvaluationItem> i = tmpSet.iterator(); i.hasNext();) {
			IEvaluationItem eval = i.next();
			IGuidelineItem[] guidelines = eval.getGuidelines();
			ITechniquesItem[][] techs = eval.getTechniques();
			for (int j = 0; j < guidelines.length; j++) {
				// Lowvision-> show all
				if (// _resultTableViewer.isShowAllGuidelineItems()
					// ||
				guidelines[j].isEnabled()) {
					techSet.addAll(Arrays.asList(techs[j]));
				}
			}
		}

		for (Iterator<ITechniquesItem> i = techSet.iterator(); i.hasNext();) {
			add(new ShowTechniquesAction(i.next()));
		}

		if (this.getItems().length == 0) {
			this.add(this._dummy);
		}
	}

}
