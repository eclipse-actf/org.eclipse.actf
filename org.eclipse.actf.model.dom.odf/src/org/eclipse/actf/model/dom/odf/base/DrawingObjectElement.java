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

import org.eclipse.actf.model.dom.odf.draw.TextBoxElement;

/**
 * Graphics ODF elements that do not have &lt;draw:frame&gt; as parent should implement this interface.
 */
public interface DrawingObjectElement extends DrawingObjectBaseElement {
	/**
	 * Return draw:caption-id attribute
	 * 
	 * @return String
	 */		
	public String getAttrDrawCaptionId();

	/**
	 * Return height of graphics
	 * 
	 * @return long
	 */		
	long getHeight();

	/**
	 * Return width of graphics
	 * 
	 * @return long
	 */		
	long getWidth();

	/**
	 * Return x position of graphics
	 * 
	 * @return long
	 */		
	long getX();

	/**
	 * Return y position of graphics
	 * 
	 * @return long
	 */		
	long getY();

	/**
	 * Set height of graphics
	 * 
	 * @param height
	 *            height of graphics
	 */		
	void setHeight(long height);

	/**
	 * Set width of graphics
	 * 
	 * @param width
	 *            width of graphics
	 */		
	void setWidth(long width);

	/**
	 * Set x position of graphics
	 * 
	 * @param x
	 *            x position
	 */		
	void setX(long x);

	/**
	 * Set y position of graphics
	 * 
	 * @param y
	 *            y position
	 */		
	void setY(long y);

	/**
	 * Return index of page which have this graphics
	 * 
	 * @return long
	 */		
	long getPageIndex();

	/**
	 * Return z-index of graphics
	 * 
	 * @return long
	 */		
	long getZIndex();

	/**
	 * Return text box element bounded by draw:caption-id attribute.
	 * In ODF 1.0, this function always return null.
	 * 
	 * @return TextBoxElement
	 */		
	public TextBoxElement getBoundCaptionTextBoxElement();

	/**
	 * Return text box element bounded by draw:caption-id attribute
	 * If ODF version is specified as 1.0, this function return null.
	 * 
	 * @param version
	 *            version of ODF
	 * @return TextBoxElement
	 */		
	public TextBoxElement getBoundCaptionTextBoxElement(double version);
}