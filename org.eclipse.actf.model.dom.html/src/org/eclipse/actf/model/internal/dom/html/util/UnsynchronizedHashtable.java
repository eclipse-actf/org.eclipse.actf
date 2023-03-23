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

package org.eclipse.actf.model.internal.dom.html.util;

import org.eclipse.actf.model.internal.dom.html.impl.Constructor;

public class UnsynchronizedHashtable {
	class Entry {
		String key;

		Constructor val;

		Entry next;
	}

	private Entry table[] = new Entry[256];

	private int hashCode(char key[]) {
		int ret = 0;
		for (int i = key.length - 1; i >= 0; i--) {
			ret = 25 * ret + (key[i] & 0x1f) - 1;
		}
		return ret;
	}

	public final Constructor get(String key) {
		char str[] = key.toCharArray();
		for (Entry entry = table[0xff & hashCode(str)]; entry != null; entry = entry.next) {
			String entryKey = entry.key;
			if (entryKey.equalsIgnoreCase(key))
				return entry.val;
		}
		return null;
	}

	public final void put(String key, Constructor val) {
		char str[] = key.toCharArray();
		Entry entry = new Entry();
		entry.key = key;
		entry.val = val;
		int index = 0xff & hashCode(str);
		entry.next = table[index];
		table[index] = entry;
	}
}
