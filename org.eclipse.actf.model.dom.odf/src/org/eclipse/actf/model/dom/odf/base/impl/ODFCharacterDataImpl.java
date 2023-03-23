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

import org.eclipse.actf.model.dom.odf.base.ODFCharacterData;
import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;


public class ODFCharacterDataImpl extends ODFNodeImpl implements
		ODFCharacterData {

	public ODFCharacterDataImpl(ODFDocument odfDoc,
			CharacterData innerCharacterData) {
		super(odfDoc, innerCharacterData);
	}

	public void appendData(String arg) throws DOMException {
		if (iNode instanceof CharacterData) {
			((CharacterData) iNode).appendData(arg);
		}
	}

	public void deleteData(int offset, int count) throws DOMException {
		if (iNode instanceof CharacterData) {
			((CharacterData) iNode).deleteData(offset, count);
		}
	}

	public String getData() throws DOMException {
		if (iNode instanceof CharacterData) {
			return ((CharacterData) iNode).getData();
		}
		return null;
	}

	public int getLength() {
		if (iNode instanceof CharacterData) {
			return ((CharacterData) iNode).getLength();
		}
		return 0;
	}

	public void insertData(int offset, String arg) throws DOMException {
		if (iNode instanceof CharacterData) {
			((CharacterData) iNode).insertData(offset, arg);
		}
	}

	public void replaceData(int offset, int count, String arg)
			throws DOMException {
		if (iNode instanceof CharacterData) {
			((CharacterData) iNode).replaceData(offset, count, arg);
		}
	}

	public void setData(String data) throws DOMException {
		if (iNode instanceof CharacterData) {
			((CharacterData) iNode).setData(data);
		}
	}

	public String substringData(int offset, int count) throws DOMException {
		if (iNode instanceof CharacterData) {
			return ((CharacterData) iNode).substringData(offset, count);
		}
		return null;
	}
}
