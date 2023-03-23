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
import org.eclipse.swt.internal.ole.win32.GUID;
import org.eclipse.swt.ole.win32.OLE;

/**
 * COM related utilities. Calling vtable method, creating GUID, and creating
 * dispatch object from GUID.
 */
@SuppressWarnings("restriction")
public class COMUtil {	
	
	static {
		try {
			System.loadLibrary("AccessibiltyWin32Library"); //$NON-NLS-1$
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create dispatch object from GUID
	 * 
	 * @param rclsid
	 *            GUID
	 * @return dispatch object
	 */
	public static long createDispatch(GUID rclsid) {
		long[] ppv = new long[1];
		int result = COM.CoCreateInstance(rclsid, 0, COM.CLSCTX_INPROC_HANDLER
				| COM.CLSCTX_INPROC_SERVER | COM.CLSCTX_LOCAL_SERVER
				| /*COM.CLSCTX_REMOTE_SERVER*/16, COM.IIDIDispatch, ppv);
		if (result != OLE.S_OK)
			OLE.error(OLE.ERROR_CANNOT_CREATE_OBJECT, result);
		return ppv[0];
	}

	/**
	 * Calling vtable method
	 * 
	 * @param fnNumber
	 *            vtable index
	 * @param ppVtbl
	 *            vtable pointer
	 * @param arg0
	 *            argument 0
	 * @return hresult
	 */
	public static final native int VtblCall(int fnNumber, long /*int*/ ppVtbl, int arg0);

	/**
	 * @param fnNumber
	 * @param ppVtbl
	 * @param arg0
	 *            argument 0
	 * @param arg1
	 *            argument 1
	 * @return
	 */
	public static final native int VtblCall(int fnNumber, long /*int*/ ppVtbl, int arg0,
			int arg1);

	/**
	 * @param fnNumber
	 * @param ppVtbl
	 * @param arg0
	 *            argument 0
	 * @param arg1
	 *            argument 1
	 * @param arg2
	 *            argument 2
	 * @return
	 */
	public static final native int VtblCall(int fnNumber, long /*int*/ ppVtbl, int arg0,
			int arg1, int arg2);

	/**
	 * @param fnNumber
	 * @param ppVtbl
	 * @param arg0
	 *            argument 0
	 * @param arg1
	 *            argument 1
	 * @param arg2
	 *            argument 2
	 * @param arg3
	 *            argument 3
	 * @return
	 */
	public static final native int VtblCall(int fnNumber, long /*int*/ ppVtbl, int arg0,
			int arg1, int arg2, int arg3);

	/**
	 * @param fnNumber
	 * @param ppVtbl
	 * @param arg0
	 *            argument 0
	 * @param arg1
	 *            argument 1
	 * @param arg2
	 *            argument 2
	 * @param arg3
	 *            argument 3
	 * @param arg4
	 *            argument 4
	 * @return
	 */
	public static final native int VtblCall(int fnNumber, long /*int*/ ppVtbl, int arg0,
			int arg1, int arg2, int arg3, int arg4);

	/**
	 * @param fnNumber
	 * @param ppVtbl
	 * @param arg0
	 *            argument 0
	 * @param arg1
	 *            argument 1
	 * @param arg2
	 *            argument 2
	 * @param arg3
	 *            argument 3
	 * @param arg4
	 *            argument 4
	 * @param arg5
	 *            argument 5
	 * @return
	 */
	public static final native int VtblCall(int fnNumber, long /*int*/ ppVtbl, int arg0,
			int arg1, int arg2, int arg3, int arg4, int arg5);

	/**
	 * @param fnNumber
	 * @param ppVtbl
	 * @param arg0
	 *            argument 0
	 * @param arg1
	 *            argument 1
	 * @param arg2
	 *            argument 2
	 * @param arg3
	 *            argument 3
	 * @param arg4
	 *            argument 4
	 * @param arg5
	 *            argument 5
	 * @param arg6
	 *            argument 6
	 * @return
	 */
	public static final native int VtblCall(int fnNumber, long /*int*/ ppVtbl, int arg0,
			int arg1, int arg2, int arg3, int arg4, int arg5, int arg6);

	/**
	 * @param fnNumber
	 * @param ppVtbl
	 * @param arg0
	 *            argument 0
	 * @param arg1
	 *            argument 1
	 * @param arg2
	 *            argument 2
	 * @param arg3
	 *            argument 3
	 * @param arg4
	 *            argument 4
	 * @param arg5
	 *            argument 5
	 * @param arg6
	 *            argument 6
	 * @param arg7
	 *            argument 7
	 * @return
	 */
	public static final native int VtblCall(int fnNumber, long /*int*/ ppVtbl, int arg0,
			int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7);

	/* TODO */
	public static final native int VtblCall(int fnNumber, long /*int*/ ppVtbl, int arg0,
			int arg1, int arg2, long arg3);
	
	public static final native int VtblCall(int fnNumber, long /*int*/ ppVtbl, int arg0,
			int arg1, long arg2, long arg3);
	
	public static final native int VtblCall(int fnNumber, long /*int*/ ppVtbl, int arg0,
			long arg1, long arg2, long arg3);
	
	public static final native int VtblCall(int fnNumber, long /*int*/ ppVtbl, int arg0,
			int arg1, long arg2, long arg3, long arg4);
	
	public static final native int VtblCall(int fnNumber, long /*int*/ ppVtbl, int arg0,
			int arg1, long arg2, long arg3, long arg4, long arg5);
	
	public static final native int VtblCall(int fnNumber, long /*int*/ ppVtbl, int arg0,
			long arg1, long arg2, long arg3, long arg4, long arg5);
	
	/**
	 * Create GUID from target String
	 * 
	 * @param lpsz
	 *            target
	 * @return GUID
	 */
	public static GUID IIDFromString(String lpsz) {
		int length = lpsz.length();
		char[] buffer = new char[length + 1];
		lpsz.getChars(0, length, buffer, 0);
		GUID lpiid = new GUID();
		if (COM.IIDFromString(buffer, lpiid) == OLE.S_OK)
			return lpiid;
		return null;
	}
}
