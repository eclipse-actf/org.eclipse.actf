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

import org.w3c.dom.html.HTMLHeadElement;

@SuppressWarnings("nls")
public class SHHeadElement extends SHElement implements HTMLHeadElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1765776219788457657L;

	protected SHHeadElement(String tagName, SHDocument doc) {
		super(tagName, doc);
	}

	public String getProfile() {
		return getAttribute("profile");
	}

	public void setProfile(String profile) {
		setAttribute("profile", profile);
	}
}
