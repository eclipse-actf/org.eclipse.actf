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

import org.eclipse.actf.model.internal.dom.sgml.impl.SGMLElement;
import org.w3c.dom.Document;
import org.w3c.dom.html.HTMLElement;


@SuppressWarnings("nls")
public class SHElement extends SGMLElement implements HTMLElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1719891221719168587L;

	protected SHElement(String name, Document doc) {
		super(name, doc);
	}

	public String getClassName() {
		return getAttribute("class");
	}

	public String getDir() {
		return getAttribute("dir");
	}

	public String getId() {
		return getAttribute("id");
	}

	public String getLang() {
		return getAttribute("lang");
	}

	public String getTitle() {
		return getAttribute("title");
	}

	public void setClassName(String className) {
		setAttribute("class", className);
	}

	public void setDir(String dir) {
		setAttribute("dir", dir);
	}

	public void setId(String id) {
		setAttribute("id", id);
	}

	public void setLang(String lang) {
		setAttribute("lang", lang);
	}

	public void setTitle(String title) {
		setAttribute("title", title);
	}
}
