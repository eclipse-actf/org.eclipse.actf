/*******************************************************************************
 * Copyright (c) 2007, 2025 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *    IBM Corporation - updates
 *******************************************************************************/
package org.eclipse.actf.model.internal.ui.editors.edge;

import java.lang.reflect.Field;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.internal.ole.win32.COM;
import org.eclipse.swt.internal.ole.win32.ICoreWebView2Controller;

class WebBrowserEdgeHelper {

	private ICoreWebView2Controller controller = null;

	private Field field_controller;
	private Object object_webBrowser;

	WebBrowserEdgeHelper(Browser browser) {
		try {
			Field field = browser.getClass().getDeclaredField("webBrowser");
			field.setAccessible(true);

			object_webBrowser = field.get(browser);
			field_controller = object_webBrowser.getClass().getDeclaredField("controller");
			field_controller.setAccessible(true);
			// initialization of "controller" is performed when getZoomFactor() is called.
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getController() {
		if (controller == null) {
			// initialize "controller" if it had not initialized.
			try {
				controller = (ICoreWebView2Controller) field_controller.get(object_webBrowser);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	double getZoomFactor() {
		long[] bits = { 0 };
		getController();
		if (controller != null) {
			COM.VtblCall(7, controller.getAddress(), bits);
		}
		return Double.longBitsToDouble(bits[0]);
	}

	void setZoomFactor(double zoomFactor) {
		getController();
		if (controller != null) {
			COM.VtblCall(8, controller.getAddress(), zoomFactor);
		}
	}
}
