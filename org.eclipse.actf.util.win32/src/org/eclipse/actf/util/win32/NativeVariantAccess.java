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

import org.eclipse.swt.internal.ole.win32.COM;
import org.eclipse.swt.internal.ole.win32.IDispatch;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.ole.win32.OLE;
import org.eclipse.swt.ole.win32.Variant;

/**
 * Utility class to access native Variant
 */
@SuppressWarnings("restriction")
public class NativeVariantAccess {
	private int size;

	private long pVariantAddress;

	/**
	 * Constructor of the class (size=1)
	 */
	public NativeVariantAccess() {
		this(1);
	}

	/**
	 * Constructor of the class
	 * 
	 * @param size
	 *            array size
	 */
	public NativeVariantAccess(int size) {
		this.size = size;
		this.pVariantAddress = MemoryUtil.GlobalAlloc(Variant.sizeof * size);
		for (int i = 0; i < size; i++) {
			COM.VariantInit(getAddress(i));
		}
	}

	/**
	 * Dispose the object
	 */
	public void dispose() {
		for (int i = 0; i < size; i++) {
			COM.VariantClear(getAddress(i));
		}
		MemoryUtil.GlobalFree(pVariantAddress);
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
	 * @return native address at the index
	 */
	public long getAddress(int index) {
		return pVariantAddress + Variant.sizeof * index;
	}

	/**
	 * @return type of Variant (index=0)
	 */
	public short getType() {
		return getType(0);
	}

	/**
	 * @param index
	 *            target index
	 * @return type of Variant at the index
	 */
	public short getType(int index) {
		short[] pType = new short[1];
		MemoryUtil.MoveMemory(pType, getAddress(index), 2);
		return pType[0];
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
	 * @return integer value at the index
	 */
	public int getInt(int index) {
		if (OLE.VT_I4 == getType(index)) {
			int[] pInt = new int[1];
			MemoryUtil.MoveMemory(pInt, getAddress(index) + 8, 4);
			return pInt[0];
		}
		return -1;
	}

	/**
	 * @return IDispatch at index 0
	 */
	public IDispatch getDispatch() {
		return getDispatch(0);
	}

	/**
	 * @param index
	 *            target index
	 * @return IDispatch at the index
	 */
	public IDispatch getDispatch(int index) {
		if (OLE.VT_DISPATCH == getType(index)) {
			long[] pInt = new long[1];
			OS.MoveMemory(pInt, getAddress(index) + 8, 8);
			return new IDispatch(pInt[0]);
		}
		return null;
	}

	/**
	 * @return native String (index=0)
	 */
	public String getString() {
		return getString(0);
	}

	/**
	 * @param index
	 *            target index
	 * @return native String at the index
	 */
	public String getString(int index) {
		if (OLE.VT_BSTR == getType(index)) {
			long[] hMem = new long[1];
			OS.MoveMemory(hMem, getAddress(index) + 8, OS.PTR_SIZEOF);
			if (0 != hMem[0]) {
				int size = COM.SysStringByteLen(hMem[0]);
				if (size > 0) {
					char[] buffer = new char[(size + 1) / 2];
					MemoryUtil.MoveMemory(buffer, hMem[0], size);
					return new String(buffer);
				}
			}
		}
		return null;
	}

	/**
	 * @return Variant (index=0)
	 */
	public Variant getVariant() {
		return getVariant(0);
	}

	/**
	 * @param index
	 *            target index
	 * @return Variant at the index
	 */
	public Variant getVariant(int index) {
		switch (getType(index)) {
		case OLE.VT_I4:
			return new Variant(getInt(index));
		case OLE.VT_BSTR:
			return new Variant(getString(index));
		case OLE.VT_EMPTY:
			return new Variant();
		}
		return null;
	}

	/**
	 * @param variant
	 *            variant
	 */
	public void setVariant(Variant variant) {
		setVariant(0, variant);
	}

	/**
	 * @param index
	 *            target index
	 * @param variant
	 *            variant
	 */
	public void setVariant(int index, Variant variant) {
		long address = getAddress(index);
		COM.VariantClear(address);
		short type = variant.getType(); 
        COM.MoveMemory(address, new short[] {type}, 2);
		switch (type) {
		case OLE.VT_I4:
			COM.MoveMemory(address + 8, new int[]{variant.getInt()}, 4);
			break;
		case OLE.VT_BSTR:
			char[] data = (variant.getString()+"\0").toCharArray();
			COM.MoveMemory(address + 8, new long [] {COM.SysAllocString(data)}, OS.PTR_SIZEOF);
			break;
		}
	}

}
