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

import java.io.IOException;
import java.io.Reader;

import org.eclipse.actf.model.dom.html.ParseException;

@SuppressWarnings("nls")
class DTDTokenizer extends SGMLTokenizer {
	/**
	 * Constructs SGMLTokenizer instance.
	 * @param reader reader that read SGML Documents.
	 */
	DTDTokenizer(Reader reader) {
		super(reader, DEFAULT);
	}
	DTDTokenizer(Reader reader, int state) {
		super(reader, state);
	}
	protected int defaultState() throws IOException, ParseException {
		int ch;
		ch = read();
		while (Character.isWhitespace((char)ch))
			ch = read();
		switch (ch) {
			case '%' :
				return (ttype = '%');
			case '<' :
				if ((ch = read()) == '/') {
					state = TAG;
					return (ttype = ETAGO);
				} else if (ch == '?') {
					StringBuffer data = new StringBuffer();
					while ((ch = read()) != '>' && ch != EOF) {
						data.append((char)ch);
					}
					sval = data.toString();
					return (ttype = PI);
				} else if (ch == '!') { // declaration.
					if ((ch = read()) == '-') { // <!-
						if ((ch = read()) == '-') { // <!-- enter comment
							StringBuffer sb = new StringBuffer();
							while (true) {
								while ((ch = read()) != '-' && ch != EOF)
									sb.append((char)ch);
								if ((ch = read()) == '-') { // --
									while ((ch = read()) == '-')
										sb.append((char)ch);
									StringBuffer ws = new StringBuffer();
									while (Character.isWhitespace((char)ch)) {
										ws.append((char)ch);
										ch = read();
									}
									if (ch == '>') {
										state = DEFAULT;
										sval = new String(sb);
										return (ttype = COMMENT);
									} else {
										sb.append("--");
										sb.append(ws);
										unread(ch);
									}
								} else if (ch == EOF) {
									throw new DTDParseException(
										"Unexpeceted EOF in comment. at "
											+ getReader()
											+ ':'
											+ getCurrentLine());
								} else {
									sb.append('-');
									sb.append((char)ch);
								}
							}
						} else { // <!- 
							unread(ch);
							unread('-');
						}
					} else {
						unread(ch);
					}
					state = TAG;
					return (ttype = MDO);
				} else {
					unread(ch);
					state = TAG;
					return (ttype = STAGO);
				}
			case EOF :
				return (ttype = EOF);
			case ';' :
				return (ttype = ';');
			case ']' :
				return (ttype = ']');
			default :
				if (Character.isLetter((char)ch)) {
					unread(ch);
					return readNameChar();
				}
				throw new DTDParseException(
					"Unknown character '"
						+ (char)ch
						+ "at "
						+ getReader()
						+ ':'
						+ getCurrentLine());
		}
	}
	public void skipToDSC() throws IOException {
		while (read() != ']');
	}
	protected int tag() throws IOException {
		int ch;
		StringBuffer sb = new StringBuffer();
		through();
		switch (ch = read()) {
			case '-' :
				if (Character.isWhitespace((char) (ch = read()))) {
					return (ttype = MINUS);
				} else if (ch == '(') {
					return (ttype = EXCO);
				} else if (ch == ')') {
					unread(ch);
					return (ttype = MINUS);
				} else if (!Character.isDigit((char)ch)) {
					while (!Character.isWhitespace((char)ch) && ch != '>') {
						sb.append((char)ch);
						ch = read();
					}
					unread(ch);
					sval = new String(sb);
					return (ttype = MISC);
				}
				sb.append('-');
				sb.append((char)ch);
				while (Character.isDigit((char) (ch = read())))
					sb.append((char)ch);
				if (ch == '%') {
					sb.append((char)ch);
				} else {
					unread(ch);
				}
				sval = new String(sb);
				return (ttype = NUM);
			case '+' :
				if (Character.isWhitespace((char) (ch = read()))) {
					return (ttype = PLUS);
				} else if (ch == '(') {
					return (ttype = INCO);
					//add 040703 to handle (tbody+|tr+)
					//add 040929 to handle (tr)+>
				} else if (ch == ')' || ch=='|' || ch=='>') {
					unread(ch);
					return (ttype = PLUS);
				} else if (!Character.isDigit((char)ch)) {
					while (!Character.isWhitespace((char)ch) && ch != '>') {
						sb.append((char)ch);
						ch = read();
					}
					unread(ch);
					sval = new String(sb);
					return (ttype = MISC);
				}
				sb.append('+');
				sb.append((char)ch);
				while (Character.isDigit((char) (ch = read())))
					sb.append((char)ch);
				if (ch == '%') {
					sb.append((char)ch);
				} else {
					unread(ch);
				}
				sval = new String(sb);
				return (ttype = NUM);
			case '#' :
				sb.append((char)ch);
				while (Character.isLetter((char) (ch = read()))
					|| Character.isDigit((char)ch))
					sb.append((char)ch);
				unread(ch);
				sval = new String(sb);
				return (ttype = NUM);
			case '\'' :
			case '"' :
				int closing = ch;
				while (true) {
					if ((ch = read()) == closing) {
						break;
					} else if (ch == EOF) {
						throw new IOException(
							"Unexpected EOF at "
								+ getReader()
								+ ':'
								+ getCurrentLine());
					} else {
						sb.append((char)ch);
					}
				}
				sval = new String(sb);
				return (ttype = STRING);
			case '>' :
				state = DEFAULT;
				return (ttype = '>');
			default :
				if (Character.isLetter((char)ch)) {
					if (ch == 'O') {
						int ch1;
						if (Character.isWhitespace((char) (ch1 = read())))
							return (ttype = OMITTABLE);
						unread(ch1);
					}
					unread(ch);
					return readNameChar();
				} else if (Character.isDigit((char)ch)) {
					sb.append((char)ch);
					ch = read();
					while (Character.isDigit((char)ch)) {
						sb.append((char)ch);
						ch = read();
					}
					if (ch == '%') {
						sb.append((char)ch);
					} else {
						unread(ch);
					}
					sval = new String(sb);
					return (ttype = NUM);
				} else {
					return (ttype = ch);
				}
		}
	}
	private int readNameChar() throws IOException {
		int ch = read();
		if (Character.isLetter((char)ch)) {
			StringBuffer sb = new StringBuffer();
			sb.append((char)ch);
			ch = read();
			while (Character.isLetter((char)ch)
				|| Character.isDigit((char)ch)
				|| ch == '.'
				|| ch == '-'
				|| ch == ':'
				|| ch == '#') {
				sb.append((char)ch);
				ch = read();
			}
			unread(ch);
			sval = sb.toString();
			return (ttype = NAME_CHAR);
		} else {
			unread(ch);
			return -1;
		}
	}

	protected void through() throws IOException {
		int ch;
		while (true) {
			ch = read();
			if (!Character.isWhitespace((char)ch)) {
				if (ch == '-') {
					if ((ch = read()) == '-') { // getting into a comment.
						while (true) {
							while ((ch = read()) != '-' && ch != EOF); // -
							if (ch == EOF) {
								throw new IOException("Unexpected EOF in comment");
							}
							if (read() == '-') { // -- getting out of a comment
								through();
								break;
							}
						}
					} else {
						unread(ch);
						unread('-');
						return;
					}
				} else {
					unread(ch);
					return;
				}
			}
		}
	}
}
