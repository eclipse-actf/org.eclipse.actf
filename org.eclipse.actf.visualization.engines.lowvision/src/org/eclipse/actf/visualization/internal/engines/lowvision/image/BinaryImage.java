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
import java.util.Vector;

import org.eclipse.actf.visualization.engines.lowvision.image.ImageException;

/*
 * Binary Image data[y][x] ([0][0] to [height-1][width-1])
 */
public class BinaryImage {

	public static final short METHOD_SPECIFY_FOREGROUND = 0; // fgcolor

	public static final short METHOD_SPECIFY_BACKGROUND = 1; // bgcolor

	public static final int NOT_MEASURED = -1;
	
	int width;

	int height;

	public byte[][] data;

	private int area = NOT_MEASURED;

	public BinaryImage(int _w, int _h) {
		width = _w;
		height = _h;
		data = new byte[height][width];
	}

	public BinaryImage(int _width, int _height, int[][] _data, short _method,
			int _i) throws ImageException {
		this((new Int2D(_width, _height, _data)), _method, _i);
	}

	public BinaryImage(IInt2D _i2d, short _method, int _i) {
		width = _i2d.getWidth();
		height = _i2d.getHeight();
		data = new byte[height][width];

		if (_method == METHOD_SPECIFY_FOREGROUND) {
			setDataSpecifyForeground(_i2d, _i);
		}
		if (_method == METHOD_SPECIFY_BACKGROUND) {
			setDataSpecifyBackground(_i2d, _i);
		}
	}

