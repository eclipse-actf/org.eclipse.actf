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

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.impl.ODFStylableElementImpl;
import org.eclipse.actf.model.dom.odf.chart.ChartConstants;
import org.eclipse.actf.model.dom.odf.chart.PlotAreaElement;
import org.eclipse.actf.model.dom.odf.dr3d.Dr3dConstants;
import org.eclipse.actf.model.dom.odf.svg.SVGConstants;
import org.eclipse.actf.model.dom.odf.table.TableConstants;
import org.w3c.dom.Element;


class PlotAreaElementImpl extends ODFStylableElementImpl implements
		PlotAreaElement {
	private static final long serialVersionUID = -4201756033126842992L;

	protected PlotAreaElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public String getAttrTableCellRangeAddress() {
		if (hasAttributeNS(TableConstants.TABLE_NAMESPACE_URI,
				TableConstants.ATTR_CELL_RANGE_ADDRESS))
			return getAttributeNS(TableConstants.TABLE_NAMESPACE_URI,
					TableConstants.ATTR_CELL_RANGE_ADDRESS);
		return null;
	}

	public String getAttrChartDataSourceHasLabels() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_DATA_SOURCE_HAS_LABELS))
			return getAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_DATA_SOURCE_HAS_LABELS);
		return null;
	}

	public String getAttrChartTableNumberList() {
		if (hasAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
				ChartConstants.ATTR_TABLE_NUMBER_LIST))
			return getAttributeNS(ChartConstants.CHART_NAMESPACE_URI,
					ChartConstants.ATTR_TABLE_NUMBER_LIST);
		return null;
	}

	public String getAttrSvgX() {
		if (hasAttributeNS(SVGConstants.SVG_NAMESPACE_URI, SVGConstants.ATTR_X))
			return getAttributeNS(SVGConstants.SVG_NAMESPACE_URI,
					SVGConstants.ATTR_X);
		return null;
	}

	public String getAttrSvgY() {
		if (hasAttributeNS(SVGConstants.SVG_NAMESPACE_URI, SVGConstants.ATTR_Y))
			return getAttributeNS(SVGConstants.SVG_NAMESPACE_URI,
					SVGConstants.ATTR_Y);
		return null;
	}

	public String getAttrSvgWidth() {
		if (hasAttributeNS(SVGConstants.SVG_NAMESPACE_URI,
				SVGConstants.ATTR_WIDTH))
			return getAttributeNS(SVGConstants.SVG_NAMESPACE_URI,
					SVGConstants.ATTR_WIDTH);
		return null;
	}

	public String getAttrSvgHeight() {
		if (hasAttributeNS(SVGConstants.SVG_NAMESPACE_URI,
				SVGConstants.ATTR_HEIGHT))
			return getAttributeNS(SVGConstants.SVG_NAMESPACE_URI,
					SVGConstants.ATTR_HEIGHT);
		return null;
	}

	public String getAttrDr3dVrp() {
		if (hasAttributeNS(Dr3dConstants.DR3D_NAMESPACE_URI,
				Dr3dConstants.ATTR_VRP))
			return getAttributeNS(Dr3dConstants.DR3D_NAMESPACE_URI,
					Dr3dConstants.ATTR_VRP);
		return null;
	}

	public String getAttrDr3dVpn() {
		if (hasAttributeNS(Dr3dConstants.DR3D_NAMESPACE_URI,
				Dr3dConstants.ATTR_VPN))
			return getAttributeNS(Dr3dConstants.DR3D_NAMESPACE_URI,
					Dr3dConstants.ATTR_VPN);
		return null;
	}

	public String getAttrDr3dVup() {
		if (hasAttributeNS(Dr3dConstants.DR3D_NAMESPACE_URI,
				Dr3dConstants.ATTR_VUP))
			return getAttributeNS(Dr3dConstants.DR3D_NAMESPACE_URI,
					Dr3dConstants.ATTR_VUP);
		return null;
	}

	public String getAttrDr3dProjection() {
		if (hasAttributeNS(Dr3dConstants.DR3D_NAMESPACE_URI,
				Dr3dConstants.ATTR_PROJECTION))
			return getAttributeNS(Dr3dConstants.DR3D_NAMESPACE_URI,
					Dr3dConstants.ATTR_PROJECTION);
		return null;
	}

	public String getAttrDr3dDistance() {
		if (hasAttributeNS(Dr3dConstants.DR3D_NAMESPACE_URI,
				Dr3dConstants.ATTR_DISTANCE))
			return getAttributeNS(Dr3dConstants.DR3D_NAMESPACE_URI,
					Dr3dConstants.ATTR_DISTANCE);
		return null;
	}

	public String getAttrDr3dFocalLength() {
		if (hasAttributeNS(Dr3dConstants.DR3D_NAMESPACE_URI,
				Dr3dConstants.ATTR_FOCAL_LENGTH))
			return getAttributeNS(Dr3dConstants.DR3D_NAMESPACE_URI,
					Dr3dConstants.ATTR_FOCAL_LENGTH);
		return null;
	}

	public int getAttrDr3dShadowSlant() {
		if (hasAttributeNS(Dr3dConstants.DR3D_NAMESPACE_URI,
				Dr3dConstants.ATTR_SHADOW_SLANT)) {
			return new Integer(getAttributeNS(Dr3dConstants.DR3D_NAMESPACE_URI,
					Dr3dConstants.ATTR_SHADOW_SLANT)).intValue();
		}
		return -1;
	}

	public String getAttrDr3dShadeMode() {
		if (hasAttributeNS(Dr3dConstants.DR3D_NAMESPACE_URI,
				Dr3dConstants.ATTR_SHADE_MODE))
			return getAttributeNS(Dr3dConstants.DR3D_NAMESPACE_URI,
					Dr3dConstants.ATTR_SHADE_MODE);
		return null;
	}

	public String getAttrDr3dAmbientColor() {
		if (hasAttributeNS(Dr3dConstants.DR3D_NAMESPACE_URI,
				Dr3dConstants.ATTR_AMBIENT_COLOR))
			return getAttributeNS(Dr3dConstants.DR3D_NAMESPACE_URI,
					Dr3dConstants.ATTR_AMBIENT_COLOR);
		return null;
	}

	public boolean getAttrDr3dLightingMode() {
		if (hasAttributeNS(Dr3dConstants.DR3D_NAMESPACE_URI,
				Dr3dConstants.ATTR_LIGHTING_MODE)) {
			return new Boolean(getAttributeNS(Dr3dConstants.DR3D_NAMESPACE_URI,
					Dr3dConstants.ATTR_LIGHTING_MODE)).booleanValue();
		}
		return false;
	}
}