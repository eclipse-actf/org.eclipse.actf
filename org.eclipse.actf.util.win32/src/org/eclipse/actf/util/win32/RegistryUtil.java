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
import org.eclipse.swt.internal.win32.TCHAR;

public class RegistryUtil {

	@SuppressWarnings("unused")
	private static final int REG_NONE = 0, REG_SZ = 1, REG_EXPAND_SZ = 2,
			REG_BINARY = 3, REG_DWORD = 4, REG_DWORD_BIG_ENDIAN = 5,
			REG_LINK = 6, REG_MULTI_SZ = 7, REG_RESOURCE_LIST = 8,
			REG_FULL_RESOURCE_DESCRIPTOR = 9,
			REG_RESOURCE_REQUIREMENTS_LIST = 10, REG_QWORD = 11;

	private static final int KEY_READ = 0x20019;

	public static final long HKEY_CLASSES_ROOT = 0x80000000L;
	public static final long HKEY_CURRENT_USER = 0x80000001L;
	public static final long HKEY_LOCAL_MACHINE = 0x80000002L;

	public static int getRegistryInt(long hKeyParent, String subKeyString,
			String valueString) {
		long hKey = open(hKeyParent, subKeyString, 0, KEY_READ);
		if (0 != hKey) {
			try {
				return getIntegerValue(hKey, valueString);
			} finally {
				close(hKey);
			}
		}
		return 0;
	}

	public static String getRegistryString(long hKeyParent, String subKeyString,
			String valueString) {
		long hKey = open(hKeyParent, subKeyString, 0, KEY_READ);
		if (0 != hKey) {
			try {
				return getStringValue(hKey, valueString);
			} finally {
				close(hKey);
			}
		}
		return null;
	}

	private static long open(long hKeyParent, String subKeyString, int ulOptions,
			int samDesired) {
		TCHAR keyName = new TCHAR(0, subKeyString, true);
		long[] phKey = new long[1];
		if (0 == OS.RegOpenKeyEx(hKeyParent, keyName, ulOptions, samDesired,
				phKey)) {
			return phKey[0];
		}
		return 0;
	}

	private static int close(long hKey) {
		return OS.RegCloseKey(hKey);
	}

	private static int getIntegerValue(long hKey, String valueString) {
		TCHAR valueName = new TCHAR(0, valueString, true);
		int[] pcbData = new int[] { 4 };
		int[] pType = new int[1];
		int[] pData = new int[1];
		if (0 == OS.RegQueryValueEx(hKey, valueName, 0, pType, pData, pcbData)) {
			if (REG_DWORD == pType[0]) {
				return pData[0];
			}
		}
		return 0;
	}

	private static String getStringValue(long hKey, String valueString) {
		TCHAR valueName = new TCHAR(0, valueString, true);
		int[] pcbData = new int[1];
		int[] pType = new int[1];
		if (0 == OS.RegQueryValueEx(hKey, valueName, 0, pType, (TCHAR) null,
				pcbData)) {
			if (REG_SZ == pType[0]) {
				TCHAR buff = new TCHAR(0, pcbData[0] / TCHAR.sizeof);
				if (0 == OS.RegQueryValueEx(hKey, valueName, 0, pType, buff,
						pcbData)) {
					return buff.toString(0, buff.strlen());
				}
			}
		}
		return null;
	}

}
