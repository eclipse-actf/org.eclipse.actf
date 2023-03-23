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
import org.eclipse.actf.visualization.engines.lowvision.image.ImageException;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.BinaryImage;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.IInt2D;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.ImageUtil;

public class ImageReader14 {

	// TBD ImageType conversion for BufferdImage (gif: ImageIO.read() ->
	// BufferedImage (TYPE_BYTE_INDEXED))

	public static BufferedImage readBufferedImage(String _fileName)
			throws LowVisionIOException {
		short type = IoUtil.getFileType(_fileName);
		if (type != IoUtil.TYPE_UNKNOWN)
			return (readBufferedImage(_fileName, type));
		else
			throw new LowVisionIOException("Unknown image format: _fileName"); //$NON-NLS-1$
	}

	public static BufferedImage readBufferedImage(String _fileName, short _type)
			throws LowVisionIOException {
		BufferedImage bufIm = null;
		if (_type == IoUtil.TYPE_BMP) {
			bufIm = BMPReader.readBufferedImage(_fileName);
		} else if (_type == IoUtil.TYPE_JPEG) {
			try {
				bufIm = ImageIO.read(new File(_fileName));
			} catch (IOException ioe) {
				throw new LowVisionIOException("File \"" + _fileName //$NON-NLS-1$
						+ "\" cannot be read."); //$NON-NLS-1$
			}
		} else if (_type == IoUtil.TYPE_GIF) {
			try {
				bufIm = ImageIO.read(new File(_fileName));
			} catch (IOException ioe) {
				throw new LowVisionIOException("File \"" + _fileName //$NON-NLS-1$
						+ "\" cannot be read."); //$NON-NLS-1$
			}
		} else if (_type == IoUtil.TYPE_PNG) {
			try {
				bufIm = ImageIO.read(new File(_fileName));
			} catch (IOException ioe) {
				throw new LowVisionIOException("File \"" + _fileName //$NON-NLS-1$
						+ "\" cannot be read."); //$NON-NLS-1$
			}
		} else {
			throw new LowVisionIOException("Unknown image format: _fileName"); //$NON-NLS-1$
		}

		if (bufIm == null) {
			throw new LowVisionIOException("The image file cannot be read: " //$NON-NLS-1$
					+ _fileName);
		}
		return (bufIm);
	} 

	public static IInt2D readInt2D(String _fileName) throws LowVisionIOException {
		try {
			return (ImageUtil
					.bufferedImageToInt2D(readBufferedImage(_fileName)));
		} catch (ImageException e) {
			e.printStackTrace();
			throw new LowVisionIOException(
					"ImageException occurred while converting BufferedImage into Int2D."); //$NON-NLS-1$
		}
	}

	public static IInt2D readInt2D(String _fileName, short _type)
			throws LowVisionIOException {
		try {
			return (ImageUtil.bufferedImageToInt2D(readBufferedImage(_fileName,
					_type)));
		} catch (ImageException e) {
			e.printStackTrace();
			throw new LowVisionIOException(
					"ImageException occurred while converting BufferedImage into Int2D."); //$NON-NLS-1$
		}
	}

	public static BinaryImage readBinaryImage(String _fileName)
			throws LowVisionIOException {
		short type = IoUtil.getFileType(_fileName);
		if (type != IoUtil.TYPE_UNKNOWN)
			return (readBinaryImage(_fileName, type));
		else
			throw new LowVisionIOException("Unknown image format."); //$NON-NLS-1$
	}

	public static BinaryImage readBinaryImage(String _fileName, short _type)
			throws LowVisionIOException {
		if (_type == IoUtil.TYPE_PBM)
			return (PBMReader.readBinaryImage(_fileName));
		else
			throw new LowVisionIOException("Unknown image format.");  //$NON-NLS-1$
	}
}
