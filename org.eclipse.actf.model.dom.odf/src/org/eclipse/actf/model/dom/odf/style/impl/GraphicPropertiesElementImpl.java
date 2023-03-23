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
import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.base.impl.ODFElementImpl;
import org.eclipse.actf.model.dom.odf.draw.DrawConstants;
import org.eclipse.actf.model.dom.odf.draw.FillImageElement;
import org.eclipse.actf.model.dom.odf.draw.GradientElement;
import org.eclipse.actf.model.dom.odf.draw.OpacityElement;
import org.eclipse.actf.model.dom.odf.style.GraphicPropertiesElement;
import org.eclipse.actf.model.dom.odf.style.StyleConstants;
import org.eclipse.actf.model.dom.odf.svg.SVGConstants;
import org.w3c.dom.Element;


class GraphicPropertiesElementImpl extends ODFElementImpl implements
		GraphicPropertiesElement {
	private static final long serialVersionUID = 5150171870087297027L;

	protected GraphicPropertiesElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAttrDrawStroke() {
		if (hasAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ATTR_STROKE))
			return getAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ATTR_STROKE);
		return null;
	}

	public String getAttrDrawStrokeDash() {
		if (hasAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ATTR_STROKE_DASH))
			return getAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ATTR_STROKE_DASH);
		return null;
	}

	public String getAttrSvgStrokeWidth() {
		if (hasAttributeNS(SVGConstants.SVG_NAMESPACE_URI,
				SVGConstants.ATTR_STROKE_WIDTH))
			return getAttributeNS(SVGConstants.SVG_NAMESPACE_URI,
					SVGConstants.ATTR_STROKE_WIDTH);
		return null;
	}

	public String getAttrSvgStrokeColor() {
		if (hasAttributeNS(SVGConstants.SVG_NAMESPACE_URI,
				SVGConstants.ATTR_STROKE_COLOR))
			return getAttributeNS(SVGConstants.SVG_NAMESPACE_URI,
					SVGConstants.ATTR_STROKE_COLOR);
		return null;
	}

	public String getAttrDrawMarkerStartWidth() {
		if (hasAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ATTR_MARKER_START_WIDTH))
			return getAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ATTR_MARKER_START_WIDTH);
		return null;
	}

	public String getAttrDrawMarkerEndWidth() {
		if (hasAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ATTR_MARKER_END_WIDTH))
			return getAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ATTR_MARKER_END_WIDTH);
		return null;
	}

	public String getAttrSvgStrokeOpacity() {
		if (hasAttributeNS(SVGConstants.SVG_NAMESPACE_URI,
				SVGConstants.ATTR_STROKE_OPACITY))
			return getAttributeNS(SVGConstants.SVG_NAMESPACE_URI,
					SVGConstants.ATTR_STROKE_OPACITY);
		return null;
	}

	public String getAttrDrawOpacity() {
		if (hasAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ATTR_OPACITY))
			return getAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ATTR_OPACITY);
		return null;
	}

	public OpacityElement getAttrDrawOpacityName() {
		if (hasAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ATTR_OPACITY_NAME)) {
			String str = getAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ATTR_OPACITY_NAME);
			ODFElement opacityElement = findElementByAttrValueFromStyleDoc(
					DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ELEMENT_OPACITY,
					DrawConstants.DRAW_NAMESPACE_URI, DrawConstants.ATTR_NAME,
					str);
			if ((opacityElement != null)
					&& (opacityElement instanceof OpacityElement)) {
				return (OpacityElement) opacityElement;
			}
		}
		return null;
	}

	public String getAttrDrawFill() {
		if (hasAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ATTR_FILL))
			return getAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ATTR_FILL);
		return null;
	}

	public GradientElement getAttrDrawFillGradientName() {
		if (hasAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ATTR_FILL_GRADIENT_NAME)) {
			String str = getAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ATTR_FILL_GRADIENT_NAME);
			ODFElement fillGradientElement = findElementByAttrValueFromStyleDoc(
					DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ELEMENT_GRADIENT,
					DrawConstants.DRAW_NAMESPACE_URI, DrawConstants.ATTR_NAME,
					str);
			if ((fillGradientElement != null)
					&& (fillGradientElement instanceof GradientElement)) {
				return (GradientElement) fillGradientElement;
			}
		}
		return null;
	}

	public int getAttrDrawGradientStepCount() {
		if (hasAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ATTR_GRADIENT_STEP_COUNT)) {
			return new Integer(getAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ATTR_GRADIENT_STEP_COUNT)).intValue();
		}
		return -1;
	}

	public String getAttrDrawFillColor() {
		if (hasAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ATTR_FILL_COLOR))
			return getAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ATTR_FILL_COLOR);
		return null;
	}

	public String getAttrDrawFillImageName() {
		if (hasAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ATTR_FILL_IMAGE_NAME)) {
			String str = getAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ATTR_FILL_IMAGE_NAME);
			ODFElement fillImageElement = findElementByAttrValueFromStyleDoc(
					DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ELEMENT_FILL_IMAGE,
					DrawConstants.DRAW_NAMESPACE_URI, DrawConstants.ATTR_NAME,
					str);
			if ((fillImageElement != null)
					&& (fillImageElement instanceof FillImageElement)) {
				return ((FillImageElement) fillImageElement).getAttrXLinkHref()
						.toString();
			}
		}
		return null;
	}

	public String getAttrDrawFillImageWidth() {
		if (hasAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ATTR_FILL_IMAGE_WIDTH))
			return getAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ATTR_FILL_IMAGE_WIDTH);
		return null;
	}

	public String getAttrDrawFillImageHeight() {
		if (hasAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ATTR_FILL_IMAGE_HEIGHT))
			return getAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ATTR_FILL_IMAGE_HEIGHT);
		return null;
	}

	public String getAttrStyleRepeat() {
		if (hasAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_REPEAT))
			return getAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_REPEAT);
		return null;
	}

	public String getAttrDrawFillImageRefPointX() {
		if (hasAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ATTR_FILL_IMAGE_REF_POINT_X))
			return getAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ATTR_FILL_IMAGE_REF_POINT_X);
		return null;
	}

	public String getAttrDrawFillImageRefPointY() {
		if (hasAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ATTR_FILL_IMAGE_REF_POINT_Y))
			return getAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ATTR_FILL_IMAGE_REF_POINT_Y);
		return null;
	}

	public String getAttrDrawFillImageRefPoint() {
		if (hasAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ATTR_FILL_IMAGE_REF_POINT))
			return getAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ATTR_FILL_IMAGE_REF_POINT);
		return null;
	}

	public String getAttrDrawTileRepeatOffset() {
		if (hasAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ATTR_TILE_REPEAT_OFFSET))
			return getAttributeNS(DrawConstants.DRAW_NAMESPACE_URI,
					DrawConstants.ATTR_TILE_REPEAT_OFFSET);
		return null;
	}

	public String getAttrStyleHorizontalPos() {
		if (hasAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ATTR_HORIZONTAL_POS))
			return getAttributeNS(StyleConstants.STYLE_NAMESPACE_URI,
					StyleConstants.ATTR_HORIZONTAL_POS);
		return null;
	}
}