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

@SuppressWarnings("nls")
abstract class SHALinkElement extends SHElement {
	private static final long serialVersionUID = 6128418712613433758L;

	SHALinkElement(String name, SHDocument doc) {
		super(name, doc);
	}

	public String getAccessKey() {
		return getAttribute("accesskey");
	}

	public void setAccessKey(String accessKey) {
		setAttribute("accesskey", accessKey);
	}

	public String getCoords() {
		return getAttribute("coords");
	}

	public void setCoords(String coords) {
		setAttribute("coords", coords);
	}

	public String getHref() {
		return getAttribute("href");
	}

	public void setHref(String href) {
		setAttribute("href", href);
	}

	public String getShape() {
		return getAttribute("shape");
	}

	public void setShape(String shape) {
		setAttribute("shape", shape);
	}

	/**
	 * @exception NumberFormatException
	 */
	public int getTabIndex() {
		return Integer.parseInt(getAttribute("tabindex"));
	}

	public void setTabIndex(int tabIndex) {
		setAttribute("tabindex", Integer.toString(tabIndex));
	}

	public String getTarget() {
		return getAttribute("target");
	}

	public void setTarget(String target) {
		setAttribute("target", target);
	}
}
