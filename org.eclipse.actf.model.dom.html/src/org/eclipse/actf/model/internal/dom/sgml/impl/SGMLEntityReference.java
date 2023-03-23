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

class SGMLEntityReference {
	private String name;

	private SGMLEntityDeclaration ed;

	SGMLEntityReference(String name, SGMLEntityDeclaration ed) {
		this.name = name;
		this.ed = ed;
	}

	SGMLEntityDeclaration getEntityDeclaration() {
		return ed;
	}

	String getName() {
		return name;
	}
}
