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

/*
 * Image file reader for gif/png (also can use for jpeg)
 */
public class ImageFileReader {

	// TODO use javax.imageio

	public static BufferedImage readBufferedImage(String _fileName) {
		ImageFileReaderController cont = new ImageFileReaderController(
				_fileName);
		BufferedImage bufIm = cont.getImage();
		return (bufIm);

		// return( (new ImageFileReaderController(_fileName)).getImage() );
	}
}
