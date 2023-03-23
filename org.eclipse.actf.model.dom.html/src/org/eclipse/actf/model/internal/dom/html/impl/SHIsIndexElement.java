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

import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLIsIndexElement;

@SuppressWarnings("nls")
public class SHIsIndexElement extends SHElement implements HTMLIsIndexElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1318886793394743733L;

	protected SHIsIndexElement(String tagName, SHDocument doc) {
		super(tagName, doc);
	}

	public HTMLFormElement getForm() {
		for (Node ret = getParentNode(); ret != null; ret = ret.getParentNode()) {
			if (ret instanceof HTMLFormElement)
				return (HTMLFormElement) ret;
		}
		return null;
	}

	public String getPrompt() {
		return getAttribute("prompt");
	}

	public void setPrompt(String prompt) {
		setAttribute("prompt", prompt);
	}
}
