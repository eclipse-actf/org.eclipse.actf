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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;

import org.eclipse.actf.model.dom.html.ParseException;
import org.eclipse.actf.model.internal.dom.sgml.ISGMLConstants;

/**
 * Tokenizer interface
 */
abstract class SGMLTokenizer implements ISGMLConstants {
	protected LineNumberReader reader;

	public BufferedReader getReader() {
		return this.reader;
	}

	/**
	 * token value.
	 */
	public String sval;

	/**
	 * token type.
	 */
	public int ttype;

	protected int state = DEFAULT;

	protected boolean pushBacked;

	/**
	 * Constructs SGMLTokenizer instance.
	 * 
	 * @param reader
	 *            reader that read SGML Documents.
	 */
	public SGMLTokenizer(Reader reader) {
		this(reader, DEFAULT);
	}

	SGMLTokenizer(Reader reader, int state) {
		if (reader instanceof LineNumberReader) {
			this.reader = (LineNumberReader) reader;
		} else {
			this.reader = new LineNumberReader(reader);
		}
		this.state = state;
		pushBacked = false;
	}

	abstract protected int defaultState() throws IOException, ParseException;

	/**
	 * Gets Current Line Number.
	 * 
	 * @return line number.
	 */
	public final int getCurrentLine() {
		return reader.getLineNumber() + 1;
	}

	/**
	 * Get a token from a SGML document and return a type of the token.
	 * 
	 * @return type of a read token.
	 * @exception IOException
	 *                If an I/O error occurs
	 * @exception ParseException
	 *                If an Unexpected EOF occurs
	 */
	public final int nextToken() throws IOException, ParseException {
		if (pushBacked) {
			pushBacked = false;
			return ttype;
		}
		switch (state) {
		case DEFAULT:
			return defaultState();
		case TAG:
			return tag();
		default:
			return -1;
		}
	}

	/**
	 * Push back tokens. Next call of nextToken() returns current ttype and sval
	 * will not change.
	 */
	public final void pushBack() {
		pushBacked = true;
	}

	/**
	 * Character Ring Buffer for unread;
	 */
	protected int charBuffer[] = new int[BUF_SIZ];

	public final static int BUF_SIZ = 1024;

	protected int index = 0;

	protected final int read() throws IOException {
		if (index > 0) {
			return charBuffer[--index];
		} else {
			return reader.read();
		}
	}

	protected final void unread(int ch) {
		charBuffer[index++] = ch;
	}

	final void switchTo(int state) {
		this.state = state;
	}

	abstract protected int tag() throws ParseException, IOException;

	final void close() throws IOException {
		this.reader.close();
	}
}
