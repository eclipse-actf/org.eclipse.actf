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
package org.eclipse.actf.model.dom.odf.number;

/**
 * NumberConstants defines the constant values used by data style namespace of ODF DOM API
 */
@SuppressWarnings("nls")
public final class NumberConstants {
	static public final String NUMBER_NAMESPACE_URI = "urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0";

	static public final String ELEMENT_NUMBER = "number";

	static public final String ELEMENT_TEXT = "text";

	static public final String ELEMENT_NUMBER_STYLE = "number-style";

	static public final String ATTR_MIN_INTEGER_DIGITS = "min-integer-digits";

	static public final String ATTR_DECIMAL_PLACES = "decimal-places";

	static public final String ATTR_GROUPING = "grouping";
}