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
 * Interface for &lt;draw:gradient&gt; element.
 */
public interface GradientElement extends ODFElement {
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
	 * Return draw:start-color attribute
	 * 
	 * @return String
	 */	
	public String getAttrDrawStartColor();

	/**
	 * Return draw:end-color attribute
	 * 
	 * @return String
	 */	
	public String getAttrDrawEndColor();

	/**
	 * Return draw:cx attribute
	 * 
	 * @return String
	 */	
	public String getAttrDrawCx();

	/**
	 * Return draw:cy attribute
	 * 
	 * @return String
	 */	
	public String getAttrDrawCy();

	/**
	 * Return draw:start-intensity attribute
	 * 
	 * @return String
	 */	
	public String getAttrDrawStartIntensity();

	/**
	 * Return draw:end-intensity attribute
	 * 
	 * @return String
	 */	
	public String getAttrDrawEndIntensity();

	/**
	 * Return draw:angle attribute
	 * 
	 * @return int
	 */	
	public int getAttrDrawAngle();

	/**
	 * Return draw:border attribute
	 * 
	 * @return String
	 */	
	public String getAttrDrawBorder();
}