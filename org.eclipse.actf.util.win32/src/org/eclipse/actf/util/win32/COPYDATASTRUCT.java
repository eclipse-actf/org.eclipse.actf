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

import org.eclipse.swt.internal.win32.OS;

/**
 * Utility class to send/receive messages with other window
 */
@SuppressWarnings("restriction")
public class COPYDATASTRUCT {

	public static final int WM_COPYDATA = 0x4a;
	public static final int sizeof = 20;

	public long dwData;
	public int cbData;
	public long lpData;
	public byte[] data;

	/**
	 * Create a COPYDATASTRUCT object using String data
	 * 
	 * @param dwData
	 * @param strData
	 */
	public COPYDATASTRUCT(long dwData, String strData) {
		this(dwData, strData.getBytes());
	}

	/**
	 * Create a COPYDATASTRUCT object using binary data
	 * 
	 * @param dwData
	 * @param data
	 */
	public COPYDATASTRUCT(long dwData, byte[] data) {
		this.dwData = dwData;
		if (null != data) {
			this.cbData = data.length;
			this.data = new byte[data.length];
			System.arraycopy(data, 0, this.data, 0, data.length);
		}
	}

	/**
	 * Create a COPYDATASTRUCT object using lParam
	 * 
	 * @param lParam
	 */
	public COPYDATASTRUCT(long lParam) {
		int[] pEntries = new int[5];
		OS.MoveMemory(pEntries, lParam, sizeof);
		dwData = (long)pEntries[1] << 32 | pEntries[0] & 0xFFFFFFFFL;
		cbData = pEntries[2];
		lpData = (long)pEntries[4] << 32 | pEntries[3] & 0xFFFFFFFFL;
		if (0 != lpData && cbData > 0) {
			data = new byte[(int)cbData];
			OS.MoveMemory(data, lpData, (int)cbData);
		} else {
			data = new byte[0];
		}
	}

	/**
	 * Copy a COPYDATASTRUCT object to memory
	 * 
	 * @param pData
	 */
	private void setData(long pData) {
		long p = 0;
		if (null != data) {
			p = OS.GlobalAlloc(OS.GMEM_FIXED | OS.GMEM_ZEROINIT, (int) cbData);
			OS.MoveMemory(p, data, (int) cbData);
		}
		OS.MoveMemory(pData, new long[] { dwData, cbData, p }, sizeof);
	}

	/**
	 * Send a COPYDATASTRUCT object to another window using WM_COPYDATA
	 * 
	 * @param hwndTo
	 *            target window to send data
	 * @param hwndFrom
	 *            send data from this window
	 * @return result code
	 */
	public long sendMessage(long hwndTo, long hwndFrom) {
		long lpData = OS.GlobalAlloc(OS.GMEM_FIXED | OS.GMEM_ZEROINIT, sizeof);
		try {
			setData(lpData);
			return OS.SendMessage(hwndTo, WM_COPYDATA, hwndFrom, lpData);
		} finally {
			OS.GlobalFree(lpData);
		}
	}

	/**
	 * Retrieve a String data
	 * 
	 * @return data as new String
	 */
	public String getStringData() {
		try {
			return new String(data);
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "dwData=" + dwData + ", cbData=" + cbData + ", lpData=0x" + Long.toHexString(lpData) + ", data=\"" + new String(data) + "\""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	}
}
