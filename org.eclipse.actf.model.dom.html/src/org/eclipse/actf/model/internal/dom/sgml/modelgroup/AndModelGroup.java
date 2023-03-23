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

import java.util.Hashtable;

import org.eclipse.actf.model.internal.dom.sgml.ISGMLParser;
import org.w3c.dom.Node;

public class AndModelGroup extends CompositeModelGroup {
	public AndModelGroup(IModelGroup modelGroup) {
		super(modelGroup);
	}

	public void add(IModelGroup modelGroup) {
		/*
		 * (a & b & (c & d)) != (a & b & c & d)
		 */
		children[childLength++] = modelGroup;
	}

	public String toString() {
		String ret = new String("("); //$NON-NLS-1$
		for (int i = 0; i < childLength - 1; i++) {
			ret = ret + children[i] + '&';
		}
		ret = ret + children[childLength - 1] + ')';
		return ret;
	}

	public boolean match(ISGMLParser parser, Node parent, Node child) {
		Hashtable<Node, AndContext> map = parser.getAndMap();
		AndContext ac = map.get(parent);
		if (ac == null) {
			ac = new AndContext(this);
			map.put(parent, ac);
		} else if (ac.prev != null && ac.prev.match(parser, parent, child)) {
			return true;
		} else if (ac.prevIndex != -1) {
			ac.mgs[ac.prevIndex] = null;
		}
		for (int i = childLength - 1; i >= 0; i--) {
			IModelGroup mg = ac.mgs[i];
			if (mg != null && mg.match(parser, parent, child)) {
				ac.prevIndex = i;
				if (mg instanceof RepModelGroup || mg instanceof PlusModelGroup) {
					ac.prev = mg;
				}
				return true;
			}
		}
		return false;
	}

	public boolean optional() {
		for (int i = childLength - 1; i >= 0; i--) {
			if (!children[i].optional())
				return false;
		}
		return true;
	}

	public class AndContext {
		IModelGroup mgs[];

		IModelGroup prev = null;

		int prevIndex = -1;

		AndContext(AndModelGroup amg) {
			mgs = new IModelGroup[amg.childLength];
			for (int i = 0; i < mgs.length; i++) {
				mgs[i] = amg.children[i];
			}
		}

		void setPrev(IModelGroup mg) {
			prev = mg;
		}
	}
}