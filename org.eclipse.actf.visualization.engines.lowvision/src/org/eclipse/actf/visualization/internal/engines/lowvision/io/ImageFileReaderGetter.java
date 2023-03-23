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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import org.eclipse.actf.visualization.internal.engines.lowvision.DebugUtil;

class ImageFileReaderGetter extends Thread {
	ImageFileReaderController caller = null;

	String fileName = null;

	BufferedImage image = null;

	ImageFileReaderGetter(ImageFileReaderController _caller, String _fileName) {
		caller = _caller;
		fileName = _fileName;
	}

	public void run() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image im = tk.getImage(fileName);
		Component comp = new Component() {
			private static final long serialVersionUID = 2850121681250542701L;
		};
		MediaTracker tracker = new MediaTracker(comp);
		tracker.addImage(im, 0);

		try {
			tracker.waitForID(0);
		} catch (InterruptedException ie) {
			DebugUtil.outMsg(this, "Thread for getting \"" + fileName //$NON-NLS-1$
					+ "\" was interrupted."); //$NON-NLS-1$
			// ie.printStackTrace();
		}

		int w = im.getWidth(null);
		int h = im.getHeight(null);
		// DebugUtil.outMsg( this, "width = " + w + ", height = " + h );

		if (w > 0 && h > 0) {
			// DebugUtil.outMsg( this, "BufferedImage will be created." );
			image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			Graphics gr = image.getGraphics();
			gr.drawImage(im, 0, 0, null);
		}
		if (image == null) {
			DebugUtil.outMsg(this, "BufferedImage is null."); //$NON-NLS-1$
		}

		caller.setBufferedImage(image);
		// DebugUtil.outMsg( null, "checkAll() =" + tracker.checkAll() );
		// tracker.removeImage(im,0);
		// try{
		// Thread.sleep(50);
		// }catch( InterruptedException e ){
		// }

		caller.setGottenFlag(true);
	}
}
