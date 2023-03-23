/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Daisuke SATO - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.ui.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.eclipse.swt.program.Program;

/**
 * Utility class to launch the default browser in the Operation System.
 */
public class BrowserLaunch {
	/**
	 * Open target URL with the default browser in the Operation System.
	 * 
	 * @param url
	 *            target URL
	 */
	public static void launch(final String url) {
		if (url == null)
			return;

		new Thread(new Runnable() {
			public void run() {
				// System.out.println(convertURL(url));
				Program.launch(convertURL(url));
			}
		}).start();
	}

	private static final String FILE_STR = "file:///"; //$NON-NLS-1$

	private static String convertURL(String url) {
		if (url.startsWith(FILE_STR)) {
			url = url.substring(FILE_STR.length());

			int index = url.lastIndexOf("#"); //$NON-NLS-1$
			if (index != -1)
				url = url.substring(0, index);

			try {
				url = URLDecoder.decode(url, "UTF-8"); //$NON-NLS-1$
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			url.replace('/', '\\');
		}
		return url;
	}

}
