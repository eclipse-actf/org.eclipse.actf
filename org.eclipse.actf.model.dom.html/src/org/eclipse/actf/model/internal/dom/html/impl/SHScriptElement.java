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

import org.w3c.dom.CharacterData;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLScriptElement;

@SuppressWarnings("nls")
public class SHScriptElement extends SHElement implements HTMLScriptElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4183083040741115537L;

	protected SHScriptElement(String name, SHDocument doc) {
		super(name, doc);
	}

	public String getText() {
		Node child = getFirstChild();
		return child == null ? null : child.getNodeValue();
	}

	public void setText(String text) {
		Node child = getFirstChild();
		if (child == null) {
			appendChild(getOwnerDocument().createCDATASection(text));
		} else if (child instanceof CharacterData) {
			((CharacterData) child).setData(text);
		} else {
			insertBefore(getOwnerDocument().createCDATASection(text), child);
		}
	}

	public String getHtmlFor() {
		return getAttribute("for");
	}

	public void setHtmlFor(String htmlFor) {
		setAttribute("for", htmlFor);
	}

	public String getEvent() {
		return getAttribute("event");
	}

	public void setEvent(String event) {
		setAttribute("event", event);
	}

	public String getCharset() {
		return getAttribute("charset");
	}

	public void setCharset(String charset) {
		setAttribute("charset", charset);
	}

	public boolean getDefer() {
		String val = getAttribute("defer");
		return val != null && val.length() != 0;
	}

	public void setDefer(boolean defer) {
		setAttribute("defer", defer ? "defer" : null);
	}

	public String getSrc() {
		return getAttribute("src");
	}

	public void setSrc(String src) {
		setAttribute("src", src);
	}

	public String getType() {
		return getAttribute("type");
	}

	public void setType(String type) {
		setAttribute("type", type);
	}
}
