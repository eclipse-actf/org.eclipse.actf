/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Masahide WASHIZAWA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.engines.voicebrowser;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("nls")
public class TextUtil {

	private static Map<Character, String> replaceMap = new HashMap<Character, String>();

	static {
		replaceMap.put(new Character('\u2122'), "trade mark");
		replaceMap.put(new Character('\u00A9'), "copyright mark");
		replaceMap.put(new Character('\u00AE'), "registered mark");
	}

	/**
	 * Method replace.
	 * @param s
	 * @return StringBuffer
	 */
	public static StringBuffer replace(StringBuffer s) {
		//StringBuffer tmp = new StringBuffer(s.toString());
		int length = s.length();
		for (int i = 0; i < length; i++) {
			String insert = replaceMap.get(new Character(s.charAt(i)));

			if (insert != null) {
				s.deleteCharAt(i);
				s.insert(i, " " + insert);
				i = i + insert.length();
				length = length + insert.length();
			}
		}
		return s;
	}

	/**
	 * Method substitute.
	 * @param input
	 * @param pattern
	 * @param replacement
	 * @return String
	 */
	public static String substitute(
		String input,
		String pattern,
		String replacement) {
		int index = input.indexOf(pattern.toLowerCase());

		if (index == -1) {
			return input;
		}

		StringBuffer buffer = new StringBuffer();

		buffer.append(input.substring(0, index) + replacement);

		if (index + pattern.length() < input.length()) {
			String rest =
				input.substring(index + pattern.length(), input.length());
			buffer.append(substitute(rest, pattern, replacement));
		}
		return buffer.toString();
	}

	/**
	 * Method trim.
	 * @param s
	 * @return String
	 */
	public static String trim(String s) {
		int from = 0, to = s.length();
		if (to == 0)
			return "";
		try {
			char c;
			for (c = s.charAt(from);
				(from < to)
					&& (Character.isWhitespace(c)
						|| Character.isSpaceChar(c)
						|| isReturn(c));
				) {
				from++;
				if (from == to) {
					return "";
				}
				c = s.charAt(from);
			}

			if (from != 0 && (to - from) == 1) {
				return s.substring(from, to + 1);
			}

			to = to - 1;
			for (c = s.charAt(to);
				(from < to)
					&& (Character.isWhitespace(c)
						|| Character.isSpaceChar(c)
						|| isReturn(c));
				) {

				to--;
				c = s.charAt(to);
			}
			return s.substring(from, to + 1);
		} catch (StringIndexOutOfBoundsException sioobe) {
			//sioobe.printStackTrace();
			//System.out.println("TextUtil: 74: Error: trim: [" + s + "]");
			return s;
		}
	}

	/**
	 * Method isReturn.
	 * @param c
	 * @return boolean
	 */
	private static boolean isReturn(char c) {
		if (c == '\n') {
			return true;
		} else if (c == '\r') {
			return true;
		} else {
			return false;
		}
	}
}
