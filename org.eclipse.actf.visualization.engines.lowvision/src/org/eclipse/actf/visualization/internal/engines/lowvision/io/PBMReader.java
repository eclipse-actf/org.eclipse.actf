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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.actf.visualization.engines.lowvision.LowVisionIOException;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.BinaryImage;

public class PBMReader {
	public static BinaryImage readBinaryImage(String _fileName) throws LowVisionIOException {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(_fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new LowVisionIOException("The file was not found: " + _fileName); //$NON-NLS-1$
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));

		try {
			char magic1 = (char) (br.read());
			char magic2 = (char) (br.read());
			// support "P1" (not "P4")
			if (magic1 != 'P' || magic2 != '1') {
				br.close();
				throw new LowVisionIOException("Bad magic number: " + magic1 + magic2 //$NON-NLS-1$
						+ "\n\"P4\" cannot be treated in this version.\n(Only \"P1\" is accepted.)"); //$NON-NLS-1$
			}
		} catch (IOException e) {
			throw new LowVisionIOException("IOException occurred while reading the magic number."); //$NON-NLS-1$
		}

		StringBuffer sb = new StringBuffer();
		try {
			// remove comments, etc.
			String oneLine = null;
			while ((oneLine = br.readLine()) != null) {
				int index = oneLine.indexOf("#"); //$NON-NLS-1$
				if (index == -1)
					sb.append(oneLine + "\n"); //$NON-NLS-1$
				else
					sb.append(oneLine.substring(0, index) + "\n"); //$NON-NLS-1$
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new LowVisionIOException("IO error occurred when reading."); //$NON-NLS-1$
		}

		String content = sb.toString();
		if (!Character.isWhitespace(content.charAt(0))) {
			throw new LowVisionIOException("The magic number must be followed by a white space."); //$NON-NLS-1$
		}
		content = content.substring(1);

		Pattern pat = Pattern.compile("(\\d+)\\s(\\d+)\\s([01\\s]+)"); //$NON-NLS-1$
		Matcher mat = pat.matcher(content);
		if (!(mat.find())) {
			throw new LowVisionIOException("Invalid data format (1)."); //$NON-NLS-1$
		}
		int count = mat.groupCount();
		if (count != 3) {
			throw new LowVisionIOException("Invalid data format (2). count = " + count); //$NON-NLS-1$
		}
		int width = Integer.parseInt(mat.group(1));
		int height = Integer.parseInt(mat.group(2));
		String dataStr = mat.group(3);
		Pattern pat2 = Pattern.compile("\\s+"); //$NON-NLS-1$
		Matcher mat2 = pat2.matcher(dataStr);
		String data = mat2.replaceAll(""); //$NON-NLS-1$

		if (data.length() != width * height) {
			throw new LowVisionIOException("Data size does not equal to width*height"); //$NON-NLS-1$
		}

		BinaryImage bi = new BinaryImage(width, height);
		byte[][] bidata = bi.getData();
		int k = 0;
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				if (data.charAt(k) == '1')
					bidata[j][i] = 1;
				k++;
			}
		}

		return (bi);
	}
}
