/*******************************************************************************
 * Copyright (c) 2010, 2016 Ministry of Internal Affairs and Communications (MIC),
 * IBM Corporation and Others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Yasuharu GOTOU (MIC) - initial API and implementation
 *    Kentarou FUKUDA (IBM) - [383882] - Eclipse 4.2 adaptation
 *******************************************************************************/
package org.eclipse.actf.examples.michecker;

import org.eclipse.actf.model.ui.util.PerspectiveListenerForBrowserLaunch;
import org.eclipse.actf.ui.util.PlatformUIUtil;
import org.eclipse.actf.visualization.eval.guideline.GuidelineHolder;
import org.eclipse.actf.visualization.eval.guideline.IGuidelineData;
import org.eclipse.actf.visualization.ui.IVisualizationView;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	private static final String NOT_FIRST = "notFirst";

	public ApplicationWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	public void preWindowOpen() {

		PlatformUI.getPreferenceStore().setValue(
				IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS,
				false);

		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setShowCoolBar(true);
		configurer.setShowMenuBar(true);
		configurer.setShowStatusLine(true);
		configurer.setTitle(MiCheckerPlugin.getResourceString("window.title")); //$NON-NLS-1$

		// Show perspective name on title
		configurer.getWindow().addPerspectiveListener(
				new IPerspectiveListener() {
					public void perspectiveActivated(IWorkbenchPage page,
							IPerspectiveDescriptor perspective) {
						getWindowConfigurer()
								.setTitle(
										perspective.getLabel()
												+ " - " + MiCheckerPlugin.getResourceString("window.title")); //$NON-NLS-1$ //$NON-NLS-2$
					}

					public void perspectiveChanged(IWorkbenchPage page,
							IPerspectiveDescriptor perspective, String changeId) {
					}
				});

		PerspectiveListenerForBrowserLaunch
				.setTargetUrl(PlatformUI
						.getWorkbench()
						.getHelpSystem()
						.resolve(
								"/org.eclipse.actf.examples.michecker.doc/docs/readme1st.html", //$NON-NLS-1$
								true));

	}

	@SuppressWarnings("nls")
	public void postWindowOpen() {
		// remove search and run menus
		IMenuManager menuManager = getWindowConfigurer()
				.getActionBarConfigurer().getMenuManager();
		IContributionItem[] items = menuManager.getItems();
		for (int i = 0; i < items.length; i++) {
			if (null != items[i].getId()
					&& (items[i].getId().equals("org.eclipse.search.menu") || items[i]
							.getId().equals("org.eclipse.ui.run"))) {
				items[i].dispose();
			}
		}

		// hide quick access (for Eclipse 4.2.x)
		/*
		try {
			IWorkbenchWindow window = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow();
			MWindow model = ((WorkbenchWindow) window).getModel();
			EModelService modelService = model.getContext().get(
					EModelService.class);
			modelService.find("SearchField", model).setToBeRendered(false);
		} catch (Exception e) {

		}
		*/

		PreferenceManager prefManager = getWindowConfigurer()
				.getWorkbenchConfigurer().getWorkbench().getPreferenceManager();
		for (IPreferenceNode node : prefManager.getRootSubNodes()) {
			if ("org.eclipse.actf.ui.preferences.RootPreferencePage"
					.equals(node.getId())) {
				node.remove("org.eclipse.actf.util.vocab.preferences.VocabPreferencePage");
				node.remove("org.eclipse.actf.ai.voice.preferences.VoicePreferencePage");
			}
		}

		IPreferenceStore prefStore = MiCheckerPlugin.getDefault()
				.getPreferenceStore();

		if (!prefStore.getBoolean(NOT_FIRST)) {
			GuidelineHolder gh = GuidelineHolder.getInstance();
			IGuidelineData[] guidelines = gh.getLeafGuidelineData();
			boolean[] enabledItems = new boolean[guidelines.length];
			for (int i = 0; i < guidelines.length; i++) {
				IGuidelineData gData = guidelines[i];
				enabledItems[i] = false;
				if ("JIS".equalsIgnoreCase(gData.getGuidelineName())
						&& !"AAA".equalsIgnoreCase(gData.getLevelStr())) {
					enabledItems[i] = true;
				}
			}
			gh.setEnabledGuidelineWithLevels(enabledItems);

			prefStore.setValue(NOT_FIRST, true);
		}

		PlatformUIUtil.showView(IVisualizationView.DETAILED_REPROT_VIEW_ID);
	}

}
