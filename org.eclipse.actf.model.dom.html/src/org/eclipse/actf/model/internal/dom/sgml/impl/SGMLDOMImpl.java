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

package org.eclipse.actf.model.internal.dom.sgml.impl;

import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;

/**
 * Light-weight implementation of DOM</a>. This implementation is thread-unsafe.
 * If you require thread-safe implementation, use Xerces DOM. This supports:
 * <ul>
 * <li>Level 1 Core
 * <li>Level 1 HTML
 * <li>Level 2 Core (No namespaces)
 * <li>Level 2 HTML
 * </ul>
 * 
 * @see SGMLParser#setDOMImplementation(org.w3c.dom.DOMImplementation)
 */
public class SGMLDOMImpl implements DOMImplementation {
	/**
	 * @return always false because this is not XML implementation but SGML.
	 */
	public boolean hasFeature(String feature, String version) {
		return false;
	}

	public DocumentType createDocumentType(String qualifiedName,
			String publicId, String systemId) {
		return new SGMLDocType(qualifiedName, publicId, systemId);
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
			return new SGMLDocument(this);
		} else if (doctype.getOwnerDocument() != null
				|| !(doctype instanceof SGMLDocType)) {
			throw new DOMException(
					DOMException.WRONG_DOCUMENT_ERR,
					doctype
							+ " has been already owned or created by a different DOMImplementation") { //$NON-NLS-1$

				/**
								 * 
								 */
				private static final long serialVersionUID = 7000893257059775660L;
			};
		} else {
			Document ret = new SGMLDocument(this);
			ret.appendChild(doctype);
			return ret;
		}
	}

	protected SGMLDOMImpl() {
	}

	private static SGMLDOMImpl instance = new SGMLDOMImpl();

	public static DOMImplementation getDOMImplementation() {
		return instance;
	}

	/*
	 * DOM Level 3
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.DOMImplementation#getFeature(java.lang.String,
	 * java.lang.String)
	 */
	public Object getFeature(String feature, String version) {
		// TODO Auto-generated method stub
		return null;
	}
}
