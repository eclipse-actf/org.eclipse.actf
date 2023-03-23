/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer.impl;

import org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer.IElementViewerManager;
import org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer.IHighlightElementListener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class ElementViewerManager implements IVisualizeStyleInfoListener,
		IElementViewerManager {

	private IViewerPanel viewerPanel;

	private IHighlightElementListener highlightElementListener;

	private VisualizeStyleInfo styleInfo;

	private Shell shell;

	public ElementViewerManager() {
		shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		VisualizeStyleInfoManager.getInstance().addLisnter(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer.IElementViewerManager#setHighlightElementListener(org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer.IHighlightElementListener)
	 */
	public void setHighlightElementListener(IHighlightElementListener hel) {
		this.highlightElementListener = hel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer.IElementViewerManager#openElementViewer()
	 */
	public void openElementViewer() {
		if (isExist()) {
			viewerPanel.forceActive();
		} else {
			viewerPanel = new ViewerPanelJFace(shell, styleInfo,
					highlightElementListener);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer.IElementViewerManager#activateElementViewer()
	 */
	public void activateElementViewer() {
		if (isExist()) {
			viewerPanel.forceActive();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer.IElementViewerManager#hideElementViewer()
	 */
	public void hideElementViewer() {
		if (isExist()) {
			viewerPanel.hide();
		}
	}

	public void update(VisualizeStyleInfo styleInfo) {
		this.styleInfo = styleInfo;
		if (isExist()) {
			viewerPanel.asyncUpdateValue(styleInfo);
		}
	}

	private boolean isExist() {
		return ((null != viewerPanel) && (!viewerPanel.isDisposed()));
	}

}
