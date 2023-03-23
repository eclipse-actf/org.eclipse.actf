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
 * Interface for &lt;draw:hatch&gt; element.
 */
public interface HatchElement extends ODFElement {
	/**
	 * Return draw:name attribute
	 * 
	 * @return String
	 */	
	public String getAttrDrawName();

	/**
	 * Return draw:display-name attribute
	 * 
	 * @return String
	 */	
	public String getAttrDrawDisplayName();

	/**
	 * Return draw:style attribute
	 * 
	 * @return String
	 */	
	public String getAttrDrawStyle();

	/**
	 * Return draw:color attribute
	 * 
	 * @return String
	 */	
	public String getAttrDrawColor();

	/**
	 * Return draw:distance attribute
	 * 
	 * @return String
	 */	
	public String getAttrDrawDistance();
	
	/**
	 * Return draw:rotation attribute
	 * 
	 * @return String
	 */	
	public String getAttrDrawRotation();
}