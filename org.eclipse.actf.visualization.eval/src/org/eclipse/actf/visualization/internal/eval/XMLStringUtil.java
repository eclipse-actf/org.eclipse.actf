/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Shin SAITO - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.eval;


/**
 * Utility class to canonicalize String 
 */
public class XMLStringUtil {


	/**
	 * Canonicalize target String
	 * 
	 * @param targetS
	 *            the target String
	 * @return canonicalized String
	 */
	@SuppressWarnings("nls")
	public static String canonicalize(String targetS) {
		return (targetS.replaceAll("\\p{Cntrl}", "").replaceAll("&", "&amp;")
				.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll(
						"\"", "&quot;").replaceAll("\'", "&apos;"));
	}

}
