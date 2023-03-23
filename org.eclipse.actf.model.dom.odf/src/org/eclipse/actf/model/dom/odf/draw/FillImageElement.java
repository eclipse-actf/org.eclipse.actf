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
package org.eclipse.actf.model.dom.odf.draw;

import org.eclipse.actf.model.dom.odf.base.ODFElement;

/**
 * Interface for &lt;draw:fill-image&gt; element.
 */
public interface FillImageElement extends ODFElement {
	/**
	 * Return draw:name attribute
	 * 
	 * @return String
	 */		
	public String getAttrDrawName();

	/**
	 * Return xlink:href attribute
	 * 
	 * @return String
	 */		
	public String getAttrXLinkHref();

	/**
	 * Return xlink:type attribute
	 * 
	 * @return String
	 */	
	public String getAttrXLinkType();

	/**
	 * Return xlink:show attribute
	 * 
	 * @return String
	 */	
	public String getAttrXLinkShow();

	/**
	 * Return xlink:actuate attribute
	 * 
	 * @return String
	 */	
	public String getAttrXLinkActuate();
}