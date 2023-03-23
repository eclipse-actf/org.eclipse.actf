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
package org.eclipse.actf.model.dom.odf.chart;

import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.content.IStylable;

/**
 * Interface for &lt;chart:plot-area&gt; element.
 */
public interface PlotAreaElement extends ODFElement, IStylable {
	/**
	 * Return table:cell-range-address attribute
	 * 
	 * @return String
	 */	
	public String getAttrTableCellRangeAddress();

	/**
	 * Return chart:data-source-has-labels attribute
	 * 
	 * @return String
	 */		
	public String getAttrChartDataSourceHasLabels();

	/**
	 * Return chart:table-number-list attribute
	 * 
	 * @return String
	 */		
	public String getAttrChartTableNumberList();

	/**
	 * Return svg:x attribute
	 * 
	 * @return String
	 */		
	public String getAttrSvgX();

	/**
	 * Return svg:y attribute
	 * 
	 * @return String
	 */	
	public String getAttrSvgY();

	/**
	 * Return svg:width attribute
	 * 
	 * @return String
	 */	
	public String getAttrSvgWidth();

	/**
	 * Return svg:height attribute
	 * 
	 * @return String
	 */		
	public String getAttrSvgHeight();

	/**
	 * Return dr3d:vrp attribute
	 * 
	 * @return String
	 */		
	public String getAttrDr3dVrp();

	/**
	 * Return dr3d:vpn attribute
	 * 
	 * @return String
	 */	
	public String getAttrDr3dVpn();

	/**
	 * Return dr3d:vup attribute
	 * 
	 * @return String
	 */	
	public String getAttrDr3dVup();

	/**
	 * Return dr3d:projection attribute
	 * 
	 * @return String
	 */		
	public String getAttrDr3dProjection();

	/**
	 * Return dr3d:distance attribute
	 * 
	 * @return String
	 */		
	public String getAttrDr3dDistance();

	/**
	 * Return dr3d:focal-length attribute
	 * 
	 * @return String
	 */		
	public String getAttrDr3dFocalLength();

	/**
	 * Return dr3d:shadow-slant attribute
	 * 
	 * @return String
	 */	
	public int getAttrDr3dShadowSlant();

	/**
	 * Return dr3d:shade-mode attribute
	 * 
	 * @return String
	 */		
	public String getAttrDr3dShadeMode();

	/**
	 * Return dr3d:ambient-color attribute
	 * 
	 * @return String
	 */		
	public String getAttrDr3dAmbientColor();

	/**
	 * Return dr3d:lighting-mode attribute
	 * 
	 * @return boolean
	 */		
	public boolean getAttrDr3dLightingMode();
}