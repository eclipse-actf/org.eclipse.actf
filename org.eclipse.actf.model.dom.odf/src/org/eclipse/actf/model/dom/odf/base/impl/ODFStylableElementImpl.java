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
package org.eclipse.actf.model.dom.odf.base.impl;

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.content.IStylable;
import org.eclipse.actf.model.dom.odf.content.IStyleListener;
import org.eclipse.actf.model.dom.odf.style.StyleElement;
import org.w3c.dom.Element;


public abstract class ODFStylableElementImpl extends ODFElementImpl implements
		ODFElement, IStylable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1550683236941591367L;

	protected ODFStylableElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public void setStyle(StyleElement style) {
		// TODO Auto-generated method stub
	}

	public StyleElement getStyle() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addStyleListener(IStyleListener listener, String topic) {
		// TODO Auto-generated method stub
	}

	public void removeStyleListener(IStyleListener listener, String topic) {
		// TODO Auto-generated method stub
	}
}
