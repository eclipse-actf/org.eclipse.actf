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

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.win32.RECT;
import org.eclipse.swt.internal.win32.TCHAR;

/**
 * Utility class for window handle.
 */
@SuppressWarnings("restriction")
public class WindowUtil {

	public static final long HWND_TOP = OS.HWND_TOP;
	public static final long HWND_TOPMOST = OS.HWND_TOPMOST;
	public static final long HWND_NOTOPMOST = OS.HWND_NOTOPMOST;
	public static final long HWND_BOTTOM = OS.HWND_BOTTOM;

	public static final int SWP_NOSIZE = OS.SWP_NOSIZE;
	public static final int SWP_NOMOVE = OS.SWP_NOMOVE;

	/**
	 * Get window text.
	 * 
	 * @param hWnd
	 *            target window
	 * @return window text
	 */
	public static String GetWindowText(long hWnd) {
		int size = OS.GetWindowTextLength(hWnd);
		if (0 == size) {
			return ""; //$NON-NLS-1$
		}
		TCHAR buffer = new TCHAR(0, size + 1);
		return buffer.toString(0, OS.GetWindowText(hWnd, buffer, buffer
				.length()));
	}

	/**
	 * Get window class name
	 * 
	 * @param hWnd
	 *            target window
	 * @return window class name
	 */
	public static String GetWindowClassName(long hWnd) {
		TCHAR buffer = new TCHAR(0, 256);
		return buffer.toString(0, OS
				.GetClassName(hWnd, /*buffer*/buffer.chars, buffer.length()));
	}

	/**
	 * Get window rectangle
	 * 
	 * @param hWnd
	 *            target window
	 * @return window rectangle
	 */
	public static Rectangle GetWindowRectangle(long hWnd) {
		RECT rect = new RECT();
		OS.GetWindowRect(hWnd, rect);
		return new Rectangle(rect.left, rect.top, rect.right - rect.left,
				rect.bottom - rect.top);
	}

	/**
	 * Check if target window is visible
	 * 
	 * @param hWnd
	 *            target window
	 * @return true if the target window is visible
	 */
	public static boolean IsWindowVisible(long hWnd) {
		return OS.IsWindowVisible(hWnd);
	}

	/**
	 * Get desktop window
	 * 
	 * @return desktop window
	 */
	public static long GetDesktopWindow() {
		return OS.GetDesktopWindow();
	}

	/**
	 * Get child window
	 * 
	 * @param hWnd
	 *            target window
	 * @return child window of the target
	 */
	public static long GetChildWindow(long hWnd) {
		return OS.GetWindow(hWnd, OS.GW_CHILD);
	}

	/**
	 * Get next window
	 * 
	 * @param hWnd
	 *            target window
	 * @return next window of the target
	 */
	public static long GetNextWindow(long hWnd) {
		return OS.GetWindow(hWnd, OS.GW_HWNDNEXT);
	}

	/**
	 * Get owner window
	 * 
	 * @param hWnd
	 *            target window
	 * @return owner window of the target
	 */
	public static long GetOwnerWindow(long hWnd) {
		return OS.GetWindow(hWnd, OS.GW_OWNER);
	}

	/**
	 * Get parent window
	 * 
	 * @param hWnd
	 *            target window
	 * @return parent window of the target
	 */
	public static long GetParentWindow(long hWnd) {
		return OS.GetParent(hWnd);
	}

	/**
	 * Check if target is popup menu
	 * 
	 * @param hwnd
	 *            target window
	 * @return true if the target is popup menu
	 */
	public static boolean isPopupMenu(long hwnd) {
		if ("#32768".equals(GetWindowClassName(hwnd))) { //$NON-NLS-1$
			return 0 == GetOwnerWindow(hwnd);
		}
		return false;
	}

	/**
	 * Find target window
	 * 
	 * @param windowClass
	 *            class name
	 * @param windowName
	 *            window name
	 * @return target window, or null if not available
	 */
	@Deprecated
	public static long FindWindow(String windowClass, String windowName) {
		return 0;
//		return OS.FindWindow(new TCHAR(0, windowClass, true), new TCHAR(0,
//				windowName, true));
	}

	/**
	 * Bring target Window to top
	 * 
	 * @param hWnd
	 *            target window
	 * @return true if succeeded
	 */
	public static boolean BringWindowToTop(long hWnd) {
		return OS.BringWindowToTop(hWnd);
	}

	/**
	 * Change size, position and Z order of a target Window.
	 * 
	 * @param hWnd
	 *            target window
	 * @param hWndInsertAfter
	 *            the window to precede the positioned window in the Z order, or
	 *            one of the following values (HWND_BOTTOM, HWND_NOTOPMOST,
	 *            HWND_TOP, HWND_TOPMOST)
	 * @param X
	 *            new position X
	 * @param Y
	 *            new position Y
	 * @param cx
	 *            new width
	 * @param cy
	 *            new height
	 * @param uFlags
	 *            window sizing and positioning flags.
	 * @return true if succeeded
	 */
	public static boolean SetWindowPos(long hWnd, long hWndInsertAfter, int X,
			int Y, int cx, int cy, int uFlags) {
		return OS.SetWindowPos(hWnd, hWndInsertAfter, X, Y, cx, cy, uFlags);
	}

	static {
		try {
			System.loadLibrary("AccessibiltyWin32Library"); //$NON-NLS-1$
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final int WS_EX_LAYERED = 0x80000;

	public static final int LWA_COLORKEY = 0x01;

	public static final int LWA_ALPHA = 0x02;

	protected static final native int SetLayeredWindowAttributes(long hwnd,
			int crKey, char bAlpha, int dwFlags);

}
