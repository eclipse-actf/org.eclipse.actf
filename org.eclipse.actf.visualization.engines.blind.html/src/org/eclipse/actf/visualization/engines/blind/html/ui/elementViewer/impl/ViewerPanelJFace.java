/*******************************************************************************
 * Copyright (c) 2004, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer.impl;

import java.util.List;

import org.eclipse.actf.model.ui.IModelService;
import org.eclipse.actf.model.ui.editor.browser.IWebBrowserACTF;
import org.eclipse.actf.model.ui.util.ModelServiceUtils;
import org.eclipse.actf.visualization.engines.blind.BlindVizResourceUtil;
import org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer.IHighlightElementListener;
import org.eclipse.actf.visualization.internal.engines.blind.html.util.VisualizationAttributeInfo;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class ViewerPanelJFace implements IViewerPanel {
	private static final Image ICON_EXCLA = BlindVizResourceUtil.getImageDescriptor(
			"icons/excla_squ.png").createImage();

	private Shell shell;

	private List<VisualizationAttributeInfo> idList;

	private IHighlightElementListener prb;

	private ElementViewerJFace idViewer;

	private ElementViewerJFace akeyViewer;

	private ElementViewerJFace classViewer;

	private CSSViewer cssViewer;

	private boolean isDisposed = false;

	public ViewerPanelJFace(Shell _shell, VisualizeStyleInfo styleInfo,
			IHighlightElementListener _prb) {

		if (styleInfo == null) {
			styleInfo = new VisualizeStyleInfo();
			idList = null;
		} else {
			idList = styleInfo.getOrigIdList();
		}

		prb = _prb;
		shell = new Shell(_shell, SWT.MIN | SWT.MAX | SWT.RESIZE | SWT.CLOSE);
		shell.setLayout(new GridLayout());

		shell.setText(TITLE_NAME);
		shell.setImage(ICON_EXCLA); //$NON-NLS-1$

		shell.setLocation(100, 100);

		Composite composite = new Composite(shell, SWT.NULL);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 1;
		composite.setLayout(gridLayout1);

		TabFolder viewerFolder = new TabFolder(composite, SWT.NULL);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		// gridData.heightHint = 300;
		viewerFolder.setLayoutData(gridData);

		TabItem tabItemEle = new TabItem(viewerFolder, SWT.NULL);
		tabItemEle.setText(ID_TAB_TITLE);
		idViewer = new ElementViewerJFace(viewerFolder, prb);

		idViewer.setCategoryColWidth(100);
		idViewer.setDescColWidth(0);
		idViewer.setContentProvider(new ArrayContentProvider());
		idViewer.setLabelProvider(new ElementLabelAndColorProvider());
		idViewer.setElementList(idList);
		tabItemEle.setControl(idViewer.getComposite());

		TabItem tabItemAKey = new TabItem(viewerFolder, SWT.NULL);
		tabItemAKey.setText(ACCESSKEY_TAB_TITLE);
		akeyViewer = new ElementViewerJFace(viewerFolder, prb);
		akeyViewer.setCategoryColWidth(0);
		akeyViewer.setDescColWidth(150);
		akeyViewer.setContentProvider(new ArrayContentProvider());
		akeyViewer.setLabelProvider(new ElementLabelAndColorProvider());
		akeyViewer.setElementList(styleInfo.getAccesskeyList());
		tabItemAKey.setControl(akeyViewer.getComposite());

		TabItem tabItemClass = new TabItem(viewerFolder, SWT.NULL);
		tabItemClass.setText(CLASS_TAB_TITLE);
		classViewer = new ElementViewerJFace(viewerFolder, prb);
		classViewer.setCategoryColWidth(0);
		classViewer.setDescColWidth(0);
		classViewer.setContentProvider(new ArrayContentProvider());
		classViewer.setLabelProvider(new ElementLabelAndColorProvider());
		classViewer.setElementList(styleInfo.getClassList());
		tabItemClass.setControl(classViewer.getComposite());

		TabItem tabItemCss = new TabItem(viewerFolder, SWT.NULL);
		tabItemCss.setText(CSS_TAB_TITLE);
		cssViewer = new CSSViewer(viewerFolder);
		cssViewer.setCssSet(styleInfo.getImportedCssSet());
		tabItemCss.setControl(cssViewer.getCompositeCss());

		viewerFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (idList != null) {
					recoveryHighlight();
				}
			}
		});

		Button closeButton = new Button(composite, SWT.NULL);
		closeButton.setText(CLOSE_BUTTON);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		closeButton.setLayoutData(gridData);
		closeButton.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				shell.dispose();
			}
		});

		// TODO move to manager?
		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent arg0) {
				if (null != idList) {
					recoveryHighlight();
				}
				// prb.setViewerPanel(null);
				isDisposed = true;
			}
		});

		shell.setSize(450, 420);

		shell.open();

	}

	private void recoveryHighlight() {
		IModelService dataSource = ModelServiceUtils.getActiveModelService();

		if (dataSource != null && dataSource instanceof IWebBrowserACTF) {
			((IWebBrowserACTF) dataSource).clearHighlight();
		}
		prb.clearHighlight();
	}

	public void asyncUpdateValue(VisualizeStyleInfo styleInfo) {
		final VisualizeStyleInfo _styleInfo = styleInfo;
		shell.getDisplay().asyncExec(new Thread() {
			public void run() {
				idList = _styleInfo.getOrigIdList();
				idViewer.setElementList(idList);
				akeyViewer.setElementList(_styleInfo.getAccesskeyList());
				classViewer.setElementList(_styleInfo.getClassList());
				cssViewer.setCssSet(_styleInfo.getImportedCssSet());
			}
		});
	}

	public void forceActive() {
		shell.setMinimized(false);
		shell.setVisible(true);
		shell.forceActive();
	}

	public void hide() {
		shell.setVisible(false);
	}

	public boolean isDisposed() {
		return isDisposed;
	}

}
