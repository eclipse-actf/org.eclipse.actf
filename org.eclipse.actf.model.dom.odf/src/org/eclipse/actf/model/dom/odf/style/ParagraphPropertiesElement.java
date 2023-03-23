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
 * Interface for &lt;style:paragraph-properties&gt; element.
 */
public interface ParagraphPropertiesElement extends StylePropertiesBase {
	public TabStopsElement getTabStopsElement();

	public String getAttrFormatLineHeight();

	public void setAttrFormatLineHeight(String height);

	public String getAttrStyleLineHeightAtLeast();

	public void setAttrStyleLineHeightAtLeast(String height);

	public String getAttrFormatTextAlign();

	public void setAttrFormatTextAlign(String align);

	public String getAttrStyleVerticalAlign();

	public void setAttrStyleVerticalAlign(String align);

	public String getAttrFormatMarginLeft();

	public String getAttrFormatMarginRight();

	public String getAttrFormatMarginTop();

	public String getAttrFormatMarginBottom();

	public String getAttrFormatBackgroundColor();
}
