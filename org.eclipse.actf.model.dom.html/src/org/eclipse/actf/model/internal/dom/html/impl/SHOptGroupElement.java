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

import org.w3c.dom.html.HTMLOptGroupElement;

@SuppressWarnings("nls")
public class SHOptGroupElement extends SHElement implements HTMLOptGroupElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 133676314495698687L;

	protected SHOptGroupElement(String tagName, SHDocument doc) {
		super(tagName, doc);
	}

	public boolean getDisabled() {
		String val = getAttribute("disabled");
		return val != null && val.length() != 0;
	}

	public void setDisabled(boolean disabled) {
		setAttribute("disabled", disabled ? "disabled" : null);
	}

	public String getLabel() {
		return getAttribute("label");
	}

	public void setLabel(String label) {
		setAttribute("label", label);
	}
}
