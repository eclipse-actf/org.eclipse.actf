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
import org.eclipse.actf.model.dom.odf.base.impl.ODFElementImpl;
import org.eclipse.actf.model.dom.odf.chart.ChartConstants;
import org.eclipse.actf.model.dom.odf.chart.DataPointElement;
import org.w3c.dom.Element;


class DataPointElementImpl extends ODFElementImpl implements DataPointElement {
	private static final long serialVersionUID = -7212238489251171612L;

	protected DataPointElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public int getAttrChartRepeated() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_REPEATED)) {
			return new Integer(getAttributeNS(
					ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_REPEATED)).intValue();
		}
		return -1;
	}
}