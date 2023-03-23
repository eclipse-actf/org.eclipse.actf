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
 * Interface for &lt;draw:image&gt; element.
 */
public interface ImageElement extends EmbedDrawingObjectElement {
	/**
	 * Return xlink:href attribute
	 * 
	 * @return xlink:href attribute.
	 */		
	String getAttrXlinkHref();

	/**
	 * Return xlink:type attribute
	 * 
	 * @return xlink:type attribute.
	 */		
	String getAttrXlinkType();

	/**
	 * Return xlink:show attribute
	 * 
	 * @return xlink:show attribute.
	 */		
	String getAttrXlinkShow();

	/**
	 * Return xlink:actuate attribute
	 * 
	 * @return xlink:actuate attribute.
	 */		
	String getAttrXlinkActuate();
}