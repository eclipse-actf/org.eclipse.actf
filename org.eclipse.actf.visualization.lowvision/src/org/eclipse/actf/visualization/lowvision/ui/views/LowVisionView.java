/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.lowvision.ui.views;

import org.eclipse.actf.mediator.MediatorEvent;
import org.eclipse.actf.model.ui.IModelService;
import org.eclipse.actf.model.ui.IModelServiceScrollManager;
import org.eclipse.actf.visualization.lowvision.ui.actions.LowVisionSaveAction;
import org.eclipse.actf.visualization.lowvision.ui.actions.LowVisionSettingsAction;
import org.eclipse.actf.visualization.lowvision.ui.actions.LowVisionSimulateAction;
import org.eclipse.actf.visualization.lowvision.ui.internal.Messages;
import org.eclipse.actf.visualization.lowvision.ui.internal.PartControlLowVision;
import org.eclipse.actf.visualization.ui.IVisualizationView;
import org.eclipse.actf.visualization.ui.VisualizationStatusLineContributionItem;
import org.eclipse.actf.visualization.ui.report.table.ResultTableLabelProviderLV;
import org.eclipse.actf.visualization.ui.report.table.ResultTableSorterLV;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

public class LowVisionView extends ViewPart implements IVisualizationView {

	private PartControlLowVision partRightLowVision;

	private IBaseLabelProvider baseLabelProvider = new ResultTableLabelProviderLV();
	private ResultTableSorterLV viewerSorter = new ResultTableSorterLV();

	private Action wholepageAction = new Action(Messages.LowVisionView_whole_page_1,
			Action.AS_CHECK_BOX){		
	};

	public LowVisionView() {
		super();
		wholepageAction.setChecked(true);
	}

	public void init(IViewSite site) throws PartInitException {
		setSite(site);
		setStatusLine();
	}

	public void createPartControl(Composite parent) {
		partRightLowVision = new PartControlLowVision(this, parent);
		// TODO
		getSite().getPage().addSelectionListener(
				IVisualizationView.DETAILED_REPROT_VIEW_ID, partRightLowVision);
		// ((IViewSite)getSite()).getActionBars().setGlobalActionHandler("visualizationAction",
		// new Action(){
		// public void run() {
		// partRightLowVision.doSimulate();
		// }
		// }
		// );

		// prepare actions

		IActionBars bars = getViewSite().getActionBars();
		IMenuManager menuManager = bars.getMenuManager();
		IToolBarManager toolbarManager = bars.getToolBarManager();

		toolbarManager.add(new LowVisionSimulateAction(partRightLowVision));
		toolbarManager.add(new LowVisionSettingsAction());
		toolbarManager.add(new LowVisionSaveAction(partRightLowVision));

		toolbarManager
				.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));

		menuManager.add(wholepageAction);
		menuManager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	public void setFocus() {
	}

	public void setStatusMessage(String statusMessage) {
		IContributionItem[] items = getViewSite().getActionBars()
				.getStatusLineManager().getItems();
		for (int i = 0; i < items.length; i++) {
			if (null != items[i]
					&& items[i].getId().equals(
							VisualizationStatusLineContributionItem.ID
									+ IVisualizationView.ID_LOWVISIONVIEW)) {
				((VisualizationStatusLineContributionItem) items[i])
						.setStatusMessage(statusMessage);
			}
		}
	}

	public void setInfoMessage(String infoMessage) {
		IContributionItem[] items = getViewSite().getActionBars()
				.getStatusLineManager().getItems();
		for (int i = 0; i < items.length; i++) {
			if (null != items[i]
					&& items[i].getId().equals(
							VisualizationStatusLineContributionItem.ID
									+ IVisualizationView.ID_LOWVISIONVIEW)) {
				((VisualizationStatusLineContributionItem) items[i])
						.setInfoMessage(infoMessage);
			}
		}
	}

	private void setStatusLine() {
		getViewSite().getActionBars().getStatusLineManager().add(
				new VisualizationStatusLineContributionItem(
						IVisualizationView.ID_LOWVISIONVIEW));
	}

	public IBaseLabelProvider getLabelProvider() {
		return baseLabelProvider;
	}

	public ViewerSorter getTableSorter() {
		viewerSorter.reset();
		return viewerSorter;
	}

	public int getResultTableMode() {
		return MODE_LOWVISION;
	}

	public void doVisualize() {
		if (partRightLowVision != null) {
			partRightLowVision.doSimulate();
		}
	}

	public void modelserviceChanged(MediatorEvent event) {
		setCurrentModelService(event);
	}

	public void modelserviceInputChanged(MediatorEvent event) {
		setCurrentModelService(event);
	}
	
	private void setCurrentModelService(MediatorEvent event){
		IModelService current = event.getModelServiceHolder().getModelService();
		partRightLowVision.setCurrentModelService(current);
		if (null == current) {
			wholepageAction.setEnabled(false);
			return;
		}

		switch (current.getScrollManager().getScrollType()) {
		case IModelServiceScrollManager.ABSOLUTE_COORDINATE:
		case IModelServiceScrollManager.INCREMENTAL:
		case IModelServiceScrollManager.PAGE:
			wholepageAction.setEnabled(true);
			break;
		case IModelServiceScrollManager.NONE:
		default:
			wholepageAction.setEnabled(false);
		}
	}

	public void reportChanged(MediatorEvent event) {
		// TODO implement report update here

	}

	public void reportGeneratorChanged(MediatorEvent event) {
		// do nothing
	}

	public void setVisualizeMode(int mode) {
		// do nothing
	}
	
	public boolean isWholepage(){
		return wholepageAction.isChecked();
	}

}
