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

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

/**
 * ComboFieldEditor has a combobox.
 */
public class ComboFieldEditor extends FieldEditor {

	Combo comboField;
	private String[][] labelsAndValues;
	protected String value;

	protected ComboFieldEditor(String name, String labelText,
			String[][] labelsAndValues, Composite parent) {
		init(name, labelText);
		initLabelsAndValues(labelsAndValues);
		createControl(parent);
		setEnabled(comboField.getItemCount() > 0, parent);
	}

	protected void initLabelsAndValues(String[][] labelsAndValues) {
		assert checkArray(labelsAndValues) == true;
		this.labelsAndValues = labelsAndValues;
	}

	protected void adjustForNumColumns(int numColumns) {
		GridData gd = (GridData) comboField.getLayoutData();
		gd.horizontalSpan = numColumns - 1;
		gd.grabExcessHorizontalSpace = gd.horizontalSpan == 1;
	}

	protected void doFillIntoGrid(Composite parent, int numColumns) {
		getLabelControl(parent);

		comboField = getComboControl(parent);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = numColumns - 1;
		comboField.setLayoutData(gd);
	}

	protected void doLoad() {
		updateValue(getPreferenceStore().getString(getPreferenceName()));
	}

	protected void doLoadDefault() {
		updateValue(getPreferenceStore().getDefaultString(getPreferenceName()));
	}

	protected void doStore() {
		if (null != value) {
			getPreferenceStore().setValue(getPreferenceName(), value);
		} else {
			getPreferenceStore().setToDefault(getPreferenceName());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#getNumberOfControls()
	 */
	public int getNumberOfControls() {
		return 2;
	}

	private boolean checkArray(String[][] table) {
		if (table == null)
			return false;
		for (int i = 0; i < table.length; i++) {
			String[] array = table[i];
			if (array == null || array.length != 2)
				return false;
		}
		return true;
	}

	private void updateValue(String selectedValue) {
		if (null != selectedValue) {
			for (int i = 0; i < labelsAndValues.length; i++) {
				if (labelsAndValues[i][1].equals(selectedValue)) {
					comboField.select(i);
					this.value = selectedValue;
					return;
				}
			}
		}
		if (labelsAndValues.length > 0) {
			comboField.select(0);
			this.value = labelsAndValues[0][1];
		}
		return;
	}

	/**
	 * @return The combobox object.
	 */
	public Combo getComboControl() {
		return comboField;
	}

	protected Combo getComboControl(Composite parent) {
		if (null == comboField) {
			comboField = new Combo(parent, SWT.DROP_DOWN | SWT.SIMPLE
					| SWT.BORDER | SWT.READ_ONLY);
			comboField.setFont(parent.getFont());
			if (null != labelsAndValues) {
				for (int i = 0; i < labelsAndValues.length; i++) {
					comboField.add(labelsAndValues[i][0]);
				}
			}
			comboField.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					int index = comboField.getSelectionIndex();
					if (index >= 0) {
						String oldValue = value;
						value = labelsAndValues[index][1];
						setPresentsDefaultValue(false);
						fireValueChanged(VALUE, oldValue, value);
					}
				}
			});
			comboField.addDisposeListener(new DisposeListener() {
				public void widgetDisposed(DisposeEvent event) {
					comboField = null;
				}
			});
		} else {
			checkParent(comboField, parent);
		}
		return comboField;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#setEnabled(boolean,
	 *      org.eclipse.swt.widgets.Composite)
	 */
	public void setEnabled(boolean enabled, Composite parent) {
		super.setEnabled(enabled, parent);
		getComboControl(parent).setEnabled(enabled);
	}
}
