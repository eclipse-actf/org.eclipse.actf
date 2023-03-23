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

import java.util.Iterator;

import org.eclipse.actf.model.dom.odf.ODFConstants.ContentType;
import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.impl.ODFElementImpl;
import org.eclipse.actf.model.dom.odf.office.TextElement;
import org.eclipse.actf.model.dom.odf.range.IContentRange;
import org.eclipse.actf.model.dom.odf.range.ITextElementContainer;
import org.eclipse.actf.model.dom.odf.range.TextRange;
import org.eclipse.actf.model.dom.odf.range.impl.ITextElementContainerUtil;
import org.w3c.dom.Element;


class TextElementImpl extends ODFElementImpl implements TextElement {
	private static final long serialVersionUID = -7728910075988439163L;

	protected TextElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public TextRange select(long seltopx, long seltopy, long selendx,
			long selendy) {
		// TODO Auto-generated method stub
		return null;
	}

	public ContentType getContentType() {
		return ContentType.WRITE;
	}

	public IContentRange createRange() {
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