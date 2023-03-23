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

import java.io.IOException;
import java.io.InputStream;

public class RereadableInputStream extends InputStream {
	private InputStream in;

	private byte buf[] = new byte[10000];

	private int bufSiz = 0, index = 0;

	private boolean buffering = true, normal = false;

	public RereadableInputStream(InputStream in) {
		this.in = in;
	}

	public void close() throws IOException {
		if (normal) {
			in.close();
		}

	}

	public int read() throws IOException {
		if (buffering) {
			int ret = in.read();
			if (bufSiz == buf.length) {
				buffering = false;
				normal = true;
			} else {
				buf[bufSiz++] = (byte) ret;
			}
			return ret;
		} else if (normal) {
			return in.read();
		} else {
			int ret = buf[index++];
			if (index == bufSiz) {
				normal = true;
				buf = null;
			}
			return ret;
		}
	}

	public int read(byte b[]) throws IOException {
		return read(b, 0, b.length);
	}

	public int read(byte b[], int off, int len) throws IOException {
		if (buffering) {
			int ret = in.read(b, off, len);
			if (bufSiz + ret < buf.length) {
				for (int i = 0; i < ret; i++)
					buf[bufSiz + i] = b[off + i];
				bufSiz += ret;
			} else {
				buffering = false;
				normal = true;
			}
			return ret;
		} else if (normal) {
			return in.read(b, off, len);
		} else {
			int ret;
			for (ret = 0; ret < b.length && index < bufSiz; ret++, index++) {
				b[ret] = buf[index];
			}
			if (index == bufSiz) {
				normal = true;
				buf = null;
			}
			return ret;
		}
	}

	public void reset() throws IOException {
		if (buffering == false) {
			throw new IOException("Cannot reset " + this); //$NON-NLS-1$
		}
		buffering = false;
	}
}
