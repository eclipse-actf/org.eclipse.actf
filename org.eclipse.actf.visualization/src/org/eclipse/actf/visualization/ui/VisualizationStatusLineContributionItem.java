/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Norimasa HAYASHIDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.ui;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.StatusLineLayoutData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Status line contribution item commonly used in visualization components<br>
 * 
 * example:
 * ViewPart.getViewSite().getActionBars().getStatusLineManager().add(new
 * VisualizationStatusLineContributionItem(id));
 */
public class VisualizationStatusLineContributionItem extends ContributionItem {

	/**
	 * ID
	 */
	public static final String ID = "org.eclipse.actf.visualization.VisualizationStatusLineContributionItem"; //$NON-NLS-1$

	private final int MSGLABEL_WIDTHHINT = 300;

	CLabel _statusMessageLabel;

	CLabel _infoMessageLabel;

	// TODO move to common

	/**
	 * Constructor of the class
	 * 
	 * @param id
	 *            contribution item ID
	 */
	public VisualizationStatusLineContributionItem(String id) {
		super(ID + id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.ContributionItem#fill(org.eclipse.swt.widgets.Composite)
	 */
	public void fill(Composite parent) {

		Label leftSeparator = new Label(parent, SWT.SEPARATOR);

		this._statusMessageLabel = new CLabel(parent, SWT.NONE);

		Label centerSeparator = new Label(parent, SWT.SEPARATOR);

		this._infoMessageLabel = new CLabel(parent, SWT.NONE);

		Label rightSeparator = new Label(parent, SWT.SEPARATOR);

		StatusLineLayoutData msgLabelLayoutData = new StatusLineLayoutData();
		msgLabelLayoutData.widthHint = MSGLABEL_WIDTHHINT;
		this._statusMessageLabel.setLayoutData(msgLabelLayoutData);
		msgLabelLayoutData = new StatusLineLayoutData();
		msgLabelLayoutData.widthHint = MSGLABEL_WIDTHHINT + 80;
		this._infoMessageLabel.setLayoutData(msgLabelLayoutData);

		GC gc = new GC(parent);
		gc.setFont(parent.getFont());
		FontMetrics fm = gc.getFontMetrics();
		int heightHint = fm.getHeight();
		gc.dispose();

		StatusLineLayoutData separatorLayoutData = new StatusLineLayoutData();
		separatorLayoutData.heightHint = heightHint;

		leftSeparator.setLayoutData(separatorLayoutData);
		centerSeparator.setLayoutData(separatorLayoutData);
		rightSeparator.setLayoutData(separatorLayoutData);
	}

	/**
	 * Set status message to status line
	 * 
	 * @param statusMessage
	 *            status message
	 */
	public void setStatusMessage(String statusMessage) {
		if (null != this._statusMessageLabel
				&& !this._statusMessageLabel.isDisposed()) {
			this._statusMessageLabel.setText(statusMessage);
			this._statusMessageLabel.update();
		}
	}

	/**
	 * Set information message to status line
	 * 
	 * @param infoMessage
	 *            information message
	 */
	public void setInfoMessage(String infoMessage) {
		if (null != this._infoMessageLabel
				&& !this._infoMessageLabel.isDisposed()) {
			this._infoMessageLabel.setText(infoMessage);
			this._infoMessageLabel.update();
		}
	}
}
