/*******************************************************************************
 * Copyright (c) 2004, 2024 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Norimasa HAYASHIDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.blind.ui.views;

import java.util.HashSet;

import org.eclipse.actf.mediator.MediatorEvent;
import org.eclipse.actf.ui.util.AbstractPartListener;
import org.eclipse.actf.ui.util.PlatformUIUtil;
import org.eclipse.actf.visualization.blind.ui.actions.BlindSaveAction;
import org.eclipse.actf.visualization.blind.ui.actions.BlindVisualizationAction;
import org.eclipse.actf.visualization.blind.ui.internal.PartControlBlind;
import org.eclipse.actf.visualization.blind.ui.internal.SelectionListenerBlind;
import org.eclipse.actf.visualization.engines.blind.html.ui.actions.BlindOpenIdCssAction;
import org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer.ElementViewerManagerFactory;
import org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer.IElementViewerManager;
import org.eclipse.actf.visualization.engines.blind.ui.actions.BlindSettingAction;
import org.eclipse.actf.visualization.engines.blind.ui.actions.BlindVisualizationBrowserModeAction;
import org.eclipse.actf.visualization.eval.EvaluationUtil;
import org.eclipse.actf.visualization.ui.IVisualizationView;
import org.eclipse.actf.visualization.ui.VisualizationStatusLineContributionItem;
import org.eclipse.actf.visualization.ui.report.table.ResultTableLabelProvider;
import org.eclipse.actf.visualization.ui.report.table.ResultTableSorter;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IPageListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

public class BlindView extends ViewPart implements IVisualizationView {

	private IBaseLabelProvider baseLabelProvider;

	private ResultTableSorter viewerSorter;

	private IElementViewerManager elementViewerManager;

	private PartControlBlind partRightBlind;

	private HashSet<IWorkbenchPage> pageSet = new HashSet<IWorkbenchPage>();

	private BlindVisualizationBrowserModeAction browserVisualizaton;

	private IToolBarManager toolbarManager;

	private boolean isFirst = true;

	private org.eclipse.jface.util.IPropertyChangeListener propChecngeListener;

	public BlindView() {
		super();
		baseLabelProvider = new ResultTableLabelProvider();
		viewerSorter = new ResultTableSorter();
		elementViewerManager = ElementViewerManagerFactory.getInstance();
	}

	public void init(IViewSite site) throws PartInitException {
		setSite(site);
		setStatusLine();
	}

	public void createPartControl(Composite parent) {
		partRightBlind = new PartControlBlind(this, parent);

		// TODO use mediator
		getSite().getPage().addSelectionListener(IVisualizationView.DETAILED_REPROT_VIEW_ID,
				new SelectionListenerBlind(partRightBlind));

		// for element viewer
		elementViewerManager.setHighlightElementListener(partRightBlind);
		addPartListener();

		// prepare actions

		IActionBars bars = getViewSite().getActionBars();
		// IMenuManager menuManager = bars.getMenuManager();
		toolbarManager = bars.getToolBarManager();

		browserVisualizaton = new BlindVisualizationBrowserModeAction();

		toolbarManager.add(new BlindVisualizationAction(partRightBlind));
		toolbarManager.add(browserVisualizaton);
		toolbarManager.add(new Separator("target"));
		toolbarManager.add(new Separator("settings"));
		toolbarManager.add(new BlindSettingAction());
		toolbarManager.add(new BlindSaveAction(partRightBlind));
		toolbarManager.add(new BlindOpenIdCssAction());

		toolbarManager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	public void setFocus() {
	}

	public void setStatusMessage(String statusMessage) {
		IContributionItem[] items = getViewSite().getActionBars().getStatusLineManager().getItems();
		for (int i = 0; i < items.length; i++) {
			if (null != items[i] && items[i].getId()
					.equals(VisualizationStatusLineContributionItem.ID + IVisualizationView.ID_BLINDVIEW)) {
				((VisualizationStatusLineContributionItem) items[i]).setStatusMessage(statusMessage);
			}
		}
	}

	public void setInfoMessage(String infoMessage) {
		IContributionItem[] items = getViewSite().getActionBars().getStatusLineManager().getItems();
		for (int i = 0; i < items.length; i++) {
			if (null != items[i] && items[i].getId()
					.equals(VisualizationStatusLineContributionItem.ID + IVisualizationView.ID_BLINDVIEW)) {
				((VisualizationStatusLineContributionItem) items[i]).setInfoMessage(infoMessage);
			}
		}
	}

	private void initPage(IWorkbenchPage page) {
		if (pageSet.add(page)) {
			page.addPartListener(new AbstractPartListener() {
				public void partActivated(IWorkbenchPartReference partRef) {
					IWorkbenchPart part = partRef.getPart(false);
					if (part instanceof IVisualizationView) {
						if (part.equals(BlindView.this)) {
							elementViewerManager.activateElementViewer();
						} else {
							elementViewerManager.hideElementViewer();
						}

					}
				}

				@Override
				public void partVisible(IWorkbenchPartReference partRef) {
					IWorkbenchPart part = partRef.getPart(false);
					if (part instanceof IVisualizationView) {
						if (part.equals(BlindView.this)) {
							elementViewerManager.activateElementViewer();
							if (null != toolbarManager) {
								IContributionItem items[] = toolbarManager.getItems();
								for (IContributionItem tmpItem : items) {
									if ("org.eclipse.actf.visualization.internal.eval.ui.actions.TargetDocumentChangeAction"
											.equals(tmpItem.getId())) { // $NON-NLS-1$
										try {
											IAction tmpAction = ((ActionContributionItem) tmpItem).getAction();
											if (null != tmpAction && isFirst) {
												isFirst = false;
												EvaluationUtil.updateAction(tmpAction);
												propChecngeListener = EvaluationUtil
														.addTargetDocumentChangeActionListner(tmpAction);
											}
										} catch (Exception e) {
										}
									}
								}

							}
						}
					}
				}
			});
		}
	}

	private void addPartListener() {
		IWorkbenchWindow activeWindow = PlatformUIUtil.getActiveWindow();

		activeWindow.addPageListener(new IPageListener() {

			public void pageActivated(IWorkbenchPage page) {
			}

			public void pageClosed(IWorkbenchPage page) {
				pageSet.remove(page);
			}

			public void pageOpened(IWorkbenchPage page) {
				initPage(page);
			}

		});
		IWorkbenchPage activePage = PlatformUIUtil.getActivePage();
		if (activePage != null) {
			initPage(activePage);
		}
	}

	private void setStatusLine() {
		getViewSite().getActionBars().getStatusLineManager()
				.add(new VisualizationStatusLineContributionItem(IVisualizationView.ID_BLINDVIEW));
	}

	public IBaseLabelProvider getLabelProvider() {
		return baseLabelProvider;
	}

	public ViewerSorter getTableSorter() {
		viewerSorter.reset();
		return viewerSorter;
	}

	public int getResultTableMode() {
		return MODE_DEFAULT;
	}

	public void doVisualize() {
		if (partRightBlind != null) {
			partRightBlind.doVisualize();
		}
	}

	public void modelserviceChanged(MediatorEvent event) {
		if (partRightBlind.isBrowserModeSupported(event.getModelServiceHolder())) {
			browserVisualizaton.setEnabled(true);
		} else {
			browserVisualizaton.setEnabled(false);
		}
	}

	public void modelserviceInputChanged(MediatorEvent event) {
		// do nothing
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

	@Override
	public void dispose() {
		EvaluationUtil.removeTargetDocumentChangeActionListner(propChecngeListener);
		super.dispose();
	}

}
