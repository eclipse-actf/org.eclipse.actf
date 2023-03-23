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
package org.eclipse.actf.model.internal.ui;

import java.util.EventObject;
import java.util.Map;



public class FavoritesChangeEvent extends EventObject {

	private static final long serialVersionUID = 7558042163036173736L;

	private Map<String, String> _favoritesMap;
	
	public FavoritesChangeEvent(Object obj, Map<String, String> favoritesMap) {
		super(obj);
		setFavoritesMap(favoritesMap);
	}

	public void setFavoritesMap(Map<String, String> favoritesMap) {
		this._favoritesMap = favoritesMap;
	}

	public Map<String, String> getFavoritesMap() {
		return this._favoritesMap;
	}
	
}
