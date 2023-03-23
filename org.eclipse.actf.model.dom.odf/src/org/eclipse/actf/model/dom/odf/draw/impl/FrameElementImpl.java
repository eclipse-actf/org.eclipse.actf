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

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.base.impl.DrawingObjectElementImpl;
import org.eclipse.actf.model.dom.odf.draw.DrawConstants;
import org.eclipse.actf.model.dom.odf.draw.FrameElement;
import org.eclipse.actf.model.dom.odf.svg.SVGConstants;
import org.eclipse.actf.model.dom.odf.table.TableConstants;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


class FrameElementImpl extends DrawingObjectElementImpl implements FrameElement {
	private static final long serialVersionUID = 1022779482608752448L;

	protected FrameElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public String getAttrTableEndCellAddress() {
		if (hasAttributeNS(TableConstants.TABLE_NAMESPACE_URI,
				TableConstants.ATTR_END_CELL_ADDRESS))
			return getAttributeNS(TableConstants.TABLE_NAMESPACE_URI,
					TableConstants.ATTR_END_CELL_ADDRESS);
		return null;
	}

	public String getAttrTableEndX() {
		if (hasAttributeNS(TableConstants.TABLE_NAMESPACE_URI,
				TableConstants.ATTR_END_X))
			return getAttributeNS(TableConstants.TABLE_NAMESPACE_URI,
					TableConstants.ATTR_END_X);
		return null;
	}

	public String getAttrTableEndY() {
		if (hasAttributeNS(TableConstants.TABLE_NAMESPACE_URI,
				TableConstants.ATTR_END_Y))
			return getAttributeNS(TableConstants.TABLE_NAMESPACE_URI,
					TableConstants.ATTR_END_Y);
		return null;
	}

	public int getAttrDrawZIndex() {
		if (hasAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ATTR_Z_INDEX)) {
			return new Integer(getAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ATTR_Z_INDEX)).intValue();
		}
		return -1;
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

	private List<ODFElement> getChildElements() {
		List<ODFElement> list = new Vector<ODFElement>();
		NodeList nl = this.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node child = nl.item(i);
			if (child instanceof ODFElement) {
				list.add((ODFElement)child);
			}
		}
		return list;
	}

	public long getContentSize() {
		return getChildElements().size();
	}

	public Iterator<ODFElement> getChildIterator() {
		return getChildElements().iterator();
	}
}