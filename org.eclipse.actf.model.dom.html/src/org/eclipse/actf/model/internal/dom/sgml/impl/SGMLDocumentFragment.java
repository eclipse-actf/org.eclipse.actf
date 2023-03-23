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
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;

@SuppressWarnings("nls")
class SGMLDocumentFragment extends SGMLParentNode implements DocumentFragment {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1284138676307716452L;

	void check(Node newChild) throws DOMException {
		switch (newChild.getNodeType()) {
		case ELEMENT_NODE:
		case PROCESSING_INSTRUCTION_NODE:
		case COMMENT_NODE:
		case TEXT_NODE:
		case CDATA_SECTION_NODE:
		case ENTITY_REFERENCE_NODE:
			break;
		default:
			throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR, newChild
					+ " cannot be a child of " + this) {

						/**
						 * 
						 */
						private static final long serialVersionUID = 8525641959587643275L;
			};
		}
	}

	public String getNodeName() {
		return "#document-fragment";
	}

	public String getNodeValue() throws DOMException {
		return null;
	}

	public void setNodeValue(String nodeValue) throws DOMException {
		throw new DOMException(DOMException.NO_DATA_ALLOWED_ERR, this
				+ " cannot have any data.") {

					/**
					 * 
					 */
					private static final long serialVersionUID = 4846687549476016385L;
		};
	}

	public short getNodeType() {
		return DOCUMENT_FRAGMENT_NODE;
	}

	SGMLDocumentFragment(Document doc) {
		super(doc);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("DocumentFragment[");
		for (Node child = getFirstChild(); child != null; child = child
				.getNextSibling()) {
			sb.append(child.toString());
			if (child.getNextSibling() != null) {
				sb.append(',');
			}
		}
		sb.append(']');
		return sb.toString();
	}
}
