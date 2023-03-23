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

import java.util.Vector;

import org.eclipse.actf.model.internal.ui.FavoritesChangeEvent;
import org.eclipse.actf.model.internal.ui.FavoritesChangeListener;
import org.eclipse.jface.action.Action;

abstract public class FavoritesAction extends Action {

	private Vector<FavoritesChangeListener> _favoritesChangeListeners = new Vector<FavoritesChangeListener>();

	public void addFavoritesChangeListener(FavoritesChangeListener fcl) {
		this._favoritesChangeListeners.addElement(fcl);
	}

	public void removeFavoritesChangeListener(FavoritesChangeListener fcl) {
		this._favoritesChangeListeners.removeElement(fcl);
	}

	protected void fireFavoritesChanged(FavoritesChangeEvent fce) {
		if (this._favoritesChangeListeners.size() > 0) {
			for (int i = 0; i < this._favoritesChangeListeners.size(); i++) {
				this._favoritesChangeListeners.elementAt(i).favoritesChanged(
						fce);
			}
		}
	}
}
