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
 * IRules interface to access a collection of {@link IRule} objects in the
 * styleSheet.
 */
public interface IRules {

	/**
	 * @return number of {@link IRule} in a collection
	 */
	int getLength();

	/**
	 * 
	 * @param index
	 *            Index into the collection.
	 * @return The {@link IRule} at the <code>index</code>th position, or
	 *         <code>null</code> if that is not a valid index.
	 */
	IRule item(int index);
}
