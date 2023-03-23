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

import org.w3c.dom.html.HTMLLIElement;

@SuppressWarnings("nls")
public class SHLIElement extends SHElement implements HTMLLIElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9160759518224092053L;

	protected SHLIElement(String tagName, SHDocument doc) {
		super(tagName, doc);
	}

	public String getType() {
		return getAttribute("type");
	}

	public void setType(String type) {
		setAttribute("type", type);
	}

	/**
	 * @exception NumberFormatException
	 */
	public int getValue() {
		return Integer.parseInt(getAttribute("value"));
	}

	public void setValue(int value) {
		setAttribute("value", Integer.toString(value));
	}
}
