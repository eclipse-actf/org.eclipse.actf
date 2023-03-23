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

import org.eclipse.actf.model.dom.html.IParserError;
import org.eclipse.actf.model.dom.html.ParseException;
import org.eclipse.actf.model.internal.dom.sgml.ISGMLConstants;
import org.xml.sax.DocumentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Tokenizer for document instance.
 */
@SuppressWarnings("nls")
class InsTokenizer implements ISGMLConstants, Locator {
	private boolean extractChar = true;

	private boolean extractNum = true;

	private int state;

	private boolean pushBacked;

	public String sval;

	public int ttype;

	private Reader reader;

	void extractCharEntity(boolean b) {
		extractChar = b;
	}

	void extractNumEntity(boolean b) {
		extractNum = b;
	}

	private SGMLParser parser;

	/**
	 * Constructs InsTokenizer instance.
	 * 
	 * @param reader
	 *            reader that read SGML Documents.
	 */
	public InsTokenizer(Reader reader, SGMLParser parser) {
		this(reader, DEFAULT, parser);
	}

	InsTokenizer(Reader reader, int state, SGMLParser parser) {
		this.reader = reader;
		this.state = state;
		this.parser = parser;
		pushBacked = false;
	}

	private boolean forward(String str, boolean ignoreCase) throws IOException {
		int buf[] = new int[str.length()];
		boolean ret = true;
		int i;
		if (!ignoreCase) {
			for (i = 0; i < buf.length; i++) {
				buf[i] = read();
				if (buf[i] != str.charAt(i)) {
					ret = false;
					i++;
					break;
				}
			}
		} else {
			for (i = 0; i < buf.length; i++) {
				buf[i] = read();
				if (Character.toUpperCase((char) buf[i]) != Character
						.toUpperCase(str.charAt(i))
						&& Character.toLowerCase((char) buf[i]) != Character
								.toLowerCase(str.charAt(i))) {
					ret = false;
					i++;
					break;
				}
			}
		}
		while (i > 0)
			unread(buf[--i]);
		return ret;
	}

