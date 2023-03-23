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

import org.w3c.dom.html.HTMLQuoteElement;

@SuppressWarnings("nls")
public class SHQuoteElement extends SHElement implements HTMLQuoteElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6314446806659581723L;

	protected SHQuoteElement(String tagName, SHDocument doc) {
		super(tagName, doc);
	}

	public String getCite() {
		return getAttribute("cite");
	}

	public void setCite(String cite) {
		setAttribute("cite", cite);
	}
}
