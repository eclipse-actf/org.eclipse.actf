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
 * Interface for &lt;chart:legend&gt; element.
 */
public interface LegendElement extends ODFElement, IStylable {
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
	 * Return chart:legend-position attribute
	 * 
	 * @return String
	 */			
	public String getAttrChartLegendPosition();
}