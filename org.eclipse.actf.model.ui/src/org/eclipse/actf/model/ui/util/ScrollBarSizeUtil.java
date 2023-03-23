/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.model.ui.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Utility class to obtain scroll bar size
 */
public class ScrollBarSizeUtil {
	private static int HORIZONTALBAR_HEIGHT = 15;
	private static int VERTICALBAR_WIDTH = 15;
	private static boolean isInit = false;

	private static void init() {
		// to avoid to use swt.internal
		if (!isInit) {
			try {
				Display display = Display.getCurrent();
				if (null != display) {
					Shell shell = new Shell(display);
					shell.setBounds(0, 0, 1, 1);
					Composite composite = new Composite(shell, SWT.V_SCROLL
							| SWT.H_SCROLL);
					// shell.open();
					HORIZONTALBAR_HEIGHT = composite.getHorizontalBar()
							.getSize().y;
					VERTICALBAR_WIDTH = composite.getVerticalBar().getSize().x;
					composite.dispose();
					shell.dispose();

					if (HORIZONTALBAR_HEIGHT < 0) {
						HORIZONTALBAR_HEIGHT = 15;
					}
					if (VERTICALBAR_WIDTH < 0) {
						VERTICALBAR_WIDTH = 15;
					}

					isInit = true;
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Get height of horizontal scroll bar
	 * 
	 * @return height of horizontal scroll bar
	 */
	public static int getHorizontalBarHeight() {
		init();
		return HORIZONTALBAR_HEIGHT;
	}

	/**
	 * Get width of vertical scroll bar
	 * 
	 * @return width of vertical scroll bar
	 */
	public static int getVerticalBarWidth() {
		init();
		return VERTICALBAR_WIDTH;
	}

}
