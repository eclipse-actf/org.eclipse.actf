/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.ai.voice;

import org.eclipse.actf.ai.voice.internal.VoicePlugin;

/**
 * Utility class to use ACTF Voice functions.
 */
public class VoiceUtil {

	/**
	 * Returns an implementation of IVoice to perform text synthesis operations.
	 * 
	 * @return IVoice implementation
	 */
	public static IVoice getVoice() {
		return VoicePlugin.getVoice();
	}

	/**
	 * Returns the default speed of the TTS engine which is saved in the
	 * preference.
	 * 
	 * @return The default speed of the TTS engine
	 */
	public static int getDefaultSpeed() {
		return VoicePlugin.getDefault().getPreferenceStore().getInt(
				IVoice.PREF_SPEED);
	}

}
