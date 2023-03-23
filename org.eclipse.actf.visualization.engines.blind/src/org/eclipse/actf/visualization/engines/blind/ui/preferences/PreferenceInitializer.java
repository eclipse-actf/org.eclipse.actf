/*******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.engines.blind.ui.preferences;

import java.util.Locale;

import org.eclipse.actf.visualization.internal.engines.blind.BlindVizEnginePlugin;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;




public class PreferenceInitializer extends AbstractPreferenceInitializer {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
     */
    @SuppressWarnings("nls")
	public void initializeDefaultPreferences() {
        IPreferenceStore store = BlindVizEnginePlugin.getDefault().getPreferenceStore();
                
        //TODO
        if(Locale.getDefault().equals(Locale.JAPAN)){
            store.setDefault(IBlindPreferenceConstants.BLIND_LANG, IBlindPreferenceConstants.LANG_JA);
        }else{
            store.setDefault(IBlindPreferenceConstants.BLIND_LANG, IBlindPreferenceConstants.LANG_EN);            
        }
                
        store.setDefault(IBlindPreferenceConstants.BLIND_MODE, IBlindPreferenceConstants.BLIND_LAYOUT_MODE);
        store.setDefault(IBlindPreferenceConstants.BLIND_MAX_TIME_SECOND, 90);
        store.setDefault(IBlindPreferenceConstants.BLIND_MAX_TIME_COLOR, "0,64,64");
        store.setDefault(IBlindPreferenceConstants.BLIND_TABLE_HEADER_COLOR, "153,255,0");
        store.setDefault(IBlindPreferenceConstants.BLIND_HEADING_TAGS_COLOR, "51,204,255");
        store.setDefault(IBlindPreferenceConstants.BLIND_INPUT_TAGS_COLOR, "255,153,0");
        store.setDefault(IBlindPreferenceConstants.BLIND_LABEL_TAGS_COLOR, "255,128,255");
        store.setDefault(IBlindPreferenceConstants.BLIND_TABLE_BORDER_COLOR, "0,0,0");
        store.setDefault(IBlindPreferenceConstants.BLIND_CAPTION_COLOR, "255,255,128");
                     
    }

}
