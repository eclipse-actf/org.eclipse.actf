/*******************************************************************************
 * Copyright (c) 2007, 2019 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.util.win32;

/**
 * Utility class to access native integer
 */
public class NativeIntAccess {
	private long pIntAddress = 0;

	/**
	 * Default Constructor (size=1)
	 */
	public NativeIntAccess() {
		this(1);
	}

	/**
	 * Constructor of the class
	 * 
	 * @param size
	 *            array size
	 */
	public NativeIntAccess(int size) {
		pIntAddress = MemoryUtil.GlobalAlloc(4 * size);
	}

	/**
	 * Dispose the object
	 */
	public void dispose() {
		MemoryUtil.GlobalFree(pIntAddress);
	}

	/**
	 * @return native address (index=0)
	 */
	public long getAddress() {
		return getAddress(0);
	}

	/**
	 * @param index
	 *            target index
	 * @return native address
	 */
	public long getAddress(int index) {
		return pIntAddress + index * 4;
	}

	/**
	 * @return integer value (index=0)
	 */
	public int getInt() {
		return getInt(0);
	}

	/**
	 * @param index
	 *            target index
	 * @return integer value
	 */
	public int getInt(int index) {
		int[] pInt = new int[1];
		MemoryUtil.MoveMemory(pInt, getAddress(index), 4);
		return pInt[0];
	}

	/**
	 * Set native integer value at index 0
	 * 
	 * @param value
	 *            target value
	 */
	public void setInt(int value) {
		setInt(0, value);
	}

	/**
	 * Set native integer value at target index
	 * 
	 * @param index
	 *            target index
	 * @param value
	 *            target value
	 */
	public void setInt(int index, int value) {
		int[] pInt = new int[1];
		pInt[0] = value;
		MemoryUtil.MoveMemory(getAddress(index), pInt, 4);
	}
}