	private int eatComment() throws IOException, ParseException {
		int ch;
		miscIndex = 0;
		if ((ch = read()) == '-') { // <!-

			if ((ch = read()) == '-') { // <!-- enter comment
				while (true) {
					while ((ch = read()) != '-' && ch != EOF) {
						setCharToMiscBuffer(miscIndex, (char) ch);
						miscIndex++;
					}
					if ((ch = read()) == '-') { // --
						if ((ch = read()) == '-') {
							// This is a very nasty case. <!-- ... ---...>
							// eat comment until '>' appears.
							if (miscBuffer.length <= miscIndex + 2) {
								expandMiscBuffer();
							}
							miscBuffer[miscIndex] = miscBuffer[miscIndex + 1] = miscBuffer[miscIndex + 2] = '-';
							miscIndex += 3;
							storeUntil('>');
							if (Character
									.isWhitespace(miscBuffer[miscIndex - 1])
									|| miscBuffer[miscIndex - 1] == '-'
									|| miscBuffer[miscIndex - 1] == '!') {
								if (miscBuffer[miscIndex - 2] == '-'
										&& miscBuffer[miscIndex - 1] == '-') {
									miscIndex -= 2;
								}
								state = DEFAULT;
								sval = new String(miscBuffer, 0, miscIndex);
								parser.setCharacter(miscBuffer, 0, miscIndex);
								return (ttype = COMMENT);
							} else {
								setCharToMiscBuffer(miscIndex, '>');
								miscIndex++;
								continue;
							}
						} else if (ch == '!') { // doesn't meet specification,
							// but...
							if ((ch = read()) == '>') {
								state = DEFAULT;
								sval = new String(miscBuffer, 0, miscIndex);
								parser.setCharacter(miscBuffer, 0, miscIndex);
								return (ttype = COMMENT);
							} else {
								if (miscBuffer.length <= miscIndex + 2) {
									expandMiscBuffer();
								}
								miscBuffer[miscIndex] = miscBuffer[miscIndex + 1] = '-';
								miscBuffer[miscIndex + 2] = '!';
								miscIndex += 3;
								unread(ch);
								continue;
							}
						}
						int wsIndex = 0;
						char ws[] = null;
						if (Character.isWhitespace((char) ch)) {
							// Compressed Representation.
							ws = new char[256];
							ws[0] = (char) ch;
							ws[1] = 1;
							while (Character.isWhitespace((char) ch)) {
								if (ch == ws[wsIndex]) {
									ws[wsIndex + 1]++;
								} else {
									wsIndex += 2;
									ws[wsIndex] = (char) ch;
									ws[wsIndex + 1] = 1;
								}
								ch = read();
							}
						}
						if (ch == '>') {
							state = DEFAULT;
							sval = new String(miscBuffer, 0, miscIndex);
							parser.setCharacter(miscBuffer, 0, miscIndex);
							return (ttype = COMMENT);
						} else {
							if (miscBuffer.length <= miscIndex + 2) {
								expandMiscBuffer();
							}
							miscBuffer[miscIndex] = miscBuffer[miscIndex + 1] = '-';
							miscIndex += 2;
							for (int i = 0; i < wsIndex; i += 2) {
								for (int j = 0; j < ws[i + 1]; j++)
									setCharToMiscBuffer(miscIndex, ws[i]);
								miscIndex++;
							}
							unread(ch);
						}
					} else if (ch == EOF) {
						parser.error(IParserError.MISC_ERR,
								"Unexpected EOF in comment.");
						for (int i = 0; i < miscBuffer.length; i++) {
							if (miscBuffer[i] == '>') {
								unread(miscBuffer, i + 1, miscIndex - i - 1);
								state = DEFAULT;
								sval = new String(miscBuffer, 0, i);
								parser.setCharacter(miscBuffer, 0, miscIndex);
								return (ttype = COMMENT);
							}
						}
						throw new ParseException("Unexpected EOF in comment.");
					} else {
						setCharToMiscBuffer(miscIndex, '-');
						miscIndex++;
						setCharToMiscBuffer(miscIndex, (char) ch);
						miscIndex++;
					}
				} // while (true) {
			} else { // <!-
				// if finished reading dtd, deals with <!- ... > as a comment
				sval = "-" + (char) ch + eatUntil('>');
				char svalChar[] = sval.toCharArray();
				parser.setCharacter(svalChar, 0, svalChar.length);
				return (ttype = COMMENT);
			}
		} else if ((ch == 'D' || ch == 'd') && forward("OCTYPE", true)) { // chech
			// if
			// <!DOCTYPE
			unread(ch);
			state = TAG;
			return (ttype = MDO);
		} else {
			// if finished reading dtd, deals with <! ... > as a comment
			sval = (char) ch + eatUntil('>');
			char svalChar[] = sval.toCharArray();
			parser.setCharacter(svalChar, 0, svalChar.length);
			return (ttype = COMMENT);
		}
	}

	private int etagStartIndex;

	private int defaultState() throws IOException, ParseException, SAXException {
		miscIndex = 0;
		int ch = read();
		while (Character.isWhitespace((char) ch)) {
			addCharToMiscBuffer((char) ch);
			ch = read();
		}
		if (miscIndex > 0) {
			if (prsvWS) {
				unread(ch);
				sval = new String(miscBuffer, 0, miscIndex);
				parser.setCharacter(miscBuffer, 0, miscIndex);
				return (ttype = WHITESPACE);
			}
		}
		switch (ch) {
		case '<':
			if (miscIndex > 0 && !prsvWS) {
				DocumentHandler docHandler = parser.getDocumentHandler();
				if (docHandler != null) {
					unread(ch);
					docHandler.ignorableWhitespace(miscBuffer, 0, miscIndex);
					read();
				}
			}
			miscIndex = 0;
			if ((ch = read()) == '/') {
				if (Character.isLowerCase((char) (ch = read()))
						|| Character.isUpperCase((char) ch)) {
					unread(ch);
					state = ETAG;
					etagStartIndex = miscIndex;
					addCharToMiscBuffer('<');
					addCharToMiscBuffer('/');
					return (ttype = ETAGO);
				} else {
					addCharToMiscBuffer('<');
					addCharToMiscBuffer('/');
					return readPCDATA(ch);
				}
			} else if (ch == '?') {
				miscIndex = 0;
				while ((ch = read()) != '>' && ch != EOF) {
					addCharToMiscBuffer((char) ch);
				}
				sval = new String(miscBuffer, 0, miscIndex);
				return (ttype = PI);
			} else if (ch == '!') { // declaration.
				return eatComment();
			} else if (Character.isLowerCase((char) ch)
					|| Character.isUpperCase((char) ch)) {
				unread(ch);
				state = TAG;
				return (ttype = STAGO);
			} else {
				addCharToMiscBuffer('<');
				return readPCDATA(ch);
			}
		case EOF:
			return (ttype = EOF);
		default:
			return readPCDATA(ch);
		}
	}

