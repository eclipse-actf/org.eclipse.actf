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
package org.eclipse.actf.model.dom.odf.style;

/**
 * Interface for &lt;style:chart-properties&gt; element.
 */
public interface ChartPropertiesElement extends StylePropertiesBase {
	public String getAttrStyleDirection();

	public boolean getAttrChartJapaneseCandleStick();

	public boolean getAttrChartStockWithVolume();

	public boolean getAttrChartThreeDimensional();

	public boolean getAttrChartDeep();

	public boolean getAttrChartLines();

	public String getAttrChartInterpolation();

	public String getAttrChartSymbolType();

	public boolean getAttrChartVertical();

	public int getAttrChartLinesUsed();

	public boolean getAttrChartConnectBars();

	public String getAttrChartSeriesSource();

	public boolean getAttrChartMeanValue();

	public double getAttrChartErrorMargin();

	public double getAttrChartErrorLowerLimit();

	public double getAttrChartErrorUpperLimit();

	public String getAttrChartErrorCategory();

	public double getAttrChartErrorPercentage();

	public String getAttrChartRegressionType();

	public String getAttrChartDataLabelNumber();

	public boolean getAttrChartDataLabelText();

	public boolean getAttrChartDataLabelSymbol();

	public boolean getAttrChartDisplayLabel();

	public boolean getAttrChartTickMarksMajorInner();

	public boolean getAttrChartTickMarksMajorOuter();

	public boolean getAttrChartLogarithmic();

	public boolean getAttrChartTextOverlap();

	public boolean getAttrTextLineBreak();

	public String getAttrChartLabelArrangement();

	public boolean getAttrChartVisible();

	public int getAttrChartGapWidth();

	public int getAttrChartOverlap();

	public String getAttrChartSolidType();

	public double getAttrChartOrigin();

	public double getAttrChartMinimum();

	public double getAttrChartMaximum();
}