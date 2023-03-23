/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.ui.util;

/**
 * Interface to provide commonly used Dialog messages
 */
public interface IDialogConstants {
	/**
	 * OK
	 */
	public static final String OK = " " + Messages.DialogConst_OK //$NON-NLS-1$
			+ " "; //$NON-NLS-1$
	/**
	 * cancel
	 */
	public static final String CANCEL = Messages.DialogConst_Cancel;
	/**
	 * none
	 */
	public static final String NONE = Messages.DialogConst_None;
	/**
	 * Help
	 */
	public static final String HELP = " " //$NON-NLS-1$
			+ Messages.DialogConst_Help + " "; //$NON-NLS-1$
	/**
	 * Add
	 */
	public static final String ADD = Messages.DialogConst_Add;
	/**
	 * Delete
	 */
	public static final String DELETE = Messages.DialogConst_Delete;
	/**
	 * Close
	 */
	public static final String CLOSE = Messages.DialogConst_Close;
	/**
	 * Browse
	 */
	public static final String BROWSE = Messages.DialogConst_Browse;
	/**
	 * Type target URL, then press enter.
	 */
	public static final String OPENFILE_INFO = Messages.DialogConst_OpenFile;
}
