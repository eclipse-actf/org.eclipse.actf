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

import org.w3c.dom.html.HTMLOListElement;

@SuppressWarnings("nls")
public class SHOListElement extends SHElement implements HTMLOListElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7241221131042587609L;

	protected SHOListElement(String tagName, SHDocument doc) {
		super(tagName, doc);
	}

	public boolean getCompact() {
		String val = getAttribute("compact");
		return val != null && val.length() != 0;
	}

	public void setCompact(boolean compact) {
		setAttribute("compact", compact ? "compact" : null);
	}

	/**
	 * @exception NumberFormatException
	 */
	public int getStart() {
		return Integer.parseInt(getAttribute("start"));
	}

	public void setStart(int start) {
		setAttribute("start", Integer.toString(start));
	}

	public String getType() {
		return getAttribute("type");
	}

	public void setType(String type) {
		setAttribute("type", type);
	}
}
