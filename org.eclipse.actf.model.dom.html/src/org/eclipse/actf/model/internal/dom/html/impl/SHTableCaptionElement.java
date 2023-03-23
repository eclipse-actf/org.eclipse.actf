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

import org.w3c.dom.html.HTMLTableCaptionElement;

@SuppressWarnings("nls")
public class SHTableCaptionElement extends SHElement implements
		HTMLTableCaptionElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6744201758204710363L;

	protected SHTableCaptionElement(String name, SHDocument doc) {
		super(name, doc);
	}

	public String getAlign() {
		return getAttribute("align");
	}

	public void setAlign(String align) {
		setAttribute("align", align);
	}
}
