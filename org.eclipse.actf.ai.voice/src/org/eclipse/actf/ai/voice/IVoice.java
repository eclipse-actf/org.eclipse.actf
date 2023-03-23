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

import org.eclipse.actf.ai.tts.ITTSEngine;

/**
 * The IVoice interface enables an application to perform text synthesis
 * operations.
 */
public interface IVoice {

	/**
	 * Speak the contents of a text string without event listening
	 * 
	 * @param text
	 *            text string to be spoken
	 * @param flush
	 *            true to flash all pending speak request prior to speak text
	 *            false to append this request at end of speak request
	 */
	public void speak(String text, boolean flush);

	/**
	 * Speak the contents of a text string with event listening
	 * 
	 * @param text
	 *            text string to be spoken
	 * @param flush
	 *            true to flash all pending speak request prior to speak text
	 *            false to append this request at end of speak request
	 * @param index
	 *            positive int value to be reported via IVoiceEventListener
	 *            Negative value will be ignored
	 * @see IVoiceEventListener
	 */
	public void speak(String text, boolean flush, int index);

	/**
	 * Flash all pending speak request
	 */
	public void stop();

	/**
	 * Set speaking speed
	 * 
	 * @param speed
	 *            speaking speed in range between 0 and 100 0: Minimum speed 50:
	 *            Normal speed 100: Maximum speed
	 */
	public void setSpeed(int speed);

	/**
	 * Get current speaking speed
	 * 
	 * @return speaking speed in range between 0 and 100
	 * @see #setSpeed(int)
	 */
	public int getSpeed();

	/**
	 * Set event listener in order to receive index event
	 * 
	 * @param eventListener
	 */
	public void setEventListener(IVoiceEventListener eventListener);

	/**
	 * Minimum speaking speed
	 */
	public static final int SPEED_MIN = 0;

	/**
	 * Maximum speaking speed
	 */
	public static final int SPEED_MAX = 100;

	/**
	 * Normal speaking speed
	 */
	public static final int SPEED_NORMAL = 50;

	/**
	 * A named preference that hold ID of current TTS engine
	 */
	public static final String PREF_ENGINE = "activeEngine"; //$NON-NLS-1$

	/**
	 * A named preference that hold speaking speed preferred by user
	 */
	public static final String PREF_SPEED = "defaultSpeed"; //$NON-NLS-1$

	/**
	 * Get current low-level TTS Engine interface. <BR>
	 * Please note: This function is provided for special voice application
	 * which requires precise control of TTS engine features. Use of low-level
	 * interface is generally not recommended.
	 * 
	 * @return ITTSEngine
	 */
	public ITTSEngine getTTSEngine();
}
