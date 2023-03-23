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

import org.w3c.dom.html.HTMLFrameSetElement;

@SuppressWarnings("nls")
public class SHFrameSetElement extends SHElement implements HTMLFrameSetElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1412449082506625711L;

	protected SHFrameSetElement(String tagName, SHDocument doc) {
		super(tagName, doc);
	}

	public String getCols() {
		return getAttribute("cols");
	}

	public String getRows() {
		return getAttribute("rows");
	}

	public void setCols(String cols) {
		setAttribute("cols", cols);
	}

	public void setRows(String rows) {
		setAttribute("rows", rows);
	}
}
