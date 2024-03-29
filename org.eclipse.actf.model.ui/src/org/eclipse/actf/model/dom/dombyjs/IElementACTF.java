/*******************************************************************************
 * Copyright (c) 2022, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.dom.dombyjs;

import org.w3c.dom.Element;

/**
 * The extended interface of the {@link Element}
 */
public interface IElementACTF extends Element {
	/**
	 * Highlight the element.
	 */
	void highlight();

	/**
	 * Clear highlight of the element.
	 * 
	 */
	void unhighlight();
}
