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
package org.eclipse.actf.model.dom.odf.util.converter;

import org.eclipse.actf.model.dom.odf.base.ODFDocument;

/**
 * ODF converter class should implement this interface
 */
@SuppressWarnings("nls")
public interface ODFConverter extends TextExtractor {
	public static final String OUTPUT_ATTR_ODF_CONTENT_ID = "actf-odf-content-id";

	public static final String OUTPUT_ATTR_ODF_TAGNAME = "odf-tagname";

	void setDocument(ODFDocument document);

	void convertDocument(String fileName, boolean enableStyle);
}
