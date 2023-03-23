/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.engines.voicebrowser;

import java.io.File;

import org.eclipse.actf.ai.tts.ITTSEngine;
import org.eclipse.actf.ai.voice.IVoiceEventListener;

public class DummyTTSEngine implements ITTSEngine {

    public void dispose() {
    }

    public int getSpeed() {
        return 0;
    }

    public void setEventListener(IVoiceEventListener eventListener) {
    }

    public void setGender(String gender) {
    }

    public void setLanguage(String language) {
    }

    public void setSpeed(int speed) {
    }

    public void speak(String text, int flags, int index) {
    }

    public void stop() {
    }

    public boolean isAvailable() {
        return true;
    }

	public boolean isDisposed() {
		return false;
	}

	public boolean canSpeakToFile() {
		return false;
	}

	public boolean speakToFile(String text, File file) {
		return false;
	}

}
