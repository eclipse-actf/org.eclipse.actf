/*******************************************************************************
 * Copyright (c) 1998, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Goh KONDOH - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.internal.dom.html.impl;

import org.w3c.dom.html.HTMLParagraphElement;

@SuppressWarnings("nls")
public class SHParagraphElement extends SHElement implements
		HTMLParagraphElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7463993950987771405L;

	protected SHParagraphElement(String name, SHDocument doc) {
		super(name, doc);
	}

	public String getAlign() {
		return getAttribute("align");
	}

	public void setAlign(String align) {
		setAttribute("align", align);
	}
}
