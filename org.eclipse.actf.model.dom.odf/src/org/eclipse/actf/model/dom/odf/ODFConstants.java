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
package org.eclipse.actf.model.dom.odf;

/**
 * ODFConstants defines the constant values used by ODF DOM API
 */
@SuppressWarnings("nls")
public final class ODFConstants {
	public enum ContentType {
		WRITE, SPREADSHEET, PRESENTATION, DRAW, CHART, NONE
	}

	public static final String ODT_SUFFIX = ".odt";

	public static final String ODS_SUFFIX = ".ods";

	public static final String ODP_SUFFIX = ".odp";

	public static final String ODF_MANIFEST_DIR = "META-INF";

	public static final String ODF_CONTENT_FILENAME = "content.xml";

	public static final String ODF_STYLE_FILENAME = "styles.xml";

	public static final String ODF_MANIFEST_FILENAME = ODF_MANIFEST_DIR
			+ System.getProperty("file.separator") + "manifest.xml";
}