	private void setDataSpecifyForeground(IInt2D _i2d, int _foreground) {
		area = 0;
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				if (_i2d.getData()[j][i] == _foreground) {
					data[j][i] = 1;
					area++;
				} else
					data[j][i] = 0;
			}
		}
	}

	private void setDataSpecifyBackground(IInt2D _i2d, int _background) {
		area = 0;
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				if (_i2d.getData()[j][i] == _background)
					data[j][i] = 0;
				else {
					data[j][i] = 1;
					area++;
				}
			}
		}
	}

	public BinaryImage(BufferedImage _bi, short _method, int _i) {
		width = _bi.getWidth();
		height = _bi.getHeight();
		data = new byte[height][width];

		if (_method == METHOD_SPECIFY_FOREGROUND) {
			setDataSpecifyForeground(_bi, _i);
		}
		if (_method == METHOD_SPECIFY_BACKGROUND) {
			setDataSpecifyBackground(_bi, _i);
		}
	}

	private void setDataSpecifyForeground(BufferedImage _bi, int _foreground) {
		WritableRaster srcRaster = _bi.copyData(null);
		DataBufferInt srcBufInt = (DataBufferInt) (srcRaster.getDataBuffer());
		int[] srcArray = srcBufInt.getData();

		area = 0;
		int k = 0;
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				if (srcArray[k] == _foreground) {
					data[j][i] = 1;
					area++;
				} else
					data[j][i] = 0;
				k++;
			}
		}
	}

	private void setDataSpecifyBackground(BufferedImage _bi, int _background) {
		WritableRaster srcRaster = _bi.copyData(null);
		DataBufferInt srcBufInt = (DataBufferInt) (srcRaster.getDataBuffer());
		int[] srcArray = srcBufInt.getData();

		area = 0;
		int k = 0;
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				if (srcArray[k] == _background)
					data[j][i] = 0;
				else {
					data[j][i] = 1;
					area++;
				}
				k++;
			}
		}
	}

	/*
	 * create (daub) image by using vertex set -vertex set must be ordered in
	 * clockwise -assume convex shape (to obtain convex closure)
	 */
	public BinaryImage(int _w, int _h, Coord[] _vertex) {
		this(_w, _h);

		Coord[] vertex = null;
		int numVertex = _vertex.length;
		if (_vertex[0].equals(_vertex[numVertex - 1])) {
			vertex = _vertex;
		} else {
			vertex = new Coord[numVertex + 1];
			for (int i = 0; i < numVertex; i++) {
				vertex[i] = _vertex[i];
			}
			vertex[numVertex] = _vertex[0];
			numVertex++;
		}

		Coord curPoint = new Coord(-1, -1);
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				data[j][i] = 1;
				curPoint.set(i, j);
				for (int k = 0; k < numVertex - 1; k++) {
					if (Coord
							.isLeftToVector(vertex[k], vertex[k + 1], curPoint)) {
						data[j][i] = 0;
						break;
					}
				}
			}
		}
		measureArea();
	}

	public BinaryImage deepCopy() {
		BinaryImage bi = new BinaryImage(width, height);
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				bi.data[j][i] = this.data[j][i];
			}
		}
		bi.area = this.area;
		return (bi);
	}

	/*
	 * copy into new BinaryImage (0,0)-> (_offsetX,_offsetY)
	 * 
	 * protrusion -strict -> error -not strict -> clip
	 */
	@SuppressWarnings("nls")
	public BinaryImage offsetCopy(int _newWidth, int _newHeight, int _offsetX,
			int _offsetY, boolean _strict) throws ImageException {
		BinaryImage bi = new BinaryImage(_newWidth, _newHeight);
		if (_offsetX < 0 || _newWidth <= _offsetX) {
			throw new ImageException("Bad offset value.");
		}
		if (_offsetY < 0 || _newHeight <= _offsetY) {
			throw new ImageException("Bad offset value.");
		}
		int endI = width;
		int endJ = height;
		int overWidth = _offsetX + width - _newWidth;
		int overHeight = _offsetY + height - _newHeight;
		if (overWidth > 0) {
			if (_strict) {
				throw new ImageException("Not enough image area.");
			} else {
				endI -= overWidth;
			}
		}
		if (overHeight > 0) {
			if (_strict) {
				throw new ImageException("Not enough image area.");
			} else {
				endJ -= overHeight;
			}
		}
		for (int j = 0; j < endJ; j++) {
			for (int i = 0; i < endI; i++) {
				bi.data[j + _offsetY][i + _offsetX] = this.data[j][i];
			}
		}

		return (bi);
	}

	public BinaryImage offsetCopy(int _newWidth, int _newHeight, int _offsetX,
			int _offsetY) throws ImageException {
		return (offsetCopy(_newWidth, _newHeight, _offsetX, _offsetY, true));
	}

	public int getWidth() {
		return (width);
	}

	public int getHeight() {
		return (height);
	}

	public byte[][] getData() {
		return (data);
	}

	public int getArea() {
		if (area == NOT_MEASURED) {
			measureArea();
		}
		return (area);
	}

	// calc area
	public int measureArea() {
		area = 0;
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				if (data[j][i] != 0)
					area++;
			}
		}
		return (area);
	}

	public boolean equals(BinaryImage _bi) {
		if (this.width != _bi.width) {
			return (false);
		}
		if (this.height != _bi.height) {
			return (false);
		}
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				if (this.data[j][i] != _bi.data[j][i]) {
					return (false);
				}
			}
		}
		return (true);
	}

	// fg->black, bg->white
	public BufferedImage toBufferedImage() {
		return (toBufferedImage(0x00000000, 0x00ffffff));
	}

	public BufferedImage toBufferedImage(int _foreground, int _background) {
		BufferedImage destImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		WritableRaster destRaster = destImage.copyData(null);
		DataBufferInt destBufInt = (DataBufferInt) (destRaster.getDataBuffer());
		int[] destArray = destBufInt.getData();

		int k = 0;
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				if (data[j][i] == 0)
					destArray[k] = _background;
				else
					destArray[k] = _foreground;
				k++;
			}
		}

		destImage.setData(destRaster);
		return (destImage);
	}

	public LabeledImage labelConnectedComponents() {
		return (new LabeledImage(this));
	}

	public LabeledImage labelConnectedComponents(short _method) {
		return (new LabeledImage(this, _method));
	}

	// return array of fg Coord
	public Coord[] extractPoints() {
		Vector<Coord> tmpVec = new Vector<Coord>();

		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				if (data[j][i] != 0) {
					Coord co = new Coord(i, j);
					tmpVec.addElement(co);
				}
			}
		}

		int size = tmpVec.size();
		if (size > 0) {
			Coord[] coArray = new Coord[size];
			for (int i = 0; i < size; i++) {
				coArray[i] = tmpVec.elementAt(i);
			}
			return (coArray);
		} else {
			return (null);
		}
	}

	// put points as fg
	public void putPoints(Coord[] _coArray) {
		if (_coArray == null || _coArray.length == 0)
			return;
		int len = _coArray.length;
		for (int i = 0; i < len; i++) {
			Coord co = _coArray[i];
			data[co.y][co.x] = 1;
		}
	}

	// return most Top-Left fg point
	public Coord topLeftPoint() {
		for (int j = 0; j < height; j++)
			for (int i = 0; i < width; i++)
				if (data[j][i] != 0)
					return (new Coord(i, j));
		return (null);
	}

	public static BinaryImage subtract(BinaryImage _bi1, BinaryImage _bi2)
			throws ImageException {
		int width = _bi1.width;
		int height = _bi1.height;
		if (width != _bi2.width || height != _bi2.height) {
			throw new ImageException(
					"subtract(): Sizes of the two image must be the same"); //$NON-NLS-1$
		}

		BinaryImage newImage = new BinaryImage(width, height);
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				if (_bi1.data[j][i] != 0 && _bi2.data[j][i] == 0)
					newImage.data[j][i] = 1;
			}
		}

		return (newImage);
	}

	public BinaryImage subtract(BinaryImage _bi) throws ImageException {
		return (subtract(this, _bi));
	}

	public LineSegment detectLongestHorizontalLine() {
		return (detectLongestHorizontalLine(height - 1));
	}

	public LineSegment detectLongestHorizontalLine(int _maxHeight) {
		int maxLength = 0;
		Coord maxStart = new Coord(-1, -1);
		Coord maxEnd = new Coord(-1, -1);
		Coord curStart = new Coord(-1, -1);
		for (int j = 0; j <= _maxHeight; j++) {
			boolean flag = false;
			int curLength = 0;
			for (int i = 0; i < width; i++) {
				if (data[j][i] != 0) {
					if (flag) {
						curLength++;
					} else {
						flag = true;
						curLength = 1;
						curStart.set(i, j);
					}
				} else {
					if (flag) {
						if (curLength >= maxLength) {
							maxLength = curLength;
							maxStart.copy(curStart);
							maxEnd.set(i - 1, j);
						}
						flag = false;
					}
					// else{
					// }
				}
			}
			if (flag && curLength >= maxLength) {
				maxLength = curLength;
				maxStart.copy(curStart);
				maxEnd.set(width - 1, j);
			}
		}

		if (maxLength > 0) {
			return (new LineSegment(maxStart, maxEnd));
		} else {
			return (null);
		}
	}

	// extract underline from this image
	public BinaryImage drawUnderline() throws ImageException {
		// TODO separated by other image
		return (drawLongestHorizontalLine());
	}

	// 1px
	public BinaryImage drawLongestHorizontalLine() throws ImageException {
		LineSegment horSeg = detectLongestHorizontalLine();
		if (horSeg == null)
			return (null);
		if (horSeg.isVertical() || horSeg.isDiagonal())
			throw new ImageException(
					"Endpoints cannot be successfully detected"); //$NON-NLS-1$
		BinaryImage bi = new BinaryImage(width, height);
		Coord left = horSeg.getLeftPoint();
		Coord right = horSeg.getRightPoint();
		for (int i = left.x; i <= right.x; i++) {
			bi.data[left.y][i] = 1;
		}
		return (bi);
	}

	// covers thick lines (2px and above)
	public BinaryImage drawLongestHorizontalLineWithThick()
			throws ImageException {
		LineSegment horSeg = detectLongestHorizontalLine();
		if (horSeg == null)
			return (null);
		if (horSeg.isVertical() || horSeg.isDiagonal())
			throw new ImageException(
					"Endpoints cannot be successfully detected"); //$NON-NLS-1$
		BinaryImage bi = new BinaryImage(width, height);
		Coord left = horSeg.getLeftPoint();
		Coord right = horSeg.getRightPoint();
		for (int i = left.x; i <= right.x; i++) {
			bi.data[left.y][i] = 1;
		}
		int maxHeight = left.y - 1;
		int curX0 = left.x;
		int curX1 = right.x;
		while (maxHeight > 0) {
			LineSegment horSeg2 = detectLongestHorizontalLine(maxHeight);
			if (horSeg2 == null) {
				break;
			}
			if (horSeg2.isVertical() || horSeg2.isDiagonal())
				throw new ImageException(
						"Endpoints cannot be successfully detected"); //$NON-NLS-1$
			Coord left2 = horSeg.getLeftPoint();
			Coord right2 = horSeg.getRightPoint();
			if ((left2.y == maxHeight) && (left2.x == curX0)
					&& (right2.x == curX1)) {
				for (int i = left2.x; i <= right2.x; i++) {
					bi.data[maxHeight][i] = 1;
				}
				maxHeight--;
			} else {
				break;
			}
		}

		return (bi);
	}

	public void dump() {
		dump(System.out);
	}

	public void dump(PrintStream _ps) {
		PrintWriter pw = new PrintWriter(_ps, true);
		dump(pw);
	}

	@SuppressWarnings("nls")
	public void dump(PrintWriter _pw) {
		_pw.println("-------------------------------");
		_pw.println("Dumping a BinaryImage");
		_pw.println("Width = " + width + ", Height = " + height);
		for (int j = 0; j < height; j++) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < width; i++) {
				if (data[j][i] == 0)
					sb.append("0");
				else
					sb.append("1");
			}
			_pw.println(sb.toString());
		}
		_pw.println("-------------------------------");
	}

	public void dump(PrintStream _ps, int _left, int _top, int _width,
			int _height) {
		PrintWriter pw = new PrintWriter(_ps, true);
		dump(pw, _left, _top, _width, _height);
	}

	@SuppressWarnings("nls")
	public void dump(PrintWriter _pw, int _left, int _top, int _width,
			int _height) {
		_pw.println("-------------------------------");
		_pw.println("Dumping a part of a BinaryImage");
		_pw.println("Image width = " + width + ", Image height = " + height);
		_pw.println("Start point = ( " + _left + ", " + _top + ")");
		_pw.println("Printed width = " + _width + ", Printed height = "
				+ _height);
		for (int j = 0; j < _height; j++) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < _width; i++) {
				if (data[j + _top][i + _left] == 0)
					sb.append("0");
				else
					sb.append("1");
			}
			_pw.println(sb.toString());
		}
		_pw.println("-------------------------------");
	}
}
