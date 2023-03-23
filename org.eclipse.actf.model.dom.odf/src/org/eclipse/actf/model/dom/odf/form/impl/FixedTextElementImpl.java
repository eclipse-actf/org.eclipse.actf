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
import org.eclipse.actf.model.dom.odf.form.FixedTextElement;
import org.eclipse.actf.model.dom.odf.form.FormConstants;
import org.w3c.dom.Element;


class FixedTextElementImpl extends FormControlElementImpl implements
		FixedTextElement {
	private static final long serialVersionUID = -4686476250005638294L;

	protected FixedTextElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public String getAttrFormFor() {
		if (hasAttributeNS(FormConstants.FORM_NAMESPACE_URI,
				FormConstants.ATTR_FOR))
			return getAttributeNS(FormConstants.FORM_NAMESPACE_URI,
					FormConstants.ATTR_FOR);
		return null;
	}
}