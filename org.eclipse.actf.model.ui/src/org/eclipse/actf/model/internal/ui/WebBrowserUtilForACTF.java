/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.model.internal.ui;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.eclipse.ui.IEditorPart;

public class WebBrowserUtilForACTF {

	private static final String BROWSER_EDITOR_GET_URL_METHOD = "getURL"; //$NON-NLS-1$
	private static final String BROWSER_EDITOR_ID = "org.eclipse.ui.browser.editor"; //$NON-NLS-1$
	private static final String BROWSER_VIEWER_CLASS = "org.eclipse.ui.internal.browser.BrowserViewer"; //$NON-NLS-1$

	// need to ask swt team to provide APIs

	public static String getUrl(IEditorPart editor) {
		String url = null;

		if (editor != null
				&& BROWSER_EDITOR_ID.equals(editor.getEditorSite().getId())) {
			try {
				Field[] fields = editor.getClass().getDeclaredFields();
				for (Field field : fields) {
					if (BROWSER_VIEWER_CLASS.equals(field.getType().getName())) {
						field.setAccessible(true);
						Object view = field.get(editor);
						Method getURL = view.getClass().getMethod(
								BROWSER_EDITOR_GET_URL_METHOD, new Class[] {});
						url = (String) getURL.invoke(view, new Object[] {});
						field.setAccessible(false);
					}
				}
			} catch (Exception e) {
			}
		}
		return url;
	}

}