	private int entityBuf[] = new int[64];

	private int entityBufIndex;

	private int rslvCharEnt() throws IOException, ParseException, SAXException {
		entityBufIndex = 0;
		int ch = entityBuf[entityBufIndex++] = read();
		if (ch == '#') {
			ch = entityBuf[entityBufIndex++] = read();
			int radix = 10;
			if (ch == 'x' || ch == 'X') {
				radix = 16;
				ch = entityBuf[entityBufIndex++] = read();
			}
			int k = Character.digit((char) ch, radix);
			if (k >= 0) {
				int num = k;
				ch = entityBuf[entityBufIndex++] = read();
				while ((k = Character.digit((char) ch, radix)) >= 0) {
					num = num * radix + k;
					ch = entityBuf[entityBufIndex++] = read();
				}
				if (ch == ';') {
					char orig[] = new char[entityBufIndex - 1];
					for (int i = 0; i < entityBufIndex - 1; i++) {
						orig[i] = (char) entityBuf[i];
					}
					parser.putCharNumEntity(new Character((char) num),
							new String(orig));
					return num;
				}
			}
		} else if (Character.isLowerCase((char) ch)
				|| Character.isUpperCase((char) ch)) {
			do {
				ch = entityBuf[entityBufIndex++] = read();
			} while (Character.isLowerCase((char) ch)
					|| Character.isUpperCase((char) ch));
			if (ch == ';') {
				char orig[] = new char[entityBufIndex - 1];
				for (int i = 0; i < entityBufIndex - 1; i++)
					orig[i] = (char) entityBuf[i];
				String orgStr = new String(orig);
				int charEntity = parser.getCharEntity(orgStr);
				if (charEntity != -1) {
					parser.putCharNumEntity(new Character((char) charEntity),
							orgStr);
					return charEntity;
				}
			}
		}
		for (int i = entityBufIndex - 1; i >= 0; i--)
			unread(entityBuf[i]);
		return -1;
	}

	private void setCharToMiscBuffer(int miscIndex, char c) {
		if (miscBuffer.length <= miscIndex) {
			expandMiscBuffer();
		}
		miscBuffer[miscIndex] = c;
	}

	private void addCharToMiscBuffer(char c) {
		if (miscBuffer.length <= miscIndex) {
			expandMiscBuffer();
		}
		miscBuffer[miscIndex++] = c;
	}

