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

package org.eclipse.actf.model.ui.util;

import java.net.URL;

import org.eclipse.ui.IPageService;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IWorkbenchPage;

/**
 * Utility to launch an ACTF Web Browser when a specified perspective is
 * activated.
 */
public class PerspectiveListenerForBrowserLaunch implements
		IPerspectiveListener {

	private static final String BROWSER_ID = "org.eclipse.actf.model.ui.editors.edge.WebBrowserEditor"; //$NON-NLS-1$
	private static String TARGET_URL = "about:blank"; //$NON-NLS-1$

	private String id;

	/**
	 * Set the start page URL. If targetUrl is null, "about:blank" will be used.
	 * 
	 * @param targetUrl
	 *            URL for start page
	 */
	public static void setTargetUrl(URL targetUrl) {
		if (targetUrl != null) {
			TARGET_URL = targetUrl.toString();
		} else {
			TARGET_URL = "about:blank"; //$NON-NLS-1$
		}
	}

	/**
	 * Initialize perspective listener for specified perspective.
	 * 
	 * @param perspectiveID
	 *            target perspective ID
	 * @see IPageService#addPerspectiveListener(IPerspectiveListener)
	 */
	public PerspectiveListenerForBrowserLaunch(String perspectiveID) {
		this.id = perspectiveID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPerspectiveListener#perspectiveActivated(org.eclipse.ui.IWorkbenchPage,
	 *      org.eclipse.ui.IPerspectiveDescriptor)
	 */
	public void perspectiveActivated(IWorkbenchPage page,
			IPerspectiveDescriptor perspective) {
		if (id.equals(perspective.getId())) {
			if (!ModelServiceUtils.activateEditorPart(BROWSER_ID)) {
				ModelServiceUtils.launch(TARGET_URL, BROWSER_ID);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPerspectiveListener#perspectiveChanged(org.eclipse.ui.IWorkbenchPage,
	 *      org.eclipse.ui.IPerspectiveDescriptor, java.lang.String)
	 */
	public void perspectiveChanged(IWorkbenchPage page,
			IPerspectiveDescriptor perspective, String changeId) {
	}

}
