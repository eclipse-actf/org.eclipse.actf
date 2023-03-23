/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.model.internal.dom.sgml;

import java.io.IOException;
import java.io.PrintWriter;

import org.w3c.dom.Document;

public interface ISGMLDocument extends ISGMLNode, Document {

	/**
	 * Print Document as SGML. starttag's string in this document are from the
	 * original document. Even if attributes in elements are modified,
	 * starttag's string does not change.
	 * 
	 * @param pw
	 *            stream to write this document.
	 * @param indent
	 *            indent if true. Otherwise, not indent.
	 * 
	 */
	public void printAsSGML(PrintWriter pw, boolean indent) throws IOException;

	/**
	 * Print Document as XML
	 * 
	 * @param pw
	 *            stream to write this document.
	 * @param indent
	 *            indent if true. Otherwise, not indent.
	 * @param enc
	 *            encoding	
	 */
	public void printAsXML(PrintWriter pw, boolean indent, String enc)
			throws IOException;

}
