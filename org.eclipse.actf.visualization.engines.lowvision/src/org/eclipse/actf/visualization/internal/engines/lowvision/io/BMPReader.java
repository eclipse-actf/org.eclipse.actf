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
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.actf.visualization.engines.lowvision.LowVisionIOException;
import org.eclipse.actf.visualization.internal.engines.lowvision.DebugUtil;
import org.eclipse.actf.visualization.internal.engines.lowvision.DosUtil;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.Int2D;

public class BMPReader {
	
	//TODO remove redundancy
	
	public static Int2D readInt2D(String _fileName) throws LowVisionIOException {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(_fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new LowVisionIOException("The file was not found: " //$NON-NLS-1$
					+ _fileName);
		}
		return (readInt2D(fis));
	}

	public static BufferedImage readBufferedImage(String _fileName)
			throws LowVisionIOException {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(_fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new LowVisionIOException("The file was not found: " //$NON-NLS-1$
					+ _fileName);
		}
		return (readBufferedImage(fis));
	}

	@SuppressWarnings("nls")
	public static int getBitCount(String _fileName) throws LowVisionIOException {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(_fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new LowVisionIOException("The file was not found: "
					+ _fileName);
		}
		return (getBitCount(fis));
	}

	@SuppressWarnings("nls")
	public static int getBitCount(InputStream _is) throws LowVisionIOException {
		short bitCount;

		DataInputStream dis = new DataInputStream(new BufferedInputStream(_is));

		// // read header
		try {
			// magic number "BM"
			int magic1 = dis.readByte();
			int magic2 = dis.readByte();
			if (magic1 != 0x42 || magic2 != 0x4d) {
				throw new LowVisionIOException("Bad magic characters: "
						+ magic1 + " and " + magic2);
			}

			/* fileSize = */DosUtil.upsideDownInt(dis.readInt());

			// skip reserved byte
			dis.skipBytes(4);

			/* offSet = */DosUtil.upsideDownInt(dis.readInt());

			// second struct size
			/* structSize = */DosUtil.upsideDownInt(dis.readInt());

			/* imageWidth = */DosUtil.upsideDownInt(dis.readInt());
			/* imageHeight = */DosUtil.upsideDownInt(dis.readInt());

			// 1
			dis.skipBytes(2);

			// support 8/16/24/32 bit image
			bitCount = DosUtil.upsideDownShort(dis.readShort());
			if (bitCount != 8 && bitCount != 16 && bitCount != 24
					&& bitCount != 32) {
				throw new LowVisionIOException(
						"Current version processes 8-bit/16-bit/24-bit/32-bit images only. The image's bitcount = "
								+ bitCount);
			}
			return (bitCount);
		} catch (IOException e) {
			e.printStackTrace();
			throw new LowVisionIOException(
					"IO error occurred when reading the header.");
		}
	}

