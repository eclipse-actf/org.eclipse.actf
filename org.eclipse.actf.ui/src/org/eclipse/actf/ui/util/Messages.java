/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.ui.util;

import org.eclipse.osgi.util.NLS;

public final class Messages extends NLS {

	private static final String BUNDLE_NAME = "messages";//$NON-NLS-1$

	private Messages() {
		// Do not instantiate
	}

	public static String DialogConst_OK;
	public static String DialogConst_Cancel;
	public static String DialogConst_None;
	public static String DialogConst_Help;
	public static String DialogConst_Add;
	public static String DialogConst_Delete;
	public static String DialogConst_Close;
	public static String DialogConst_Browse;
	public static String DialogConst_OpenFile;
	public static String MenuConst__Display_1;
	public static String MenuConst_Save;
	public static String MenuConst_Settings;
	public static String Tooltip_Save;
	public static String Tooltip_Settings;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}