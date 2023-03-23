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

import org.eclipse.actf.model.dom.odf.ODFConstants.ContentType;
import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.impl.ODFElementImpl;
import org.eclipse.actf.model.dom.odf.chart.LegendElement;
import org.eclipse.actf.model.dom.odf.chart.PlotAreaElement;
import org.eclipse.actf.model.dom.odf.chart.TitleElement;
import org.eclipse.actf.model.dom.odf.office.ChartElement;
import org.eclipse.actf.model.dom.odf.range.IContentRange;
import org.eclipse.actf.model.dom.odf.table.TableElement;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


class ChartElementImpl extends ODFElementImpl implements ChartElement {
	private static final long serialVersionUID = 8537717378251071515L;

	protected ChartElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public org.eclipse.actf.model.dom.odf.chart.ChartElement getChartChartElement() {
		NodeList children = getChildNodes();
		if ((children != null) && (children.getLength() != 0)) {
			for (int i = 0; i < children.getLength(); i++) {
				if (children.item(i) instanceof org.eclipse.actf.model.dom.odf.chart.ChartElement)
					return (org.eclipse.actf.model.dom.odf.chart.ChartElement) children
							.item(i);
			}
		}
		return null;
	}

	public TitleElement getChartTitleElement() {
		org.eclipse.actf.model.dom.odf.chart.ChartElement chartChartElemnt = getChartChartElement();
		NodeList children = chartChartElemnt.getChildNodes();
		if ((children != null) && (children.getLength() != 0)) {
			for (int i = 0; i < children.getLength(); i++) {
				if (children.item(i) instanceof TitleElement)
					return (TitleElement) children.item(i);
			}
		}
		return null;
	}

	public PlotAreaElement getChartPlotAreaElement() {
		org.eclipse.actf.model.dom.odf.chart.ChartElement chartChartElemnt = getChartChartElement();
		NodeList children = chartChartElemnt.getChildNodes();
		if ((children != null) && (children.getLength() != 0)) {
			for (int i = 0; i < children.getLength(); i++) {
				if (children.item(i) instanceof PlotAreaElement)
					return (PlotAreaElement) children.item(i);
			}
		}
		return null;
	}

	public LegendElement getChartLegendElement() {
		org.eclipse.actf.model.dom.odf.chart.ChartElement chartChartElemnt = getChartChartElement();
		NodeList children = chartChartElemnt.getChildNodes();
		if ((children != null) && (children.getLength() != 0)) {
			for (int i = 0; i < children.getLength(); i++) {
				if (children.item(i) instanceof LegendElement)
					return (LegendElement) children.item(i);
			}
		}
		return null;
	}

	public TableElement getChartTableElement() {
		org.eclipse.actf.model.dom.odf.chart.ChartElement chartChartElemnt = getChartChartElement();
		NodeList children = chartChartElemnt.getChildNodes();
		if ((children != null) && (children.getLength() != 0)) {
			for (int i = 0; i < children.getLength(); i++) {
				if (children.item(i) instanceof TableElement)
					return (TableElement) children.item(i);
			}
		}
		return null;
	}

	public ContentType getContentType() {
		return ContentType.CHART;
	}

	public IContentRange createRange() {
		// TODO Auto-generated method stub
		return null;
	}
}