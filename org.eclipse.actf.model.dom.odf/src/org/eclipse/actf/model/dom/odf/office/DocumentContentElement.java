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
package org.eclipse.actf.model.dom.odf.office;

import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.style.StyleElement;

/**
 * Interface for &lt;office:document-content&gt; element.
 */
public interface DocumentContentElement extends ODFElement {
	public double getAttrOfficeVersion();

	public BodyElement getBodyElement();

	public void save(String uri);

	public StyleElement createStyle(String family);

	public FontFaceDeclsElement getFontFaceDeclsElement();

	public AutomaticStylesElement getAutomaticStylesElement();
}