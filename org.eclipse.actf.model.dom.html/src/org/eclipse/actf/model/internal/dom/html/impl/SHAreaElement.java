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

import org.w3c.dom.html.HTMLAreaElement;

@SuppressWarnings("nls")
public class SHAreaElement extends SHALinkElement implements HTMLAreaElement {
	private static final long serialVersionUID = 5249355768381046693L;

	protected SHAreaElement(String tagName, SHDocument doc) {
		super(tagName, doc);
	}

	public String getAlt() {
		return getAttribute("alt");
	}

	public void setAlt(String alt) {
		setAttribute("alt", alt);
	}

	public boolean getNoHref() {
		String nohref = getAttribute("nohref");
		return nohref != null && nohref.equals("nohref");
	}

	public void setNoHref(boolean noHref) {
		setAttribute("nohref", (noHref ? "nohref" : null));
	}
}
