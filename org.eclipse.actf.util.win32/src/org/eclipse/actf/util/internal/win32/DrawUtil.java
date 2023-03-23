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

package org.eclipse.actf.util.internal.win32;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.win32.RECT;

/**
 * Utility class to draw rectangle
 */
@SuppressWarnings("restriction")
public class DrawUtil {

	private static final int bandWidth = 3;

	/**
	 * Draw rectangle
	 * 
	 * @param hwndDraw
	 *            target window
	 * @param rect
	 *            rectangle
	 * @param color
	 *            color
	 */
	public static void drawRectangle(long hwndDraw, Rectangle rect, int color) {
		long hDC = OS.GetDCEx(hwndDraw, 0, OS.DCX_CACHE);
		long oldBrush = OS.SelectObject(hDC, OS.CreateSolidBrush(color));
		OS.PatBlt(hDC, rect.x, rect.y, rect.width, bandWidth, OS.PATCOPY);
		OS.PatBlt(hDC, rect.x, rect.y + bandWidth, bandWidth, rect.height
				- (bandWidth * 2), OS.PATCOPY);
		OS.PatBlt(hDC, rect.x + rect.width - bandWidth, rect.y + bandWidth,
				bandWidth, rect.height - (bandWidth * 2), OS.PATCOPY);
		OS.PatBlt(hDC, rect.x, rect.y + rect.height - bandWidth, rect.width,
				bandWidth, OS.PATCOPY);
		OS.DeleteObject(OS.SelectObject(hDC, oldBrush));
		OS.ReleaseDC(hwndDraw, hDC);
	}

	/**
	 * Redraw specified rectangle. Call this method after drawRectangle method.
	 * 
	 * @param hwndDraw
	 *            target window
	 * @param rect
	 *            target rectangle
	 */
	public static void redrawRectangle(long hwndDraw, Rectangle rect) {
		RECT osRect = new RECT();
		osRect.left = rect.x - bandWidth;
		osRect.top = rect.y - bandWidth;
		osRect.right = rect.x + rect.width + bandWidth;
		osRect.bottom = rect.y + rect.height + bandWidth;
		OS.RedrawWindow(hwndDraw, osRect, 0, OS.RDW_INVALIDATE
				| OS.RDW_ALLCHILDREN);
	}

}
