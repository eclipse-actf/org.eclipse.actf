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
package org.eclipse.actf.model.dom.odf.style.impl;

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.impl.ODFElementImpl;
import org.eclipse.actf.model.dom.odf.draw.DrawConstants;
import org.eclipse.actf.model.dom.odf.presentation.PresentationConstants;
import org.eclipse.actf.model.dom.odf.style.DrawingPagePropertiesElement;
import org.w3c.dom.Element;


class DrawingPagePropertiesElementImpl extends ODFElementImpl implements
		DrawingPagePropertiesElement {
	private static final long serialVersionUID = -3812620868676549011L;

	protected DrawingPagePropertiesElementImpl(ODFDocument odfDoc,
			Element element) {
		super(odfDoc, element);
	}

	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean getAttrPresentationDisplayHeader() {
		if (hasAttributeNS(PresentationConstants.PRESENTATION_NAMESPACE_URI,
				PresentationConstants.ATTR_DISPLAY_HEADER)) {
			return new Boolean(getAttributeNS(
					PresentationConstants.PRESENTATION_NAMESPACE_URI,
					PresentationConstants.ATTR_DISPLAY_HEADER)).booleanValue();
		}
		return false;
	}

	public boolean getAttrPresentationDisplayFooter() {
		if (hasAttributeNS(PresentationConstants.PRESENTATION_NAMESPACE_URI,
				PresentationConstants.ATTR_DISPLAY_FOOTER)) {
			return new Boolean(getAttributeNS(
					PresentationConstants.PRESENTATION_NAMESPACE_URI,
					PresentationConstants.ATTR_DISPLAY_FOOTER)).booleanValue();
		}
		return false;
	}

	public boolean getAttrPresentationDisplayPageNumber() {
		if (hasAttributeNS(PresentationConstants.PRESENTATION_NAMESPACE_URI,
				PresentationConstants.ATTR_DISPLAY_PAGE_NUMBER)) {
			return new Boolean(getAttributeNS(
					PresentationConstants.PRESENTATION_NAMESPACE_URI,
					PresentationConstants.ATTR_DISPLAY_PAGE_NUMBER))
					.booleanValue();
		}
		return false;
	}

	public boolean getAttrPresentationDisplayDateTime() {
		if (hasAttributeNS(PresentationConstants.PRESENTATION_NAMESPACE_URI,
				PresentationConstants.ATTR_DISPLAY_DATE_TIME)) {
			return new Boolean(getAttributeNS(
					PresentationConstants.PRESENTATION_NAMESPACE_URI,
					PresentationConstants.ATTR_DISPLAY_DATE_TIME))
					.booleanValue();
		}
		return false;
	}

	public String getAttrDrawBackgroundSize() {
		if (hasAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ATTR_BACKGROUND_SIZE))
			return getAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ATTR_BACKGROUND_SIZE);
		return null;
	}

	public String getAttrDrawFill() {
		if (hasAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ATTR_FILL))
			return getAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ATTR_FILL);
		return null;
	}
}