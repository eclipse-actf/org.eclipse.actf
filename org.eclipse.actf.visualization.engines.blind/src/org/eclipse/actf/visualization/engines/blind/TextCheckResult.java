/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
 
package org.eclipse.actf.visualization.engines.blind;

public enum TextCheckResult {
	/**
	 * Flag that means ALT may be OK.
	 */
	OK,

	/**
	 * Flag means ALT is null string ("").
	 */
	NULL,

	/**
	 * Flag means ALT is not null, but contains only blank characters.
	 */
	BLANK,

	/**
	 * Flag means ALT is not null, contains only blank characters and contains
	 * one or more &nbsp; characters.
	 */
	BLANK_NBSP,

	/**
	 * Flag means entire ALT string matches one of the NG words.
	 */
	NG_WORD,

	/**
	 * Flag means ALT string ends with the extension that is used for images.
	 */
	IMG_EXT,

	/**
	 * Flag means ALT string is equal to src attribute value.
	 */
	SAME_AS_SRC,

	/**
	 * Flag means ALT string is separated by in-word space characters, e.g.
	 * "p u s h".
	 */
	SPACE_SEPARATED,

	/**
	 * Flag means ALT string is Japanese and separated by in-word space
	 * characters, e.g. "処　理　開　始"　(start processing).
	 */
	SPACE_SEPARATED_JP,

	/**
	 * Contains a lot of space characters and graphical symbols. Usually
	 * reported for ASCII arts.
	 */
	ASCII_ART,

	/**
	 * Flag means ALT string contains many NG words within it (ratio is greater
	 * than 0.6).
	 */
	INCLUDING_MANY_NG_WORD,

	/**
	 * Flag means ALT string contains several NG words within it (ratio is
	 * between 0.3 and 0.6).
	 */
	INCLUDING_NG_WORD;
}
