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

import org.w3c.dom.html.HTMLModElement;

@SuppressWarnings("nls")
public class SHModElement extends SHElement implements HTMLModElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3483927947885465633L;

	protected SHModElement(String tagName, SHDocument doc) {
		super(tagName, doc);
	}

	public String getCite() {
		return getAttribute("cite");
	}

	public void setCite(String cite) {
		setAttribute("cite", cite);
	}

	public String getDateTime() {
		return getAttribute("datetime");
	}

	public void setDateTime(String dateTime) {
		setAttribute("datetime", dateTime);
	}
}
