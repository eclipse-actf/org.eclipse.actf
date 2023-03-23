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
package org.eclipse.actf.model.ui.editor.browser;

import java.util.Map;

import org.eclipse.actf.model.ui.ModelServiceSizeInfo;
import org.eclipse.swt.graphics.RGB;

/**
 * Interface for style information of content and {@link IWebBrowserACTF}
 */
public interface IWebBrowserStyleInfo {

	/**
	 * Get current style information of the content in {@link IWebBrowserACTF}
	 * 
	 * @return Returns the Map between Elements (XPath) and it's current styles.
	 */
	public abstract Map<String, ICurrentStyles> getCurrentStyles();

	/**
	 * Get current size information of {@link IWebBrowserACTF}
	 * 
	 * @param isWhole
	 *            if true returns whole page size, if false returns visible area
	 *            size
	 * @return Returns the Browser's size information.
	 */
	public abstract ModelServiceSizeInfo getSizeInfo(boolean isWhole);

	/**
	 * Get the unvisited link color in RGB format.
	 * 
	 * @return Returns the unvisited link color in RGB format.
	 */
	public abstract RGB getUnvisitedLinkColor();

	/**
	 * Get the visited link color in RGB format.
	 * 
	 * @return Returns the visited link color in RGB format.
	 */
	public abstract RGB getVisitedLinkColor();

}