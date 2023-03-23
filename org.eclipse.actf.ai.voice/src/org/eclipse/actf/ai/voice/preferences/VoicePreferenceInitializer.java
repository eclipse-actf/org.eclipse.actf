/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.ai.voice.preferences;

import org.eclipse.actf.ai.tts.TTSRegistry;
import org.eclipse.actf.ai.voice.IVoice;
import org.eclipse.actf.ai.voice.internal.VoicePlugin;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;


public class VoicePreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = VoicePlugin.getDefault().getPreferenceStore();
		store.setDefault(IVoice.PREF_ENGINE, TTSRegistry.getDefaultEngine());
		store.setDefault(IVoice.PREF_SPEED,  IVoice.SPEED_NORMAL/*50*/);
	}

}
