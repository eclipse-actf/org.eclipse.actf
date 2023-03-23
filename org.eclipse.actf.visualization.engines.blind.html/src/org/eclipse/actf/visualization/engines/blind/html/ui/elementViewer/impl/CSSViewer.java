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

import java.util.Iterator;
import java.util.Set;

import org.eclipse.actf.visualization.internal.engines.blind.html.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class CSSViewer {

	private Composite compositeCss;

	private List cssListCon;

	public CSSViewer(Composite parent) {
		compositeCss = new Composite(parent, SWT.NULL);
		compositeCss.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 1;
		compositeCss.setLayout(gridLayout1);

		cssListCon = new List(compositeCss, SWT.SINGLE | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.BORDER);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		cssListCon.setLayoutData(gridData);

		cssListCon.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent arg0) {
				if (arg0.stateMask == SWT.BUTTON3) {
					if (cssListCon.getSelectionIndex() >= 0)
						openPopupMenu(cssListCon.getSelectionIndex());
				}
			}

			// public void mouseDoubleClick(MouseEvent arg0) {
			// if (cssListCon.getSelectionIndex() >= 0)
			// getAndViewFile(cssListCon.getSelectionIndex());
			// }
		});
	}

	/**
	 * @return
	 */
	public Composite getCompositeCss() {
		return compositeCss;
	}

	public void setCssSet(Set<String> cssSet) {
		cssListCon.removeAll();
		if (cssSet != null) {
			for (Iterator<String> i = cssSet.iterator(); i.hasNext();) {
				cssListCon.add(i.next());
			}
		}
	}

	private void openPopupMenu(int index) {
		String[] itemName = new String[2];
		itemName[0] = Messages.CSSViewer_0;
		itemName[1] = Messages.CSSViewer_1;
		boolean[] enabled = new boolean[2];
		enabled[0] = true;
		enabled[1] = true;
		PopupMenu popupMenu = new PopupMenu(new Shell(), itemName, enabled);
		String strRet = popupMenu.open();
		if (strRet.equals(itemName[0])) {
			Clipboard clipboard = new Clipboard(compositeCss.getDisplay());
			clipboard.setContents(new Object[] { cssListCon.getItem(index) },
					new Transfer[] { TextTransfer.getInstance() });
		} else if (strRet.equals(itemName[1])) {
			getAndViewFile(cssListCon.getSelectionIndex());
		}
	}

	private void getAndViewFile(int index) {
		try {
			PlatformUI.getWorkbench().getHelpSystem().displayHelpResource(
					cssListCon.getItem(index));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