	private int readPCDATA(int cur) throws IOException, ParseException,
			SAXException {
		int succeeding;
		int firstLFindex = this.lastLFindex;
		if (!extractChar && !extractNum) {
			setCharToMiscBuffer(miscIndex, (char) cur);
			miscIndex++;

			while (((cur = read()) != '<' || !Character
					.isLowerCase((char) (succeeding = sence()))
					&& !Character.isUpperCase((char) succeeding)
					&& succeeding != '!'
					&& succeeding != '?'
					&& (succeeding != '/' || !Character
							.isLowerCase((char) (succeeding = sence(1)))
							&& !Character.isUpperCase((char) succeeding)))
					&& cur != EOF) {
				setCharToMiscBuffer(miscIndex, (char) cur);

				if (miscBuffer[miscIndex - 1] == '\n' && cur != '\n') {
					firstLFindex = this.index - 1;
				}
				miscIndex++;
			}
		} else if (cur != EOF) {
			do {
				if (cur == '&') {
					if (!extractNum) {
						if ((cur = read()) == '#') {
							addCharToMiscBuffer('&');
							addCharToMiscBuffer('#');
							continue;
						} else {
							unread(cur);
						}
					}
					int charEntity = rslvCharEnt();
					if (charEntity == -1) {
						addCharToMiscBuffer('&');
					} else {
						addCharToMiscBuffer((char) charEntity);
					}
				} else {
					setCharToMiscBuffer(miscIndex, (char) cur);
					if (miscIndex > 0 && miscBuffer[miscIndex - 1] == '\n'
							&& cur != '\n') {
						firstLFindex = this.index - 2;
					}
					miscIndex++;
				}
			} while (((cur = read()) != '<' || !Character
					.isLowerCase((char) (succeeding = sence()))
					&& !Character.isUpperCase((char) succeeding)
					&& succeeding != '!'
					&& succeeding != '?'
					&& (succeeding != '/' || !Character
							.isLowerCase((char) (succeeding = sence(1)))
							&& !Character.isUpperCase((char) succeeding)))
					&& cur != EOF);
		}
		if (cur != EOF)
			unread(cur);
		if (!prsvWS) {
			int begin = 0;
			while ((cur = miscBuffer[begin]) == '\r' || cur == '\n')
				begin++;
			int end = miscIndex - 1;
			try {
				while ((cur = miscBuffer[end]) == '\r' || cur == '\n')
					end--;
			} catch (ArrayIndexOutOfBoundsException e) {
				sval = new String(miscBuffer, 0, miscIndex);
				parser.setCharacter(miscBuffer, 0, miscIndex);
				return (ttype = PCDATA);
			}
			int len = end + 1 - begin;
			sval = new String(miscBuffer, begin, len);
			parser.setCharacter(miscBuffer, begin, len);
			if (end != miscIndex - 1) {
				unread(miscBuffer, end + 1, miscIndex - end - 1);
				lastLFindex = firstLFindex;
			}
		} else {
			sval = new String(miscBuffer, 0, miscIndex);
			parser.setCharacter(miscBuffer, 0, miscIndex);
			return (ttype = WHITESPACE);
		}
		return (ttype = PCDATA);
	}

	private char endTagBuffer[] = new char[64];

	/**
	 * Gets raw text up to end tag specified(&lt;<code>end</code>&gt;)
	 * 
	 * @param end
	 *            Specified end tags name
	 * @return raw text up to specified end tag.
	 */
	final String rawText(String end) throws IOException, ParseException {
		char endTagArray[] = end.toUpperCase().toCharArray();
		int ch;
		miscIndex = 0;
		int limitCandidate = 0;

		boolean inComment = false;

		outer: while (true) {
			ch = read();
			if (!inComment && ch == '<') {
				ch = read();
				if (ch == '/') {
					if (limitCandidate == 0) {
						limitCandidate = miscIndex;
					}
					endTagBufferIndex = 0;
					for (int i = 0; i < end.length(); i++) {
						endTagBuffer[endTagBufferIndex] = (char) read();
						if ((endTagBuffer[endTagBufferIndex] & 0xffdf) == endTagArray[endTagBufferIndex]) {
							endTagBufferIndex++;
						} else {
							endTagBufferIndex++;
							addCharToMiscBuffer('<');
							addCharToMiscBuffer('/');
							for (int j = 0; j < endTagBufferIndex; j++) {
								addCharToMiscBuffer(endTagBuffer[j]);
							}
							continue outer;
						}
					}
					while (Character.isWhitespace((char) (ch = read()))) {
						endTagBuffer[endTagBufferIndex++] = (char) ch;
					}
					if (ch == '>') {
						endTagBufferIndex += 3;
						unread(endTagBuffer, 0, endTagBufferIndex);
						return new String(miscBuffer, 0, miscIndex);
					} else {
						endTagBuffer[endTagBufferIndex++] = (char) ch;
						addCharToMiscBuffer('<');
						addCharToMiscBuffer('/');
						for (int i = 0; i < endTagBufferIndex; i++) {
							addCharToMiscBuffer(endTagBuffer[i]);
						}
					}
				} else if (ch == '!') {
					addCharToMiscBuffer('<');
					addCharToMiscBuffer((char) ch);
					ch = read();
					addCharToMiscBuffer((char) ch);
					if (ch == '-') {
						ch = read();
						addCharToMiscBuffer((char) ch);
						if (ch == '-') {
							inComment = true;
						}
					}

				} else {
					addCharToMiscBuffer('<');
					addCharToMiscBuffer((char) ch);
				}
			} else if (ch == EOF) {
				parser.error(IParserError.MISC_ERR,
						"Unexpected EOF in CDATA after " + end);
				endTagBufferIndex = 0;
				if (limitCandidate != 0) {
					unread(miscBuffer, limitCandidate, miscIndex
							- limitCandidate);
					return new String(miscBuffer, 0, limitCandidate);
				} else {
					return new String(miscBuffer, 0, miscIndex);
				}
			} else if (inComment && ch == '-') {
				addCharToMiscBuffer((char) ch);
				ch = read();
				addCharToMiscBuffer((char) ch);
				if (ch == '-') {
					ch = read();
					addCharToMiscBuffer((char) ch);
					if (ch == '>') {
						inComment = false;
					}
				}

			} else {
				// setCharToMiscBuffer(miscIndex, (char)ch);
				// miscIndex++;
				addCharToMiscBuffer((char) ch);
			}
		}
	}

