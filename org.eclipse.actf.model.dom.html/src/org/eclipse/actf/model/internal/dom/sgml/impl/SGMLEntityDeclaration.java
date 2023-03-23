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

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.eclipse.actf.model.internal.dom.sgml.modelgroup.IModelGroup;

class SGMLEntityDeclaration {
	private String name = null;

	private String replacementString = null;

	private IModelGroup replacementSubtree = null;

	private AttributeDefinition attributeDefinitions[] = null;

	private SGMLDocTypeDef dtd;

	SGMLEntityDeclaration(String entityName, String entityString,
			SGMLDocTypeDef dtd) {
		this.name = entityName;
		this.replacementString = entityString;
		this.dtd = dtd;
	}

	AttributeDefinition[] getAttributeDefinitions() {
		return attributeDefinitions;
	}

	Reader getReplacementReader() throws IOException {
		return new StringReader(replacementString);
	}

	String getReplacementString() {
		return replacementString;
	}

	IModelGroup getReplacementSubtree() {
		return replacementSubtree;
	}

	void setAttributeDefinitions(AttributeDefinition ads[]) {
		attributeDefinitions = ads;
	}

	void setReplacementString(String arg) {
		replacementString = arg;
	}

	void setReplacementSubtree(IModelGroup arg) {
		replacementSubtree = arg;
	}

	public String toString() {
		return name + '=' + replacementString;
	}

	String getEntityName() {
		return name;
	}

	private int charEntity = -1;

	int getReplacementChar() {
		return charEntity;
	}

	void setReplacementChar(int ch) {
		charEntity = ch;
	}

	SGMLDocTypeDef getDTD() {
		return dtd;
	}
}
