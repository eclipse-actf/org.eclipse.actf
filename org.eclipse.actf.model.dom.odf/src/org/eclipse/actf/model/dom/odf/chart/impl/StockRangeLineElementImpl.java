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
import org.eclipse.actf.model.dom.odf.chart.StockRangeLineElement;
import org.w3c.dom.Element;


class StockRangeLineElementImpl extends ODFStylableElementImpl implements
		StockRangeLineElement {
	private static final long serialVersionUID = 6328237457664667672L;

	protected StockRangeLineElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}
}