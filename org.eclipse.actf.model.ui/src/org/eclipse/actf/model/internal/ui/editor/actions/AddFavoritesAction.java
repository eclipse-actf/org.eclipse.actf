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

import org.eclipse.actf.model.internal.ui.FavoritesChangeEvent;
import org.eclipse.actf.model.internal.ui.FavoritesUtil;
import org.eclipse.actf.model.internal.ui.editor.dialogs.FavoritesAddDialog;
import org.eclipse.actf.model.ui.IModelService;
import org.eclipse.actf.model.ui.editor.browser.IWebBrowserACTF;
import org.eclipse.actf.model.ui.util.ModelServiceMessages;
import org.eclipse.actf.model.ui.util.ModelServiceUtils;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * Action to add location of the current active {@link IModelService} as a
 * favorites
 */
public class AddFavoritesAction extends FavoritesAction {

	public static final String ID = AddFavoritesAction.class.getName();

	private IWorkbenchWindow _window;

	public AddFavoritesAction(IWorkbenchWindow window,
			Map<String, String> favoritesMap) {
		this._window = window;

		setId(ID);
		setText(ModelServiceMessages.MenuConst_AddFavorites); 
	}

	public void run() {
		if (null != this._window) {

			IModelService modelService = ModelServiceUtils
					.getActiveModelService();
			if (modelService == null) {
				return;
			}

			String title = modelService.getURL();
			if (modelService instanceof IWebBrowserACTF) {
				title = ((IWebBrowserACTF) modelService).getLocationName();
			}
			Map<String, String> tmpMap = FavoritesUtil.getFavoritesMap();
			FavoritesAddDialog addDialog = new FavoritesAddDialog(this._window
					.getShell(), tmpMap, title);

			int ret = addDialog.open();
			if (ret == IDialogConstants.OK_ID) {
				String url = modelService.getURL();
				tmpMap.put(addDialog.getName(), url);
				FavoritesUtil.saveFavoritesMap(tmpMap);
				FavoritesChangeEvent fce = new FavoritesChangeEvent(this,
						tmpMap);
				fireFavoritesChanged(fce);
			}
		}
	}
}
