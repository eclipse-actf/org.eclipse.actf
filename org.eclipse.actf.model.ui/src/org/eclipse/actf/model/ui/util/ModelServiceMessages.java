/*******************************************************************************
 * Copyright (c) 2007, 2024 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.model.ui.util;

import org.eclipse.osgi.util.NLS;

public final class ModelServiceMessages extends NLS {

	private static final String BUNDLE_NAME = "messages";//$NON-NLS-1$

	private ModelServiceMessages() {
		// Do not instantiate
	}

	public static String MenuConst_BrowserFreeSize;
	public static String MenuConst_BrowserSize;
	public static String MenuConst_Zoom;
	public static String MenuConst__Font_2;
	public static String MenuConst_Largest_3;
	public static String MenuConst_Large_4;
	public static String MenuConst_Medium_5;
	public static String MenuConst_Small_6;
	public static String MenuConst_Smallest_7;
	public static String MenuConst_F_avorites_8;
	public static String MenuConst_AddFavorites;
	public static String MenuConst_ArrangeFavorites;
	public static String WebBrowser_Address;
	public static String WebBrowser_Backward_4;
	public static String WebBrowser_Forward_5;
	public static String WebBrowser_Stop;
	public static String WebBrowser_Refresh;
	public static String WebBrowser_Script;
	public static String WebBrowser_Go;
	public static String WebBrowser_Backward_4_tp;
	public static String WebBrowser_Forward_5_tp;
	public static String WebBrowser_Stop_tp;
	public static String WebBrowser_Refresh_tp;
	public static String WebBrowser_Script_tp;
	public static String WebBrowser_Go_tp;
	public static String DialogArrangeFavorite_Title;
	public static String DialogArrangeFavorite_Modify_Name;
	public static String DialogArrangeFavorite_Delete;
	public static String DialogAddFavorite_Title;
	public static String DialogAddFavorite_Name_Label;
	public static String DialogAddFavorite_Text_Empty_Alert;
	public static String DialogAddFavorite_Text_Exist_Alert;
	public static String DialogConst_Name_Label;
	public static String ImageCreator_ImageTooLarge;

	static {
		NLS.initializeMessages(BUNDLE_NAME, ModelServiceMessages.class);
	}
}