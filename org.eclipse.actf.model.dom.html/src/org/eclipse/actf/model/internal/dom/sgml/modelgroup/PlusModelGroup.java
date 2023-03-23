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


public class PlusModelGroup implements IModelGroup {
    private IModelGroup child;

    RepModelGroup rep;

    public PlusModelGroup(IModelGroup modelGroup) {
        child = modelGroup;
        rep = new RepModelGroup(modelGroup);
    }

    // for debug.
    public String toString() {
        return "(" + child + ")+";  //$NON-NLS-1$//$NON-NLS-2$
    }

    public boolean match(ISGMLParser parser, Node parent, Node child) {
        Hashtable<Node, Node> map = parser.getPlusMap();
        boolean ret;
        if (map.get(parent) == null) {
            ret = this.child.match(parser, parent, child);
            if (ret) {
                map.put(parent, parent);
            }
//            System.out.println("plus: "+ret);
            return ret;
        } else {
            ret = rep.match(parser, parent, child);
//            System.out.println("plus: "+ret);
            return ret;
        }
    }

    public boolean optional() {
        return child.optional();
    }

    public void refer(boolean infinite) {
    }

    private boolean hash[];

    public boolean match(int number) {
        return hash[number];
    }

    public boolean[] rehash(int totalSize) {
        return hash != null ? hash : (hash = rep.rehash(totalSize));
    }
}