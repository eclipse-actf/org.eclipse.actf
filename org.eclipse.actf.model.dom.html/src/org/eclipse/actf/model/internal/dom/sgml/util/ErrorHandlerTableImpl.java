/*******************************************************************************
 * Copyright (c) 1998, 2016 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Goh KONDOH - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.internal.dom.sgml.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import org.eclipse.actf.model.dom.html.IErrorHandler;

/**
 * For JDK1.2 or later
 */
public class ErrorHandlerTableImpl extends Hashtable<URL, IErrorHandler[]> implements
		IErrorHandlerTable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5122037603773966517L;

	public ErrorHandlerTableImpl() {
		init();
	}

	private void init() {
		Properties prop = new Properties();
		try {
			prop.load(getClass()
					.getResourceAsStream("errorhandlers.properties")); //$NON-NLS-1$
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (Enumeration<Object> e = prop.keys(); e.hasMoreElements();) {
			String urlStr = (String) e.nextElement();
			URL url;
			try {
				url = new URL(urlStr);
			} catch (MalformedURLException ex) {
				ex.printStackTrace();
				continue;
			}
			put(url, createErrorHandlers((String) prop.get(urlStr)));
		}
	}

	private String errorHandlerNameArray[] = new String[128];

	private IErrorHandler[] createErrorHandlers(String errorHandlerNames) {
		int lastComma = 0;
		int errorHandlerNum = 0;
		for (int i = 0; i < errorHandlerNames.length(); i++) {
			if (errorHandlerNames.charAt(i) == ',') {
				errorHandlerNameArray[errorHandlerNum++] = errorHandlerNames
						.substring(lastComma, i);
				lastComma = i + 1;
			}
		}
		errorHandlerNameArray[errorHandlerNum++] = errorHandlerNames
				.substring(lastComma);

		IErrorHandler ret[] = new IErrorHandler[errorHandlerNum];
		for (int i = 0; i < errorHandlerNum; i++) {
			try {
				Class<?> errorHandlerClass = Class
						.forName(errorHandlerNameArray[i]);
				ret[i] = (IErrorHandler) errorHandlerClass.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	public IErrorHandler[] getErrorHandlers(URL url) {
		return get(url);
	}
}
