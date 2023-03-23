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
package org.eclipse.actf.visualization.internal.ui.report.srcviewer;

import org.eclipse.actf.ui.util.IDialogConstants;
import org.eclipse.actf.visualization.internal.ui.report.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class DialogTextSearch {
    private Shell shell;

    private SrcViewer viewer;

    private StyledText styledText;

    private Button searchButton;

    //TODO replace
    
    public DialogTextSearch(Shell _shell, SrcViewer _viewer) {
        shell = _shell;
        shell.setText(Messages.DialogTextSearch_0); 
        viewer = _viewer;
        styledText = viewer.getStyledText();
        createSettingControls();
    }

    private void createSettingControls() {
        Composite composite = new Composite(shell, SWT.NULL);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        composite.setLayout(gridLayout);

        Composite leftComp = new Composite(composite, SWT.NULL);
        leftComp.setLayout(new GridLayout());

        Composite strComp = new Composite(leftComp, SWT.NULL);
        gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        strComp.setLayout(gridLayout);
        strComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Label label = new Label(strComp, SWT.NONE);
        label.setText(Messages.DialogTextSearch_1); 

        final Text text = new Text(strComp, SWT.BORDER);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.widthHint = 250;
        text.setLayoutData(gridData);
        text.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent arg0) {
                if (text.getText().equals("")) { //$NON-NLS-1$
                    searchButton.setEnabled(false);
                } else {
                    searchButton.setEnabled(true);
                }

            }
        });

        Composite optionComp = new Composite(leftComp, SWT.NULL);
        gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        optionComp.setLayout(gridLayout);

        final Button disLetterSize = new Button(optionComp, SWT.CHECK);
        disLetterSize.setText(Messages.DialogTextSearch_3); 

        Group group = new Group(optionComp, SWT.NONE);
        group.setText(Messages.DialogTextSearch_4); 
        gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        group.setLayout(gridLayout);

        Button upButton = new Button(group, SWT.RADIO);
        upButton.setText(Messages.DialogTextSearch_5); 
        final Button downButton = new Button(group, SWT.RADIO);
        downButton.setText(Messages.DialogTextSearch_6); 
        downButton.setSelection(true);

        Composite rightComp = new Composite(composite, SWT.NULL);
        rightComp.setLayout(new GridLayout());
        rightComp.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

        searchButton = new Button(rightComp, SWT.PUSH);
        searchButton.setText(Messages.DialogTextSearch_7); 
        searchButton.setEnabled(false);
        gridData = new GridData();
        gridData.widthHint = 100;
        searchButton.setLayoutData(gridData);
        shell.setDefaultButton(searchButton);
        searchButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent arg0) {
                String str = text.getText();
                int pos;
                String wholeText = styledText.getText();
                int findedPos;
                if (downButton.getSelection()) {
                    pos = styledText.getSelectionRange().x + styledText.getSelectionRange().y;
                    if (disLetterSize.getSelection()) {
                        findedPos = wholeText.indexOf(str, pos);
                        if (findedPos == -1 && pos > 0) {
                            if (SWT.CANCEL == popupMessage(Messages.DialogTextSearch_8, 
                                    SWT.OK | SWT.CANCEL)) {
                                return;
                            }
                            findedPos = wholeText.indexOf(str, 0);
                        }
                    } else {
                        findedPos = wholeText.toLowerCase().indexOf(str.toLowerCase(), pos);
                        if (findedPos == -1 && pos > 0) {
                            if (SWT.CANCEL == popupMessage(Messages.DialogTextSearch_8, 
                                    SWT.OK | SWT.CANCEL)) {
                                return;
                            }
                            findedPos = wholeText.toLowerCase().indexOf(str.toLowerCase(), 0);
                        }
                    }

                } else {
                    pos = styledText.getSelectionRange().x;
                    if (disLetterSize.getSelection()) {
                        findedPos = wholeText.lastIndexOf(str, pos - 1);
                        if (findedPos == -1 && pos < wholeText.length()) {
                            if (SWT.CANCEL == popupMessage(Messages.DialogTextSearch_10, 
                                    SWT.OK | SWT.CANCEL)) {
                                return;
                            }
                            findedPos = wholeText.lastIndexOf(str, wholeText.length());
                        }
                    } else {
                        findedPos = wholeText.toLowerCase().lastIndexOf(str.toLowerCase(), pos - 1);
                        if (findedPos == -1 && pos < wholeText.length()) {
                            if (SWT.CANCEL == popupMessage(Messages.DialogTextSearch_10, 
                                    SWT.OK | SWT.CANCEL)) {
                                return;
                            }
                            findedPos = wholeText.toLowerCase().lastIndexOf(str.toLowerCase(), wholeText.length());
                        }
                    }
                }

                if (findedPos == -1) {
                    popupMessage(Messages.DialogTextSearch_12, SWT.OK); 
                } else {
                    viewer.selectByOffset(findedPos, str.length());
                }

            }
        });

        Button cancelButton = new Button(rightComp, SWT.PUSH);
        cancelButton.setText(IDialogConstants.CANCEL);
        gridData = new GridData();
        gridData.widthHint = 100;
        cancelButton.setLayoutData(gridData);
        cancelButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent arg0) {
                shell.close();
            }
        });
    }

    private int popupMessage(String msg, int option) {
        MessageBox msgBox = new MessageBox(shell, option);
        msgBox.setMessage(msg);
        return msgBox.open();
    }

}
