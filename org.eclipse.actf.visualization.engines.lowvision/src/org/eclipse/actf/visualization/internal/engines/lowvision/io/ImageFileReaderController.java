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
import java.util.Date;

import org.eclipse.actf.visualization.internal.engines.lowvision.DebugUtil;

class ImageFileReaderController {
	private static final long MAX_TIME_TO_GET = 30000; // msec

	private String fileName = null;

	private boolean gottenFlag = false;

	private BufferedImage image = null;

	private ImageFileReaderGetter getter = null;

	ImageFileReaderController(String _fileName) {
		fileName = _fileName;
		// timer = new ImageFileReaderTimer( this, MAX_TIME_TO_GET );
		getter = new ImageFileReaderGetter(this, _fileName);
		// timer.start();
		getter.setDaemon(true);
		getter.start();
		waitForData();
	}

	BufferedImage getImage() {
		return (image);
	}

	private void waitForData() {
		try {
			for (long st = (new Date()).getTime(); (new Date()).getTime() - st <= MAX_TIME_TO_GET; Thread
					.sleep(50)) {
				if (gottenFlag /* || timeOutFlag */) {
					getter.interrupt();
					return;
				}
			}
			DebugUtil.outMsg(null, "Thread for getting \"" + fileName //$NON-NLS-1$
					+ "\" was timed out."); //$NON-NLS-1$
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		} finally {
			getter.interrupt();
		}
	}

	void setGottenFlag(boolean _b) {
		gottenFlag = _b;
	}

	void setBufferedImage(BufferedImage _bi) {
		image = _bi;

	}
}
