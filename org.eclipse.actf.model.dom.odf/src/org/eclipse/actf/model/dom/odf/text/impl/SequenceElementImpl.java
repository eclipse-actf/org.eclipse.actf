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

import org.eclipse.actf.model.dom.odf.ODFException;
import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.impl.ODFStylableElementImpl;
import org.eclipse.actf.model.dom.odf.content.IEditListener;
import org.eclipse.actf.model.dom.odf.content.IEditable;
import org.eclipse.actf.model.dom.odf.text.SequenceElement;
import org.w3c.dom.Element;


class SequenceElementImpl extends ODFStylableElementImpl implements
		SequenceElement {
	private static final long serialVersionUID = 1475929066534021515L;

	protected SequenceElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public Object getValue() {
		return this.getTextContent();
	}

	public void setValue(Object data) {
		// TODO validate style:num-format attribute
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
}