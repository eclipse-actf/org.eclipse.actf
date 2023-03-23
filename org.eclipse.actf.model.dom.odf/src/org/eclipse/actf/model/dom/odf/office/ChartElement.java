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
package org.eclipse.actf.model.dom.odf.office;

import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.chart.LegendElement;
import org.eclipse.actf.model.dom.odf.chart.PlotAreaElement;
import org.eclipse.actf.model.dom.odf.chart.TitleElement;
import org.eclipse.actf.model.dom.odf.table.TableElement;

/**
 * Interface for &lt;office:chart&gt; element.
 */
public interface ChartElement extends ODFElement {
	public org.eclipse.actf.model.dom.odf.chart.ChartElement getChartChartElement();

	public TitleElement getChartTitleElement();

	public PlotAreaElement getChartPlotAreaElement();

	public LegendElement getChartLegendElement();

	public TableElement getChartTableElement();
}