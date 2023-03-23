/*******************************************************************************
 * Copyright (c) 2010,2011 Ministry of Internal Affairs and Communications (MIC).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Yasuharu GOTOU (MIC) - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.examples.michecker.ui.actions;

import java.util.Vector;

import org.eclipse.actf.examples.michecker.caption.CaptionData;
import org.eclipse.actf.examples.michecker.internal.Messages;
import org.eclipse.actf.examples.michecker.smil.SMILReader;
import org.eclipse.actf.examples.michecker.ui.dialogs.URLOpenDialog;
import org.eclipse.actf.examples.michecker.views.CaptionView;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;

public class OpenSmil implements IWorkbenchWindowActionDelegate {

	IWorkbenchWindow window;
	Shell shell;

	public void run(IAction action) {
		URLOpenDialog openDialog = new URLOpenDialog(shell);
		String openFile = null;
		if (openDialog.open() == 1) {
			openFile = openDialog.getUrl();
		}

		if (openFile != null && !openFile.equals("")) { //$NON-NLS-1$
			System.out.println(openFile);
			SMILReader smilReader = new SMILReader();
			boolean flag = smilReader.read(openFile);
			if (flag) {
				Vector<CaptionData> capV = smilReader.getCaptions();

				try {
					IViewPart viewPart = window.getActivePage().showView(
							CaptionView.ID_SMILVIEW);
					((CaptionView)viewPart).setCaptionData(capV);
				} catch (PartInitException pie) {
					pie.printStackTrace();
				}

			} else {
				MessageDialog.openInformation(shell, Messages.Error, openFile
						+ " " + Messages.Not_supported);
			}
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void init(IWorkbenchWindow window) {
		this.window = window;
		this.shell = window.getShell();
	}

}
