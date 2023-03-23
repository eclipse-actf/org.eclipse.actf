/*******************************************************************************
* Copyright (c) 2004, 2015 IBM Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*  IBM Corporation - initial API and implementation
*  Kentarou FUKUDA - [232278]
*******************************************************************************/ 

package org.eclipse.actf.ui.preferences;

import java.util.Map;

import org.eclipse.actf.ui.ActfUIPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


/**
 * This class represents a preference page that
 * is contravenuted to the Preferences dialog. By
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */
public class RootPreferencePage extends AbstractBasePreferencePage
	implements IWorkbenchPreferencePage
{

	//This string contains the ACTF title
	private static final String ACTF_TITLE_STRING = "Accessibility Tools Framework (ACTF).\r\n\r\n"; //$NON-NLS-1$

	// Text Control to hold ACTF information.
	private static Text actfDescription;

	public RootPreferencePage () {
		super();
		noDefaultAndApplyButton();
		setPreferenceStore(ActfUIPlugin.getDefault().getPreferenceStore());
	}

	protected Control createContents (Composite parent) {
		Composite descComposite = new Composite(parent, SWT.NONE);
		GridLayout descLayout = new GridLayout(2, false);
		descComposite.setLayout(descLayout);
		//duplicated message
//		GridData labelGd = new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false);
//		Label descriptionLabel = new Label(descComposite, SWT.LEFT);
//		descriptionLabel.setText("ACTF Information");
//		descriptionLabel.setLayoutData(labelGd);
		actfDescription = new Text(descComposite, (SWT.READ_ONLY | SWT.WRAP | SWT.MULTI));
		String actfDescriptionString = ACTF_TITLE_STRING;
		actfDescription.setText(actfDescriptionString);
		GridData descriptionGd = new GridData();
		actfDescription.setLayoutData(descriptionGd);
		return (parent);
	}

	/**
	 * @deprecated will be removed in 1.4.0
	 */
	@SuppressWarnings("rawtypes")
	public static Map getPreferences () {
		return null;
	}

	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 * 
	 * @deprecated will be removed in 1.4.0
	 */
	public void createFieldEditors () {
	}

	public void init (IWorkbench workbench) {
	}
}
