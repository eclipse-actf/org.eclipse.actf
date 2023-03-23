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

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.draw.AreaRectangleElement;
import org.w3c.dom.Element;


class AreaRectangleElementImpl extends ImageMapAreaElementImpl implements
		AreaRectangleElement {
	private static final long serialVersionUID = 2649141873537166703L;

	protected AreaRectangleElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}
}