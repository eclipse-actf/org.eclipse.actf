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
package org.eclipse.actf.model.dom.odf.table.impl;

import java.util.Iterator;

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.base.impl.ODFElementImpl;
import org.eclipse.actf.model.dom.odf.base.impl.ODFStylableElementImpl;
import org.eclipse.actf.model.dom.odf.content.IStyleListener;
import org.eclipse.actf.model.dom.odf.range.ITextElementContainer;
import org.eclipse.actf.model.dom.odf.style.StyleConstants;
import org.eclipse.actf.model.dom.odf.style.StyleElement;
import org.eclipse.actf.model.dom.odf.table.CoveredTableCellElement;
import org.eclipse.actf.model.dom.odf.table.TableConstants;
import org.w3c.dom.Element;


class CoveredTableCellElementImpl extends ODFStylableElementImpl implements
		CoveredTableCellElement {
	private static final long serialVersionUID = -7373283588048632663L;

	protected CoveredTableCellElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public void setStyle(StyleElement style) {
		// TODO Auto-generated method stub

	}

	public StyleElement getStyle() {
		Element root = getOwnerDocument().getDocumentElement();
		if (root instanceof ODFElementImpl) {
			String styleName = getAttributeNS(
					TableConstants.TABLE_NAMESPACE_URI,
					TableConstants.ATTR_STYLE_NAME);
			ODFElement elem = ((ODFElementImpl) root).findElementByAttrValue(
					StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ELEMENT_STYLE,
					StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_NAME, styleName);
			if ((elem != null) && (elem instanceof StyleElement))
				return (StyleElement) elem;
		}
		return null;
	}

	public void addStyleListener(IStyleListener listener, String topic) {
		// TODO Auto-generated method stub

	}

	public void removeStyleListener(IStyleListener listener, String topic) {
		// TODO Auto-generated method stub

	}

	public Iterator<ITextElementContainer> getChildIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	public long getContentSize() {
		// TODO Auto-generated method stub
		return 0;
	}
}
