/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.ai.voice;

import java.util.EventListener;

/**
 * The listener interface for receiving notification of voice events.
 */
public interface IVoiceEventListener extends EventListener {

	/**
	 * Invoked when TTS engine begins speaking text or complete speaking text.
	 * This event only occurs if index parameter is specified on IVoice#speak()
	 * request.
	 * 
	 * @param index
	 *            positive integer when TTS engine begins speaking text -1 when
	 *            TTS engine completed speaking text
	 * @see IVoice#speak(String, boolean, int)
	 */
	public abstract void indexReceived(int index);

}
