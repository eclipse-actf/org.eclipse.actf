/*******************************************************************************
 * Copyright (c) 2010, 2011 Ministry of Internal Affairs and Communications (MIC).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Yasuharu GOTOU (MIC) - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.examples.michecker.ui.actions;

import java.net.URL;

import org.eclipse.actf.examples.michecker.MiCheckerPlugin;
import org.eclipse.actf.examples.michecker.internal.Messages;
import org.eclipse.actf.model.ui.IModelService;
import org.eclipse.actf.model.ui.util.ModelServiceUtils;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

public class OpenW3CValidator implements IViewActionDelegate {

	private static final String VALIDATION = "Validation";

	private Shell shell;

	IPreferenceStore prefStore = MiCheckerPlugin.getDefault()
			.getPreferenceStore();

	public void run(IAction action) {
		IWorkbenchBrowserSupport browserSupport = PlatformUI.getWorkbench()
				.getBrowserSupport();

		if (!prefStore.getBoolean(VALIDATION)) {
			try {
				IWebBrowser browser = browserSupport.getExternalBrowser();
				browser.openURL(new URL("http://validator.w3.org/about.html"));
				if (MessageDialog.openConfirm(shell, Messages.Validation_confirmT,
						Messages.Validation_confirm)) {
					prefStore.setValue(VALIDATION, true);
				} else {
					return;
				}
			} catch (Exception e) {
			}
		}

		IModelService ms = ModelServiceUtils.getActiveModelService();

		if (ms == null)
			return;

		String urlS = ms.getURL();
		System.out.println(urlS);
		if (urlS == null)
			return;

		if (urlS.startsWith("file:") || urlS.matches("http://127.0.0.1(:|/).*")) {
			try {
				IWebBrowser browser = browserSupport.getExternalBrowser();
				browser.openURL(new URL(
						"http://validator.w3.org/#validate_by_upload"));
				MessageDialog.openInformation(shell,
						Messages.Validation_localfileT,
						Messages.Validation_localfileM);

			} catch (Exception e) {
			}
			return;
		}

		try {
			IWebBrowser browser = browserSupport.getExternalBrowser();
			browser.openURL(new URL("http://validator.w3.org/check?uri="
					+ ms.getURL()));
		} catch (Exception e) {
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		System.out.println(selection);
	}

	public void init(IViewPart view) {
		shell = view.getViewSite().getShell();
	}

}
