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
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.eclipse.actf.visualization.engines.lowvision.LowVisionIOException;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.BinaryImage;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.ImageUtil;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.Int2D;

public class ImageWriter {
	public static void writeBufferedImage(BufferedImage _bi, String _fileName) throws LowVisionIOException {
		short type = IoUtil.getFileType(_fileName);
		if (type != IoUtil.TYPE_UNKNOWN)
			writeBufferedImage(_bi, _fileName, type);
		else
			throw new LowVisionIOException("Unknown image format."); //$NON-NLS-1$
	}

	public static void writeBufferedImage(BufferedImage _bi, String _fileName, short _type)
			throws LowVisionIOException {
		if (_type == IoUtil.TYPE_BMP)
			BMPWriter.writeBufferedImage(_bi, _fileName);
		else if (_type == IoUtil.TYPE_JPEG) {
			boolean result = true;
			try {
				File targetFile = new File(_fileName);
				result = ImageIO.write(_bi, "jpg", targetFile);
			} catch (IOException e) {
				e.printStackTrace();
				throw new LowVisionIOException("IO error occurred while decoding/closing."); //$NON-NLS-1$
			}
			if(!result){
				throw new LowVisionIOException("No appropriate writer is found while writing a JPEG image."); //$NON-NLS-1$
			}
		} else if (_type == IoUtil.TYPE_GIF) {
			File outFile = new File(_fileName);
			boolean result = true;
			try {
				result = ImageIO.write(_bi, "GIF", outFile); //$NON-NLS-1$
			} catch (IOException ioe) {
				ioe.printStackTrace();
				throw new LowVisionIOException("An error occurs while writing a GIF image."); //$NON-NLS-1$
			}
			if (!result) {
				throw new LowVisionIOException("No appropriate writer is found while writing a GIF image."); //$NON-NLS-1$
			}
			// throw new LowVisionIOException("This file format cannot be
			// written out.");
		} else if (_type == IoUtil.TYPE_PNG) {
			File outFile = new File(_fileName);
			boolean result = true;
			try {
				result = ImageIO.write(_bi, "PNG", outFile); //$NON-NLS-1$
			} catch (IOException ioe) {
				ioe.printStackTrace();
				throw new LowVisionIOException("An error occurs while writing a PNG image."); //$NON-NLS-1$
			}
			if (!result) {
				throw new LowVisionIOException("No appropriate writer is found while writing a PNG image."); //$NON-NLS-1$
			}
			// throw new LowVisionIOException("This file format cannot be
			// written out.");
		} else
			throw new LowVisionIOException("Unknown image format."); //$NON-NLS-1$
	}

	public static void writeInt2D(Int2D _i2d, String _fileName) throws LowVisionIOException {
		writeBufferedImage(ImageUtil.int2DToBufferedImage(_i2d), _fileName);
	}

	public static void writeInt2D(Int2D _i2d, String _fileName, short _type) throws LowVisionIOException {
		writeBufferedImage(ImageUtil.int2DToBufferedImage(_i2d), _fileName, _type);
	}

	public static void writeBinaryImage(BinaryImage _bi, String _fileName) throws LowVisionIOException {
		short type = IoUtil.getFileType(_fileName);
		if (type != IoUtil.TYPE_UNKNOWN)
			writeBinaryImage(_bi, _fileName, type);
		else
			throw new LowVisionIOException("Unknown image format."); //$NON-NLS-1$
	}

	public static void writeBinaryImage(BinaryImage _bi, String _fileName, short _type) throws LowVisionIOException {
		if (_type == IoUtil.TYPE_PBM)
			PBMWriter.writeBinaryImage(_bi, _fileName);
		else
			throw new LowVisionIOException("Unknown image format."); //$NON-NLS-1$
	}
}
