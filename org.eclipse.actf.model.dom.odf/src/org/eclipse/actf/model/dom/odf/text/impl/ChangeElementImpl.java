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
import org.eclipse.actf.model.dom.odf.base.impl.ODFElementImpl;
import org.eclipse.actf.model.dom.odf.text.ChangeElement;
import org.w3c.dom.Element;


class ChangeElementImpl extends ODFElementImpl implements ChangeElement {
	private static final long serialVersionUID = 921467331518199499L;

	public ChangeElementImpl(ODFDocument odfDoc, Element iElement) {
		super(odfDoc, iElement);
	}
}
