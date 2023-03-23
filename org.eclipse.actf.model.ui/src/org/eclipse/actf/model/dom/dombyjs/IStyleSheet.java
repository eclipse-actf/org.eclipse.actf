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

import org.w3c.dom.DOMException;

/**
 *	Interface to access styleSheet information.
 */
public interface IStyleSheet {

	/**
	 * @return the URL of the linked style sheet
	 */
	String getHref();

	/**
	 * @return the title of this style sheet.
	 */
	String getTitle();

	/**
	 * @return the {@link IStyleSheet} that imported this style sheet.
	 */
	IStyleSheet getParentStyleSheet();

	/**
	 * @return a collection of imported style sheets from this style sheet.
	 */
	IStyleSheets getImports();

	/**
	 * @return a collection of rules defined in this style sheet.
	 */
	IRules getRules();

	/**
	 * @return the style sheet in text form.
	 */
	String getCssText() throws DOMException;
	
	//TODO
	//ownerNode (IE9)
		
}
