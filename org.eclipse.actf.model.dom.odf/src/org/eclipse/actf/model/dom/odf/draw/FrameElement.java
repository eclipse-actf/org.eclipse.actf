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

import java.util.Iterator;

import org.eclipse.actf.model.dom.odf.base.DrawingObjectElement;
import org.eclipse.actf.model.dom.odf.base.ODFElement;

/**
 * Interface for &lt;draw:frame&gt; element.
 */
public interface FrameElement extends DrawingObjectElement {
	/**
	 * Return table:end-cell-address attribute
	 * 
	 * @return String
	 */
	public String getAttrTableEndCellAddress();

	/**
	 * Return table:end-x attribute
	 * 
	 * @return String
	 */
	public String getAttrTableEndX();

	/**
	 * Return table:end-y attribute
	 * 
	 * @return String
	 */
	public String getAttrTableEndY();

	/**
	 * Return draw:z-index attribute
	 * 
	 * @return int
	 */
	public int getAttrDrawZIndex();

	/**
	 * Return svg:width attribute
	 * 
	 * @return String
	 */
	public String getAttrSvgWidth();

	/**
	 * Return svg:height attribute
	 * 
	 * @return String
	 */
	public String getAttrSvgHeight();

	/**
	 * Return svg:x attribute
	 * 
	 * @return String
	 */
	public String getAttrSvgX();

	/**
	 * Return svg:y attribute
	 * 
	 * @return String
	 */
	public String getAttrSvgY();

	/**
	 * Return number of child elements
	 * 
	 * @return long
	 */
	public long getContentSize();

	/**
	 * Return iterator for child elements
	 * 
	 * @return Iterator&lt;ODFElement&gt;
	 */
	public Iterator<ODFElement> getChildIterator();
}