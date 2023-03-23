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

import org.xml.sax.AttributeList;

class AttributeListImpl implements AttributeList {
    private String names[] = new String[8];

    private String values[] = new String[8];

    private String types[] = new String[8];

    private int num = 0;

    private void expand() {
        String newArray[] = new String[num * 2];
        System.arraycopy(names, 0, newArray, 0, num);
        names = newArray;
        newArray = new String[num * 2];
        System.arraycopy(values, 0, newArray, 0, num);
        values = newArray;
        newArray = new String[num * 2];
        System.arraycopy(types, 0, newArray, 0, num);
        types = newArray;
    }

    final void addAttribute(String name, String type, String value) {
        int num = this.num;
        if (names.length == num)
            expand();
        names[num] = name;
        types[num] = type;
        values[num] = value;
        this.num = num + 1;
    }

    public int getLength() {
        return num;
    }

    public String getName(int i) {
        try {
            return names[i];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public String getType(int i) {
        try {
            return types[i];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public String getValue(int i) {
        try {
            return values[i];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public String getType(String name) {
        for (int i = num - 1; i >= 0; i--) {
            if (names[i].equalsIgnoreCase(name))
                return types[i];
        }
        return null;
    }

    public String getValue(String name) {
        for (int i = num - 1; i >= 0; i--) {
            if (names[i].equalsIgnoreCase(name))
                return values[i];
        }
        return null;
    }
}