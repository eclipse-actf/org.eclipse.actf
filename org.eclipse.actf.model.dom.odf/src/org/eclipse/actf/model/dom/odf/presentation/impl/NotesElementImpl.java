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
package org.eclipse.actf.model.dom.odf.presentation.impl;

import java.util.Iterator;

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.impl.ODFStylableElementImpl;
import org.eclipse.actf.model.dom.odf.content.IEditListener;
import org.eclipse.actf.model.dom.odf.content.IEditable;
import org.eclipse.actf.model.dom.odf.presentation.NotesElement;
import org.eclipse.actf.model.dom.odf.range.ITextElementContainer;
import org.w3c.dom.Element;


class NotesElementImpl extends ODFStylableElementImpl implements NotesElement {
	private static final long serialVersionUID = 7810292508884604322L;

	protected NotesElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
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

	public long getContentSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Iterator<ITextElementContainer> getChildIterator() {
		// TODO Auto-generated method stub
		return null;
	}
}