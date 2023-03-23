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

package org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class PopupMenu {
	private Shell shell;

	private String returnCode;

	private String[] itemName;

	private boolean[] enableItem;

	// TODO replace

	public PopupMenu(Shell _shell, String[] itemName, boolean[] enable) {
		shell = new Shell(_shell);
		shell.setLayout(new GridLayout());
		this.itemName = itemName;
		this.enableItem = enable;
	}

	public String open() {
		Menu popupMenu = new Menu(shell);

		MenuItem[] item = new MenuItem[itemName.length];

		for (int i = 0; i < itemName.length; i++) {
			item[i] = new MenuItem(popupMenu, SWT.PUSH);
			item[i].setText(itemName[i]);
			item[i].addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					MenuItem mItem = (MenuItem) event.getSource();
					returnCode = mItem.getText();
					shell.close();
				}
			});
			if (enableItem[i]) {
				item[i].setEnabled(true);
			} else {
				item[i].setEnabled(false);
			}
		}

		popupMenu.setVisible(true);

		Display display = shell.getDisplay();
		while (!shell.isDisposed() || !display.readAndDispatch()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		return returnCode;
	}

}
