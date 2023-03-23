/*******************************************************************************
 * Copyright (c) 2007, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.internal.ui.editors.edge;

import org.eclipse.actf.model.ui.editor.actions.GoBackAction;
import org.eclipse.actf.model.ui.editor.actions.GoForwardAction;
import org.eclipse.actf.model.ui.editor.actions.RefreshAction;
import org.eclipse.actf.model.ui.editor.actions.StopAction;
import org.eclipse.actf.model.ui.editor.browser.IWebBrowserACTF;
import org.eclipse.actf.model.ui.editor.browser.WebBrowserEventUtil;
import org.eclipse.actf.model.ui.util.ModelServiceMessages;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;

public class WebBrowserToolbar extends Composite {

	// TODO move to base plugin

	private Text _addressText;

	private boolean _isFocusOnText;

	private IWebBrowserACTF browser;

	public WebBrowserToolbar(IWebBrowserACTF browser, Composite parent,
			int style) {
		super(parent, style);

		this.browser = browser;

		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginBottom = 1;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.numColumns = 3;
		setLayout(gridLayout);

		initLayout();
	}

	private void initLayout() {
		
		ToolBar toolBarL = new ToolBar(this, SWT.LEFT);
		ToolBarManager toolBarManagerL = new ToolBarManager(toolBarL);
		toolBarManagerL.add(new GoBackAction());
		toolBarManagerL.add(new GoForwardAction());
		toolBarManagerL.add(new StopAction());
		toolBarManagerL.add(new RefreshAction());
		
		toolBarManagerL.update(true);		
		
		/*
		Label addressLabel = new Label(this, SWT.NONE);
		addressLabel.setLayoutData(new GridData());
		addressLabel.setText(" " + ModelServiceMessages.WebBrowser_Address); //$NON-NLS-1$		
		addressLabel.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent arg0) {
				if (arg0.stateMask == SWT.ALT
						&& (arg0.character == 'd' || arg0.character == 'D')) {
					_addressText.setFocus();
					_addressText.selectAll();
				}
			}
		});
		*/

		this._addressText = new Text(this, SWT.SINGLE | SWT.BORDER);
		GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		// gridData.widthHint = 1024;
		_addressText.setLayoutData(gridData);
		this._addressText.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent arg0) {
				_isFocusOnText = false;
				if (browser != null) {
					WebBrowserEventUtil.focusLostOfAddressText(browser);
				}
			}

			public void focusGained(FocusEvent arg0) {
				_addressText.selectAll();
				if (browser != null) {
					WebBrowserEventUtil.focusGainedOfAddressText(browser);
				}
			}
		});
		
		_addressText.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent arg0) {
				if (arg0.stateMask == SWT.ALT
						&& (arg0.character == 'd' || arg0.character == 'D')) {
					_addressText.setFocus();
					_addressText.selectAll();
				}
			}
		});


		this._addressText.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent arg0) {
				if (!_isFocusOnText) {
					_addressText.selectAll();
					_isFocusOnText = true;
				}
			}

			public void mouseDoubleClick(MouseEvent e) {
				_addressText.selectAll();
			}
		});

		// TODO use action

		this._addressText.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if (e.character == SWT.CR) {
					if (browser != null) {
						browser.open(_addressText.getText());
						// TODO: Remaining the focus on the address field causes
						// navigation problem on aiBrowser
						// As a makeshift, we move the focus to the toolbar
						// parent but we should reconsider
						// this treatment.
						WebBrowserToolbar.this.forceFocus();
					}
				}
			}

			public void keyReleased(KeyEvent e) {
			}
		});

		ToolBar toolBar = new ToolBar(this, SWT.RIGHT);
		ToolBarManager toolBarManager = new ToolBarManager(toolBar);

		Action navigateAction = new Action(ModelServiceMessages.WebBrowser_Go,
				BrowserEdge_Plugin.imageDescriptorFromPlugin(
						"org.eclipse.actf.model.ui", "icons/browser/go.png")) {//$NON-NLS-1$ //$NON-NLS-2$
			public void run() {
				if (browser != null && _addressText != null) {
					browser.open(_addressText.getText());
				}
			}
		};
		navigateAction.setToolTipText(ModelServiceMessages.WebBrowser_Go_tp);

		ActionContributionItem navigateAction2 = new ActionContributionItem(
				navigateAction);
		navigateAction2.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		toolBarManager.add(navigateAction2);

		toolBarManager.update(true);

		// _compositeParent.getShell().setDefaultButton(searchButton);
	}

	public String getAddressTextString() {
		return _addressText.getText();
	}

	public void setAddressTextString(String targetS) {
		_addressText.setText(targetS);
	}

	public void setFocusToAddressText(boolean selectAll) {
		if (!isVisible())
			setVisible(true);
		_addressText.setFocus();
		if (selectAll) {
			_addressText.selectAll();
		} else {
			_addressText.setSelection(0);
		}
	}

	public void showAddressText(boolean flag) {
		this.setVisible(flag);
	}

}
