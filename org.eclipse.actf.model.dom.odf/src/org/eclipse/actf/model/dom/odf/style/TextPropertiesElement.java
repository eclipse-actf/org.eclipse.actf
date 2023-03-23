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
 * Interface for &lt;style:text-properties&gt; element.
 */
public interface TextPropertiesElement extends StylePropertiesBase {
	public String getAttrStyleFontName();

	public String getAttrStyleFontFamilyGeneric();

	public String getAttrStyleFontPitch();

	public String getAttrStyleFontFamilyAsian();

	public String getAttrStyleFontPitchAsian();

	public String getAttrStyleFontSizeAsian();

	public String getAttrStyleFontFamilyComplex();

	public String getAttrStyleFontPitchComplex();

	public String getAttrStyleFontSizeComplex();

	public String getAttrStyleTextUnderlineStyle();

	public String getAttrStyleTextPosition();

	public String getAttrFormatFontFamily();

	public String getAttrFormatFontSize();

	public String getAttrFormatFontStyle();

	public String getAttrFormatFontWeight();

	public String getAttrFormatColor();

	public String getAttrFormatBackgroundColor();
}