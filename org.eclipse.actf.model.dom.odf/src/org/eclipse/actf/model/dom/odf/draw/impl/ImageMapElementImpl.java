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
import org.eclipse.actf.model.dom.odf.base.impl.EmbedDrawingObjectElementImpl;
import org.eclipse.actf.model.dom.odf.draw.DrawConstants;
import org.eclipse.actf.model.dom.odf.draw.FrameElement;
import org.eclipse.actf.model.dom.odf.draw.ImageElement;
import org.eclipse.actf.model.dom.odf.draw.ImageMapElement;
import org.eclipse.actf.util.xpath.XPathService;
import org.eclipse.actf.util.xpath.XPathServiceFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

class ImageMapElementImpl extends EmbedDrawingObjectElementImpl implements
		ImageMapElement {
	private static final long serialVersionUID = -8706135729383317676L;

	private static final XPathService xpathService = XPathServiceFactory
			.newService();

	@SuppressWarnings("nls")
	private static final Object EXP1 = xpathService
			.compile("./*[namespace-uri()='" + DrawConstants.DRAW_NAMESPACE_URI
					+ "' and local-name()='" + DrawConstants.ELEMENT_IMAGE
					+ "']");

	@SuppressWarnings("nls")
	private static final Object EXP2 = xpathService.compile("./*["
			+ "(namespace-uri()='" + DrawConstants.DRAW_NAMESPACE_URI
			+ "' and local-name()='" + DrawConstants.ELEMENT_AREA_CIRCLE + "')"
			+ " or (namespace-uri()='" + DrawConstants.DRAW_NAMESPACE_URI
			+ "' and local-name()='" + DrawConstants.ELEMENT_AREA_POLYGON
			+ "')" + " or (namespace-uri()='"
			+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
			+ DrawConstants.ELEMENT_AREA_RECTANGLE + "')" + "]");

	protected ImageMapElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public ImageElement getImageElements() {
		FrameElement frame = getFrameElement();
		if (frame != null) {
			NodeList nl = xpathService.evalForNodeList(EXP1, this);
			if ((nl != null) && (nl.getLength() == 1))
				return (ImageElement) nl.item(0);
			if ((nl != null) && (nl.getLength() > 1)) {
				new ODFException(
						"draw:image has more than one text:sequence elements.") //$NON-NLS-1$
						.printStackTrace();
			}
		}
		return null;
	}

	public NodeList getAreaElements() {
		return xpathService.evalForNodeList(EXP2, this);
	}
}