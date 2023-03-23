/*******************************************************************************
 * Copyright (c) 1998, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Goh KONDOH - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.internal.dom.html.impl;

import org.eclipse.actf.model.internal.dom.sgml.impl.SGMLDOMImpl;
import org.eclipse.actf.model.internal.dom.sgml.impl.SGMLDocType;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.html.HTMLDocument;

public class SHDOMImpl extends SGMLDOMImpl {
	/**
	 * @param title
	 *            always ignored.
	 */
	public HTMLDocument createHTMLDocument(String title) {
		return new SHDocument(this);
	}

	/**
	 * @param namespaceURI
	 *            always ignored.
	 * @param qualifiedName
	 *            always ignored.
	 */
	public Document createDocument(String namespaceURI, String qualifiedName,
			DocumentType doctype) throws DOMException {
		if (doctype == null) {
			return new SHDocument(this);
		} else if (doctype.getOwnerDocument() != null
				|| !(doctype instanceof SGMLDocType)) {
			throw new DOMException(DOMException.WRONG_DOCUMENT_ERR, doctype
					+ " has been already owned.") { //$NON-NLS-1$
				private static final long serialVersionUID = 5706310596972371053L;
			};
		} else {
			Document ret = createHTMLDocument(null);
			ret.insertBefore(doctype, null);
			return ret;
		}
	}

	protected SHDOMImpl() {
	}

	private static SHDOMImpl instance = new SHDOMImpl();

	public static DOMImplementation getDOMImplementation() {
		return instance;
	}
}
