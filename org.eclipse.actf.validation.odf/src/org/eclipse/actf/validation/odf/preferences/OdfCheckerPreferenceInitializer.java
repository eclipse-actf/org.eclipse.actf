/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tatsuya ISHIHARA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.validation.odf.preferences;

import org.eclipse.actf.validation.odf.OdfCheckerPlugin;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;



public class OdfCheckerPreferenceInitializer extends AbstractPreferenceInitializer {
    public void initializeDefaultPreferences() {
        IPreferenceStore store = OdfCheckerPlugin.getDefault().getPreferenceStore();
        store.setDefault(OdfCheckerPreferenceConstants.ODFPLUGIN_DETECT_VERSION, true);
    }
}

