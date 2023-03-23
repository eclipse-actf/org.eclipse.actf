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
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

@SuppressWarnings("nls")
public class SGMLDocType extends SGMLNode implements DocumentType {
	private static final long serialVersionUID = -4133901290836989142L;

	/**
	 * @serial
	 */
	private String name;

	private String orgId = null;

	/**
	 * ID. For HTML4.0 strict, it is "-//W3C//DTD HTML 4.0//EN".
	 * 
	 * @serial
	 */
	protected String id;

	public String getPublicId() {
		return id;
	}

	/**
	 * @serial
	 */
	private String systemID;

	public String getSystemId() {
		return systemID;
	}

	// SGMLDocType(String name, String id, Document doc) {
	// super(doc);
	// this.name = name;
	// this.id = id;
	// }

	/**
	 * @serial
	 */
	private String internalSubset;

	public String getInternalSubset() {
		return internalSubset;
	}

	public void setInternalSubset(String str) {
		internalSubset = str;
	}

	public SGMLDocType(String name, String publicID, String systemID) {
		super(null);
		this.name = name;
		this.id = publicID;
		this.systemID = systemID;
	}

	// SGMLNode abstract method implementation.
	void check(Node newChild) throws DOMException {
		throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR, this
				+ "cannot have children.") {
			private static final long serialVersionUID = -1251093824923453659L;
		};
	}

	/**
	 * Does not support this method.
	 * 
	 * @return always return null
	 */
	public NamedNodeMap getEntities() {
		return null;
	}

	// DocumentType interface implementation.
	public String getName() {
		return name;
	}

	// Node interface implementation.
	public String getNodeName() {
		return id;
	}

	public short getNodeType() {
		return DOCUMENT_TYPE_NODE;
	}

	public String getNodeValue() throws DOMException {
		return null;
	}

	/**
	 * Does not support this method.
	 * 
	 * @return always return null
	 */
	public NamedNodeMap getNotations() {
		return null;
	}

	public void setNodeValue(String nodeValue) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"cannot set value on " + this) {
			private static final long serialVersionUID = -2040752127102111247L;
		};
	}

	/**
	 * @serial
	 */
	private String rawString;

	public void setRawString(String str) {
		this.rawString = str;
	}

	/**
	 * @return raw string from original document if
	 *         {@link #setRawString(java.lang.String)} has been invoked.
	 *         Otherwise, <code>&lt;!DOCTYPE name PUBLIC "id" &gt;</code>
	 */
	public String toString() {
		return rawString != null ? rawString : "<!DOCTYPE " + name
				+ " PUBLIC \"" + id + "\">";
	}

	/**
	 * 
	 * @return original ID if HTML Parser overrides it. If not, it returns null.
	 */
	public String getOrgId() {
		return orgId;
	}

	protected void setOrgId(String orgId) {
		this.orgId = orgId;
	}
}
