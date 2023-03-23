/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.internal.ui.editor.dialogs;

import java.util.Map;

import org.eclipse.actf.model.ui.util.ModelServiceMessages;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class FavoritesAddDialog extends Dialog {

	private Map<String, String> _map;

	private String _name;

	private Text _nameText;

	public FavoritesAddDialog(Shell shell, Map<String, String> map, String name) {
		super(shell);
		this._map = map;
		this._name = name;
	};

	public String getName() {
		return _name;
	}

	protected void okPressed() {
		_name = _nameText.getText();
		if (_name.equals("")) { //$NON-NLS-1$
			MessageDialog.openError(getShell(), "Error", ModelServiceMessages.DialogAddFavorite_Text_Empty_Alert); //$NON-NLS-1$
			return;
		} else if (_map.containsKey(_name)) {
			MessageDialog.openError(getShell(), "Error", ModelServiceMessages.DialogAddFavorite_Text_Exist_Alert); //$NON-NLS-1$
			return;
		}

		super.okPressed();
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 2;
		composite.setLayout(gridLayout1);

		Label label = new Label(composite, SWT.NONE);
		label.setText(ModelServiceMessages.DialogAddFavorite_Name_Label);

		this._nameText = new Text(composite, SWT.BORDER);
		this._nameText.setText(_name);
		GridData gridData = new GridData();
		gridData.widthHint = 250;
		this._nameText.setLayoutData(gridData);

		return (composite);
	};

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(ModelServiceMessages.DialogAddFavorite_Title);
		newShell.setLocation(200, 200);
	}
}
