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

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.base.impl.AbstractODFNodeFactory;
import org.eclipse.actf.model.dom.odf.chart.ChartConstants;
import org.w3c.dom.Element;

public class ChartNodeFactory extends AbstractODFNodeFactory {
	private static final Map<String, Class<? extends ODFElement>> tagMap = new HashMap<String, Class<? extends ODFElement>>();

	static {
		tagMap.put(ChartConstants.ELEMENT_CHART, ChartElementImpl.class);
		tagMap.put(ChartConstants.ELEMENT_TITLE, TitleElementImpl.class);
		tagMap.put(ChartConstants.ELEMENT_LEGEND, LegendElementImpl.class);
		tagMap.put(ChartConstants.ELEMENT_PLOT_AREA, PlotAreaElementImpl.class);
		tagMap.put(ChartConstants.ELEMENT_AXIS, AxisElementImpl.class);
		tagMap.put(ChartConstants.ELEMENT_CATEGORIES,
				CategoriesElementImpl.class);
		tagMap.put(ChartConstants.ELEMENT_GRID, GridElementImpl.class);
		tagMap.put(ChartConstants.ELEMENT_SERIES, SeriesElementImpl.class);
		tagMap.put(ChartConstants.ELEMENT_DATA_POINT,
				DataPointElementImpl.class);
		tagMap.put(ChartConstants.ELEMENT_STOCK_GAIN_MARKER,
				StockGainMarkerElementImpl.class);
		tagMap.put(ChartConstants.ELEMENT_STOCK_LOSS_MARKER,
				StockLossMarkerElementImpl.class);
		tagMap.put(ChartConstants.ELEMENT_STOCK_RANGE_LINE,
				StockRangeLineElementImpl.class);
		tagMap.put(ChartConstants.ELEMENT_WALL, WallElementImpl.class);
		tagMap.put(ChartConstants.ELEMENT_FLOOR, FloorElementImpl.class);
	}

	public static Element createElement(ODFDocument odfDoc, String tagName,
			Element element) {
		Class<? extends ODFElement> cs = tagMap.get(tagName);
		if (null == cs) {
			return null;
		}

		try {
			return (Element)findElementConstractor(cs).newInstance(
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