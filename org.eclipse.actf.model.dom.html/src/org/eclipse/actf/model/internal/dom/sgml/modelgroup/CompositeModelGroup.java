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

public abstract class CompositeModelGroup implements IModelGroup {
    protected IModelGroup children[];

    protected int childLength;

    public void refer(boolean infinite) {
        for (int i = 0; i < childLength; i++) {
            children[i].refer(infinite);
        }
    }

    protected CompositeModelGroup(IModelGroup modelGroup) {
        children = new IModelGroup[128];
        children[0] = modelGroup;
        childLength = 1;
        modelGroup.refer(false);
    }

    public boolean match(int number) {
//        System.out.println(hash[number]);
        return hash[number];
    }

    private boolean hash[] = null;

    public boolean[] rehash(int totalSize) {
        if (hash != null) {
            return hash;
        }
        hash = new boolean[totalSize];
        for (int i = childLength - 1; i >= 0; i--) {
            boolean childHash[] = children[i].rehash(totalSize);
            if (childHash != null) {
                for (int j = totalSize - 1; j >= 0; j--)
                    hash[j] |= childHash[j];
            }
        }
        return hash;
    }

	public int getChildLength() {
		return childLength;
	}
}