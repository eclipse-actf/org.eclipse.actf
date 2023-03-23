/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.ui.preferences;

import org.eclipse.jface.preference.ScaleFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;

/**
 * A field editor for an integer type preference. It obtain current Scale value
 * from {@link IScaleValueLabelProvider} and show the value as a Label.
 * 
 * @see ScaleFieldEditor
 */
public class ScaleFieldEditorWithValue extends ScaleFieldEditor {

	private Label valueLabel;

	private Label minLabel;

	private Label maxLabel;

	private IScaleValueLabelProvider scaleValueLabelProvider;

	private class DefaultScaleValueLabelProvider implements
			IScaleValueLabelProvider {
		public String getScaleValueText(Scale scale) {
			return Integer.toString(scale.getSelection());
		}
	}

	/**
	 * Create a new scale field editor with min/max/current value Labels. The
	 * current Scale value is converted to String by using
	 * {@link IScaleValueLabelProvider}. Default
	 * {@link IScaleValueLabelProvider} will convert current value by using
	 * Integer.toString().
	 * 
	 * @param name
	 *            the name of the preference this field editor works on
	 * @param labelText
	 *            the label text of the field editor
	 * @param parent
	 *            the parent of the field editor's control
	 */
	public ScaleFieldEditorWithValue(String name, String labelText,
			Composite parent) {
		super(name, labelText, parent);
		scaleValueLabelProvider = new DefaultScaleValueLabelProvider();
	}

	/**
	 * Create a new scale field editor with min/max/current value Labels. The
	 * current Scale value is converted to String by using
	 * {@link IScaleValueLabelProvider}. Default
	 * {@link IScaleValueLabelProvider} will convert current value by using
	 * Integer.toString().
	 * 
	 * @param name
	 *            the name of the preference this field editor works on
	 * @param labelText
	 *            the label text of the field editor
	 * @param parent
	 *            the parent of the field editor's control
	 * @param min
	 *            the value used for Scale.setMinimum(int).
	 * @param max
	 *            the value used for Scale.setMaximum(int).
	 * @param increment
	 *            the value used for Scale.setIncrement(int).
	 * @param pageIncrement
	 *            the value used for Scale.setPageIncrement(int).
	 */
	public ScaleFieldEditorWithValue(String name, String labelText,
			Composite parent, int min, int max, int increment, int pageIncrement) {
		super(name, labelText, parent, min, max, increment, pageIncrement);
		minLabel.setText(Integer.toString(min));
		maxLabel.setText(Integer.toString(max));
		scaleValueLabelProvider = new DefaultScaleValueLabelProvider();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.ScaleFieldEditor#doFillIntoGrid(org.eclipse.swt.widgets.Composite,
	 *      int)
	 */
	@Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		super.doFillIntoGrid(parent, numColumns - 1);
		valueLabel = new Label(parent, SWT.RIGHT);
		GridData gridData = new GridData();
		gridData.widthHint = 50;
		valueLabel.setLayoutData(gridData);

		scale.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				valueLabel.setText(scaleValueLabelProvider
						.getScaleValueText(scale));
			}
		});

		(new Label(parent, SWT.NONE)).setLayoutData(new GridData());

		minLabel = new Label(parent, SWT.LEFT);
		gridData = new GridData();
		gridData.widthHint = 50;
		minLabel.setLayoutData(gridData);

		maxLabel = new Label(parent, SWT.RIGHT);
		gridData = new GridData();
		gridData.widthHint = 50;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = SWT.RIGHT;
		maxLabel.setLayoutData(gridData);

		(new Label(parent, SWT.NONE)).setLayoutData(new GridData());

	}

	@Override
	protected void adjustForNumColumns(int numColumns) {
		super.adjustForNumColumns(numColumns - 1);
		((GridData) maxLabel.getLayoutData()).horizontalSpan = numColumns - 3;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.ScaleFieldEditor#getNumberOfControls()
	 */
	@Override
	public int getNumberOfControls() {
		return super.getNumberOfControls() + 2;
	}

	@Override
	protected void doLoad() {
		super.doLoad();
		valueLabel.setText(scaleValueLabelProvider.getScaleValueText(scale));
	}

	@Override
	protected void doLoadDefault() {
		super.doLoadDefault();
		valueLabel.setText(scaleValueLabelProvider.getScaleValueText(scale));
	}

	protected Label getMaxLabel() {
		return maxLabel;
	}

	protected Label getMinLabel() {
		return minLabel;
	}

	protected Label getValueLabel() {
		return valueLabel;
	}

	/**
	 * Set new {@link IScaleValueLabelProvider} to be used to obtain current
	 * Scale value.
	 * 
	 * @param scaleValueLabelProvider
	 *            target {@link IScaleValueLabelProvider}
	 */
	public void setScaleValueLabelProvider(
			IScaleValueLabelProvider scaleValueLabelProvider) {
		if (scaleValueLabelProvider != null) {
			this.scaleValueLabelProvider = scaleValueLabelProvider;
		}
	}

}
