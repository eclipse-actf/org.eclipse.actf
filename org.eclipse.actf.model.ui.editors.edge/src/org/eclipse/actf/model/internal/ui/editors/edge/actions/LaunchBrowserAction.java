/*******************************************************************************
 * Copyright (c) 2006, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.internal.ui.editors.edge.actions;

import org.eclipse.actf.model.ui.editors.edge.WebBrowserEditor;
import org.eclipse.actf.model.ui.util.ModelServiceUtils;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class LaunchBrowserAction implements IWorkbenchWindowActionDelegate {

	public void dispose() {
	}

	public void init(IWorkbenchWindow window) {
	}

	public void run(IAction action) {
		ModelServiceUtils.launch("about:blank", WebBrowserEditor.ID); //$NON-NLS-1$
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

}
