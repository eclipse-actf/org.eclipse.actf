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

import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.content.IStyleListener;

/**
 * Interface for &lt;style:style&gt; element.
 */
public interface StyleElement extends ODFElement {
	public String getAttrStyleMasterPageName();

	public String getAttrStyleParentStyleName();

	public String getAttrStyleDataStyleName();

	public StyleElement createChild(String family);

	public String getFamily();

	public String getName();

	public void setName(String name);

	public StylePropertiesBase getPropertyElement(long idx);

	public long getPropertySize();

	public void putPropertyElement(StylePropertiesBase property);

	public void addListener(IStyleListener listener, String topic);

	public void removeListener(IStyleListener listener, String topic);
}