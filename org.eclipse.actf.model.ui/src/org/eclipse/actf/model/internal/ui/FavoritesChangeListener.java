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

import java.util.EventListener;

/**
 * 
 */
public interface FavoritesChangeListener extends EventListener {
	/**
	 * This method will be invoked internally if the favorite items are changed
	 * 
	 * @param e
	 *            information about favorite change
	 */
	public void favoritesChanged(FavoritesChangeEvent e);
}
