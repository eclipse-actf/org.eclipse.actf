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
import org.eclipse.actf.model.dom.odf.content.IStyleListener;
import org.eclipse.actf.model.dom.odf.style.StyleConstants;
import org.eclipse.actf.model.dom.odf.style.StyleElement;
import org.eclipse.actf.model.dom.odf.style.StylePropertiesBase;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class StyleElementImpl extends ODFElementImpl implements StyleElement {
	private static final long serialVersionUID = 9075754119248346645L;

	protected StyleElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public StyleElement createChild(String family) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getFamily() {
		if (hasAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_FAMILY))
			return getAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_FAMILY);
		return null;
	}

	public String getName() {
		if (hasAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_NAME))
			return getAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_NAME);
		return null;
	}

	public void setName(String name) {
		setAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_NAME, name);
	}

	public StylePropertiesBase getPropertyElement(long idx) {
		int count = 0;
		NodeList nl = getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (node instanceof StylePropertiesBase) {
				if (idx == count)
					return (StylePropertiesBase) node;
				count++;
			}
		}
		return null;
	}

	public long getPropertySize() {
		int count = 0;
		NodeList nl = getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (node instanceof StylePropertiesBase) {
				count++;
			}
		}
		return count;
	}

	public void putPropertyElement(StylePropertiesBase property) {
		// TODO Auto-generated method stub

	}

	public void addListener(IStyleListener listener, String topic) {
		// TODO Auto-generated method stub

	}

	public void removeListener(IStyleListener listener, String topic) {
		// TODO Auto-generated method stub

	}

	public String getAttrStyleMasterPageName() {
		if (hasAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_MASTER_PAGE_NAME))
			return getAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_MASTER_PAGE_NAME);
		return null;
	}

	public String getAttrStyleParentStyleName() {
		if (hasAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_PARENT_STYLE_NAME))
			return getAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_PARENT_STYLE_NAME);
		return null;
	}

	public String getAttrStyleDataStyleName() {
		if (hasAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_DATA_STYLE_NAME))
			return getAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_DATA_STYLE_NAME);
		return null;
	}
}