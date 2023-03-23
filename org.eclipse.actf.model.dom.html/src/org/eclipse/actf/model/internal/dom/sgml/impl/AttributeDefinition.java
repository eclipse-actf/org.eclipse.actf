/*******************************************************************************
 * Copyright (c) 1998, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Goh KONDOH - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.model.internal.dom.sgml.impl;

import java.util.Enumeration;
import java.util.Vector;

import org.eclipse.actf.model.dom.html.ParseException;

/**
 * Attribute definition
 * 
 * @see ElementDefinition#getAttributeDef(java.lang.String)
 */
public class AttributeDefinition {
	// DeclaredValueType
	static final int CDATA = 1;

	static final int ID = 2;

	static final int IDREF = 3;

	static final int IDREFS = 4;

	static final int NUMBER = 5;

	static final int NAME = 6;

	static final int NAME_TOKEN_GROUP = 7;

	static final int NMTOKEN = 8;

	static final int NAMES = 9;

	// DefaultValueType
	static final int FIXED = 1;

	static final int REQUIRED = 2;

	static final int IMPLIED = 3;

	private int declaredType = 0;

	private int defaultType = 0;

	private String name;

	String defaultValue;

	private Vector<String> nameTokens;

	AttributeDefinition(String name) {
		this.name = name;
	}

	void addNameToken(String token) throws ParseException {
		if (declaredType != NAME_TOKEN_GROUP)
			throw new DTDParseException();
		if (nameTokens == null)
			nameTokens = new Vector<String>();
		nameTokens.addElement(token);
	}

	int getDeclaredType() {
		return declaredType;
	}

	@SuppressWarnings("nls")
	private String declaredTypeStrs[] = { null, "CDATA", "ID", "IDREF",
			"IDREFS", "NUMBER", "NAME", "NAME_TOKEN_GROUP", "NMTOKEN", "NAMES" };

	String getDeclaredTypeStr() {
		return declaredTypeStrs[declaredType];
	}

	int getDefaultType() {
		return defaultType;
	}

	String getDefaultValue() {
		return this.defaultValue;
	}

	String getName() {
		return name;
	}

	boolean nameTokenGroupContain(String token) {
		return nameTokens.contains(token);
	}

	void setDeclaredType(int type) {
		declaredType = type;
	}

	void setDefaultType(int type) {
		defaultType = type;
	}

	void setDefaultValue(String str) {
		this.defaultValue = str;
	}

	void setName(String arg) {
		name = arg;
	}

	// for debug.
	public String toString() {
		String ret = name + '\t';
		if (declaredType == NAME_TOKEN_GROUP) {
			for (Enumeration<String> e = nameTokens.elements(); e.hasMoreElements();) {
				ret = ret + ' ' + e.nextElement();
			}
		} else {
			ret = ret + declaredValueTable[declaredType];
		}
		ret = ret + '\t';
		if (defaultValueTable[defaultType] != null) {
			ret = ret + defaultValueTable[defaultType] + ' ';
		}
		if (defaultValue != null) {
			ret = ret + defaultValue;
		}
		return ret;
	}

	@SuppressWarnings("nls")
	private static String declaredValueTable[] = { null, "CDATA", "ID",
			"IDREF", "IDREFS", "NUMBER", "NAME", null, "NMTOKEN" };

	@SuppressWarnings("nls")
	private static String defaultValueTable[] = { null, "#FIXED", "#REQUIRED",
			"#IMPLIED" };
}