	@SuppressWarnings("nls")
	public static BufferedImage readBufferedImage(InputStream _is)
			throws LowVisionIOException {
		int fileSize;
		int offSet;
		// int structSize;
		int imageWidth;
		int imageHeight;
		short bitCount;
		// int imageSize;
		int linePadding;

		DataInputStream dis = new DataInputStream(new BufferedInputStream(_is));

		try {
			// magic number "BM"
			int magic1 = dis.readByte();
			int magic2 = dis.readByte();
			if (magic1 != 0x42 || magic2 != 0x4d) {
				throw new LowVisionIOException("Bad magic characters: "
						+ magic1 + " and " + magic2);
			}

			fileSize = DosUtil.upsideDownInt(dis.readInt());

			// skip reserved byte
			dis.skipBytes(4);

			offSet = DosUtil.upsideDownInt(dis.readInt());

			/* structSize = */DosUtil.upsideDownInt(dis.readInt());

			imageWidth = DosUtil.upsideDownInt(dis.readInt());
			imageHeight = DosUtil.upsideDownInt(dis.readInt());

			// 1
			dis.skipBytes(2);

			// support 8/16/24/32 bit images
			bitCount = DosUtil.upsideDownShort(dis.readShort());
			if (bitCount != 8 && bitCount != 16 && bitCount != 24
					&& bitCount != 32) {
				throw new LowVisionIOException(
						"Current version processes 8-bit/16-bit/24-bit/32-bit images only. The image's bitcount = "
								+ bitCount);
			}

			dis.skipBytes(4);

			/* imageSize = */DosUtil.upsideDownInt(dis.readInt());
			linePadding = 0;
			if (bitCount == 24) {
				int res = imageWidth * 3 % 4;
				if (res != 0)
					linePadding = 4 - res;
			} else if (bitCount == 16 && (imageWidth % 2 == 1)) { // 2004/12(2)
				linePadding = 2;
			}

			if (bitCount == 8 && imageWidth * imageHeight + offSet != fileSize) {
				DebugUtil.errMsg(null, "WARNING!!  Bad file size. imageWidth="
						+ imageWidth + ", linePadding=" + linePadding
						+ ", imageHeight=" + imageHeight + ", offSet=" + offSet
						+ ", fileSize=" + fileSize);
			}
			if (bitCount == 24
					&& (imageWidth * 3 + linePadding) * imageHeight + offSet != fileSize) {
				DebugUtil.errMsg(null, "WARNING!!  Bad file size. imageWidth="
						+ imageWidth + ", linePadding=" + linePadding
						+ ", imageHeight=" + imageHeight + ", offSet=" + offSet
						+ ", fileSize=" + fileSize);
			}
			if (bitCount == 32
					&& imageWidth * 4 * imageHeight + offSet != fileSize) {
				DebugUtil.errMsg(null, "WARNING!!  Bad file size. imageWidth="
						+ imageWidth + ", linePadding=" + linePadding
						+ ", imageHeight=" + imageHeight + ", offSet=" + offSet
						+ ", fileSize=" + fileSize);
			}
			// TODO 16bit check

			dis.skipBytes(16);

		} catch (IOException e) {
			e.printStackTrace();
			throw new LowVisionIOException(
					"IO error occurred when reading the header.");
		}

		int[] colorTable = null;
		if (bitCount == 8) {
			try {
				byte[] byteTable = new byte[1024];
				int readNum = dis.read(byteTable);
				if (readNum != 1024) {
					throw new LowVisionIOException(
							"Amount of the image data is not enough.");
				}
				colorTable = new int[256];
				for (int k = 0; k < 256; k++) {
					int b = byteTable[4 * k];
					int g = byteTable[4 * k + 1];
					int r = byteTable[4 * k + 2];
					colorTable[k] = (r << 16) + (g << 8) + b;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/*
		 * (1)start from bottom left (2)B->G->R (24 bit format)
		 */

		int bytesPerLine = 0;
		if (bitCount == 8) {
			bytesPerLine = imageWidth;
		} else if (bitCount == 16) { // 2004/12(3)
			bytesPerLine = imageWidth * 2;
		} else if (bitCount == 24) {
			bytesPerLine = imageWidth * 3;
		} else if (bitCount == 32) {
			bytesPerLine = imageWidth * 4;
		}
		byte[][] srcArray = new byte[imageHeight][bytesPerLine];

		BufferedImage destImage = new BufferedImage(imageWidth, imageHeight,
				BufferedImage.TYPE_INT_RGB);
		WritableRaster destRaster = destImage.copyData(null);
		DataBufferInt destBufInt = (DataBufferInt) (destRaster.getDataBuffer());
		int[] destArray = destBufInt.getData();

		try {
			for (int j = 0; j < imageHeight; j++) {
				int readNum = dis.read(srcArray[j]);
				if (readNum != bytesPerLine) {
					throw new LowVisionIOException(
							"Amount of the image data is not enough.");
				}
				dis.skipBytes(linePadding);
			}

			int destIndex = 0;
			if (bitCount == 8) {
				for (int j = imageHeight - 1; j >= 0; j--) {
					for (int i = 0; i < imageWidth; i++) {
						destArray[destIndex] = colorTable[srcArray[j][i]];
						destIndex++;
					}
				}
			} else if (bitCount == 16) { // 2004/12(4)
				for (int j = imageHeight - 1; j >= 0; j--) {
					for (int i = 0; i < imageWidth; i++) {
						int i16 = ((int) (srcArray[j][i * 2 + 1]) << 8)
								| (srcArray[j][i * 2] & 0xff);
						int b = i16 & 0x1f;
						int g = (i16 >> 5) & 0x1f;
						int r = (i16 >> 10) & 0x1f;
						int pixel = r << 19 | g << 11 | b << 3;
						destArray[destIndex] = pixel;
						destIndex++;
					}
				}
			} else if (bitCount == 24) {
				for (int j = imageHeight - 1; j >= 0; j--) {
					for (int i = 0; i < imageWidth; i++) {
						int pixel = 0;
						pixel |= ((int) (srcArray[j][i * 3]) & 0xff); // b
						pixel |= ((int) (srcArray[j][i * 3 + 1]) & 0xff) << 8; // g
						pixel |= ((int) (srcArray[j][i * 3 + 2]) & 0xff) << 16; // r
						destArray[destIndex] = pixel;
						destIndex++;
					}
				}
			} else if (bitCount == 32) {
				for (int j = imageHeight - 1; j >= 0; j--) {
					for (int i = 0; i < imageWidth; i++) {
						int pixel = 0;
						pixel |= ((int) (srcArray[j][i * 4]) & 0xff); // b
						pixel |= ((int) (srcArray[j][i * 4 + 1]) & 0xff) << 8; // g
						pixel |= ((int) (srcArray[j][i * 4 + 2]) & 0xff) << 16; // r
						destArray[destIndex] = pixel;
						destIndex++;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new LowVisionIOException(
					"IO error occurred when reading the image data.");
		}
		srcArray = null;

		destImage.setData(destRaster);

		try {
			dis.close();
			_is.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new LowVisionIOException(
					"IO error occurred when closing the input streams.");
		}

		return (destImage);
	}

	@SuppressWarnings("nls")
	public static Int2D readInt2D(InputStream _is) throws LowVisionIOException {
		int fileSize;
		int offSet;
		// int structSize;
		int imageWidth;
		int imageHeight;
		short bitCount;
		// int imageSize;
		int linePadding;

		DataInputStream dis = new DataInputStream(new BufferedInputStream(_is));

		try {
			int magic1 = dis.readByte();
			int magic2 = dis.readByte();
			if (magic1 != 0x42 || magic2 != 0x4d) {
				throw new LowVisionIOException("Bad magic characters: "
						+ magic1 + " and " + magic2);
			}

			fileSize = DosUtil.upsideDownInt(dis.readInt());

			dis.skipBytes(4);

			offSet = DosUtil.upsideDownInt(dis.readInt());

			/* structSize = */DosUtil.upsideDownInt(dis.readInt());

			imageWidth = DosUtil.upsideDownInt(dis.readInt());
			imageHeight = DosUtil.upsideDownInt(dis.readInt());

			dis.skipBytes(2);

			bitCount = DosUtil.upsideDownShort(dis.readShort());
			// if( bitCount != 8 && bitCount != 24 && bitCount != 32 ){
			if (bitCount != 8 && bitCount != 16 && bitCount != 24
					&& bitCount != 32) {
				throw new LowVisionIOException(
						"Current version processes 8-bit/24-bit/32-bit images only. The image's bitcount = "
								+ bitCount);
			}

			dis.skipBytes(4);

			/* imageSize = */DosUtil.upsideDownInt(dis.readInt());
			linePadding = 0;
			if (bitCount == 24) {
				int res = imageWidth * 3 % 4;
				if (res != 0)
					linePadding = 4 - res;
			} else if (bitCount == 16 && (imageWidth % 2 == 1)) { // 2004/12(2)
				linePadding = 2;
			}

			if (bitCount == 8 && imageWidth * imageHeight + offSet != fileSize) {
				// throw new LowVisionIOException( "Bad file size. imageWidth="
				// + imageWidth + ", linePadding=" + linePadding + ",
				// imageHeight=" + imageHeight + ", offSet=" + offSet + ",
				// fileSize=" + fileSize );
				DebugUtil.errMsg(null, "WARNING!!  Bad file size. imageWidth="
						+ imageWidth + ", linePadding=" + linePadding
						+ ", imageHeight=" + imageHeight + ", offSet=" + offSet
						+ ", fileSize=" + fileSize);
			}
			if (bitCount == 24
					&& (imageWidth * 3 + linePadding) * imageHeight + offSet != fileSize) {
				// throw new LowVisionIOException( "Bad file size. imageWidth="
				// + imageWidth + ", linePadding=" + linePadding + ",
				// imageHeight=" + imageHeight + ", offSet=" + offSet + ",
				// fileSize=" + fileSize );
				DebugUtil.errMsg(null, "WARNING!!  Bad file size. imageWidth="
						+ imageWidth + ", linePadding=" + linePadding
						+ ", imageHeight=" + imageHeight + ", offSet=" + offSet
						+ ", fileSize=" + fileSize);
			}
			if (bitCount == 32
					&& imageWidth * 4 * imageHeight + offSet != fileSize) {
				// throw new LowVisionIOException( "Bad file size. imageWidth="
				// + imageWidth + ", linePadding=" + linePadding + ",
				// imageHeight=" + imageHeight + ", offSet=" + offSet + ",
				// fileSize=" + fileSize );
				DebugUtil.errMsg(null, "WARNING!!  Bad file size. imageWidth="
						+ imageWidth + ", linePadding=" + linePadding
						+ ", imageHeight=" + imageHeight + ", offSet=" + offSet
						+ ", fileSize=" + fileSize);
			}
			// TODO 16bit check

			dis.skipBytes(16);

		} catch (IOException e) {
			e.printStackTrace();
			throw new LowVisionIOException(
					"IO error occurred when reading the header.");
		}

		int[] colorTable = null;
		if (bitCount == 8) {
			try {
				byte[] byteTable = new byte[1024];
				int readNum = dis.read(byteTable);
				if (readNum != 1024) {
					throw new LowVisionIOException(
							"Amount of the image data is not enough.");
				}
				colorTable = new int[256];
				for (int k = 0; k < 256; k++) {
					int b = byteTable[4 * k];
					int g = byteTable[4 * k + 1];
					int r = byteTable[4 * k + 2];
					colorTable[k] = (r << 16) + (g << 8) + b;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		int bytesPerLine = 0;
		if (bitCount == 8) {
			bytesPerLine = imageWidth;
		} else if (bitCount == 16) { // 2004/12(3)
			bytesPerLine = imageWidth * 2;
		} else if (bitCount == 24) {
			bytesPerLine = imageWidth * 3;
		} else if (bitCount == 32) {
			bytesPerLine = imageWidth * 4;
		}
		byte[][] srcArray = new byte[imageHeight][bytesPerLine];

		Int2D destImage = new Int2D(imageWidth, imageHeight);

		try {
			for (int j = 0; j < imageHeight; j++) {
				int readNum = dis.read(srcArray[j]);
				if (readNum != bytesPerLine) {
					throw new LowVisionIOException(
							"Amount of the image data is not enough.");
				}
				dis.skipBytes(linePadding);
			}

			if (bitCount == 8) {
				for (int j = imageHeight - 1; j >= 0; j--) {
					for (int i = 0; i < imageWidth; i++) {
						destImage.getData()[imageHeight - j - 1][i] = colorTable[srcArray[j][i]];
					}
				}
			} else if (bitCount == 16) { // 2004/12(4)
				for (int j = imageHeight - 1; j >= 0; j--) {
					for (int i = 0; i < imageWidth; i++) {
						int i16 = ((int) (srcArray[j][i * 2 + 1]) << 8)
								| (srcArray[j][i * 2] & 0xff);
						int b = i16 & 0x1f;
						int g = (i16 >> 5) & 0x1f;
						int r = (i16 >> 10) & 0x1f;
						int pixel = r << 19 | g << 11 | b << 3;
						destImage.getData()[imageHeight - j - 1][i] = pixel;
					}
				}
			} else if (bitCount == 24) {
				for (int j = imageHeight - 1; j >= 0; j--) {
					for (int i = 0; i < imageWidth; i++) {
						int pixel = 0;
						pixel |= ((int) (srcArray[j][i * 3]) & 0xff); // b
						pixel |= ((int) (srcArray[j][i * 3 + 1]) & 0xff) << 8; // g
						pixel |= ((int) (srcArray[j][i * 3 + 2]) & 0xff) << 16; // r
						destImage.getData()[imageHeight - j - 1][i] = pixel;
					}
				}
			} else if (bitCount == 32) {
				for (int j = imageHeight - 1; j >= 0; j--) {
					for (int i = 0; i < imageWidth; i++) {
						int pixel = 0;
						pixel |= ((int) (srcArray[j][i * 4]) & 0xff); // b
						pixel |= ((int) (srcArray[j][i * 4 + 1]) & 0xff) << 8; // g
						pixel |= ((int) (srcArray[j][i * 4 + 2]) & 0xff) << 16; // r
						destImage.getData()[imageHeight - j - 1][i] = pixel;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new LowVisionIOException(
					"IO error occurred when reading the image data.");
		}
		srcArray = null;

		try {
			dis.close();
			_is.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new LowVisionIOException(
					"IO error occurred when closing the input streams.");
		}

		return (destImage);
	}
}
