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
import org.eclipse.swt.internal.win32.OS;

/**
 * Utility class to access native String
 */
@SuppressWarnings("restriction")
public class NativeStringAccess {
	private long pBSTRAddress = 0;

	private long[] hMem = new long[1];

	/**
	 * Constructor of the class
	 */
	public NativeStringAccess() {
		pBSTRAddress = MemoryUtil.GlobalAlloc(4);
	}

	/**
	 * Dispose the object
	 */
	public void dispose() {
		if (0 != hMem[0]) {
			COM.SysFreeString(hMem[0]);
		}
		MemoryUtil.GlobalFree(pBSTRAddress);
	}

	/**
	 * @return native address
	 */
	public long getAddress() {
		return pBSTRAddress;
	}

	/**
	 * @return string value
	 */
	public String getString() {
		OS.MoveMemory(hMem, pBSTRAddress, 8);
		if (0 != hMem[0]) {
			int size = COM.SysStringByteLen(hMem[0]);
			if (size > 0) {
				char[] buffer = new char[(size + 1) / 2];
				MemoryUtil.MoveMemory(buffer, hMem[0], size);
				return new String(buffer);
			}
		}
		return null;// "";
	}

	/**
	 * Set native String value
	 * 
	 * @param text
	 */
	public void setString(String text) {
		OS.MoveMemory(hMem, pBSTRAddress, 8);
		if (0 != hMem[0]) {
			COM.SysFreeString(hMem[0]);
		}
		char[] data = (text + "\0").toCharArray(); //$NON-NLS-1$
		long ptr = COM.SysAllocString(data);
		COM.MoveMemory(pBSTRAddress, new long[] { ptr }, 8);
		OS.MoveMemory(hMem, pBSTRAddress, 8);
	}
}
