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
import org.w3c.dom.NodeList;

/**
 * Interface for &lt;draw:image-map&gt; element.
 */
public interface ImageMapElement extends EmbedDrawingObjectElement {
	/**
	 * Return &lt;draw:image&gt; element 
	 * 
	 * @return ImageElement
	 */	
	public ImageElement getImageElements();

	/**
	 * Return node list of image map area elements
	 * 
	 * @return NodeList
	 */	
	public NodeList getAreaElements();
}