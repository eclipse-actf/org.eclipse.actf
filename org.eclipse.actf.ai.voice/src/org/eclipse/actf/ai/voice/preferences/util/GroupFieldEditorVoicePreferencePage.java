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
package org.eclipse.actf.ai.voice.preferences.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;

/**
 * GroupFieldEditorVoicePreferencePage is a template of the preference page of
 * TTS engine plug-ins.
 */
public class GroupFieldEditorVoicePreferencePage extends
		FieldEditorPreferencePage {

	private List<Group> groups = new ArrayList<Group>();

	protected GroupFieldEditorVoicePreferencePage() {
		super(GRID);
	}

	protected void createFieldEditors() {
	}

	protected Group createFieldGroup(String name) {
		Group group = new Group(getFieldEditorParent(), SWT.NONE);
		if (null != name) {
			group.setText(name);
		}
		GridLayout layout = new GridLayout();
		group.setLayout(layout);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		group.setLayoutData(data);
		groups.add(group);
		return group;
	}

	protected void adjustGridLayout() {
		super.adjustGridLayout();
		int numColumns = ((GridLayout) getFieldEditorParent().getLayout()).numColumns;
		for (int i = 0; i < groups.size(); i++) {
			Group group = groups.get(i);
			GridLayout layout = (GridLayout) group.getLayout();
			GridData data = (GridData) group.getLayoutData();
			layout.numColumns = numColumns;
			layout.marginWidth = 5;
			layout.marginHeight = 5;
			data.horizontalSpan = numColumns;
		}
	}
}