	private int endTagBufferIndex = 0;

	void eatCDATAEndTag() throws IOException {
		for (int i = endTagBufferIndex; i > 0; i--)
			read();
	}

	private char attrValBuffer[] = new char[8192];

	private static boolean includeAngleBracket(char chars[], int begin, int end) {
		while (begin < end) {
			char ch = chars[begin++];
			if (ch == '>' || ch == '<')
				return true;
		}
		return false;
	}

	private void checkAttrValBuffer(int index) {
		int curSize = attrValBuffer.length;
		if (curSize < index + 128) {
			char tmpBuffer[] = new char[curSize + 8192];
			System.arraycopy(attrValBuffer, 0, tmpBuffer, 0, curSize);
			attrValBuffer = tmpBuffer;
		}
	}

	/**
	 * Reads attribute values. Nasty attribute value examples:
	 * <ul>
	 * <li>width=<em>&quot;100&quot;%</em>
	 * <li>width=<em>&quot;100%</em> height=...
	 * </ul>
	 */
	final String readAttributeValue(AttributeDefinition ad, ElementDefinition ed)
			throws IOException, ParseException, SAXException {
		int ch;
		while (Character.isWhitespace((char) (ch = read())))
			;
		miscIndex = 0;
		int attrValBufferIndex = 0;

		addCharToMiscBuffer((char) ch);

		if (ch == '\'' || ch == '"') {
			int endChar = ch;
			while ((ch = read()) != EOF && ch != endChar) {
				checkAttrValBuffer(attrValBufferIndex);

				setCharToMiscBuffer(miscIndex, (char) ch);
				miscIndex++;
				if (ch == '&' && (extractChar || extractNum)) {
					if (!extractNum) {
						if ((ch = read()) == '#') {
							attrValBuffer[attrValBufferIndex++] = '&';
							attrValBuffer[attrValBufferIndex++] = '#';
							continue;
						} else {
							unread(ch);
						}
					}
					int charEntity = rslvCharEnt();
					if (charEntity == -1) {
						attrValBuffer[attrValBufferIndex++] = '&';
					} else {
						attrValBuffer[attrValBufferIndex++] = (char) charEntity;

						for (int i = 0; i < entityBufIndex; i++) {
							addCharToMiscBuffer((char) entityBuf[i]);
						}
					}
				} else {
					try {
						attrValBuffer[attrValBufferIndex] = (char) ch;
					} catch (ArrayIndexOutOfBoundsException e) {
						char newAttrValBuffer[] = new char[attrValBuffer.length * 2];
						System.arraycopy(attrValBuffer, 0, newAttrValBuffer, 0,
								attrValBufferIndex);
						attrValBuffer = newAttrValBuffer;
					}
					attrValBufferIndex++;
				}
			}
			if (ch != EOF) {
				addCharToMiscBuffer((char) ch);
			} else {
				parser
						.error(IParserError.ATTR_VALUE,
								"EOF in attribute value.");
			}
			if (miscIndex > 1
					&& (includeAngleBracket(miscBuffer, 1, miscIndex - 1) || miscBuffer[miscIndex - 2] == '=')
					&& parser.handleError(IParserError.ATTR_VALUE, new String(
							miscBuffer, 0, miscIndex))) {
				return readAttributeValue(ad, ed);
			}
		} else if (ch == '>') {
			unread(ch);
			return "";
		} else {
			int ch1 = -1;
			if (!isUnquotedChar((char) ch) && (ch1 = sence()) == '"'
					|| ch1 == '\'' || Character.isWhitespace((char) ch1)) {
				return readAttributeValue(ad, ed);
			}
			attrValBuffer[attrValBufferIndex++] = (char) ch;
			while ((ch = read()) != EOF && ch != '>'
					&& !Character.isWhitespace((char) ch)) {

				checkAttrValBuffer(attrValBufferIndex);

				if (ch == '&' && (extractChar || extractNum)) {
					if (!extractNum) {
						if ((ch = read()) == '#') {
							attrValBuffer[attrValBufferIndex++] = '&';
							attrValBuffer[attrValBufferIndex++] = '#';
							continue;
						} else {
							unread(ch);
						}
					}
					int charEntity = rslvCharEnt();
					if (charEntity == -1) {
						attrValBuffer[attrValBufferIndex++] = '&';
					} else {
						attrValBuffer[attrValBufferIndex++] = (char) charEntity;
					}
				} else {
					attrValBuffer[attrValBufferIndex++] = (char) ch;
				}
			}
			if (ch == '>')
				unread(ch);
		}
		ch = read();
		String ret = new String(attrValBuffer, 0, attrValBufferIndex);
		if (ch == '"' || ch == '\'') {
			if (!parser.handleError(IParserError.BEFORE_ATTRNAME, "\"")) {
				parser.error(IParserError.STARTTAG_SYNTAX_ERR,
						"requires attribute name after \"" + ret + '"');
			}
		} else {
			unread(ch);
		}
		return ret;
	}

