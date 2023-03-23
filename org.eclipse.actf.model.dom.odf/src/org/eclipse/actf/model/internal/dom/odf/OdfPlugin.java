/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tatsuya ISHIHARA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.model.internal.dom.odf;

import org.eclipse.core.runtime.Plugin;

/**
 * The activator class controls the plug-in life cycle
 */
public class OdfPlugin extends Plugin {
	// The shared instance
	private static OdfPlugin plugin;

	/**
	 * The constructor
	 */	
	public OdfPlugin() {
		plugin = this;
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */	
	public static OdfPlugin getDefault() {
		return plugin;
	}
}
