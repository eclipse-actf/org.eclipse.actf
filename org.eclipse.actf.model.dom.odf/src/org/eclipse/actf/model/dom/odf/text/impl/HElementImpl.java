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
import org.eclipse.actf.model.dom.odf.base.impl.ODFStylableElementImpl;
import org.eclipse.actf.model.dom.odf.content.IEditListener;
import org.eclipse.actf.model.dom.odf.content.IEditable;
import org.eclipse.actf.model.dom.odf.range.ITextElementContainer;
import org.eclipse.actf.model.dom.odf.range.impl.ITextElementContainerUtil;
import org.eclipse.actf.model.dom.odf.text.HElement;
import org.eclipse.actf.model.dom.odf.text.TextConstants;
import org.w3c.dom.Element;


class HElementImpl extends ODFStylableElementImpl implements HElement {
	private static final long serialVersionUID = -2763953514136052510L;

	protected HElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public int getAttrTextOutlineLevel() {
		if (hasAttributeNS(TextConstants.TEXT_NAMESPACE_URI,
				TextConstants.ATTR_OUTLINE_LEVEL))
			return new Integer(getAttributeNS(TextConstants.TEXT_NAMESPACE_URI,
					TextConstants.ATTR_OUTLINE_LEVEL)).intValue();
		return -1;
	}

	public boolean getAttrTextRestartNumbering() {
		if (hasAttributeNS(TextConstants.TEXT_NAMESPACE_URI,
				TextConstants.ATTR_RESTART_NUMBERING))
			return new Boolean(getAttributeNS(TextConstants.TEXT_NAMESPACE_URI,
					TextConstants.ATTR_RESTART_NUMBERING)).booleanValue();
		return false;
	}

	public int getAttrTextStartValue() {
		if (hasAttributeNS(TextConstants.TEXT_NAMESPACE_URI,
				TextConstants.ATTR_START_VALUE))
			return new Integer(getAttributeNS(TextConstants.TEXT_NAMESPACE_URI,
					TextConstants.ATTR_START_VALUE)).intValue();
		return -1;
	}

	public boolean getAttrTextIsListHeader() {
		if (hasAttributeNS(TextConstants.TEXT_NAMESPACE_URI,
				TextConstants.ATTR_IS_LIST_HEADER))
			return new Boolean(getAttributeNS(TextConstants.TEXT_NAMESPACE_URI,
					TextConstants.ATTR_IS_LIST_HEADER)).booleanValue();
		return false;
	}

	public Object getValue() {
		return this.getTextContent();
	}

	public void setValue(Object data) {
		if (data instanceof String) {
			this.setTextContent((String) data);
		} else {
			new ODFException("invalid object for text:h setValue function.") //$NON-NLS-1$
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

	public long getContentSize() {
		return ITextElementContainerUtil.getContentSize(this);
	}

	public Iterator<ITextElementContainer> getChildIterator() {
		return ITextElementContainerUtil.getChildIterator(this);
	}
}