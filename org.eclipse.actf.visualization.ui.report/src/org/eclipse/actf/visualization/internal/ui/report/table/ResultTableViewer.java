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
package org.eclipse.actf.visualization.internal.ui.report.table;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.actf.model.ui.IModelService;
import org.eclipse.actf.visualization.eval.IEvaluationResult;
import org.eclipse.actf.visualization.eval.guideline.GuidelineHolder;
import org.eclipse.actf.visualization.eval.guideline.GuidelineSelectionChangedEvent;
import org.eclipse.actf.visualization.eval.guideline.IGuidelineData;
import org.eclipse.actf.visualization.eval.guideline.IGuidelineSlectionChangedListener;
import org.eclipse.actf.visualization.eval.problem.HighlightTargetSourceInfo;
import org.eclipse.actf.visualization.eval.problem.IProblemConst;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.internal.ui.report.ReportMessageDialog;
import org.eclipse.actf.visualization.internal.ui.report.action.ClearSelectionAction;
import org.eclipse.actf.visualization.internal.ui.report.action.CopyAction;
import org.eclipse.actf.visualization.internal.ui.report.action.GuidelineSubMenu;
import org.eclipse.actf.visualization.internal.ui.report.action.ShowDescriptionAction;
import org.eclipse.actf.visualization.internal.ui.report.action.SrcHighlightAction;
import org.eclipse.actf.visualization.internal.ui.report.action.TechniquesSubMenu;
import org.eclipse.actf.visualization.internal.ui.report.srcviewer.SrcViewerForPT;
import org.eclipse.actf.visualization.ui.IVisualizationView;
import org.eclipse.actf.visualization.ui.report.table.IResultTableSorter;
import org.eclipse.actf.visualization.ui.report.table.ResultTableSorter;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class ResultTableViewer implements IGuidelineSlectionChangedListener {

	private class ColumnSelectionAdapter extends SelectionAdapter {
		private int column;

		public ColumnSelectionAdapter(int column) {
			this.column = column;
		}

		public void widgetSelected(SelectionEvent arg0) {
			tableViewer.setSelection(null);
			if (tableSorter instanceof IResultTableSorter) {
				((IResultTableSorter) tableSorter).setCurColumn(column);
			}
			tableViewer.refresh();
		}
	}

	private GuidelineHolder guidelineHolder = GuidelineHolder.getInstance();

	private int mode = IVisualizationView.MODE_DEFAULT;

	private Table table;

	private ViewerSorter tableSorter = new ResultTableSorter();

	private ResultTableFilter tableFilter;

	private TableViewer tableViewer;

	private SrcViewerForPT srcViewerForPT;

	private int curMode = -1;

	private File currentSoruceFile;

	private SrcHighlightAction srcHighlightAction;

	private boolean isShowAllGuidelineItems = false;
	
	private Shell shell;

	public ResultTableViewer(Composite parent) {

		shell = parent.getShell();
		
		tableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION
				| SWT.MULTI);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		srcViewerForPT = SrcViewerForPT.initSrcViewerForPT(parent.getShell());

		initColumns();

		changeColumnLayout();

		tableViewer.setContentProvider(new ArrayContentProvider());
		tableFilter = new ResultTableFilter();
		tableViewer.addFilter(tableFilter);

		MenuManager popupMenu = new MenuManager();
		popupMenu.add(new ClearSelectionAction(tableViewer));
		popupMenu.add(new GuidelineSubMenu(this));
		popupMenu.add(new TechniquesSubMenu(this));
		srcHighlightAction = new SrcHighlightAction(this);
		srcHighlightAction.setEnabled(false);
		popupMenu.add(srcHighlightAction);
		popupMenu.add(new CopyAction(this));
		popupMenu.add(new ShowDescriptionAction(tableViewer));
		table.setMenu(popupMenu.createContextMenu(table));

		guidelineHolder.addGuidelineSelectionChangedListener(this);

		tableViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						ISelection selection = event.getSelection();
						if (srcViewerForPT != null
								&& selection instanceof IStructuredSelection) {
							ArrayList<HighlightTargetSourceInfo> srcLineArray = new ArrayList<HighlightTargetSourceInfo>();
							for (@SuppressWarnings("unchecked")
							Iterator<IProblemItem> i = ((IStructuredSelection) selection)
									.iterator(); i.hasNext();) {
								IProblemItem tmpItem = i.next();
								srcLineArray.addAll(Arrays.asList(tmpItem
										.getHighlightTargetSoruceInfo()));
							}

							HighlightTargetSourceInfo[] target = new HighlightTargetSourceInfo[srcLineArray
									.size()];
							srcLineArray.toArray(target);
							srcViewerForPT.highlightSrcViewer(target,
									getCurrentSoruceFile());
						}
					}
				});
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				ISelection selection = event.getSelection();
				if (selection instanceof IStructuredSelection) {
					for (@SuppressWarnings("unchecked")
					Iterator<IProblemItem> i = ((IStructuredSelection) selection)
							.iterator(); i.hasNext();) {
						IProblemItem tmpItem = i.next();
						ReportMessageDialog.openReportMEssageDialog(shell, tmpItem);
					}
				}
			}
		});
	}

	public TableViewer getTableViewer() {
		return tableViewer;
	}

	private void changeColumnLayout() {
		srcViewerForPT.setSrcChanged(true);
		switch (mode) {
		case IVisualizationView.MODE_LOWVISION:
			initLVMode();
			// srcViewerForPT.updateSrcViewer(mode);
			break;
		case IVisualizationView.MODE_DEFAULT:
		default:
			initDefaultMode();
			// srcViewerForPT.updateSrcViewer(mode);
			break;
		}
	}

	public void refresh() {
		// change checker setting
		// TODO
		tableViewer.refresh();
	}

	private void initColumns() {
		int columnSize = 4 + guidelineHolder.getGuidelineData().length
				+ guidelineHolder.getMetricsNames().length;
		int lvColumn = 4 + 6 + guidelineHolder.getGuidelineData().length;

		if (lvColumn > columnSize) {
			columnSize = lvColumn;
		}

		for (int i = 0; i < columnSize; i++) {
			TableColumn column = new TableColumn(table, SWT.LEFT);
			column.addSelectionListener(new ColumnSelectionAdapter(i));
		}

	}

	private void initDefaultMode() {
		// table.clearAll();

		TableColumn[] columns = table.getColumns();
		int curPos = 0;

		for (int i = 0; i < columns.length; i++) {
			columns[i].setImage(null);
			columns[i].setResizable(true);
		}

		columns[curPos].setText(IProblemConst.TITLE_HIGHLIGHT);
		columns[curPos].setWidth(25);
		curPos++;

		String[] tmpSarray = guidelineHolder.getLocalizedMetricsNames();
		boolean[] enabledMetrics = guidelineHolder.getMatchedMetrics();
		for (int i = 0; i < tmpSarray.length; i++) {
			columns[curPos].setText(tmpSarray[i]);
			/*
			 * switch (i % 5) { case 0: columns[curPos].setImage(ReportPlugin
			 * .imageDescriptorFromPlugin(ReportPlugin.PLUGIN_ID,
			 * "icons/IconRed.gif").createImage()); break; case 1:
			 * columns[curPos].setImage(ReportPlugin
			 * .imageDescriptorFromPlugin(ReportPlugin.PLUGIN_ID,
			 * "icons/IconYellow.gif").createImage()); break; case 2:
			 * columns[curPos].setImage(ReportPlugin
			 * .imageDescriptorFromPlugin(ReportPlugin.PLUGIN_ID,
			 * "icons/IconBlue.gif").createImage()); break; case 3:
			 * columns[curPos].setImage(ReportPlugin
			 * .imageDescriptorFromPlugin(ReportPlugin.PLUGIN_ID,
			 * "icons/IconGreen.gif").createImage()); break; case 4:
			 * columns[curPos].setImage(ReportPlugin
			 * .imageDescriptorFromPlugin(ReportPlugin.PLUGIN_ID,
			 * "icons/IconPink.gif").createImage()); break; default: }
			 */
			if (enabledMetrics[i]) {
				columns[curPos].setWidth(55);
			} else {
				columns[curPos].setWidth(0);
				columns[curPos].setResizable(false);
			}
			curPos++;
		}

		IGuidelineData[] dataArray = guidelineHolder.getGuidelineData();
		for (int i = 0; i < dataArray.length; i++) {
			columns[curPos].setText(dataArray[i].getGuidelineName());
			if (dataArray[i].isMatched()) {
				columns[curPos].setWidth(80);
			} else {
				columns[curPos].setWidth(0);
				columns[curPos].setResizable(false);
			}
			curPos++;
		}

		columns[curPos].setText(IProblemConst.TITLE_TECHNIQUS);
		columns[curPos].setWidth(65);
		curPos++;

		columns[curPos].setText(IProblemConst.TITLE_LINE);
		columns[curPos].setWidth(60);
		curPos++;

		columns[curPos].setText(IProblemConst.TITLE_DESCRIPTION);
		columns[curPos].setWidth(1000);
		curPos++;

		for (int i = curPos; i < columns.length; i++) {
			columns[i].setWidth(0);
			columns[i].setResizable(false);
		}

	}

	private void initLVMode() {

		TableColumn[] columns = table.getColumns();
		int curPos = 0;

		for (int i = 0; i < columns.length; i++) {
			columns[i].setImage(null);
			columns[i].setResizable(true);
		}

		columns[curPos].setText(IProblemConst.TITLE_ICON);
		columns[curPos].setWidth(45);
		curPos++;

		IGuidelineData[] dataArray = guidelineHolder.getGuidelineData();
		for (int i = 0; i < dataArray.length; i++) {
			columns[curPos].setText(dataArray[i].getGuidelineName());
			if (dataArray[i].isMatched()) {
				columns[curPos].setWidth(70);
			} else {
				columns[curPos].setWidth(0);
				columns[curPos].setResizable(false);
			}
			curPos++;
		}

		columns[curPos].setText(IProblemConst.TITLE_SEVERITY);
		columns[curPos].setWidth(55);
		curPos++;
		columns[curPos].setText(IProblemConst.TITLE_FORECOLOR);
		columns[curPos].setWidth(70);
		curPos++;
		columns[curPos].setText(IProblemConst.TITLE_BACKCOLOR);
		columns[curPos].setWidth(70);
		curPos++;
		columns[curPos].setText(IProblemConst.TITLE_X);
		columns[curPos].setWidth(50);
		curPos++;
		columns[curPos].setText(IProblemConst.TITLE_Y);
		columns[curPos].setWidth(50);
		curPos++;
		columns[curPos].setText(IProblemConst.TITLE_AREA);
		columns[curPos].setWidth(50);
		curPos++;

		columns[curPos].setText(IProblemConst.TITLE_TECHNIQUS);
		columns[curPos].setWidth(60);
		curPos++;

		columns[curPos].setText(IProblemConst.TITLE_DESCRIPTION);
		columns[curPos].setWidth(1000);
		curPos++;

		for (int i = curPos; i < columns.length; i++) {
			columns[i].setWidth(0);
			columns[i].setResizable(false);
		}

		// tableViewer.setLabelProvider(new ResultTableLabelProviderLV());
		// tableViewer.setSorter(tableSorterLV);

	}

	public void changeFilterType(int type) {
		tableFilter.setSeverity(type);
		refresh();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.actf.visualization.eval.guideline.
	 * IGuidelineSlectionChangedListener
	 * #selectionChanged(org.eclipse.actf.visualization
	 * .eval.guideline.GuidelineSelectionChangedEvent)
	 */
	public void selectionChanged(GuidelineSelectionChangedEvent e) {
		changeColumnLayout();
		// tableFilter.setEnabledMetrics(guidelineHolder.getEnabledMetrics());
		refresh();
	}

	public void setResult(IVisualizationView vizView, IEvaluationResult result) {
		if (vizView == null) {
			// TODO
		} else {
			mode = vizView.getResultTableMode();

			currentSoruceFile = result.getSourceFile();
			isShowAllGuidelineItems = result.isShowAllGuidelineItems();
			tableSorter = vizView.getTableSorter();

			if (curMode != vizView.getResultTableMode()) {
				tableViewer.setInput(new Vector<IProblemItem>());
				changeColumnLayout();
			}

			tableViewer.setLabelProvider(vizView.getLabelProvider());
			tableViewer.setSorter(tableSorter);

			tableViewer.setInput(result.getProblemList());

			// tableSorter.reset();
			// tableSorterLV.reset();

			srcViewerForPT.setSrcChanged(true);
			// TODO return boolean
			srcViewerForPT.updateSrcViewer(result.getSourceFile());
			srcHighlightAction.setEnabled(result.getSourceFile() != null);
		}

	}

	public File getCurrentSoruceFile() {
		return currentSoruceFile;
	}

	public boolean isShowAllGuidelineItems() {
		return isShowAllGuidelineItems;
	}

	public void setModelService(IModelService modelService) {
	}

}
