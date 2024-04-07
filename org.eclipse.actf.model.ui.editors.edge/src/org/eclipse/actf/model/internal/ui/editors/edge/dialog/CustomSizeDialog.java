/*******************************************************************************
 * Copyright (c) 2024 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.model.internal.ui.editors.edge.dialog;

import org.eclipse.actf.model.internal.ui.editors.edge.Messages;
import org.eclipse.actf.ui.util.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class CustomSizeDialog {

	private Shell shell;

	private int returnCode = 0;

	private Text widthText, heightText;
	private int width, height;

	public CustomSizeDialog(Shell shell, int width, int height) {
		this.shell = new Shell(shell, SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
		this.shell.setLayout(new GridLayout());
		this.width = validateSize(width);
		this.height = validateSize(height);
	}

	private void createButtonControls() {
		Composite composite = new Composite(this.shell, SWT.NULL);

		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_END);
		gridData.heightHint = 100;
		composite.setLayoutData(gridData);

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.horizontalSpacing = 20;
		layout.marginWidth = 20;
		layout.marginHeight = 10;
		composite.setLayout(layout);

		Button okButton = new Button(composite, SWT.PUSH);
		okButton.setText(IDialogConstants.OK);
		okButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				returnCode = 1;

				width = 100;
				try {
					width = Integer.parseInt(widthText.getText());
				} catch (Exception ex) {
				}

				height = 100;
				try {
					height = Integer.parseInt(heightText.getText());
				} catch (Exception ex) {
				}

				shell.close();
			}
		});

		this.shell.setDefaultButton(okButton);
	}

	private int validateSize(int target) {
		if (target > 2048) {
			target = 2048;
		} else if (target < 256) {
			target = 256;
		}
		return target;
	}

	private void createSettingControls() {
		GridLayout gridLayout1;

		Composite composite = new Composite(shell, SWT.NULL);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 5;
		composite.setLayout(gridLayout1);

		Label infoLabel = new Label(composite, SWT.NONE);
		infoLabel.setText(Messages.CustomSizeDialog_info);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 5;
		gridData.heightHint = 25;
		infoLabel.setLayoutData(gridData);

		// Width label
		Label label1 = new Label(composite, SWT.NONE);
		label1.setText(Messages.CustomSizeDialog_width);

		widthText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		widthText.setText(Integer.toString(width));
		widthText.addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {
				widthText.selectAll();
			}

			@Override
			public void focusLost(FocusEvent e) {
				try {
					int tmpSize = Integer.parseInt(widthText.getText());
					width = validateSize(tmpSize);
				} catch (Exception ex) {
				}
				widthText.setText(Integer.toString(width));
			}
		});

		gridData = new GridData();
		gridData.widthHint = 50;
		widthText.setLayoutData(gridData);

		// Spacer
		Label label2 = new Label(composite, SWT.NONE);
		label2.setText("     "); //$NON-NLS-1$

		// Height label
		Label label3 = new Label(composite, SWT.NONE);
		label3.setText(Messages.CustomSizeDialog_height);

		heightText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		heightText.setText(Integer.toString(height));
		heightText.addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {
				heightText.selectAll();
			}

			@Override
			public void focusLost(FocusEvent e) {
				try {
					int tmpSize = Integer.parseInt(heightText.getText());
					height = validateSize(tmpSize);
				} catch (Exception ex) {
				}
				heightText.setText(Integer.toString(height));
			}
		});

		gridData = new GridData();
		gridData.widthHint = 50;
		heightText.setLayoutData(gridData);

	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int open() {
		this.shell.setText(Messages.CustomSizeDialog_title);

		createSettingControls();

		createButtonControls();
		this.shell.setSize(350, 150);
		this.shell.open();
		this.shell.setLocation(100, 100);

		Display display = shell.getDisplay();
		while (!shell.isDisposed() || !display.readAndDispatch()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		return this.returnCode;
	}

}
