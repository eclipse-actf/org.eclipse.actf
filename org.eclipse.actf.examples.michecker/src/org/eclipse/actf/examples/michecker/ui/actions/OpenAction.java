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

import org.eclipse.actf.examples.michecker.ui.dialogs.URLOpenDialog;
import org.eclipse.actf.model.ui.util.ModelServiceUtils;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class OpenAction implements IWorkbenchWindowActionDelegate {

	private IWorkbenchWindow _window;

	public void dispose() {
	}

	public void init(IWorkbenchWindow window) {
		this._window = window;
	}

	public void run(IAction action) {
		URLOpenDialog openURLDialog = new URLOpenDialog(this._window.getShell());
		if (1 == openURLDialog.open()) {

			String sUrl = openURLDialog.getUrl();

			ModelServiceUtils.launch(sUrl);

		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

}
