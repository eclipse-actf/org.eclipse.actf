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
import org.eclipse.actf.model.dom.odf.chart.ChartConstants;
import org.eclipse.actf.model.dom.odf.style.ChartPropertiesElement;
import org.eclipse.actf.model.dom.odf.style.StyleConstants;
import org.eclipse.actf.model.dom.odf.text.TextConstants;
import org.w3c.dom.Element;


class ChartPropertiesElementImpl extends ODFElementImpl implements
		ChartPropertiesElement {
	private static final long serialVersionUID = 5381557644775039533L;

	protected ChartPropertiesElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAttrStyleDirection() {
		if (hasAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_DIRECTION))
			return getAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_DIRECTION);
		return null;
	}

	public boolean getAttrChartJapaneseCandleStick() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_JAPANESE_CANDLE_STICK)) {
			return new Boolean(getAttributeNS(
					ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_JAPANESE_CANDLE_STICK)).booleanValue();
		}
		return false;
	}

	public boolean getAttrChartStockWithVolume() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_STOCK_WITH_VOLUME)) {
			return new Boolean(getAttributeNS(
					ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_STOCK_WITH_VOLUME)).booleanValue();
		}
		return false;
	}

	public boolean getAttrChartThreeDimensional() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_THREE_DIMENSIONAL)) {
			return new Boolean(getAttributeNS(
					ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_THREE_DIMENSIONAL)).booleanValue();
		}
		return false;
	}

	public boolean getAttrChartDeep() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_DEEP)) {
			return new Boolean(getAttributeNS(
					ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_DEEP)).booleanValue();
		}
		return false;
	}

	public boolean getAttrChartLines() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_LINES)) {
			return new Boolean(getAttributeNS(
					ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_LINES)).booleanValue();
		}
		return false;
	}

	public String getAttrChartInterpolation() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_INTERPOLATION))
			return getAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_INTERPOLATION);
		return null;
	}

	public String getAttrChartSymbolType() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_SYMBOL_TYPE))
			return getAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_SYMBOL_TYPE);
		return null;
	}

	public boolean getAttrChartVertical() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_VERTICAL)) {
			return new Boolean(getAttributeNS(
					ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_VERTICAL)).booleanValue();
		}
		return false;
	}

	public int getAttrChartLinesUsed() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_LINES_USED)) {
			return new Integer(getAttributeNS(
					ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_LINES_USED)).intValue();
		}
		return -1;
	}

	public boolean getAttrChartConnectBars() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_CONNECT_BARS)) {
			return new Boolean(getAttributeNS(
					ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_CONNECT_BARS)).booleanValue();
		}
		return false;
	}

	public String getAttrChartSeriesSource() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_SERIES_SOURCE))
			return getAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_SERIES_SOURCE);
		return null;
	}

	public boolean getAttrChartMeanValue() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_MEAN_VALUE)) {
			return new Boolean(getAttributeNS(
					ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_MEAN_VALUE)).booleanValue();
		}
		return false;
	}

	public double getAttrChartErrorMargin() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_ERROR_MARGIN)) {
			return new Double(getAttributeNS(
					ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_ERROR_MARGIN)).doubleValue();
		}
		return -1.0;
	}

	public double getAttrChartErrorLowerLimit() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_ERROR_LOWER_LIMIT)) {
			return new Double(getAttributeNS(
					ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_ERROR_LOWER_LIMIT)).doubleValue();
		}
		return -1.0;
	}

	public double getAttrChartErrorUpperLimit() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_ERROR_UPPER_LIMIT)) {
			return new Double(getAttributeNS(
					ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_ERROR_UPPER_LIMIT)).doubleValue();
		}
		return -1.0;
	}

	public String getAttrChartErrorCategory() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_ERROR_CATEGORY))
			return getAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_ERROR_CATEGORY);
		return null;
	}

	public double getAttrChartErrorPercentage() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_ERROR_PERCENTAGE)) {
			return new Double(getAttributeNS(
					ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_ERROR_PERCENTAGE)).doubleValue();
		}
		return -1.0;
	}

	public String getAttrChartRegressionType() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_REGRESSION_TYPE))
			return getAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_REGRESSION_TYPE);
		return null;
	}

	public String getAttrChartDataLabelNumber() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_DATA_LABEL_NUMBER))
			return getAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_DATA_LABEL_NUMBER);
		return null;
	}

	public boolean getAttrChartDataLabelText() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_DATA_LABEL_TEXT)) {
			return new Boolean(getAttributeNS(
					ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_DATA_LABEL_TEXT)).booleanValue();
		}
		return false;
	}

	public boolean getAttrChartDataLabelSymbol() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_DATA_LABEL_SYMBOL)) {
			return new Boolean(getAttributeNS(
					ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_DATA_LABEL_SYMBOL)).booleanValue();
		}
		return false;
	}

	public boolean getAttrChartDisplayLabel() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_DISPLAY_LABEL)) {
			return new Boolean(getAttributeNS(
					ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_DISPLAY_LABEL)).booleanValue();
		}
		return false;
	}

	public boolean getAttrChartTickMarksMajorInner() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_TICK_MARKS_MAJOR_INNER)) {
			return new Boolean(getAttributeNS(
					ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_TICK_MARKS_MAJOR_INNER)).booleanValue();
		}
		return false;
	}

	public boolean getAttrChartTickMarksMajorOuter() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_TICK_MARKS_MAJOR_OUTER)) {
			return new Boolean(getAttributeNS(
					ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_TICK_MARKS_MAJOR_OUTER)).booleanValue();
		}
		return false;
	}

	public boolean getAttrChartLogarithmic() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_LOGARITHMIC)) {
			return new Boolean(getAttributeNS(
					ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_LOGARITHMIC)).booleanValue();
		}
		return false;
	}

	public boolean getAttrChartTextOverlap() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_TEXT_OVERLAP)) {
			return new Boolean(getAttributeNS(
					ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_TEXT_OVERLAP)).booleanValue();
		}
		return false;
	}

	public boolean getAttrTextLineBreak() {
		if (hasAttributeNS(TextConstants.TEXT_NAMESPACE_URI,
				TextConstants.ATTR_LINE_BREAK)) {
			return new Boolean(getAttributeNS(TextConstants.TEXT_NAMESPACE_URI,
					TextConstants.ATTR_LINE_BREAK)).booleanValue();
		}
		return false;
	}

	public String getAttrChartLabelArrangement() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_LABEL_ARRANGEMENT))
			return getAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_LABEL_ARRANGEMENT);
		return null;
	}

	public boolean getAttrChartVisible() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_VISIBLE)) {
			return new Boolean(getAttributeNS(
					ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_VISIBLE)).booleanValue();
		}
		return false;
	}

	public int getAttrChartGapWidth() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_GAP_WIDTH)) {
			return new Integer(getAttributeNS(
					ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_GAP_WIDTH)).intValue();
		}
		return -1;
	}

	public int getAttrChartOverlap() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_OVERLAP)) {
			return new Integer(getAttributeNS(
					ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_OVERLAP)).intValue();
		}
		return -1;
	}

	public String getAttrChartSolidType() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_SOLID_TYPE))
			return getAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_SOLID_TYPE);
		return null;
	}

	public double getAttrChartOrigin() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_ORIGIN)) {
			return new Double(getAttributeNS(
					ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_ORIGIN)).doubleValue();
		}
		return -1.0;
	}

	public double getAttrChartMinimum() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_MINIMUM)) {
			return new Double(getAttributeNS(
					ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_MINIMUM)).doubleValue();
		}
		return -1.0;
	}

	public double getAttrChartMaximum() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_MAXIMUM)) {
			return new Double(getAttributeNS(
					ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_MAXIMUM)).doubleValue();
		}
		return -1.0;
	}
}