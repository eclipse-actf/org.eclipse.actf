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
package org.eclipse.actf.model.internal.ui.editor.actions;

import java.util.Map;
import java.util.TreeMap;

import org.eclipse.actf.model.internal.ui.FavoritesChangeEvent;
import org.eclipse.actf.model.internal.ui.FavoritesUtil;
import org.eclipse.actf.model.internal.ui.editor.dialogs.FavoritesArrangeDialog;
import org.eclipse.actf.model.ui.util.ModelServiceMessages;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.ui.IWorkbenchWindow;


public class ArrangeFavoritesAction extends FavoritesAction {

	public static final String ID = ArrangeFavoritesAction.class.getName();

	private IWorkbenchWindow _window;

	public ArrangeFavoritesAction(IWorkbenchWindow window, Map<String, String> favoritesMap) {
		this._window = window;

		setId(ID);
		setText(ModelServiceMessages.MenuConst_ArrangeFavorites);
	}

	public void run() {
		Map<String, String> tmpMap = new TreeMap<String, String>();
		tmpMap.putAll(FavoritesUtil.getFavoritesMap());
		FavoritesArrangeDialog arrangeDialog = new FavoritesArrangeDialog(
				this._window.getShell(), tmpMap);

		int returnCode = arrangeDialog.open();
		if (returnCode == IDialogConstants.OK_ID && arrangeDialog.isModified()) {
			tmpMap = arrangeDialog.getFavoritesMap();
			FavoritesUtil.saveFavoritesMap(tmpMap);
			FavoritesChangeEvent fce = new FavoritesChangeEvent(this, tmpMap);
			fireFavoritesChanged(fce);
		}
	}
}
