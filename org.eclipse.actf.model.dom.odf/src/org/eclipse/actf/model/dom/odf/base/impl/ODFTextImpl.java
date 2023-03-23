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

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.ODFText;
import org.w3c.dom.DOMException;
import org.w3c.dom.Text;


class ODFTextImpl extends ODFCharacterDataImpl implements ODFText {

	public ODFTextImpl(ODFDocument odfDoc, Text innerText) {
		super(odfDoc, innerText);
	}

	public String getWholeText() {
		if (iNode instanceof Text) {
			return ((Text) iNode).getWholeText();
		}
		return null;
	}

	public boolean isElementContentWhitespace() {
		if (iNode instanceof Text) {
			return ((Text) iNode).isElementContentWhitespace();
		}
		return false;
	}

	public Text replaceWholeText(String content) throws DOMException {
		if (iNode instanceof Text) {
			return ((Text) iNode).replaceWholeText(content);
		}
		return null;
	}

	public Text splitText(int offset) throws DOMException {
		if (iNode instanceof Text) {
			return ((Text) iNode).splitText(offset);
		}
		return null;
	}
}
