/*******************************************************************************
 * Copyright (c) 2010, 2011 Ministry of Internal Affairs and Communications (MIC).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Yasuharu GOTOU (MIC) - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.examples.michecker.ui.dialogs;

import org.eclipse.actf.examples.michecker.internal.Messages;
import org.eclipse.actf.ui.util.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class URLOpenDialog {

	private Shell _shell;

	private Text _urlText;

	private String _url = ""; //$NON-NLS-1$

	private int _returnCode = 0;

	// TODO replace (old implementation)

	public URLOpenDialog(Shell shell) {
		this._shell = new Shell(shell, SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
		this._shell.setLayout(new GridLayout());
	}

	private void createButtonControls() {
		Composite composite = new Composite(this._shell, SWT.NULL);

		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_END
				| GridData.VERTICAL_ALIGN_END);
		gridData.heightHint = 50;
		composite.setLayoutData(gridData);

		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.horizontalSpacing = 20;
		layout.marginWidth = 20;
		layout.marginHeight = 10;
		composite.setLayout(layout);

		Button okButton = new Button(composite, SWT.PUSH);
		okButton.setText(IDialogConstants.OK); 
		okButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				_returnCode = 1;
				_url = _urlText.getText();
				_shell.close();
			}
		});

		Button cancelButton = new Button(composite, SWT.PUSH);
		cancelButton.setText(IDialogConstants.CANCEL); 
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				_returnCode = 0;
				_shell.close();
			}
		});

		Button openButton = new Button(composite, SWT.PUSH);
		openButton.setText(IDialogConstants.BROWSE); 
		openButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FileDialog openDialog = new FileDialog(_shell, SWT.OPEN);
				String openFile = openDialog.open();

				if (openFile != null && !openFile.equals("")) { //$NON-NLS-1$
					_urlText.setText(openFile);
				}
			}
		});

		this._shell.setDefaultButton(okButton);
	}

	private void createSettingControls() {
		GridLayout gridLayout1;

		Composite composite = new Composite(_shell, SWT.NULL);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 2;
		composite.setLayout(gridLayout1);

		// information
		Label infoLabel = new Label(composite, SWT.NONE);
		infoLabel.setText(IDialogConstants.OPENFILE_INFO);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		infoLabel.setLayoutData(gridData);

		// URL label
		Label label1 = new Label(composite, SWT.NONE);
		label1.setText("URL: "); //$NON-NLS-1$

		// Create the TextBox
		_urlText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		_urlText.setText(""); //$NON-NLS-1$

		gridData = new GridData();
		gridData.widthHint = 300;
		_urlText.setLayoutData(gridData);
	}

	public int open() {
		this._shell.setText(Messages.DialogOpenURL_Open_URL);

		createSettingControls();

		createButtonControls();
		this._shell.setSize(375, 150);
		this._shell.open();
		this._shell.setLocation(100, 100);

		Display display = _shell.getDisplay();
		while (!_shell.isDisposed() || !display.readAndDispatch()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		return this._returnCode;
	}

	public String getUrl() {
		return this._url;
	}
}
