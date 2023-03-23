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

import org.eclipse.actf.model.dom.odf.ODFException;
import org.eclipse.actf.model.dom.odf.base.DrawingObjectBaseElement;
import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.w3c.dom.Element;


@SuppressWarnings("nls")
public abstract class DrawingObjectBaseElementImpl extends ODFElementImpl
		implements DrawingObjectBaseElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4526393398550531657L;

	protected DrawingObjectBaseElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public ODFElement getShortDescElement() {
		ODFDocument doc = (ODFDocument) this.getOwnerDocument();
		return getShortDescElement(doc.getODFVersion());
	}

	public ODFElement getShortDescElement(double version) {
		if (version == 1.0) {
			return this.getSVGDescElement();
		} else if (version > 1.0) {
			return this.getSVGTitleElement();
		} else {
			new ODFException("invalid office version").printStackTrace();
		}
		return null;
	}

	public ODFElement getLongDescElement() {
		ODFDocument doc = (ODFDocument) this.getOwnerDocument();
		return getLongDescElement(doc.getODFVersion());
	}

	public ODFElement getLongDescElement(double version) {
		if (version == 1.0) {
			new ODFException("long desc is not available for ODF 1.0")
					.printStackTrace();
		} else if (version > 1.0) {
			return this.getSVGDescElement();
		} else {
			new ODFException("invalid office version").printStackTrace();
		}
		return null;
	}
}