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
import org.eclipse.actf.model.dom.odf.chart.AxisElement;
import org.eclipse.actf.model.dom.odf.chart.ChartConstants;
import org.w3c.dom.Element;


class AxisElementImpl extends ODFStylableElementImpl implements AxisElement {
	private static final long serialVersionUID = 7482924035365697274L;

	protected AxisElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public String getAttrChartDimension() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_DIMENSION))
			return getAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_DIMENSION);
		return null;
	}

	public String getAttrChartName() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_NAME))
			return getAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_NAME);
		return null;
	}
}