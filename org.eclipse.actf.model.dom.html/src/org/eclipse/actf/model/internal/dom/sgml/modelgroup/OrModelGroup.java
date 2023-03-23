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

import org.eclipse.actf.model.dom.html.ParseException;
import org.eclipse.actf.model.internal.dom.sgml.ISGMLParser;
import org.eclipse.actf.model.internal.dom.sgml.impl.ElementDefinition;
import org.w3c.dom.Node;


public class OrModelGroup extends CompositeModelGroup {
    public OrModelGroup(IModelGroup modelGroup) {
        super(modelGroup);
        if (modelGroup instanceof OrModelGroup) {
            OrModelGroup orMG = (OrModelGroup) modelGroup;
            this.childLength = orMG.childLength;
            for (int i = 0; i < this.childLength; i++) {
                this.children[i] = orMG.children[i];
            }
        }
    }

    /**
     * (a | b | (c | d)) == (a | b | c | d)
     */
    public void add(IModelGroup modelGroup) {
        if (modelGroup instanceof OrModelGroup) {
            OrModelGroup orModelGroup = (OrModelGroup) modelGroup;
            for (int i = 0; i < orModelGroup.childLength; i++) {
                children[childLength++] = orModelGroup.children[i];
            }
        } else {
            children[childLength++] = modelGroup;
        }
    }

    public ElementDefinition[] getChildren() throws ParseException {
        ElementDefinition ret[] = new ElementDefinition[childLength];
        for (int i = 0; i < childLength; i++) {
            ret[i] = (ElementDefinition) children[i];
        }
        return ret;
    }

    // for debug.
    public String toString() {
		StringBuilder ret = new StringBuilder();
		ret.append("(");
        for (int i = 0; i < childLength - 1; i++) {
            ret.append(children[i]);
            ret.append('|');
        }
        ret.append(children[childLength - 1]);
        ret.append(")");
        return ret.toString();
    }

    public boolean match(ISGMLParser parser, Node parent, Node child) {
        for (int i = 0; i < childLength; i++) {
            if (children[i].match(parser, parent, child)) {
//                System.out.println("or: match");
                return true;
            }
        }
//        System.out.println("or: out");
        return false;
    }

    public boolean optional() {
        for (int i = childLength - 1; i >= 0; i--) {
            if (children[i].optional())
                return true;
        }
        return false;
    }
}