	private int sence(int forward) throws IOException {
		switch (forward) {
		case 0:
			int ch = read();
			unread(ch);
			return ch;
		case 1:
			int ch0 = read();
			ch = read();
			unread(ch);
			unread(ch0);
			return ch;
		default:
			int buf[] = new int[forward];
			for (int i = 0; i < forward; i++) {
				buf[i] = read();
			}
			for (int i = forward - 1; i >= 0; i--) {
				unread(buf[i]);
			}
			return buf[forward - 1];
		}
	}

	private int sence() throws IOException {
		int ch = read();
		unread(ch);
		return ch;
	}

	private boolean isUnquotedChar(char ch) {
		return Character.isLowerCase(ch) || Character.isUpperCase(ch)
				|| Character.isDigit(ch) || ch == '-' || ch == '.';
	}

	private int etag() throws ParseException, IOException {
		int ch;
		// while (Character
		// .isWhitespace(miscBuffer[miscIndex++] = (char) (ch = read())));
		while (Character.isWhitespace((char) (ch = read()))) {
			addCharToMiscBuffer((char) ch);
		}
		;
		addCharToMiscBuffer((char) ch);

		if (ch == '>') {
			state = DEFAULT;
			sval = ">";
			parser.setCharacter(miscBuffer, etagStartIndex, miscIndex
					- etagStartIndex);
			return (ttype = '>');
		} else if (ch == EOF) {
			state = DEFAULT;
			sval = "";
			return (ttype = EOF);
		} else if (Character.isLowerCase((char) ch)
				|| Character.isUpperCase((char) ch)) {
			int startIndex = miscIndex - 1;
			do {
				setCharToMiscBuffer(miscIndex, (char) (ch = read()));
				miscIndex++;
			} while (Character.isLowerCase((char) ch)
					|| Character.isUpperCase((char) ch)
					|| Character.isDigit((char) ch) || ch == '.' || ch == '-'
					|| ch == ':' || ch == '#');
			unread(ch);
			miscIndex--;
			sval = new String(miscBuffer, startIndex, miscIndex - startIndex);
			return (ttype = NAME_CHAR);
		} else {
			return etag();
		}
	}

