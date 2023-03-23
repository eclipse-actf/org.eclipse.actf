/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.ui;

/**
 * ModelServiceSizeInfo stores size information of {@link IModelService}
 */
public class ModelServiceSizeInfo {

	int viewSizeX;

	int viewSizeY;

	int wholeSizeX;

	int wholeSizeY;

	/**
	 * Constructor of the class
	 * 
	 * @param viewSizeX
	 *            current visible view size (horizontal)
	 * @param viewSizeY
	 *            current visible view size (vertical)
	 * @param wholeSizeX
	 *            entire content size (horizontal)
	 * @param wholeSizeY
	 *            entire content size (vertical)
	 */
	public ModelServiceSizeInfo(int viewSizeX, int viewSizeY, int wholeSizeX,
			int wholeSizeY) {
		this.viewSizeX = viewSizeX;
		this.viewSizeY = viewSizeY;
		this.wholeSizeX = wholeSizeX;
		this.wholeSizeY = wholeSizeY;
	}

	/**
	 * @return view size X
	 */
	public int getViewSizeX() {
		return viewSizeX;
	}

	/**
	 * @return view size Y
	 */
	public int getViewSizeY() {
		return viewSizeY;
	}

	/**
	 * @return entire content size X
	 */
	public int getWholeSizeX() {
		return wholeSizeX;
	}

	/**
	 * @return entire content size Y
	 */
	public int getWholeSizeY() {
		return wholeSizeY;
	}

	/**
	 * Convert to array {view size X, view size Y, whole size X, whole size Y}
	 * 
	 * @return converted size information
	 */
	public int[] toArray() {
		return new int[] { viewSizeX, viewSizeY, wholeSizeX, wholeSizeY };
	}

	@Override
	@SuppressWarnings("nls")
	public String toString() {
		return "View: (" + viewSizeX + ", " + viewSizeY + ") Whole: ("
				+ wholeSizeX + ", " + wholeSizeY + ")";
	}

}
