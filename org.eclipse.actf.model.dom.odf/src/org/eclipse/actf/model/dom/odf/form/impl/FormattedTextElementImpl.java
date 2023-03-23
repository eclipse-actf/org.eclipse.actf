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
package org.eclipse.actf.model.dom.odf.form.impl;

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.form.FormattedTextElement;
import org.w3c.dom.Element;


class FormattedTextElementImpl extends FormControlElementImpl implements
		FormattedTextElement {
	private static final long serialVersionUID = -1130995199483752922L;

	protected FormattedTextElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}
}