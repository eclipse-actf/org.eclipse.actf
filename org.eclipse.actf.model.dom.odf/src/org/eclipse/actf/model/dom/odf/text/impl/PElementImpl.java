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
package org.eclipse.actf.model.dom.odf.text.impl;

import java.util.Iterator;

import org.eclipse.actf.model.dom.odf.ODFException;
import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.base.impl.ODFStylableElementImpl;
import org.eclipse.actf.model.dom.odf.content.IEditListener;
import org.eclipse.actf.model.dom.odf.content.IEditable;
import org.eclipse.actf.model.dom.odf.range.ITextElementContainer;
import org.eclipse.actf.model.dom.odf.range.impl.ITextElementContainerUtil;
import org.eclipse.actf.model.dom.odf.style.StyleConstants;
import org.eclipse.actf.model.dom.odf.style.StyleElement;
import org.eclipse.actf.model.dom.odf.text.PElement;
import org.eclipse.actf.model.dom.odf.text.TextConstants;
import org.w3c.dom.Element;


class PElementImpl extends ODFStylableElementImpl implements PElement {
	private static final long serialVersionUID = -8044542053180348600L;

	protected PElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public StyleElement getStyle() {
		String styleName = getAttributeNS(TextConstants.TEXT_NAMESPACE_URI,
				TextConstants.ATTR_STYLE_NAME);
		ODFElement style = findElementByAttrValue(
				StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ELEMENT_STYLE,
				StyleConstants.STYLE_NAMESPACE_URI, StyleConstants.ATTR_NAME,
				styleName);
		if ((style != null) && (style instanceof StyleElement))
			return (StyleElement) style;
		return null;
	}

	public Object getValue() {
		return this.getTextContent();
	}

	public void setValue(Object data) {
		if (data instanceof String) {
			this.setTextContent((String) data);
		} else {
			new ODFException("invalid object for text:p setValue function.") //$NON-NLS-1$
					.printStackTrace();
		}
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

	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	public long getContentSize() {
		return ITextElementContainerUtil.getContentSize(this);
	}

	public Iterator<ITextElementContainer> getChildIterator() {
		return ITextElementContainerUtil.getChildIterator(this);
	}
}