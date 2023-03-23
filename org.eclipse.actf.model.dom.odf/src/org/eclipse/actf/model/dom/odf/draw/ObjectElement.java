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

import org.eclipse.actf.model.dom.odf.base.EmbedDrawingObjectElement;

/**
 * Interface for &lt;draw:object&gt; element.
 */
public interface ObjectElement extends EmbedDrawingObjectElement {
	/**
	 * Return draw:notify-on-update-of-ranges attribute
	 * 
	 * @return String
	 */		
	public String getAttrDrawNotifyOnUpdateOfRanges();

	/**
	 * Set xlink:href attribute
	 * 
	 * @param href
	 *            xlink:href attribute
	 */	
	public void setAttrXlinkHref(String href);

	/**
	 * Return xlink:href attribute
	 * 
	 * @return String
	 */	
	public String getAttrXlinkHref();

	/**
	 * Return xlink:type attribute
	 * 
	 * @return String
	 */	
	public String getAttrXlinkType();

	/**
	 * Return xlink:show attribute
	 * 
	 * @return String
	 */	
	public String getAttrXlinkShow();

	/**
	 * Return xlink:actuate attribute
	 * 
	 * @return String
	 */	
	public String getAttrXlinkActuate();

	/**
	 * Return true if this object is embedded spreadsheet in presentation document
	 * 
	 * @return boolean
	 */	
	public boolean isPresentationTable();
}