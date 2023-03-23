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
package org.eclipse.actf.model.dom.odf.base.impl;

import org.eclipse.actf.model.dom.odf.base.ODFAttr;
import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.TypeInfo;


class ODFAttrImpl extends ODFNodeImpl implements ODFAttr {

	public ODFAttrImpl(ODFDocument odfDoc, Attr iAttr) {
		super(odfDoc, iAttr);
	}

	public String getName() {
		return ((Attr) iNode).getName();
	}

	public Element getOwnerElement() {
		if (iNode instanceof Attr) {
			Element iOwner = ((Attr) iNode).getOwnerElement();
			if (iOwner == null)
				return null;

			return (Element) odfDoc.getODFNode(iOwner);
		}
		return null;
	}

	public TypeInfo getSchemaTypeInfo() {
		return ((Attr) iNode).getSchemaTypeInfo();
	}

	public boolean getSpecified() {
		return ((Attr) iNode).getSpecified();
	}

	public String getValue() {
		return ((Attr) iNode).getValue();
	}

	public boolean isId() {
		return ((Attr) iNode).isId();
	}

	public void setValue(String value) throws DOMException {
		((Attr) iNode).setValue(value);
	}
}
