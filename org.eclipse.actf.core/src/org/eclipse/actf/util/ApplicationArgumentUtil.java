/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Hisashi MIYASHITA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.util;

import org.eclipse.core.runtime.Platform;

/**
 * Utility class for checking application argument
 * 
 */
public class ApplicationArgumentUtil {
	/**
	 * Checks existence of application argument
	 * 
	 * @param arg
	 *            the target argument to check
	 * @return true if the target argument exists in application arguments
	 */
	public static boolean isAvailable(String arg) {
		String[] args = Platform.getApplicationArgs();
		for (int i = 0; i < args.length; i++) {
			if (arg.equals(args[i]))
				return true;
		}
		return false;
	}
}
