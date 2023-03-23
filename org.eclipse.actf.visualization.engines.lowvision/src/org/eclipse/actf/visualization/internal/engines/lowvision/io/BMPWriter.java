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
package org.eclipse.actf.visualization.internal.engines.lowvision.io;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.actf.visualization.engines.lowvision.LowVisionIOException;
import org.eclipse.actf.visualization.internal.engines.lowvision.DosUtil;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.ImageUtil;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.Int2D;

public class BMPWriter {
	private static final int DEFAULT_BITCOUNT = 24;

	public static void writeInt2D(Int2D _i2d, String _fileName, int _bitCount)
			throws LowVisionIOException {
		writeBufferedImage(ImageUtil.int2DToBufferedImage(_i2d), _fileName,
				_bitCount);
	}

	public static void writeInt2D(Int2D _i2d, String _fileName)
			throws LowVisionIOException {
		writeBufferedImage(ImageUtil.int2DToBufferedImage(_i2d), _fileName);
	}

	public static void writeInt2D(Int2D _i2d, OutputStream _os, int _bitCount)
			throws LowVisionIOException {
		writeBufferedImage(ImageUtil.int2DToBufferedImage(_i2d), _os, _bitCount);
	}

	public static void writeInt2D(Int2D _i2d, OutputStream _os)
			throws LowVisionIOException {
		writeBufferedImage(ImageUtil.int2DToBufferedImage(_i2d), _os);
	}

	public static void writeBufferedImage(BufferedImage _bi, String _fileName,
			int _bitCount) throws LowVisionIOException {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(_fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new LowVisionIOException("The file was not found: " //$NON-NLS-1$
					+ _fileName);
		}
		writeBufferedImage(_bi, fos, _bitCount);
	}

	public static void writeBufferedImage(BufferedImage _bi, String _fileName)
			throws LowVisionIOException {
		writeBufferedImage(_bi, _fileName, DEFAULT_BITCOUNT);
	}

	public static void writeBufferedImage(BufferedImage _bi, OutputStream _os)
			throws LowVisionIOException {
		writeBufferedImage(_bi, _os, DEFAULT_BITCOUNT);
	}

	public static void writeBufferedImage(BufferedImage _bi, OutputStream _os,
			int _bitCount) throws LowVisionIOException {
		// only for BufferedImage.TYPE_INT_RGB

		if (_bitCount != 16 && _bitCount != 24) {
			throw new LowVisionIOException("Invalid bitCount: " + _bitCount); //$NON-NLS-1$
		}

		int imageWidth = _bi.getWidth();
		int imageHeight = _bi.getHeight();
		int residual = 0;
		if (_bitCount == 24) {
			residual = imageWidth * 3 % 4; // 24bit
		} else if (_bitCount == 16) {
			residual = imageWidth * 2 % 4; // 16bit
		}
		int linePadding = 0;
		if (residual > 0)
			linePadding = 4 - residual;
		int imageSize = 0;
		if (_bitCount == 24) {
			imageSize = (imageWidth * 3 + linePadding) * imageHeight;
		} else if (_bitCount == 16) {
			imageSize = (imageWidth * 2 + linePadding) * imageHeight;
		}
		int fileSize = imageSize + 54;

		DataOutputStream dos = new DataOutputStream(_os);

		try {
			// magic number "BM"
			dos.writeByte(0x42);
			dos.writeByte(0x4d);
			dos.writeInt(DosUtil.upsideDownInt(fileSize));
			dos.writeInt(0);
			dos.writeInt(DosUtil.upsideDownInt(54));
			dos.writeInt(DosUtil.upsideDownInt(40));
			dos.writeInt(DosUtil.upsideDownInt(imageWidth));
			dos.writeInt(DosUtil.upsideDownInt(imageHeight));
			dos.writeShort(DosUtil.upsideDownShort((short) 1));
			dos.writeShort(DosUtil.upsideDownShort((short) _bitCount));
			dos.writeInt(DosUtil.upsideDownInt(0));
			dos.writeInt(DosUtil.upsideDownInt(imageSize));
			dos.writeInt(DosUtil.upsideDownInt(4724));
			dos.writeInt(DosUtil.upsideDownInt(4724));
			dos.writeInt(DosUtil.upsideDownInt(0));
			dos.writeInt(DosUtil.upsideDownInt(0));
		} catch (IOException e) {
			e.printStackTrace();
			throw new LowVisionIOException(
					"IO error occurred when writing header."); //$NON-NLS-1$
		}

		WritableRaster srcRaster = _bi.copyData(null);
		DataBufferInt srcBufInt = (DataBufferInt) (srcRaster.getDataBuffer());
		int[] srcArray = srcBufInt.getData();

		byte[][] destArray2d = null;
		int srcIndex = 0;
		if (_bitCount == 24) {
			destArray2d = new byte[imageHeight][imageWidth * 3];
			for (int j = imageHeight - 1; j >= 0; j--) {
				for (int i = 0; i < imageWidth; i++) {
					int rgb = srcArray[srcIndex];
					destArray2d[j][i * 3] = (byte) (rgb & 0xff); // b
					destArray2d[j][i * 3 + 1] = (byte) (rgb >> 8 & 0xff); // g
					destArray2d[j][i * 3 + 2] = (byte) (rgb >> 16 & 0xff); // r
					srcIndex++;
				}
			}
		} else if (_bitCount == 16) {
			destArray2d = new byte[imageHeight][imageWidth * 2];
			for (int j = imageHeight - 1; j >= 0; j--) {
				for (int i = 0; i < imageWidth; i++) {
					int rgb = srcArray[srcIndex];
					int r = (rgb >> 19) & 0x1f;
					int g = (rgb >> 11) & 0x1f;
					int b = (rgb >> 3) & 0x1f;
					destArray2d[j][i * 2] = (byte) (((g & 0x7) << 5) | b);
					destArray2d[j][i * 2 + 1] = (byte) ((r << 2) | ((g >> 3) & 0x3));
					srcIndex++;
				}
			}
		}

		try {
			for (int j = 0; j < imageHeight; j++) {
				dos.write(destArray2d[j]);
				for (int i = 0; i < linePadding; i++) {
					dos.writeByte(0);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new LowVisionIOException(
					"IO error occurred while writing image data."); //$NON-NLS-1$
		}

		try {
			dos.close();
			_os.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new LowVisionIOException(
					"IO error occurred when closing output streams."); //$NON-NLS-1$
		}
	}
}
