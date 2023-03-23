/*******************************************************************************
 * Copyright (c) 2007, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.ui;

import org.eclipse.swt.graphics.Point;

/**
 * Interface to scroll {@link IModelService}
 */
public interface IModelServiceScrollManager {
	/**
	 * Scroll type: not supported
	 */
	static int NONE = -1;
	/**
	 * Scroll type: absolute coordinate
	 */
	static int ABSOLUTE_COORDINATE = 0;
	/**
	 * Scroll type: incremental
	 */
	static int INCREMENTAL = 1;
	/**
	 * Scroll type: page by page
	 */
	static int PAGE = 2;

	/**
	 * Get scroll type of the {@link IModelService}
	 * 
	 * @return scroll type
	 */
	int getScrollType();

	/**
	 * Scroll to y
	 * 
	 * @param y             target Y coordinate
	 * @param waitRendering if true, wait a finish of rendering
	 */
	void absoluteCoordinateScroll(int y, boolean waitRendering);

	/**
	 * Scroll to (x,y)
	 * 
	 * @param x             target X coordinate
	 * @param y             target Y coordinate
	 * @param waitRendering if true, wait a finish of rendering
	 */
	void absoluteCoordinateScroll(int x, int y, boolean waitRendering);

	/**
	 * Incremental scroll (horizontal)
	 * 
	 * @param waitRendering if true, wait a finish of rendering
	 * @return scroll size, or -1 if not supported
	 */
	int incrementScrollX(boolean waitRendering);

	/**
	 * Decremental scroll (horizontal)
	 * 
	 * @param waitRendering if true, wait a finish of rendering
	 * @return scroll size, or -1 if not supported
	 */
	int decrementScrollX(boolean waitRendering);

	/**
	 * Incremental scroll (vertical)
	 * 
	 * @param waitRendering if true, wait a finish of rendering
	 * @return scroll size, or -1 if not supported
	 */
	int incrementScrollY(boolean waitRendering);

	/**
	 * Decremental scroll (vertical)
	 * 
	 * @param waitRendering if true, wait a finish of rendering
	 * @return scroll size, or -1 if not supported
	 */
	int decrementScrollY(boolean waitRendering);

	/**
	 * Incremental large scroll (horizontal)
	 * 
	 * @param waitRendering if true, wait a finish of rendering
	 * @return scroll size, or -1 if not supported
	 */
	int incrementLargeScrollX(boolean waitRendering);

	/**
	 * Decremental large scroll (horizontal)
	 * 
	 * @param waitRendering if true, wait a finish of rendering
	 * @return scroll size, or -1 if not supported
	 */
	int decrementLargeScrollX(boolean waitRendering);

	/**
	 * Incremental large scroll (vertical)
	 * 
	 * @param waitRendering if true, wait a finish of rendering
	 * @return scroll size, or -1 if not supported
	 */
	int incrementLargeScrollY(boolean waitRendering);

	/**
	 * Decremental large scroll (vertical)
	 * 
	 * @param waitRendering if true, wait a finish of rendering
	 * @return scroll size, or -1 if not supported
	 */
	int decrementLargeScrollY(boolean waitRendering);

	/**
	 * Incremental page scroll (Page up)
	 * 
	 * @param waitRendering if true, wait a finish of rendering
	 * @return scroll size, or -1 if not supported
	 */
	int incrementPageScroll(boolean waitRendering);

	/**
	 * Decremental page scroll (Page down)
	 * 
	 * @param waitRendering if true, wait a finish of rendering
	 * @return scroll size, or -1 if not supported
	 */
	int decrementPageScroll(boolean waitRendering);

	/**
	 * Jump to specified page
	 * 
	 * @param pageNumber    target page number
	 * @param waitRendering if true, wait a finish of rendering
	 * @return resulting page number
	 */
	int jumpToPage(int pageNumber, boolean waitRendering);

	/**
	 * Get current page number of the content
	 * 
	 * @return current page number
	 */
	int getCurrentPageNumber();

	/**
	 * Get page number of the last page of the content
	 * 
	 * @return page number of the last page
	 */
	int getLastPageNumber();

	/**
	 * Get screen size of {@link IModelService}
	 * 
	 * @param isWhole
	 *                <ul>
	 *                <li>true: try to calculate entire area of the content</li>
	 *                <li>false: current visible area size</li>
	 *                </ul>
	 * @return screen size of {@link IModelService}
	 */
	ModelServiceSizeInfo getSize(boolean isWhole);

	/**
	 * Get relative position to Display of {@link IModelService}
	 * 
	 * @return relative position to Display of {@link IModelService}
	 */
	Point getRelativePositionToDisplay();

}
