/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tatsuya ISHIHARA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.model.dom.odf.number;

import org.eclipse.actf.model.dom.odf.base.ODFElement;

/**
 * Interface for &lt;number:number&gt; element.
 */
public interface NumberElement extends ODFElement {
	/**
	 * Return number:min-integer-digits attribute
	 * 
	 * @return String
	 */			
	public int getAttrNumberMinIntegerDigits();

	/**
	 * Return number:decimal-places attribute
	 * 
	 * @return String
	 */			
	public int getAttrNumberDecimalPlaces();

	/**
	 * Return number:grouping attribute
	 * 
	 * @return String
	 */			
	public boolean getAttrNumberGrouping();
}