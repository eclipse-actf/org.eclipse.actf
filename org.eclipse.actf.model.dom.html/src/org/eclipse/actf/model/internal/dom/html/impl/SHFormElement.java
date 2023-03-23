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

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLFormElement;

@SuppressWarnings("nls")
public class SHFormElement extends SHElement implements HTMLFormElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4544434906167810276L;

	protected SHFormElement(String tagName, SHDocument doc) {
		super(tagName, doc);
	}

	public HTMLCollection getElements() {
		int len = 0;
		HTMLElement elements[] = new HTMLElement[8];
		Node tmp1, tmp2;
		tmp1 = this;
		outer: while (tmp1 != null) {
			if (tmp1 instanceof Element) {
				String name = tmp1.getNodeName();
				if (name.equalsIgnoreCase("INPUT")
						|| name.equalsIgnoreCase("SELECT")
						|| name.equalsIgnoreCase("TEXTAREA")
						|| name.equalsIgnoreCase("LABEL")
						|| name.equalsIgnoreCase("BUTTON")) {
					if (len == elements.length) {
						HTMLElement buf[] = new HTMLElement[len * 2];
						System.arraycopy(elements, 0, buf, 0, len);
						elements = buf;
					}
					elements[len++] = (HTMLElement) tmp1;
				}
			}
			if ((tmp2 = tmp1.getFirstChild()) == null) {
				if (tmp1 == this) {
					break outer;
				} else {
					tmp2 = tmp1.getNextSibling();
				}
			}
			while (tmp2 == null && tmp1 != null) {
				tmp1 = tmp2 = tmp1.getParentNode();
				if (tmp1 != this) {
					tmp2 = tmp1.getNextSibling();
				} else {
					break outer;
				}
			}
			tmp1 = tmp2;
		}
		return ((SHDocument) getOwnerDocument())
				.createCollection(elements, len);
	}

	public int getLength() {
		return getElements().getLength();
	};

	public String getName() {
		return getAttribute("name");
	}

	public void setName(String name) {
		setAttribute("name", name);
	}

	public String getAcceptCharset() {
		return getAttribute("accept-charset");
	}

	public void setAcceptCharset(String acceptCharset) {
		setAttribute("accept-charset", acceptCharset);
	}

	public String getAction() {
		return getAttribute("action");
	}

	public void setAction(String action) {
		setAttribute("action", action);
	}

	public String getEnctype() {
		return getAttribute("enctype");
	}

	public void setEnctype(String enctype) {
		setAttribute("enctype", enctype);
	}

	public String getMethod() {
		return getAttribute("method");
	}

	public void setMethod(String method) {
		setAttribute("method", method);
	}

	public String getTarget() {
		return getAttribute("target");
	}

	public void setTarget(String target) {
		setAttribute("target", target);
	}

	/**
	 * do nothing
	 */
	public void submit() { /* do nothing; */
	}

	/**
	 * do nothing
	 */
	public void reset() { /* do nothing; */
	}
}
