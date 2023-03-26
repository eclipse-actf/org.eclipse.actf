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
import org.eclipse.actf.validation.odf.internal.Messages;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;



public class OdfCheckerPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public OdfCheckerPreferencePage() {
        super(GRID);
        setPreferenceStore(OdfCheckerPlugin.getDefault().getPreferenceStore());
    }

    public void init(IWorkbench workbench) {
    }

    protected void createFieldEditors() {
        addField(new BooleanFieldEditor(OdfCheckerPreferenceConstants.ODFPLUGIN_DETECT_VERSION, 
                Messages.ODFCheckerPreferencePage_detectVersion,
                getFieldEditorParent()));    
    }
}
