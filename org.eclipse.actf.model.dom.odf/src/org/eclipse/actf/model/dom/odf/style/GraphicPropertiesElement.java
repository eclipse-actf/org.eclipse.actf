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

import org.eclipse.actf.model.dom.odf.draw.GradientElement;
import org.eclipse.actf.model.dom.odf.draw.OpacityElement;

/**
 * Interface for &lt;style:graphic-properties&gt; element.
 */
public interface GraphicPropertiesElement extends StylePropertiesBase {
	public String getAttrDrawStroke();

	public String getAttrDrawStrokeDash();

	public String getAttrSvgStrokeWidth();

	public String getAttrSvgStrokeColor();

	public String getAttrDrawMarkerStartWidth();

	public String getAttrDrawMarkerEndWidth();

	public String getAttrSvgStrokeOpacity();

	public String getAttrDrawOpacity();

	public OpacityElement getAttrDrawOpacityName();

	public String getAttrDrawFill();

	public String getAttrDrawFillColor();

	public GradientElement getAttrDrawFillGradientName();

	public int getAttrDrawGradientStepCount();

	public String getAttrDrawFillImageName();

	public String getAttrDrawFillImageWidth();

	public String getAttrDrawFillImageHeight();

	public String getAttrStyleRepeat();

	public String getAttrDrawFillImageRefPointX();

	public String getAttrDrawFillImageRefPointY();

	public String getAttrDrawFillImageRefPoint();

	public String getAttrDrawTileRepeatOffset();

	public String getAttrStyleHorizontalPos();
}