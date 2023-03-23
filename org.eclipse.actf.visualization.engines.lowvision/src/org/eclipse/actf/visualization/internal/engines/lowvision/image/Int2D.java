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

import org.eclipse.actf.visualization.engines.lowvision.LowVisionIOException;
import org.eclipse.actf.visualization.engines.lowvision.image.ImageException;
import org.eclipse.actf.visualization.internal.engines.lowvision.io.BMPWriter;

/*
 * data[y][x] = BufferdImage.TYPE_INT_RGB (null, R, G, B)
 * 
 * data[0][0] is top left
 */
public class Int2D implements IInt2D {
	private int width;

	private int height;

	private int[][] data;

	public Int2D(int _w, int _h) {
		this.width = _w;
		this.height = _h;
		data = new int[getHeight()][getWidth()];
	}

	public Int2D(int _w, int _h, int[][] _array) throws ImageException {
		this(_w, _h);
		if (_array.length < _h || _array[0].length < _w) {
			throw new ImageException("Out of range"); //$NON-NLS-1$
		}
		for (int j = 0; j < getHeight(); j++) {
			for (int i = 0; i < getWidth(); i++) {
				this.getData()[j][i] = _array[j][i];
			}
		}
	}

	public Int2D(Int2D _src) {
		this(_src.getWidth(), _src.getHeight());
		for (int j = 0; j < getHeight(); j++) {
			for (int i = 0; i < getWidth(); i++) {
				this.getData()[j][i] = _src.getData()[j][i];
			}
		}
	}

	public Int2D(BufferedImage _bi) {
		this(_bi.getWidth(), _bi.getHeight());
		WritableRaster srcRaster = _bi.copyData(null);
		DataBufferInt srcBufInt = (DataBufferInt) (srcRaster.getDataBuffer());
		int[] srcArray = srcBufInt.getData();
		int k = 0;
		for (int j = 0; j < getHeight(); j++) {
			for (int i = 0; i < getWidth(); i++) {
				getData()[j][i] = srcArray[k];
				k++;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.actf.visualization.internal.engines.lowvision.image.IInt2D#deepCopy()
	 */
	public Int2D deepCopy() {
		Int2D dest = new Int2D(width, height);
		int[][] destData = dest.getData();
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				destData[j][i] = data[j][i];
			}
		}
		return (dest);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.actf.visualization.internal.engines.lowvision.image.IInt2D#getWidth()
	 */
	public int getWidth() {
		return (width);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.actf.visualization.internal.engines.lowvision.image.IInt2D#getHeight()
	 */
	public int getHeight() {
		return (height);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.actf.visualization.internal.engines.lowvision.image.IInt2D#toBufferedImage()
	 */
	public BufferedImage toBufferedImage() {
		return (ImageUtil.int2DToBufferedImage(this));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.actf.visualization.internal.engines.lowvision.image.IInt2D#writeToBMPFile(java.lang.String)
	 */
	public void writeToBMPFile(String _fileName) throws LowVisionIOException {
		BMPWriter.writeInt2D(this, _fileName);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.actf.visualization.internal.engines.lowvision.image.IInt2D#writeToBMPFile(java.lang.String, int)
	 */
	public void writeToBMPFile(String _fileName, int _bitCount)
			throws LowVisionIOException {
		BMPWriter.writeInt2D(this, _fileName, _bitCount);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.actf.visualization.internal.engines.lowvision.image.IInt2D#fill(int)
	 */
	public void fill(int _color) {
		for (int j = 0; j < getHeight(); j++) {
			for (int i = 0; i < getWidth(); i++) {
				getData()[j][i] = _color;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.actf.visualization.internal.engines.lowvision.image.IInt2D#cutMargin(int)
	 */
	public Int2D cutMargin(int _m) throws ImageException {
		if (getWidth() <= 2 * _m || getHeight() <= 2 * _m) {
			throw new ImageException("The margin is too wide."); //$NON-NLS-1$
		}
		int newW = getWidth() - 2 * _m;
		int newH = getHeight() - 2 * _m;
		Int2D i2d = new Int2D(newW, newH);
		for (int j = 0; j < newH; j++) {
			for (int i = 0; i < newW; i++) {
				i2d.getData()[j][i] = this.getData()[j + _m][i + _m];
			}
		}
		return (i2d);
	}

	public void drawContour(ConnectedComponent _cc, int _color,
			boolean _overWrite) {
		Int2D dest = this;
		if (!_overWrite) {
			dest = this.deepCopy();
		}

		int ccLeft = _cc.left;
		int ccTop = _cc.top;
		int ccWidth = _cc.shape.width;
		int ccHeight = _cc.shape.height;

		for (int j = 0; j < ccHeight; j++) {
			if (_cc.shape.data[j][0] != 0) {
				dest.getData()[j + ccTop][ccLeft] = _color;
			}
			if (_cc.shape.data[j][ccWidth - 1] != 0) {
				dest.getData()[j + ccTop][ccWidth - 1 + ccLeft] = _color;
			}
		}
		for (int i = 0; i < ccWidth; i++) {
			if (_cc.shape.data[0][i] != 0) {
				dest.getData()[ccTop][i + ccLeft] = _color;
			}
			if (_cc.shape.data[ccHeight - 1][0] != 0) {
				dest.getData()[ccHeight - 1 + ccTop][ccLeft] = _color;
			}
		}

		for (int j = 1; j < ccHeight - 1; j++) {
			for (int i = 1; i < ccWidth - 1; i++) {
				if (_cc.shape.data[j][i] != 0) {
					if (_cc.shape.data[j][i - 1] == 0
							|| _cc.shape.data[j][i + 1] == 0
							|| _cc.shape.data[j - 1][i] == 0
							|| _cc.shape.data[j + 1][i] == 0) {
						dest.getData()[j + ccTop][i + ccLeft] = _color;
					}
				}
			}
		}
	}

	public int[][] getData() {
		return data;
	}

}
