/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.ui.util;

import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.ui.dialogs.PreferencesUtil;

/**
 * Utility class to launch preference page.
 */
public class PreferenceUtils {

	private static PreferenceDialog _preferenceDialog = null;

	/**
	 * Open a workbench preference dialog.
	 */
	public static void openPreferenceDialog() {
		openPreferenceDialog(null);
	}

	/**
	 * Open a workbench preference dialog and selects particular preference
	 * page. If there is already a preference dialog open this dialog is used
	 * and its selection is set to the page with id preferencePageId. Show the
	 * other pages as filtered results using whatever filtering criteria the
	 * search uses.
	 * 
	 * @param selectedPreferencePageId
	 *            target preference page ID
	 */
	public static void openPreferenceDialog(String selectedPreferencePageId) {
		_preferenceDialog = PreferencesUtil.createPreferenceDialogOn(null,
				selectedPreferencePageId, null, null);
		_preferenceDialog.open();
	}

	/**
	 * Close the preference dialog launched by using this utility.
	 */
	public static void close() {
		if (null != _preferenceDialog) {
			_preferenceDialog.close();
			_preferenceDialog = null;
		}
	}
}
