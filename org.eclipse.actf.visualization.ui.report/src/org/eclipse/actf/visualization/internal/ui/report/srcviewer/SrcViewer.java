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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import org.eclipse.actf.util.JapaneseEncodingDetector;
import org.eclipse.actf.visualization.internal.ui.report.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class SrcViewer {

    private Composite parentCom;

    private Display display;

    private StyledText styledText, lineText;

    private Text lineNo, columnNo;

    int start, end;

    Color hlFg, hlBg;

    Vector<Integer> startV, lenV;
    Vector<Color> fgV, bgV;

    private static Shell searchDlgShell;

    private String highLightedText = ""; //$NON-NLS-1$

    private boolean highlighted = false;

    private Button copyButton;

    /**
     *  
     */
    public SrcViewer(Composite parent) {
        
        //TODO very old implimentation, use JFace SourceViewer
        
        parentCom = parent;
        display = parent.getDisplay();

        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        composite.setLayout(gridLayout);


        Composite composite2 = new Composite(composite, SWT.NONE);
        gridLayout = new GridLayout();
        gridLayout.numColumns = 6;
        gridLayout.marginHeight = gridLayout.marginWidth = 0;
        gridLayout.horizontalSpacing = gridLayout.verticalSpacing = 0;
        composite2.setLayout(gridLayout);

        GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        composite2.setLayoutData(gridData);

        Label label = new Label(composite2, SWT.NONE);
        label.setText(Messages.SrcViewer_3); 
        gridData = new GridData();
        gridData.horizontalIndent = 5;
        label.setLayoutData(gridData);

        lineNo = new Text(composite2, SWT.BORDER | SWT.RIGHT);
        gridData = new GridData();
        gridData.widthHint = 70;
        lineNo.setLayoutData(gridData);
        lineNo.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent arg0) {
                if (arg0.stateMask == SWT.CTRL && (arg0.keyCode == 'f' || arg0.keyCode == 'F')) {
                    openSearchDialog();
                }
            }

            public void keyReleased(KeyEvent arg0) {
                if (arg0.stateMask != SWT.NONE) {
                    return;
                }
                try {
                    clearHighlight(false);
                    int line = Integer.parseInt(lineNo.getText());
                    int lineCnt = styledText.getLineCount();
                    if (line < 1) {
                        line = 1;
                        lineNo.setText(String.valueOf(line));
                    } else if (line > lineCnt) {
                        line = lineCnt;
                        lineNo.setText(String.valueOf(line));
                    }
                    columnNo.setText("0"); //$NON-NLS-1$
                    highlight(line, 0, line + 1, 0, false);

                } catch (NumberFormatException e) {
                    lineNo.setText(""); //$NON-NLS-1$
                }

            }
        });

        label = new Label(composite2, SWT.NONE);
        label.setText(Messages.SrcViewer_6); 
        gridData = new GridData();
        gridData.horizontalIndent = 10;
        label.setLayoutData(gridData);

        columnNo = new Text(composite2, SWT.BORDER | SWT.RIGHT);
        gridData = new GridData();
        gridData.widthHint = 70;
        columnNo.setLayoutData(gridData);
        columnNo.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent arg0) {
                if (arg0.stateMask == SWT.CTRL && (arg0.keyCode == 'f' || arg0.keyCode == 'F')) {
                    openSearchDialog();
                }
            }

            public void keyReleased(KeyEvent arg0) {
                if (arg0.stateMask != SWT.NONE) {
                    return;
                }

                try {
                    clearHighlight(false);
                    int line = Integer.parseInt(lineNo.getText());
                    int colCnt;
                    if (line < styledText.getLineCount()) {
                        colCnt = styledText.getOffsetAtLine(line) - styledText.getOffsetAtLine(line - 1) - 1;
                    } else {
                        colCnt = styledText.getCharCount() - styledText.getOffsetAtLine(line - 1);

                    }
                    int column = Integer.parseInt(columnNo.getText());
                    if (column < 0) {
                        column = 0;
                        columnNo.setText(String.valueOf(column));
                    } else if (column >= colCnt) {
                        column = colCnt;
                        columnNo.setText(String.valueOf(column));
                    }
                    highlight(line, column, line + 1, 0, false);

                } catch (NumberFormatException e) {
                    columnNo.setText(""); //$NON-NLS-1$
                }

            }
        });

        Button searchButton = new Button(composite2, SWT.PUSH);
        searchButton.setText(Messages.SrcViewer_8); 
        gridData = new GridData();
        gridData.horizontalIndent = 20;
        searchButton.setLayoutData(gridData);
        searchButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent arg0) {
                openSearchDialog();
            }
        });

        copyButton = new Button(composite2, SWT.PUSH);
        copyButton.setText(Messages.SrcViewer_9); 
        gridData = new GridData();
        gridData.horizontalIndent = 10;
        copyButton.setLayoutData(gridData);
        copyButton.setEnabled(false);
        copyButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent arg0) {
                copyToClipBoard();
            }
        });

        Composite composite3 = new Composite(composite, SWT.NONE);
        gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        composite3.setLayout(gridLayout);

        gridData = new GridData(GridData.FILL_BOTH);
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessVerticalSpace = true;
        composite3.setLayoutData(gridData);

        lineText = new StyledText(composite3, SWT.NONE);
        gridData = new GridData(GridData.VERTICAL_ALIGN_FILL);
        gridData.widthHint = 32;
        //gridData.horizontalAlignment=GridData.FILL;
        gridData.grabExcessVerticalSpace = true;
        lineText.setLayoutData(gridData);
        lineText.setEditable(false);

        styledText = new StyledText(composite3, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        gridData = new GridData(GridData.FILL_BOTH);
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessVerticalSpace = true;
        styledText.setLayoutData(gridData);

        styledText.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent arg0) {
                getSelectionLineCol();
                setCopyButton();
            }
        });

        styledText.addMouseListener(new MouseAdapter() {
            public void mouseDown(MouseEvent arg0) {
                getSelectionLineCol();
            }
        });

        styledText.setKeyBinding('C' | SWT.MOD1, SWT.NULL);
        styledText.setKeyBinding('X' | SWT.MOD1, SWT.NULL);

        styledText.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent arg0) {
                if (arg0.keyCode == SWT.PAGE_DOWN || arg0.keyCode == SWT.PAGE_UP || arg0.keyCode == SWT.HOME
                        || arg0.keyCode == SWT.END || arg0.keyCode == SWT.ARROW_DOWN || arg0.keyCode == SWT.ARROW_UP
                        || arg0.keyCode == SWT.ARROW_LEFT || arg0.keyCode == SWT.ARROW_RIGHT) {
                    getSelectionLineCol();
                    setLineText();
                } else if (arg0.stateMask == SWT.CTRL) {
                    if (arg0.keyCode == 'f' || arg0.keyCode == 'F') {
                        openSearchDialog();
                    } else if (arg0.keyCode == 'c' || arg0.keyCode == 'C') {
                        copyToClipBoard();
                    }
                }
            }
        });

        styledText.getVerticalBar().addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent arg0) {
                setLineText();
            }
        });

        parentCom.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent arg0) {
                if (arg0.stateMask == SWT.CTRL) {
                    if (arg0.keyCode == 'f' || arg0.keyCode == 'F') {
                        openSearchDialog();
                    } else if (arg0.keyCode == 'c' || arg0.keyCode == 'C') {
                        copyToClipBoard();
                    }
                }
            }
        });

        hlFg = styledText.getSelectionForeground();
        hlBg = styledText.getSelectionBackground();

        startV = new Vector<Integer>();
        lenV = new Vector<Integer>();
        fgV = new Vector<Color>();
        bgV = new Vector<Color>();
    }

    private void setCopyButton() {
        String selText = styledText.getSelectionText();
        if (selText.equals("") && highLightedText.equals("") //$NON-NLS-1$ //$NON-NLS-2$
                || !selText.equals("") && highlighted) { //$NON-NLS-1$
            copyButton.setEnabled(false);
        } else {
            copyButton.setEnabled(true);
        }
    }

    private void copyToClipBoard() {
        if (!copyButton.getEnabled()) {
            return;
        }

        String strText;
        if (!highLightedText.equals("")) { //$NON-NLS-1$
            strText = highLightedText;
        } else {
            strText = styledText.getSelectionText();
        }

        Clipboard clipboard = new Clipboard(display);
        clipboard.setContents(new Object[] { strText }, new Transfer[] { TextTransfer.getInstance() });
    }

    private void openSearchDialog() {
        if (searchDlgShell == null || searchDlgShell.isDisposed()) {
            searchDlgShell = new Shell(parentCom.getShell(), SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
            searchDlgShell.setSize(600, 170);
            searchDlgShell.setLayout(new FillLayout());
            new DialogTextSearch(searchDlgShell, this);
            searchDlgShell.open();
            
        } else {
            searchDlgShell.forceActive();
        }
    }

    private void setLineText() {
        StringBuffer strBuf = new StringBuffer();
        int len = lineText.getBounds().height / styledText.getLineHeight();
        int top = styledText.getTopIndex();
        for (int i = 0; i < len; i++) {
            strBuf.append(String.valueOf(top + i + 1) + "\r\n"); //$NON-NLS-1$
        }
        lineText.setText(strBuf.toString());
        styledText.setTopIndex(top);
    }

    public void setHighlightColor(Color hlFg, Color hlBg) {
        this.hlFg = hlFg;
        this.hlBg = hlBg;
    }

    public void getSelectionLineCol() {
        int pos = styledText.getSelectionRange().x;
        int line = styledText.getLineAtOffset(pos) + 1;// 1 base
        lineNo.setText(new Integer(line).toString());
        columnNo.setText(new Integer(pos - styledText.getOffsetAtLine(line - 1)).toString());

    }

    public void openFile(File target) {
        styledText.setText(""); //$NON-NLS-1$
        //TODO check
        try {
            String encoding = "MS932"; //$NON-NLS-1$

            JapaneseEncodingDetector JED = null;

            InputStreamReader isr = null;
            InputStream is = new FileInputStream(target);
            try {
                JED = new JapaneseEncodingDetector(is);
                encoding = JED.detect();
                isr = new InputStreamReader(JED.getInputStream(), encoding);
            } catch (Exception e2) {
                //e2.printStackTrace();
            }

            BufferedReader br = null;
            if (isr != null) {
                br = new BufferedReader(isr);
            } else {
                br = new BufferedReader(new FileReader(target));
            }
            String line = new String();
            try {
                while ((line = br.readLine()) != null) {
                    styledText.append(line + "\n"); //$NON-NLS-1$
                }
            } catch (Exception e) {
                try {
                    isr = new InputStreamReader(JED.getInputStream(), "MS932"); //$NON-NLS-1$
                    styledText.setText(""); //$NON-NLS-1$
                    br = new BufferedReader(isr);
                    while ((line = br.readLine()) != null) {
                        styledText.append(line + "\n"); //$NON-NLS-1$
                    }
                } catch (Exception e2) {
                    styledText.setText(Messages.SrcViewer_0 + target.getName()); 
                }
            }
            is.close();
            br.close();
            isr.close();
        } catch (Exception e) {
            //e.printStackTrace();
        }
        
        setLineText();
        initializeColor();
    }

    public void setText(String target) {
        styledText.setText(target);
        setLineText();
        initializeColor();
    }

    private void initializeColor() {
        startV.clear();
        lenV.clear();
        fgV.clear();
        bgV.clear();

        lineNo.setText(""); //$NON-NLS-1$
        columnNo.setText(""); //$NON-NLS-1$
    }

    public void selectByOffset(int start, int len) {
        clearHighlight();
        styledText.setSelection(start, start + len);
        getSelectionLineCol();
        setLineText();
    }

    private void setStyleByOffset(int start, int len, Color fgcolor, Color bgcolor) {
        StyleRange styleRange = new StyleRange();
        styleRange.start = start;
        styleRange.length = len;
        styleRange.foreground = fgcolor;
        styleRange.background = bgcolor;
        styledText.setStyleRange(styleRange);
    }

    public void clearColor() {
        styledText.setStyleRange(null);
        initializeColor();
    }

    public void setColor(int startLine, int startPos, int endLine, int endPos, Color fgcolor, Color bgcolor) {
        calculateStartEnd(startLine, startPos, endLine, endPos);
        setStyleByOffset(start, end - start, fgcolor, bgcolor);
        startV.add(new Integer(start));
        lenV.add(new Integer(end - start));
        fgV.add(fgcolor);
        bgV.add(bgcolor);
    }

    public void clearHighlight() {
        clearHighlight(true);
        highlighted = false;
        highLightedText = ""; //$NON-NLS-1$
        setCopyButton();
    }

    private void clearHighlight(boolean bUpdate) {

        styledText.setStyleRange(null);
        for (int i = 0; i < startV.size(); i++) {
            StyleRange styleRange = new StyleRange();
            styleRange.start = (startV.get(i)).intValue();
            styleRange.length = (lenV.get(i)).intValue();
            styleRange.foreground = fgV.get(i);
            styleRange.background = bgV.get(i);
            styledText.setStyleRange(styleRange);
        }

        if (bUpdate) {
            lineNo.setText(""); //$NON-NLS-1$
            columnNo.setText(""); //$NON-NLS-1$
        }

    }

    public void highlight(int startLine, int startPos, int endLine, int endPos) {
        highlight(startLine, startPos, endLine, endPos, true);
    }

    private void highlight(int startLine, int startPos, int endLine, int endPos, boolean bUpdate) {
        //Color oldFg=styledText.getSelectionForeground();
        //Color oldBg=styledText.getSelectionBackground();

        calculateStartEnd(startLine, startPos, endLine, endPos);

        //styledText.setSelectionForeground(new Color(null, 255, 255, 255));
        //styledText.setSelectionBackground(new Color(null, 0, 0, 0));

        styledText.setSelection(styledText.getText().length());
        styledText.setSelection(start);
        setLineText();

        //styledText.setSelectionRange(start, end - start);
        setStyleByOffset(start, end - start, hlFg, hlBg);
        if (end > start) {
            if (highlighted) {
                highLightedText = ""; //$NON-NLS-1$
            } else {
                highLightedText = styledText.getText(start, end);
                highlighted = true;
            }
            setCopyButton();
        }

        //startV.add(new Integer(start));
        //lenV.add(new Integer(end - start));

        if (bUpdate) {
            lineNo.setText(new Integer(startLine).toString());
            columnNo.setText(new Integer(startPos).toString());
        }
        //styledText.setSelectionForeground(oldFg);
        //styledText.setSelectionBackground(oldBg);
    }

    public void highlightLine(int line) {
        highlightLines(line, line);
    }

    public void highlightLines(int start, int end) {
        highlight(start, 0, end + 1, 0);
    }

    private void calculateStartEnd(int startLine, int startPos, int endLine, int endPos) {
        if (startPos < 0) {
            startPos = 0;
        }
        if (endPos < 0) {
            endPos = 0;
        }

        try {
            start = styledText.getOffsetAtLine(startLine - 1) + startPos;
        } catch (Exception e) {
            start = styledText.getCharCount();
        }
        try {
            end = styledText.getOffsetAtLine(endLine - 1) + endPos;
        } catch (Exception e1) {
            end = styledText.getCharCount();
        }
        if (start > end) {
            start = end;
        }
    }

    public StyledText getStyledText() {
        return styledText;
    }

    public void closeSearchDlgShell() {
        if (searchDlgShell != null && !searchDlgShell.isDisposed()) {
            searchDlgShell.dispose();
        }
    }
}
