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
package org.eclipse.actf.model.dom.odf.style;

/**
 * Interface for &lt;style:page-layout-properties&gt; element.
 */
public interface PageLayoutPropertiesElement extends StylePropertiesBase {
	public String getAttrFormatMarginTop();

	public String getAttrFormatMarginBottom();

	public String getAttrFormatMarginLeft();

	public String getAttrFormatMarginRight();

	public String getAttrFormatPageWidth();

	public String getAttrFormatPageHeight();

	public String getAttrStylePrintOrientation();
}