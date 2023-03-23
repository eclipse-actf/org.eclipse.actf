/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer;

import java.util.Enumeration;

/**
 * Interface to add information to element information viewer. Contributers need
 * to register the instance to
 * org.eclipse.actf.visualization.engines.blind.html.elementInfoProvider
 * extension point.
 */
public interface IElementViewerInfoProvider {
	/**
	 * @return categories of IDs
	 */
	Enumeration<String> getCategories();

	/**
	 * @param id
	 *            target ID
	 * @return ID information
	 */
	IElementViewerIdInfo getIdInfo(String id);

	/**
	 * @param accessKey
	 *            target accesskey
	 * @return accesskey information
	 */
	IElementViewerAccessKeyInfo getAccessKeyInfo(String accessKey);
}
