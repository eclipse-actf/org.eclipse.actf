/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tatsuya ISHIHARA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.model.dom.odf.office.impl;

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.impl.ODFElementImpl;
import org.eclipse.actf.model.dom.odf.office.AutomaticStylesElement;
import org.eclipse.actf.model.dom.odf.style.StyleElement;
import org.w3c.dom.Element;


class AutomaticStylesElementImpl extends ODFElementImpl implements
		AutomaticStylesElement {
	private static final long serialVersionUID = 3389268071502737826L;

	protected AutomaticStylesElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public StyleElement getStyleElementByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getStyleElementSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	public StyleElement getStyleElement(long index) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addStyleElement(StyleElement element) {
		// TODO Auto-generated method stub

	}
}