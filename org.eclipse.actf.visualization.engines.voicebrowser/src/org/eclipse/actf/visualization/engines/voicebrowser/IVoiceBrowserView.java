/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Masahide WASHIZAWA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.engines.voicebrowser;

/**
 * The interface to add voice browser text view to a voice browser controller.
 * 
 * @see IVoiceBrowserController
 */
public interface IVoiceBrowserView {

	/**
	 * Voice browser controller invokes this method to hand out reading text to
	 * voice browser text view. The implementation will show this text into
	 * view.
	 * 
	 * @param text
	 *            reading text
	 */
	void drawText(String text);

	/**
	 * Voice browser controller invokes this method to hand out additional reading
	 * text to append current text in voice browser text view. The
	 * implementation will append this text into view.
	 * 
	 * @param text
	 *            additional reading text
	 */
	void drawAppendText(String text);
}
