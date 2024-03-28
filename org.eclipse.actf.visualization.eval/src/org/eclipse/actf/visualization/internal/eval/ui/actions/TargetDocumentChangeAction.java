/*******************************************************************************
 * Copyright (c) 2024 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.eval.ui.actions;

import org.eclipse.actf.visualization.eval.EvaluationUtil;
import org.eclipse.actf.visualization.internal.eval.EvaluationPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

public class TargetDocumentChangeAction implements IViewActionDelegate {

	private static ImageDescriptor HTML_IMAGE = EvaluationPlugin.getImageDescriptor("icons/view16/html.png"); //$NON-NLS-1$
	private static ImageDescriptor LIVEDOM_IMAGE = EvaluationPlugin.getImageDescriptor("icons/view16/dom.png");
	private static String SELECT_TARGET = EvaluationPlugin.getResourceString("actf.action.selecttarget"); //$NON-NLS-1$
	private static String SELECT_TARGET_ORG = EvaluationPlugin.getResourceString("actf.action.selecttarget.org"); //$NON-NLS-1$
	private static String SELECT_TARGET_LIVE = EvaluationPlugin.getResourceString("actf.action.selecttarget.live"); //$NON-NLS-1$

	@Override
	public void run(IAction action) {
		if (action.isChecked()) {
			if (EvaluationUtil.isOriginalDOM()) {
				EvaluationUtil.setOriginalDOM(false);
			}
		} else {
			if (!EvaluationUtil.isOriginalDOM()) {
				EvaluationUtil.setOriginalDOM(true);
			}
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

	@Override
	public void init(IViewPart view) {
	}

	public static void updateAction(IAction action) {
		if (EvaluationUtil.isOriginalDOM()) {
			action.setChecked(false);
			action.setImageDescriptor(HTML_IMAGE);
			action.setDescription(SELECT_TARGET + SELECT_TARGET_ORG);
			action.setToolTipText(SELECT_TARGET + SELECT_TARGET_ORG);
		} else {
			action.setChecked(true);
			action.setImageDescriptor(LIVEDOM_IMAGE);
			action.setDescription(SELECT_TARGET + SELECT_TARGET_LIVE);
			action.setToolTipText(SELECT_TARGET + SELECT_TARGET_LIVE);
		}
	}

}
