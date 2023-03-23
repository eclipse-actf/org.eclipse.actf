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

import java.util.List;

import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.presentation.NotesElement;
import org.w3c.dom.NodeList;

/**
 * Interface for &lt;draw:page&gt; element.
 */
public interface PageElement extends ODFElement {
	/**
	 * Create new object in page
	 * 
	 * @param x
	 *            x position of new object
	 * @param y
	 *            y position of new object
	 * @param width
	 *            width of new object
	 * @param height
	 *            height of new object
	 * @return ODFElement
	 */		
	public ODFElement createObject(long x, long y, long width, long height);

	/**
	 * Return draw:name attribute
	 * 
	 * @return String
	 */
	public String getAttrDrawName();

	/**
	 * Return draw:style-name attribute
	 * 
	 * @return String
	 */		
	public String getAttrDrawStyleName();

	/**
	 * Return draw:master-page-name attribute
	 * 
	 * @return String
	 */		
	public String getAttrDrawMasterPageName();

	/**
	 * Return draw:nav-order attribute
	 * 
	 * @return String
	 */		
	public String getAttrDrawNavOrder();

	/**
	 * Return page index
	 * 
	 * @return int
	 */		
	public int getPageIndex();

	/**
	 * Return child ODF nodes by navigation order
	 * 
	 * @return List&lt;ODFElement&gt;
	 */		
	public List<ODFElement> getChildNodesInNavOrder();

	/**
	 * Return graphic elements in page
	 * 
	 * @return NodeList
	 */		
	public NodeList getDrawingObjectElements();

	/**
	 * Return &lt;presentation:notes&gt; element
	 * 
	 * @return NotesElement
	 */		
	public NotesElement getPresentationNotesElement();
}