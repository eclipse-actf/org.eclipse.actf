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
package org.eclipse.actf.model.dom.odf.util;

import org.eclipse.actf.model.dom.odf.ODFConstants;
import org.eclipse.actf.model.dom.odf.ODFConstants.ContentType;

/**
 * Utility class used by ODF file read and write classes
 */
public class ODFFileUtils {
	public static boolean isODFFile(String sUrl) {
		if (sUrl == null)
			return false;

		return sUrl.endsWith(ODFConstants.ODT_SUFFIX)
				|| sUrl.endsWith(ODFConstants.ODS_SUFFIX)
				|| sUrl.endsWith(ODFConstants.ODP_SUFFIX);
	}

	public static ContentType getODFFileType(String sUrl) {
		if (sUrl == null)
			return ContentType.NONE;

		if (sUrl.endsWith(ODFConstants.ODT_SUFFIX))
			return ContentType.WRITE;
		else if (sUrl.endsWith(ODFConstants.ODS_SUFFIX))
			return ContentType.SPREADSHEET;
		else if (sUrl.endsWith(ODFConstants.ODP_SUFFIX))
			return ContentType.PRESENTATION;

		return ContentType.NONE;
	}
}
