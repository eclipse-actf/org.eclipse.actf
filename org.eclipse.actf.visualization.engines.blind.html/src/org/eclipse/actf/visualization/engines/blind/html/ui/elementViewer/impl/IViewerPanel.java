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

import org.eclipse.actf.ui.util.IDialogConstants;

public interface IViewerPanel {
	final static String TITLE_NAME = "ID/AccessKey/Class/CSS Inspector"; //$NON-NLS-1$

	final static String CLOSE_BUTTON = IDialogConstants.CLOSE;

	final static String ID_TAB_TITLE = "ID"; //$NON-NLS-1$

	final static String ACCESSKEY_TAB_TITLE = "AccessKey"; //$NON-NLS-1$

	final static String CLASS_TAB_TITLE = "Class"; //$NON-NLS-1$

	final static String CSS_TAB_TITLE = "CSS"; //$NON-NLS-1$

	public abstract void asyncUpdateValue(VisualizeStyleInfo styleInfo);

	public abstract void forceActive();

	public abstract void hide();

	public boolean isDisposed();
}
