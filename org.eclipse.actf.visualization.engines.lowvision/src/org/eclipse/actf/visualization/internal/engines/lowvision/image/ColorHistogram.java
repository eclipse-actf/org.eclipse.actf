/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Junji MAEDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.engines.lowvision.image;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class ColorHistogram {
	private HashMap<Integer, ColorHistogramBin> pixelMap = new HashMap<Integer, ColorHistogramBin>();

	private ColorHistogramBin[] sortedArrayByOccurrence = null;

	// shows histogram was changed or not after make sortedArrayByOccurrence
	private boolean changedFlag = true;

	public boolean changed() {
		return (changedFlag);
	}

	public ColorHistogramBin[] getSortedArrayByOccurrence() {
		return (sortedArrayByOccurrence);
	}

	public int getSize() {
		return (pixelMap.size());
	}

	public void put(int _color) {
		changedFlag = true;
		Integer curPixel = new Integer(_color);
		ColorHistogramBin curHistBin = pixelMap.get(curPixel);
		if (curHistBin != null) {
			curHistBin.occurrence++;
		} else {
			curHistBin = new ColorHistogramBin(curPixel.intValue());
			curHistBin.occurrence++;
			pixelMap.put(curPixel, curHistBin);
		}
	}

	public void makeSortedArrayByOccurrence() {
		if (!changedFlag) {
			return;
		}
		if (getSize() == 0) {
			return;
		}
		changedFlag = false;
		Object[] keyArray = pixelMap.keySet().toArray();
		int len = keyArray.length;
		sortedArrayByOccurrence = new ColorHistogramBin[len];

		for (int i = 0; i < len; i++) {
			sortedArrayByOccurrence[i] = pixelMap.get(keyArray[i]);
		}
		Arrays.sort(sortedArrayByOccurrence, new ComparatorByOccurrence());
	}

	public class ComparatorByOccurrence implements
			Comparator<ColorHistogramBin> {
		public int compare(ColorHistogramBin o1, ColorHistogramBin o2) {
			return (o2.occurrence - o1.occurrence);
		}
	}

	public static ColorHistogram makeColorHistogram(int[][] _pixel, int _width,
			int _height) {
		ColorHistogram histo = new ColorHistogram();
		for (int j = 0; j < _height; j++) {
			for (int i = 0; i < _width; i++) {
				histo.put(_pixel[j][i]);
			}
		}
		histo.makeSortedArrayByOccurrence();
		return (histo);
	}

	public static ColorHistogram makeColorHistogram(IInt2D _i2d) {
		return (makeColorHistogram(_i2d.getData(), _i2d.getWidth(), _i2d
				.getHeight()));
	}

	public void dump(PrintStream _ps) {
		PrintWriter pw = new PrintWriter(_ps, true);
		dump(pw);
	}

	@SuppressWarnings("nls")
	public void dump(PrintWriter _pw) {
		if (changedFlag)
			makeSortedArrayByOccurrence();
		int len = sortedArrayByOccurrence.length;
		for (int i = 0; i < len; i++) {
			ColorHistogramBin cur = sortedArrayByOccurrence[i];
			int curOccur = cur.occurrence;
			int r = cur.getR();
			int g = cur.getG();
			int b = cur.getB();
			_pw.println(i + ": rgb = ( " + r + ", " + g + ", " + b
					+ ") occurrence = " + curOccur);
		}
	}

}
