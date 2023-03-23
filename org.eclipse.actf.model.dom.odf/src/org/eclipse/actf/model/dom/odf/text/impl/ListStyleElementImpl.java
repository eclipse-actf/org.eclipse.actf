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
import org.eclipse.actf.model.dom.odf.text.ListStyleElement;
import org.w3c.dom.Element;


class ListStyleElementImpl extends StyleElementImpl implements ListStyleElement {
	private static final long serialVersionUID = 8829467892638490058L;

	protected ListStyleElementImpl(ODFDocument odfDoc, Element element) {
		super(odfDoc, element);
	}

	public long getListLevelStyleBulletSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	public ListLevelStyleBulletElement getListLevelStyleBullet(long idx) {
		// TODO Auto-generated method stub
		return null;
	}

	public void appendListLeverStyleBullet(ListLevelStyleBulletElement elem) {
		// TODO Auto-generated method stub

	}
}