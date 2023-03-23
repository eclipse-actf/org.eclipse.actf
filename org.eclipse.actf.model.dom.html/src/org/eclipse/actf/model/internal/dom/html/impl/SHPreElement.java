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

import org.w3c.dom.html.HTMLPreElement;

@SuppressWarnings("nls")
public class SHPreElement extends SHElement implements HTMLPreElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8700014974885203783L;

	protected SHPreElement(String name, SHDocument doc) {
		super(name, doc);
	}

	/**
	 * @exception NumberFormatException
	 */
	public int getWidth() {
		return Integer.parseInt(getAttribute("width"));
	}

	public void setWidth(int width) {
		setAttribute("width", Integer.toString(width));
	}
}
