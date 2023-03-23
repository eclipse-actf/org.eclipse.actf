/*******************************************************************************
 * Copyright (c) 2006, 2009 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Norimasa HAYASHIDA - initial API and implementation
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.blind.html.ui;

import org.eclipse.actf.visualization.ui.IVisualizationPerspective;
import org.eclipse.actf.visualization.ui.IVisualizationView;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.IViewRegistry;

@SuppressWarnings("nls")
public class HTMLPerspective implements IPerspectiveFactory,
		IVisualizationPerspective {

	private static final String RESOURCE_FOLDER = "actf.html.resource.folder";

	private static final String VIEW_FOLDER = "actf.html.visualization.view.folder";

	private static final String REPORT_FOLDER = "actf.html.report.view.folder";

	private static final String RESOURCE_NAVIGATOR = "org.eclipse.ui.views.ResourceNavigator";
	private static final String PACKAGE_EXPLORER = "org.eclipse.jdt.ui.PackageExplorer";

	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(true);

		// layout info: moved to fragment XML
		// initializer: moved to Startup.java

		String editorArea = layout.getEditorArea();

		// check existence of Resource Navigator/Package Explorer
		// (do not add place holder in the case of RCP)
		IWorkbench workbench = PlatformUI.getWorkbench();
		boolean hasResourceNavigator = false;
		boolean hasPackageExplorer = false;
		if (workbench != null) {
			IViewRegistry viewReg = workbench.getViewRegistry();
			hasResourceNavigator = (viewReg.find(RESOURCE_NAVIGATOR) != null);
			hasPackageExplorer = (viewReg.find(PACKAGE_EXPLORER) != null);
		}

		if (hasResourceNavigator) {
			IFolderLayout resourceFolder = layout.createFolder(RESOURCE_FOLDER,
					IPageLayout.LEFT, 0.2f, editorArea);
			resourceFolder.addView(RESOURCE_NAVIGATOR);
			if (hasPackageExplorer) {
				resourceFolder.addPlaceholder(PACKAGE_EXPLORER);
			}
		} else if (hasPackageExplorer) {
			IFolderLayout resourceFolder = layout.createFolder(RESOURCE_FOLDER,
					IPageLayout.LEFT, 0.2f, editorArea);
			resourceFolder.addView(PACKAGE_EXPLORER);
		}

		IFolderLayout reportFolder = layout.createFolder(REPORT_FOLDER,
				IPageLayout.BOTTOM, 0.7f, editorArea);
		reportFolder.addView(IVisualizationView.SUMMARY_REPORT_VIEW_ID);
		reportFolder.addView(IVisualizationView.DETAILED_REPROT_VIEW_ID);
		
		IFolderLayout viewFolder = layout.createFolder(VIEW_FOLDER,
				IPageLayout.RIGHT, 0.5f, editorArea);
		viewFolder.addView(IVisualizationView.ID_BLINDVIEW);
		viewFolder.addView(IVisualizationView.ID_LOWVISIONVIEW);
		
		

	}

}