	private int tag() throws ParseException, IOException {
		int ch;
		miscIndex = 0;
		while (Character.isWhitespace((char) (ch = read())))
			;

		if (ch == '!') {
			System.out.println("comment between DOCTYPE and HTML !!!");
			return eatComment();
		}

		if (ch == '>') {
			state = DEFAULT;
			sval = ">";
			return (ttype = '>');
		} else if (ch == EOF) {
			state = DEFAULT;
			sval = "";
			return (ttype = EOF);
		} else if (Character.isLowerCase((char) ch)
				|| Character.isUpperCase((char) ch)) {

			do {
				setCharToMiscBuffer(miscIndex, (char) ch);
				miscIndex++;
				ch = read();
			} while (Character.isLowerCase((char) ch)
					|| Character.isUpperCase((char) ch)
					|| Character.isDigit((char) ch) || ch == '.' || ch == '-'
					|| ch == ':' || ch == '#');
			unread(ch);
			sval = new String(miscBuffer, 0, miscIndex);
			return (ttype = NAME_CHAR);
		} else if (Character.isDigit((char) ch)) {
			do {
				addCharToMiscBuffer((char) ch);
				ch = read();
			} while (Character.isDigit((char) ch));
			if (ch == '%') {
				addCharToMiscBuffer((char) ch);
			} else {
				unread(ch);
			}
			sval = new String(miscBuffer, 0, miscIndex);
			return (ttype = NUM);
		} else if (ch == '<') {
			ch = read();
			if (ch == '/') {
				sval = "</";
				miscBuffer[0] = '<';
				miscBuffer[1] = '/';
				etagStartIndex = 0;
				state = ETAGO;
				return (ttype = ETAGO);
			} else {
				unread(ch);
				sval = "<";
				return (ttype = '<');
			}
		} else {
			return (ttype = ch);
		}
	}

	final void consumeUntil(char limit) throws IOException {
		while (limit != read())
			;
	}

	private boolean prsvWS = false;

	void setPreserveWhitespace(boolean preserv) {
		this.prsvWS = preserv;
	}

	private int index = 0;

	private char charBuffer[] = new char[INITIAL_BUF_SIZ];

	private void expandBuffer(int increase) {
		if (bufLimit != index) {
			increase += index < bufLimit ? (bufLimit + index)
					: (charBuffer.length - index + bufLimit);
		}
		int length = charBuffer.length * 2;
		while (length < increase)
			length *= 2;
		char newBuffer[] = new char[length];
		if (index < bufLimit) {
			System.arraycopy(charBuffer, index, newBuffer, index, bufLimit
					- index);
		} else if (index != bufLimit) {
			System.arraycopy(charBuffer, 0, newBuffer, 0, bufLimit);
			System.arraycopy(charBuffer, index, newBuffer, length
					- charBuffer.length + index, charBuffer.length - index);
			index = length - charBuffer.length + index;
		}
		charBuffer = newBuffer;
	}

	public final static int INITIAL_BUF_SIZ = 8192;

	public final static int READ_UNIT = 1024;

	private int bufLimit = 0;

	private char miscBuffer[] = new char[INITIAL_BUF_SIZ];

	private int miscIndex = 0;

	private void expandMiscBuffer() {
		char newBuffer[];
		if (miscBuffer.length < 32768) {
			newBuffer = new char[miscBuffer.length * 2];
		} else {
			newBuffer = new char[miscBuffer.length + 32768];
		}

		System.arraycopy(miscBuffer, 0, newBuffer, 0, miscIndex);
		miscBuffer = newBuffer;
	}

	private void storeUntil(char limit) throws IOException {
		int ch = read();
		while (ch != limit && ch != EOF) {
			setCharToMiscBuffer(miscIndex, (char) ch);
			miscIndex++;
			ch = read();
		}
	}

	final String eatUntil(char limit) throws IOException {
		miscIndex = 0;
		int ch = read();
		while (ch != limit && ch != EOF) {
			setCharToMiscBuffer(miscIndex, (char) ch);
			miscIndex++;
			ch = read();
		}
		return new String(miscBuffer, 0, miscIndex);
	}

