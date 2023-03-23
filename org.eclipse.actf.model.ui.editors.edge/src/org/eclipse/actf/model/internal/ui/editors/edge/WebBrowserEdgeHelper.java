/*******************************************************************************
 * Copyright (c) 2007, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.model.internal.ui.editors.edge;

import java.lang.reflect.Field;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.internal.ole.win32.COM;
import org.eclipse.swt.internal.ole.win32.ICoreWebView2Controller;

class WebBrowserEdgeHelper {

    private ICoreWebView2Controller controller;

    WebBrowserEdgeHelper(Browser browser) {
        try {
            Field field = browser.getClass().getDeclaredField("webBrowser");
            field.setAccessible(true);
            Object webBrowser = field.get(browser);
            field = webBrowser.getClass().getDeclaredField("controller");
            field.setAccessible(true);
            controller = (ICoreWebView2Controller)field.get(webBrowser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    double getZoomFactor() {
        long[] bits = { 0 };
        if (controller != null) {
            COM.VtblCall(7, controller.getAddress(), bits);
        }
        return Double.longBitsToDouble(bits[0]);
    }

    void setZoomFactor(double zoomFactor) {
        if (controller != null) {
            COM.VtblCall(8, controller.getAddress(), zoomFactor);
        }
    }
}
