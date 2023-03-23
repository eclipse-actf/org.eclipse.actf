/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.engines.blind.ui.preferences;

import java.util.TreeSet;

import org.eclipse.actf.ui.util.IDialogConstants;
import org.eclipse.actf.visualization.engines.blind.TextChecker;
import org.eclipse.actf.visualization.internal.engines.blind.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class EditNGWordDialog {

	// TODO use dialog

	private static final String DIALOG_TITLE = Messages.AltDialog_TITLE;

	private Shell shell;

	private int iReturnCode = 0;

	private String strFilter;

	private Table ngWordTable;

	private TextChecker textChecker = TextChecker.getInstance();

	public EditNGWordDialog(Shell _shell) {
		shell = new Shell(_shell, SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
		shell.setLayout(new GridLayout());

	}

	private boolean setNewValue() {
		TreeSet<String> newSet = new TreeSet<String>();
		for (TableItem item : ngWordTable.getItems()) {
			String tmpS = item.getText();
			if (tmpS != null && (tmpS = tmpS.trim()).length() > 0) {
				newSet.add(tmpS);
			}
		}
		textChecker.setInappropriateAltSet(newSet);

		return true;
	}

	private void createButtonControls() {
		Composite composite = new Composite(shell, SWT.NULL);

		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_END
				| GridData.VERTICAL_ALIGN_END);
		gridData.heightHint = 50;
		composite.setLayoutData(gridData);

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.horizontalSpacing = 20;
		layout.marginWidth = 20;
		layout.marginHeight = 20;
		composite.setLayout(layout);

		Button okButton = new Button(composite, SWT.PUSH);
		okButton.setText(IDialogConstants.OK); 
		okButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (setNewValue() == false)
					return;
				iReturnCode = 1;
				shell.close();
			}
		});

		Button cancelButton = new Button(composite, SWT.PUSH);
		cancelButton.setText(IDialogConstants.CANCEL); 
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				iReturnCode = 0;
				shell.close();
			}
		});

		shell.setDefaultButton(okButton);
	}

	private void createSettingControls() {
		GridLayout gridLayout1;

		Composite composite = new Composite(shell, SWT.NULL);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 2;
		composite.setLayout(gridLayout1);

		ngWordTable = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION
				| SWT.H_SCROLL | SWT.V_SCROLL);

		GridData gridData1 = new GridData(GridData.FILL_BOTH);
		gridData1.heightHint = 150;
		gridData1.horizontalSpan = 2;
		ngWordTable.setLayoutData(gridData1);
		ngWordTable.setHeaderVisible(true);
		ngWordTable.setLinesVisible(true);

		TableColumn col = new TableColumn(ngWordTable, SWT.NONE);
		col.setText(Messages.AltDialog_Column_Name); 
		col.setWidth(200);

		for (String value : textChecker.getInappropriateALTSet()) {
			TableItem item = new TableItem(ngWordTable, SWT.NONE);
			item.setText(value);
		}

		final Text text = new Text(composite, SWT.BORDER);
		gridData1 = new GridData(GridData.FILL_HORIZONTAL);
		gridData1.horizontalSpan = 2;
		text.setLayoutData(gridData1);

		Button addButton = new Button(composite, SWT.PUSH);
		addButton.setText(IDialogConstants.ADD);
		addButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent arg0) {
				String str = text.getText().toLowerCase();
				if (str.equals("")) { //$NON-NLS-1$
					popupMessage(Messages.AltDialog_MSG_No_String); 
					return;
				}
				int count = ngWordTable.getItemCount();
				for (int i = 0; i < count; i++) {
					if (ngWordTable.getItem(i).getText().equals(str)) {
						popupMessage(Messages.AltDialog_MSG_Existed); 
						return;
					}
				}
				TableItem item = new TableItem(ngWordTable, SWT.NONE);
				item.setText(str);
			}
		});
		Button delButton = new Button(composite, SWT.PUSH);
		delButton.setText(IDialogConstants.DELETE);
		delButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				if (ngWordTable.getSelectionCount() > 0) {
					ngWordTable.getSelection()[0].dispose();
				}
			}
		});


	}

	private void popupMessage(String str) {
		MessageBox msgBox = new MessageBox(shell, SWT.OK);
		msgBox.setMessage(str);
		msgBox.open();
	}

	public int open() {
		shell.setText(DIALOG_TITLE);
		// shell.setSize(400, 300);

		createSettingControls();

		createButtonControls();

		shell.setLocation(200, 200);
		shell.pack();
		shell.open();

		Display display = shell.getDisplay();
		while (!shell.isDisposed() || !display.readAndDispatch()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		return iReturnCode;
	}

	/**
	 * @return
	 */
	public String getStrFilter() {
		return strFilter;
	}

}
