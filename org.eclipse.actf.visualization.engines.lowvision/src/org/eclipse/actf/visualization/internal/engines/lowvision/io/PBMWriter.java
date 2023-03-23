/*******************************************************************************
 * Copyright (c) 2003, 2016 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Junji MAEDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.engines.lowvision.io;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import org.eclipse.actf.visualization.engines.lowvision.LowVisionIOException;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.BinaryImage;

/*
 * write PBM file ("P1")
 */
public class PBMWriter {
	private static final int LINE_LENGTH = 64;

	public static void writeBinaryImage(BinaryImage _bi, String _fileName)
			throws LowVisionIOException {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(_fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new LowVisionIOException("The file was not found: " //$NON-NLS-1$
					+ _fileName);
		}
		PrintWriter pw = new PrintWriter(fos, true);

		int width = _bi.getWidth();
		int height = _bi.getHeight();

		pw.println("P1"); //$NON-NLS-1$
		pw.print("" + width + " " + height); //$NON-NLS-1$ //$NON-NLS-2$
		int lineLength = LINE_LENGTH;
		if (width < lineLength)
			lineLength = width;
		StringBuffer sb = new StringBuffer(""); //$NON-NLS-1$
		byte[][] bidata = _bi.getData();
		int k = 0;
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				if (k % lineLength == 0) {
					pw.println(sb.toString());
					sb = new StringBuffer();
				}
				k++;
				if (bidata[j][i] == 0)
					sb.append("0"); //$NON-NLS-1$
				else
					sb.append("1"); //$NON-NLS-1$
			}
		}
		pw.println(sb.toString());
		pw.close();
	}
}
