/*******************************************************************************
 * Copyright (c) 2008, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.model.ui;

import org.eclipse.swt.graphics.Rectangle;
import org.w3c.dom.Element;

/**
 * ImagePositionInfo stores position information of images
 */
public class ImagePositionInfo {
	int x, y, width, height;
	int orgX, orgY, orgWidth, orgHeight;

	String url = ""; //$NON-NLS-1$
	Element element = null;
	int handle = -1;
	double zoomFactor = 1;

	/**
	 * 
	 * @deprecated
	 * 
	 *             Constructor of {@link ImagePositionInfo}
	 * 
	 * @param x      X position
	 * @param y      Y position
	 * @param width  width
	 * @param height height
	 * @param url    image URL
	 */
	public ImagePositionInfo(int x, int y, int width, int height, String url) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		this.orgX = x;
		this.orgY = y;
		this.orgWidth = width;
		this.orgHeight = height;

		this.url = url;
	}

	public ImagePositionInfo(Rectangle rect, String url, Element image) {
		this(rect, 1, url, image);
	}

	public ImagePositionInfo(Rectangle rect, double zoomFactor, String url, Element image) {
		this.zoomFactor = zoomFactor;

		this.x = calcZoom(rect.x);
		this.y = calcZoom(rect.y);
		this.width = calcZoom(rect.width);
		this.height = calcZoom(rect.height);

		this.orgX = rect.x;
		this.orgY = rect.y;
		this.orgWidth = rect.width;
		this.orgHeight = rect.height;

		this.url = url;
		this.element = image;
	}

	/**
	 * @return height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Set height
	 * 
	 * @param height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return URL of image as String
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Set URL of image
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Set width
	 * 
	 * @param width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return X position
	 */
	public int getX() {
		return x;
	}

	/**
	 * Set X position
	 * 
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return Y position
	 */
	public int getY() {
		return y;
	}

	/**
	 * Set Y position
	 * 
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}

	public Element getElement() {
		return element;
	}

	/**
	 * @return ZoomFactor
	 */
	public double getZoomFactor() {
		return zoomFactor;
	}
	
	private int calcZoom(double target) {
		return (int) (target * zoomFactor + 0.5f);
	}

}
