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

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.base.impl.AbstractODFNodeFactory;
import org.eclipse.actf.model.dom.odf.draw.DrawConstants;
import org.w3c.dom.Element;

public class DrawNodeFactory extends AbstractODFNodeFactory {
	private static final Map<String, Class<? extends ODFElement>> tagMap = new HashMap<String, Class<? extends ODFElement>>();

	static {
		tagMap.put(DrawConstants.ELEMENT_AREA_CIRCLE,
				AreaCircleElementImpl.class);
		tagMap.put(DrawConstants.ELEMENT_AREA_POLYGON,
				AreaPolygonElementImpl.class);
		tagMap.put(DrawConstants.ELEMENT_AREA_RECTANGLE,
				AreaRectangleElementImpl.class);
		tagMap.put(DrawConstants.ELEMENT_CAPTION, CaptionElementImpl.class);
		tagMap.put(DrawConstants.ELEMENT_CIRCLE, CircleElementImpl.class);
		tagMap.put(DrawConstants.ELEMENT_CONNECTOR, ConnectorElementImpl.class);
		tagMap.put(DrawConstants.ELEMENT_CONTROL, ControlElementImpl.class);
		tagMap.put(DrawConstants.ELEMENT_CUSTOM_SHAPE,
				CustomShapeElementImpl.class);
		tagMap.put(DrawConstants.ELEMENT_ELLIPSE, EllipseElementImpl.class);
		tagMap
				.put(DrawConstants.ELEMENT_FILL_IMAGE,
						FillImageElementImpl.class);
		tagMap.put(DrawConstants.ELEMENT_FRAME, FrameElementImpl.class);
		tagMap.put(DrawConstants.ELEMENT_G, GElementImpl.class);
		tagMap.put(DrawConstants.ELEMENT_GRADIENT, GradientElementImpl.class);
		tagMap.put(DrawConstants.ELEMENT_HATCH, HatchElementImpl.class);
		tagMap.put(DrawConstants.ELEMENT_IMAGE, ImageElementImpl.class);
		tagMap.put(DrawConstants.ELEMENT_IMAGE_MAP, ImageMapElementImpl.class);
		tagMap.put(DrawConstants.ELEMENT_LINE, LineElementImpl.class);
		tagMap.put(DrawConstants.ELEMENT_MEASURE, MeasureElementImpl.class);
		tagMap.put(DrawConstants.ELEMENT_OBJECT, ObjectElementImpl.class);
		tagMap
				.put(DrawConstants.ELEMENT_OBJECT_OLE,
						ObjectOleElementImpl.class);
		tagMap.put(DrawConstants.ELEMENT_OPACITY, OpacityElementImpl.class);
		tagMap.put(DrawConstants.ELEMENT_PAGE, PageElementImpl.class);
		tagMap.put(DrawConstants.ELEMENT_PAGE_THUMBNAIL,
				PageThumbnailElementImpl.class);
		tagMap.put(DrawConstants.ELEMENT_PATH, PathElementImpl.class);
		tagMap.put(DrawConstants.ELEMENT_POLYGON, PolygonElementImpl.class);
		tagMap.put(DrawConstants.ELEMENT_POLYLINE, PolylineElementImpl.class);
		tagMap.put(DrawConstants.ELEMENT_RECT, RectElementImpl.class);
		tagMap.put(DrawConstants.ELEMENT_REGULAR_POLYGON,
				RegularPolygonElementImpl.class);
		tagMap.put(DrawConstants.ELEMENT_STROKE_DASH,
				StrokeDashElementImpl.class);
		tagMap.put(DrawConstants.ELEMENT_TEXT_BOX, TextBoxElementImpl.class);
	}

	public static Element createElement(ODFDocument odfDoc, String tagName,
			Element element) {
		Class<? extends ODFElement> cs = tagMap.get(tagName);
		if (null == cs) {
			return null;
		}

		try {
			return findElementConstractor(cs).newInstance(
					new Object[] { odfDoc, element });
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
}