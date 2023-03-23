/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.ui;

/**
 * Interface to store position and size information of Item
 */
public interface IPositionSize {

	/**
	 * @return area size of Item
	 */
	public int getArea();

	/**
	 * @return height of Item
	 */
	public int getHeight();

	/**
	 * @return width of Item
	 */
	public int getWidth();

	/**
	 * @return X position of Item
	 */
	public int getX();

	/**
	 * @return Y position of Item
	 */
	public int getY();

	/**
	 * Set height
	 * 
	 * @param i
	 *            height of Item
	 */
	public void setHeight(int i);

	/**
	 * Set width
	 * 
	 * @param i
	 *            width of Item
	 */
	public void setWidth(int i);

	/**
	 * Set X position
	 * 
	 * @param i
	 *            X position of Item
	 */
	public void setX(int i);

	/**
	 * Set Y position
	 * 
	 * @param i
	 *            Y position of Item
	 */
	public void setY(int i);

}
