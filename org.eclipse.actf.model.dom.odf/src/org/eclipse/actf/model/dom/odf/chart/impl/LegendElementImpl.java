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
import org.eclipse.actf.model.dom.odf.chart.LegendElement;
import org.eclipse.actf.model.dom.odf.svg.SVGConstants;
import org.w3c.dom.Element;


class LegendElementImpl extends ODFStylableElementImpl implements LegendElement {
	private static final long serialVersionUID = 2552292055359445007L;

	protected LegendElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public String getAttrChartLegendPosition() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_LEGEND_POSITION))
			return getAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_LEGEND_POSITION);
		return null;
	}

	public String getAttrSvgX() {
		if (hasAttributeNS(SVGConstants.SVG_NAMESPACE_URI, SVGConstants.ATTR_X))
			return getAttributeNS(SVGConstants.SVG_NAMESPACE_URI,
					SVGConstants.ATTR_X);
		return null;
	}

	public String getAttrSvgY() {
		if (hasAttributeNS(SVGConstants.SVG_NAMESPACE_URI, SVGConstants.ATTR_Y))
			return getAttributeNS(SVGConstants.SVG_NAMESPACE_URI,
					SVGConstants.ATTR_Y);
		return null;
	}
}