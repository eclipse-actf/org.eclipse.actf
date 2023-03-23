/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.eval.preferences;

import org.eclipse.actf.util.FileUtils;
import org.eclipse.actf.visualization.internal.eval.Messages;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;


public class NavigabilityWarningDialog extends TitleAreaDialog {

	public static final int ENABLE_ALL = 0;

	public static final int DISABLE_NAVIGABILITY = 1;

	public static final int CONTINUE = 2;

	private Button buttonWCAGall;

	private Button buttonNavOff;

	private Button buttonGoAhead;

	/**
	 * @param arg0
	 */
	public NavigabilityWarningDialog(Shell arg0) {
		super(arg0);
		setShellStyle(SWT.PRIMARY_MODAL | SWT.SHELL_TRIM);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell arg0) {
		super.configureShell(arg0);
		// arg0.setText("Checker Option Warning");
		// arg0.setImage(Images.Excla);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.TitleAreaDialog#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite arg0) {
		Control contents = super.createContents(arg0);
		setMessage(
				Messages.NavigabilityWarningDialog_Message1 + FileUtils.LINE_SEP + Messages.NavigabilityWarningDialog_Message2, IMessageProvider.WARNING); 
		return contents;
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new GridLayout());

		buttonWCAGall = new Button(composite, SWT.RADIO);
		buttonWCAGall.setText(Messages.NavigabilityWarningDialog_EnableWCAG); 
		buttonNavOff = new Button(composite, SWT.RADIO);
		buttonNavOff.setText(Messages.NavigabilityWarningDialog_DisableNav); 
		buttonGoAhead = new Button(composite, SWT.RADIO);
		buttonGoAhead.setText(Messages.NavigabilityWarningDialog_Continue); 

		return composite;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	}

	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			if (buttonWCAGall.getSelection()) {
				setReturnCode(ENABLE_ALL);
			} else if (buttonNavOff.getSelection()) {
				setReturnCode(DISABLE_NAVIGABILITY);
			} else {
				setReturnCode(CONTINUE);
			}

			close();
		}
	}

}
