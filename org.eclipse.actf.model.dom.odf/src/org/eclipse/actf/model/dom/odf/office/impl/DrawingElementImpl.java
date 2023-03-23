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
import org.eclipse.actf.model.dom.odf.ODFConstants.ContentType;
import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.impl.ODFElementImpl;
import org.eclipse.actf.model.dom.odf.draw.DrawConstants;
import org.eclipse.actf.model.dom.odf.draw.PageElement;
import org.eclipse.actf.model.dom.odf.office.DrawingElement;
import org.eclipse.actf.model.dom.odf.range.IContentRange;
import org.eclipse.actf.util.xpath.XPathService;
import org.eclipse.actf.util.xpath.XPathServiceFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

class DrawingElementImpl extends ODFElementImpl implements DrawingElement {
	private static final long serialVersionUID = -1324161975256395109L;

	private static final XPathService xpathService = XPathServiceFactory
			.newService();

	@SuppressWarnings("nls")
	private static final Object EXP1 = xpathService
			.compile("./*[namespace-uri()='" + DrawConstants.DRAW_NAMESPACE_URI
					+ "' and local-name()='" + DrawConstants.ELEMENT_PAGE
					+ "']");

	protected DrawingElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public ContentType getContentType() {
		return ContentType.DRAW;
	}

	public IContentRange createRange() {
		// TODO Auto-generated method stub
		return null;
	}

	public PageElement getPage(long idx) {
		NodeList nl = xpathService.evalForNodeList(EXP1, this);
		if ((idx < 0) || (idx >= nl.getLength())) {
			new ODFException("invalid page index").printStackTrace(); //$NON-NLS-1$
			return null;
		}
		return (PageElement) nl.item((int) idx);
	}

	public long getPageSize() {
		return xpathService.evalForNodeList(EXP1, this).getLength();
	}

	public PageElement createPage(long idx) {
		// TODO Auto-generated method stub
		return null;
	}
}