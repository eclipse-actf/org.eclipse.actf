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

import java.util.Iterator;
import java.util.Map;

import org.eclipse.actf.model.ui.util.ModelServiceMessages;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class FavoritesArrangeDialog extends Dialog {

	private Map<String, String> _favoritesMap;
	private boolean modified = false;

	public FavoritesArrangeDialog(Shell shell, Map<String, String> favoritesMap) {
		super(shell);
		this._favoritesMap = favoritesMap;
	};

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 2;
		composite.setLayout(gridLayout1);

		final Table table = new Table(composite, SWT.MULTI | SWT.FULL_SELECTION
				| SWT.BORDER);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		gridData.heightHint = 100;
		table.setLayoutData(gridData);

		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		String[] cols = {
				ModelServiceMessages.DialogConst_Name_Label,
				"URL: " }; //$NON-NLS-1$
		TableColumn col = new TableColumn(table, SWT.BEGINNING);
		col.setText(cols[0]);
		col.setWidth(200);
		col = new TableColumn(table, SWT.BEGINNING);
		col.setText(cols[1]);
		col.setWidth(300);

		Iterator<String> keyIt = _favoritesMap.keySet().iterator();
		while (keyIt.hasNext()) {
			String name = keyIt.next();
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, name);
			item.setText(1, _favoritesMap.get(name));
		}

		Button button = new Button(composite, SWT.PUSH);
		button.setText(ModelServiceMessages.DialogArrangeFavorite_Modify_Name);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				if (table.getSelectionCount() > 0) {
					String name = table.getSelection()[0].getText(0);
					FavoritesAddDialog dlg = new FavoritesAddDialog(getShell(),
							_favoritesMap, name);
					int ret = dlg.open();
					String newName = dlg.getName();
					if (ret == IDialogConstants.OK_ID && !newName.equals(name)) {
						table.getSelection()[0].setText(0, newName);
						_favoritesMap.put(newName, _favoritesMap.get(name));
						_favoritesMap.remove(name);
						modified = true;
					}
				}
			}
		});

		button = new Button(composite, SWT.PUSH);
		button.setText(ModelServiceMessages.DialogArrangeFavorite_Delete);
		button.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent arg0) {
				if (table.getSelectionCount() > 0) {
					TableItem[] items = table.getSelection();
					for (int i = 0; i < items.length; i++) {
						_favoritesMap.remove(items[i].getText(0));
					}
					table.remove(table.getSelectionIndices());
					modified = true;
				}
			}

		});

		return composite;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(ModelServiceMessages.DialogArrangeFavorite_Title);
	}

	public Map<String, String> getFavoritesMap() {
		return this._favoritesMap;
	}

	public boolean isModified() {
		return modified;
	}
}
