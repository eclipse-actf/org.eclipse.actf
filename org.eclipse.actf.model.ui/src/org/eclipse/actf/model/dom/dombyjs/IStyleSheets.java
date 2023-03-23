/*******************************************************************************
 * Copyright (c) 2011, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.model.dom.dombyjs;

/**
 * Interface to access a collection of {@link IStyleSheet} objects in the
 * document.
 */
public interface IStyleSheets {

	/**
	 * @return number of {@link IStyleSheet} in a collection
	 */
	int getLength();

	/**
	 * 
	 * @param index
	 *            Index into the collection.
	 * @return The {@link IStyleSheet} at the <code>index</code>th position,
	 *         or <code>null</code> if that is not a valid index.
	 */
	IStyleSheet item(int index);
}
