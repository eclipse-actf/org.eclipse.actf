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

import org.w3c.dom.html.HTMLStyleElement;

@SuppressWarnings("nls")
public class SHStyleElement extends SHElement implements HTMLStyleElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8058784497367151916L;

	protected SHStyleElement(String tagName, SHDocument doc) {
		super(tagName, doc);
	}

	/**
	 * @return always false (For an application, override this method).
	 */
	public boolean getDisabled() {
		return false;
	}

	/**
	 * Does nothing.
	 */
	public void setDisabled(boolean disabled) {
	}

	public String getMedia() {
		return getAttribute("media");
	}

	public void setMedia(String media) {
		setAttribute("media", media);
	}

	public String getType() {
		return getAttribute("type");
	}

	public void setType(String type) {
		setAttribute("type", type);
	}
}
