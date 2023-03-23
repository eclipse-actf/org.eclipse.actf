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
package org.eclipse.actf.model.dom.odf.draw.impl;

import org.eclipse.actf.model.dom.odf.ODFException;
import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.impl.DrawingObjectElementImpl;
import org.eclipse.actf.model.dom.odf.draw.ControlElement;
import org.eclipse.actf.model.dom.odf.draw.DrawConstants;
import org.eclipse.actf.model.dom.odf.form.FixedTextElement;
import org.eclipse.actf.model.dom.odf.form.FormConstants;
import org.eclipse.actf.model.dom.odf.form.FormControlElement;
import org.eclipse.actf.model.dom.odf.office.OfficeConstants;
import org.eclipse.actf.util.xpath.XPathServiceFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

class ControlElementImpl extends DrawingObjectElementImpl implements
		ControlElement {
	private static final long serialVersionUID = -4483584669064968195L;

	protected ControlElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public String getAttrDrawControl() {
		if (hasAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ATTR_CONTROL))
			return getAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ATTR_CONTROL);
		return null;
	}

	@SuppressWarnings("nls")
	public FormControlElement getFormControlElement() {
		NodeList nl = XPathServiceFactory.newService().evalPathForNodeList(
				"ancestor::*/*[namespace-uri()='"
						+ OfficeConstants.OFFICE_NAMESPACE_URI
						+ "' and local-name()='"
						+ OfficeConstants.ELEMENT_FORMS
						+ "']/*[namespace-uri()='"
						+ FormConstants.FORM_NAMESPACE_URI
						+ "' and local-name()='" + FormConstants.ELEMENT_FORM
						+ "']/*[attribute::*[namespace-uri()='"
						+ FormConstants.FORM_NAMESPACE_URI
						+ "' and local-name()='id']='" + getAttrDrawControl()
						+ "']", this);

		if ((nl != null) && (nl.getLength() == 1)) {
			Element control = (Element) nl.item(0);
			if (control instanceof FormControlElement) {
				return (FormControlElement) control;
			} else {
				new ODFException(
						"draw:id should specify the id of FormControlElement")
						.printStackTrace();
				System.err.println("namespace : " + control.getNamespaceURI());
				System.err.println("localname : " + control.getLocalName());
				System.err
						.println("text content : " + control.getTextContent());
			}
		} else if ((nl != null) && (nl.getLength() > 1)) {
			new ODFException(
					"draw:control should not have more thant one form:property element")
					.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("nls")
	public FixedTextElement getFormLabelFixedTextElement() {
		String drawControl = getAttrDrawControl();
		NodeList nl = XPathServiceFactory.newService().evalPathForNodeList(
				"ancestor::*/*[namespace-uri()='"
						+ OfficeConstants.OFFICE_NAMESPACE_URI
						+ "' and local-name()='"
						+ OfficeConstants.ELEMENT_FORMS
						+ "']/*[namespace-uri()='"
						+ FormConstants.FORM_NAMESPACE_URI
						+ "' and local-name()='" + FormConstants.ELEMENT_FORM
						+ "']/*[namespace-uri()='"
						+ FormConstants.FORM_NAMESPACE_URI
						+ "' and local-name()='"
						+ FormConstants.ELEMENT_FIXED_TEXT
						+ "'][attribute::*[namespace-uri()='"
						+ FormConstants.FORM_NAMESPACE_URI
						+ "' and local-name()='" + FormConstants.ATTR_FOR
						+ "']='" + drawControl + "']", this);
		if ((nl != null) && (nl.getLength() == 1)) {
			Element text = (Element) nl.item(0);
			if (text instanceof FixedTextElement) {
				return (FixedTextElement) text;
			}
		} else if ((nl != null) && (nl.getLength() > 1)) {
			new ODFException(
					"draw:control should not have more thant one form label field element")
					.printStackTrace();
		}
		return null;
	}
}