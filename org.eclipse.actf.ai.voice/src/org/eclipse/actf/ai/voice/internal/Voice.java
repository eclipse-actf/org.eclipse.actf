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
package org.eclipse.actf.ai.voice.internal;

import org.eclipse.actf.ai.tts.ITTSEngine;
import org.eclipse.actf.ai.tts.TTSRegistry;
import org.eclipse.actf.ai.voice.IVoice;
import org.eclipse.actf.ai.voice.IVoiceEventListener;
import org.eclipse.actf.ai.voice.VoiceUtil;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;


/**
 * Voice manages TTS engines using preference.
 * This speaks string with specified TTS engine in the preference.
 */
public class Voice implements IVoice, IPropertyChangeListener {

	private ITTSEngine ttsEngine = null;
	
    private IVoiceEventListener eventListener;
	
    private static final IPreferenceStore preferenceStore = VoicePlugin.getDefault().getPreferenceStore();
	
	/**
	 * Constructor of the Voice.
	 */
	public Voice() {
		ttsEngine = newTTSEngine();
		VoicePlugin.getDefault().addPropertyChangeListener(this);
		setSpeed();
	}
	
	private ITTSEngine newTTSEngine() {
		ITTSEngine engine = TTSRegistry.createTTSEngine(preferenceStore.getString(PREF_ENGINE));
		if( null == engine ) {
			engine = TTSRegistry.createTTSEngine(TTSRegistry.getDefaultEngine());
		}
		return engine;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent event) {
		if( PREF_ENGINE.equals(event.getProperty()) ) {
			if( null != ttsEngine ) {
				stop();
				ttsEngine.dispose();
				ttsEngine = newTTSEngine();
				setSpeed();
				setEventListener(eventListener);
			}
		}
		else if( PREF_SPEED.equals(event.getProperty()) ) {
			stop();
			setSpeed();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.actf.ai.voice.IVoice#speak(java.lang.String, boolean)
	 */
	public void speak(String text, boolean flush) {
		speak(text,flush,-1);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.actf.ai.voice.IVoice#speak(java.lang.String, boolean, int)
	 */
	public void speak(String text, boolean flush, int index) {
		if( null != ttsEngine ) {
			ttsEngine.speak(text,flush?ITTSEngine.TTSFLAG_FLUSH:ITTSEngine.TTSFLAG_DEFAULT,index);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.actf.ai.voice.IVoice#stop()
	 */
	public void stop() {
		if( null != ttsEngine ) {
			ttsEngine.stop();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.actf.ai.voice.IVoice#getSpeed()
	 */
	public int getSpeed() {
		if( null != ttsEngine ) {
			return ttsEngine.getSpeed();
		}
		return -1;
	}

	/**
	 * Set the settings to the default speed of the TTS engine. 
	 */
	public void setSpeed() {
		setSpeed(VoiceUtil.getDefaultSpeed());
	}
    
	/* (non-Javadoc)
	 * @see org.eclipse.actf.ai.voice.IVoice#setSpeed(int)
	 */
	public void setSpeed(int speed) {
		if( null != ttsEngine ) {
			ttsEngine.setSpeed(speed);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.actf.ai.voice.IVoice#setEventListener(org.eclipse.actf.ai.voice.IVoiceEventListener)
	 */
	public void setEventListener(IVoiceEventListener eventListener) {
		this.eventListener = eventListener;
		if( null != ttsEngine ) {
			ttsEngine.setEventListener(eventListener);
		}
	}
	
	/**
	 * Prepare to dispose the object.
	 */
	public void dispose() {
		if( null != ttsEngine ) {
			stop();
			ttsEngine.dispose();
			ttsEngine = null;
		}
	}
    
    /* (non-Javadoc)
     * @see org.eclipse.actf.ai.voice.IVoice#getTTSEngine()
     */
    public ITTSEngine getTTSEngine() {
        return ttsEngine;
    }
}
