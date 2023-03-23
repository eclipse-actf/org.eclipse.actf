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
package org.eclipse.actf.model.dom.odf.dr3d.impl;

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.impl.ODFElementImpl;
import org.eclipse.actf.model.dom.odf.dr3d.Dr3dConstants;
import org.eclipse.actf.model.dom.odf.dr3d.LightElement;
import org.w3c.dom.Element;


class LightElementImpl extends ODFElementImpl implements LightElement {
	private static final long serialVersionUID = -6265631982423831275L;

	protected LightElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public String getAttrDr3dDiffuseColor() {
		if (hasAttributeNS(Dr3dConstants.DR3D_NAMESPACE_URI,
				Dr3dConstants.ATTR_DIFFUSE_COLOR))
			return getAttributeNS(Dr3dConstants.DR3D_NAMESPACE_URI,
					Dr3dConstants.ATTR_DIFFUSE_COLOR);
		return null;
	}

	public String getAttrDr3dDirection() {
		if (hasAttributeNS(Dr3dConstants.DR3D_NAMESPACE_URI,
				Dr3dConstants.ATTR_DIRECTION))
			return getAttributeNS(Dr3dConstants.DR3D_NAMESPACE_URI,
					Dr3dConstants.ATTR_DIRECTION);
		return null;
	}

	public boolean getAttrDr3dEnabled() {
		if (hasAttributeNS(Dr3dConstants.DR3D_NAMESPACE_URI,
				Dr3dConstants.ATTR_ENABLED)) {
			return new Boolean(getAttributeNS(Dr3dConstants.DR3D_NAMESPACE_URI,
					Dr3dConstants.ATTR_ENABLED)).booleanValue();
		}
		return false;
	}

	public boolean getAttrDr3dSpecular() {
		if (hasAttributeNS(Dr3dConstants.DR3D_NAMESPACE_URI,
				Dr3dConstants.ATTR_SPECULAR)) {
			return new Boolean(getAttributeNS(Dr3dConstants.DR3D_NAMESPACE_URI,
					Dr3dConstants.ATTR_SPECULAR)).booleanValue();
		}
		return false;
	}
}