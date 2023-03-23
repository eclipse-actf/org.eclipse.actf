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

import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.internal.Callback;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Abstract class for message transfer with other windows via
 * {@link COPYDATASTRUCT}
 */
@SuppressWarnings("restriction")
public abstract class WMCMonitor {

	private static String productName = "Unknown"; //$NON-NLS-1$
	static {
		IProduct product = Platform.getProduct();
		if (null != product) {
			productName = product.getName();
		}
	}
	private long oldShellProc = 0;

	/**
	 * Create a WMCMonitor with default title
	 */
	public WMCMonitor() {
		this("WMCWindow:" + productName); //$NON-NLS-1$
	}

	/**
	 * Create a WMCMonitor
	 * 
	 * @param title
	 *            window title
	 */
	public WMCMonitor(String title) {
		Display display = Display.getCurrent();
		if (null != display) {
			Shell activeShell = display.getActiveShell();
			if (null != activeShell) {
				Callback callback = new Callback(this, "shellWindowProc", 4); //$NON-NLS-1$
				long address = callback.getAddress();
				if (address != 0) {
					final Shell shell = new Shell();
					shell.setVisible(false);
					shell.setBounds(0, 0, 0, 0);
					shell.setText(title);
					oldShellProc = OS.GetWindowLongPtr(shell.handle,
							OS.GWL_WNDPROC);
					OS.SetWindowLongPtr(shell.handle, OS.GWL_WNDPROC, address);
					activeShell.addDisposeListener(new DisposeListener() {
						public void widgetDisposed(DisposeEvent e) {
							shell.dispose();
						}
					});
				} else {
					callback.dispose();
				}
			}
		}
	}

	/**
	 * Window procedure
	 * 
	 * @param hwnd
	 * @param msg
	 * @param wParam
	 * @param lParam
	 * @return
	 */
	long shellWindowProc(long hwnd, long msg, long wParam, long lParam) {
		try {
			if (COPYDATASTRUCT.WM_COPYDATA == msg) {
				return onCopyData(hwnd, wParam, new COPYDATASTRUCT(lParam));
			}
			return OS.CallWindowProc(oldShellProc, hwnd, (int)msg, wParam, lParam);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * WM_COPYDATA handler
	 * 
	 * @param hwnd
	 * @param hwndFrom
	 * @param cds
	 * @return 1 if processed
	 */
	protected abstract long onCopyData(long hwnd, long hwndFrom, COPYDATASTRUCT cds);

}
