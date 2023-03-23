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

import org.eclipse.actf.model.dom.odf.ODFException;
import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.impl.ODFElementImpl;
import org.eclipse.actf.model.dom.odf.office.AutomaticStylesElement;
import org.eclipse.actf.model.dom.odf.office.BodyElement;
import org.eclipse.actf.model.dom.odf.office.DocumentContentElement;
import org.eclipse.actf.model.dom.odf.office.FontFaceDeclsElement;
import org.eclipse.actf.model.dom.odf.office.OfficeConstants;
import org.eclipse.actf.model.dom.odf.style.StyleElement;
import org.eclipse.actf.util.xpath.XPathService;
import org.eclipse.actf.util.xpath.XPathServiceFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

class DocumentContentElementImpl extends ODFElementImpl implements
		DocumentContentElement {
	private static final long serialVersionUID = -6010974975405611664L;

	private static final XPathService xpathService = XPathServiceFactory
			.newService();

	@SuppressWarnings("nls")
	private static final Object EXP1 = xpathService
			.compile("./*[namespace-uri()='"
					+ OfficeConstants.OFFICE_NAMESPACE_URI
					+ "' and local-name()='" + OfficeConstants.ELEMENT_BODY
					+ "']");

	protected DocumentContentElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public double getAttrOfficeVersion() {
		if (hasAttributeNS(OfficeConstants.OFFICE_NAMESPACE_URI,
				OfficeConstants.ATTR_VERSION)) {
			return new Double(getAttributeNS(
					OfficeConstants.OFFICE_NAMESPACE_URI,
					OfficeConstants.ATTR_VERSION)).doubleValue();
		}
		return -1.0;
	}

	public FontFaceDeclsElement getFontFaceDeclsElement() {
		NodeList children = getChildNodes();
		if ((children != null) && (children.getLength() != 0)) {
			for (int i = 0; i < children.getLength(); i++) {
				if (children.item(i) instanceof FontFaceDeclsElement) {
					return (FontFaceDeclsElement) children.item(i);
				}
			}
		}
		return null;
	}

	public AutomaticStylesElement getAutomaticStylesElement() {
		NodeList children = getChildNodes();
		if ((children != null) && (children.getLength() != 0)) {
			for (int i = 0; i < children.getLength(); i++) {
				if (children.item(i) instanceof AutomaticStylesElement) {
					return (AutomaticStylesElement) children.item(i);
				}
			}
		}
		return null;
	}

	public BodyElement getBodyElement() {
		NodeList body = xpathService.evalForNodeList(EXP1, this);
		if ((body == null) || (body.getLength() != 1)) {
			new ODFException("ODF document must have one office:body element") //$NON-NLS-1$
					.printStackTrace();
			return null;
		}
		return (BodyElement) body.item(0);
	}

	public void save(String uri) {
		// TODO Auto-generated method stub

	}

	public StyleElement createStyle(String family) {
		// TODO Auto-generated method stub
		return null;
	}
}