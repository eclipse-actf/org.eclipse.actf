/*******************************************************************************
 * Copyright (c) 2006, 2016 IBM Corporation and Others
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

import org.eclipse.actf.ui.preferences.GroupFieldEditorPreferencePage;
import org.eclipse.actf.ui.preferences.ScaleFieldEditorWithValue;
import org.eclipse.actf.visualization.engines.blind.ParamBlind;
import org.eclipse.actf.visualization.internal.engines.blind.BlindVizEnginePlugin;
import org.eclipse.actf.visualization.internal.engines.blind.Messages;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class BlindPreferencePage extends GroupFieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String ID = BlindPreferencePage.class.getName();

	// private RadioGroupFieldEditor modeRadio;

	private Group visualizationSetting;

	public BlindPreferencePage() {
		super();
		setPreferenceStore(BlindVizEnginePlugin.getDefault().getPreferenceStore());
	}

	public void init(IWorkbench workbench) {
	}

	private void createInappropriateAltPart(Composite parent) {
		Group inAppropriateAltGroup = createFieldGroup(Messages.DialogSettingBlind_NG_Word___Wrong_Text_5);

		Button editListButton = new Button(inAppropriateAltGroup, SWT.PUSH);
		editListButton.setText(Messages.DialogSettingBlind_NG_Word_Wrong_Text_Edit____25);
		editListButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				editNgwordWrongtxt();
			}
		});
	}

	private void createLanguagePart(Composite parent) {

		addField(new RadioGroupFieldEditor(IBlindPreferenceConstants.BLIND_LANG, Messages.DialogSettingBlind_Language_4,
				1, new String[][] { { Messages.DialogSettingBlind_English_15, IBlindPreferenceConstants.LANG_EN },
						{ Messages.DialogSettingBlind_Japanese_16, IBlindPreferenceConstants.LANG_JA }
				// TODO
				// ,
				// { Messages.DialogSettingBlind_SimpliedChinese_17,
				// IBlindPreferenceConstants.LANG_ZH }
		}, parent));
	}

	private void editNgwordWrongtxt() {
		EditNGWordDialog dlg = new EditNGWordDialog(getShell());
		dlg.open();
	}

	protected void createFieldEditors() {

		Composite parent = getFieldEditorParent();

		// modeRadio = new RadioGroupFieldEditor(
		// IBlindPreferenceConstants.BLIND_MODE,
		// Messages.DialogSettingBlind_Visualization_mode_3,
		// 1,
		// new String[][] {
		// {
		// Messages.DialogSettingBlind_Layout_mode_9,
		// IBlindPreferenceConstants.BLIND_LAYOUT_MODE },
		// {
		// Messages.DialogSettingBlind_Voice_browser_output_mode_8,
		// IBlindPreferenceConstants.BLIND_BROWSER_MODE } },
		// parent);
		//
		// addField(modeRadio);

		visualizationSetting = createFieldGroup(Messages.DialogSettingBlind_LayoutModeSetting);

		addField(new ColorFieldEditor(IBlindPreferenceConstants.BLIND_MAX_TIME_COLOR,
				Messages.DialogSettingBlind_Color_for_maximum_time_19, visualizationSetting));
		ScaleFieldEditorWithValue maxTime = new ScaleFieldEditorWithValue(
				IBlindPreferenceConstants.BLIND_MAX_TIME_SECOND, Messages.DialogSettingBlind_Maximum_time_17,
				visualizationSetting, 30, 180, 5, 30);
		addField(maxTime);

		addField(new ColorFieldEditor(IBlindPreferenceConstants.BLIND_TABLE_BORDER_COLOR,
				Messages.DialogSettingBlind_Tabel_border_24, visualizationSetting));

		addField(new ColorFieldEditor(IBlindPreferenceConstants.BLIND_HEADING_TAGS_COLOR,
				Messages.DialogSettingBlind_Heading_tags_21, visualizationSetting));
		addField(new ColorFieldEditor(IBlindPreferenceConstants.BLIND_TABLE_HEADER_COLOR,
				Messages.DialogSettingBlind_Table_headers_20, visualizationSetting));
		addField(new ColorFieldEditor(IBlindPreferenceConstants.BLIND_INPUT_TAGS_COLOR,
				Messages.DialogSettingBlind_Input_tags_22, visualizationSetting));
		addField(new ColorFieldEditor(IBlindPreferenceConstants.BLIND_LABEL_TAGS_COLOR,
				Messages.DialogSettingBlind_Label_tags_23, visualizationSetting));
		addField(new ColorFieldEditor(IBlindPreferenceConstants.BLIND_CAPTION_COLOR,
				Messages.DialogSettingBlind_Caption_tags_25, visualizationSetting));

		createInappropriateAltPart(parent);

		if (Locale.getDefault().equals(Locale.JAPAN)) {
			// TODO automatic encoding detect in visualization
			createLanguagePart(parent);
		}

		boolean isLayoutMode = IBlindPreferenceConstants.BLIND_LAYOUT_MODE
				.equals(getPreferenceStore().getString(IBlindPreferenceConstants.BLIND_MODE));
		visualizationSetting.setEnabled(isLayoutMode);
		visualizationSetting.setVisible(isLayoutMode);

	}

	@Override
	protected void performDefaults() {
		super.performDefaults();

		boolean isLayoutMode = IBlindPreferenceConstants.BLIND_LAYOUT_MODE
				.equals(getPreferenceStore().getString(IBlindPreferenceConstants.BLIND_MODE));
		visualizationSetting.setEnabled(isLayoutMode);
		visualizationSetting.setVisible(isLayoutMode);
	}

	// @Override
	// public void propertyChange(PropertyChangeEvent event) {
	// super.propertyChange(event);
	//
	// if (event.getSource() == modeRadio) {
	// if (!event.getOldValue().equals(event.getNewValue())) {
	// boolean isLayoutMode = IBlindPreferenceConstants.BLIND_LAYOUT_MODE
	// .equals(event.getNewValue());
	// visualizationSetting.setEnabled(isLayoutMode);
	// visualizationSetting.setVisible(isLayoutMode);
	// }
	// }
	// }

	@Override
	public boolean performOk() {
		boolean result = super.performOk();

		ParamBlind.refresh();

		return result;
	}

}
