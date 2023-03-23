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
package org.eclipse.actf.model.dom.odf.chart.impl;

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.impl.ODFStylableElementImpl;
import org.eclipse.actf.model.dom.odf.chart.ChartConstants;
import org.eclipse.actf.model.dom.odf.chart.ChartElement;
import org.eclipse.actf.model.dom.odf.svg.SVGConstants;
import org.w3c.dom.Element;


class ChartElementImpl extends ODFStylableElementImpl implements ChartElement {
	private static final long serialVersionUID = 8422349783806697219L;

	protected ChartElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public String getAttrSvgWidth() {
		if (hasAttributeNS(SVGConstants.SVG_NAMESPACE_URI,
				SVGConstants.ATTR_WIDTH))
			return getAttributeNS(SVGConstants.SVG_NAMESPACE_URI,
					SVGConstants.ATTR_WIDTH);
		return null;
	}

	public String getAttrSvgHeight() {
		if (hasAttributeNS(SVGConstants.SVG_NAMESPACE_URI,
				SVGConstants.ATTR_HEIGHT))
			return getAttributeNS(SVGConstants.SVG_NAMESPACE_URI,
					SVGConstants.ATTR_HEIGHT);
		return null;
	}

	public String getAttrChartClass() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_CLASS))
			return getAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_CLASS);
		return null;
	}
}