	private boolean skipLF;

	private boolean fillBuffer() throws IOException {
		if (bufLimit == charBuffer.length) {
			index = 0;
			bufLimit = reader.read(charBuffer, 0, READ_UNIT);
			if (bufLimit == -1)
				return false;
		} else if (bufLimit + READ_UNIT < charBuffer.length) {
			int count = reader.read(charBuffer, bufLimit, READ_UNIT);
			if (count == -1)
				return false;
			bufLimit += count;
		} else {
			int count = reader.read(charBuffer, bufLimit, charBuffer.length
					- bufLimit);
			if (count == -1)
				return false;
			bufLimit += count;
		}
		return true;
	}

	// private void printCharBuffer() {
	// if (bufLimit < index) {
	// System.err.print(
	// new String(charBuffer, index, charBuffer.length - index));
	// System.err.println(new String(charBuffer, 0, bufLimit));
	// } else if (bufLimit == index) {
	// System.err.println("Empty");
	// } else {
	// System.err.println(new String(charBuffer, index, bufLimit - index));
	// }
	// }

	private int read() throws IOException {
		if (bufLimit == index) {
			if (!fillBuffer())
				return -1;
		} else if (index == charBuffer.length) {
			index = 0;
		}
		int ret = charBuffer[index++];
		if (skipLF) {
			if (ret == '\n') {
				if (bufLimit == index) {
					if (!fillBuffer())
						return -1;
				} else if (index == charBuffer.length) {
					index = 0;
				}
				lastLFindex++;
				ret = charBuffer[index++];
			}
			skipLF = false;
		}
		switch (ret) {
		case '\r':
			skipLF = true;
		case '\n':
			lineNumber++;
			lastLFindex = index - 1;
			return '\n';
		}
		return ret;
	}

	private int lastLFindex = -1;

	private int lineNumber = 0;

	private void unread(int ch) throws IOException {
		if (ch == '\n')
			lineNumber--;
		if (index == 0) {
			charBuffer[index = charBuffer.length - 1] = (char) ch;
		} else {
			charBuffer[--index] = (char) ch;
		}
	}

	void unread(String str) throws IOException {
		unread(str.toCharArray());
	}

	private void unread(char str[], int begin, int len) {
		if (bufLimit >= index) {
			if (charBuffer.length - (bufLimit - index) < len) {
				expandBuffer(len);
			}
		} else if (index - bufLimit < len) {
			expandBuffer(len);
		}
		if (index < len) {
			int i = begin + len - 1;
			int j;
			for (j = index - 1; j >= 0; j--) {
				if ((charBuffer[j] = str[i--]) == '\n')
					lineNumber--;
			}
			j = charBuffer.length - 1;
			for (; i >= begin; j--) {
				if ((charBuffer[j] = str[i--]) == '\n')
					lineNumber--;
			}
			index = j + 1;
		} else {
			for (int i = begin + len - 1; i >= begin; i--) {
				if ((charBuffer[--index] = str[i]) == '\n')
					lineNumber--;
			}
		}
	}

	private void unread(char str[]) {
		unread(str, 0, str.length);
	}

	final int nextToken() throws IOException, ParseException, SAXException {
		if (pushBacked) {
			pushBacked = false;
			return ttype;
		}

		switch (state) {
		case DEFAULT:
			return defaultState();
		case TAG:
			return tag();
		case ETAG:
			return etag();
		default:
			return (ttype = EOF);
		}
	}

	final void pushBack() {
		pushBacked = true;
	}

	final int getCurrentLine() {
		return lineNumber + 1;
	}

	final int getCurrentCol() {
		return getColumnNumber();
	}

	final void switchTo(int state) {
		this.state = state;
	}

	final void close() throws IOException {
		this.reader.close();
	}

	public String getPublicId() {
		return null;
	}

	public String getSystemId() {
		return null;
	}

	public int getLineNumber() {
		return lineNumber + 1;
	}

	public int getColumnNumber() {
		int ret;
		if (index >= lastLFindex) {
			ret = index - lastLFindex;
		} else {
			ret = charBuffer.length - lastLFindex + index;
		}
		return ret;
	}
}
