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

import org.eclipse.actf.visualization.ui.IVisualizationView;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;

public class SimulationAction implements IWorkbenchWindowActionDelegate {

	private IWorkbenchWindow _window;

	public void dispose() {
	}

	public void init(IWorkbenchWindow window) {
		this._window = window;
	}

	public void run(IAction action) {
		try {
			IViewPart viewPart = this._window.getActivePage().showView(
					IVisualizationView.ID_LOWVISIONVIEW);
			if (viewPart != null) {
				((IVisualizationView) viewPart).doVisualize();
			}

		} catch (PartInitException pie) {
			pie.printStackTrace();
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

}
