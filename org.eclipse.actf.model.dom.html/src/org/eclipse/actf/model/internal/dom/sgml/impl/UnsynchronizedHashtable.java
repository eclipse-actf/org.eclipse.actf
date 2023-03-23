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

class UnsynchronizedHashtable {
	class Entry {
		String key;

		ElementDefinition val;

		int num;

		Entry next;
	}

	private ElementDefinition linearTable[] = new ElementDefinition[256];

	private int count = 0;

	private void expand() {
		ElementDefinition newTable[] = new ElementDefinition[count * 2];
		System.arraycopy(linearTable, 0, newTable, 0, count);
		linearTable = newTable;
	}

	private Entry table[] = new Entry[256];

	private int hashCode(char key[]) {
		int ret = 0;
		for (int i = key.length - 1; i >= 0; i--) {
			ret = 25 * ret + (key[i] & 0x1f) - 1;
		}
		return ret;
	}

	final ElementDefinition get(int number) {
		return linearTable[number];
	}

	final ElementDefinition get(String key) {
		char str[] = key.toCharArray();
		for (Entry entry = table[0xff & hashCode(str)]; entry != null; entry = entry.next) {
			String entryKey = entry.key;
			if (entryKey.equalsIgnoreCase(key))
				return entry.val;
		}
		return null;
	}

	final void put(String key, ElementDefinition val) {
		char str[] = key.toCharArray();
		Entry entry = new Entry();
		entry.key = key;
		entry.val = val;
		val.number = entry.num = count;
		if (linearTable.length == count)
			expand();
		linearTable[count++] = val;
		int index = 0xff & hashCode(str);
		entry.next = table[index];
		table[index] = entry;
	}

	final int size() {
		return count;
	}
}
