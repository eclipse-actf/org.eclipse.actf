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
package org.eclipse.actf.model.dom.odf.base;

import org.eclipse.actf.model.dom.odf.svg.DescElement;
import org.eclipse.actf.model.dom.odf.svg.TitleElement;

/**
 * All graphics ODF elements should implement this interface.
 */
public interface DrawingObjectBaseElement extends ODFElement {
	/**
	 * Return &lt;svg:title&gt; element
	 * 
	 * @return TitleElement
	 */	
	public TitleElement getSVGTitleElement();

	/**
	 * Return &lt;svg:desc&gt; element
	 * 
	 * @return DescElement
	 */	
	public DescElement getSVGDescElement();

	/**
	 * Return short description element
	 * &lt;svg:desc&gt; element for ODF 1.0
	 * &lt;svg:title&gt; element for ODF later than 1.0
	 * 
	 * @return ODFElement
	 */		
	public ODFElement getShortDescElement();

	/**
	 * Return long description element
	 * &lt;svg:desc&gt; element for ODF later than 1.0
	 * For ODF 1.0, this function always returns null
	 * 
	 * @return ODFElement
	 */	
	public ODFElement getLongDescElement();

	/**
	 * Return short description element
	 * &lt;svg:desc&gt; element for ODF 1.0
	 * &lt;svg:title&gt; element for ODF later than 1.0
	 *
	 * @param version
	 *            version of ODF
	 * @return ODFElement
	 */		
	public ODFElement getShortDescElement(double version);

	/**
	 * Return long description element
	 * &lt;svg:desc&gt; element for ODF later than 1.0
	 * For ODF 1.0, this function always returns null
	 * 
	 * @param version
	 *            version of ODF
	 * @return ODFElement
	 */	
	public ODFElement getLongDescElement(double version);
}