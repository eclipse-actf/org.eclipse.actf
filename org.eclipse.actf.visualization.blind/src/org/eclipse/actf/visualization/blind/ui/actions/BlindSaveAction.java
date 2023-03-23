/*******************************************************************************
 * Copyright (c) 2005, 2010 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.blind.ui.actions;

import org.eclipse.actf.ui.util.Messages;
import org.eclipse.actf.visualization.blind.ui.internal.PartControlBlind;
import org.eclipse.actf.visualization.engines.blind.BlindVizResourceUtil;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class BlindSaveAction extends Action {
	private PartControlBlind pcb;
	private boolean saveReport = false;

	public BlindSaveAction(PartControlBlind prb) {
		super(Messages.MenuConst_Save,
				AS_DROP_DOWN_MENU);
		this.pcb = prb;

		setToolTipText(Messages.Tooltip_Save);
		setImageDescriptor(BlindVizResourceUtil
				.getImageDescriptor("icons/ButtonSave.png")); //$NON-NLS-1$

		setMenuCreator(new IMenuCreator() {

			public void dispose() {
			}

			public Menu getMenu(Control parent) {
				Menu menu = new Menu(parent);
				MenuItem item1 = new MenuItem(menu, SWT.CHECK);
				item1.setText(org.eclipse.actf.visualization.blind.ui.internal.Messages.Report);
				item1.setSelection(saveReport);
				item1.addSelectionListener(new SelectionListener() {
					public void widgetSelected(SelectionEvent e) {
						saveReport = !saveReport;
					}

					public void widgetDefaultSelected(SelectionEvent e) {
					}
				});
				return menu;
			}

			public Menu getMenu(Menu parent) {
				return null;
			}
		});

	}

	public void run() {
		pcb.doSave(saveReport);
	}

}
