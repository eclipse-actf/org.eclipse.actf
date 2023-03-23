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


public class OptModelGroup implements IModelGroup {
	private IModelGroup child;

	public OptModelGroup(IModelGroup modelGroup) {
		child = modelGroup;
		modelGroup.refer(false);
	}

	// for debug.
	public String toString() {
		return "(" + child + ")?"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	public boolean match(ISGMLParser parser, Node parent, Node child) {
		boolean ret = this.child.match(parser, parent, child);
		// System.out.println("opt: "+ret);
		return ret;
	}

	public boolean optional() {
		return true;
	}

	public void refer(boolean infinite) {
		child.refer(infinite);
	}

	private boolean hash[];

	public boolean match(int number) {
		return hash[number];
	}

	public boolean[] rehash(int totalSize) {
		return hash != null ? hash : (hash = child.rehash(totalSize));
	}
}