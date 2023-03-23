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

import org.w3c.dom.html.HTMLDListElement;

@SuppressWarnings("nls")
public class SHDListElement extends SHElement implements HTMLDListElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7619690896418307193L;

	protected SHDListElement(String tagName, SHDocument doc) {
		super(tagName, doc);
	}

	public boolean getCompact() {
		String val = getAttribute("compact");
		return val != null && val.length() != 0;
	}

	public void setCompact(boolean compact) {
		setAttribute("compact", compact ? "compact" : null);
	}
}
