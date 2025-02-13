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

package org.eclipse.actf.model.internal.ui.editors.edge;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = Messages.class.getPackageName() + ".messages"; //$NON-NLS-1$
	
	public static String WebBrowserEdgeImpl_HTMLforLocalFileError;
	public static String BrowserSizeMenu_custom;
	public static String CustomSizeDialog_title;
	public static String CustomSizeDialog_info;
	public static String CustomSizeDialog_width;
	public static String CustomSizeDialog_height;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
