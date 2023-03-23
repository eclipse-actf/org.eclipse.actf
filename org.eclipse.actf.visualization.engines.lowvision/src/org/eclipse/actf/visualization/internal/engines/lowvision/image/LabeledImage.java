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

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Stack;
import java.util.Vector;

import org.eclipse.actf.visualization.engines.lowvision.image.ImageException;
import org.eclipse.actf.visualization.internal.engines.lowvision.color.ColorUtil;

/*
 * Labeled image obtained by connected component detection.
 * 
 * use (width, height, label) or (numComponents, components)
 * 
 * label bg-> 0 fg (connected components) > 1 (Integer)
 * 
 */
public class LabeledImage {
	public static final short METHOD_8_CONNECTIVITY = 0;

	public static final short METHOD_4_CONNECTIVITY = 1;

	int width;

	int height;

	int[][] label;

	int numComponents; // numComponents == components.length

	ConnectedComponent[] components;

	short method;

	int left;

	int right;

	int top;

	int bottom;

	int count;

	public LabeledImage(BinaryImage _bi) {
		this(_bi, METHOD_8_CONNECTIVITY);
	}

	public LabeledImage(BinaryImage _bi, short _method) {
		width = _bi.width;
		height = _bi.height;
		label = new int[height][width];
		method = _method;

		Vector<ConnectedComponent> ccVector = new Vector<ConnectedComponent>();
		int currentLabel = 1;
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				// same color & not labeled
				if (_bi.data[j][i] > 0 && label[j][i] == 0) {
					label[j][i] = currentLabel;
					count = 1;
					labelNeighbor(_bi, i, j, currentLabel, _method);

					ConnectedComponent currentCC = new ConnectedComponent(left,
							top, right - left + 1, bottom - top + 1, count);
					for (int j2 = top; j2 <= bottom; j2++) {
						for (int i2 = left; i2 <= right; i2++) {
							if (label[j2][i2] == currentLabel) {
								currentCC.shape.data[j2 - top][i2 - left] = 1;
							}
						}
					}
					ccVector.addElement(currentCC);

					currentLabel++;
				}
			}
		}

		numComponents = currentLabel - 1;
		components = new ConnectedComponent[numComponents];
		short conn = ConnectedComponent.CONNECTIVITY_UNSET;
		if (method == METHOD_8_CONNECTIVITY)
			conn = ConnectedComponent.CONNECTIVITY_8;
		else if (method == METHOD_4_CONNECTIVITY)
			conn = ConnectedComponent.CONNECTIVITY_4;
		for (int i = 0; i < numComponents; i++) {
			components[i] = ccVector.elementAt(i);
			components[i].connectivity = conn;
		}
	}

	/*
	 * search connected components for pixel(_i, _j)
	 */
	private void labelNeighbor(BinaryImage _bi, int _i, int _j, int _label,
			short _method) {
		left = _i;
		right = _i;
		top = _j;
		bottom = _j;

		Stack<Coord> labelingStack = new Stack<Coord>();
		Coord startCoord = new Coord(_i, _j);
		labelingStack.push(startCoord);

		while (!labelingStack.empty()) {
			Coord co = labelingStack.pop();
			int i = co.x;
			int j = co.y;

			// right
			if (i < width - 1 && _bi.data[j][i + 1] > 0 && label[j][i + 1] == 0) {
				label[j][i + 1] = _label;
				count++;
				if (right < i + 1)
					right = i + 1;
				Coord co2 = new Coord(i + 1, j);
				labelingStack.push(co2);
			}
			// left
			if (i > 0 && _bi.data[j][i - 1] > 0 && label[j][i - 1] == 0) {
				label[j][i - 1] = _label;
				count++;
				if (left > i - 1)
					left = i - 1;
				Coord co2 = new Coord(i - 1, j);
				labelingStack.push(co2);
			}
			// upper
			if (j > 0 && _bi.data[j - 1][i] > 0 && label[j - 1][i] == 0) {
				label[j - 1][i] = _label;
				count++;
				if (top > j - 1)
					top = j - 1;
				Coord co2 = new Coord(i, j - 1);
				labelingStack.push(co2);
			}
			// bottom
			if (j < height - 1 && _bi.data[j + 1][i] > 0
					&& label[j + 1][i] == 0) {
				label[j + 1][i] = _label;
				count++;
				if (bottom < j + 1)
					bottom = j + 1;
				Coord co2 = new Coord(i, j + 1);
				labelingStack.push(co2);
			}
			if (_method == METHOD_8_CONNECTIVITY) {
				// upper left
				if (j > 0 && i > 0 && _bi.data[j - 1][i - 1] > 0
						&& label[j - 1][i - 1] == 0) {
					label[j - 1][i - 1] = _label;
					count++;
					if (left > i - 1)
						left = i - 1;
					if (top > j - 1)
						top = j - 1;
					Coord co2 = new Coord(i - 1, j - 1);
					labelingStack.push(co2);
				}
				// upper right
				if (j > 0 && i < width - 1 && _bi.data[j - 1][i + 1] > 0
						&& label[j - 1][i + 1] == 0) {
					label[j - 1][i + 1] = _label;
					count++;
					if (right < i + 1)
						right = i + 1;
					if (top > j - 1)
						top = j - 1;
					Coord co2 = new Coord(i + 1, j - 1);
					labelingStack.push(co2);
				}
				// bottom left
				if (j < height - 1 && i > 0 && _bi.data[j + 1][i - 1] > 0
						&& label[j + 1][i - 1] == 0) {
					label[j + 1][i - 1] = _label;
					count++;
					if (left > i - 1)
						left = i - 1;
					if (bottom < j + 1)
						bottom = j + 1;
					Coord co2 = new Coord(i - 1, j + 1);
					labelingStack.push(co2);
				}
				// bottom right
				if (j < height - 1 && i < width - 1
						&& _bi.data[j + 1][i + 1] > 0
						&& label[j + 1][i + 1] == 0) {
					label[j + 1][i + 1] = _label;
					count++;
					if (right < i + 1)
						right = i + 1;
					if (bottom < j + 1)
						bottom = j + 1;
					Coord co2 = new Coord(i + 1, j + 1);
					labelingStack.push(co2);
				}
			}
		}
	}

	public int getWidth() {
		return (width);
	}

	public int getHeight() {
		return (height);
	}

	public int[][] getLabel() {
		return (label);
	}

	public int getNumComponents() {
		return (numComponents);
	}

	public ConnectedComponent[] getComponents() {
		return (components);
	}

	public short getMethod() {
		return (method);
	}

	public BinaryImage toBinaryImage() {
		BinaryImage bi = new BinaryImage(width, height);
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				if (label[j][i] != 0)
					bi.data[j][i] = 1;
			}
		}

		return (bi);
	}

	/*
	 * assign color to each label and generate bufferd image
	 */
	public BufferedImage toBufferedImage() throws ImageException {
		if ((numComponents >> 24) != 0) {
			throw new ImageException("Too many labels to make bufferedImage."); //$NON-NLS-1$
		}

		BufferedImage destImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		WritableRaster destRaster = destImage.copyData(null);
		DataBufferInt destBufInt = (DataBufferInt) (destRaster.getDataBuffer());
		int[] destArray = destBufInt.getData();

		// int pixel = 0;
		int k = 0;
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				destArray[k] = ColorUtil.distinguishableColor(label[j][i]);
				k++;
			}
		}

		destImage.setData(destRaster);
		return (destImage);
	}

	public void dump(PrintStream _ps, boolean _printLabel) {
		PrintWriter pw = new PrintWriter(_ps, true);
		dump(pw, _printLabel);
	}

	public void dump(PrintWriter _pw, boolean _printLabel) {
		if (_printLabel)
			dumpLabel(_pw);
		else
			dumpComponents(_pw);
	}

	public void dumpLabel(PrintStream _ps) {
		PrintWriter pw = new PrintWriter(_ps, true);
		dumpLabel(pw);
	}

	@SuppressWarnings("nls")
	public void dumpLabel(PrintWriter _pw) {
		_pw.println("-------------------------------");
		_pw.println("Dumping a labeledImage");
		_pw.println("Width = " + width + ", Height = " + height);
		for (int j = 0; j < height; j++) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < width; i++) {
				if (label[j][i] == 0)
					sb.append(".");
				else
					sb.append("" + (label[j][i] % 10));
			}
			_pw.println(sb.toString());
		}
		_pw.println("-------------------------------");
	}

	public void dumpComponents(PrintStream _ps) {
		PrintWriter pw = new PrintWriter(_ps, true);
		dumpComponents(pw);
	}

	@SuppressWarnings("nls")
	public void dumpComponents(PrintWriter _pw) {
		_pw.println("-------------------------------");
		_pw.println("Dumping components");
		_pw.println("# of components = " + numComponents);
		for (int i = 0; i < numComponents; i++) {
			dumpComponents(_pw, i);
		}
		_pw.println("-------------------------------");
	}

	public void dumpComponents(PrintStream _ps, int _i) {
		PrintWriter pw = new PrintWriter(_ps, true);
		dumpComponents(pw, _i);
	}

	@SuppressWarnings("nls")
	public void dumpComponents(PrintWriter _pw, int _i) {
		_pw.println("-----");
		_pw.println("Components # = " + _i);
		components[_i].dump(_pw);
	}
}
