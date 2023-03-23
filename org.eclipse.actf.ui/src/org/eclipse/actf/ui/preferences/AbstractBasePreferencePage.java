/*******************************************************************************
* Copyright (c) 2004, 2008 IBM Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*  IBM Corporation - initial API and implementation
*******************************************************************************/ 

package org.eclipse.actf.ui.preferences;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public abstract class AbstractBasePreferencePage extends PreferencePage
{

	/**
	 * The field editors, or <code>null</code> if not created yet.
	 */
	// uses child's createContents implemented method
	protected abstract Control createContents (Composite parent);

	public Composite createStandardLayout (Composite parent, int numCols) {
		Composite stdComposite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		// if numCols == 0 this just skip this step altogether
		if (numCols != 0) {
			layout.numColumns = numCols;
		}
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		stdComposite.setLayout(layout);
		GridData gd = null;
		gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalAlignment = SWT.BEGINNING;
		gd.verticalAlignment = SWT.BEGINNING;
		stdComposite.setLayoutData(gd);
		return stdComposite;
	} // end--createStandardLayout

	public Composite createExceptionLayout (Composite parent) {
		Composite exComposite = new Composite(parent, SWT.NONE);
		GridLayout exLayout = new GridLayout(2, false);
		exComposite.setLayout(exLayout);
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.GRAB_HORIZONTAL);
		gd.horizontalSpan = 3;
		exComposite.setLayoutData(gd);
		return exComposite;
	} // end--createExceptionLayout

	public void initializeEditor (PreferencePage prefPage, FieldEditor editor,
									IPreferenceStore preferenceStore) {
		// TODO
		// The following line is a Eclipse 3.1 API only. It is commented out because it breaks Eclipse 3.0.
		// When we no longer support Eclipse 3.0 we should uncomment this line.
		//editor.setPage(prefPage);
		editor.setPreferenceStore(preferenceStore);
		editor.load();
	} // end--initializeEditor

	public void createLineSeparator (Composite parent) {
		// create a line
		Label s0 = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd0 = new GridData(GridData.FILL_HORIZONTAL);
		gd0.horizontalSpan = 2;
		s0.setLayoutData(gd0);
	} // end--createLineSeparator
} // end--AbstractBasePreferencePage
