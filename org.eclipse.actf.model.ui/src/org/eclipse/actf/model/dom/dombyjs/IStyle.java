/*******************************************************************************
 * Copyright (c) 2007, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Daisuke SATO - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.dom.dombyjs;

/**
 * IStyle interface defines the simplest methods to be implemented by HTML Style
 * object.
 */
public interface IStyle {
	/**
	 * @param name the style name.
	 * @param value the style value.
	 * @return whether the operation is succeeded or not.
	 */
	boolean put(String name, String value);

	/**
	 * @param name the style name to be obtained.
	 * @return the value of the style. It might be a String, a Number, or a Object.
	 */
	Object get(String name);
}
