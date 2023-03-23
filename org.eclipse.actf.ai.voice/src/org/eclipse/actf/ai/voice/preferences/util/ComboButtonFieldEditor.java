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
package org.eclipse.actf.ai.voice.preferences.util;

import org.eclipse.actf.ai.voice.internal.Messages;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * ComboButtonFieldEditor has a combobox with a button.
 */
public class ComboButtonFieldEditor extends ComboFieldEditor {

	private Button testButton;

	private String testButtonText;

	protected ComboButtonFieldEditor(String name, String labelText,
			String[][] labelsAndValues, Composite parent) {
		super(name, labelText, labelsAndValues, parent);
	}

	protected void adjustForNumColumns(int numColumns) {
		super.adjustForNumColumns(numColumns - 1);
	}

	protected void doFillIntoGrid(Composite parent, int numColumns) {
		super.doFillIntoGrid(parent, numColumns - 1);
		testButton = getButtonControl(parent);
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		int widthHint = convertHorizontalDLUsToPixels(testButton,
				IDialogConstants.BUTTON_WIDTH);
		gd.widthHint = Math.max(widthHint, testButton.computeSize(SWT.DEFAULT,
				SWT.DEFAULT, true).x);
		testButton.setLayoutData(gd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.ai.voice.preferences.ComboFieldEditor#getNumberOfControls()
	 */
	public int getNumberOfControls() {
		return super.getNumberOfControls() + 1;
	}

	protected void testPressed(int index) {
	}

	protected Button getButtonControl(Composite parent) {
		if (testButton == null) {
			testButton = new Button(parent, SWT.PUSH);
			if (testButtonText == null)
				testButtonText = Messages.voice_test; 
			testButton.setText(testButtonText);
			testButton.setFont(parent.getFont());
			testButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					testPressed(comboField.getSelectionIndex());
				}
			});
			testButton.addDisposeListener(new DisposeListener() {
				public void widgetDisposed(DisposeEvent event) {
					testButton = null;
				}
			});
		} else {
			checkParent(testButton, parent);
		}
		return testButton;
	}

	/**
	 * @param text
	 *            the text of the button.
	 */
	public void setTestButtonText(String text) {
		assert text != null;
		testButtonText = text;
		if (testButton != null)
			testButton.setText(text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.ai.voice.preferences.ComboFieldEditor#setEnabled(boolean,
	 *      org.eclipse.swt.widgets.Composite)
	 */
	public void setEnabled(boolean enabled, Composite parent) {
		super.setEnabled(enabled, parent);
		if (testButton != null) {
			testButton.setEnabled(enabled);
		}
	}
}
