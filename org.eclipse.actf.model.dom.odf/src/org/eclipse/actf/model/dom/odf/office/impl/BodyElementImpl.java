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

import java.util.List;
import java.util.Vector;

import org.eclipse.actf.model.dom.odf.base.ContentBaseElement;
import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.base.impl.ODFElementImpl;
import org.eclipse.actf.model.dom.odf.office.BodyElement;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

class BodyElementImpl extends ODFElementImpl implements BodyElement {
	private static final long serialVersionUID = -3847937030086999653L;

	static private final List<Class<? extends ODFElement>> validBodyRootElement = new Vector<Class<? extends ODFElement>>();
	static {
		validBodyRootElement.add(DrawingElementImpl.class);
		validBodyRootElement.add(SpreadSheetElementImpl.class);
		validBodyRootElement.add(ChartElementImpl.class);
	}

	protected BodyElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public ContentBaseElement getContent() {
		NodeList children = getChildNodes();
		if ((children != null) && (children.getLength() != 0)) {
			for (int i = 0; i < children.getLength(); i++) {
				if (children.item(i) instanceof ContentBaseElement)
					return (ContentBaseElement) children.item(i);
			}
		}
		return null;
	}
}