/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.ai.voice.preferences;

import org.eclipse.actf.ai.tts.TTSRegistry;
import org.eclipse.actf.ai.voice.IVoice;
import org.eclipse.actf.ai.voice.VoiceUtil;
import org.eclipse.actf.ai.voice.internal.Messages;
import org.eclipse.actf.ai.voice.internal.Voice;
import org.eclipse.actf.ai.voice.internal.VoicePlugin;
import org.eclipse.actf.ai.voice.preferences.util.GroupFieldEditorVoicePreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.ScaleFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class VoicePreferencePage extends GroupFieldEditorVoicePreferencePage
		implements IWorkbenchPreferencePage {

	private static final String SAMPLE_TEXT = "Hello. This is test."; //$NON-NLS-1$

	private static IVoice voice = VoiceUtil.getVoice();

	public VoicePreferencePage() {
		super();
		setPreferenceStore(VoicePlugin.getDefault().getPreferenceStore());
		setDescription(Messages.voice_description); 
	}

	public void createFieldEditors() {
		
		final RadioGroupFieldEditor rgfe;
		String[][] labelAndIds = TTSRegistry.getLabelAndIds();
		addField(rgfe = new RadioGroupFieldEditor(IVoice.PREF_ENGINE, Messages.voice_engine, 1, labelAndIds,
				getFieldEditorParent()));
		Composite c = rgfe.getRadioBoxControl(getFieldEditorParent());
		for (int i = 0; i < labelAndIds.length; i++) {
			if (labelAndIds[i][1].length() == 0) {
				c.getChildren()[i].setEnabled(false);
			}
		}

		final ScaleFieldEditor speedEditor;
		addField(speedEditor = new ScaleFieldEditor(IVoice.PREF_SPEED,
				Messages.voice_speed, 
				getFieldEditorParent(), IVoice.SPEED_MIN, IVoice.SPEED_MAX, 5,
				25));

		Composite comp = new Composite(getFieldEditorParent(), SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = layout.marginWidth = 0;
		comp.setLayout(layout);
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
		gd.horizontalSpan = speedEditor.getNumberOfControls();
		comp.setLayoutData(gd);

		Button testButton = new Button(comp, SWT.NONE);
		testButton.setText(Messages.voice_test);
		testButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				voice.setSpeed(speedEditor.getScaleControl().getSelection());
				voice.speak(SAMPLE_TEXT, false);
			}
		});
	}

	@Override
	public boolean performCancel() {
		return super.performCancel();
	}
	
	public void init(IWorkbench workbench) {
	}

	public void dispose() {
		super.dispose();
		((Voice) voice).setSpeed();
	}
}
