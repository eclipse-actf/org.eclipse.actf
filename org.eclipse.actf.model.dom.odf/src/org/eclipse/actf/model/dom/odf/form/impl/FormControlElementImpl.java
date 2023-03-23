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
import org.eclipse.actf.model.dom.odf.base.impl.ODFElementImpl;
import org.eclipse.actf.model.dom.odf.form.FormConstants;
import org.eclipse.actf.model.dom.odf.form.FormControlElement;
import org.w3c.dom.Element;


abstract class FormControlElementImpl extends ODFElementImpl implements
		FormControlElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2496037340231132413L;

	protected FormControlElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public String getAttrFormLabel() {
		if (hasAttributeNS(FormConstants.FORM_NAMESPACE_URI,
				FormConstants.ATTR_LABEL))
			return getAttributeNS(FormConstants.FORM_NAMESPACE_URI,
					FormConstants.ATTR_LABEL);
		return null;
	}

	public String getAttrFormTabIndex() {
		if (hasAttributeNS(FormConstants.FORM_NAMESPACE_URI,
				FormConstants.ATTR_TAB_INDEX))
			return getAttributeNS(FormConstants.FORM_NAMESPACE_URI,
					FormConstants.ATTR_TAB_INDEX);
		return null;
	}

	public boolean getAttrFormTabStop() {
		if (hasAttributeNS(FormConstants.FORM_NAMESPACE_URI,
				FormConstants.ATTR_TAB_INDEX)) {
			return new Boolean(getAttributeNS(FormConstants.FORM_NAMESPACE_URI,
					FormConstants.ATTR_TAB_STOP)).booleanValue();
		}
		return false;
	}

	public String getAttrFormControlImplementation() {
		if (hasAttributeNS(FormConstants.FORM_NAMESPACE_URI,
				FormConstants.ATTR_CONTROL_IMPLEMENTATION))
			return getAttributeNS(FormConstants.FORM_NAMESPACE_URI,
					FormConstants.ATTR_CONTROL_IMPLEMENTATION);
		return null;
	}
}