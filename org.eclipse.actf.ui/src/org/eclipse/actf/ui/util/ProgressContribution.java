/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Daisuke SATO - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.ui.util;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.StatusLineLayoutData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;

/**
 * A contribution item implementation for adding ProgressBar commonly used in
 * ACTF.
 * 
 * @see ControlContribution
 * @see ProgressBar
 */
public class ProgressContribution extends ControlContribution {

	/**
	 * Creates a control contribution item with the given id.
	 * 
	 * @param id
	 */
	public ProgressContribution(String id) {
		super(id);
	}

	/**
	 * Progress Contribution ID to share the contribution within ACTF
	 */
	public static final String PROGRESS_CONTRIBUTION_ID = "actf.progress"; //$NON-NLS-1$

	private ProgressBar progressBar;

	private Label label;

	private String text;

	private int value;

	protected Control createControl(Composite parent) {
		StatusLineLayoutData data = new StatusLineLayoutData();
		data.heightHint = 12;
		Composite c = new Composite(parent, SWT.NONE);
		c.setLayoutData(data);

		RowLayout rl = new RowLayout();
		rl.wrap = false;
		c.setLayout(rl);

		new Label(c, SWT.SEPARATOR);

		label = new Label(c, SWT.NONE);
		if (text != null)
			label.setText(text);

		progressBar = new ProgressBar(c, SWT.HORIZONTAL | SWT.SMOOTH);
		RowData r = new RowData();
		r.width = 150;
		r.height = 16;
		progressBar.setLayoutData(r);
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		progressBar.setSelection(value);

		return c;
	}

	/**
	 * Set text as Label of contribution item
	 * @param text target String
	 */
	public void setText(String text) {
		this.text = text;
		if(text != null){
			label.setText(text);
		}else{
			label.setText(""); //$NON-NLS-1$
		}
	}

	/**
	 * Set percentage of progress
	 * @param percent target percentage value
	 */
	public void setValue(int percent) {
		value = percent;
		if (progressBar != null) {
			progressBar.setSelection(percent);
			progressBar.redraw();
		}
	}
}
