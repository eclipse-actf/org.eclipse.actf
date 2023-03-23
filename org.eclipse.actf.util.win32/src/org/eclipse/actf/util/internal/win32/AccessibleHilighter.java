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

import org.eclipse.actf.util.win32.WindowUtil;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Utility class to show rectangle using OvelayWindow
 */
public class AccessibleHilighter {

	private static Thread lastThread = null;
	private static final int COLOR_YELLOW = 0x00FFFF;
	private static final int COLOR_GREEN = 0x00FF00;
	private static final int[][] FLASHING_COLORS = { { COLOR_YELLOW, 250 },
			{ COLOR_GREEN, 250 }, { COLOR_YELLOW, 250 }, { COLOR_GREEN, 250 },
			{ COLOR_YELLOW, 250 }, { COLOR_GREEN, 250 }, { COLOR_YELLOW, 1000 } };

	/**
	 * Flash rectangle without using OverlayWindow.
	 * 
	 * Note: This function may leave garbages on the screen.
	 * 
	 * @param rect
	 */
	public static void flashRectangle(final Rectangle rect) {
		if (null != lastThread) {
			lastThread.interrupt();
		}
		if (null != rect) {
			lastThread = new Thread(new Runnable() {
				public void run() {
					long hwndDraw = WindowUtil.GetDesktopWindow();
					try {
						for (int i = 0; i < FLASHING_COLORS.length; i++) {
							DrawUtil.drawRectangle(hwndDraw, rect,
									FLASHING_COLORS[i][0]);
							Thread.sleep(FLASHING_COLORS[i][1]);
						}
					} catch (InterruptedException e) {
					}
					DrawUtil.redrawRectangle(hwndDraw, rect);
					if (lastThread == Thread.currentThread()) {
						lastThread = null;
					}
				}
			});
			lastThread.start();
		}
	}
}
