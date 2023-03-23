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

import org.eclipse.actf.model.dom.odf.draw.FrameElement;
import org.eclipse.actf.model.dom.odf.text.SequenceElement;

/**
 * Graphics ODF elements that have &lt;draw:frame&gt; as parent should implement this interface.
 */
public interface EmbedDrawingObjectElement extends DrawingObjectBaseElement {
	/**
	 * Return &lt;draw:framw&gt; element
	 * 
	 * @return FrameElement
	 */	
	public FrameElement getFrameElement();

	/**
	 * Return &lt;text:sequence&gt; element
	 * 
	 * @return SequenceElement
	 */	
	public SequenceElement getTextSequenceElement();
}