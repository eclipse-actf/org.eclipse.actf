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

package org.eclipse.actf.model.internal.dom.sgml.modelgroup;

import org.eclipse.actf.model.internal.dom.sgml.ISGMLParser;
import org.w3c.dom.Node;


public class RepModelGroup implements IModelGroup {
	private IModelGroup child;

	public RepModelGroup(IModelGroup modelGroup) {
		child = modelGroup;
		modelGroup.refer(true);
	}

	public String toString() {
		return "(" + child + ")*";  //$NON-NLS-1$//$NON-NLS-2$
	}

	public boolean match(ISGMLParser parser, Node parent, Node child) {
		parser.clearContextMap(parent);
		return this.child.match(parser, parent, child);
	}

	public boolean optional() {
		return true;
	}

	public void refer(boolean infinite) {
	}

	private boolean hash[];

	public boolean match(int number) {
		return hash[number];
	}

	public boolean[] rehash(int totalSize) {
		return hash != null ? hash : (hash = child.rehash(totalSize));
	}
}
