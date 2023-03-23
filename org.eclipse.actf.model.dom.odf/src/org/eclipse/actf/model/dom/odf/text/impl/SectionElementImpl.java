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

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.impl.ODFStylableElementImpl;
import org.eclipse.actf.model.dom.odf.content.IEditListener;
import org.eclipse.actf.model.dom.odf.content.IEditable;
import org.eclipse.actf.model.dom.odf.range.ITextElementContainer;
import org.eclipse.actf.model.dom.odf.text.SectionElement;
import org.w3c.dom.Element;


class SectionElementImpl extends ODFStylableElementImpl implements
		SectionElement {
	private static final long serialVersionUID = 6798818985841796544L;

	protected SectionElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public void addEditListener(IEditListener observer, String topic) {
		// TODO Auto-generated method stub

	}

	public Object getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public void insert(IEditable data) {
		// TODO Auto-generated method stub

	}

	public void remove() {
		// TODO Auto-generated method stub

	}

	public void removeEditListener(IEditListener observer, String topic) {
		// TODO Auto-generated method stub

	}

	public void setValue(Object data) {
		// TODO Auto-generated method stub

	}

	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	public Iterator<ITextElementContainer> getChildIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	public long getContentSize() {
		// TODO Auto-generated method stub
		return 0;
	}
}