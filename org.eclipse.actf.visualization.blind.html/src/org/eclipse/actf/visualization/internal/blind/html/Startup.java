/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.blind.html;

import org.eclipse.actf.model.ui.util.PerspectiveListenerForBrowserLaunch;
import org.eclipse.actf.visualization.ui.IVisualizationPerspective;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class Startup implements IStartup {

	public void earlyStartup() {

		// can't use activeWindow in startup phase
		IWorkbenchWindow[] windows = PlatformUI.getWorkbench()
				.getWorkbenchWindows();
		for (IWorkbenchWindow window : windows) {
			addListener(window);
		}

		PlatformUI.getWorkbench().addWindowListener(new IWindowListener() {
			public void windowActivated(IWorkbenchWindow window) {
			}

			public void windowClosed(IWorkbenchWindow window) {
			}

			public void windowDeactivated(IWorkbenchWindow window) {
			}

			public void windowOpened(IWorkbenchWindow window) {
				addListener(window);
			}

		});
	}

	private void addListener(IWorkbenchWindow window) {
		final PerspectiveListenerForBrowserLaunch listener = new PerspectiveListenerForBrowserLaunch(
				IVisualizationPerspective.ID_HTML_PERSPECTIVE);
		window.addPerspectiveListener(listener);
		final IWorkbenchPage page = window.getActivePage();
		if (page != null) {
			window.getShell().getDisplay().asyncExec(new Runnable() {
				public void run() {
					listener.perspectiveActivated(page, page.getPerspective());
				}
			});
		}
	}

}
