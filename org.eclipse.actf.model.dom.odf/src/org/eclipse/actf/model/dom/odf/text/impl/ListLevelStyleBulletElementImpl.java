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
package org.eclipse.actf.model.dom.odf.text.impl;

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.style.impl.StyleElementImpl;
import org.eclipse.actf.model.dom.odf.text.ListLevelStyleBulletElement;
import org.eclipse.actf.model.dom.odf.text.TextConstants;
import org.w3c.dom.Element;


class ListLevelStyleBulletElementImpl extends StyleElementImpl implements
		ListLevelStyleBulletElement {
	private static final long serialVersionUID = -5280300696409997886L;

	protected ListLevelStyleBulletElementImpl(ODFDocument odfDoc,
			Element element) {
		super(odfDoc, element);
	}

	public long getAttrLevel() {
		return new Integer(getAttributeNS(TextConstants.TEXT_NAMESPACE_URI,
				TextConstants.ATTR_LEVEL)).intValue();
	}

	public String getAttrBulletChar() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setAttrBulletChar(String character) {
		// TODO Auto-generated method stub

	}
}