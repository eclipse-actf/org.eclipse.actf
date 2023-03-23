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
package org.eclipse.actf.model.dom.odf.chart.impl;

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.impl.ODFStylableElementImpl;
import org.eclipse.actf.model.dom.odf.chart.TitleElement;
import org.eclipse.actf.model.dom.odf.content.IEditListener;
import org.eclipse.actf.model.dom.odf.content.IEditable;
import org.eclipse.actf.model.dom.odf.svg.SVGConstants;
import org.w3c.dom.Element;


class TitleElementImpl extends ODFStylableElementImpl implements TitleElement {
	private static final long serialVersionUID = 4722975487761185402L;

	protected TitleElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public String getAttrSvgX() {
		if (hasAttributeNS(SVGConstants.SVG_NAMESPACE_URI, SVGConstants.ATTR_X))
			return getAttributeNS(SVGConstants.SVG_NAMESPACE_URI,
					SVGConstants.ATTR_X);
		return null;
	}

	public String getAttrSvgY() {
		if (hasAttributeNS(SVGConstants.SVG_NAMESPACE_URI, SVGConstants.ATTR_Y))
			return getAttributeNS(SVGConstants.SVG_NAMESPACE_URI,
					SVGConstants.ATTR_Y);
		return null;
	}

	public Object getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setValue(Object data) {
		// TODO Auto-generated method stub

	}

	public void remove() {
		// TODO Auto-generated method stub

	}

	public void insert(IEditable data) {
		// TODO Auto-generated method stub

	}

	public void addEditListener(IEditListener observer, String topic) {
		// TODO Auto-generated method stub

	}

	public void removeEditListener(IEditListener observer, String topic) {
		// TODO Auto-generated method stub

	}
}