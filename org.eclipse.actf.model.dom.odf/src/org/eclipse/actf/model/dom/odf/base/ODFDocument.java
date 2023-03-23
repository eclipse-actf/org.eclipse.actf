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
package org.eclipse.actf.model.dom.odf.base;

import org.w3c.dom.Document;

/**
 * The extended interface of the {@link Document}.
 */
public interface ODFDocument extends Document {
	/**
	 * Return URL of ODF document
	 * 
	 * @return String
	 */	
	public String getURL();

	/**
	 * Set URL of ODF document
	 * 
	 * @param sUrl
	 *            URL of ODF document
	 */	
	public void setURL(String sUrl);

	/**
	 * Set ODF version
	 * 
	 * @param version
	 *            ODF version
	 */	
	public void setODFVersion(double version);

	/**
	 * Return ODF version
	 * 
	 * @return double
	 */	
	public double getODFVersion();

	/**
	 * Associate styles.xml document to this class
	 * 
	 * @param styleDoc
	 *            ODF document created by styles.xml
	 */	
	public void setStyleDocument(ODFDocument styleDoc);

	/**
	 * Return ODF document created by styles.xml
	 * 
	 * @return ODFDocument
	 */	
	public ODFDocument getStyleDocument();
}